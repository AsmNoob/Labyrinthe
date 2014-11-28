import java.io.*;
import java.util.*;

public class Dijkstra {
	private int SWEET = 3;
	private int EXIT = 5;
	private int[][] MATRIX;
	private int[][] MATRIX_SWEET; // contient les bonbons ne faisant pas partie du parcours d'origine et utilisé pour un monstre dans celui ci
	private int[][] SWEET_WAYS;
	private int[][][] DATA_SWEET;
	private ArrayList<Node> LIST_NODE;
	private int NB_NODES;
	private int IN = 9999;
	private int[] SWEET_USE; // chaque monstre possedant un bonbon déjà utilisé voit sa valeur modifier SWEET_USE[MONSTRE] = BONBON et inversement SWEET_USE[BONBON] = MONSTER
	private int[] VISITED;// permet de savoir les noeuds déjà visités
	private int[] VISITED_REAL; // permet de savoir les noeuds reellement visité par le parcours choisi.
	private	int[] PREDECESSOR; // garde en mémoire de quel noeud on est venu pour arriver en predecessor[i]
	private	ArrayList<Integer> MULTI_NODE =new ArrayList<Integer>(); // list dont l'indice des nodes ayant plusieurs direction sont égale a 1
	private HashMap<Integer,int []> NODE_MONSTER = new HashMap<Integer,int []>();
	private HashMap<Integer,Integer> SWEET_NODE = new HashMap<Integer,Integer>(); // key=node, value=indice dans MATRIX_SWEET[][value]



