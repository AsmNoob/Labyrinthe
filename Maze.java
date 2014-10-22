	/* TIO NOGUERAS Gérard - 000333083 - INFO2 */
/* DEFONTAINE Alexis -  - INFO2 */
import java.io.*;
import java.util.*;

public class Maze{
	//Attributs
	private int matrix[][];
	private int lines; // hauteur
	private int columns; // longueur
	private int[] PosPacman = new int[2];
	private List<List<Integer>> listMonsters;
	private List<List<Integer>> listSweets;

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


	//Constructeurs
	public Maze(String FileName){
		Parsing(FileName);
		printMatrix(matrix);
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
			int size = Character.getNumericValue(line.charAt(12)); // line[i]
			matrix = new int[size+1][size+1]; // [0 -> 8] 
			int indexLine = 0;
			while((line = br.readLine())!= null){
				int indexColumn = 0;
				for(int i = 0; i< line.length();i++){ // parcours line lue
					if(i%4 == 0){ // intersection et murs
						System.out.print(Analyse_Case(line.charAt(i),indexLine,indexColumn,size));
						matrix[indexLine][indexColumn] = Analyse_Case(line.charAt(i),indexLine,indexColumn,size);
						indexColumn++;
					}else if (i%4 == 2) { // espaces, monstres, bonbons et pakkuman
						System.out.print(Analyse_Case(line.charAt(i),indexLine,indexColumn,size));
						matrix[indexLine][indexColumn] = Analyse_Case(line.charAt(i),indexLine,indexColumn,size);
						indexColumn++;
					}
				}
				System.out.println();
				indexLine++;
			}
		}catch(FileNotFoundException e){
			System.err.println("Caught FileNotFoundException: " + e.getMessage());
		}catch(IOException e){
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}

	//Fonction qui remplace les caractères par des entiers selon
	//les values choisies
	public int Analyse_Case(char value, int line, int column, int size){
		int res = 0;
		if(value == '-' || value == '|' || value == '+'){
			res = WALL;
		}else if (value == 'P'){
			PosPacman[0] = line;
			PosPacman[1] = column;
			res = PACMAN;
		}else if (value == ' ') {
			if(line == 0 || line == size || column == 0 || column == size){ //rappel la matrix est en [size+1][size+1]
				res = EXIT;
			}else{
				res = SPACE;
			}
		}else if (value == '°') {
			List<Integer> coord = Arrays.asList(line,column);
			listSweets = Arrays.asList(coord);
			res = SWEET;
		}else if (Character.getNumericValue(value) >= 1){
			List<Integer> coord = Arrays.asList(line,column);
			System.out.println(coord);
			listMonsters = Arrays.asList(coord);
			res = MONSTER;
		}
		return res;
	}

	//------ Getters/Setters ------//
	public int[] getCoordPacman(){
		return PosPacman;
	}

	public List<List<Integer>> getCoordMonsters(){ // liste[n][2] 
		return listMonsters;
	}

	public List<List<Integer>> getCoordSweets(){
		return listSweets;
	}

	public int[][] getMaze(){
		return matrix;
	}

	public void printMatrix(int[][] matrix){
		System.out.println(matrix.length);
		for(int i = 0; i < matrix.length;i++){
			for(int j = 0; j < matrix[0].length;j++){
				System.out.print(OutputAnalyse(matrix[i][j]),i,j);
			}
			System.out.println();
		}
	}

	public char OutputAnalyse(int value,int line,int column){
		char res = '';
		if(value == PACMAN)
			res = 'P';
		else if (value == MONSTER ) {
			
		}else if (value ==  ) {
			
		}else if (value ==  ) {
			
		}else if (value ==  ) {
			
		}

	}

	//--------- Check autour de la case des types d'objets trouvés --------//
}