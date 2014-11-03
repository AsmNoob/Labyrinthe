import java.util.ArrayList;

public class Node {
	// attribut
	private int node_value; //pacman,bonus, monstre ou sortie
	private int pos_crypt; // position de la node dans la matrice
	private ArrayList<Node> node_link_ = new ArrayList<Node>();//contient les node
	private ArrayList<Arc> arc_link_ = new ArrayList<Arc>();// contient les arc lié avec le précedent via leur indice

	//constructeur
	public Node(int posCrypt){
		pos_crypt = posCrypt;
	}

	// methode permettant de regrouper toutes les connexions existantes avec ce noeud.
	public void add_link(Node node, Arc arc){
		node_link_.add(node);
		arc_link_.add(arc);
	}
	//getter
	
}