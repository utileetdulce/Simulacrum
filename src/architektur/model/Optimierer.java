package architektur.model;

import java.util.ArrayList;

import architektur.model.gebaude.Flur;
import architektur.model.gebaude.Gebaeude;
import architektur.model.gebaude.Raum;
import architektur.model.gebaude.Toilette;

import com.jme.math.Vector3f;

/**
 * 
 *Der Opimierer hat zum Ziel die Anordnung der Raume im Gebäude zu optimieren und damit die 
 *Gesamtfitness eines Gebaudes verbessert, indem Raume aus ihrer bisherigen Position herausgetrennt 
 *und an anderer Stelle wieder eingefuegt werden. Jeder Raum besitzt einen Fitnesswert, der sich aus 
 *seiner vertikalen und horizontalen Anordnung im Gebaude ergibt. Nach jeder Poitionsaenderung eines Raumes
 *muss daher eine Aktualisierung aller beeinflusster Raume noetig.
 *Ist die gesamtfitness nach der Aenderung besser, so wird die 
 *Aenderung beibehalten. Ist sie schlechter, wird die Aenderung rückfgaengig gemacht. 
 *Nach einer bestimmten Zahl von Misserfolgen wird die Optimierung beendet und die Raumlaengen auf 
 *die Langen der Flure skaliert.
 *  
 * @author Philipp Niederlag 
 * 		   Stud.Nr.: 11057798
 * 		   Gruppennummer 4
 * 		   SS 09 
 * 		   Email: wp-poduction@web.de
 * 
 * @version 1.8
 */
public class Optimierer {

	/**
	 * iterationsZaehler gibt den aktuellen Iterationsschritt an
	 */
	private int iterationsZaehler=0;
	
	/**
	 * raumeImGebaude gibt die im Gebaude angeordneten Raume an
	 */
	private ArrayList<Raum> raumeImGebaude;
	
	/**
	 * raumeNichtAngeordnet gibt die nicht im Gebaude angeordneten Raume an
	 */
	private ArrayList<Raum> raumeNichtAngeordnet = new ArrayList<Raum>();
	
	/**
	 * flureImGebaude gibt die im Gebaude angeordneten Flur an
	 */
	private ArrayList<Flur> flureImGebaude;

	/**
	 * aktuelleGesamtFitness gibt die aktuelle Gesamtfitness des Gebaudes an
	 */
	private float aktuelleGesamtFitness;
	
	/**
	 * Optimierungsschritt gibt den aktuellen Optimierungsschritt an
	 */
	private int Optimierungsschritt=0;

	/**
	 * neueGesamtFitness gibt die neue Gesamtfitness des Gebaudes an
	 */
	private float neueGesamtFitness;
	
	/**
	 * schlechterRaum gibt den Raum mit der schlechtesten Ffitness des Gebaudes an
	 */
	private Raum schlechterRaum;
	
	/**
	 * anzahlDerAuszutauschendenRaume gibt an wieviele Raume auf einmal ausgetauscht werden sollen
	 */
	private int anzahlDerAuszutauschendenRaume = 1;
		
	/**
	 * flurBackup Sicherung des Flurs des entfernten Raumes
	 */
	private int[] flurBackup = new int[anzahlDerAuszutauschendenRaume];
	
	/**
	 * flurSeiteBackup Sicherung der Flurseite des entfernten Raumes
	 */
	private int[] flurSeiteBackup = new int[anzahlDerAuszutauschendenRaume];
	
	/**
	 * flurSeitePosBackup Sicherung der Position (an einer Flurseite) des entfernten Raumes
	 */
	private int[] flurSeitePosBackup = new int[anzahlDerAuszutauschendenRaume];
	
	/**
	 * misserfolgFlur gibt die Anzahl an missglueckten Anordnungsversuchen diesen Flur betreffen an
	 */
	private int misserfolgFlur=0;
	
