
import java.io.*;
import java.util.*;

//Constructeur de la class Node, il faut sa position dans la matrice, et quatre donnée correspondant a UP,DOWN,LEFT,RIGHT initialisé a null dont les valeurs changeront lors de la création de celle ci, permettant de savoir plus tard dans quel direction aller directement. Chaque direction sera un pointeur vers le sommet qui est au bout de son chemin. Puisque que nos sommet sont principalement des intersections cela nous permettera de choisir directement le bon chemin .

public class Graph {
	//Attribut
	private int DIM_LINE; //dimention de la matrice 
	private int DIM_COLUMN; //dimention de la matrice
	private int LINE_SIZE; //dimention de la matrice 
	private int COLUMN_SIZE; //dimention de la matrice
	private HashMap<Integer,Node> ENS_NODE = new HashMap<Integer,Node>();
	private	ArrayList<Node> LIST_NODE;
	private int[] DIRECTION = new int[] {-1,1,0,0};
	private int DIRECTION_SIZE = DIRECTION.length-1;
	private int NB_NODES;
	private int iterrator = 0;
	private int optimisation = 0;
	private int pakkumanPos_crypt;
	private int exitPos_crypt;
	// #define
	
	private int IN = 9999; 

	// constructeur
	public Graph(int[][] mat, int[] pakkumanCoord){
		DIM_LINE = mat.length;
		DIM_COLUMN = mat[0].length;
		LINE_SIZE = (int) Math.pow(10,Math.floor(Math.log10(DIM_LINE))+1);
		COLUMN_SIZE = (int) Math.pow(10,Math.floor(Math.log10(DIM_COLUMN))+1);
		pakkumanPos_crypt = pos_cryptage(pakkumanCoord[0]*2+1,pakkumanCoord[1]*2+1);

		// Test du temps d'execution du 

		long begin = System.currentTimeMillis();
		create_graph(mat,pakkumanPos_crypt,pakkumanPos_crypt, true, null); 
		NB_NODES = ENS_NODE.size();
		LIST_NODE = new ArrayList<Node>(ENS_NODE.values());
		//fais un switch entre la node Pakkuman et la node en premiere position 
		LIST_NODE.set(LIST_NODE.indexOf(ENS_NODE.get(pakkumanPos_crypt)),LIST_NODE.set(0,ENS_NODE.get(pakkumanPos_crypt)));
    	
    	long step1 = System.currentTimeMillis();
		graph_converter();
    	long step2 = System.currentTimeMillis();
		float time1 = ((float) (step1-begin)) / 1000f;
		float time2 = ((float) (step2-step1)) / 1000f;
		//print_graph();
		System.out.print("Time exe || create_graph : ");
		System.out.print(time1);
		System.out.print(" || graph_converter : ");
		System.out.println(time2);
		System.out.print("Total Time execution: ");
		System.out.println(time1+time2);

		System.out.print("Optimisation || nb node : ");
		System.out.print(ENS_NODE.size()+optimisation);
		System.out.print(" to ");
		System.out.println(ENS_NODE.size());

	}


	//------------Getter/Setter----------//

	public HashMap<Integer,Node> get_ensNode(){
		return ENS_NODE;
	}

	// creer une node et la place dans la list de stockage
	// temps d'exe = 0
	public Node create_node(int elem, int pos_crypt){
		Node current_node = new Node(pos_crypt, elem);
		ENS_NODE.put(pos_crypt,current_node);
		return current_node;
	}
	// selectionne la noeud courrant -déjà existant ou non-.
	public Node select_currentNode(int elem, int pos_crypt){
		Node current_node = null;
		if (!ENS_NODE.containsKey(pos_crypt)) {
			current_node = create_node(elem, pos_crypt);
		}
		else{
			current_node = ENS_NODE.get(pos_crypt);
		}
		return current_node;
	}

