import java.util.*;

public class Node {
	// attribut
	private int node_value; //pacman,bonus, monstre ou sortie
	private int pos_crypt; // position de la node dans la matrice
	private HashMap<Node,Arc> ens_link = new HashMap<Node,Arc>();

	//constructeur
	public Node(int posCrypt, int elem){
		pos_crypt = posCrypt;
		node_value = elem;
	}

	//______setter
	// methode permettant de regrouper toutes les connexions existantes avec ce noeud.
	public void add_link(Node node, Arc arc){
		// condition evitant les doublons
		if (node != this && (!ens_link.containsKey(node) || (arc.get_weight() < ens_link.get(node).get_weight()))) {ens_link.put(node,arc);}
	}
	public void supp_link(Node node){
		if (ens_link.containsKey(node)) {
			ens_link.remove(node);
			node.supp_link(this);
		}
	}
	public Arc get_arc(Node node){
		if (ens_link.containsKey(node)) {return ens_link.get(node);}
		return null;
	}
	public void set_nodeValue(int value){
		node_value = value;
	}
	public void set_posCrypt(int posCrypt){
		pos_crypt = posCrypt;
	}
	
	//______getter
	public int get_nodeValue(){
		return node_value;
	}
	public ArrayList<Node> get_ensLink(){
		return new ArrayList<Node>(ens_link.keySet());
	}
	public ArrayList<Arc> get_ensArc(){
		return new ArrayList<Arc>(ens_link.values());
	}
	public int get_posCrypt(){
		return pos_crypt;
	}
	//print position crypté de la node
	public void print_nodePos(){
		System.out.print(pos_crypt);
	}

	//print liaison entre noeud
	public void print(){
		int size_globalWay = ens_link.size();
		ArrayList<Arc> arc_link =  new ArrayList<Arc>(ens_link.values());
		System.out.print("Node : "); this.print_nodePos();
		System.out.println();

		System.out.println("link : ");
		for (int i = 0; i < size_globalWay ; i++ ) {
			System.out.print("        ");
			arc_link.get(i).print_nodes();;System.out.print(" : Arc || ");
			

			arc_link.get(i).print_arc();
			System.out.println(); 
		}
		System.out.println();

	}

}