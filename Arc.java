import java.util.*;

public class Arc {
	//attribut
	private ArrayList<ArrayList<Integer>> global_way = new ArrayList<ArrayList<Integer>>();
	//constructeur
	public Arc(int pos_x, int pos_y){
		add_way( pos_x, pos_y);
	}
	// Methode ajoutant un chemin de coordonée (x,y) dans l'attribut global_way
	public void add_way(int pos_x,int pos_y){
		ArrayList<Integer> coord = new ArrayList<Integer>();
		coord.add(pos_x); coord.add(pos_y);
		global_way.add(coord);

	}
	//getter
	public ArrayList<Integer> get(int pos){
		return global_way.get(pos);
	}
	public int get_weight(){
		return global_way.size();
	}
	
}