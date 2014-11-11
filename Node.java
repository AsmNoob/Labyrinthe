import java.util.ArrayList;

public class Node {
	// attribut
	private int node_value; //pacman,bonus, monstre ou sortie
	private int pos_crypt; // position de la node dans la matrice
	private ArrayList<Node> node_link = new ArrayList<Node>();//contient les nodes dont celle ci a une connexion
	private ArrayList<Arc> arc_link = new ArrayList<Arc>();// contient les arc lié avec le précedent via leur indice
	//constructeur
	public Node(int posCrypt, int elem){
		pos_crypt = posCrypt;
		node_value = elem;
	}

	//______setter
	// methode permettant de regrouper toutes les connexions existantes avec ce noeud.
	public void add_link(Node node, Arc arc){
		if (!arc_link.contains(arc) && node != this){
			node_link.add(node);
			arc_link.add(arc);
		}
	}
	public void supp_link(Node node){
		for (int i = 0; i< node_link.size() ; i++ ) {
			if (node == node_link.get(i)){
				node_link.remove(i);
				arc_link.remove(i);
				node.supp_link(this);
			}
		}
	}
	
	//______getter
	public int get_nodeValue(){
		return node_value;
	}
	public ArrayList<Node> get_ensLink(){
		return node_link;
	}
	public ArrayList<Arc> get_ensArc(){
		return arc_link;
	}
	//print position crypté de la node
	public void print_nodePos(){
		System.out.print(pos_crypt);
	}

	//print liaison entre noeud
	public void print(){
		int size_globalWay = node_link.size();
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