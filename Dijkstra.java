import java.io.*;
import java.util.*;

public class Dijkstra {

	private String ELEM = "";
	private int SWEET = 3;
	private int EXIT = 5;
	private int[][] MATRIX_SWEET; // contient les bonbons ne faisant pas partie du parcours d'origine et utilisé pour un monstre dans celui ci
	private int[][] WAY_SUPP; 
	private int[][][] DATA_SWEET;
	private int NB_NODES;
	private int IN = 99999;
	private int[] VISITED;// permet de savoir les noeuds déjà visités
	private int[] VISITED_REAL; // permet de savoir les noeuds reellement visité par le parcours choisi.
	private	int[] PREDECESSOR; // garde en mémoire de quel noeud on est venu pour arriver en predecessor[i]
	private int[] PREDECESSOR_INIT;
	private int[] DISTANCE; // permet de connaitre la distance jusqu'a  un certain node.
	private int[] LIST_IN; // liste permettant d'initialisé toute les distances a IN.
	private int[] LIST_OUT; // liste ne contenant que des -1 indiquant que la recherche d'un predecessor est terminé.
	private ArrayList<Node> LIST_NODE;
	private	ArrayList<Integer> MULTI_NODE =new ArrayList<Integer>(); // list dont l'indice des nodes ayant plusieurs direction sont égale a 1
	private ArrayList<Integer> SWEET_INDEX =new ArrayList<Integer>();
	private HashMap<Node,Integer> INDEX_NODE = new HashMap<Node,Integer>();
	private int INDEX_EXIT;


	public Dijkstra(ArrayList<Node> listNode){ // Prints Mat_linkNode
		try{
			LIST_NODE = listNode;
			for (int i = 0;i < LIST_NODE.size() ;i++ ) {
				INDEX_NODE.put(listNode.get(i),i);
			}
			NB_NODES = LIST_NODE.size();
			VISITED = new int[NB_NODES]; 
			PREDECESSOR = new int[NB_NODES]; 

			createData_struc();
			dijkstra(0,EXIT);

			//print_allWay(DISTANCE,PREDECESSOR, 0);
			//System.out.println("-------------- EXIT --------------");
			print_way(DISTANCE,PREDECESSOR,INDEX_EXIT,0);


			ArrayList<Node> way = get_finalWay();
			
			for (int i = 0;i < way.size() ;i++ ) {
				System.out.print(way.get(i).get_posCrypt() + "|");
			}
		}catch(NullPointerException e){
			System.err.println("Caught NullPointerException in Dijkstra algorithm: " + e.getMessage());
		}
	}

//--------------------------------------------------------------------------------------------------------------//
	
	public int get_totalWeight(){
		int weight = IN;
		try{
			weight = DISTANCE[INDEX_EXIT];
		}catch (NullPointerException e) {
			System.err.println("Caught NullPointerException in get_totalWeight(): " + e.getMessage());
		}
		return weight;
		
	}

	public ArrayList<Node> get_finalWay(){
		ArrayList<Node> finalWay = new ArrayList<Node>();
		try{
			int indexOut =  INDEX_EXIT;
			int j = INDEX_EXIT;
			if (DISTANCE[INDEX_EXIT] == IN){
				int max= 0;
				for (int i = 0 ;i < NB_NODES ;i++ ) {
					System.out.print(DISTANCE[i] +"|");
					if (max< DISTANCE[i] && DISTANCE[i]  != IN) {
						j=indexOut=i;
						max=DISTANCE[i];
					}
				}
			}

			do{
				finalWay.add(0,LIST_NODE.get(j));
				j=PREDECESSOR[j];
				
			}while(j!=0);
			finalWay.add(0,LIST_NODE.get(j));
			int m;
			for (int k = 0; k < SWEET_INDEX.size(); k++) {

				
				if (MATRIX_SWEET[indexOut][k] != 0) {// difference entre afficher un chemin et devoir l'utiliser. L'actualisation du
					m = SWEET_INDEX.get(k);
					int dim1 = finalWay.size();
					//finalWay.add(LIST_NODE.get(m));

					do{
						finalWay.add(LIST_NODE.get(m));
						m=WAY_SUPP[indexOut][m];

					}while(m>=0);	
					int dim2 = finalWay.size();
					finalWay = sort_way(finalWay,dim2-dim1);
				}

			}
		}catch(NullPointerException e){
			System.err.println("Caught NullPointerException in Dijkstra.get_way() method: " + e.getMessage());
		}
		return finalWay;
	}