	/**
	 * misserfolgRaum gibt die Anzahl an missglueckten Anordnungsversuchen diesen Raum betreffen an
	 */
	private int misserfolgRaum=0;
	
	/**
	 * misserfolgKritisch gibt die Anzahl an wiederholt missglueckten Anordnungsversuchen an
	 */
	private int misserfolgKritisch=0;
	
	/**
	 * fertig gibt an, ob die Optimierung die angegebene Qualitaet erreicht hat
	 */	
	private boolean fertig=true;
	
	/**
	 * optimieren = verbessert die Gesamtfitness eines Gebaudes, indem Raume aus ihrer bisherigen Position herausgetrennt und an anderer Stelle wieder eingefuegt werden.
	 * Ist die gesamtfitness danach besser, so wird die Aenderung beibehalten. Ist sie schlechter, wird die Aenderung rückfgaengig gemacht.
	 * 
	 * @param iterationen
	 * 					 	- gibt die aktuelle Anzahl an Iterationen zurueck
	 * @param fh
	 * 						- Gebaude, dessen Raume optimiert angeordnt werden sollen
	 */
	public void optimieren(int iterationen, Gebaeude fh) {
		
		for (int i = 0; i < iterationen; i++) {
			//Abbruch, wenn misserfolgKritisch > 100
			if(fertig==false){
			
				// Bestandsaufnahme
				flureImGebaude = this.cocktailAngeordneterFlure(fh);
				raumeImGebaude = this.cocktailAngeordneterRaume(fh);
				
				// Alle flure und raume aktulaisieren
				for(int g=0; g<flureImGebaude.size(); g++){
					updateFlurseite(flureImGebaude.get(g),0);	updateFlurseite(flureImGebaude.get(g),1);
				}
				
				System.out.print("RaumeImGebaude: " + raumeImGebaude.size()  + " |  ");
				//Fehlersuche
//				fehlerSuche(fh);
				
				// Wegnehmen
				webnehmen(fh);				
				
				System.out.print("RaumeNichtAngeordnet: " +raumeNichtAngeordnet.size()+ " |  ");
				// Hinzufuegen
				raumHinzufuegen();
				
				//Vergleichen
				vergleichen(fh);
				
					
				
			}
			if(misserfolgKritisch > 10000 && fertig==false){
				skaliereRaume();
				fertig=true;
			}
	
		}
		iterationsZaehler++;

	}

	/**
	 * Die Methode gibt eine ArrayList von Raeumen zurueck, die im Gebaeude
	 * angeordnet sind
	 * 
	 * @param fh
	 * 				Gebaude, das die Raume haelt
	 * @return ArrayList<Raum>
	 */
	private ArrayList<Raum> cocktailAngeordneterRaume(Gebaeude fh) {
		ArrayList<Raum> tempList = new ArrayList<Raum>();
		// Stockwerke
		for (int st = 0; st < fh.stockwerkeImGebaude.size(); st++) {
			// flure
			for (int f = 0; f < fh.stockwerkeImGebaude.get(st).flureImStockwerk
					.size(); f++) { 
				// flurseiten
				for (int f_lr = 0; f_lr < fh.stockwerkeImGebaude.get(st).flureImStockwerk
						.get(f).flurseite.length; f_lr++){
					// raeume
					for (int r = 0; r < fh.stockwerkeImGebaude.get(st).flureImStockwerk
							.get(f).flurseite[f_lr].size(); r++) {

						tempList
								.add((Raum) fh.stockwerkeImGebaude.get(st).flureImStockwerk
										.get(f).flurseite[f_lr].get(r)); 

						Raum tempRaum;
						tempRaum = (Raum) fh.stockwerkeImGebaude.get(st).flureImStockwerk.get(f).flurseite[f_lr].get(r);
						
						// angeordnet setzen
						tempRaum.setAngeordnet(true);
												
						// Fehlermeldung, falls raumID nicht mit index in arrayList uebereinstimmt
						if (tempRaum.getRaumID() != r) {
							System.out.println(+tempRaum.getRaumID() + " " + r
									+ " | st, f, flr :" + st + ", " + f + ", "
									+ f_lr);
						}
					}

				}
			}
		}
		return tempList;
	}

