
import java.io.*;
import java.util.*;

//Constructeur de la class Node, il faut sa position dans la matrice, et quatre donnée correspondant a UP,DOWN,LEFT,RIGHT initialisé a null dont les valeurs changeront lors de la création de celle ci, permettant de savoir plus tard dans quel direction aller directement. Chaque direction sera un pointeur vers le sommet qui est au bout de son chemin. Puisque que nos sommet sont principalement des intersections cela nous permettera de choisir directement le bon chemin .

public class Graph {
	//Attribut
	private int DIM_LINE; //dimention de la matrice 
	private int DIM_COLUMN; //dimention de la matrice
	private int LINE_SIZE; //dimention de la matrice 
	private int COLUMN_SIZE; //dimention de la matrice
	private HashMap<Integer,Node> ENS_NODE = new HashMap<Integer,Node>(); //dictionnaire permettant de retrouver rapidement les nodes par leur positions crypté
	private	ArrayList<Node> LIST_NODE;// liste contenant les nodes crées
	private int[] DIRECTION = new int[] {-1,1,0,0}; // liste permettant de contenir les differents modifications applicable a la matrice pour s'y deplacer
	private int DIRECTION_SIZE = DIRECTION.length-1;
	private int NB_NODES; // contient le nombre de nodes créées
	private int iterrator = 0;// variable à titre informatif permetttant avec optimisation de savoir combien de node on a supprimé
	private int optimisation = 0;// variable à titre informatif permetttant avec iterrator de savoir combien de node on a supprimé
	private int PAKKUMAN_CRYPT; // position du pakkuman crypté
	private int EXIT_CRYPT; // position de la sortie crypté
	private int IN = 9999; //valeur par defaut de la distance séparant deux nodes non testées

	//-----------------------------------Constructeur-------------------------------------//
	public Graph(int[][] mat, int[] pakkumanCoord){
		try{

			build_struct(mat, pakkumanCoord);
			create_graph(mat,PAKKUMAN_CRYPT,PAKKUMAN_CRYPT, true, null);

			NB_NODES = ENS_NODE.size();
			LIST_NODE = new ArrayList<Node>(ENS_NODE.values());

			//fais un switch entre la node Pakkuman et la node en premiere position 
			LIST_NODE.remove(ENS_NODE.get(PAKKUMAN_CRYPT));

			LIST_NODE.add(0,ENS_NODE.get(PAKKUMAN_CRYPT));
			graph_converter();
			
			System.out.print("Optimisation || nb node : ");
			System.out.print(ENS_NODE.size()+optimisation);
			System.out.print(" to ");
			System.out.println(ENS_NODE.size());
		}catch(NullPointerException e ){
			System.err.println("Caught NullPointerException in Graph creation: " + e.getMessage());
		}

	}
	// fonction permettant de mettre en place les variables de classes dont on aura besoin
	public void build_struct(int[][] mat, int[] pakkumanCoord){

		DIM_LINE = mat.length;
		DIM_COLUMN = mat[0].length;
		LINE_SIZE = (int) Math.pow(10,Integer.toString(DIM_LINE).length());// (int) Math.pow(10,Math.floor(Math.log10(DIM_LINE))+1);// permet de savoir 
		COLUMN_SIZE = (int) Math.pow(10,Integer.toString(DIM_COLUMN).length());//
		PAKKUMAN_CRYPT = pos_cryptage(pakkumanCoord[0]*2+1,pakkumanCoord[1]*2+1);

	}
	//-----------------------------------Getter-------------------------------------//
	// recupere la position crypté de la sortie
	public int get_exitPos(){
		return EXIT_CRYPT;
	}
	// recupere la liste des noeuds créés
	public ArrayList<Node> get_listNode(){
		return LIST_NODE;
	}
	//recupere le dictionnaire des noeuds créés avec leur position crypté en clé
	public HashMap<Integer,Node> get_ensNode(){
		return ENS_NODE;
	}