	public ArrayList<Node>  sort_way(ArrayList<Node> way, int size){
		int index_insert = way.indexOf(way.get(way.size()-1)) +1; // on veut inserer après le noeuds de connexion
		way.add(index_insert,way.get(way.size()-1));
		way.remove(way.size()-1);
		for (int i = 0; i < size-1; i++ ) {
			way.add(index_insert+i,way.get(way.size()-1));
			if (i+1<size-1) {
				way.add(index_insert+i+1,way.get(way.size()-1));
			}
			way.remove(way.size()-1);	
		}
		return way;
	}
	public void createData_struc(){

		WAY_SUPP = new int[NB_NODES][NB_NODES]; // matrice permettant pour chaque noeuds de garder en mémoire les chemins supplementaire existant pour récuperer des bonbons.
		LIST_IN = new int[NB_NODES]; 
		LIST_OUT = new int[NB_NODES];
		Arrays.fill(LIST_OUT,-1);
		
		for(int i = 0; i < NB_NODES; i++){
			LIST_IN[i] = IN;// permet de commencer par un test de chaque connexion car si il est directement connécté a un monstre, la distance qu'on devra ajouter pour la recuperation du bonbon sera plus grande que le chemin de base (qui est impossible)
		
			if (LIST_NODE.get(i).isSweet()) {SWEET_INDEX.add(i);}
			if (LIST_NODE.get(i).isExit()) {INDEX_EXIT = i;}
			WAY_SUPP[i] = LIST_OUT;
		}
		MATRIX_SWEET = new int[NB_NODES][SWEET_INDEX.size()]; // matrice permettant d'établir la liaison entre chaque noeud et les bonbons afin de savoir si ils ont été utilisé
	}

	public int weigthLink(int actu_node, int link_node){
		try{
			return LIST_NODE.get(actu_node).get_arc(LIST_NODE.get(link_node)).get_weight();
		}catch(NullPointerException e){
			return IN;
		}
	}

	// On choisit quel est le noeud connecté au noeud actuel avec le plus court chemin.
	public int[] find_lightWay(int[] distance, int[] visited,int[] nb_sweet, int valueToFind){
		int[] lightWay = new int[2];
		int min = IN;
		for (int i = 0; i < NB_NODES ;i++ ) {
			if( distance[i] < min && visited[i] != 1 && (!LIST_NODE.get(i).isUnidirectionnel() || LIST_NODE.get(i).get_nodeValue() == valueToFind) && (!LIST_NODE.get(i).isMonster() || nb_sweet[i] > 0))
			{ 
				// si le noeud n'a pas été visité et la distance entre les noeuds est < le min
			min = distance[i]; // min prend la distance entre les noeuds
				lightWay[1]= i; // on svg i comme étant le prochain noeud
			}			
		}
		lightWay[0]=min;
		return lightWay;
	}
//--------------------------------------------------------------------------------------------------------------//

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

	public int find_sweet(int actu_node, int monster){
		//System.out.println("|--|--|-- DIJKSTRA_SWEET");
		ELEM = "    ";

		MULTI_NODE.clear();
		VISITED_REAL = new int[NB_NODES];

		state_actuWay(actu_node);
		//System.out.println(ELEM + " MULTI_NODE: " +MULTI_NODE);
		for (int i=0; i< MULTI_NODE.size() ;i++ ) {
			dijkstra_sweet(monster,MULTI_NODE.get(i),i);
		}
		int min = choice_bestSweet(monster);
		ELEM = "";
		//System.out.println("|--|--|-- END_DIJKSTRA");
		if (min == IN) { return IN;}
		else{ return min*2;}
	}

	public int choice_bestSweet(int monster){
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
		//System.out.println(ELEM + monster + " Sweet: " + sweet + " Min: " + min);
		if (min != IN) { updateData_newSweet(monster, multi_node, sweet);}
		return min;
	}

	public void updateData_newSweet(int monster,int multi_node, int sweet){
		int l=sweet;
		int k =-1; // predecessor temporaire
		do{
			if (k != -1 && WAY_SUPP[monster][k] == -1 ) {WAY_SUPP[monster][k] = l;}
			k = l;
			//System.out.print(l + "<--");
			l=DATA_SWEET[multi_node][2][l];
			if (k == l) {break;}
		}while(l>=0);
		/*System.out.println();
		System.out.print(ELEM + " PREDECESSOR_SUPP: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +WAY_SUPP[monster][i]);
		}
		System.out.println();
		System.out.println(ELEM + SWEET_INDEX);*/
		MATRIX_SWEET[monster][SWEET_INDEX.indexOf(sweet)] = 1;

	}