	/**
	 * Die Methode gibt eine ArrayList von Fluren zurueck, die im Gebaeude
	 * angeordnet sind
	 * 
	 * @param fh
	 * 			Gebaude, das alle Flure haelt
	 * @return ArrayList<Flur>
	 */
	private ArrayList<Flur> cocktailAngeordneterFlure(Gebaeude fh) {
		ArrayList<Flur> tempList = new ArrayList<Flur>();
		// Stockwerke
		for (int st = 0; st < fh.stockwerkeImGebaude.size(); st++) {
			// Flure
			for (int f = 0; f < fh.stockwerkeImGebaude.get(st).flureImStockwerk
					.size(); f++) {
				tempList
						.add((Flur) fh.stockwerkeImGebaude.get(st).flureImStockwerk
								.get(f));
				
			}// Flure END
		}
		return tempList;
	}

	/**
		 * Die Methode berechnet die aktuelle Gesamtfitness des Gebaeudes
		 * 
		 * @param raume
		 *            Liste mit allen Raumen im Gebaude
		 * @return float
		 * 				gibt die Gesamtfitness zurück
		 */
		private float gesamtFitnessBerechnen(ArrayList<Raum> raume) {
	
			//Fehlerabfrage
			if(raume.size()==0) System.out.println("Keine Raume zur Fitnessberechnung da");
			float gesamtFitness = 0;
			// Cocktail angeordneter Raume durchlaufen
			for (int i = 0; i < raume.size(); i++) {
				raume.get(i).fitness_berechnen();
	//			System.out.println(raume.get(i).fitness_gesamt);
				gesamtFitness += raume.get(i).fitness_gesamt;
			}
			return gesamtFitness;
		}

	/**
	 * Die Methode gibt den Raum des Gebaeudes mit der schlechtesten Fitness aus
	 * 
	 * @param raume
	 *            Liste mit allen Raumen im Gebaude
	 * @return Raum
	 * 				schlechtester Raum im Gebaude
	 */
	private  Raum schlechtesterRaum(ArrayList<Raum> raume) {
		Raum schlechtesterRaum = new Raum();
		schlechtesterRaum = raume.get(0);

		for (int i = 0; i < raume.size(); i++) {
			raume.get(i).fitness_berechnen();
			if (schlechtesterRaum.getFitness_gesamt() > raume.get(i).fitness_gesamt) {
				schlechtesterRaum = raume.get(i);
			}
			//Variante, die den Raumcocktail nach fitness sortiert
			schlechtesterRaum=raume.get(i);
			if(schlechtesterRaum.getFitness_gesamt() < raume.get(0).getFitness_gesamt()){
				raume.remove(schlechtesterRaum);
				raume.add(0, schlechtesterRaum);
			}
		}
		return schlechtesterRaum;
	}

	/**
	 * Die Methode gibt den Flur des Gebaeudes mit dem groessten freiraum aus
	 * 
	 * @param flure
	 *            Liste mit allen Fluren im Gebaude
	 * @return int[]
	 * 			int Array. Der erste index gibt den Flur an. Der zweite die Flurseite
	 */
	public int[] besterFlur(ArrayList<Flur> flure) {
		int[] flurUndSeite = new int[2];
		float restLaengeReferenz = 0;

		for (int i = 0; i < flure.size(); i++) {

			if (flure.get(i).getRestLangeFlurLinks() > restLaengeReferenz) {
				flurUndSeite[0] = i;
				flurUndSeite[1] = 0;
				restLaengeReferenz = flure.get(i).getRestLangeFlurLinks();
			}
			if (flure.get(i).getRestLangeFlurRechts() > restLaengeReferenz) {
				flurUndSeite[0] = i;
				flurUndSeite[1] = 1;
				restLaengeReferenz = flure.get(i).getRestLangeFlurRechts();
			}
		}
		return flurUndSeite;
	}
	
