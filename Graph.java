
import java.lang.Object; // a vérifier

//Constructeur de la class Node, il faut sa position dans la matrice, et quatre donnée correspondant a UP,DOWN,LEFT,RIGHT initialisé a Null dont les valeurs changeront lors de la création de celle ci, permettant de savoir plus tard dans quel direction aller directement. Chaque direction sera un pointeur vers le sommet qui est au bout de son chemin. Puisque que nos sommet sont principalement des intersections cela nous permettera de choisir directement le bon chemin .
public class Graph {
	//Attribut
	private int dim; //dimention de la matrice
	private Dictionary<List,Node> ens_node;

	// test si la postion suivante est dans les bornes et autre qu'un mur.
	public static boolean test_nextPosition(int[][] mat, int i, int j, int x, int y, int k, int l){
		if ((i+x) >= 0 && (i+x) < dim && (j+y)>= 0 && (j+y) < dim && ((i+x,l+y) != (k,l) || k,l = Null,Null)) {
			int elem = int[i+x][j+y] mat;
			// mur = -1
			if (elem != -1 | elem) {
				return true;
			}
		}
		return false;
	}
	// crée une nouvelle node si elle n'éxiste pas encore !
	public static void check_newNode(int i, int j){
		List coord = Arrays.asList(i,j);
		try {
			ens_node.get(coord);
		}catch(NullPointerException){
			Node node = new Node(i,j);
			ens_node.put(coord,node);
		}
	}
	// vérifie que l'element n'est pas un monstre/pacman/bonus/sortie
	public static void check_elemSpecial(int elem, int i, int j, Sommet prec){
		if (mat[i][j] > 0) {check_newNode(i, j);}
	}



	public static void create_graph(int[][] mat, int i, int j, int k, int l, Sommet prec){
		//i,j position actuel, k,l position precedente

		boolean multi_direction = False;

		if test_direction(mat,i,j,k,l,-1,0){//UP
			check_elemSpecial(mat, i, j, prec);
			create_graph(mat,i-1,j,i,j);
			multi_direction = True;
		}
		
		if test_direction(mat,i,j,1,0){//DOWN
			if (multi_direction) { check_newNode(mat, i, j, prec);}
			else {check_elemSpecial(mat, i, j, prec);}

			create_graph(mat,i+1,j,i,j);
			multi_direction = True;
		}
		
		if test_direction(mat,i,j,0,-1){//LEFT
			if (multi_direction) { check_newNode(mat, i, j, prec);}
			else {check_elemSpecial(mat, i, j, prec);}

			create_graph(mat,i,j-1,i,j);
			multi_direction = True;
		}
		
		if test_direction(mat,i,j,0,1){//RIGHT
			if (multi_direction) { check_newNode(mat, i, j, prec);}
			else {check_elemSpecial(mat, i, j, prec);}

			create_graph(mat,i,j+1,i,j);

		}
			
	}

	public static void main(int[][] mat, int i_start, int j_start) {
		dim = mat.length;
		Sommet node = new Sommet (i,j, Null);

		create_graph(mat,i_start,j_start, Null, Null,jnode);

		// -> évitons cela en sauvegardant la position du pacman lors de la transformation
		//en matrice au chargement du fichier.

	}


}















