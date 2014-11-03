import java.util.*;

public class Arc {
	//attribut
	private ArrayList<Integer> global_way = new ArrayList<Integer>();
	//constructeur
	public Arc(int pos_cryptInit){
		add_way(pos_cryptInit);
	}
	// Methode ajoutant un chemin de coordonée (x,y) dans l'attribut global_way
	public void add_way(int pos_crypt){
		global_way.add(pos_crypt);

	}
	//getter
	public int get(int pos){
		return global_way.get(pos);
	}
	public int get_weight(){
		return global_way.size();
	}
	
}