	/**
	 * webnehmen  entfernt raum aus dem gebaude
	 * 
	 * @param fh
	 *            Das FH Gebaude
	 */
	private void webnehmen(Gebaeude fh){
	// Wegnehmen
	aktuelleGesamtFitness = gesamtFitnessBerechnen(raumeImGebaude);
	System.out.print("aktuelle Fitness: " + aktuelleGesamtFitness + " |  ");

	// Wieviele Raume sollen in einem Schritt herausgetrennt werden
	for (int v = 0; v < anzahlDerAuszutauschendenRaume; v++) {
		// schlechtesten raum bestimmen
		raumeImGebaude = this.cocktailAngeordneterRaume(fh);
		schlechterRaum = this.schlechtesterRaum(raumeImGebaude);
		//nach sortierung sollte der schlechteste raum an erster stelle stehen
		if(misserfolgRaum>50 || misserfolgRaum>raumeImGebaude.size()-1){
			misserfolgRaum=0;
		}
		schlechterRaum = raumeImGebaude.get(misserfolgRaum);
		raumeNichtAngeordnet.add(schlechterRaum);
		// fuer den Backup-Fall speichern
		flurBackup[v] = flureImGebaude.indexOf(schlechterRaum.getFlur());
		flurSeiteBackup[v] = schlechterRaum.getFlurSeite();
		flurSeitePosBackup[v] = schlechterRaum.getRaumID();
		//das eigentliche heraustrennen
		System.out.println("Schlechtester Raum Nr."+misserfolgRaum+
				", "+schlechterRaum.getClass().getName() +" etage:"+schlechterRaum.getFlur().etage.etage 
				+" mit Fitness: "+schlechterRaum.getFitness_gesamt());
		trenneRaumHeraus(schlechterRaum);
	}
	}
	
	/**
	 * trenneRaumHeraus Die Methode trennt den herauszutrennenden Raum heraus
	 * 
	 * @param raum
	 *            der herauszutrennende Raum
	 */
	private void trenneRaumHeraus(Raum raum) {
		Flur tempFlur = raum.getFlur();
		int flurSeite = raum.getFlurSeite();
		int raumID = raum.getRaumID();
		
		// Raum aus Flurseiten ArrayList entfernen		
		tempFlur.flurseite[flurSeite].remove(raumID);		

		updateFlurseite(tempFlur, flurSeite);
		
		// aus Node trennen
		if (raum.getFlurSeite() == 0)
			tempFlur.NodeRaumeLinks.detachChild(raum);
		if (raum.getFlurSeite() == 1)
			tempFlur.NodeRaumeRechts.detachChild(raum);

		raum.setFitness_gesamt(0);
		raum.setAngeordnet(false);
		raum.setNachbar_links(null);
		raum.setNachbar_rechts(null);
		raum.setFlur(null);
		raum.setFlurSeite(-1);		
	}
	
	/**
	 * raumHinzufuegen -Die Methode geht alle nicht angeordneten Raume duch und ordnet sie an
	 */
	private void raumHinzufuegen() {
		// Hinzufuegen
		if(misserfolgFlur > 20)misserfolgFlur=0;
		int position = 0;		
		for (int v = 0; v < raumeNichtAngeordnet.size(); v++) {		
			int[] besterFlur = this.besterFlur(flureImGebaude);	
			// Wenn der beste Flur der ist, aus dem der raum kommt oder schon eine bestimmte Zahl an Misserfolge mit dem besten Raum waren, dann...
			if(( besterFlur[0]==flurBackup[v] && besterFlur[1]==flurSeiteBackup[v] ) || misserfolgFlur > 15){
				besterFlur[0] = (int) (flureImGebaude.size()*Math.random());
				besterFlur[1] = (int)(Math.random()*2);
			}
			if(raumeNichtAngeordnet.get(v).fitness_faktor_horiz<10) position=15;
			System.out.print("bester (&voriger) flur :  " +besterFlur[0]+"("+flurBackup[v]+")"+"/"+ besterFlur[1]+"("+flurSeiteBackup[v]+")" +" RestL:"+ flureImGebaude.get(besterFlur[0]).restLangeFlurLinks+" RestR:"+ flureImGebaude.get(besterFlur[0]).restLangeFlurRechts+" |  ");
			raumImGebaudePlazieren(raumeNichtAngeordnet, flureImGebaude, besterFlur[0], besterFlur[1], position, v);
		}
	}
	
