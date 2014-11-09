	/* TIO NOGUERAS Gérard - 000333083 - INFO2 */
/* DEFONTAINE Alexis -  - INFO2 */
import java.io.*;
import java.util.*;

public class Maze{
	//Attributs
	private int matrix[][];
	private int lines; // hauteur
	private int columns; // longueur
	private int NbMonsters;
	private int NbSweets;
	private int[] PacmansPos = new int[2];
	private ArrayList<ArrayList<Integer>> MonstersList = new ArrayList<ArrayList<Integer>>();
	private ArrayList<ArrayList<Integer>> SweetsList = new ArrayList<ArrayList<Integer>>();

	// à mettre en define
	/*  Murs = -1
		Espace = 0
		Pacman = 1
		Monstre = 2
		Bonbon = 3
		Sortie = 4
	*/
	public int WALL = -1;
	public int SPACE = 0;
	public int PACMAN = 1;
	public int MONSTER = 2;
	public int SWEET = 3;
	public int EXIT = 4;
	public int WAY = 5;


	//Constructeurs
	public Maze(String FileName){
		System.out.println("Controle_MazeStart");

		Parsing(FileName);
		PrintMatrix(matrix);
	}

	//Méthodes


	//-------Parsage du fichier--------//
	public void Parsing(String FileName){
		try {
			InputStream ips = new FileInputStream(FileName);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String line;
			line = br.readLine();
			System.out.println(line);
			int lines = Character.getNumericValue(line.charAt(12));
			int columns = Character.getNumericValue(line.charAt(19));
			// int size = Character.getNumericValue(line.charAt(12)); // line[i]
			matrix = new int[lines+1][columns+1]; // [0 -> 8] 
			int indexLine = 0;
			while(indexLine < lines){
				line = br.readLine();
				int indexColumn = 0;
				for(int i = 0; i< line.length();i++){ // parcours line lue
					if(i%4 == 0){ // intersection et murs
						System.out.print(Analyse_Case(line.charAt(i),indexLine,indexColumn));
						matrix[indexLine][indexColumn] = Analyse_Case(line.charAt(i),indexLine,indexColumn); // Check
						indexColumn++;
					}else if (i%4 == 2) { // espaces, monstres, bonbons et pakkuman
						System.out.print(Analyse_Case(line.charAt(i),indexLine,indexColumn));
						matrix[indexLine][indexColumn] = Analyse_Case(line.charAt(i),indexLine,indexColumn);
						indexColumn++;
					}
				}
				System.out.println();
				indexLine++;
			}
			//Analyser la fin du fichier
			/*
			Elements du Labyrinthe: 
				Monstres: 4
				Bonbons: 3
			Emplacements:
				Pakkuman: (6,5)
				Monstres: (4,4) (5,4) (6,2) (4,0) 
				Bonbons: (2,0) (3,7) (3,5) 
			*/
			line = br.readLine(); // Elements du Labyrinthe: 
			line = br.readLine(); int NbMonsters = Character.getNumericValue(line.charAt(19)); //Monstres: 4
			line = br.readLine(); int NbSweets = Character.getNumericValue(line.charAt(18)); //Bonbons: 3
			line = br.readLine(); // Emplacements:
			line = br.readLine(); // Pakkuman: (6,5)
			line = br.readLine(); // Monstres: (4,4) (5,4) (6,2) (4,0)
			line = br.readLine(); // Bonbons: (2,0) (3,7) (3,5)


		}catch(FileNotFoundException e){
			System.err.println("Caught FileNotFoundException: " + e.getMessage());
		}catch(IOException e){
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}

	//Fonction qui remplace les caractères par des entiers selon
	//les values choisies
	public int Analyse_Case(char value, int line, int column){
		int res = 0;
		if(value == '-' || value == '|' || value == '+'){
			res = WALL;
		}else if (value == 'P'){
			PacmansPos[0] = line;
			PacmansPos[1] = column;
			res = PACMAN;
		}else if (value == ' ') {
			if(line == 0 || line == lines || column == 0 || column == columns){ //rappel la matrix est en [size+1][size+1]
				res = EXIT;
			}else{
				res = SPACE;
			}
		}else if (value == 'B') {
			//TROUVER UNE SOLUTION
			ArrayList<Integer> coord = new ArrayList<Integer>(2);
			coord.add(line);coord.add(column);
			SweetsList.add(coord);
			res = SWEET;
		}else if (Character.getNumericValue(value) >= 1){
			ArrayList<Integer> coord = new ArrayList<Integer>(2);
			coord.add(line);coord.add(column);
			MonstersList.add(coord);
			res = MONSTER;
		}
		return res;
	}

	//------ Getters/Setters ------//
	public int[] getCoordPacman(){
		return PacmansPos;
	}

	public ArrayList<ArrayList<Integer>> getCoordMonsters(){ // ArrayListe[n][2] 
		return MonstersList;
	}

	public ArrayList<ArrayList<Integer>> getCoordSweets(){
		return SweetsList;
	}

	public int[][] getMaze(){
		return matrix;
	}

	public voic InitialSituation(int[][] matrix){
		// renvoie la situation initiale du Labyrinthe
		for(int i = 0; i < matrix.length;i++){
			for(int j = 0; j < matrix[0].length;j++){
				PrintWriter.print(OutputAnalyse(matrix[i][j],i,j)); // Verifier
			}
			PrinterWriter.println();
		
	}

	public void PrintMatrix(int[][] matrix){
		System.out.println(matrix.length);
		for(int i = 0; i < matrix.length;i++){
			for(int j = 0; j < matrix[0].length;j++){
				System.out.print(OutputAnalyse(matrix[i][j],i,j));
			}
			System.out.println();
		}
	}

	public String OutputAnalyse(int value,int line,int column){
		String res = "";
		if(value == PACMAN){
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