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
		double[] alphabeta = maxValue(grilleDepart, Integer.MAX_VALUE, Integer.MIN_VALUE, depth);

		return (int) alphabeta[0];
	}

	private double[] maxValue(Grille grille, double alpha, double beta, int depth) {
		double alpha1 = alpha;
		double beta1 = beta;
		double[] max = { Constantes.COUP_NON_DEFINI, Integer.MIN_VALUE };
		if (grille.isFinished() || depth <= 0
				|| grille.getEtatPartie(symboleMax, depth) == Constantes.VICTOIRE_JOUEUR_1) {
			max[1] = grille.evaluer(symboleMax) - grille.evaluer(symboleMin);
			return max;
		}
		for (int column = 0; column < Constantes.NB_COLONNES; column++) {
			if (!grille.isCoupPossible(column))
				continue;
			Grille newGrille = grille.clone();
			newGrille.ajouterCoup(column, symboleMin);
			max[1] = Double.max(max[1], minValue(newGrille, alpha1, beta1, depth - 1)[1]);
			if(max[1]>=beta){
				max[0]=column;
				return max;
			}
			alpha1 = Double.max(alpha1, max[1]);
		}
		return max;
	}

	private double[] minValue(Grille grille, double alpha, double beta, int depth) {
		double alpha1 = alpha;
		double beta1 = beta;
		double[] min = { Constantes.COUP_NON_DEFINI, Integer.MAX_VALUE };
		if (grille.isFinished() || depth <= 0
				|| grille.getEtatPartie(symboleMin, depth) == Constantes.VICTOIRE_JOUEUR_1) {
			min[1] = grille.evaluer(symboleMax) - grille.evaluer(symboleMin);
			return min;
		}
		for (int column = 0; column < Constantes.NB_COLONNES; column++) {
			if (!grille.isCoupPossible(column))
				continue;
			Grille newGrille = grille.clone();
			newGrille.ajouterCoup(column, symboleMin);
			min[1] = Double.min(min[1], maxValue(newGrille, alpha1, beta1, depth - 1)[1]);
			if(min[1]<=alpha){
				min[0]=column;
				return min;
			}
			beta1 = Double.min(beta1, min[1]);
		}
		return min;
	}
}
