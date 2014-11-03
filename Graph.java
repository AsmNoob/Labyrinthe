
import java.util.*; // a vérifier

//Constructeur de la class Node, il faut sa position dans la matrice, et quatre donnée correspondant a UP,DOWN,LEFT,RIGHT initialisé a null dont les valeurs changeront lors de la création de celle ci, permettant de savoir plus tard dans quel direction aller directement. Chaque direction sera un pointeur vers le sommet qui est au bout de son chemin. Puisque que nos sommet sont principalement des intersections cela nous permettera de choisir directement le bon chemin .

public class Graph {
	//Attribut
	private int dim; //dimention de la matrice
	private Dictionary<Integer,Node> ens_node;// dictionnaire comprenant en key la position cryptée et en valeur la node a cette position.
	private boolean new_arc = false;

	// constructeur
	public Graph(int[][] mat, int init_i, int init_j){
		dim = mat.length;
		create_graph(mat,init_i,init_j, -1, -1, null);
		print_graph(mat);
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
		int pos_crypt = pos_cryptage(i,j);
		try {
			ens_node.get(pos_crypt);
		}catch(NullPointerException e){
			Node node = new Node(pos_crypt);
			if (!ens_node.isEmpty()){
				int pre_pos = current_arc.get(0);
				Node pre_node = ens_node.get(pre_pos);
				pre_node.add_link(node,current_arc);
			}
			ens_node.put(pos_crypt,node);
			new_arc = true;
		}
	}
	// vérifie que l'element n'est pas un monstre/pacman/bonus/sortie
	public void check_elemSpecial(int elem, int i, int j, Arc current_arc){
		if (elem > 0) {check_newNode(i, j, current_arc);}
	}
	public void print_graph(int[][] mat){
		
	}
	// cryptage de la position i,j
	public int pos_cryptage(int i, int j){
		int pos = 10000+(i*100)+j;
		return pos;
	}

	// decryptage de la position 
	public int pos_decryptage( int pos){
		int j = pos %100;
		int i = (pos-10000-j)/100;

		return i; 
	}
	// parcours en backtraking du labyrinthe créant a chaque intersection de chemin une node - un sommet-.
	public void create_graph(int[][] mat, int i, int j, int k, int l, Arc current_arc){
		//i,j position actuel, k,l position precedente

		boolean multi_direction = false;
		int pos_crypt = pos_cryptage(i,j);
		if (new_arc) { 
			current_arc = new Arc(pos_crypt);
			new_arc = false;
		}
		else {current_arc.add_way(pos_crypt);}

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
	
/*  
	private ArrayList<ArrayList<Integer>> direction = new ArrayList<ArrayList>(new ArrayList<Integer>()) {};
		 Arrays.asList(Arrays.asList(-1,0),Arrays.asList(1,0),Arrays.asList(0,-1),Arrays.asList(0,1)); ??? gros bordel d'initialisé un
		 arrays d'array d'entier xD en pause pour l'instant

	public void create_graph(int[][] mat, int i, int j, int k, int l, Arc current_arc){
		//i,j position actuel, k,l position precedente

		boolean multi_direction = false;

		if (new_arc) { 
			current_arc = new Arc(i,j);
			new_arc = false;
		}
		else {current_arc.add_way(i,j);}

		check_elemSpecial(mat[i][j], i, j,current_arc);
		for (int i = 0; i < direction.length ; i++ ) {
			ArrayList coord = direction.get(i);
			if (test_nextPosition(mat,i,j,k,l,coord.get(0),coord.get(1))){//DOWN
				if (multi_direction) { check_newNode(i, j, current_arc);}
				create_graph(mat,i+coord.get(0),j+coord.get(1),i,j,current_arc);
				multi_direction = true;
			}
		}
	}
*/