	public Dijkstra(ArrayList<Node> listNode){ // Prints Mat_linkNode

		LIST_NODE = listNode; 
		NB_NODES =LIST_NODE.size();
		VISITED = new int[NB_NODES]; 
		PREDECESSOR = new int[NB_NODES]; 
		SWEET_USE = new int[NB_NODES];
		createMat_link();
		dijkstra(0,EXIT);
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +LIST_NODE.get(i).get_posCrypt());
		}
		System.out.println();
	}

	public void createMat_link(){

		MATRIX = new int[NB_NODES][NB_NODES]; // utilisé pou le liens entre les noeuds
		for(int i = 0; i < NB_NODES; i++){
			if (LIST_NODE.get(i).isSweet()) {SWEET_NODE.put(i,SWEET_NODE.size());}
			for(int j = i; j < NB_NODES;j++){
				// MOCHE !!!!!!
				try{
					MATRIX[i][j] = MATRIX[j][i] = LIST_NODE.get(i).get_arc(LIST_NODE.get(j)).get_weight();
				}catch(NullPointerException e){
					MATRIX[i][j] = MATRIX[j][i] = IN;
				}
			}
		}
		MATRIX_SWEET = new int[NB_NODES][SWEET_NODE.size()]; // matrice permettant d'établir la liaison entre chaque noeud et les bonbons afin de savoir si ils ont été utilisé
	}

	// On choisit quel est le noeud connecté au noeud actuel avec le plus court chemin.
	public int[] find_lightWay(int[] distance, int[] visited, int valueToFind){
		int[] lightWay = new int[2];
		int min = IN;

		for (int i = 0; i < NB_NODES ;i++ ) {
			//System.out.println("ActuNode: " +LIST_NODE.get(actu_node).get_posCrypt()+ " Test_link: " + LIST_NODE.get(i).get_posCrypt() + " state: " + visited[i] + " is_link: " +LIST_NODE.get(actu_node).isLinkTo(LIST_NODE.get(i)));
 			//LIST_NODE.get(i).print();
			if( distance[i] < min && visited[i] != 1 && (!LIST_NODE.get(i).isUnidirectionnel() || LIST_NODE.get(i).get_nodeValue() == valueToFind))
			{ 
				//System.out.println("Short_link: " + LIST_NODE.get(i).get_posCrypt());
				// si le noeud n'a pas été visité et la distance entre les noeuds est < le min
				min = distance[i]; // min prend la distance entre les noeuds
				lightWay[1]= i; // on svg i comme étant le prochain noeud
			}			
		}
		lightWay[0]=min;
		return lightWay;
	}

	public void state_actuWay(int actu_node){

		int j = actu_node;
		do{
			if (LIST_NODE.get(j).isMultidirectionnel()) {MULTI_NODE.add(j);}
			j=PREDECESSOR[j];
			VISITED_REAL[j] = 1;
		}while(j!=0); //0 est l'indice zero dans LIST_NODE qui correspond au pakkuman
		DATA_SWEET = new int[MULTI_NODE.size()][3][NB_NODES];// Matrix reprenant pour chaque noeud multidirectionnel les listes predecessor/distance/nb_sweet

	}

	public int find_sweet(int actu_node){
		MULTI_NODE.clear();
		VISITED_REAL = new int[NB_NODES];

		state_actuWay(actu_node);
		
		for (int i=0; i< MULTI_NODE.size() ;i++ ) {
			dijkstra_sweet(actu_node,MULTI_NODE.get(i),i);
		}
		int min = choice_bestSweet(actu_node);
		System.out.println("END_DIJKSTRA");
		if (min == IN) { return IN;}
		else{ return min*2;}
	}

	public int choice_bestSweet(int actu_node){
		int min = IN;
		int sweet = 0;//BAD
		int multi_node= 0;
		for (int i = 0; i< MULTI_NODE.size() ;i++) {
			for (int j=0; j< NB_NODES ;j++ ) {
				if (DATA_SWEET[i][0][j] >= 1 && DATA_SWEET[i][1][j] < min) {
					min = DATA_SWEET[i][1][j]; 
					sweet = j;
					multi_node=i;//MULTI_NODE.get(i);
				}
			}
		}
		System.out.println("Sweet: " + sweet + " Min: " + min);
		int l=sweet;
		do{
			System.out.print(l + "<--");
			l=DATA_SWEET[multi_node][2][l];

		}while(l!=0); 
		System.out.println(SWEET_NODE);
		if (min != IN) {MATRIX_SWEET[actu_node][SWEET_NODE.get(sweet)] = 1;}
		return min;
	}

	public boolean sweet_notUse(int actu_node, int sweet){
		System.out.println("SWEET_NOTUSE");
		System.out.println(SWEET_NODE);
		System.out.println(sweet + " " + !(MATRIX_SWEET[actu_node][SWEET_NODE.get(sweet)] == 1));
		if (MATRIX_SWEET[actu_node][SWEET_NODE.get(sweet)] == 1) {return false;}
		return true;
	}
	public void dijkstra_sweet (int actu_node,int start_node,int index){
		System.out.println("DIJKSTRA_SWEET");
		int[] distance = new int[NB_NODES]; // permet de connaitre la distance jusqu'a  un certain node.
		int[] nb_sweet = new int[NB_NODES]; // permet de savoir combien de bonbon le chemin jusqu'à cette node possède.
		int[] predecessor = new int[NB_NODES];

		int min;
		int[] lightWay = new int[2];
		int next_node = start_node;
		int pre_node = start_node;

		// Algorithme préparé pour que PAKKUMAN soit en mat[0][0]
		distance = MATRIX[start_node]; // distance de node0 aux autres nodes
		VISITED_REAL[start_node] = 1; // on considère le premier node comme visité
		distance[start_node] = 0; // car c'est le noeud de départ

		while(LIST_NODE.get(next_node).get_nodeValue() != SWEET){ // On s'arrete si la valeur de la node actuelle traité est celle qu'on cherche

			print_state(predecessor,distance,VISITED_REAL);

			pre_node = next_node;
			lightWay = find_lightWay(distance,VISITED_REAL,SWEET);
			min = lightWay[0]; next_node =lightWay[1];
			if (pre_node == next_node) {break;}
			else{ pre_node = predecessor[next_node];}

			System.out.println("Next_node: " + LIST_NODE.get(next_node).get_posCrypt());

			nb_sweet[next_node] = nb_sweet[pre_node];
			if (LIST_NODE.get(next_node).isMonster()) {nb_sweet[next_node] -=1;}
			if (LIST_NODE.get(next_node).isSweet() && sweet_notUse(actu_node,next_node)) {nb_sweet[next_node] +=1;}
			
			VISITED_REAL[next_node] = 1;// On indique que le noeud a été visité

			//if (nb_sweet[next_node] <=0) { // si le parcours actuelle possède un bonbon on a plus besoin d'aller plus loin
			System.out.println("NB_SWEET: " + nb_sweet[next_node]);
			for(int j = 0; j < NB_NODES; j++){
				// On regarde si la distance entre le "min" + la distance du "next_node" est plus petite que la distance à partir du noeud de départ
				
				if( VISITED_REAL[j]!=1 && (min+MATRIX[next_node][j] < distance[j])){
					System.out.println("Link_node: " + LIST_NODE.get(j).get_posCrypt());
					// si c'est le cas on indique la nouvelle distance entre le noeud de départ et le noeud 'j'
					if( !LIST_NODE.get(j).isMonster() || nb_sweet[next_node] >0) {
						distance[j] = min+MATRIX[next_node][j];
						// on indique par quel noeud on est passé avant
						nb_sweet[j] = nb_sweet[next_node];
						predecessor[j] = next_node;
					}
				}
			}
	//		}
		}
		DATA_SWEET[index][0] = nb_sweet;
		DATA_SWEET[index][1] = distance;
		DATA_SWEET[index][2] = predecessor;
	}

	public int find_shortWay(){
		int min = IN;
		int choice =0 ;// utiliser un try
		for (int i = 0; i< MULTI_NODE.size() ;i++ ) {

			if (min > SWEET_WAYS[i][NB_NODES+1] && SWEET_WAYS[i][NB_NODES+1] > 0) { 
				choice = i;
				min = SWEET_WAYS[i][NB_NODES+1];
			}
		}
		SWEET_WAYS[choice][NB_NODES+1] = 0;
		return min;
	}
	public void dijkstra (int indexStartNode, int valueToFind){

		int[] distance = new int[NB_NODES]; // permet de connaitre la distance jusqu'a  un certain node.
		int[] nb_sweet = new int[NB_NODES]; // permet de savoir combien de bonbon le chemin jusqu'à cette node possède.

		int min;
		int way_supp;
		int[] lightWay = new int[2];
		int next_node = indexStartNode;
		int pre_node = indexStartNode;
		int[] info_supp = new int[NB_NODES+2]; // 2 elements de plus pour contenir les informations supplementaire concernant a un ajout de chemin afin de trouver un bonbon 

		boolean noConnexion = false;
		// Algorithme préparé pour que PAKKUMAN soit en mat[0][0]
		distance = MATRIX[indexStartNode]; // distance de node0 aux autres nodes
		VISITED[indexStartNode] = 1; // on considère le premier node comme visité
		distance[indexStartNode] = 0; // car c'est le noeud de départ

		while(LIST_NODE.get(next_node).get_nodeValue() != valueToFind){ // On s'arrete si la valeur de la node actuelle traité est celle qu'on cherche

			print_state(PREDECESSOR,distance,VISITED);
			pre_node = next_node;
			lightWay = find_lightWay(distance,VISITED,valueToFind);
			min = lightWay[0]; next_node =lightWay[1];
			if (pre_node == next_node) {break;}
			else{ pre_node = PREDECESSOR[next_node];}
			System.out.println("Next_node: " + LIST_NODE.get(next_node).get_posCrypt());

			// On indique que le noeud a été visité
			MATRIX_SWEET[next_node] = MATRIX_SWEET[pre_node];
			nb_sweet[next_node] = nb_sweet[pre_node];
			if (LIST_NODE.get(next_node).isMonster()) {nb_sweet[next_node] -=1;}
			if (LIST_NODE.get(next_node).isSweet()) {nb_sweet[next_node] +=1;}
			
			VISITED[next_node] = 1;
			// On indique si le noeud est multidirectionnel
			for(int j = 0; j < NB_NODES; j++){
				way_supp = 0;
				// On regarde si la distance entre le "min" + la distance du "next_node" est plus petite que la distance à partir du noeud de départ
				
				if( VISITED[j]!=1 && (min+MATRIX[next_node][j] < distance[j])){

					System.out.println("Link_node: " + LIST_NODE.get(j).get_posCrypt());
					// si c'est le cas on indique la nouvelle distance entre le noeud de départ et le noeud 'j'
					if(LIST_NODE.get(j).isMonster() && nb_sweet[next_node] <=0) { 
						way_supp = find_sweet(next_node);
					}
					System.out.println("DISTANCE --> " + (min+way_supp+MATRIX[next_node][j]));

					if (min+way_supp+MATRIX[next_node][j] < distance[j]) {
						System.out.println("Distance_node: " +distance[j]);
						distance[j] = min+MATRIX[next_node][j]+way_supp;
						// on indique par quel noeud on est passé avant
						MATRIX_SWEET[j] = MATRIX_SWEET[next_node];
						nb_sweet[j] = nb_sweet[next_node];
						PREDECESSOR[j] = next_node;
					}
				}
			}
		}
		print_way(distance,PREDECESSOR, indexStartNode);
	}

	public void print_state(int[] predecessor, int[] distance, int[] visited){
		System.out.println("Indice node: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" + i+ "=" +LIST_NODE.get(i).get_posCrypt());
		}
		System.out.println();
		System.out.println("Distance: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +distance[i]);
		}
		System.out.println();
		
		System.out.println("Predecessor: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +predecessor[i]);
		}
		System.out.println();
		System.out.println("Visited: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +visited[i]);
		}
		System.out.println();
	}

	// On va donc récupérer tous les chemins les plus court en partant 
	public void print_way(int[] distance, int[] predecessor, int indexStartNode){

		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +LIST_NODE.get(i).get_posCrypt()+": "+ distance[i]);
		}
		System.out.println("|");
		int j;
		for(int i = 0; i < NB_NODES; i++){
			if (distance[i]<IN) {System.out.println("Good_Way");}
			else {System.out.println("Wrong_way");}
			if(i!=indexStartNode){
				System.out.print("Path = ");System.out.print(LIST_NODE.get(i).get_posCrypt());
				j = i;
				do{
					j=predecessor[j];
					System.out.print(" <- ");System.out.print(LIST_NODE.get(j).get_posCrypt());
					
				}while(j!=indexStartNode);
			}
			System.out.println();
		}
	}
}