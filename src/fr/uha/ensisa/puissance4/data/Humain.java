package fr.uha.ensisa.puissance4.data;

import fr.uha.ensisa.puissance4.ui.Console;
import fr.uha.ensisa.puissance4.util.Constantes;

public class Humain extends Joueur {

	public Humain(String nom, int order) {
		super(nom, order);		
	}

	@Override
	public int getType() {
		return Constantes.JOUEUR_HUMAN;
	}

	@Override
	public String getTypeNom() {
		return "Humain";
	}



	@Override
	public int joue(Grille grille, Console console, int tour) {
		return (console.getHumanCoup(this.getNom())-1);
	}

}
