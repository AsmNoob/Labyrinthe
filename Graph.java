
import java.io.*;
import java.util.*;

//Constructeur de la class Node, il faut sa position dans la matrice, et quatre donnée correspondant a UP,DOWN,LEFT,RIGHT initialisé a null dont les valeurs changeront lors de la création de celle ci, permettant de savoir plus tard dans quel direction aller directement. Chaque direction sera un pointeur vers le sommet qui est au bout de son chemin. Puisque que nos sommet sont principalement des intersections cela nous permettera de choisir directement le bon chemin .

public class Graph {
	//Attribut
	private int dim; //dimention de la matrice a utiliser pour le cryptage
	private HashMap<Integer,Node> ens_node = new HashMap<Integer,Node>();
	private int[] DIRECTION = new int[] {-1,1,0,0};
	private int DIRECTION_SIZE = DIRECTION.length-1;
	private int iterrator;

	// define
	private int IN = 9999;
	

	// constructeur
	public Graph(int[][] mat, int[] pacmanCoord){
		dim = mat.length;
		iterrator = 0;
		long begin = System.currentTimeMillis();
<<<<<<< HEAD
		create_graph(mat,pacmanCoord[0]*2+1,pacmanCoord[1]*2+1, pacmanCoord[0]*2+1,pacmanCoord[1]*2+1, true, null); // TO CHANGE: à mon avis tu px éviter ça ^^
		//create_graph2(mat,pacmanCoord[0]*2+1,pacmanCoord[1]*2+1, pacmanCoord[0]*2+1,pacmanCoord[1]*2+1, true, null, new ArrayList<Integer>());
=======
		create_graph(mat,pacmanCoord[0]*2+1,pacmanCoord[1]*2+1, pacmanCoord[0]*2+1,pacmanCoord[1]*2+1, true, null); 
>>>>>>> 7f5254444991542c559221cc08d0e1a0e6b66f95
    	long step1 = System.currentTimeMillis();
		int nbNode_afterOptim = list_posNode.size();
		// TO FIX: problème avec optimisation_graph quand il rencontre à nouveau Pakkuman il me semble ... 
		optimisation_graph(list_node.get(0),null);
    	long step2 = System.currentTimeMillis();
<<<<<<< HEAD
		graph_converter();
    	long step3 = System.currentTimeMillis();
    	print_graph();
=======
    	//print_graph();
>>>>>>> 7f5254444991542c559221cc08d0e1a0e6b66f95
		float time1 = ((float) (step1-begin)) / 1000f;
		float time2 = ((float) (step2-step1)) / 1000f;
		float time3 = ((float) (step3-step2)) / 1000f;

		System.out.print("Time exe || create_graph : ");
		System.out.print(time1);
		System.out.print(" || Optimisation : ");
		System.out.print(time2);
		System.out.print(" || graph_converter : ");
		System.out.println(time3);

		System.out.print("Optimisation || nb node : ");
<<<<<<< HEAD
		System.out.print(nbNode_afterOptim);
=======
		System.out.print(ens_node.size()+optimisation);
>>>>>>> 7f5254444991542c559221cc08d0e1a0e6b66f95
		System.out.print(" to ");
		System.out.println(ens_node.size());

	}
	// creer une node et la place dans la list de stockage
	// temps d'exe = 0
	public Node create_node(int elem, int pos_crypt){
		Node current_node = new Node(pos_crypt, elem);
		ens_node.put(pos_crypt,current_node);
		return current_node;
	}
	// selectionne la noeud courrant -déjà existant ou non-.
	public Node select_currentNode(int elem, int pos_crypt){
		Node current_node = null;
		if (!ens_node.containsKey(pos_crypt)) {
			current_node = create_node(elem, pos_crypt);
		}
		else{
			current_node = ens_node.get(pos_crypt);
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
	}

	// test si la postion suivante est dans les bornes et autre qu'un mur.
	//appel recursif de la fonction avec test_twoCase en valeur de controle car les murs se situent entre deux chemin, ils ne constituent pas de chemin en lui même. 
	public int check_nextPosition(int[][] mat, int actuLine, int actuColumn, int preLine, int preColumn, int line_add, int column_add, boolean test_twoCase){
		//System.out.print(" actuLine: " );System.out.print(actuLine);System.out.print(" actuColumn: " );System.out.print(actuColumn);System.out.print(" preLine: " );System.out.print(preLine);System.out.print(" preColumn: " );System.out.print(preColumn);System.out.print(" line_add: " );System.out.print(line_add);System.out.print(" column_add: " );System.out.println(column_add);
		int newLine = actuLine+line_add; int newColumn = actuColumn+column_add;
		if (( (newLine >= 0) && (newLine < dim) && (newColumn>= 0) && (newColumn < dim) && (newLine != preLine || newColumn!= preColumn)) || (preLine == actuLine && preColumn == actuColumn)) {
			int elem = mat[newLine][newColumn];
			// mur = -1
			if (elem != -1) {
				if (!test_twoCase){return check_nextPosition(mat, newLine, newColumn, preLine, preColumn,line_add,column_add,true);}
				else {return 1;}
			}
		}
		return 0;
	}

	//affichage des données récuperer après analyse du labyrinthe. 
	public void print_graph(){
		ArrayList<Node> list_node = new ArrayList<Node>(ens_node.values());
		int size_dict = list_node.size();
		for (int i = 0; i < size_dict; i++ ) {
			list_node.get(i).print();
		}
	}

	public void graph_converter(){
		// TO ADD TO MAKEFILE: dot -Tpdf Graph.dot -o Graph.pdf
		try{
			PrintWriter writer = new PrintWriter ("Graph.dot");
			writer.println("graph chemin {");
			writer.println();
			ArrayList<String> list_arcsPrinted = new ArrayList<String>();
			ArrayList<Node> list_node = new ArrayList<Node>(ens_node.values());
			for(int i = 0; i < list_node.size(); i++){

				for(int j = 0; j < list_node.get(i).get_ensLink().size();j++){ // parcourt les noeuds lies
					if(!(list_arcsPrinted.contains(Integer.toString(list_node.get(i).get_ensLink().get(j).get_posCrypt())+Integer.toString(list_node.get(i).get_posCrypt())))){
						writer.print("	");writer.print(list_node.get(i).get_posCrypt());writer.print(" -- ");writer.print(list_node.get(i).get_ensLink().get(j).get_posCrypt());writer.print(" [label=");writer.print(list_node.get(i).get_ensArc().get(j).get_weight());writer.println("]");
						String arcPrinted =  Integer.toString(list_node.get(i).get_posCrypt())+ Integer.toString(list_node.get(i).get_ensLink().get(j).get_posCrypt());
						//System.out.print("arcPrinted: ");System.out.println(arcPrinted);
						list_arcsPrinted.add(arcPrinted);
					}
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
			int test = check_nextPosition(mat, actuLine, actuColumn, preLine, preColumn,DIRECTION[i],DIRECTION[j],false);

			data_direction[i] = test;
			data_direction[DIRECTION_SIZE+1]+=test;
			j--;
		}
		
		/*System.out.print("Detect_isNode || ");
		System.out.println(Arrays.toString(data_direction));
		*/
		return data_direction;
	}

	// int matrice_cout = [list_node.length][list_node.length]
	// Avec la distance entre les noeuds et infini dans le cas d'une liaison non-directe
	// Question:

	public int shortestPath_algorithm(int[][] matrice_cout,Node source,Node target){
		int dist[] = new int[matrice_cout[0].length];
		int prev[] = new int[matrice_cout[0].length];
		int selected[] = new int[matrice_cout[0].length];
		char path[] = new char[matrice_cout[0].length];
		for(int i = 0; i < matrice_cout[0].length; i++){
			dist[i] = IN;
			prev[i] = -1;
		}
		int start = source.get_nodeValue();
		selected[start] = 1;
		dist[start] = 0;
		while( selected[target.get_nodeValue()] == 0){
			int min = IN;
			int m = 0;
			for(int i = 0; i < matrice_cout[0].length; i++){
				int d = dist[start] + matrice_cout[start][i];
				if(d < dist[i] && selected[i] == 0){
					dist[i] = d;
					prev[i] = start;
				}
				if(min > dist[i] && selected[i] == 0){
					min = dist[i];
					m = i;
				}
			}
			start = m;
			selected[start] = 1;
		}
		start = target.get_nodeValue();
		int j = 0;
		while(start != -1){
			//path[j++] = start + 65; // WTFS
			j++;
			start = prev[start];
		}
		path[j] = '\0';
		String pathe = new StringBuffer(new String(path)).reverse().toString();
		System.out.println(pathe);
		return dist[target.get_nodeValue()];
	}


}