	public void update_nodeLink(Arc current_arc){
		current_arc.get_startNode().add_link(current_arc.get_endNode(),current_arc);
		current_arc.get_endNode().add_link(current_arc.get_startNode(),current_arc);
	}
	public Arc start_Arc(int pos_crypt, Node start_node){
		Arc current_arc = new Arc();
		current_arc.add_way(pos_crypt);
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
	public int check_nextPosition(int[][] mat, int pos_crypt, int prePos_crypt, int line_add, int column_add, boolean test_twoCase){
		//System.out.print(" actuLine: " );System.out.print(actuLine);System.out.print(" actuColumn: " );System.out.print(actuColumn);System.out.print(" preLine: " );System.out.print(preLine);System.out.print(" preColumn: " );System.out.print(preColumn);System.out.print(" line_add: " );System.out.print(line_add);System.out.print(" column_add: " );System.out.println(column_add);
		int newPos_crypt = modif_posCrypt(pos_crypt,line_add,column_add);
		int[] newPositions = pos_decryptage(newPos_crypt);

		//si on est dans le cas ou l'element vaut la sortie il ne faut pas faire d'appel récursif puisque pour revenir dans les positions courrante et non les murs
		if (valueMat(mat, pos_crypt) == 4) {test_twoCase = true;}
		if (( (newPositions[0] >= 0) && (newPositions[0] < DIM_LINE) && (newPositions[1]>= 0) && (newPositions[1] < DIM_COLUMN) && (newPos_crypt != prePos_crypt)) || (pos_crypt == prePos_crypt)) {
			int elem = mat[newPositions[0]][newPositions[1]];
			// mur = -1
			if (elem == 4) {return 1;}
			if (elem != -1) {
				if (!test_twoCase){return check_nextPosition(mat,newPos_crypt,prePos_crypt, line_add,column_add,true);}
				else {return 2;}
			}
		}
		return 0;
	}

	//affichage des données récuperer après analyse du labyrinthe. 
	public void print_graph(){
		int size_dict = LIST_NODE.size();
		for (int i = 0; i < size_dict; i++ ) {
			LIST_NODE.get(i).print();
		}
	}
	public void graph_converter(){
		try{
			PrintWriter writer = new PrintWriter ("Graph.dot");
			writer.println("graph chemin {");
			writer.println();
			ArrayList<String> list_arcsPrinted = new ArrayList<String>();
			
			for(int i = 0; i < LIST_NODE.size(); i++){

				for(int j = 0; j < LIST_NODE.get(i).get_ensLink().size();j++){ // parcourt les noeuds lies
					if(!(list_arcsPrinted.contains(Integer.toString(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt())+Integer.toString(LIST_NODE.get(i).get_posCrypt())))){
						writer.print("	");
						writer.print(LIST_NODE.get(i).get_posCrypt());
						writer.print(" -- ");
						writer.print(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt());
						writer.print(" [label=");
						writer.print(LIST_NODE.get(i).get_ensArc().get(j).get_weight());
						writer.println("]");
						String arcPrinted =  Integer.toString(LIST_NODE.get(i).get_posCrypt())+ Integer.toString(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt());
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
		int pos = (LINE_SIZE*COLUMN_SIZE)+(i*LINE_SIZE)+j;
		return pos;
	}
	public int modif_posCrypt(int pos_crypt, int addLine, int addColumn){
		return pos_cryptage(pos_decryptage(pos_crypt)[0]+addLine,pos_decryptage(pos_crypt)[1]+addColumn);
	}

	// decryptage de la position 
	public int[] pos_decryptage( int pos_crypt){
		int[] position= new int[] {(pos_crypt-LINE_SIZE*COLUMN_SIZE-(pos_crypt%COLUMN_SIZE))/COLUMN_SIZE,pos_crypt %COLUMN_SIZE};
		return position; 
	}

	// detect si la position actuelle est une node et renvois une liste d'entier contenant les directions possible suivante.
	public int[] detect_isNode(int[][] mat, int pos_crypt, int prePos_crypt){
		int j = DIRECTION_SIZE;
		int[] data_direction = new int[5];// 4 premier entier -0,1- correspond au direction possible ou non le 5eme si il est >1 nous indiques que c'est une node.
		for (int i = 0; i<= DIRECTION_SIZE; i++){
			int test = check_nextPosition(mat, pos_crypt, prePos_crypt,DIRECTION[i],DIRECTION[j],false);

			data_direction[i] = test;
			if (test>0){data_direction[DIRECTION_SIZE+1]+=1;}
			j--;
		}
		
		/*System.out.print("Detect_isNode || ");
		System.out.println(Arrays.toString(data_direction));
		*/
		return data_direction;
	}


	public int[][] createMat_linkNode(){
		Node actu_node;
		int[][] matrix = new int[NB_NODES][NB_NODES]; // utilisé pou le liens entre les noeuds
		for(int i = 0; i < NB_NODES; i++){
			actu_node = LIST_NODE.get(i); // Pas cool, tu initialises un noeud par constructeur de copie au lieu d'utiliser une bête référence
			for(int j = i; j < NB_NODES;j++){
				// MOCHE !!!!!!
				try{
					matrix[i][j] = matrix[j][i] = actu_node.get_arc(LIST_NODE.get(j)).get_weight();
				}catch(NullPointerException e){
					matrix[i][j] = matrix[j][i] = IN;
				}
			}
		}

		return matrix;
	}


	public void run_dikstra(){ // Prints Mat_linkNode
		int[][] matrix = createMat_linkNode();
		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +LIST_NODE.get(i).get_posCrypt());
		}
		System.out.println();

	}
	
	// On choisit quel est le noeud connecté au noeud actuel avec le plus court chemin.
	public int[] find_lightWay(int[] distance, int[] visited, int valueToFind){
		int[] lightWay = int[2];
		int min = 9999; //IN;
		int next_node;
		Node test_node;

		for (int i = 0; i < NB_NODES ;i++ ) {
			//System.out.println("ActuNode: " +LIST_NODE.get(actu_node).get_posCrypt()+ " Test_link: " + LIST_NODE.get(i).get_posCrypt() + " state: " + visited[i] + " is_link: " +LIST_NODE.get(actu_node).isLinkTo(LIST_NODE.get(i)));
 			//LIST_NODE.get(i).print();
			test_node =LIST_NODE.get(next_node);
			if( min > distance[i] && visited[i] != 1 && (!test_node.isUnidirectionnel() || test_node.get_nodeValue() == valueToFind))
			{ 
				//System.out.println("Short_link: " + LIST_NODE.get(i).get_posCrypt());
				// si le noeud n'a pas été visité et la distance entre les noeuds est < le min
				min = distance[i]; // min prend la distance entre les noeuds
				next_node= i; // on svg i comme étant le prochain noeud
			}			
		}
		lightWay[0]=min;lightWay[1]=next_node;
		return lightWay;
	}

	//verifie si dans le parcous actuelle ne se trouve pas déjà un bonbon
	public boolean check_actuWay(int[] predecessor_ref, int actu_node){
		int j = actu_node;
		do{
			j=predecessor_ref[j];
			// si on trouve un bonbon avant de trouver un monstre dans le parcours de reference on renvois true
			if (LIST_NODE.get(j).isSweet()) {return true;}
			if (LIST_NODE.get(j).isMonster()) {return false;}

		}while(j!=0); //0 est l'indice zero dans LIST_NODE qui correspond au pakkuman

		// Attention si tu trouves ni Bonbon ni Monstre => pas de return renvoyé.
		return false;
	}

	// parcous le chemin de reference déjà parcouru et crée une list contenant les nodes déjà parcouru et pas l'ensemble des nodes testé
	public int[] create_visited(int[] predecessor_ref, int actu_node){
		int j = actu_node;
		int[] visited = new int[NB_NODES];
		do{
			visited[j] = 1;
			j=predecessor_ref[j];
			// si on trouve un bonbon avant de trouver un monstre dans le parcours de reference on renvois true
		}while(j!=0); //0 est l'indice zero dans LIST_NODE qui correspond au pakkuman
		return visited;
	}

	public int[] find_sweet(int[][] matrix, int[] predecessor_ref, int[] multidirectionnel, int actu_node){

		int[] distance = new int[NB_NODES]; // permet de connaitre la distance jusqu'a un certain noeud
		int[] predecessor = new int[NB_NODES+2]; // garde en mémoire de quel noeud on est venu pour arriver en predecessor[i] || deux element de plus indiquant le poids de l'arc supplementaire, au maximum un parcours total du graph (impossible),et la valeur du noeud sur lequel il y a une deviation
		if(check_actuWay(predecessor_ref,actu_node)){return predecessor;}
		int[] visited = create_visited(predecessor_ref,actu_node);

		int next_node;
		int[] lightWay = new int[2];

		for (int k = 0; k < LIST_NODE ; k++) {
			distance = matrix[k]; // distance de node0 aux autres nodes
			distance[k] = 0; // car c'est le noeud de départ
		int[] shortWay = int[2];

		// indexStartNode pas défini et je sais pas trop comment tu voulais l'initialiser
		distance = matrix[indexStartNode]; // distance de node0 aux autres nodes
		visited[indexStartNode] = 1; // on considère le premier node comme visité
		distance[indexStartNode] = 0; // car c'est le noeud de départ

		for (int k = 0; k < NB_NODES ; k++) {

			next_node = k;
			while(multidirectionnel[k] ==1 && !LIST_NODE.get(next_node).isSweet()){
				print_state(predecessor,distance);

				lightWay = find_lightWay(distance,visited,3); //3 pour bonbon
				min=lightWay[0]; next_node =lightWay[1];

				System.out.println("Next_node: " + LIST_NODE.get(next_node).get_posCrypt());

				// On indique que le noeud a été visité
				visited[next_node] = 1;
			
				for(int j = 0; j < NB_NODES; j++){
				
					// On regarde si la distance entre le "min" + la distance du "next_node" est plus petite que la distance à partir du noeud de départ
					if( visited[j]!=1 && (min+matrix[next_node][j] < distance[j]) && !LIST_NODE.get(j).isMonster()){
						System.out.println("Link_node: " + LIST_NODE.get(j).get_posCrypt());
						// si c'est le cas on indique la nouvelle distance entre le noeud de départ et le noeud 'j'
						distance[j] = min+matrix[next_node][j];
						// on indique par quel noeud on est passé avant
						predecessor[j] = next_node;
					}
				}
			}
		}
		print_way(distance,indexStartNode);
	}

	public void dijkstra (int[][] matrix, int indexStartNode, int valueToFind){

		int[] distance = new int[NB_NODES]; // permet de connaitre la distance jusqu'a  un certain noeud
		int[] visited = new int[NB_NODES]; // permet de savoir les noeuds déjà visités
		int[] predecessor = new int[NB_NODES]; // garde en mémoire de quel noeud on est venu pour arriver en predecessor[i]
		int[] multidirectionnel = new int[NB_NODES]; // 1 pour l'indice des nodes multidirectionnel rencontré.
		//int exitPos = LIST_NODE.indexOf(ENS_NODE.get(exitPos_crypt));// permet de recuperer la place de la node de sortie dans la LIST_NODE

		int min;
		int[] lightWay = new int[2];
		int next_node = indexStartNode;

		// Algorithme préparé pour que PAKKUMAN soit en mat[0][0]

		distance = matrix[indexStartNode]; // distance de node0 aux autres nodes
		visited[indexStartNode] = 1; // on considère le premier node comme visité
		distance[indexStartNode] = 0; // car c'est le noeud de départ

		while(LIST_NODE.get(next_node).get_nodeValue() != valueToFind){ // On s'arrete si la valeur de la node actuelle traité est celle qu'on cherche

			print_state(predecessor,distance);

			lightWay = find_lightWay(distance,visited,valueToFind);
			min=lightWay[0]; next_node =lightWay[1];

			System.out.println("Next_node: " + LIST_NODE.get(next_node).get_posCrypt());

			// On indique que le noeud a été visité
			visited[next_node] = 1;
			// On indique si le noeud est multidirectionnel
			if (LIST_NODE.get(next_node).isMultidirectionnel()) {multidirectionnel[next_node]=1;}

			for(int j = 0; j < NB_NODES; j++){
				
				// On regarde si la distance entre le "min" + la distance du "next_node" est plus petite que la distance à partir du noeud de départ
				if( visited[j]!=1 && (min+matrix[next_node][j] < distance[j])){
					System.out.println("Link_node: " + LIST_NODE.get(j).get_posCrypt());
					// si c'est le cas on indique la nouvelle distance entre le noeud de départ et le noeud 'j'
					if () {
					 	
					 } find_sweet(matrix, multidirectionnel, parcours);
					distance[j] = min+matrix[next_node][j];
					// on indique par quel noeud on est passé avant
					predecessor[j] = next_node;
				}
			}
		}
		print_way(distance,predecessor, indexStartNode);
	}


	public void print_state(int[] predecessor, int[] distance){
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
	}
	public int[] get_shortWay(int[] distance, int[] predecessor){
		int min;
		int shortLink;
		for(int i = 0; i < NB_NODES; i++){
			if (min > distance[i]){ 
				min = distance[i];
				shortLink = i;
			}
		}

		int j;
		for(int i = 0; i < nb_nodes; i++){
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

	// On va donc récupérer tous les chemins les plus court en partant 
	public void print_way(int[] distance, int[] predecessor, int indexStartNode){

		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +LIST_NODE.get(i).get_posCrypt()+": "+ distance[i]);
		}
		System.out.println("|");
		int j;
		for(int i = 0; i < NB_NODES; i++){
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

	// supprime les noeuds ne menant a rien autre qu'un vide ou un monstre O(4N)
	public void optimisation_graph(Node current_node){
		//if (current_node.get_nodeValue() == 2 || current_node.get_nodeValue() == 0 || current_node.get_nodeValue() == 1 ){
		if (current_node.isUnidirectionnel() && (current_node.isFreeSpace() || current_node.isMonster())) {
			current_node.get_ensLink().get(0).supp_link(current_node);
			ENS_NODE.remove(current_node.get_posCrypt());
			optimisation++;
		}
		else if (current_node.isBidirectionnel() && current_node.isFreeSpace()) {
			Node nodeLink1 = current_node.get_ensLink().get(0);
			Node nodeLink2 = current_node.get_ensLink().get(1);
			Arc arc_toLink = new Arc();
			ArrayList<Integer> addTo_globalWay;
			if (nodeLink1.get_arc(current_node).get_weight() <= nodeLink2.get_arc(current_node).get_weight() ) {
				arc_toLink.set_globalWay(nodeLink1.get_arc(current_node).get_globalWay());
				addTo_globalWay = nodeLink2.get_arc(current_node).get_globalWay();
			}
			else{
				arc_toLink.set_globalWay(nodeLink2.get_arc(current_node).get_globalWay());
				addTo_globalWay = nodeLink1.get_arc(current_node).get_globalWay();
			}
			for (int i =0;i < addTo_globalWay.size() ;i++ ) {
				arc_toLink.add_way(addTo_globalWay.get(i));
			}
			arc_toLink.set_startNode(nodeLink1);arc_toLink.set_endNode(nodeLink2);
			nodeLink1.supp_link(current_node); nodeLink2.supp_link(current_node);
			arc_toLink.set_startNode(nodeLink1);arc_toLink.set_endNode(nodeLink2);
			arc_toLink.set_stateArc(false);
			update_nodeLink(arc_toLink);
			ENS_NODE.remove(current_node.get_posCrypt());
			optimisation++;
		}
	}

	public int valueMat( int[][] mat, int pos_crypt){
		int[] positions = pos_decryptage(pos_crypt);
		return mat[positions[0]][positions[1]];
	}

	// parcours en backtraking du labyrinthe créant a chaque intersection de chemin une node - un sommet-.  
	public void create_graph(int[][] mat, int pos_crypt, int prePos_crypt,boolean isNode,Arc current_arc){
		//actuLine,j position actuel, preLine,preColumn position precedente
		/*System.out.print("Controle_Create || prePos --> ");System.out.print(preLine);System.out.print(preColumn);
		System.out.print(" || actuPos --> ");System.out.print(actuLine);System.out.print(actuColumn);
		System.out.print(" || isNode --> ");System.out.println(isNode);
		*/
		Node current_node = null;
		//int pos_crypt= pos_cryptage(actuLine,actuColumn);
		iterrator++;
		int nb_testDirection = 0;
		if (!ENS_NODE.containsKey(pos_crypt)){
			int[] data_direction = detect_isNode(mat, pos_crypt, prePos_crypt);

			if (data_direction[DIRECTION_SIZE+1]>1 || valueMat(mat, pos_crypt)>0)  { 

				current_node = select_currentNode(valueMat(mat, pos_crypt),pos_crypt);
				if (current_node.isExit()) {exitPos_crypt=pos_crypt;}

				if (current_arc != null) {
					end_Arc(current_arc,current_node,pos_crypt);
					update_nodeLink(current_arc); 
				}
				isNode = true;
			}
			else {isNode = false;}

			if (!isNode) {current_arc.add_way(pos_crypt);}
			for (int i = 0; i <= DIRECTION_SIZE; i++) {
				if (data_direction[i] >= 1) {
					if (isNode) { current_arc = start_Arc(pos_crypt,current_node);}
					
					int newPos_crypt = modif_posCrypt(pos_crypt,(DIRECTION[i]*data_direction[i]),(DIRECTION[DIRECTION_SIZE-i]*data_direction[i]));
					create_graph(mat,newPos_crypt, pos_crypt,isNode,current_arc);
					nb_testDirection++;
				}
				if (nb_testDirection==data_direction[DIRECTION_SIZE+1] ) {break;}
			}
			if(isNode){optimisation_graph(current_node);}
		
		}
	}
}