	/**
	 * raumImGebaudePlazieren  -Die Methode geht alle nicht angeordneten Raume duch und ordnet sie an
	 * @param raum
	 *            der herauszutrennende Raum 
	 */
	@SuppressWarnings("unchecked")
	private void raumImGebaudePlazieren(ArrayList<Raum> raume,
			ArrayList<Flur> flure, int flur, int flurSeite, int position, int i) {

			// besterFlur = flur mit laengster Restlaenge
			int i_f = flur;
			int i_fs = flurSeite;
			
			if(raume.get(i) instanceof Toilette) position=0;
			else position=1;// (int)(Math.random()*5) + 1;
			//verringert die positionDesEinfuegens ,falls der Flur gar nicht soviele Raume besitzt
			while (position > flure.get(i_f).flurseite[i_fs].size()) {
				position = position - 1;
			}				
			
			// Raum an x'ste Stelle des Flurs einsetzen			
			flure.get(i_f).flurseite[i_fs].add(position, raume.get(i));
			
			//Alles aktualisieren
			updateFlurseite(flure.get(i_f),i_fs);
	}
	
	/**
	 * vergleichen  -Die Methode geht alle nicht angeordneten Raume duch und ordnet sie an
	 * @param fh
	 *            das FH Gebaude 
	 */
	private void vergleichen(Gebaeude fh){
	// Vergleichen
	raumeImGebaude = this.cocktailAngeordneterRaume(fh);
	neueGesamtFitness = gesamtFitnessBerechnen(raumeImGebaude);
	System.out.println("\nFitnessdifferenz: "	+ (-aktuelleGesamtFitness + neueGesamtFitness));

	// wenn fitness schlechter(hoeherer Wert): rueckgaengig machen
	if (neueGesamtFitness <= aktuelleGesamtFitness) {
		System.out.println("KEINE VERBESSERUNG DER FITNESS");
		// raume wieder heraustrennen
		for (int v = 0; v < raumeNichtAngeordnet.size(); v++) {
			trenneRaumHeraus(raumeNichtAngeordnet.get(v));
			// und fuege sie wieder an die alte position ein
			raumImGebaudePlazieren(raumeNichtAngeordnet, flureImGebaude, flurBackup[v], flurSeiteBackup[v], flurSeitePosBackup[v], v);
		}
		misserfolgFlur++;
		misserfolgRaum++;
		if(misserfolgRaum>4)misserfolgKritisch++;
	} 
	// wenn fitness gleich: beibehalten
	if(neueGesamtFitness == aktuelleGesamtFitness){
		System.out.println("(gleichstand)");
		raumeNichtAngeordnet.clear();
	}
	// wenn fitness besser: beibehalten
	if(neueGesamtFitness > aktuelleGesamtFitness){
		System.out.println("FITNESS VERBESSERT");
		raumeNichtAngeordnet.clear();
		misserfolgFlur=0;
		misserfolgRaum=0;
		Optimierungsschritt++;
	}
	
	raumeNichtAngeordnet.clear();

	updateRaumPosition(raumeImGebaude);
}

