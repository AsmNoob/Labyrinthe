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
		long begin = System.currentTimeMillis();
		Parsing(FileName);
		long step1 = System.currentTimeMillis();
		/*System.out.println();
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
		System.out.println();*/
		//PrintMatrix(matrix);
		System.out.println("Controle_InitialSituation");
		/*System.out.println();*/
		InitialSituation();
		long step2 = System.currentTimeMillis();
		System.out.println();
		System.out.println();
		float time1 = ((float) (step1-begin)) / 1000f;
		float time2 = ((float) (step2-step1)) / 1000f;

		System.out.print("Time exe || Parsing : ");
		System.out.print(time1);
		System.out.print(" || InitialSituation : ");
		System.out.println(time2);
		System.out.print(" Total Time execution: ");
		System.out.println(time1+time2);

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
			/*for(int i = 0; i < splitted_line.length;i++){
				System.out.println(splitted_line[i]);
			}*/
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

	public void InitialSituation(){
		// renvoie la situation initiale du Labyrinthe
		try{
			PrintWriter writer = new PrintWriter ("InitialSituation.txt");
			writer.println("Situation de départ:");
			/*for(int k = 0; k < matrix.length;k++){ 
				System.out.print(k%10 + " ");
			}*/
			//System.out.println();
			for(int i = 0; i < matrix.length;i++){
				for(int j = 0; j < matrix[0].length;j++){
					//System.out.print(OutputAnalyse(matrix[i][j],i,j));
					writer.print(OutputAnalyse(matrix[i][j],i,j)); // Verifier
				}
				//System.out.print(" " + i);
				//System.out.println();
				writer.println();
			}
			writer.close();
		}catch(FileNotFoundException e){
			System.err.println("Caught FileNotFoundException in Maze InitialSituation writing: " + e.getMessage());
		}catch(NullPointerException e){
			System.err.println("Caught NullPointerException in Maze InitialSituation writing: " + e.getMessage());
		}
		
	}

	public String get_direction(int[] prePos, int[] actuPos){
		if (actuPos[0]-prePos[0]==-1) {return "nord";}
		if (actuPos[0]-prePos[0]==1) {return "sud";}
		if (actuPos[1]-prePos[1]==-1) {return "ouest";}
		return "est";
	}
	//---------- Output en terminal et écriture sur fichier ----------//

	public void FinalSituation(ArrayList<Node> way, Graph graph){
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

		// Chemin parcouru

		try {
			System.out.println("Déplacements de M.Pakkuman:");
			/*ArrayList<Integer> pred = new ArrayList<Integer>(2);
			pred.add(0);pred.add(0);
			ArrayList<Integer> actual = new ArrayList<Integer>(2);
			//(i,j): (+1,0) => sud | (-1,0) => nord | (0,-1) => ouest |(0,+1) => est
			//(i*2,j) => 2,-2,-1,1
			HashMap<Integer,String> directions = new HashMap<Integer,String>();
			directions.put(2,"sud");
			directions.put(-2,"nord");
			directions.put(-1,"ouest");
			directions.put(1,"est");
			// parcours des nodes principales
			for(int i = 0; i < way.size();i++){
				System.out.println("//---------------"+way.get(i).get_posCrypt()+"-------------------//");
				System.out.println("//---- SOUS-Noeuds-----//");
				ArrayList<Node> ens_linkNode = way.get(i).get_ensLink();
				for(int j = 0; j < ens_linkNode.size();j++){
					int posTest = ens_linkNode.get(j).get_posCrypt();
					String valTest = Integer.toString(posTest);
					int n1T =  Integer.parseInt(valTest.substring(1,(valTest.length())/2+1));
					int n2T = Integer.parseInt(valTest.substring((valTest.length())/2+1));
					n1T = (n1T%2==1) ? (n1T-1)/2 : n1T/2;
					n2T = (n2T%2==1) ? (n2T-1)/2 : n2T/2;
					System.out.println("("+n1T+","+n2T+")");
				}
				System.out.println("//---------------oOo------------------//");
				int pos = way.get(i).get_posCrypt();
				String val = Integer.toString(pos);

				int n1 =  Integer.parseInt(val.substring(1,(val.length())/2+1));
				int n2 = Integer.parseInt(val.substring((val.length())/2+1));
				n1 = (n1%2==1) ? (n1-1)/2 : n1/2;
				n2 = (n2%2==1) ? (n2-1)/2 : n2/2;
				actual.add(0,n1);actual.add(1,n2);
				System.out.println((i+1)+". ("+n1+","+n2+") "+analyse_way(way.get(i),actual,pred,directions));
				pred.add(0,n1);pred.add(1,n2);
			}
			System.out.println("Trouvé un plus court chemin de longueur"+".");
			System.out.println("M. Pakkuman a récolté "+"bonbon(s) et rencontré "+"monstre(s).");*/

			int[] pred = new int[2];
			int[] coord;
			boolean find_exit = false;


			// parcours des nodes principales
			for(int i = 1; i < way.size();i++){
				coord = graph.pos_decryptage(way.get(i-1).get_posCrypt()); //coordonée décrypté. vérifié et correcte
				coord[0]=(coord[0]-1)/2;coord[1]=(coord[1]-1)/2;
				System.out.print( "("+coord[0]+","+coord[1]+") " );
				if (i == 1) { System.out.println("Départ");
				}
				else if (way.get(i-1).isMonster()) {
					System.out.println(get_direction(pred,coord) + ", bonbon donné au petit monstre");
				}
				else if (way.get(i-1).isSweet()) {
					System.out.println(get_direction(pred,coord) + ", bonbon récolté");
				}
				else{System.out.println(get_direction(pred,coord));}

				pred = coord.clone();

				ArrayList<Integer> arc = way.get(i-1).get_arc(way.get(i)).get_globalWay();
				
				if (arc.get(0) != way.get(i-1).get_posCrypt()) { Collections.reverse(arc);}
					
				for (int j = 1; j <arc.size()-1 ;j++ ) {

					coord = graph.pos_decryptage(arc.get(j)); //coordonée décrypté. vérifié et correcte
					//System.out.println("TEST VALEURS: arc;get(j) = "+arc.get(j)+" et graph.get_exitPos() = "+graph.get_exitPos());
					if (arc.get(j) == graph.get_exitPos()) {find_exit= true;}
					coord[0]=(coord[0]-1)/2;coord[1]=(coord[1]-1)/2;
					System.out.print( "("+coord[0]+","+coord[1]+") "); 
					if (find_exit) {
						System.out.println("Sortie!");
					}
					else {System.out.println(get_direction(pred,coord));}
					pred = coord.clone();
				}
				if(way.get(i).get_posCrypt() == graph.get_exitPos()){
					System.out.println("Sortie!");
					find_exit = true;
				}
			}
			if( !find_exit){System.out.println("Il n'y a pas de sortie.");}
		}catch(NullPointerException e){
			System.err.println("Caught NullPointerException in Maze FinalSituation: " + e.getMessage());
		}
	}


	//-------------analyse_way()------------//

/*
	public String analyse_way(Node node,ArrayList<Integer> actual, ArrayList<Integer> pred,HashMap<Integer,String> directions){
		int key = (actual.get(0)-pred.get(0))*2 + (actual.get(1)-pred.get(1));
		String direction;
		if(key == 0 ){
			direction = "Départ";
		}else if (node.isSweet()){
			direction = directions.get(key)+", bonbon récolté";
		}else if (node.isMonster()){
			direction = directions.get(key)+", bonbon donné au petit monstre";
		}else if (node.isExit()){
			direction = "Sortie!";
		}else{
			direction = directions.get(key);
		}
		return direction;
	}*/

	
	//--------- Printing method ---------//

	public void PrintMatrix(int[][] matrix){
		for(int i = 0; i < matrix.length;i++){
			for(int j = 0; j < matrix[0].length;j++){
				System.out.print(OutputAnalyse(matrix[i][j],i,j));
			}
			System.out.println();
		}
	}

	// ------------ Method that analyzes the value received and returns its char value-------- //
	//------------ Used for Outputing the maze -------------//

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

}