package fr.uha.ensisa.puissance4.jeu;

import fr.uha.ensisa.puissance4.data.Joueur;
import fr.uha.ensisa.puissance4.data.Partie;
import fr.uha.ensisa.puissance4.ui.Console;

public class Jeu extends Thread{
	
	private Partie partie;
	private Console console;
	
	public Jeu(Joueur joueur1, Joueur joueur2, Console console)
	{
		this.partie=new Partie(joueur1, joueur2);
		this.console=console;
	}
	
	public void run()
	{
		console.lancementPartie(partie.getJoueur1(), partie.getJoueur2());
		while(!partie.isPartieFinie())
		{
			console.lancementTour(partie.getTour(), partie.getJoueurCourant(), partie.getGrille());
			
			long tempsReflexion=System.currentTimeMillis();
			int coup= partie.getJoueurCourant().joue(partie.getGrille(), console, partie.getTour());
			tempsReflexion=System.currentTimeMillis()-tempsReflexion;
			console.afficherCoup(partie.getJoueurCourant(), coup, tempsReflexion);
			if(!partie.jouerCoup(coup, tempsReflexion))
			{
				System.out.println("COUP INVALIDE : Recommencez !");
			}
		}
		console.closeScanner();
		console.afficherFinPartie(partie);
	}

	

}