	/**
	 * skaliereRaume Die Methode skaliert die Raume auf die Laenge der Flure
	 */
	public void skaliereRaume(){
	
		for(int flur=0; flur<flureImGebaude.size();flur++){
			for(int seite=0;seite<2;seite++)
				for (int r=0; r<flureImGebaude.get(flur).flurseite[seite].size(); r++){
		
					if(seite==0){
						float faktorL = flureImGebaude.get(flur).getLaenge()/flureImGebaude.get(flur).getLangeDerAngeordnetenRaumeLinks();
						Raum tempRaum=(Raum)flureImGebaude.get(flur).flurseite[0].get(r);
						
						//Wenn die restflurlaenge zu gross ist nicht skalieren
						if(!(faktorL>2)){
							tempRaum.setLocalScale(new Vector3f(faktorL,1,1));
							tempRaum.setLaenge(tempRaum.getLaenge()*faktorL);
						}
						
					}
					if(seite==1){
						float faktorR = flureImGebaude.get(flur).getLaenge()/flureImGebaude.get(flur).getLangeDerAngeordnetenRaumeRechts();
						Raum tempRaum=(Raum)flureImGebaude.get(flur).flurseite[1].get(r);
						
						//Wenn die restflurlaenge zu gross ist nciht skalieren
						if(!(faktorR>2)){
						tempRaum.setLocalScale(new Vector3f(faktorR,1,1));
						tempRaum.setLaenge(tempRaum.getLaenge()*faktorR);
						}
					}					
				}				
			}
		updateAllFlurseiten(flureImGebaude);
	}

	/**
	* fehlerSuche  -Die Methode dient zur Fehlersuche
	* 
	* @param raume
	*            alle Raume im Gebaude
	*/
	private void fehlerSuche(Gebaeude fh){
	
			// Stockwerke
			for (int st = 0; st < fh.stockwerkeImGebaude.size(); st++) {
				// flure
				for (int f = 0; f < fh.stockwerkeImGebaude.get(st).flureImStockwerk.size(); f++) { 
					// flurseiten
					for (int f_lr = 0; f_lr < fh.stockwerkeImGebaude.get(st).flureImStockwerk.get(f).flurseite.length; f_lr++){
						// raeume
						for (int r = 0; r < fh.stockwerkeImGebaude.get(st).flureImStockwerk.get(f).flurseite[f_lr].size(); r++) {
	
							Raum tempRaum;
							tempRaum = (Raum) fh.stockwerkeImGebaude.get(st).flureImStockwerk.get(f).flurseite[f_lr].get(r);
							
							String fehlerText="";
																									
							// Fehlermeldung, falls raumID nicht mit index in arrayList uebereinstimmt
							if (tempRaum.getRaumID() != r) {
								fehlerText+="\t\t "+tempRaum.getRaumID() + " " + r
										+ " | st, f, flr :" + st + ", " + f + ", "
										+ f_lr;												
							}
							if (tempRaum.getFlur().getLaenge()!=tempRaum.getFlur().langeDerAngeordnetenRaumeLinks+tempRaum.getFlur().getRestLangeFlurLinks()
									|| tempRaum.getFlur().getLaenge()!=tempRaum.getFlur().langeDerAngeordnetenRaumeRechts+tempRaum.getFlur().getRestLangeFlurRechts()){
								fehlerText+="\t\t flurlaengen stimmen nicht";
							}
							if (!tempRaum.isAngeordnet()){
								fehlerText+="\t\t raum "+ r +" ist nicht angeordnet";
							}
							if (!(tempRaum.getFitness_gesamt()>=-500000 && tempRaum.getFitness_gesamt()<=0) ){
								fehlerText+="\t\t raum "+ r +" fitness ist "+tempRaum.getFitness_gesamt() ;
							}
						
							
							if(fehlerText.length()>1){
								System.out.println("\n*********** Fehlerbericht:\n"+fehlerText+"\n*********************\n");
							}
						}
	
					}
				}
			}
	
	}

	/**
	 * updateAllFlurseiten  -Berechnet die neue RaumID aller Raeume an diesem Flur
	 * @param flur
	 *            alle Flure im Gebaude 
	 */
	private void updateAllFlurseiten(ArrayList<Flur> flur){
		for(int t=0; t< flur.size();t++){
			for(int ts=0; ts<2; ts++)
			updateFlurseite(flur.get(t), ts);
		}
	}
	
