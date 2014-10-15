/* TIO NOGUERAS Gérard - 000333083 - INFO2 */
/* DEFONTAINE Alexis -  - INFO2 */

public class Labyrinthe{
	//Attributs
	private char matriceFichier[][];//regarder si on mettrait pas un truc plus léger que des int
	private char matriceLiens[][];
	private int lignes; // hauteur
	private int colonnes; // longueur
	//Méthodes

	//-------Parsage du fichier--------//
	public void ParsageFichier(string Nomfichier){ 
		// (1) lecture 1ère ligne 
		lignes,colonnes = readline().split("x") // ou l'équivalent
		for(int i= 0; i< 2*lignes+1;i++){ // au lieu d'un while pour éviter de faire 1 check su le "\0"
			stringLu = readline();
			for(int j = 0; j< 2*colonnes+1;j++){
				matriceFichier[i][j] = stringLu[j]; //place chaque char du fichier dans une matrice
			}
		}
	}

	//------Création de la matrice "des liens" ------//
	public void CreationMatrice(int[][] matrice){
		for(int i=0; i< lignes;i++){
			for(int j = 0; j < colonnes;j++){
				trouverLien(matrice[i*2][j*2]);// regarder autours de chaque case
			}
		}
	}

	//--------- Check autour de la case des types d'objets trouvés --------//
	public void trouverlien()
}