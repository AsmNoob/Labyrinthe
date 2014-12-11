/* TIO NOGUERAS Gérard - 000333083 - INFO2 */
/* DEFONTAINE Alexis -  - INFO2 */
import java.io.*;
import java.util.*;
import java.lang.*; // String

public class Maze{
	//Attributs
	private int matrix[][];
	private int lines; // hauteur
	private int columns; // longueur
	private int number_monsters;
	private int number_sweets;
	private int[] pukkaman_position = new int[2];
	private ArrayList<ArrayList<Integer>> monsters_list = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> sweets_list = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> complete_way = new ArrayList<ArrayList<Integer>>();
			

	// à mettre en define
	/*  Murs = -1
		Espace = 0
		Pukkaman = 1
		Monstre = 2
		Bonbon = 3
		Sortie = 4
	*/
	public int WALL = -1;
	public int SPACE = 0;
	public int PUKKAMAN = 1;
	public int MONSTER = 2;
	public int SWEET = 3;
	public int EXIT = 4;
	public int WAY = 5;


	//Constructeurs
	public Maze(String file_name){
		long begin = System.currentTimeMillis();
		Parsing(file_name);
		long step1 = System.currentTimeMillis();
		Initial_Situation();
		long step2 = System.currentTimeMillis();
		System.out.println();
		System.out.println();
		float time1 = ((float) (step1-begin)) / 1000f;
		float time2 = ((float) (step2-step1)) / 1000f;

		System.out.print("Time exe || Parsing : ");
		System.out.print(time1);
		System.out.print(" || Initial_Situation : ");
		System.out.println(time2);
		System.out.print(" Total Time execution: ");
		System.out.println(time1+time2);

	}

	//Méthodes


	//-------Parsage du fichier--------//
	public void Parsing(String file_name){
		try {
			InputStream ips = new FileInputStream(file_name);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			String[] splitted_line;
			line = br.readLine();
			splitted_line = line.split(" ");
			lines = Integer.parseInt(splitted_line[1]);
			columns = Integer.parseInt(splitted_line[3]);
			matrix = new int[2*lines+1][2*columns+1]; // [0 -> 8] 
			int indexLine = 0;
			while(indexLine < lines*2+1){
				line = br.readLine();
				int indexColumn = 0;
				for(int i = 0; i< line.length();i++){ // parcours line lue
					if(i%4 == 0){ // intersection et murs
						matrix[indexLine][indexColumn] = Analyse_Case(line.charAt(i),indexLine,indexColumn); // Check
						indexColumn++;
					}else if (i%4 == 2) { // espaces, monstres, bonbons et pakkuman
						matrix[indexLine][indexColumn] = Analyse_Case(line.charAt(i),indexLine,indexColumn);
						indexColumn++;
					}
				}
				indexLine++;
			}
			line = br.readLine(); // Elements du Labyrinthe: 
			line = br.readLine();
			splitted_line = line.split(" ");
			number_monsters = Integer.parseInt(splitted_line[1]); //Monstres: 4
			line = br.readLine();
			splitted_line = line.split(" ");
			number_sweets = Integer.parseInt(splitted_line[1]); //Bonbons: 3
			line = br.readLine(); // Emplacements:
			line = br.readLine(); 
			splitted_line = line.split(" "); // = ["Pukkaman:","(x,y)"]
			splitted_line[1] = splitted_line[1].replace("(","");
			splitted_line[1] = splitted_line[1].replace(")","");
			String coordonnee[] = splitted_line[1].split(",");
			pukkaman_position[0] = Integer.parseInt(coordonnee[0]); // Pakkuman: (6,5)
			pukkaman_position[1] = Integer.parseInt(coordonnee[1]); 
			matrix[pukkaman_position[0]*2+1][pukkaman_position[1]*2+1] = PUKKAMAN;
			line = br.readLine();
			line = line.replaceAll( "\\(" , "" );
			line = line.replaceAll( "\\)" , "" );
			splitted_line = line.split(" "); // Monstres: (4,4) (5,4) (6,2) (4,0) 
			for(int j = 1; j < splitted_line.length;j++){ // j = 1 pour commencer à partir des coordonnées
				ArrayList<Integer> coord = new ArrayList<Integer>(2);
				coord.add(Integer.parseInt(splitted_line[j].split(",")[0]));
				coord.add(Integer.parseInt(splitted_line[j].split(",")[1]));
				monsters_list.add(coord);
				matrix[coord.get(0)*2+1][coord.get(1)*2+1] = MONSTER;
			}
			line = br.readLine();  
			line = line.replaceAll( "\\(" , "" );
			line = line.replaceAll( "\\)" , "" );
			splitted_line = line.split(" ");
			for(int j = 1; j < splitted_line.length;j++){ // j = 1 pour commencer à partir des coordonnées
				ArrayList<Integer> coord = new ArrayList<Integer>(2);
				coord.add(Integer.parseInt(splitted_line[j].split(",")[0]));
				coord.add(Integer.parseInt(splitted_line[j].split(",")[1]));
				sweets_list.add(coord);
				matrix[coord.get(0)*2+1][coord.get(1)*2+1] = SWEET;
			}
		}catch(FileNotFoundException e){
			System.err.println("Caught FileNotFoundException in Maze Parsing: " + e.getMessage());
		}catch(IOException e){
			System.err.println("Caught IOException in Maze Parsing: " + e.getMessage());
		}
	}

