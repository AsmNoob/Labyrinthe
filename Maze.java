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
	private int NbMonsters;
	private int NbSweets;
	private int[] PukkamansPos = new int[2];
	private ArrayList<ArrayList<Integer>> MonstersList = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> SweetsList = new ArrayList<ArrayList<Integer>>();

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
	public Maze(String FileName){
		System.out.println("Controle_MazeStart");
		Parsing(FileName);
		System.out.println();
		System.out.println("Controle_MazeParsedObjects");
		System.out.println();
		System.out.print("PukkamansPos: ");
		System.out.print("(");System.out.print(PukkamansPos[0]);System.out.print(","); System.out.print(PukkamansPos[1]);System.out.println(")");
		System.out.print("NbMonsters: ");
		System.out.println(NbMonsters);
		System.out.print("NbSweets: ");
		System.out.println(NbSweets);
		System.out.print("lines: ");
		System.out.println(lines);
		System.out.print("columns: ");
		System.out.println(columns);
		System.out.println();
		System.out.println("Controle_MazePrint");
		System.out.println();
		//PrintMatrix(matrix);
		System.out.println();
		System.out.println("Controle_InitialSituation");
		System.out.println();
		InitialSituation(matrix);
		System.out.println();
		System.out.println("Controle_FinalSituation");
		System.out.println();
		FinalSituation(matrix);
		System.out.println();
	}

	//Méthodes


	//-------Parsage du fichier--------//
	public void Parsing(String FileName){
		try {
			InputStream ips = new FileInputStream(FileName);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			String[] splitted_line;
			line = br.readLine();
			splitted_line = line.split(" ");
			for(int i = 0; i < splitted_line.length;i++){
				System.out.println(splitted_line[i]);
			}
			lines = Integer.parseInt(splitted_line[1]);
			columns = Integer.parseInt(splitted_line[3]);
			matrix = new int[2*lines+1][2*columns+1]; // [0 -> 8] 
			int indexLine = 0;
			while(indexLine < lines*2+1){
				line = br.readLine();
				int indexColumn = 0;
				for(int i = 0; i< line.length();i++){ // parcours line lue
					if(i%4 == 0){ // intersection et murs
						// TO CLEAN: System.out.print(Analyse_Case(line.charAt(i),indexLine,indexColumn));
						matrix[indexLine][indexColumn] = Analyse_Case(line.charAt(i),indexLine,indexColumn); // Check
						indexColumn++;
					}else if (i%4 == 2) { // espaces, monstres, bonbons et pakkuman
						// TO CLEAN: System.out.print(Analyse_Case(line.charAt(i),indexLine,indexColumn));
						matrix[indexLine][indexColumn] = Analyse_Case(line.charAt(i),indexLine,indexColumn);
						indexColumn++;
					}
				}
				indexLine++;
			}
			line = br.readLine(); // Elements du Labyrinthe: 
			line = br.readLine();
			splitted_line = line.split(" ");
			/*for(int i = 0; i < splitted_line.length;i++){
				System.out.println(splitted_line[i]);
			}*/
			NbMonsters = Integer.parseInt(splitted_line[1]); //Monstres: 4
			line = br.readLine();
			splitted_line = line.split(" ");
			/*for(int i = 0; i < splitted_line.length;i++){
				System.out.println(splitted_line[i]);
			}*/
			NbSweets = Integer.parseInt(splitted_line[1]); //Bonbons: 3
			line = br.readLine(); // Emplacements:
			line = br.readLine(); 
			splitted_line = line.split(" "); // = ["Pukkaman:","(x,y)"]
			/*for(int i = 0; i < splitted_line.length;i++){
				System.out.println(splitted_line[i]);
			}*/
			//System.out.print("TEST SPLIT: "); System.out.println(splitted_line[1]);
			splitted_line[1] = splitted_line[1].replace("(","");
			splitted_line[1] = splitted_line[1].replace(")","");
			String coordonnee[] = splitted_line[1].split(",");
			/*for(int i  = 0; i < 2; i++){
				System.out.print(coordonnee[i]);System.out.print(" "); //
			}*/

			//int coord0 = Integer.parseInt(splitted_line[1].split(",")[0]);
			//int coord1 = 
			PukkamansPos[0] = Integer.parseInt(coordonnee[0]);
			PukkamansPos[1] = Integer.parseInt(coordonnee[1]); // Pakkuman: (6,5)
			//System.out.print("PAKKUMAN [0] - [1]: "); System.out.print(PukkamansPos[0]);System.out.print(PukkamansPos[1]);
			matrix[PukkamansPos[0]*2+1][PukkamansPos[1]*2+1] = PUKKAMAN;
			line = br.readLine();
			line = line.replaceAll( "\\(" , "" );
			line = line.replaceAll( "\\)" , "" );
			splitted_line = line.split(" "); // Monstres: (4,4) (5,4) (6,2) (4,0) 
			for(int j = 1; j < splitted_line.length;j++){ // j = 1 pour commencer à partir des coordonnées
				ArrayList<Integer> coord = new ArrayList<Integer>(2);
				//System.out.println(splitted_line[j]);
				//System.out.print("TEST WEIRD SH33T: "); System.out.println(splitted_line[j].split(",")[0]);
				coord.add(Integer.parseInt(splitted_line[j].split(",")[0]));
				coord.add(Integer.parseInt(splitted_line[j].split(",")[1]));
				MonstersList.add(coord);
				matrix[coord.get(0)*2+1][coord.get(1)*2+1] = MONSTER;
			}
			line = br.readLine();  
			line = line.replaceAll( "\\(" , "" );
			line = line.replaceAll( "\\)" , "" );
			splitted_line = line.split(" ");
			for(int j = 1; j < splitted_line.length;j++){ // j = 1 pour commencer à partir des coordonnées
				ArrayList<Integer> coord = new ArrayList<Integer>(2);
				//System.out.println(splitted_line[j]);
				//System.out.print("TEST WEIRD SH00T: "); System.out.println(splitted_line[j].split(",")[0]);
				coord.add(Integer.parseInt(splitted_line[j].split(",")[0]));
				coord.add(Integer.parseInt(splitted_line[j].split(",")[1]));
				SweetsList.add(coord);
				matrix[coord.get(0)*2+1][coord.get(1)*2+1] = SWEET;
			}
		}catch(FileNotFoundException e){
			System.err.println("Caught FileNotFoundException: " + e.getMessage());
		}catch(IOException e){
			System.err.println("Caught IOException: " + e.getMessage());
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

	public int[] getCoordPukkaman(){
		return PukkamansPos;
	}

	public ArrayList<ArrayList<Integer>> getCoordMonsters(){ // ArrayList[n][2] 
		return MonstersList;
	}

	public ArrayList<ArrayList<Integer>> getCoordSweets(){
		return SweetsList;
	}

	public int[][] getMaze(){
		return matrix;
	}

	//------ MazeSituations ------//

	public void InitialSituation(int[][] matrix){
		// renvoie la situation initiale du Labyrinthe
		try{
			PrintWriter writer = new PrintWriter ("InitialSituation.txt");
			writer.println("Situation de départ:");
			for(int i = 0; i < matrix.length;i++){
				for(int j = 0; j < matrix[0].length;j++){
					System.out.print(OutputAnalyse(matrix[i][j],i,j));
					writer.print(OutputAnalyse(matrix[i][j],i,j)); // Verifier
				}
				System.out.println();
				writer.println();
			}
			writer.close();
		}catch(FileNotFoundException e){
			System.err.println("Caught FileNotFoundException: " + e.getMessage());
		}
		
	}

	public void FinalSituation(int[][] matrix){
		System.out.println();
		System.out.print("Le labyrinthe a une dimension de ");System.out.print(lines);System.out.print(" fois ");System.out.print(columns);System.out.println(".");
		System.out.print("Il contient ");System.out.print(NbMonsters);System.out.print(" monstres et ");System.out.print(NbSweets);System.out.println(" bonbons.");
		System.out.print("M.Pakkuman se trouve en position: (");System.out.print(PukkamansPos[0]);System.out.print(",");System.out.print(PukkamansPos[1]);System.out.println(")");
		System.out.print("Le(s) monstre(s) se trouve(nt) en position:");
		for(int i = 0; i < NbMonsters; i++){
			System.out.print(" (");System.out.print(MonstersList.get(i).get(0));System.out.print(",");System.out.print(MonstersList.get(i).get(1));System.out.print(")");
		}
		System.out.println();
		System.out.print("Le(s) bonbon(s) se trouve(nt) en position:");
		for(int i = 0; i < NbSweets; i++){
			System.out.print(" (");System.out.print(SweetsList.get(i).get(0));System.out.print(",");System.out.print(SweetsList.get(i).get(1));System.out.print(")");
		}
		System.out.println();
		System.out.println();
		System.out.println("Déplacements de M.Pakkuman:");


	}

	public void PrintMatrix(int[][] matrix){
		for(int i = 0; i < matrix.length;i++){
			for(int j = 0; j < matrix[0].length;j++){
				System.out.print(OutputAnalyse(matrix[i][j],i,j));
			}
			System.out.println();
		}
	}

	public String OutputAnalyse(int value,int line,int column){
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

	//--------- Check autour de la case des types d'objets trouvés --------//
}