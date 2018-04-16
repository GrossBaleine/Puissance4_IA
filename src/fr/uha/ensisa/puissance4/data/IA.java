package fr.uha.ensisa.puissance4.data;

import fr.uha.ensisa.puissance4.jeu.algosIA.Algorithm;
import fr.uha.ensisa.puissance4.jeu.algosIA.AlphaBeta;
import fr.uha.ensisa.puissance4.jeu.algosIA.Minimax;
import fr.uha.ensisa.puissance4.ui.Console;
import fr.uha.ensisa.puissance4.util.Constantes;

public class IA extends Joueur {
	
	private int algoIA;
	private int levelIA;
	
	
	public IA(String nom, int order, int algoIA, int levelIA) {
		super(nom, order);
		this.algoIA=algoIA;
		this.levelIA=levelIA;
	}

	@Override
	public int getType() {
		return Constantes.JOUEUR_IA;
	}
	
	public int getAlgoIA()
	{
		return algoIA;
	}

	@Override
	public String getTypeNom() {
		return "IA";
	}

	@Override
	public int joue(Grille grille, Console console, int tour) {
		console.reflexionIA(this.getNom());
		Algorithm iA;
		if(algoIA==Constantes.IA_MINIMAX)
		{
			iA = new Minimax(levelIA,grille, this, tour);
		}
		else
		{
			iA = new AlphaBeta(levelIA,grille,this, tour);
		}
		return iA.choisirCoup();
	}

	


}
