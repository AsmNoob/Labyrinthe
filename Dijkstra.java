import java.io.*;
import java.util.*;

public class Dijkstra {
	private int[][] MATRIX;
	private ArrayList<Node> LIST_NODE;
	private int NB_NODES;
	private int IN = 9999;
	private int[] SWEET_USE; // chaque monstre possedant un bonbon déjà utilisé voit sa valeur modifier SWEET_USE[MONSTRE] = BONBON et inversement SWEET_USE[BONBON] = MONSTER
	private int[] VISITED;// permet de savoir les noeuds déjà visités
	private	int[] PREDECESSOR; // garde en mémoire de quel noeud on est venu pour arriver en predecessor[i]
	private	int[] MULTIDIRECTION; // list dont l'indice des nodes ayant plusieurs direction sont égale a 1

	public Dijkstra(ArrayList<Node> listNode){ // Prints Mat_linkNode


		LIST_NODE = listNode; 
		NB_NODES =LIST_NODE.size();
		VISITED = new int[NB_NODES]; 
		PREDECESSOR = new int[NB_NODES]; 
		SWEET_USE = new int[NB_NODES];
		createMat_linkNode();

		for(int i = 0; i < NB_NODES; i++){
			System.out.print("|" +LIST_NODE.get(i).get_posCrypt());
		}
		System.out.println();

	}
	public int[][] createMat_linkNode(){
		MATRIX = new int[NB_NODES][NB_NODES]; // utilisé pou le liens entre les noeuds
		for(int i = 0; i < NB_NODES; i++){
			for(int j = i; j < NB_NODES;j++){
				// MOCHE !!!!!!
				try{
					MATRIX[i][j] = MATRIX[j][i] = LIST_NODE.get(i).get_arc(LIST_NODE.get(j)).get_weight();
				}catch(NullPointerException e){
					MATRIX[i][j] = MATRIX[j][i] = IN;
				}
			}
		}
	}
	
	// On choisit quel est le noeud connecté au noeud actuel avec le plus court chemin.
	public int[] find_lightWay(int[] distance, int[] visited, int valueToFind){
		int[] lightWay = int[2];
		int min = IN;
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
	public boolean check_actuWay(int actu_node, int monster){
		int j = actu_node;
		do{
			j=PREDECESSOR[j];
			// si on trouve un bonbon avant de trouver un monstre dans le parcours de reference on renvois true
			if (LIST_NODE.get(j).isSweet()) && (SWEET_USE[j] == 0 ||  SWEET_USE[j] == monster)) {
				SWEET_USE[monster] = j; // si on utilise ce bon le monstre et le bonbon son lié.
				SWEET_USE[j] = monster;
				return true;
			}

		}while(j!=0); //0 est l'indice zero dans LIST_NODE qui correspond au pakkuman
		return false;
	}

	// parcous le chemin de reference déjà parcouru et crée une list contenant les nodes déjà parcouru et pas l'ensemble des nodes testées
	public int[] modif_visited(int[] visited, int[] predecessor, int actu_node){
		int j = actu_node;
		MULTIDIRECTION = new int[NB_NODES];
		do{
			if (LIST_NODE.get(j).isMultidirectionnel()) {MULTIDIRECTION[j]=1;}
			visited[j] = 1;
			j=predecessor[j];
		}while(j!=0); //0 est l'indice zero dans LIST_NODE qui correspond au pakkuman
		return visited;
	}

