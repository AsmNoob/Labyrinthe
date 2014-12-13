import java.io.*;
import java.util.*;

public class Dijkstra {

	private int SWEET = 3;
	private int EXIT = 5;
	private int INDEX_EXIT;
	private int[][] MATRIX_SWEET; // contient les bonbons ne faisant pas partie du parcours d'origine et utilisé pour un monstre dans celui ci
	private int[][] WAY_SUPP; //contient les predecesseur de chaque node potentiellement ajoutable a leur parcours pour trouver un bonbon
	private int[][][] DATA_SWEET;// Matrix reprenant pour chaque noeud multidirectionnel les listes predecessor/distance/nb_sweet
	private int NB_NODES; // nombre de noeuds actif
	private int IN = 99999; // valeur permettant de savoir qu'un chemin est soit non parcouru soit impossible
	private int[] VISITED;// permet de savoir les noeuds déjà visités
	private int[] VISITED_REAL; // permet de savoir les noeuds reellement visité par le parcours choisi.
	private	int[] PREDECESSOR; // garde en mémoire de quel noeud on est venu pour arriver en predecessor[i]
	private int[] DISTANCE; // permet de connaitre la distance jusqu'a  un certain node.
	private int[] LIST_IN; // liste permettant d'initialisé toute les distances a IN.
	private int[] LIST_OUT; // liste ne contenant que des -1 indiquant que la recherche d'un predecessor est terminé.
	private ArrayList<Node> LIST_NODE; // liste contenant les nodes créées du graph
	private	ArrayList<Integer> MULTI_NODE =new ArrayList<Integer>(); // liste contenant les nodes multidirectionnelles propre au chemin choisit
	private ArrayList<Integer> SWEET_INDEX =new ArrayList<Integer>(); // liste contenant l'index, dans LIST_NODE, de la position des noeuds bonbons
	private HashMap<Node,Integer> INDEX_NODE = new HashMap<Node,Integer>(); // dictionnaire permettant d'éviter le procecus de recherche dans LIST_NODE quand on veut recuperer l'indice de la node dans celle-ci
	
	//-----------------------------------Constructeur-------------------------------------//
	public Dijkstra(ArrayList<Node> listNode){ // Prints Mat_linkNode
		try{
			LIST_NODE = listNode;
			

			createData_struc();
			dijkstra(0);
			print_way(DISTANCE,PREDECESSOR,INDEX_EXIT,0);

		}catch(NullPointerException e){
			System.err.println("Caught NullPointerException in Dijkstra algorithm: " + e.getMessage());
		}
	}
	//fonction permettant de construire les structures nécessaire a DIJKSTRA
	public void createData_struc(){

		NB_NODES = LIST_NODE.size();
		VISITED = new int[NB_NODES]; 
		PREDECESSOR = new int[NB_NODES]; 
		WAY_SUPP = new int[NB_NODES][NB_NODES];
		LIST_IN = new int[NB_NODES]; 
		LIST_OUT = new int[NB_NODES];
		Arrays.fill(LIST_OUT,-1);
		
		for(int i = 0; i < NB_NODES; i++){
			INDEX_NODE.put(LIST_NODE.get(i),i);
			LIST_IN[i] = IN;// permet de commencer par un test de chaque connexion car si il est directement connécté a un monstre, la distance qu'on devra ajouter pour la recuperation du bonbon sera plus grande que le chemin de base (qui est impossible)
		
			if (LIST_NODE.get(i).isSweet()) {SWEET_INDEX.add(i);}
			if (LIST_NODE.get(i).isExit()) {INDEX_EXIT = i;}
			WAY_SUPP[i] = LIST_OUT;// on initialise chaque element a -1 puisque 0 (node d'entrée) n'est pas forcement la node d'entre pour la recherche de bonbon
		}
		MATRIX_SWEET = new int[NB_NODES][SWEET_INDEX.size()]; // matrice permettant d'établir la liaison entre chaque noeud et les bonbons afin de savoir si ils ont été utilisé
	}

	//-----------------------------------Both Dijkstra-------------------------------------//
	//permet d'obtenir la taille de l'arc entre deux nodes, ou IN si il n'y a pas d'arc
	//fonction utilisée uniquement si la liaison existe
	public int weigthLink(int actu_node, int link_node){
		return LIST_NODE.get(actu_node).get_arc(LIST_NODE.get(link_node)).get_weight();
	}

