import java.util.*;

public class Arc {
	//attribut
	private List<List<Integer>> global_way;
	//constructeur
	public Arc(int pos_x, int pos_y){
		add_way( pos_x, pos_y);
	}
	// Methode ajoutant un chemin de coordonée (x,y) dans l'attribut global_way
	public void add_way(int pos_x,int pos_y){
		List<Integer> coord = Arrays.asList(pos_x,pos_y);
		global_way.add(coord);

	}
	//getter
	public List<Integer> get(int pos){
		return global_way.get(pos);
	}
	public int get_weight(){
		return global_way.size();
	}
	
}