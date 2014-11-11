import java.util.*;

public class Arc {
	//attribut
	private Node firstNode; // premiere node de l'arc
	private Node lastNode; // fin de l'arc avec la node de fin
	private ArrayList<Integer> global_way = new ArrayList<Integer>();
	//constructeur
	public Arc(int pos_cryptInit){
		add_way(pos_cryptInit);
	}
	// Methode ajoutant un chemin de coordonée (x,y) dans l'attribut global_way
	public void add_way(int pos_crypt){
		if (!global_way.contains(pos_crypt)) {
			global_way.add(pos_crypt);
		}
	}
	//___________setter
	public void set_startNode(Node startNode){
		firstNode = startNode;
	}
	public void set_endNode(Node endNode){
		lastNode = endNode;
	}
	
	//__________getter
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