	/**
	 * updateFlurseite  -Berechnet die Werte aller Raeume an diesem Flur neu
	 * 
	 * @param flur 
	 * 				zu aktualisierender Flur
	 * @param seite
	 * 				zu aktualisierende Seite
	 */
	private void updateFlurseite(Flur flur, int seite){
			
			//RaumID + Flur des Raums + Flurseite des raums + angeordnet
			for (int r=0; r<flur.flurseite[seite].size(); r++){
				Raum tempRaum;
				tempRaum=(Raum)flur.flurseite[seite].get(r);
				tempRaum.setRaumID(flur.flurseite[seite].indexOf(tempRaum));
				tempRaum.setFlur(flur);
				tempRaum.setFlurSeite(seite);
				tempRaum.setAngeordnet(true);
				if (seite == 0 && !(flur.NodeRaumeLinks.hasChild(tempRaum)))
					flur.NodeRaumeLinks.attachChild(tempRaum);
				if (seite == 1 && !(flur.NodeRaumeRechts.hasChild(tempRaum)))
					flur.NodeRaumeRechts.attachChild(tempRaum);
			}	
			
			//Flurlaengen berechnen und zuweisen
			float tempLaenge=0;
			//laenge der angeordneten Raume
			for (int r=0; r<flur.flurseite[seite].size(); r++){
				if(seite==0)tempLaenge += flur.raumeLinks.get(r).getLaenge();			
				if(seite==1)tempLaenge += flur.raumeRechts.get(r).getLaenge();			
			}
			//Zuweisung der genutzten Lange und der Restlaenge
			if(seite==0){
				flur.langeDerAngeordnetenRaumeLinks =tempLaenge;
				flur.restLangeFlurLinks = flur.getLaenge()- tempLaenge;
			}
			if(seite==1){
				flur.langeDerAngeordnetenRaumeRechts = tempLaenge;	
				flur.restLangeFlurRechts = flur.getLaenge()- tempLaenge;	
			}
			
			//nachbarn
			for (int r=0; r<flur.flurseite[seite].size(); r++){
				Raum tempRaum;
				tempRaum=(Raum)flur.flurseite[seite].get(r);
				//wenn am anfang
				if(r==0){
					tempRaum.setNachbar_links(null);
					if(flur.flurseite[seite].size()>1)
						tempRaum.setNachbar_rechts((Raum)flur.flurseite[seite].get(r+1));
					else 
						tempRaum.setNachbar_rechts(null);
				}
				//wenn am ende
				else if(r==flur.flurseite[seite].size()-1){
					tempRaum.setNachbar_rechts(null);
					if(flur.flurseite[seite].size()>1)
						tempRaum.setNachbar_links((Raum)flur.flurseite[seite].get(r-1));
				}
				else {
					tempRaum.setNachbar_links((Raum)flur.flurseite[seite].get(r-1));
					tempRaum.setNachbar_rechts((Raum)flur.flurseite[seite].get(r+1));
				}
				//Position des Raums neu berechnen
				tempRaum.updatePosition();
				//Fitness des Raums neu berechnen
				tempRaum.fitness_berechnen();
			}	
	}

	/**
	 * updateRaumID  -berechnet die neue RaumID, Flur, Flurseite aller Raeume an diesem Flur
	 * @param flur 
	 * 				-Flur, der aktualisiert werden soll
	 * @param seite 
	 * 				-seite, die aktualisiert werden soll 
	 */
	private void updateRaumID(Flur flur, int seite){
			for (int r=0; r<flur.flurseite[seite].size(); r++){
				Raum tempRaum;
				tempRaum=(Raum)flur.flurseite[seite].get(r);
				tempRaum.setRaumID(flur.flurseite[seite].indexOf(tempRaum));
				tempRaum.setFlur(flur);
				tempRaum.setFlurSeite(seite);
			}	
	}
	
