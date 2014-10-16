import java.util.*;

public class Node {
	// attribut
	private int node_value; //pacman,bonus, monstre ou sortie
	private int pos_i,pos_j; // position de la node dans la matrice
	private List<Node> node_link_;//contient les node
	private List<Arc> arc_link_;// contient les arc lié avec le précedent via leur indice

	//constructeur
	public Node(int pos_x, int pos_y){
		pos_i = pos_x;
		pos_j = pos_y;
	}

	// methode permettant de regrouper toutes les connexions existantes avec ce noeud.
	public void add_link(Node node, Arc arc){
		node_link_.add(node);
		arc_link_.add(arc);
	}
	//getter
	
}