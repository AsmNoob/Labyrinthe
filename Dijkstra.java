import java.io.*;
import java.util.*;

public class Dijkstra {

	private String ELEM = "";
	private int SWEET = 3;
	private int EXIT = 5;
	private int[][] MATRIX;
	private int[][] MATRIX_SWEET; // contient les bonbons ne faisant pas partie du parcours d'origine et utilisé pour un monstre dans celui ci
	private int[][] MATRIX_NODESUPP; 
	private int[][][] DATA_SWEET;
	private ArrayList<Node> LIST_NODE;
	private int NB_NODES;
	private int IN = 9999;
	private int[] VISITED;// permet de savoir les noeuds déjà visités
	private int[] VISITED_REAL; // permet de savoir les noeuds reellement visité par le parcours choisi.
	private	int[] PREDECESSOR; // garde en mémoire de quel noeud on est venu pour arriver en predecessor[i]
	private int[] DISTANCE; // permet de connaitre la distance jusqu'a  un certain node.
	private	ArrayList<Integer> MULTI_NODE =new ArrayList<Integer>(); // list dont l'indice des nodes ayant plusieurs direction sont égale a 1
	private ArrayList<Integer> SWEET_INDEX =new ArrayList<Integer>();
	private int INDEX_EXIT;


	public Dijkstra(ArrayList<Node> listNode){ // Prints Mat_linkNode

		LIST_NODE = listNode; 
		NB_NODES =LIST_NODE.size();
		VISITED = new int[NB_NODES]; 
		PREDECESSOR = new int[NB_NODES]; 

		createData_struc();
		dijkstra(0,EXIT);
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +LIST_NODE.get(i).get_posCrypt());
		}
		System.out.println();
		System.out.println();
		System.out.println("-------------- End Dijkstra --------------");

		print_allWay(DISTANCE,PREDECESSOR, 0);
		System.out.println();
		System.out.println();
		System.out.println("-------------- EXIT --------------");
		print_way(DISTANCE,PREDECESSOR,INDEX_EXIT,0);

	}

	public void createData_struc(){

		MATRIX = new int[NB_NODES][NB_NODES]; // utilisé pou le liens entre les noeuds
		MATRIX_NODESUPP = new int[NB_NODES][NB_NODES]; // matrice permettant pour chaque noeuds de garder en mémoire les chemins supplementaire existant pour récuperer des bonbons.
		DISTANCE = new int[NB_NODES]; 
		Arrays.fill(DISTANCE,IN); // permet de commencer par un test de chaque connexion car si il est directement connécté a un monstre, la distance qu'on devra ajouter pour la recuperation du bonbon sera plus grande que le chemin de base (qui est impossible)
		
		for(int i = 0; i < NB_NODES; i++){
			if (LIST_NODE.get(i).isSweet()) {SWEET_INDEX.add(i);}
			if (LIST_NODE.get(i).isExit()) {INDEX_EXIT = i;}
			for(int j = i; j < NB_NODES;j++){
				// MOCHE !!!!!!
				MATRIX_NODESUPP[i][j] = -1; // initialise la matrice a -1
				try{
					MATRIX[i][j] = MATRIX[j][i] = LIST_NODE.get(i).get_arc(LIST_NODE.get(j)).get_weight();
				}catch(NullPointerException e){
					MATRIX[i][j] = MATRIX[j][i] = IN;
				}
			}
		}
		MATRIX_SWEET = new int[NB_NODES][SWEET_INDEX.size()]; // matrice permettant d'établir la liaison entre chaque noeud et les bonbons afin de savoir si ils ont été utilisé
	}

	// On choisit quel est le noeud connecté au noeud actuel avec le plus court chemin.
	public int[] find_lightWay(int[] distance, int[] visited,int[] nb_sweet, int valueToFind){
		int[] lightWay = new int[2];
		int min = IN;
		System.out.print(ELEM);
		for (int i = 0; i < NB_NODES ;i++ ) {
			//System.out.println("ActuNode: " +LIST_NODE.get(actu_node).get_posCrypt()+ " Test_link: " + LIST_NODE.get(i).get_posCrypt() + " state: " + visited[i] + " is_link: " +LIST_NODE.get(actu_node).isLinkTo(LIST_NODE.get(i)));
 			//LIST_NODE.get(i).print();
 			//System.out.print(" |lightWay: " + nb_sweet[i] + " " + LIST_NODE.get(i).get_posCrypt());
			if( distance[i] < min && visited[i] != 1 && (!LIST_NODE.get(i).isUnidirectionnel() || LIST_NODE.get(i).get_nodeValue() == valueToFind) && (!LIST_NODE.get(i).isMonster() || nb_sweet[i] > 0))
			{ 
				//System.out.println("Short_link: " + LIST_NODE.get(i).get_posCrypt());
				// si le noeud n'a pas été visité et la distance entre les noeuds est < le min
				min = distance[i]; // min prend la distance entre les noeuds
				lightWay[1]= i; // on svg i comme étant le prochain noeud
			}			
		}
		//System.out.println();
		lightWay[0]=min;
		return lightWay;
	}

	public void state_actuWay(int actu_node){

		int j = actu_node;
		do{
			//System.out.println(ELEM + j);
			if (LIST_NODE.get(j).isMultidirectionnel()) {MULTI_NODE.add(j);}
			if (j>0){
				j=PREDECESSOR[j];
				VISITED_REAL[j] = 1;
			}
			else {j= -1;}
		}while(j>=0); //0 est l'indice zero dans LIST_NODE qui correspond au pakkuman
		DATA_SWEET = new int[MULTI_NODE.size()][3][NB_NODES];// Matrix reprenant pour chaque noeud multidirectionnel les listes predecessor/distance/nb_sweet

	}

	public int find_sweet(int actu_node){
		System.out.println("|--|--|-- DIJKSTRA_SWEET");
		ELEM = "    ";

		MULTI_NODE.clear();
		VISITED_REAL = new int[NB_NODES];

		state_actuWay(actu_node);
		System.out.println(ELEM + " MULTI_NODE: " +MULTI_NODE);
		for (int i=0; i< MULTI_NODE.size() ;i++ ) {
			dijkstra_sweet(actu_node,MULTI_NODE.get(i),i);
		}
		int min = choice_bestSweet(actu_node);
		ELEM = "";
		System.out.println("|--|--|-- END_DIJKSTRA");
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
					multi_node=i;
				}
			}
		}
		System.out.println(ELEM + actu_node + " Sweet: " + sweet + " Min: " + min);
		if (min != IN) { updateData_newSweet(actu_node, multi_node, sweet);}
		return min;
	}

	public void updateData_newSweet(int actu_node,int multi_node, int sweet){
		int l=sweet;
		int k =-1; // predecessor temporaire
		do{
			if (k != -1 && MATRIX_NODESUPP[actu_node][k] == -1 ) {MATRIX_NODESUPP[actu_node][k] = l;}
			k = l;
			System.out.print(l + "<--");
			l=DATA_SWEET[multi_node][2][l];
			if (k == l) {break;}
		}while(l>=0);
		System.out.println();
		System.out.print(ELEM + " PREDECESSOR_SUPP: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +MATRIX_NODESUPP[actu_node][i]);
		}
		System.out.println();
		System.out.println(ELEM + SWEET_INDEX);
		MATRIX_SWEET[actu_node][SWEET_INDEX.indexOf(sweet)] = 1;

	}

	public boolean sweet_notUse(int actu_node, int sweet){
		System.out.println(ELEM + "SWEET_NOTUSE");
		System.out.println(ELEM + SWEET_INDEX);
		System.out.println(ELEM + sweet + " " + !(MATRIX_SWEET[actu_node][SWEET_INDEX.indexOf(sweet)] == 1));
		if (MATRIX_SWEET[actu_node][SWEET_INDEX.indexOf(sweet)] == 1) {return false;}
		return true;
	}
	public void dijkstra_sweet (int actu_node, int start_node,int index){
		int[] distance = new int[NB_NODES]; // permet de connaitre la distance jusqu'a  un certain node.
		int[] nb_sweet = new int[NB_NODES]; // permet de savoir combien de bonbon le chemin jusqu'à cette node possède.
		int[] predecessor = new int[NB_NODES];
		Arrays.fill(predecessor, -1);

		int min =0;
		int min_real;
		int[] lightWay = new int[2];
		int next_node = start_node;
		int pre_node = start_node;

		// Algorithme préparé pour que PAKKUMAN soit en mat[0][0]
		distance = MATRIX[start_node]; // distance de node0 aux autres nodes
		VISITED_REAL[start_node] = 1; // on considère le premier node comme visité
		distance[start_node] = 0; // car c'est le noeud de départ
		while(nb_sweet[next_node] <=0 ){ // On s'arrete si la valeur de la node actuelle traité est celle qu'on cherche
			System.out.println(ELEM + "Actu_node: " + LIST_NODE.get(start_node).get_posCrypt());

			print_state(predecessor,distance,VISITED_REAL);
			
			VISITED_REAL[next_node] = 1;// On indique que le noeud a été visité

			System.out.println(ELEM + "NB_SWEET: " + nb_sweet[next_node]);
			
			for(int j = 0; j < NB_NODES; j++){
			// On regarde si la distance entre le "min" + la distance du "next_node" est plus petite que la distance à partir du noeud de départ
				min_real = min+MATRIX[next_node][j];
				if (MATRIX_NODESUPP[actu_node][j] != -1) {min_real = min;} // test si le chemin tester n'est pas déjà utilisé pour une précedente recherche de bonbon si oui il n'y a aucune distance supplémentaire a ajouter
					
				if( VISITED_REAL[j]!=1 && (min_real < distance[j]) && LIST_NODE.get(next_node).isLinkTo(LIST_NODE.get(j))){
					System.out.println(ELEM + "Link_node: " + LIST_NODE.get(j).get_posCrypt());
					// si c'est le cas on indique la nouvelle distance entre le noeud de départ et le noeud 'j'
					if( !LIST_NODE.get(j).isMonster() || nb_sweet[next_node] >0) {
						distance[j] = min_real;
						// on indique par quel noeud on est passé avant
						nb_sweet[j] = nb_sweet[next_node];
						predecessor[j] = next_node;
					}
				}
			}	

			pre_node = next_node;
			lightWay = find_lightWay(distance,VISITED_REAL,nb_sweet,SWEET);
			min = lightWay[0]; next_node =lightWay[1];

			if (pre_node == next_node) {break;}
			predecessor[next_node] = pre_node;

			System.out.println(ELEM + "Next_node: " + LIST_NODE.get(next_node).get_posCrypt());

			nb_sweet[next_node] = nb_sweet[predecessor[next_node]];
			if (LIST_NODE.get(next_node).isMonster()) {nb_sweet[next_node] -=1;}
			if (LIST_NODE.get(next_node).isSweet() && sweet_notUse(actu_node,next_node)) {nb_sweet[next_node] +=1;}
		}
		DATA_SWEET[index][0] = nb_sweet;
		DATA_SWEET[index][1] = distance;
		DATA_SWEET[index][2] = predecessor;
	}

	public void dijkstra (int indexStartNode, int valueToFind){

		int[] nb_sweet = new int[NB_NODES]; // permet de savoir combien de bonbon le chemin jusqu'à cette node possède.

		int min = 0;
		int way_supp;
		int[] lightWay = new int[2];
		int next_node = indexStartNode;
		int pre_node = indexStartNode;
		int[] info_supp = new int[NB_NODES+2]; // 2 elements de plus pour contenir les informations supplementaire concernant a un ajout de chemin afin de trouver un bonbon 

		// Algorithme préparé pour que PAKKUMAN soit en mat[0][0]
		VISITED[indexStartNode] = 1; // on considère le premier node comme visité

		DISTANCE[indexStartNode] = 0; // car c'est le noeud de départ

		while(LIST_NODE.get(next_node).get_nodeValue() != valueToFind){ // On s'arrete si la valeur de la node actuelle traité est celle qu'on cherche
			print_state(PREDECESSOR,DISTANCE,VISITED);
			System.out.println("Actu_node: " + LIST_NODE.get(next_node).get_posCrypt());
			
			VISITED[next_node] = 1;
			// On va d'abord faire une recherche dans les distances avant de selectionné la plus courte afin d'appeler dijkstra_sweet si nécessaire.
			for(int j = 0; j < NB_NODES; j++){
				way_supp = 0;
				// On regarde si la distance entre le "min" + la distance du "next_node" est plus petite que la distance à partir du noeud de départ
				//test si le noeud na pas encore été visité, si le min actuelle additionné a la distance entre les deux noeuds est plus petit que la distance pour atteindre ce noeud déjà enregistré et si il existe une connexion entre les deux noeuds 
				if( VISITED[j]!=1 && (min+MATRIX[next_node][j] <= DISTANCE[j]) && LIST_NODE.get(next_node).isLinkTo(LIST_NODE.get(j))){ // test si 

					System.out.println("Link_node: " + LIST_NODE.get(j).get_posCrypt() + " dist : " +(min+MATRIX[next_node][j]) + " "+  DISTANCE[j]);
					// si c'est le cas on indique la nouvelle distance entre le noeud de départ et le noeud 'j'
					if(LIST_NODE.get(j).isMonster() && nb_sweet[next_node] <=0) { 
						way_supp = find_sweet(next_node);
						if (way_supp!=IN) {nb_sweet[next_node]+=1;}
					}
					System.out.println("Distance_linkNode: " + (min+way_supp+MATRIX[next_node][j]));
					System.out.println("Distance_record: " +DISTANCE[j]);

					if (min+way_supp+MATRIX[next_node][j] < DISTANCE[j]) {
						DISTANCE[j] = min+MATRIX[next_node][j]+way_supp;
						// on indique par quel noeud on est passé avant
						MATRIX_SWEET[j] = MATRIX_SWEET[next_node].clone();
						MATRIX_NODESUPP[j] = MATRIX_NODESUPP[next_node].clone();
						nb_sweet[j] = nb_sweet[next_node];
						PREDECESSOR[j] = next_node;
					}
					System.out.println("Nb_sweet: " + nb_sweet[j] + " "+j);
				}
			}

			pre_node = next_node;
			lightWay = find_lightWay(DISTANCE,VISITED,nb_sweet,valueToFind);
			min = lightWay[0]; next_node =lightWay[1];
			if (pre_node == next_node) {break;}
			else{ pre_node = PREDECESSOR[next_node];}

			System.out.println("Next_node: " + LIST_NODE.get(next_node).get_posCrypt());

			// On indique que le noeud a été visité
			//nb_sweet[next_node] = nb_sweet[pre_node];
			if (LIST_NODE.get(next_node).isMonster()) {nb_sweet[next_node] -=1;}
			if (LIST_NODE.get(next_node).isSweet()) {nb_sweet[next_node] +=1;}
		}
	}

	public void print_state(int[] predecessor, int[] distance, int[] visited){
		System.out.print(ELEM + "|--|--|-- Indice node: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" + i+ "=" +LIST_NODE.get(i).get_posCrypt());
		}
		System.out.println();
		System.out.print(ELEM + "|--|--|-- Distance: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +distance[i]);
		}
		System.out.println();
		
		System.out.print(ELEM + "|--|--|-- Predecessor: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +predecessor[i]);
		}
		System.out.println();
		System.out.print(ELEM + "|--|--|-- Visited: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +visited[i]);
		}
		System.out.println();
	}

	// On va donc récupérer tous les chemins les plus court en partant 
	public void print_allWay(int[] distance, int[] predecessor, int indexStartNode){

		for(int i = 0; i < NB_NODES; i++){
			print_way(distance,predecessor,i,indexStartNode);
		}
	}
	public void print_way(int[] distance, int[] predecessor, int node, int indexStartNode){
		int j;
		if (distance[node]<IN) {System.out.println("Good_Way");}
		else {System.out.println("Wrong_way");}

		if(node!=indexStartNode){
			System.out.print("Weight = " + distance[node]+ "| Path = ");System.out.print(LIST_NODE.get(node).get_posCrypt());
			j = node;
			do{
				j=predecessor[j];
				System.out.print(" <- ");System.out.print(LIST_NODE.get(j).get_posCrypt());
				
			}while(j!=indexStartNode);
			System.out.println();
		}
		print_addWay(node);

	}

	public void print_addWay(int node){
		int m;
		for (int k = 0; k < SWEET_INDEX.size(); k++) {
			if (MATRIX_SWEET[PREDECESSOR[node]][k] != 0) {// difference entre afficher un chemin et devoir l'utiliser. L'actualisation du
				m = SWEET_INDEX.get(k);
				System.out.print("    Add for sweet = ");System.out.print(LIST_NODE.get(m).get_posCrypt());
				do{
					m=MATRIX_NODESUPP[node][m];
					if(m>=0) {System.out.print(" <- ");System.out.print(LIST_NODE.get(m).get_posCrypt());}
					
			}while(m>=0);	
			}
		}
		System.out.println();
	}
}