	public boolean sweet_notUse(int monster, int sweet){
		//System.out.println(ELEM + "SWEET_NOTUSE");
		//System.out.println(ELEM + SWEET_INDEX);
		//System.out.println(ELEM + sweet + " " + !(MATRIX_SWEET[monster][SWEET_INDEX.indexOf(sweet)] == 1));
		if (MATRIX_SWEET[monster][SWEET_INDEX.indexOf(sweet)] == 1) {return false;}
		return true;
	}
	public void dijkstra_sweet (int monster, int start_node,int index){
		int[] distance = new int[NB_NODES]; // permet de connaitre la distance jusqu'a  un certain node.
		int[] nb_sweet = new int[NB_NODES]; // permet de savoir combien de bonbon le chemin jusqu'à cette node possède.
		int[] predecessor = LIST_OUT.clone();
		ArrayList<Node> ensLink;
		int min =0;
		int min_real;
		int link_node;
		int[] lightWay = new int[2];
		int actu_node = start_node;
		int next_node;

		// Algorithme préparé pour que PAKKUMAN soit en mat[0][0]
		distance = LIST_IN.clone(); // distance de node0 aux autres nodes initilisé a IN partout
		VISITED_REAL[actu_node] = 1; // on considère le premier node comme visité
		distance[actu_node] = 0; // car c'est le noeud de départ
		while(nb_sweet[actu_node] <=0 ){ // On s'arrete si la valeur de la node actuelle traité est celle qu'on cherche
			//System.out.println(ELEM + "Actu_node: " + LIST_NODE.get(actu_node).get_posCrypt());

			//print_state(predecessor,distance,VISITED_REAL);
			
			VISITED_REAL[actu_node] = 1;// On indique que le noeud a été visité

			//System.out.println(ELEM + "NB_SWEET: " + nb_sweet[actu_node]);
			ensLink = LIST_NODE.get(actu_node).get_ensLink();
			for(int i = 0; i < ensLink.size(); i++){ // au maximum 4
			// On regarde si la distance entre le "min" + la distance du "actu_node" est plus petite que la distance à partir du noeud de départ
				link_node = INDEX_NODE.get(ensLink.get(i));
				min_real = min+weigthLink(actu_node,link_node);
				if (WAY_SUPP[monster][link_node] != -1) {min_real = min;} // test si le chemin tester n'est pas délink_nodeà utilisé pour une précedente recherche de bonbon si oui il n'y a aucune distance supplémentaire a alink_nodeouter
					
				if( VISITED_REAL[link_node]!=1 && (min_real < distance[link_node])){// && LIST_NODE.get(actu_node).isLinkTo(LIST_NODE.get(link_node))){
					//System.out.println(ELEM + "Link_node: " + LIST_NODE.get(link_node).get_posCrypt());
					// si c'est le cas on indique la nouvelle distance entre le noeud de départ et le noeud 'link_node'
					if( !LIST_NODE.get(link_node).isMonster() || nb_sweet[actu_node] >0) {
						distance[link_node] = min_real;
						// on indique par quel noeud on est passé avant
						nb_sweet[link_node] = nb_sweet[actu_node];
						predecessor[link_node] = actu_node;
					}
				}
			}	

			lightWay = find_lightWay(distance,VISITED_REAL,nb_sweet,SWEET);
			min = lightWay[0]; next_node =lightWay[1];

			if (actu_node == next_node) {break;}

			//System.out.println(ELEM + "Next_node: " + LIST_NODE.get(next_node).get_posCrypt());

			//nb_sweet[next_node] = nb_sweet[predecessor[next_node]];
			if (LIST_NODE.get(next_node).isMonster()) {nb_sweet[next_node] -=1;}
			if (LIST_NODE.get(next_node).isSweet() && sweet_notUse(monster,next_node)) {nb_sweet[next_node] +=1;}
			actu_node = next_node;
		}
		DATA_SWEET[index][0] = nb_sweet;
		DATA_SWEET[index][1] = distance;
		DATA_SWEET[index][2] = predecessor;
	}

//--------------------------------------------------------------------------------------------------------------//

