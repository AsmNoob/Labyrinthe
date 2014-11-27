
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
	public ArrayList<Node> get_listNode(){
		return LIST_NODE;
	}
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
			int monster_counter = 0;
			int sweet_counter = 0;
			HashMap<Integer,Integer> ens_monsters = new HashMap<Integer,Integer>();
			HashMap<Integer,Integer> ens_sweets = new HashMap<Integer,Integer>();
			for(int i = 0; i < LIST_NODE.size(); i++){

				for(int j = 0; j < LIST_NODE.get(i).get_ensLink().size();j++){ // parcourt les noeuds 
					if(!(list_arcsPrinted.contains(Integer.toString(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt())+Integer.toString(LIST_NODE.get(i).get_posCrypt())))){
						writer.print("	");
						//writer.print(LIST_NODE.get(i).get_posCrypt());
						if(test_case(LIST_NODE.get(i).get_nodeValue()) == 'F'){
							writer.print(LIST_NODE.get(i).get_posCrypt());
						}else{
							if(test_case(LIST_NODE.get(i).get_nodeValue()) == 'M'){
								// Contient pas la posCrypt
								if(!ens_monsters.containsKey(LIST_NODE.get(i).get_posCrypt())){
									monster_counter++;
									ens_monsters.put(LIST_NODE.get(i).get_posCrypt(),monster_counter);
								}
								writer.print("M");writer.print(ens_monsters.get(LIST_NODE.get(i).get_posCrypt()));writer.print("_");writer.print(LIST_NODE.get(i).get_posCrypt());
							}else if (test_case(LIST_NODE.get(i).get_nodeValue()) == 'B') {
								if(!ens_sweets.containsKey(LIST_NODE.get(i).get_posCrypt())){
									sweet_counter++;
									ens_sweets.put(LIST_NODE.get(i).get_posCrypt(),sweet_counter);
								}
								writer.print("B");writer.print(ens_sweets.get(LIST_NODE.get(i).get_posCrypt()));writer.print("_");writer.print(LIST_NODE.get(i).get_posCrypt());
							}else{
								writer.print(test_case(LIST_NODE.get(i).get_nodeValue()));writer.print("_");writer.print(LIST_NODE.get(i).get_posCrypt());
							}
						}
						writer.print(" -- ");
						//writer.print(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt());
						if(test_case(LIST_NODE.get(i).get_ensLink().get(j).get_nodeValue()) == 'F'){
							writer.print(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt());
						}else{
							if(test_case(LIST_NODE.get(i).get_ensLink().get(j).get_nodeValue()) == 'M'){
								if(!ens_monsters.containsKey(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt())){
									monster_counter++;
									ens_monsters.put(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt(),monster_counter);
								}
									writer.print("M");writer.print(ens_monsters.get(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt()));writer.print("_");writer.print(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt());
							}else if (test_case(LIST_NODE.get(i).get_ensLink().get(j).get_nodeValue()) == 'B') {
								if(!ens_sweets.containsKey(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt())){
									sweet_counter++;
									ens_sweets.put(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt(),sweet_counter);
								}
								writer.print("B");writer.print(ens_sweets.get(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt()));writer.print("_");writer.print(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt());
							}else{
								writer.print(test_case(LIST_NODE.get(i).get_ensLink().get(j).get_nodeValue()));writer.print("_");writer.print(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt());
							}
						}
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

	public char test_case(int cases){
		char res = 'F';
		if(cases == 1){
			res = 'P';
		}else if(cases == 2){
			res = 'M';
		}else if (cases == 3) {
			res = 'B';
		}else if(cases == 4){
			res = 'S';
		}
		return res;
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














