package fr.uha.ensisa.puissance4.ui;

import java.util.Scanner;

import fr.uha.ensisa.puissance4.data.Grille;
import fr.uha.ensisa.puissance4.data.Humain;
import fr.uha.ensisa.puissance4.data.IA;
import fr.uha.ensisa.puissance4.data.Joueur;
import fr.uha.ensisa.puissance4.data.Partie;
import fr.uha.ensisa.puissance4.jeu.Jeu;
import fr.uha.ensisa.puissance4.util.Constantes;
import fr.uha.ensisa.puissance4.util.Constantes.Case;

public class Console extends Thread {
	
	private Scanner entry;

	public Console()
	{
		super("console");
		entry = new Scanner(System.in);
	}
	
	public void run()
	{
		Joueur j1,j2;
		//Scanner entry = new Scanner(System.in);
		System.out.println("************* PUISSANCE 4 *************");
		j1 = choixJoueur(Constantes.JOUEUR_1, entry);
		j2 = choixJoueur(Constantes.JOUEUR_2, entry);		
		Jeu jeu = new Jeu(j1, j2, this);
		jeu.start();
	}
	
	public void closeScanner()
	{
		entry.close();
	}
	
	private Joueur choixJoueur(int order, Scanner entry)
	{
		Joueur joueur=null;
		int typeJoueur;
		String nomJoueur;
		int typeIA=-1;
		int levelIA=-1;
		do{
			System.out.println("Le joueur "+order+" est :");
			System.out.println("1) Humain");
			System.out.println("2) Intelligence Artificielle");
			System.out.print("Votre choix : ");
			typeJoueur = entry.nextInt();
		}while(typeJoueur!=1&&typeJoueur!=2);
		if(typeJoueur==Constantes.JOUEUR_HUMAN)
		{
			System.out.print("Entrez le nom du joueur "+order+" : ");
			entry.nextLine();
			nomJoueur= entry.nextLine();
			joueur=new Humain(nomJoueur,order);
		}
		else
		{
			nomJoueur=Constantes.IA_NAMES[(int)Math.floor(Math.random()*Constantes.IA_NAMES.length)];
			
			do{
				System.out.println("Quel IA pour joueur "+order+" ("+nomJoueur+") ?");
				for(int j=0; j<Constantes.IA_ALGOS.length; j++)
				{
					System.out.println((j+1)+") "+Constantes.IA_ALGOS[j]);
					
				}
				System.out.print("Votre choix : ");
				typeIA=entry.nextInt()-1;
			}while(typeIA<0||typeIA>=Constantes.IA_ALGOS.length);
			do{
				System.out.println("Niveau de difficulté de l'IA ("+nomJoueur+") [1-42] ?");
				System.out.print("Votre choix : ");
				levelIA=entry.nextInt();
			}while(levelIA<0||levelIA>Constantes.NB_TOUR_MAX);
			joueur= new IA(nomJoueur, order, typeIA, levelIA);
		}
		
		return joueur;
	}
	
	public void lancementPartie(Joueur joueur1, Joueur joueur2)
	{
		System.out.println("************* Début de partie ************");
		System.out.println("Joueur 1 : "+joueur1.getNom()+" ("+joueur1.getTypeNom()+")");
		System.out.println("Joueur 2 : "+joueur2.getNom()+" ("+joueur2.getTypeNom()+")");		
	}
	
	public void lancementTour(int tour, Joueur joueurCourant, Grille grille)
	{
		System.out.println("************* Tour "+tour+" ************");
		System.out.println("C'est à "+joueurCourant.getNom()+" de jouer !");
		afficheGrille(grille);
	}

	private void afficheGrille(Grille grille) {
		String s="";
		for(int i=Constantes.NB_LIGNES-1;i>=0;i--)
		{
			s+="|";
			for(int j=0;j<Constantes.NB_COLONNES;j++)
			{
				String symbol;
				if(grille.getCase(i, j)==Case.V)
					symbol=" ";
				else
					symbol = grille.getCase(i, j).toString();
				
				s+=symbol+"|";
			}
			s+="\n";
		}
		s+="=";
		for(int j=0;j<Constantes.NB_COLONNES;j++)
		{
			s+="==";
		}
		s+="\n";
		for(int j=0;j<Constantes.NB_COLONNES;j++)
		{
			s+=" "+(j+1);
		}
		System.out.println(s);
		
		
	}

	public int getHumanCoup(String nom) {
		System.out.print("Coup de "+nom+" : ");
		return entry.nextInt();
	}

	public void reflexionIA(String nom)
	{
		System.out.println(nom+" réfléchit ...");
	}

	public void afficherCoup(Joueur joueurCourant, int coup, long t) {
		System.out.println(joueurCourant.getNom() +" a choisi de mettre un jeton dans la colonne "+(coup+1)+" après "+timeToString(t)+" de réflexion\n");
		
	}

	public void afficherFinPartie(Partie partie) {
		String msg;
		switch(partie.getEtatPartie())
		{
			case Constantes.VICTOIRE_JOUEUR_1 : 
				msg="VICTOIRE "+partie.getJoueur1().getNom();
				break;
			case Constantes.VICTOIRE_JOUEUR_2 : 
				msg="VICTOIRE "+partie.getJoueur2().getNom();
				break;
			default : 
				msg="MATCH NUL";
				break;
		}
		System.out.println("************ "+msg+" en "+(partie.getTour()-1)+" tours ***************");
		afficheGrille(partie.getGrille());
		System.out.println(partie.getJoueur1().getNom()+" : "+timeToString(partie.getTempsReflexionJ1())+"s");
		System.out.println(partie.getJoueur2().getNom()+" : "+timeToString(partie.getTempsReflexionJ2())+"ms");
		System.out.println("******************************************************************");
		
	}
	
	private String timeToString(long t)
	{
		String s="";
		if(t>3600000)
		{
			long h=t/3600000;
			s+=h+"h ";
			t-=h*3600000;
		}
		if(t>60000)
		{
			long m=t/60000;
			s+=m+"m ";
			t-=m*60000;
		}
		if(t>1000)
		{
			long sec=t/1000;
			s+=sec+"s ";
			t-=sec*1000;
		}
		if(t>0)
		{
			s+=t+"ms";
		}
		return s;
	}
	
}
