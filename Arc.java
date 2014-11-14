import java.util.*;

public class Arc {
	//attribut
	private boolean openArc;
	private Node firstNode; // premiere node de l'arc
	private Node lastNode; // fin de l'arc avec la node de fin
	private ArrayList<Integer> global_way = new ArrayList<Integer>();
	//constructeur
	public Arc(){
		openArc = true;
	}
	// Methode ajoutant un chemin de coordonée (x,y) dans l'attribut global_way
	public void add_way(int pos_crypt){
		if (!global_way.contains(pos_crypt) && openArc) {
			global_way.add(pos_crypt);
		}
	}
	public void supp_way(int pos_crypt){
		for (int i = 0; i < global_way.size() ;i++ ) {
			if (global_way.get(i) == pos_crypt){
				global_way.remove(i);
				break;
			}
		}
	}
	//___________setter
	public void set_globalWay(ArrayList<Integer> globalWay){
		global_way = new ArrayList<Integer>(globalWay);
	}
	public void set_startNode(Node startNode){
		if (openArc) {firstNode = startNode;}
	}
	public void set_endNode(Node endNode){
		if (openArc) {lastNode = endNode;}
	}
	public void set_stateArc(boolean state){
		openArc = state;
	}
	
	//__________getter
	public ArrayList<Integer> get_globalWay(){
		return global_way;
	}
	public boolean get_stateArc(){
		return openArc;
	}
	//renvois un coordonée de l'arc à un index donnée
	public int get(int pos){
		return global_way.get(pos);
	}
	// renvois le poids de l'arc
	public int get_weight(){
		return global_way.size();
	}
	// renvois la node de debut
	public Node get_startNode(){
		return firstNode;
	}
	// renvois la node de fin
	public Node get_endNode(){
		return lastNode;
	}

	//__________print
	// affiche le chemin contenu dans l'arc
	public void print_arc(){
		int size_globalWay = global_way.size();
		for (int i = 0; i < size_globalWay ; i++ ) {
			System.out.print(global_way.get(i)); 
			if (i < size_globalWay-1){ System.out.print(" --> ");}
		}
		System.out.println();
	}
	// affiche les nodes de debut et fin
	public void print_nodes(){
		firstNode.print_nodePos();
		System.out.print(" --> ");
		lastNode.print_nodePos();

	}
}