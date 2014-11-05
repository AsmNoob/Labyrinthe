
import java.util.*; // a vérifier

//Constructeur de la class Node, il faut sa position dans la matrice, et quatre donnée correspondant a UP,DOWN,LEFT,RIGHT initialisé a null dont les valeurs changeront lors de la création de celle ci, permettant de savoir plus tard dans quel direction aller directement. Chaque direction sera un pointeur vers le sommet qui est au bout de son chemin. Puisque que nos sommet sont principalement des intersections cela nous permettera de choisir directement le bon chemin .

public class Graph {
	//Attribut
	private int dim; //dimention de la matrice
	private HashMap<Integer,Node> ens_node = new HashMap<Integer,Node>();// dictionnaire comprenant en key la position cryptée et en valeur la node a cette position.
	private boolean new_arc = true;

	// constructeur
	public Graph(int[][] mat, int[] pacmanCoord){
		dim = mat.length;
		System.out.println(dim);
		create_graph(mat,pacmanCoord[0],pacmanCoord[1], pacmanCoord[0],pacmanCoord[1], null);
		print_graph();
	}

	// test si la postion suivante est dans les bornes et autre qu'un mur.
	public boolean test_nextPosition(int[][] mat, int i, int j, int preLine, int preColumn, int line_add, int column_add){
		System.out.print("Controle_test: ");
		/*System.out.print((boolean)((i+line_add) >= 0));System.out.print((boolean)((i+line_add) < dim));
		System.out.print((boolean)((j+column_add)>= 0));System.out.print((boolean)((j+column_add) < dim));
		System.out.print((boolean)((i+line_add) != preLine));System.out.println((boolean)((j+column_add) != preColumn));
		System.out.print(" Data: ");
		*/
		System.out.print(i);System.out.print(j);System.out.print(line_add);System.out.print(column_add);System.out.print(preLine);System.out.println(preColumn);
		
		if (( ((i+line_add) >= 0) && ((i+line_add) < dim) && ((j+column_add)>= 0) && ((j+column_add) < dim) &&
		   ((i+line_add) != preLine || (j+column_add) != preColumn))
		    || (preLine == i  && preColumn == j)) {
			int elem = mat[i+line_add][j+column_add];
			// mur = -1
			if (elem != -1) {return true;}
		}
		return false;
	}

	//crée une nouvelle node si elle nexiste pas encore
	public void checkFor_newNode(int pos_crypt,Arc current_arc){
		System.out.println("Controle_check");
		if (!ens_node.containsKey(pos_crypt)){
			Node node = new Node(pos_crypt);
			if (!ens_node.isEmpty()){
				int pre_pos = current_arc.get(0);
				Node pre_node = ens_node.get(pre_pos);
				pre_node.print_nodePos();
				pre_node.add_link(node,current_arc);
			}
			ens_node.put(pos_crypt,node);
			new_arc = true;
		}
	}


	// vérifie que preColumn'element n'est pas un monstre/pacman/bonus/sortie
	public void check_elemSpecial(int elem, int pos_crypt, Arc current_arc){
		System.out.print("Controle_elem : "); System.out.println(elem);


		if (elem > 0) {checkFor_newNode(pos_crypt, current_arc);}
	}
	//affichage des données récuperer après analyse du labyrinthe. 
	public void print_graph(){
		System.out.println(ens_node.keySet());
		int size_dict = ens_node.size();
	}
	// cryptage de la position i,j
	public int pos_cryptage(int i, int j){
		int pos = 10000+(i*100)+j;
		return pos;
	}

	// decryptage de la position 
	public int pos_decryptage( int pos){
		int j = pos %100;
		int i = (pos-10000-j)/100;

		return i; 
	}
	// parcours en backtraking du labyrinthe créant a chaque intersection de chemin une node - un sommet-.
	public void create_graph(int[][] mat, int i, int j, int preLine, int preColumn, Arc current_arc){
		//i,j position actuel, preLine,preColumn position precedente
		System.out.print("Controle_Create : ");
		System.out.print(i);System.out.println(j);


		int pos_crypt = pos_cryptage(i,j);
		if (!ens_node.containsKey(pos_crypt)){
			boolean multi_direction = false;
			if (new_arc) { 
				// creattio d'un nouvel arc qui commence a la position precedente
				int prePos_crypt = pos_cryptage(preLine,preColumn);
				current_arc = new Arc(prePos_crypt);
				new_arc = false;
			}
			// ajout de la position actuelle a l'arc
			current_arc.add_way(pos_crypt);

			check_elemSpecial(mat[i][j],pos_crypt,current_arc);

			if (test_nextPosition(mat,i,j,preLine,preColumn,-1,0)){//UP

				create_graph(mat,i-1,j,i,j,current_arc);
				multi_direction = true;
			}
			if (test_nextPosition(mat,i,j,preLine,preColumn,1,0)){//DOWN
				if (multi_direction) { checkFor_newNode(pos_crypt,current_arc);}
		 		create_graph(mat,i+1,j,i,j,current_arc);
				multi_direction = true;
			}
			if (test_nextPosition(mat,i,j,preLine,preColumn,0,-1)){//LEFT
				if (multi_direction) { checkFor_newNode(pos_crypt,current_arc);}
				create_graph(mat,i,j-1,i,j,current_arc);
				multi_direction = true;
			}
			if (test_nextPosition(mat,i,j,preLine,preColumn,0,1)){//RIGHT
				if (multi_direction) { checkFor_newNode(pos_crypt,current_arc);}
				create_graph(mat,i,j+1,i,j,current_arc);
			}
		}
			
	}

}
	
/*  
	private ArrayList<ArrayList<Integer>> direction = new ArrayList<ArrayList>(new ArrayList<Integer>()) {};
		 Arrays.asList(Arrays.asList(-1,0),Arrays.asList(1,0),Arrays.asList(0,-1),Arrays.asList(0,1)); ??? gros bordel d'initialisé un
		 arrays d'array d'entier xD en pause pour preColumn'instant

	public void create_graph(int[][] mat, int i, int j, int preLine, int preColumn, Arc current_arc){
		//i,j position actuel, preLine,preColumn position precedente

		boolean multi_direction = false;

		if (new_arc) { 
			current_arc = new Arc(i,j);
			new_arc = false;
		}
		else {current_arc.add_way(i,j);}

		check_elemSpecial(mat[i][j], i, j,current_arc);
		for (int i = 0; i < direction.length ; i++ ) {
			ArrayList coord = direction.get(i);
			if (test_nextPosition(mat,i,j,preLine,preColumn,coord.get(0),coord.get(1))){//DOWN
				if (multi_direction) { check_newNode(i, j, current_arc);}
				create_graph(mat,i+coord.get(0),j+coord.get(1),i,j,current_arc);
				multi_direction = true;
			}
		}
	}
*/














