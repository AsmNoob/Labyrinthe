import java.lang.Object;

public class Arc {
	//attribut
	private List<List<int>> global_way;
	//constructeur
	public Arc(int pos_x, int pos_y){
		add_way( pos_x, pos_y);
	}
	// Methode ajoutant un chemin de coordonée (x,y) dans l'attribut global_way
	public static add_way(int pos_x,int pos_y){
		List coord = Arrays.asList(pos_x,pos_y);
		global_way.add(coord);

	}
	//getter
	
}