	public int[] find_sweet(int actu_node, int monster){
		int[] result_info = new int[NB_NODES+2]; // garde en mémoire de quel noeud on est venu pour arriver en predecessor[i] || deux element de plus indiquant le poids de l'arc supplementaire, au maximum un parcours total du graph (impossible),et la valeur du noeud sur lequel il y a une deviation

		if(check_actuWay(actu_node,monster)){return result_info;}
		else{ result_info[NB_NODES+2] = IN;}

		int[] visited = new int[NB_NODES];
		visited = modif_visited(visited, PREDECESSOR, actu_node);
		int[] distance = new int[NB_NODES]; // permet de connaitre la distance jusqu'a un certain noeud
		int[] predecessor;


		int next_node;
		boolean findConnexion =true;
		int[] lightWay = new int[2];

		// indexStartNode pas défini et je sais pas trop comment tu voulais l'initialiser
		for (int k = 0; k < NB_NODES ; k++) {
			predecessor = new int[NB_NODES+2]; // garde en mémoire de quel noeud on est venu pour arriver en predecessor[i] || deux element de plus indiquant le poids de l'arc supplementaire, au maximum un parcours total du graph (impossible),et la valeur du noeud sur lequel il y a une deviation

			distance = MATRIX[k]; // distance de node0 aux autres nodes
			distance[k] = 0; // car c'est le noeud de départ
			next_node = k;

			while(MULTIDIRECTION[k] ==1 && !LIST_NODE.get(next_node).isSweet() && findConnexion){
				findConnexion = false;
				print_state(predecessor,distance);

				lightWay = find_lightWay(distance,visited,3); //3 pour bonbon
				min=lightWay[0]; next_node =lightWay[1];

				System.out.println("Next_node: " + LIST_NODE.get(next_node).get_posCrypt());

				// On indique que le noeud a été visité
				visited[next_node] = 1;
			
				for(int j = 0; j < NB_NODES; j++){
				
					// On regarde si la distance entre le "min" + la distance du "next_node" est plus petite que la distance à partir du noeud de départ
					if( visited[j]!=1 && (min+MATRIX[next_node][j] < distance[j]) && !LIST_NODE.get(j).isMonster()){
						System.out.println("Link_node: " + LIST_NODE.get(j).get_posCrypt());
						// si c'est le cas on indique la nouvelle distance entre le noeud de départ et le noeud 'j'
						distance[j] = predecessor[NB_NODES+2] = min+MATRIX[next_node][j];
						// on indique par quel noeud on est passé avant
						predecessor[j] = predecessor[NB_NODES+1] = next_node;
						findConnexion = true;
					}
				}
			}
			if (LIST_NODE.get(next_node).isSweet() && predecessor[NB_NODES+2]< result_info[NB_NODES+2]) {
				result_info = predecessor;
			}
		}
		return result_info;
	}

	public void dijkstra (int indexStartNode, int valueToFind){

		int[] distance = new int[NB_NODES]; // permet de connaitre la distance jusqu'a  un certain noeud
		
		int min;
		int way_supp;
		int[] lightWay = new int[2];
		int next_node = indexStartNode;
		int[] info_supp = new int[NB_NODES+2]; // 2 elements de plus pour contenir les informations supplementaire concernant a un ajout de chemin afin de trouver un bonbon 

		// Algorithme préparé pour que PAKKUMAN soit en mat[0][0]

		distance = matrix[indexStartNode]; // distance de node0 aux autres nodes
		VISITED[indexStartNode] = 1; // on considère le premier node comme visité
		distance[indexStartNode] = 0; // car c'est le noeud de départ

		while(LIST_NODE.get(next_node).get_nodeValue() != valueToFind){ // On s'arrete si la valeur de la node actuelle traité est celle qu'on cherche

			print_state(PREDECESSOR,distance);

			lightWay = find_lightWay(distance,visited,valueToFind);
			min=lightWay[0]; next_node =lightWay[1];

			System.out.println("Next_node: " + LIST_NODE.get(next_node).get_posCrypt());

			// On indique que le noeud a été visité
			VISITED[next_node] = 1;
			// On indique si le noeud est multidirectionnel

			for(int j = 0; j < NB_NODES; j++){
				way_supp = 0;
				// On regarde si la distance entre le "min" + la distance du "next_node" est plus petite que la distance à partir du noeud de départ
				
				if( VISITED[j]!=1 && (min+MATRIX[next_node][j] < distance[j])){

					System.out.println("Link_node: " + LIST_NODE.get(j).get_posCrypt());
					// si c'est le cas on indique la nouvelle distance entre le noeud de départ et le noeud 'j'
					if(LIST_NODE.get(j).isMonster()) {
						info_supp = find_sweet(next_node,j);
						way_supp = info_supp[NB_NODES+2]*2; // Fois deux car le chemin trouver correspond a un aller simple d'une node spécifique appartenant au chemin actuelle et d'un bonbon
					 	
					}
					if (!LIST_NODE.get(j).isMonster() || min+way_supp+MATRIX[next_node][j] < distance[j]) {
						distance[j] = min+MATRIX[next_node][j];
						// on indique par quel noeud on est passé avant
						PREDECESSOR[j] = next_node;
					}
				}
			}
		}
		print_way(distance,PREDECESSOR, indexStartNode);
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
}