package fr.uha.ensisa.puissance4.jeu.algosIA;

import fr.uha.ensisa.puissance4.data.Grille;
import fr.uha.ensisa.puissance4.data.Joueur;
import fr.uha.ensisa.puissance4.util.Constantes;

public class AlphaBeta extends Algorithm {

	public AlphaBeta(int levelIA, Grille grilleDepart, Joueur joueurActuel, int tour) {
		super(levelIA, grilleDepart, joueurActuel, tour);

	}

	@Override
	public int choisirCoup() {
		int depth = levelIA;
		double bestValue = Integer.MIN_VALUE;
		int bestColumn = Constantes.COUP_NON_DEFINI;
		for (int column = 0; column < Constantes.NB_COLONNES; column++) {
			if (!grilleDepart.isCoupPossible(column))
				continue;
			Grille newGrille = grilleDepart.clone();
			newGrille.ajouterCoup(column, symboleMax);
			double alphabeta = minValue(newGrille, Integer.MIN_VALUE, Integer.MAX_VALUE, depth-1);
			System.out.println("Colonne : "+column + "/Value :"+ alphabeta);
			if(alphabeta>bestValue){
				bestValue = alphabeta;
				bestColumn = column;
			}
		}
		return bestColumn;
	}
	/**
	 * Renvois la valeur de grille la plus forte parmis celles explorées
	 * @param grille
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return
	 */
	private double maxValue(Grille grille, double alpha, double beta, int depth) {
		double max = Integer.MIN_VALUE;
		int etatPartie = grille.getEtatPartie(symboleMin, depth);
		if (grille.isFinished() || depth <= 0
				|| etatPartie == Constantes.VICTOIRE_JOUEUR_1
				|| etatPartie == Constantes.VICTOIRE_JOUEUR_2){
			max = grille.evaluer(symboleMax) - grille.evaluer(symboleMin);
			return max;
		}
		for (int column = 0; column < Constantes.NB_COLONNES; column++) {
			if (!grille.isCoupPossible(column))
				continue;
			Grille newGrille = grille.clone();
			newGrille.ajouterCoup(column, symboleMax);
			max = Double.max(max, minValue(newGrille, alpha, beta, depth - 1));
			if(max>=beta){
				return max;
			}
			alpha = Double.max(alpha, max);
		}
		return max;
	}
	/**
	 * Renvois la valeur de grille la plus faible parmis celles explorées
	 * @param grille
	 * @param alpha
	 * @param beta
	 * @param depth
	 * @return
	 */
	private double minValue(Grille grille, double alpha, double beta, int depth) {
		
		double min = Integer.MAX_VALUE ;
		int etatPartie = grille.getEtatPartie(symboleMin, depth);
		if (grille.isFinished() || depth <= 0
				|| etatPartie == Constantes.VICTOIRE_JOUEUR_1
				|| etatPartie == Constantes.VICTOIRE_JOUEUR_2){
			min = grille.evaluer(symboleMax) - grille.evaluer(symboleMin);
			return min;
		}
		for (int column = 0; column < Constantes.NB_COLONNES; column++) {
			if (!grille.isCoupPossible(column))
				continue;
			Grille newGrille = grille.clone();
			newGrille.ajouterCoup(column, symboleMin);
			min = Double.min(min, maxValue(newGrille, alpha, beta, depth - 1));
			if(min<=alpha){
				return min;
			}
			beta = Double.min(beta, min);
		}
		return min;
	}
}
