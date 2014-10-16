import java.lang.Object;

public class Node {
	// attribut
	private int pos_i,pos_j; // position de la node dans la matrice
	private List<List<Node,Arc>> ens_link_;

	//constructeur
	public Node(int pos_x, int pos_y){
		pos_i = pos_x;
		pos_j = pos_y;
	}

	// methode permettant de regrouper toutes les connexions existantes avec ce noeud.
	public static add_link(Node node, Arc arc){
		List link = Array.asList(node,arc);
		ens_link_.add(link);
	}
	//getter
	
}