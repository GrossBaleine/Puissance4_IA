package fr.uha.ensisa.puissance4.data;

import java.util.ArrayList;
import java.util.stream.IntStream;

import fr.uha.ensisa.puissance4.util.Constantes;
import fr.uha.ensisa.puissance4.util.Constantes.Case;

public class Grille {

	private Case[][] grille;

	public Grille() {
		grille = new Case[Constantes.NB_COLONNES][Constantes.NB_LIGNES];
		for (int i = 0; i < Constantes.NB_COLONNES; i++)
			for (int j = 0; j < Constantes.NB_LIGNES; j++) {
				grille[i][j] = Case.V;
			}
	}

	/**
	 * Constructeur qui créé une copie de la grille donné en argument
	 * 
	 * @param original
	 */
	private Grille(Grille original) {
		this.grille = original.grille;
	}

	/**
	 * Renvoie le contenu de la case aux coordonnées données en argument
	 * 
	 * @param ligne
	 * @param colonne
	 * @return
	 */
	public Case getCase(int ligne, int colonne) {
		return grille[colonne][ligne];
	}

	/**
	 * Indique s'il y a encore de la place dans la colonne indiquée
	 * 
	 * @param colonne
	 * @return
	 */
	public boolean isCoupPossible(int colonne) {
		if (colonne >= 0 && colonne < Constantes.NB_COLONNES) {
			return grille[colonne][Constantes.NB_LIGNES - 1] == Case.V;
		} else {
			return false;
		}
	}

	/**
	 * Ajoute le symbole indiqué dans la colonne indiquée ce qui permet de jouer
	 * ce coup
	 * 
	 * @param colonne
	 * @param symbole
	 */
	public void ajouterCoup(int colonne, Case symbole) {
		for (int j = 0; j < Constantes.NB_LIGNES; j++) {
			if (grille[colonne][j] == Case.V) {
				grille[colonne][j] = symbole;
				break;
			}
		}

	}

	/**
	 * Renvoie l'état de la partie
	 * 
	 * @param symboleJoueurCourant
	 * @param tour
	 * @return
	 */
	public int getEtatPartie(Case symboleJoueurCourant, int tour) {
		int victoire;
		if (symboleJoueurCourant == Constantes.SYMBOLE_J1) {
			victoire = Constantes.VICTOIRE_JOUEUR_1;
		} else {
			victoire = Constantes.VICTOIRE_JOUEUR_2;
		}
		int nbAlignes = 0;
		// Vérification alignement horizontaux
		for (int i = 0; i < Constantes.NB_LIGNES; i++) {
			for (int j = 0; j < Constantes.NB_COLONNES; j++) {
				if (grille[j][i] == symboleJoueurCourant)
					nbAlignes++;
				else
					nbAlignes = 0;
				if (nbAlignes == 4) {
					return victoire;
				}
			}
			nbAlignes = 0;
		}
		// Vérification alignement verticaux
		for (int j = 0; j < Constantes.NB_COLONNES; j++) {
			for (int i = 0; i < Constantes.NB_LIGNES; i++) {
				if (grille[j][i] == symboleJoueurCourant)
					nbAlignes++;
				else
					nbAlignes = 0;
				if (nbAlignes == 4) {
					return victoire;
				}
			}
			nbAlignes = 0;
		}
		// Vérification alignement diagonaux (bas-droite vers haut-gauche)
		for (int i = 0; i < Constantes.NB_LIGNES - 3; i++)
			for (int j = 0; j < Constantes.NB_COLONNES - 3; j++) {
				for (int x = 0; i + x < Constantes.NB_LIGNES && j + x < Constantes.NB_COLONNES; x++) {
					if (grille[j + x][i + x] == symboleJoueurCourant)
						nbAlignes++;
					else
						nbAlignes = 0;
					if (nbAlignes == 4) {
						return victoire;
					}
				}
				nbAlignes = 0;
			}

		// Vérification alignement diagonaux (bas-gauche vers haut-droit)
		for (int i = 0; i < Constantes.NB_LIGNES - 3; i++)
			for (int j = Constantes.NB_COLONNES - 1; j >= 3; j--) {
				for (int x = 0; i + x < Constantes.NB_LIGNES && j - x >= 0; x++) {
					if (grille[j - x][i + x] == symboleJoueurCourant)
						nbAlignes++;
					else
						nbAlignes = 0;
					if (nbAlignes == 4) {
						return victoire;
					}
				}
				nbAlignes = 0;
			}

		if (tour == Constantes.NB_TOUR_MAX) {
			return Constantes.MATCH_NUL;
		}

		return Constantes.PARTIE_EN_COURS;
	}