	// On choisit quel est le noeud connecté au noeud actuel avec le plus court chemin.
	public int[] find_lightWay(int[] distance, int[] visited,int[] nb_sweet, int valueToFind){
		int[] lightWay = new int[2];
		int min = IN;
		for (int i = 0; i < NB_NODES ;i++ ) {
			//derniere condition de ce if signifie que si c'est un monstre le chemin doit contenir au moins 1 bonbon a donné
			if( distance[i] < min && visited[i] != 1 && (!LIST_NODE.get(i).isUnidirectionnel() || LIST_NODE.get(i).get_nodeValue() == valueToFind) && (!LIST_NODE.get(i).isMonster() || nb_sweet[i] > 0)){ 
				min = distance[i]; // min prend la distance entre les noeuds
				lightWay[1]= i; // on svg i comme étant le prochain noeud
			}			
		}
		lightWay[0]=min;
		return lightWay;
	}
	//-----------------------------------Dijkstra Sweet-------------------------------------//
	// fonction permettant de mettre a jour les listes servant a la recherche d'un bonbon
	public void state_actuWay(int actu_node){

		int j = actu_node;
		do{
			if (LIST_NODE.get(j).isMultidirectionnel()) {MULTI_NODE.add(j);}
			if (j>0){
				j=PREDECESSOR[j];
				VISITED_REAL[j] = 1;
			}
			else {j= -1;}
		}while(j>=0); //0 est l'indice zero dans LIST_NODE qui correspond au pakkuman
		DATA_SWEET = new int[MULTI_NODE.size()][3][NB_NODES];
	}

	//fonction appelé pour trouvé un bonbon dans son parcourt et de lui renvoyer la distance a ajouté
	public int find_sweet(int actu_node, int monster){

		MULTI_NODE.clear(); // liste ne contenants que les nodes multidirectionnels propre au chemin parcouru actuellement le plus court.
		VISITED_REAL = new int[NB_NODES];

		state_actuWay(actu_node);
		//on fait un appel a dijkstra concu uniquement pour la recherche de bonbon pour chaque nodes multidirectionnelles trouvées
		for (int i=0; i< MULTI_NODE.size() ;i++ ) {
			dijkstra_sweet(monster,MULTI_NODE.get(i),i);
		}
		int min = choice_bestSweet(monster);

		if (min == IN) { return IN;}
		else{ return min*2;} //fois deux car le chemin doit etre considéré comme un aller/retour
	}
	// fonction permettant de comparé les differents chemin trouvé via dijkstra_sweet
	public int choice_bestSweet(int monster){
		int min = IN;
		int sweet = 0;
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
		// si un chemin est trouvé on appel la methode suivante
		if (min != IN) { updateData_newSweet(monster, multi_node, sweet);}
		return min;
	}

