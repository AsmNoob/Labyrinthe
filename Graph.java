
import java.util.*; // a vérifier

//Constructeur de la class Node, il faut sa position dans la matrice, et quatre donnée correspondant a UP,DOWN,LEFT,RIGHT initialisé a null dont les valeurs changeront lors de la création de celle ci, permettant de savoir plus tard dans quel direction aller directement. Chaque direction sera un pointeur vers le sommet qui est au bout de son chemin. Puisque que nos sommet sont principalement des intersections cela nous permettera de choisir directement le bon chemin .

public class Graph {
	//Attribut
	private int dim; //dimention de la matrice
	private ArrayList<Integer> list_posNode = new ArrayList<Integer>();
	private ArrayList<Node> list_node = new ArrayList<Node>();
	private boolean new_arc = true;
	private Node preNode = null;
	private int[] DIRECTION = new int[] {-1,1,0,0};
	private int DIRECTION_SIZE = DIRECTION.length-1;

	// constructeur
	public Graph(int[][] mat, int[] pacmanCoord){
		dim = mat.length;
		int pos_crypt = pos_cryptage(pacmanCoord[0],pacmanCoord[1]);
		create_graph(mat,pacmanCoord[0],pacmanCoord[1], pacmanCoord[0],pacmanCoord[1], true, null);
		print_graph();
		System.out.println(list_posNode.size());
	}

	public Node create_node(int pos_crypt){
		Node current_node = new Node(pos_crypt);
		list_node.add(current_node);list_posNode.add(pos_crypt);
		return current_node;
	}
	// selectionne la noeud courrant -déjà existant ou non-.
	public Node select_currentNode(int pos_crypt){
		Node current_node = null;
		if (!list_posNode.contains(pos_crypt)) {
			current_node = create_node(pos_crypt);
		}
		else{
			int index = list_posNode.indexOf(pos_crypt);
			current_node = list_node.get(index);
		}
		return current_node;
	}

	public Arc modif_currentArc(Arc current_arc, Node current_node, int pos_crypt){
		if (current_node != null && current_arc != null){
			end_Arc(current_arc,current_node,pos_crypt);
			update_nodeLink(current_arc,current_node);

			current_arc = start_Arc(pos_crypt,current_node);
		}
		else if (current_arc == null) {
			current_arc = start_Arc(pos_crypt,current_node);		
		}
		else{current_arc.add_way(pos_crypt);}
		return current_arc;
	}
	public void update_nodeLink(Arc current_arc,Node current_node){
		Node pre_node = current_arc.get_startNode();
		pre_node.add_link(current_node,current_arc);
		current_node.add_link(pre_node,current_arc);
	}
	public Arc start_Arc(int pos_crypt, Node start_node){
		Arc current_arc = new Arc(pos_crypt);
		current_arc.set_startNode(start_node);
		return current_arc;
	}

	public void end_Arc(Arc current_arc, Node current_node, int pos_crypt){
		current_arc.add_way(pos_crypt);
		current_arc.set_endNode(current_node);
		//System.out.println(pos_crypt); current_arc.print_arc();

	}
	// test si la postion suivante est dans les bornes et autre qu'un mur.
	public int test_nextPosition(int[][] mat, int i, int j, int preLine, int preColumn, int line_add, int column_add){
				
		if (( ((i+line_add) >= 0) && ((i+line_add) < dim) && ((j+column_add)>= 0) && ((j+column_add) < dim) &&
		   ((i+line_add) != preLine || (j+column_add) != preColumn))
		    || (preLine == i  && preColumn == j)) {
			int elem = mat[i+line_add][j+column_add];
			// mur = -1
			if (elem != -1) {return 1;}
		}
		return 0;
	}

	

	//affichage des données récuperer après analyse du labyrinthe. 
	public void print_graph(){
		System.out.println(list_posNode);
		int size_dict = list_posNode.size();
		for (int i = 0; i < size_dict; i++ ) {
			list_node.get(i).print();
		}
	}
	// cryptage de la position i,j
	public int pos_cryptage(int i, int j){
		int pos = 10000+(i*100)+j;
		return pos;
	}

	// decryptage de la position 
	public int pos_decryptage( int pos){
		int j = pos %100;
		int i = (pos-10000-(pos%100))/100;

		return i; 
	}
	// detect si la position actuelle est une node et renvois une liste d'entier contenant les directions possible suivante.
	public int[] detect_isNode(int[][] mat, int actuLine, int actuColumn, int preLine, int preColumn){
		int j = DIRECTION_SIZE;
		int[] data_direction = new int[5];// 4 premier entier -0,1- correspond au direction possible ou non le 5eme si il est >1 nous indiques que c'est une node.
		System.out.print("Detect_isNode || ");
		for (int i = 0; i<= DIRECTION_SIZE; i++){
			int test = test_nextPosition(mat, actuLine, actuColumn, preLine, preColumn,DIRECTION[i],DIRECTION[j]);

			data_direction[i] = test;
			data_direction[DIRECTION_SIZE+1]+=test;
			j--;
		}
		System.out.println(Arrays.toString(data_direction));
		return data_direction;
	}

	// parcours en backtraking du labyrinthe créant a chaque intersection de chemin une node - un sommet-.  
	public void create_graph(int[][] mat, int actuLine, int actuColumn, int preLine, int preColumn, boolean isNode,Arc current_arc){
		//actuLine,j position actuel, preLine,preColumn position precedente
		System.out.print("Controle_Create || actuPos --> ");
		System.out.print(actuLine);System.out.print(actuColumn);
		System.out.print(" || prePos --> ");
		System.out.print(preLine);System.out.print(preColumn);

		Node current_node = null;
		int pos_crypt = pos_cryptage(preLine,preColumn);
		int actu_posCrypt= pos_cryptage(actuLine,actuColumn);

		if (!list_posNode.contains(actu_posCrypt)){
			System.out.print(" || isNode --> ");
			System.out.println(isNode);
			int[] data_direction = detect_isNode(mat,actuLine,actuColumn,preLine,preColumn);

			if (data_direction[DIRECTION_SIZE+1]>1 || mat[actuLine][actuColumn]>0)  { 
				current_node = select_currentNode(actu_posCrypt);
				isNode = true;
			}
			else {isNode = false;}

			current_arc = modif_currentArc(current_arc,current_node,actu_posCrypt);

			for (int i = 0; i <= DIRECTION_SIZE; i++) {
				if (data_direction[i] == 1) {
					int newLine = actuLine+DIRECTION[i]; int newColumn = actuColumn+DIRECTION[DIRECTION_SIZE-i];
					create_graph(mat,newLine,newColumn,actuLine,actuColumn,isNode,current_arc);
				}	
			}
		}
		
	}


}















