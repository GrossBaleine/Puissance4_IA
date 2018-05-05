package fr.uha.ensisa.puissance4.data;

import fr.uha.ensisa.puissance4.util.Constantes;

public class Partie {
	
	private Joueur joueur1, joueur2;
	private long tempsReflexionJoueur1,tempsReflexionJoueur2;
	private Joueur joueurCourant;
	private int tour;
	private Grille grille;
	private int etatPartie;

	public Partie(Joueur joueur1, Joueur joueur2) {
		this.joueur1 = joueur1;
		this.joueur2 = joueur2;
		this.tempsReflexionJoueur1=0;
		this.tempsReflexionJoueur2=0;
		this.joueurCourant = joueur1;
		this.etatPartie=Constantes.PARTIE_EN_COURS;
		tour=1;
		grille=new Grille();
	}
	
	/**
	 * Renvoie le joueur 1
	 * @return Joueur joueur1
	 */
	public Joueur getJoueur1()
	{
		return joueur1;
	}
	
	/**
	 * Renvoie le joueur 2
	 * @return Joueur joueur2
	 */

	public Joueur getJoueur2()
	{
		return joueur2;
	}
	
	/**
	 * Renvoie le joueur courant
	 * @return Joueur
	 */
	public Joueur getJoueurCourant()
	{
		return joueurCourant;
	}
	
	/**
	 * Renvoie le numéro du tour actuel
	 * @return
	 */
	public int getTour()
	{
		return tour;
	}
	
	/**
	 * Renvoie la grille
	 * @return
	 */
	public Grille getGrille()
	{
		return grille;
	}
	
	/**
	 * Indique si la partie est finie
	 * @return
	 */
	public boolean isPartieFinie()
	{
		return etatPartie!=Constantes.PARTIE_EN_COURS;
	}

	/**
	 * Joue le coup indiqué (colonne où mettre un symbole)
	 * @param colonne
	 * @param tempsReflexion
	 * @return
	 */
	public boolean jouerCoup(int colonne, long tempsReflexion) {
		if(!grille.isCoupPossible(colonne))
		{
			return false;
		}
		
		if(joueurCourant==joueur1)
		{
			grille.ajouterCoup(colonne, Constantes.SYMBOLE_J1);
			tempsReflexionJoueur1+=tempsReflexion;
			verificationFinPartie();
			joueurCourant= joueur2;
		}
		else
		{
			grille.ajouterCoup(colonne, Constantes.SYMBOLE_J2);
			tempsReflexionJoueur2+=tempsReflexion;
			verificationFinPartie();
			joueurCourant=joueur1;
		}
		tour++;
		return true;
	}
	
	public long getTempsReflexionJ1()
	{
		return tempsReflexionJoueur1;
	}
	
	public long getTempsReflexionJ2()
	{
		return tempsReflexionJoueur2;
	}
	
	/**
	 * Mets à jour l'état de la partie
	 */
	private void verificationFinPartie()
	{		
		etatPartie=grille.getEtatPartie(joueurCourant.getSymbole(), tour);		
	}
	
	/**
	 * Renvoie l'état de la partie
	 * @return
	 */
	public int getEtatPartie()
	{
		return etatPartie;
	}

}