	/**
	 * updateFlurLaengen berechnet die neuen Flurlaengen
	 * @param flur 
	 * 				-Flur, der aktualisiert werden soll	 
	 * @param seite 
	 * 				-seite, die aktualisiert werden soll
	 */
	private void updateFlurLaengen(Flur flur, int seite){
		float tempLaenge=0;
		for (int r=0; r<flur.flurseite[seite].size(); r++){
			if(seite==0)tempLaenge += flur.raumeLinks.get(r).getLaenge();			
			if(seite==1)tempLaenge += flur.raumeRechts.get(r).getLaenge();			
		}
		if(seite==0){
			flur.langeDerAngeordnetenRaumeLinks =tempLaenge;
			flur.restLangeFlurLinks = flur.getLaenge()- tempLaenge;
		}
		if(seite==1){
			flur.langeDerAngeordnetenRaumeRechts = tempLaenge;	
			flur.restLangeFlurRechts = flur.getLaenge()- tempLaenge;	
		}
}
	
	/**
	 * updateNachbarn  updateFlurLaengen berechnet die neuen Nachbarn, der Raume im Flur
	 * @param flur 
	 * 				-Flur, der aktualisiert werden soll	 
	 * @param seite 
	 * 				-seite, die aktualisiert werden soll
	 */
	private void updateNachbarn(Flur flur, int seite){
		for (int r=0; r<flur.flurseite[seite].size(); r++){
			Raum tempRaum;
			tempRaum=(Raum)flur.flurseite[seite].get(r);
			//wenn am anfang
			if(r==0){
				tempRaum.setNachbar_links(null);
				if(flur.flurseite[seite].size()>1)
					tempRaum.setNachbar_rechts((Raum)flur.flurseite[seite].get(r+1));
				else 
					tempRaum.setNachbar_rechts(null);
			}
			//wenn am ende
			else if(r==flur.flurseite[seite].size()-1){
				tempRaum.setNachbar_rechts(null);
				if(flur.flurseite[seite].size()>1)
					tempRaum.setNachbar_links((Raum)flur.flurseite[seite].get(r-1));
			}
			else {
				tempRaum.setNachbar_links((Raum)flur.flurseite[seite].get(r-1));
				tempRaum.setNachbar_rechts((Raum)flur.flurseite[seite].get(r+1));
			}
		}	
}
	
	/**
	 * updateRaumPosition  Die Methode berechnet die neuen Raumpositionen aller Raume
	 * 
	 * @param raume
	 *            alle Raume im Gebaude
	 */
	private void updateRaumPosition(ArrayList<Raum> raume) {
		for (int i = 0; i < raume.size(); i++) {
			Raum tempRaum;
			tempRaum = (Raum) raume.get(i);
			tempRaum.updatePosition();
		}
	}

	/**
	 * getMisserfolgKritisch 
	 * 
	 * @return int
	 *            gibt anzahl kritischer misserfolge aus
	 */
	public int getMisserfolgKritisch() {
		return misserfolgKritisch;
	}

	/**
	 * getMisserfolgKritisch 
	 * 
	 * @return int
	 *            gibt anzahl der Optimierungsschritte aus
	 */
	public int getOptimierungsschritt() {
		return Optimierungsschritt;
	}

	/**
	 * getAktuelleGesamtFitness 
	 * 
	 * @return float
	 *            gibt anzahl  aktuelle Gesamtfitness aus
	 */
	public float getAktuelleGesamtFitness() {
		return aktuelleGesamtFitness;
	}

	/**
	 * isFertig 
	 * 
	 * @return boolean
	 *            gibt an ob der optimierer fertig ist
	 */
	public boolean isFertig() {
		return fertig;
	}

	/**
	 * setFertig 
	 * 
	 * @return fertig
	 *            sagt dem optimierer, dass er nicht fertig ist
	 */
	public void setFertig(boolean fertig) {
		this.fertig = fertig;
	}

}