	public void dijkstra (int indexStartNode, int valueToFind){

		int[] lightWay;
		int[] nb_sweet = new int[NB_NODES]; // permet de savoir combien de bonbon le chemin jusqu'à cette node possède.
		ArrayList<Node> ensLink;

		int min = 0;
		int way_supp;
		int link_node;
		int next_node;
		int actu_node = indexStartNode;

		// Algorithme préparé pour que PAKKUMAN soit en mat[0][0]
		DISTANCE = LIST_IN.clone();
		VISITED[indexStartNode] = 1; // on considère le premier node comme visité
		DISTANCE[indexStartNode] = 0; // car c'est le noeud de départ

		while(LIST_NODE.get(actu_node).get_nodeValue() != valueToFind){ // On s'arrete si la valeur de la node actuelle traité est celle qu'on cherche
			//print_state(PREDECESSOR,DISTANCE,VISITED);
			//System.out.println("Actu_node: " + LIST_NODE.get(actu_node).get_posCrypt());
			
			VISITED[actu_node] = 1;
			// On va d'abord faire une recherche dans les distances avant de selectionné la plus courte afin d'appeler dijkstra_sweet si nécessaire.
			ensLink = LIST_NODE.get(actu_node).get_ensLink();
			for(int i = 0; i < ensLink.size(); i++){ // au maximum 4
				link_node = INDEX_NODE.get(ensLink.get(i));
				way_supp = 0;
				// On regarde si la distance entre le "min" + la distance du "next_node" est plus petite que la distance à partir du noeud de départ
				//test si le noeud na pas encore été visité, si le min actuelle additionné a la distance entre les deux noeuds est plus petit que la distance pour atteindre ce noeud déjà enregistré et si il existe une connexion entre les deux noeuds 
				if( VISITED[link_node]!=1 && (min+weigthLink(actu_node,link_node) <= DISTANCE[link_node])){//} && LIST_NODE.get(actu_node).isLinkTo(LIST_NODE.get(link_node))){ // test si 

					//System.out.println("Link_node: " + LIST_NODE.get(link_node).get_posCrypt() + " dist : " +(min+weigthLink(actu_node,link_node)) + " "+  DISTANCE[link_node]);
					
					// si c'est le cas on indique la nouvelle distance entre le noeud de départ et le noeud 'next_node'
					MATRIX_SWEET[link_node] = MATRIX_SWEET[actu_node].clone();
					WAY_SUPP[link_node] = WAY_SUPP[actu_node].clone();
					if(LIST_NODE.get(link_node).isMonster() && nb_sweet[actu_node] <=0) { 
						way_supp = find_sweet(actu_node, link_node);
						if (way_supp!=IN) {nb_sweet[actu_node]+=1;} //si way_supp est different de IN c'est qu'on a trouvé un bonbon
					}
					//System.out.println("Distance_linkNode: " + (min+way_supp+weigthLink(actu_node,link_node)));
					//System.out.println("Distance_record: " +DISTANCE[link_node]);

					if (min+way_supp+weigthLink(actu_node,link_node) < DISTANCE[link_node]) {
						DISTANCE[link_node] = min+weigthLink(actu_node,link_node)+way_supp;
						nb_sweet[link_node] = nb_sweet[actu_node];
						PREDECESSOR[link_node] = actu_node;
					}
					//System.out.println("Nb_sweet: " + nb_sweet[link_node] + " "+link_node);
				}
			}

			lightWay = find_lightWay(DISTANCE,VISITED,nb_sweet,valueToFind);
			min = lightWay[0]; next_node =lightWay[1];
			if (actu_node == next_node) {break;}
			//System.out.println("Next_node: " + LIST_NODE.get(next_node).get_posCrypt());

			// On indique que le noeud a été visité
			if (LIST_NODE.get(next_node).isMonster()) {nb_sweet[next_node] -=1;}
			if (LIST_NODE.get(next_node).isSweet()) {nb_sweet[next_node] +=1;}
			actu_node = next_node;
		}
	}

//--------------------------------------------------------------------------------------------------------------//

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
		if (distance[node]<IN) {System.out.print("Good_Way || ");}
		else {System.out.print("Wrong_way || ");}

		if(node!=indexStartNode){
			System.out.println("Weight = " + distance[node]);
			System.out.print("Path = ");System.out.print(LIST_NODE.get(node).get_posCrypt());
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
			if (MATRIX_SWEET[node][k] != 0) {// difference entre afficher un chemin et devoir l'utiliser. L'actualisation du
				m = SWEET_INDEX.get(k);
				System.out.print("    Add for sweet = ");System.out.print(LIST_NODE.get(m).get_posCrypt());
				do{
					m=WAY_SUPP[node][m];
					if(m>=0) {System.out.print(" <- ");System.out.print(LIST_NODE.get(m).get_posCrypt());}
					
			}while(m>=0);	
			}
		}
		System.out.println();
	}
}