	//-----------------------------------Node-------------------------------------//
	// creer une node et la place dans la list de stockage
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
			System.out.println(pos_crypt);
			current_node = ENS_NODE.get(pos_crypt);
		}
		return current_node;
	}
	// met a jour les noeuds présent dans l'arc courant pour que ceux ci possèdent le lien vers cet arc.
	public void update_nodeLink(Arc current_arc){
		current_arc.get_startNode().add_link(current_arc.get_endNode(),current_arc);
		current_arc.get_endNode().add_link(current_arc.get_startNode(),current_arc);
	}
	//-----------------------------------Arc-------------------------------------//
	// crée un arc 
	public Arc start_Arc(int pos_crypt, Node start_node){
		Arc current_arc = new Arc();
		current_arc.add_way(pos_crypt);
		current_arc.set_startNode(start_node);
		return current_arc;
	}

	// ferme l'arc
	public void end_Arc(Arc current_arc, Node current_node, int pos_crypt){
		current_arc.add_way(pos_crypt);
		current_arc.set_endNode(current_node);
		current_arc.set_stateArc(false);
	}

	//affichage des données récuperer après analyse du labyrinthe. 
	public void print_graph(){
		for (int i = 0; i < LIST_NODE.size(); i++ ) {
			LIST_NODE.get(i).print();
		}
	}

	public void graph_converter(){
		try{
			PrintWriter writer = new PrintWriter ("Graph.dot");
			writer.println("graph chemin {");
			writer.println();
			ArrayList<String> list_arcsPrinted = new ArrayList<String>();

			for(int i = 0; i < NB_NODES; i++){

				for(int j = 0; j < LIST_NODE.get(i).get_ensLink().size();j++){ // parcourt les noeuds 
					if(!(list_arcsPrinted.contains(Integer.toString(LIST_NODE.get(i).get_ensLink().get(j).get_posCrypt())+Integer.toString(LIST_NODE.get(i).get_posCrypt())))){
						writer.print("	");
						if(test_case(LIST_NODE.get(i).get_nodeValue()) != 'F'){
							writer.print(test_case(LIST_NODE.get(i).get_nodeValue()));writer.print("_");
						}
						writer.print(LIST_NODE.get(i).get_posCrypt());
						writer.print(" -- ");
						if(test_case(LIST_NODE.get(i).get_ensLink().get(j).get_nodeValue()) != 'F'){
							writer.print(test_case(LIST_NODE.get(i).get_ensLink().get(j).get_nodeValue()));writer.print("_");
						}
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

	public char test_case(int cases){
		if(cases == 1){
			return'P';
		}else if(cases == 2){
			return 'M';
		}else if (cases == 3) {
			return 'B';
		}else if(cases == 4){
			return 'S';
		}
		return 'F';
	}

	//-----------------------------------Mouvement-------------------------------------//
	// test si la postion suivante est dans les bornes et autre qu'un mur.
	//appel recursif de la fonction avec test_twoCase en valeur de controle car les murs se situent entre deux chemin, ils ne constituent pas de chemin en tant que tel. 
	public int check_nextPosition(int[][] mat, int pos_crypt, int prePos_crypt, int line_add, int column_add, boolean test_twoCase){
		int newPos_crypt = modif_posCrypt(pos_crypt,line_add,column_add);
		int[] newPositions = pos_decryptage(newPos_crypt);
		//si on est dans le cas ou l'element vaut la sortie il ne faut pas faire d'appel récursif puisque pour revenir dans les positions courrante et non les murs
		if (valueMat(mat, pos_crypt) == 4) {test_twoCase = true;}
		if (( (newPositions[0] >= 0) && (newPositions[0] < DIM_LINE) && (newPositions[1]>= 0) && (newPositions[1] < DIM_COLUMN) && (newPos_crypt != prePos_crypt)) || (pos_crypt == prePos_crypt)) {
			int elem = mat[newPositions[0]][newPositions[1]];
			// mur = -1
			if (elem == 4) {return 1;}
			//petite astuce ici si c'est egale a la sortie on ne retourne que 1 au lieu de deux car mis a part 
			//la sortie tout les elements de la matrice se récupere par saut de deux cases.
			if (elem != -1) {
				if (!test_twoCase){return check_nextPosition(mat,newPos_crypt,prePos_crypt, line_add,column_add,true);}
				else {return 2;}
			}
		}
		return 0;
	}
	// renvois la valeur de la position crypté dans la matrice
	public int valueMat( int[][] mat, int pos_crypt){
		int[] positions = pos_decryptage(pos_crypt);
		return mat[positions[0]][positions[1]];
	}
	// cryptage de la position i,j
	public int pos_cryptage(int i, int j){
		return ((LINE_SIZE*COLUMN_SIZE)+(i*COLUMN_SIZE)+j);
	}
	// modification de la position crypté
	public int modif_posCrypt(int pos_crypt, int addLine, int addColumn){
		return pos_cryptage(pos_decryptage(pos_crypt)[0]+addLine,pos_decryptage(pos_crypt)[1]+addColumn);
	}
	// decryptage de la position 
	public int[] pos_decryptage( int pos_crypt){
		int[] position= new int[] {(pos_crypt-LINE_SIZE*COLUMN_SIZE-(pos_crypt%COLUMN_SIZE))/COLUMN_SIZE,pos_crypt %COLUMN_SIZE};
		return position; 
	}

	// detecte si la position actuelle est une node et renvois une liste d'entier contenant les directions possible suivante.
	public int[] data_ofPosition(int[][] mat, int pos_crypt, int prePos_crypt){
		int j = DIRECTION_SIZE;
		int[] data_direction = new int[5];// 4 premier entier -0,1- correspond au direction possible ou non le 5eme si il est >1 nous indiques que c'est une node.
		int test;
		for (int i = 0; i<= DIRECTION_SIZE; i++){
			test = check_nextPosition(mat, pos_crypt, prePos_crypt,DIRECTION[i],DIRECTION[j],false);

			data_direction[i] = test;
			if (test>0){data_direction[DIRECTION_SIZE+1]+=1;}
			j--;
		}

		return data_direction;
	}

	// supprime les noeuds ne menant a rien autre qu'un vide ou un monstre O(4N)
	public void optimisation_graph(Node current_node){
		//premiere optimisation permettant de supprimer les noeuds en fin de parcours étant un monstre ou un noeud quelconque 
		if (current_node.isUnidirectionnel()) {
			Node nodeLink =  current_node.get_ensLink().get(0);
			if (current_node.isFreeSpace() || current_node.isMonster() || (!current_node.isExit() && !current_node.isPakkuman() && nodeLink.isMonster())) {
				current_node.get_ensLink().get(0).supp_link(current_node);
				ENS_NODE.remove(current_node.get_posCrypt());
				optimisation++;
			}	
		}
		// cette deuxieme optimisation permet de rassembler deux nodes ensemble qui serait relié par un noeuds quelconque.
		else if (current_node.isBidirectionnel() && current_node.isFreeSpace()) {
			int pos_current = current_node.get_posCrypt();
			Node nodeLink1 = current_node.get_ensLink().get(0);
			Node nodeLink2 = current_node.get_ensLink().get(1);
			Arc arc_toLink = new Arc();
			ArrayList<Integer> way_arc1 = nodeLink1.get_arc(current_node).get_globalWay();
			ArrayList<Integer> way_arc2 = nodeLink2.get_arc(current_node).get_globalWay();

			// on inverse les arc pour que le dernier element de way_arc2 egale celui de way_arc1
			if (way_arc1.get(0) != pos_current) {Collections.reverse(way_arc1);}
			if (way_arc2.get(0) == pos_current) {Collections.reverse(way_arc2);}
			// permettant ainsi d'avoir un arc dont les positions se suivent de manière logique.
			way_arc1.remove(0); //premier element commun au deux arc puisque égale a la node de suppréssion
			way_arc2.addAll(way_arc1);
			arc_toLink.set_globalWay(way_arc2);
			//on supprime le noeuds et on met a jour l'arc
			nodeLink1.supp_link(current_node); nodeLink2.supp_link(current_node);
			if (arc_toLink.get(0) == nodeLink1.get_posCrypt()){
				arc_toLink.set_startNode(nodeLink1);arc_toLink.set_endNode(nodeLink2);}
			else{
				arc_toLink.set_startNode(nodeLink2);arc_toLink.set_endNode(nodeLink1);}
			arc_toLink.set_stateArc(false);
			update_nodeLink(arc_toLink);
			ENS_NODE.remove(current_node.get_posCrypt());
			optimisation++;
		}
	}

	// parcours en backtraking du labyrinthe créant a chaque intersection de chemin une node - un sommet-.  
	public void create_graph(int[][] mat, int pos_crypt, int prePos_crypt,boolean isNode,Arc current_arc){
		Node current_node = null;
		iterrator++;
		if (!ENS_NODE.containsKey(pos_crypt)){
			//on recupere les données nous permettant de savoir dans quel direction on peut aller
			int[] data_direction = data_ofPosition(mat, pos_crypt, prePos_crypt);

			//opération effectuer si la position actuelle est une node.
			if (data_direction[DIRECTION_SIZE+1]>1 || valueMat(mat, pos_crypt)>0)  { 

				//on crée ou selectionne une node.
				current_node = create_node(valueMat(mat, pos_crypt), pos_crypt);//select_currentNode(valueMat(mat, pos_crypt),pos_crypt);
				if (current_node.isExit()) {EXIT_CRYPT=pos_crypt;}

				//on termine l'arc actuelle si il existe.
				if (current_arc != null) {
					end_Arc(current_arc,current_node,pos_crypt);
					update_nodeLink(current_arc); 
				}
				isNode = true;
			}
			//si ce n'est pas une node on doit simplement ajouter la nouvelle position a l'arc courant
			else {
				current_arc.add_way(pos_crypt);
				isNode = false;
			}

			int i = 0; int newPos_crypt;
			int nb_testDirection = 0;

			while(nb_testDirection<data_direction[DIRECTION_SIZE+1]){
				if (data_direction[i] >= 1) {// si c'est un direction possible
					//on crée un nouvelle arc si c'est une nouvelle node
					if (isNode) { current_arc = start_Arc(pos_crypt,current_node);}
					
					newPos_crypt = modif_posCrypt(pos_crypt,(DIRECTION[i]*data_direction[i]),(DIRECTION[DIRECTION_SIZE-i]*data_direction[i]));
					create_graph(mat,newPos_crypt, pos_crypt,isNode,current_arc);
					nb_testDirection++;
				}
				i++;
			}
			// le dernier element de cette liste contient le nombre de direction possible pour la position courante
			// si c'est une node on esaye de l'optimisé
			if(isNode){optimisation_graph(current_node);}
		
		}
	}
}