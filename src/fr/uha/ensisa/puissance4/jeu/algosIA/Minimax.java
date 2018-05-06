package fr.uha.ensisa.puissance4.jeu.algosIA;

import fr.uha.ensisa.puissance4.data.Grille;
import fr.uha.ensisa.puissance4.data.Joueur;
import fr.uha.ensisa.puissance4.util.Constantes;

public class Minimax extends Algorithm {

	public Minimax(int levelIA, Grille grilleDepart, Joueur joueurActuel, int tour) {
		super(levelIA, grilleDepart, joueurActuel, tour);
	}

	@Override
	public int choisirCoup() {
	    //levelIA = profondeur de l'arbre
		//Objectif : 5-6 en 2-3 secondes
		int depth = levelIA;
		double[] minimax = maxValue(grilleDepart, depth);

	    return (int) minimax[0];
	}

	/**
	 * fonction max de minimax : 
	 * Retourne le doublet {colonne, valeur max} correspondant à la grille ayant le meilleur score pour l'ia
	 * @param grille
	 * @param depth
	 * @return
	 */
	private double[] maxValue(Grille grille, int depth) {
	    double[] max = {Constantes.COUP_NON_DEFINI, Integer.MIN_VALUE};
	    int etatPartie = grille.getEtatPartie(symboleMin, depth);
		if (grille.isFinished() || depth <= 0
				|| etatPartie == Constantes.VICTOIRE_JOUEUR_1
				|| etatPartie == Constantes.VICTOIRE_JOUEUR_2){ 	
			max[1] = grille.evaluer(symboleMax)-grille.evaluer(symboleMin);
			return max;
		}
		for (int column = 0; column < Constantes.NB_COLONNES; column++) {
			if (!grille.isCoupPossible(column)) continue;
			Grille newGrille = grille.clone();
			newGrille.ajouterCoup(column, symboleMax);
			
			double[] nextMove = this.minValue(newGrille, depth - 1);
			if (nextMove[1] >= max[1]) {
				max[1] = nextMove[1];
				max[0] = column;
			}
		}
		return max;
	}
	
	
	/**
	 * fonction min de minimax : 
	 * Retourne le doublet {colonne, valeur min} correspondant à la grille ayant le pire score pour l'ia
	 * @param grille
	 * @param depth
	 * @return
	 */
	
	private double[] minValue(Grille grille, int depth) {
		double[] min = {Constantes.COUP_NON_DEFINI, Integer.MAX_VALUE};
		int etatPartie = grille.getEtatPartie(symboleMin, depth);
		if (grille.isFinished() || depth <= 0
				|| etatPartie == Constantes.VICTOIRE_JOUEUR_1
				|| etatPartie == Constantes.VICTOIRE_JOUEUR_2){
			min[1] = grille.evaluer(symboleMax)-grille.evaluer(symboleMin);
			return min;
		}
		for (int column = 0; column < Constantes.NB_COLONNES; column++) {
			if (!grille.isCoupPossible(column)) continue;	
			Grille newGrille = grille.clone();
			newGrille.ajouterCoup(column, symboleMin);
			
			double[] nextMove = this.maxValue(newGrille, depth - 1);
			if (nextMove[1] <= min[1]) {
				min[1] = nextMove[1];
				min[0] = column;
			}
		}
		return min;
	}

}
