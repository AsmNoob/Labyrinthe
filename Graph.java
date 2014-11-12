
import java.io.*;
import java.util.*; // a vérifier

//Constructeur de la class Node, il faut sa position dans la matrice, et quatre donnée correspondant a UP,DOWN,LEFT,RIGHT initialisé a null dont les valeurs changeront lors de la création de celle ci, permettant de savoir plus tard dans quel direction aller directement. Chaque direction sera un pointeur vers le sommet qui est au bout de son chemin. Puisque que nos sommet sont principalement des intersections cela nous permettera de choisir directement le bon chemin .

public class Graph {
	//Attribut
	private int dim; //dimention de la matrice a utiliser pour le cryptage
	private ArrayList<Integer> list_posNode = new ArrayList<Integer>();
	private ArrayList<Node> list_node = new ArrayList<Node>();
	private int[] DIRECTION = new int[] {-1,1,0,0};
	private int DIRECTION_SIZE = DIRECTION.length-1;
	private int iterrator;
	

	// constructeur
	public Graph(int[][] mat, int[] pacmanCoord){
		dim = mat.length;
		iterrator = 0;
		create_graph(mat,pacmanCoord[0],pacmanCoord[1], pacmanCoord[0],pacmanCoord[1], true, null); // TO CHANGE: à mon avis tu px éviter ça ^^
		//print_graph();
		int nbNode_afterOptim = list_posNode.size();
		optimisation_graph(list_node.get(0),null);
		graph_converter();
		print_graph();
		System.out.print("Optimisation || nb node : ");
		System.out.print(nbNode_afterOptim);
		System.out.print(" to ");
		System.out.println(list_posNode.size());



	}

	public Node create_node(int elem, int pos_crypt){
		Node current_node = new Node(pos_crypt, elem);
		list_node.add(current_node);list_posNode.add(pos_crypt);
		return current_node;
	}
	// selectionne la noeud courrant -déjà existant ou non-.
	public Node select_currentNode(int elem, int pos_crypt){
		Node current_node = null;
		if (!list_posNode.contains(pos_crypt)) {
			current_node = create_node(elem, pos_crypt);
		}
		else{
			int index = list_posNode.indexOf(pos_crypt);
			current_node = list_node.get(index);
		}
		return current_node;
	}

	public void update_nodeLink(Arc current_arc){
		current_arc.get_startNode().add_link(current_arc.get_endNode(),current_arc);
		current_arc.get_endNode().add_link(current_arc.get_startNode(),current_arc);
	}
	public Arc start_Arc(int pos_crypt, Node start_node){
		Arc current_arc = new Arc(pos_crypt);
		current_arc.set_startNode(start_node);
		return current_arc;
	}

