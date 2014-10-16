import java.lang.Object;

public class Arc {
	//attribut
	private List<List<int>> global_way;
	//constructeur
	public Arc(int pos_x, int pos_y){
		add_way( pos_x, pos_y);
	}
	// Methode ajoutant un chemin de coordonée (x,y) dans l'attribut global_way
	public static void add_way(int pos_x,int pos_y){
		List coord = Arrays.asList(pos_x,pos_y);
		global_way.add(coord);

	}
	//getter
	public static List get(int pos){
		return global_way[pos];
	}
	public static int get_weight(){
		return global_way.size();
	}

	// vide l'arc --> verifier utilité
	public static void empty(){
		for (int i = 0;i< global_way.size() ; i++) {
			global_way[i].clear();
		}
		global_way.clear();
	}
	
}