	// fonction permettant de mettre a jour WAY_SUPP et MATRIX_SWEET
	public void updateData_newSweet(int monster,int multi_node, int sweet){
		int l=sweet;
		int k =-1; // predecessor temporaire
		do{
			//tout d'abord way_supp contiendra le chemin supplémentaire pour atteindre le bon 
			//stocké dans l'indice du monstre car c'est lui qui a besoin d'un bonbon
			if (k != -1 && WAY_SUPP[monster][k] == -1 ) {WAY_SUPP[monster][k] = l;}
			k = l;
			//System.out.print(l + "<--");
			l=DATA_SWEET[multi_node][2][l];
			if (k == l) {break;}
		}while(l>=0);
		//on indique quel monstre est associé a quel bonbon
		MATRIX_SWEET[monster][SWEET_INDEX.indexOf(sweet)] = 1;

	}
	//fonction permettant de savoir si le bonbon est déjà associé au monstre 
	public boolean sweet_notUse(int monster, int sweet){
		if (MATRIX_SWEET[monster][SWEET_INDEX.indexOf(sweet)] == 1) {return false;}
		return true;
	}
	// recherche du bonbon le plus proche au chemin 
	public void dijkstra_sweet (int monster, int start_node,int index){
		int[] distance = new int[NB_NODES]; // permet de connaitre la distance jusqu'a  un certain node.
		int[] nb_sweet = new int[NB_NODES]; // permet de savoir combien de bonbon le chemin jusqu'à cette node possède.
		int[] predecessor = LIST_OUT.clone(); // initialisant la liste a -1
		ArrayList<Node> ensLink;
		int min =0;
		int min_real;
		int link_node;
		int[] lightWay = new int[2];
		int actu_node = start_node;
		int next_node;

		distance = LIST_IN.clone(); // distance de node0 aux autres nodes initilisé a IN partout
		VISITED_REAL[actu_node] = 1; // on considère le premier node comme visité
		distance[actu_node] = 0; // car c'est le noeud de départ
		while(nb_sweet[actu_node] <=0 ){ // On s'arrete si la valeur de la node actuelle traité est celle qu'on cherche			
			VISITED_REAL[actu_node] = 1;// On indique que le noeud a été visité

			ensLink = LIST_NODE.get(actu_node).get_ensLink();
			for(int i = 0; i < ensLink.size(); i++){ // au maximum 4
			// On regarde si la distance entre le "min" + la distance du "actu_node" est plus petite que la distance à partir du noeud de départ
				link_node = INDEX_NODE.get(ensLink.get(i));
				min_real = min+weigthLink(actu_node,link_node);
				// test si le chemin tester n'est pas déjà utilisé pour une précedente recherche de bonbon si oui il n'y a aucune distance supplémentaire a ajouter
				if (WAY_SUPP[monster][link_node] != -1) {min_real = min;} 
					
				if( VISITED_REAL[link_node]!=1 && (min_real < distance[link_node])){// && LIST_NODE.get(actu_node).isLinkTo(LIST_NODE.get(link_node))){
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

			// si c'est le cas c'est qu'on a pas trouvé distance plus courte on est donc au bout du chemin
			if (actu_node == next_node) {break;}
			if (LIST_NODE.get(next_node).isMonster()) {nb_sweet[next_node] -=1;}
			if (LIST_NODE.get(next_node).isSweet() && sweet_notUse(monster,next_node)) {nb_sweet[next_node] +=1;}
			actu_node = next_node;
		}
		DATA_SWEET[index][0] = nb_sweet;
		DATA_SWEET[index][1] = distance;
		DATA_SWEET[index][2] = predecessor;
	}

	//-----------------------------------Dijkstra-------------------------------------//
	// recherche du chemin le plus court jusqu'a la sortie si c'est possible
	public void dijkstra (int indexStartNode){

		int[] lightWay;
		int[] nb_sweet = new int[NB_NODES]; // permet de savoir combien de bonbon le chemin jusqu'à cette node possède.
		ArrayList<Node> ensLink;

		int min = 0;
		int way_supp;
		int link_node;
		int next_node;
		int actu_node = indexStartNode;

		DISTANCE = LIST_IN.clone();
		DISTANCE[indexStartNode] = 0; // car c'est le noeud de départ

		while(LIST_NODE.get(actu_node).get_nodeValue() != EXIT){ // On s'arrete si la valeur de la node actuelle traité est celle qu'on cherche
			// On indique que le noeud a été visité
			VISITED[actu_node] = 1;
			// On va d'abord faire une recherche dans les distances avant de selectionné la plus courte afin d'appeler dijkstra_sweet si nécessaire.
			ensLink = LIST_NODE.get(actu_node).get_ensLink();
			for(int i = 0; i < ensLink.size(); i++){ // au maximum 4
				link_node = INDEX_NODE.get(ensLink.get(i));
				way_supp = 0;
				// On regarde si la distance entre le "min" + la distance du "next_node" est plus petite que la distance à partir du noeud de départ
				//test si le noeud na pas encore été visité, si le min actuelle additionné a la distance entre les deux noeuds est plus petit que la distance pour atteindre ce noeud déjà enregistré et si il existe une connexion entre les deux noeuds 
				if( VISITED[link_node]!=1 && (min+weigthLink(actu_node,link_node) <= DISTANCE[link_node])){ // si c'est le cas on indique la nouvelle distance entre le noeud de départ et le noeud 'next_node'
					// on clone les données du predecessor dans le noeuds connecté
					MATRIX_SWEET[link_node] = MATRIX_SWEET[actu_node].clone();
					WAY_SUPP[link_node] = WAY_SUPP[actu_node].clone();

					//appel a dijkstra_sweet si necessaire
					if(LIST_NODE.get(link_node).isMonster() && nb_sweet[actu_node] <=0) { 
						way_supp = find_sweet(actu_node, link_node);
						if (way_supp!=IN) {nb_sweet[actu_node]+=1;} //si way_supp est different de IN c'est qu'on a trouvé un bonbon
					}
					// mise a jour des données si c'est toujours d'actualité après dijkstra_sweet
					if (min+way_supp+weigthLink(actu_node,link_node) < DISTANCE[link_node]) {
						DISTANCE[link_node] = min+weigthLink(actu_node,link_node)+way_supp;
						nb_sweet[link_node] = nb_sweet[actu_node];
						PREDECESSOR[link_node] = actu_node;
					}
				}
			}
			// recherche de la distance la plus courte.
			lightWay = find_lightWay(DISTANCE,VISITED,nb_sweet,EXIT);
			min = lightWay[0]; next_node =lightWay[1];
			// si c'est le cas c'est qu'on a pas trouvé distance plus courte on est donc au bout du chemin
			if (actu_node == next_node) {break;}
			//on met a jour nb_sweet permettant ainsi de savoir a tout moment le nombre de bonbon present dans le chemin 
			if (LIST_NODE.get(next_node).isMonster()) {nb_sweet[next_node] -=1;}
			if (LIST_NODE.get(next_node).isSweet()) {nb_sweet[next_node] +=1;}
			actu_node = next_node;
		}
	}
	//-----------------------------------Get way-------------------------------------//
	// fonction permettant de savoir si il existe un chemin possible ou non
	public int get_totalWeight(){
		try{
			return DISTANCE[INDEX_EXIT];
		}catch (NullPointerException e) {
			System.err.println("Caught NullPointerException in get_totalWeight(): " + e.getMessage());
		}
		return IN;
	}
	// fonction permettant de trouvé une liaison direct entre pakkuman et un monstre 
	// au cas il n'existe pas de chemin jusqu'a la sortie
	public int find_monster(){
		Node pakkuman = LIST_NODE.get(0);
		for (int i = 1; i < NB_NODES; i++){
			if (pakkuman.isLinkTo(LIST_NODE.get(i)) && LIST_NODE.get(i).isMonster()) {
				return i;
			}
		}
		return 0;
	}
	// fonction permettant de trouvé le chemin le plus long possible si il n'y a pas de connexion
	// direct avec un monstre
	public int find_longestWay(){
		int max= 0;
		int indexOut= 0;
			for (int i = 1 ;i < NB_NODES ;i++ ) {
				//System.out.print(DISTANCE[i] +"|");
				if (max< DISTANCE[i] && DISTANCE[i]  != IN) {
					indexOut=i;
					max=DISTANCE[i];
				}
			}
		return indexOut;
	}
	// fonction retournant la liste des nodes a parcourir pour obtenir un chemin
	public ArrayList<Node> get_regularWay(int indexOut, ArrayList<Node> finalWay ){
		int j = indexOut;
		do{
			finalWay.add(0,LIST_NODE.get(j));
			j=PREDECESSOR[j];		
		}while(j!=0);
		finalWay.add(0,LIST_NODE.get(j));
		
		//deuxieme boucle va parcourir MATRIX_SWEET à l'indice de sortie pour savoir quel noeud il faut
		//ajouter pour completer le parcours
		int m;
		for (int k = 0; k < SWEET_INDEX.size(); k++) {
			if (MATRIX_SWEET[indexOut][k] != 0) {// si c'est le cas c'est que le bonbon a été utilisé 
				m = SWEET_INDEX.get(k);
				int dim1 = finalWay.size();
				boolean notFind_link = true;
				//on ajoute alors a la fin de finalWay les noeuds a ajouté
				do{
					if (notFind_link) {
						if (finalWay.contains(LIST_NODE.get(m))) { notFind_link = false;}
						finalWay.add(LIST_NODE.get(m));
						m=WAY_SUPP[indexOut][m];
					}
					else {m=-1;}
				}while(m>=0);
				//on va ensuite traité les derniers elements ajouté pour les placé aux bon endroits
				int dim2 = finalWay.size();
				if (dim2-dim1 > 1) {
					finalWay = sort_way(finalWay,dim2-dim1);	
				}
				else{finalWay.remove(finalWay.size()-1);}	
			}
		}
		return finalWay;
	}
	//fonction permettant de traiter l'ajout de node au parcours final
	public ArrayList<Node> sort_way(ArrayList<Node> finalWay, int size){
		int index_insert = finalWay.indexOf(finalWay.get(finalWay.size()-1)) +1; // on veut inserer après le noeuds de connexion
		finalWay.add(index_insert,finalWay.get(finalWay.size()-1));
		finalWay.remove(finalWay.size()-1);
		// on parcours la fin de la liste ajoutant a l'index_insert incrementer de i
		for (int i = 0; i < size-1; i++ ) {
			finalWay.add(index_insert+i,finalWay.get(finalWay.size()-1));
			//double ajout si ce n'est pas la derniere node a ajouté
			if (i+1<size-1) {
				finalWay.add(index_insert+i+1,finalWay.get(finalWay.size()-1));
			}
			finalWay.remove(finalWay.size()-1);	
		}
		return finalWay;
	}
	// fonction permettant d'obtenir un chemin dans tout les cas: soit jusqu'a une sortie, soit a un monstre soit une chemin long
	public ArrayList<Node> get_finalWay(){
		ArrayList<Node> finalWay = new ArrayList<Node>();
		boolean call_monster = false;
		try{
			int indexOut = INDEX_EXIT;
			if (DISTANCE[INDEX_EXIT] == IN){ 
				indexOut= find_longestWay();
				if (indexOut == 0) {
					call_monster = true;
					indexOut = find_monster();
				}	
			}
			if(!call_monster){ finalWay=get_regularWay(indexOut,finalWay);}
			else{ finalWay.add(LIST_NODE.get(0));finalWay.add(LIST_NODE.get(indexOut));}
			
		}catch(NullPointerException e){
			System.err.println("Caught NullPointerException in Dijkstra.get_way() method: " + e.getMessage());
		}
		return finalWay;
	}


	//-----------------------------------Print-------------------------------------//
	//affiche l'état des structures de sauvegarde
	public void print_state(int[] predecessor, int[] distance, int[] visited){
		System.out.print("|--|--|-- Indice node: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" + i+ "=" +LIST_NODE.get(i).get_posCrypt());
		}
		System.out.println();
		System.out.print("|--|--|-- Distance: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +distance[i]);
		}
		System.out.println();
		
		System.out.print("|--|--|-- Predecessor: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +predecessor[i]);
		}
		System.out.println();
		System.out.print("|--|--|-- Visited: ");
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +visited[i]);
		}
		System.out.println();
	}

	// On va donc récupérer tous les chemins les plus court en partant d'indexStartNode
	public void print_allWay(int[] distance, int[] predecessor, int indexStartNode){

		for(int i = 0; i < NB_NODES; i++){
			print_way(distance,predecessor,i,indexStartNode);
		}
	}
	// Affichage des noeuds présent sur le chemin d'indexStartNode et indexOut
	public void print_way(int[] distance, int[] predecessor, int indexOut, int indexStartNode){
		int j;
		if (distance[indexOut]<IN) {System.out.print("Good_Way || ");}
		else {System.out.print("Wrong_way || ");}

		if(indexOut!=indexStartNode){
			System.out.println("Weight = " + distance[indexOut]);
			System.out.print("Path = ");System.out.print(LIST_NODE.get(indexOut).get_posCrypt());
			j = indexOut;
			do{
				j=predecessor[j];
				System.out.print(" <- ");System.out.print(LIST_NODE.get(j).get_posCrypt());
				
			}while(j!=indexStartNode);
			System.out.println();
		}
		print_addWay(indexOut);
	}
	// affichage du chemin supplémentaire pour récupéré des bonbons hors parcours 
	public void print_addWay(int indexOut){
		int m;
		for (int k = 0; k < SWEET_INDEX.size(); k++) {
			if (MATRIX_SWEET[indexOut][k] != 0) {// difference entre afficher un chemin et devoir l'utiliser. L'actualisation du
				m = SWEET_INDEX.get(k);
				System.out.print("    Add for sweet = ");System.out.print(LIST_NODE.get(m).get_posCrypt());
				do{
					m=WAY_SUPP[indexOut][m];
					if(m>=0) {System.out.print(" <- ");System.out.print(LIST_NODE.get(m).get_posCrypt());}
					
			}while(m>=0);	
			}
		}
		System.out.println();
	}
}