	//Fonction qui remplace les caractères par des entiers selon les values choisies
	public int Analyse_Case(char value, int line, int column){
		int res = 0;
		if(value == '-' || value == '|' || value == '+'){
			res = WALL;
		}else if (value == ' ') {
			if(line == 0 || line == lines*2 || column == 0 || column == columns*2){ //rappel la matrix est en [size+1][size+1]
				res = EXIT;
			}else{
				res = SPACE;
			}
		}
		return res;
	}

	//------ Getters/Setters ------//

	public ArrayList<ArrayList<Integer>> get_completeWay(){
		return complete_way;
	}

	public int[] getCoordPukkaman(){
		return pukkaman_position;
	}

	public ArrayList<ArrayList<Integer>> getCoordMonsters(){ // ArrayList[n][2] 
		return monsters_list;
	}

	public ArrayList<ArrayList<Integer>> getCoordSweets(){
		return sweets_list;
	}

	public int[][] getMaze(){
		return matrix;
	}

	//------ MazeSituations ------//

	public void Initial_Situation(){
		// renvoie la situation initiale du Labyrinthe
		try{
			PrintWriter writer = new PrintWriter ("InitialSituation.txt");
			writer.println("Situation de départ:");
			matrix_Printer(writer);
			writer.close();
		}catch(FileNotFoundException e){
			System.err.println("Caught FileNotFoundException in Maze Initial_Situation writing: " + e.getMessage());
		}catch(NullPointerException e){
			System.err.println("Caught NullPointerException in Maze Initial_Situation writing: " + e.getMessage());
		}
		
	}

	public void matrix_Printer(PrintWriter writer){
		for(int i = 0; i < matrix.length;i++){
			for(int j = 0; j < matrix[0].length;j++){
				//System.out.print(Output_Analyse(matrix[i][j],i,j));
				writer.print(Output_Analyse(matrix[i][j],i,j)); // Verifier
			}
			//System.out.print(" " + i);
			//System.out.println();
			writer.println();
		}
	}

	public String get_direction(int[] pre_pos, int[] actu_pos){
		if (actu_pos[0]-pre_pos[0]==-1) {return "nord";}
		if (actu_pos[0]-pre_pos[0]==1) {return "sud";}
		if (actu_pos[1]-pre_pos[1]==-1) {return "ouest";}
		return "est";
	}
	//---------- Output en terminal et écriture sur fichier ----------//

