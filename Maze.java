	/* TIO NOGUERAS Gérard - 000333083 - INFO2 */
/* DEFONTAINE Alexis -  - INFO2 */
import java.io.*;

public class Maze{
	//Attributs
	private char matriceFichier[][];//regarder si on mettrait pas un truc plus léger que des int
	private int matrice[][];
	private int lignes; // hauteur
	private int colonnes; // longueur
	private int[] PosPacman = new int[2];

	//define
	public int WALL = -1;
	public int SPACE = 0;
	public int PACMAN = 1;
	public int MONSTER = 2;
	public int SWEET = 3;
	public int EXIT = 4;


	//Constructeurs
	public Maze(String Nomfichier){
		ParsageFichier(Nomfichier);
	}
	
	// à mettre en define
	/*  Murs = -1
		Espace = 0
		Pacman = 1
		Monstre = 2
		Bonbon = 3
		Sortie = 4
	*/

	//Méthodes
	//Fonction qui remplace les caractères par des entiers selon
	//les valeurs choisies
	public int BaseToCode(char valeur){
		int res = 0;
		if(valeur == '-' || valeur == '|'){
			res = WALL;
		}else if (valeur == 'P') {
			res = PACMAN;
		}else if (valeur == ' ') {
			res = SPACE;
		}else if (valeur == '°') {
			res = SWEET;
		}else if (Character.getNumericValue(valeur) >= 1){
			res = MONSTER;
		}
		return res;
	}

	//-------Parsage du fichier--------//
	public void ParsageFichier(String Nomfichier){
		try {
			InputStream ips = new FileInputStream(Nomfichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			ligne = br.readLine();
			System.out.println(ligne);
			int taille = Character.getNumericValue(ligne.charAt(12)); // ligne[i]
			matriceFichier = new char[taille+1][taille+1]; // [0 -> 8] 
			int indiceLigne = 0;
			while((ligne = br.readLine())!= null){
				int indice = 0;
				for(int i = 0; i< ligne.length();i++){
					if(ligne.charAt(i) == 'P'){
						PosPacman[0] = indiceLigne;
						PosPacman[1] = indice;
					}
					if(i%4 == 0){
						matriceFichier[indiceLigne][indice] = ligne.charAt(i);
						indice++;
					}else if (i%4 == 2) {
						matriceFichier[indiceLigne][indice] = ligne.charAt(i);
						indice++;
					}
				}
				System.out.println(matriceFichier[indiceLigne]);
				indiceLigne++;
			}
		}catch(FileNotFoundException e){
			System.err.println("Caught FileNotFoundException: " + e.getMessage());
		}catch(IOException e){
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}

	//------ Getters/Setters ------//
	public int[] getPosPacman(){
		return PosPacman;
	}


	public int[][] getMaze(){
		return matrice;
	}

	//--------- Check autour de la case des types d'objets trouvés --------//
}