	/**
	 * Donne un score à la grille en fonction du joueur
	 * 
	 * @param symboleJoueurCourant
	 * @return
	 */
	public double evaluer(Case symboleJoueurCourant) {
		double valeur_lignes = evalLignes(symboleJoueurCourant);
		if(valeur_lignes == Integer.MAX_VALUE)
			return valeur_lignes;
		double valeur_colonnes = evalColonnes(symboleJoueurCourant);
		if(valeur_colonnes == Integer.MAX_VALUE)
			return valeur_colonnes;
		double valeur_diagonales = evalDiagonales(symboleJoueurCourant);
		if(valeur_diagonales == Integer.MAX_VALUE)
			return valeur_diagonales;
		
		return valeur_lignes+valeur_colonnes+valeur_diagonales;
	}

	/**
	 * Clone la grille
	 */
	public Grille clone() {
		Grille copy = new Grille(this);
		return copy;
	}
	/**
	 * Evalue la valeur de toutes les lignes dans la grille actuelle pour un joueur spécifique
	 * @param symboleJoueurCourant
	 * @return
	 */
	public double evalLignes(Case symboleJoueurCourant) {
		double valeur_lignes = 0;
		for (int i = 0; i < Constantes.NB_LIGNES; i++) {
			ArrayList<Case> cellules = new ArrayList<Case>();
			for (int j = 0; j < Constantes.NB_COLONNES; j++) {
				Case cellule = grille[j][i];
				cellules.add(cellule);
			}
			double valeur_ligne = valeurAlignement(cellules, symboleJoueurCourant);
			if (valeur_ligne == Integer.MAX_VALUE) {
				valeur_lignes = Integer.MAX_VALUE;
				break;
			}
			valeur_lignes += valeur_ligne;
			valeur_ligne = 0;
		}
		return valeur_lignes;
	}
	/**
	 * Evalue la valeur de toutes les colonnes dans la grille actuelle pour un joueur spécifique
	 * @param symboleJoueurCourant
	 * @return
	 */
	public double evalColonnes(Case symboleJoueurCourant) {
		double valeur_colonnes = 0;
		for (int i = 0; i < Constantes.NB_COLONNES; i++) {
			ArrayList<Case> cellules = new ArrayList<Case>();
			for (int j = 0; j < Constantes.NB_LIGNES; j++) {
				Case cellule = grille[i][j];
				cellules.add(cellule);
			}
			double valeur_colonne = valeurAlignement(cellules, symboleJoueurCourant);
			if (valeur_colonne == Integer.MAX_VALUE) {
				valeur_colonnes = Integer.MAX_VALUE;
				break;
			}
			valeur_colonnes += valeur_colonne;
			valeur_colonne = 0;
		}
		return valeur_colonnes;
	}
	/**
	 * Evalue la valeur de toutes les diagonales dans la grille actuelle pour un joueur spécifique
	 * @param symboleJoueurCourant
	 * @return
	 */
	public double evalDiagonales(Case symboleJoueurCourant) {
		double valeur_Diagonales = 0;

		// Vérification alignement diagonaux (bas-droite vers haut-gauche)
		for (int i = Constantes.NB_LIGNES - 3; i >= 0; i--) {
			ArrayList<Case> cellules = new ArrayList<Case>();
			for (int x = 0; i + x < Constantes.NB_LIGNES; x++) {
				Case cellule = grille[x][i + x];
				cellules.add(cellule);
			}
			double valeur_Diagonale = valeurAlignement(cellules, symboleJoueurCourant);
			if (valeur_Diagonale == Integer.MAX_VALUE)
				return Integer.MAX_VALUE;
			valeur_Diagonales += valeur_Diagonale;
		}

		for (int j = 1; j < Constantes.NB_COLONNES - 3; j++) {
			ArrayList<Case> cellules = new ArrayList<Case>();
			for (int y = 0; y + j < Constantes.NB_COLONNES; y++) {
				Case cellule = grille[j + y][y];
				cellules.add(cellule);
			}
			double valeur_Diagonale = valeurAlignement(cellules, symboleJoueurCourant);
			if (valeur_Diagonale == Integer.MAX_VALUE)
				return Integer.MAX_VALUE;
			valeur_Diagonales += valeur_Diagonale;
		}

		// Vérification alignement diagonaux (bas-gauche vers haut-droit)
		for (int i = Constantes.NB_LIGNES - 3; i >= 0; i--) {
			ArrayList<Case> cellules = new ArrayList<Case>();
			for (int x = 0; i + x < Constantes.NB_LIGNES; x++) {
				Case cellule = grille[i][i + x];
				cellules.add(cellule);
			}
			double valeur_Diagonale = valeurAlignement(cellules, symboleJoueurCourant);
			if (valeur_Diagonale == Integer.MAX_VALUE)
				return Integer.MAX_VALUE;
			valeur_Diagonales += valeur_Diagonale;
		}

		for (int j = Constantes.NB_COLONNES - 1; j >= 3; j--) {
			ArrayList<Case> cellules = new ArrayList<Case>();
			for (int y = 0; y < j; y++) {
				Case cellule = grille[j - y][y];
				cellules.add(cellule);
			}
			double valeur_Diagonale = valeurAlignement(cellules, symboleJoueurCourant);
			if (valeur_Diagonale == Integer.MAX_VALUE)
				return Integer.MAX_VALUE;
			valeur_Diagonales += valeur_Diagonale;
		}
		return valeur_Diagonales;
	}
	/**
	 * Attribue une valeur à un enchaînement de Tokens (contenus dans une arraylist)
	 * @param cellules
	 * @param symboleJoueurCourant
	 * @return
	 */
	public double valeurAlignement(ArrayList<Case> cellules, Case symboleJoueurCourant) {
		double valeur_Alignement = 0;
		int cellules_disponibles = 0;
		int NBtokenAdj = 0;
		int NBtokenAlignes = 0;
		for (Case cellule : cellules) {
			if (cellule == Case.V) {
				cellules_disponibles++;
				NBtokenAdj = 0;
			} else if (cellule == symboleJoueurCourant) {
				if (cellules_disponibles > 3)
					valeur_Alignement += 3;
				else
					valeur_Alignement += cellules_disponibles;
				cellules_disponibles = 0;
				NBtokenAdj++;
				if (NBtokenAdj >= 4)
					return Integer.MAX_VALUE;
				NBtokenAlignes++;
			} else {
				if (cellules_disponibles + NBtokenAlignes >= 4)
					valeur_Alignement += cellules_disponibles + Math.pow(NBtokenAlignes, 2);
				NBtokenAlignes = 0;
				cellules_disponibles = 0;
				NBtokenAdj = 0;
			}
		}
		if (cellules_disponibles + NBtokenAlignes >= 4) {
			if (cellules_disponibles < 4)
				valeur_Alignement += cellules_disponibles + Math.pow(NBtokenAlignes, 2);
			else
				valeur_Alignement += 3 + Math.pow(NBtokenAlignes, 2);
		}

		return valeur_Alignement;
	}
	
	public boolean isFinished() {
		return IntStream.range(0, Constantes.NB_COLONNES).noneMatch(this::isCoupPossible);
	}

}
