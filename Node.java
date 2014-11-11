import java.util.ArrayList;

public class Node {
	// attribut
	private int node_value; //pacman,bonus, monstre ou sortie
	private int pos_crypt; // position de la node dans la matrice
	private ArrayList<Node> node_link = new ArrayList<Node>();//contient les nodes dont celle ci a une connexion
	private ArrayList<Arc> arc_link = new ArrayList<Arc>();// contient les arc lié avec le précedent via leur indice
	//constructeur
	public Node(int posCrypt, int elem){
		test_allDirection = false;
		pos_crypt = posCrypt;
		node_value = elem;
	}

	//______setter
	// methode permettant de regrouper toutes les connexions existantes avec ce noeud.
	public void add_link(Node node, Arc arc){
		if (!node_link.contains(node)){
			node_link.add(node);
			arc_link.add(arc);
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
			node_link.get(i).print_nodePos();
			System.out.println();
		}
		System.out.println();

	}

}