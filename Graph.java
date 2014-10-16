
import java.util.*; // a vérifier

//Constructeur de la class Node, il faut sa position dans la matrice, et quatre donnée correspondant a UP,DOWN,LEFT,RIGHT initialisé a null dont les valeurs changeront lors de la création de celle ci, permettant de savoir plus tard dans quel direction aller directement. Chaque direction sera un pointeur vers le sommet qui est au bout de son chemin. Puisque que nos sommet sont principalement des intersections cela nous permettera de choisir directement le bon chemin .

public class Graph {
	//Attribut
	private int dim; //dimention de la matrice
	private Dictionary<List,Node> ens_node;
	private boolean new_arc = false;
	// constructeur
	public Graph(int[][] mat, int init_i, int init_j){
		dim = mat.length;
		create_graph(mat,init_i,init_j, -1, -1, null);
	}

	// test si la postion suivante est dans les bornes et autre qu'un mur.
	public  boolean test_nextPosition(int[][] mat, int i, int j, int x, int y, int k, int l){
		if ((i+x) >= 0 && (i+x) < dim && (j+y)>= 0 && (j+y) < dim && ((i+x != k && j+y != l) || (k == -1  && l == -1))) {
			int elem = mat[i+x][j+y];
			// mur = -1
			if (elem != -1) {return true;}
		}
		return false;
	}

	// crée une nouvelle node si elle n'éxiste pas encore !
	public void check_newNode(int i, int j, Arc current_arc){
		List coord = Arrays.asList(i,j);
		try {
			ens_node.get(coord);
		}catch(NullPointerException e){
			Node node = new Node(i,j);
			if (!ens_node.isEmpty()){
				List pre_coord = current_arc.get(0);
				Node pre_node = ens_node.get(pre_coord);
				pre_node.add_link(node,current_arc);
			}
			ens_node.put(coord,node);
			new_arc = true;
		}
	}
	// vérifie que l'element n'est pas un monstre/pacman/bonus/sortie
	public void check_elemSpecial(int elem, int i, int j, Arc current_arc){
		if (elem > 0) {check_newNode(i, j, current_arc);}
	}
	// parcours en backtraking du labyrinthe créant a chaque intersection de chemin une node - un sommet-.
	public void create_graph(int[][] mat, int i, int j, int k, int l, Arc current_arc){
		//i,j position actuel, k,l position precedente

		boolean multi_direction = false;

		if (new_arc) { 
			current_arc = new Arc(i,j);
			new_arc = false;
		}
		else {current_arc.add_way(i,j);}

		check_elemSpecial(mat[i][j], i, j,current_arc);
	
		if (test_nextPosition(mat,i,j,k,l,-1,0)){//UP

			create_graph(mat,i-1,j,i,j,current_arc);
			multi_direction = true;
		}

		
		if (test_nextPosition(mat,i,j,k,l,1,0)){//DOWN
			if (multi_direction) { check_newNode(i, j, current_arc);}

			create_graph(mat,i+1,j,i,j,current_arc);
			multi_direction = true;
		}
		
		if (test_nextPosition(mat,i,j,k,l,0,-1)){//LEFT
			if (multi_direction) { check_newNode(i, j, current_arc);}

			create_graph(mat,i,j-1,i,j,current_arc);
			multi_direction = true;
		}
		
		if (test_nextPosition(mat,i,j,k,l,0,1)){//RIGHT
			if (multi_direction) { check_newNode(i, j, current_arc);}

			create_graph(mat,i,j+1,i,j,current_arc);
		}
			
	}

}















