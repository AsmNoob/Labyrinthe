/* TIO NOGUERAS Gérard - 000333083 - INFO2 */
/* DEFONTAINE Alexis -  - INFO2 */
import java.io.*;

public class Labyrinthe{
	//Attributs
	private char matriceFichier[][];//regarder si on mettrait pas un truc plus léger que des int
	private char matriceLiens[][];
	private int lignes; // hauteur
	private int colonnes; // longueur

	//Constructeurs
	public Labyrinthe(String Nomfichier){
		ParsageFichier(Nomfichier);
	}

	//Méthodes

	//-------Parsage du fichier--------//
	public void ParsageFichier(String Nomfichier){
		try {
			InputStream ips = new FileInputStream(Nomfichier);
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while((ligne = br.readLine())!= null){
				System.out.println(ligne);
				System.out.println(ligne.length());
			}
		}catch(FileNotFoundException e){
			System.err.println("Caught FileNotFoundException: " + e.getMessage());
		}catch(IOException e){
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}

	//------Création de la matrice "des liens" ------//
	public void CreationMatrice(int[][] matrice){
		for(int i=0; i< lignes;i++){
			for(int j = 0; j < colonnes;j++){
				//trouverLien(matrice[i*2][j*2]);// regarder autours de chaque case
			}
		}
	}

	//--------- Check autour de la case des types d'objets trouvés --------//
}