	public void Final_Situation(ArrayList<Node> way, Graph graph,int length){
		System.out.println();
		System.out.print("Le labyrinthe a une dimension de ");System.out.print(lines);System.out.print(" fois ");System.out.print(columns);System.out.println(".");
		System.out.print("Il contient ");System.out.print(number_monsters);System.out.print(" monstres et ");System.out.print(number_sweets);System.out.println(" bonbons.");
		System.out.print("M.Pakkuman se trouve en position: (");System.out.print(pukkaman_position[0]);System.out.print(",");System.out.print(pukkaman_position[1]);System.out.println(")");
		System.out.print("Le(s) monstre(s) se trouve(nt) en position:");
		for(int i = 0; i < number_monsters; i++){
			System.out.print(" (");System.out.print(monsters_list.get(i).get(0));System.out.print(",");System.out.print(monsters_list.get(i).get(1));System.out.print(")");
		}
		System.out.println();
		System.out.print("Le(s) bonbon(s) se trouve(nt) en position:");
		for(int i = 0; i < number_sweets; i++){
			System.out.print(" (");System.out.print(sweets_list.get(i).get(0));System.out.print(",");System.out.print(sweets_list.get(i).get(1));System.out.print(")");
		}
		System.out.println();
		System.out.println();

		// Chemin parcouru

		try {
			int[] pred = new int[2];
			int[] coord;
			boolean find_exit = false;
			int numbre_monsters = 0;
			int number_sweets = 0;
			int index = 0;
			String deplacement = "";
			if(way.get(way.size()-1).isExit()){
				System.out.println("Déplacements de M.Pakkuman:");
				for(int i = 1; i < way.size();i++){
					index++;
					coord = graph.pos_decryptage(way.get(i-1).get_posCrypt()); //coordonée décrypté. vérifié et correcte
					coord[0]=(coord[0]-1)/2;coord[1]=(coord[1]-1)/2;
					System.out.print( index+". ("+coord[0]+","+coord[1]+") " );
					deplacement+="("+coord[0]+","+coord[1]+") ";
					ArrayList<Integer> coor = new ArrayList<Integer>(2);
					coor.add(coord[0]);coor.add(coord[1]);
					complete_way.add(coor);
					if (i == 1) { System.out.println("Départ");
					}
					else if (way.get(i-1).isMonster()) {
						System.out.println(get_direction(pred,coord) + ", bonbon donné au petit monstre");
						numbre_monsters+=1;
					}
					else if (way.get(i-1).isSweet()) {
						System.out.println(get_direction(pred,coord) + ", bonbon récolté");
						number_sweets+=1;
					}
					else{
						// Comme la position n'est ni B, ni M => on peut écrire dessus
						if(coord[0] != pukkaman_position[0] || coord[1] != pukkaman_position[1]){
							matrix[coord[0]*2+1][coord[1]*2+1] = WAY;
						}
						System.out.println(get_direction(pred,coord));}

					pred = coord.clone();
					if(way.get(i-1).get_arc(way.get(i)) != null){
						ArrayList<Integer> arc = way.get(i-1).get_arc(way.get(i)).get_globalWay();
						if (arc.get(0) != way.get(i-1).get_posCrypt()) { Collections.reverse(arc);}
						//System.out.println("TEST VALEURS: arc;get(i-1) = "+way.get(i-1).get_posCrypt()+" et graph.get_exitPos() = "+graph.get_exitPos());
						for (int j = 1; j <arc.size()-1 ;j++ ) {
							index++;
							coord = graph.pos_decryptage(arc.get(j)); //coordonée décrypté. vérifié et correcte
							//System.out.println(" j = "+j+"arc size ="+ arc.size());
							if (arc.get(j) == graph.get_exitPos()) {find_exit= true;}
							coord[0]=(coord[0]-1)/2;coord[1]=(coord[1]-1)/2;
							System.out.print( index+". ("+coord[0]+","+coord[1]+") ");
							deplacement+="("+coord[0]+","+coord[1]+") ";
							coor = new ArrayList<Integer>(2);
							coor.add(coord[0]);coor.add(coord[1]);
							complete_way.add(coor);
							if(coord[0] != pukkaman_position[0] || coord[1] != pukkaman_position[1]){
								matrix[coord[0]*2+1][coord[1]*2+1] = WAY;
							}
							// Pas très propre
							if (way.get(i).get_posCrypt() == graph.get_exitPos() && j == (arc.size()-2)) {
								System.out.println("Sortie!");
								find_exit = true;
							}
							else {System.out.println(get_direction(pred,coord));}
							pred = coord.clone();
						}
						if(way.get(i).get_posCrypt() == graph.get_exitPos() && !find_exit){
							System.out.println("Sortie!");
							find_exit = true;
						}
					}
				}
				System.out.println();System.out.println("Trouvé un plus court chemin de longueur "+index+".");
				System.out.println("M. Pakkuman a récolté "+number_sweets+" bonbon(s) et rencontré "+numbre_monsters+" monstre(s).");
			}else{
				System.out.println("Il n'y a pas moyen de sortir vivant du labyrinthe.");
			}
			// parcours des nodes principales
			
				
			PrintWriter writer = new PrintWriter ("FinalSituation.txt");
			writer.println("Situation finale:");
			matrix_Printer(writer);
			if(find_exit){
				writer.println("Trouvé un plus court chemin de longueur "+index+".");
			}else{
				writer.println("Il n'y a pas moyen de sortir.");
			}
			
			writer.println("M. Pakkuman a pris "+number_sweets+" Bonbons !");
			writer.println("Déplacements de M.Pakkuman: "+deplacement);
			writer.close();
		/*}catch(NullPointerException e){
			System.err.println("Caught NullPointerException in Maze Final_Situation: " + e.getMessage());*/
		}catch(FileNotFoundException e){
			System.err.println("Caught FileNotFoundException in Final_Situation: " + e.getMessage());
		}
	}

	// ------------ Method that analyzes the value received and returns its char value-------- //
	//------------ Used for Outputing the maze -------------//

	public String Output_Analyse(int value,int line,int column){
		String res = "PROBLEM"; // Pour résoudre le cas où res n'est pas initialisé

		if(value == PUKKAMAN){
			res = " P ";
		}else if (value == MONSTER ) {
			res = " M ";
		}else if (value == SWEET ) {
			res = " B ";
		}else if (value == SPACE ) {
			if(column%2 == 0){
				res = " ";
			}else{
				res = "   ";
			}
		}else if (value == EXIT ) {
			res = " S ";
		}else if (value == WALL ) {
			if (line%2 == 0 && column%2 == 0) {
				res = "+";
			}else if (line%2 == 0) {
				res = "---";
			}else if (line%2 == 1 && column%2 == 0) {
				res = "|";
			}
		}else if (value == WAY) {
			res = " # ";
		}
		return res;
	}

}