	public void end_Arc(Arc current_arc, Node current_node, int pos_crypt){
		current_arc.add_way(pos_crypt);
		current_arc.set_endNode(current_node);
		current_arc.set_stateArc(false);

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

	public void graph_converter(){
		// TO ADD TO MAKEFILE: dot -Tpdf Graph.dot -o Graph.pdf 
		// TO DO: petit soucis au niveau des valeurs qui sortent :p
		try{
			PrintWriter writer = new PrintWriter ("Graph.dot");
			writer.println("graph chemin {");
			writer.println();
			for(int i = 0; i < list_node.size(); i++){

				for(int j = 0; j < list_node.get(i).get_ensLink().size();j++){ // parcourt les noeuds lies
					writer.print("	");writer.print(list_node.get(i).get_posCrypt());writer.print(" -- ");writer.print(list_node.get(i).get_ensLink().get(j).get_posCrypt());writer.print(" [label=");writer.print(list_node.get(i).get_ensArc().get(j).get_weight());writer.println("]");
				}
			}
			writer.println(); 
			writer.println("}");
			writer.close();
		}catch(FileNotFoundException e){
			System.err.println("Caught FileNotFoundException: " + e.getMessage());
		}
	}


	// cryptage de la position i,j
	public int pos_cryptage(int i, int j){
		int pos = 10000+(i*100)+j;
		return pos;
	}

	// decryptage de la position 
	public int[] pos_decryptage( int pos_crypt){
		int[] position= new int[] {pos_crypt %100,(pos_crypt-10000-(pos_crypt%100))/100};
		return position; 
	}

	// detect si la position actuelle est une node et renvois une liste d'entier contenant les directions possible suivante.
	public int[] detect_isNode(int[][] mat, int actuLine, int actuColumn, int preLine, int preColumn){
		int j = DIRECTION_SIZE;
		int[] data_direction = new int[5];// 4 premier entier -0,1- correspond au direction possible ou non le 5eme si il est >1 nous indiques que c'est une node.
		for (int i = 0; i<= DIRECTION_SIZE; i++){
			int test = test_nextPosition(mat, actuLine, actuColumn, preLine, preColumn,DIRECTION[i],DIRECTION[j]);

			data_direction[i] = test;
			data_direction[DIRECTION_SIZE+1]+=test;
			j--;
		}
		/*
		System.out.print("Detect_isNode || ");
		System.out.println(Arrays.toString(data_direction));
		*/
		return data_direction;
	}
	public void supp_node(Node node){
		for (int i = 0; i< list_node.size() ;i++ ) {
			if (list_node.get(i)== node) {
				list_node.remove(i);
				list_posNode.remove(i);
				break;
			}	
		}
	}
	// supprime les noeuds ne menant a rien autre qu'un vide ou un monstre O(4N)
	public boolean optimisation_graph(Node current_node, Node pre_node){
		if (current_node.get_nodeValue() == 2 || current_node.get_nodeValue() == 0 || current_node.get_nodeValue() == 1 ){
			int i = 0;
			boolean uselessNode = true;
			ArrayList<Node> node_link= new ArrayList<Node>(current_node.get_ensLink());
			while (i < node_link.size()){
				if (pre_node!= node_link.get(i)) {
					if ( node_link.get(i).get_nodeValue()!= 1 && node_link.get(i).get_nodeValue()!= 3 && node_link.get(i).get_nodeValue()!= 4){
						uselessNode = optimisation_graph(node_link.get(i), current_node);
					}
					else {uselessNode = false;}
				}
				i++;
			}
			if (uselessNode) {
				pre_node.supp_link(current_node);
				supp_node(current_node);
			}
			return uselessNode;
		}
		else {return false;}
	}
	// parcours en backtraking du labyrinthe créant a chaque intersection de chemin une node - un sommet-.  
	public void create_graph(int[][] mat, int actuLine, int actuColumn, int preLine, int preColumn, boolean isNode,Arc current_arc){
		//actuLine,j position actuel, preLine,preColumn position precedente
		/*System.out.print("Controle_Create || prePos --> ");System.out.print(preLine);System.out.print(preColumn);
		System.out.print(" || actuPos --> ");System.out.print(actuLine);System.out.print(actuColumn);
		System.out.print(" || isNode --> ");System.out.println(isNode);
		*/
		Node current_node = null;
		int pos_crypt= pos_cryptage(actuLine,actuColumn);
		iterrator++;
				
		if (!list_posNode.contains(pos_crypt)){
			int[] data_direction = detect_isNode(mat,actuLine,actuColumn,preLine,preColumn);

			if (data_direction[DIRECTION_SIZE+1]>1 || mat[actuLine][actuColumn]>0)  { 
				current_node = select_currentNode(mat[actuLine][actuColumn],pos_crypt);
				if (current_arc != null) {
					end_Arc(current_arc,current_node,pos_crypt);
					update_nodeLink(current_arc); 
				}
				isNode = true;
			}
			else {isNode = false;}

			if (!isNode) {current_arc.add_way(pos_crypt);}
			for (int i = 0; i <= DIRECTION_SIZE; i++) {
				if (data_direction[i] == 1) {
					if (isNode) { current_arc = start_Arc(pos_crypt,current_node);}
					int newLine = actuLine+DIRECTION[i]; int newColumn = actuColumn+DIRECTION[DIRECTION_SIZE-i];
					create_graph(mat,newLine,newColumn,actuLine,actuColumn,isNode,current_arc);
				}	
			}
		}
		else{
			/*
			Si on est dans le cas d'une connexion a une node qui a déjà été créé on entre pas dans le procesus de création de connexion
			or il se peut que la connexion n'existe pas encore. On recupere ici l'objet node, on ferme l'arc et met a jour les connexions cas pertinent pour : 
			
			+   +---+   +---+---+ On s'occupera de P puis de 1 en partant vers le haut mais au retour
			|   |     1         | du backtrack on testera a droite du 1 pour rejoindre 2 qui a une
			+   +---+ P +---+   + connexion avec P. Sans ce bloc on ne créera pas la dernierers 
			|         2 |       | connexions puisque P existe déjà lors du traitement de 2.
			+---+---+   +   +---+ 
			 	   	|       | 
					+---+---+

			*/
			current_node = select_currentNode(mat[actuLine][actuColumn],pos_crypt);
			end_Arc(current_arc,current_node,pos_crypt);
			update_nodeLink(current_arc);		
		}
		
	}

	// int matrice_cout = [list_node.length][list_node.length]
	// Avec la distance entre les noeuds et infini dans le cas d'une liaison non-directe
	// Question:
	public void shortestPath_algorithm(int[][] matrice_cout,Node source,Node target){
		int dist[] = new int[matrice_cout.length];
		int prev[] = new int[matrice_cout.length];
		int selected[] = new int[matrice_cout.length];
	}


}















