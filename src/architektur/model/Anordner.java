
package architektur.model;

import architektur.input.FHdata;
import architektur.input.GrundstuecksZelle;
import architektur.model.gebaude.Gebaeude;
import architektur.model.gebaude.Raum;
import architektur.model.gebaude.Stockwerk;

/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh,   Matrikelnummer: 11056380, groh.markus@arcor.de 
 * @author Fabian Pflaum, Matrikelnummer: 11056940, FabianPflaum@googlemail.com
 * @version 22.06.2009
 * Klasse erstellt eine Instanz vom Objekt Gebaeude, innerhalb dieses Gebaeudes in abhaengigkeit der Gesamtflaeche der Raeume im Cocktail
 * und der Flaeche der plazierten Raeume im Gebaeude Stcokwerke und Flure.<br>
 * Nach jeder Stockwerkserstellung/Flurerstellung werden diese mit Raeumen gefuellt und die Flaechen verglichen. Ist die Cocktailflaeche groesser als
 * die Flaeche der im Gebaeude plazierten Raeume werden neue Stockwerke/Flure erstellt. 
 */
public class Anordner {
	
	/**
	 * FH Daten enthaelt die Daten der FH (Studentanzahl, Raumtiefe,...)
	 */
	public FHdata meineDaten;
	
	/**
	 * Cocktail der Raume die zur FH gehoeren
	 */
	public Raum[] cocktail;
	
	/**
	 * Beinhaltet das erstellt und mit Raeumen gefuellte Gebaeude
	 */
	public Gebaeude FH;
	
	/**
	 * Die optimale Zelle zur plazierung des Gebaeudemittelpunktes
	 */
	public GrundstuecksZelle optimaleZelle;
	
	/**
	 * Flaeche der Raeume imCocktail
	 */
	public float gesamtflaecheCocktail;
	
	/**
	 * Flaeche der im Gebaeude angeordneter Raeume 
	 */
	public float tatsaechlichGenutzeFlaecheGebaeude;
	
	
	/**
	 * Konstruktor
	 * @param meineDaten Daten der FH
	 * @param cocktail Raume der FH
	 */
	public Anordner (FHdata meineDaten, Raum[] cocktail, GrundstuecksZelle optimaleZelle){
		this.meineDaten = meineDaten;
		this.cocktail = cocktail;
		this.optimaleZelle = optimaleZelle;
		this.tatsaechlichGenutzeFlaecheGebaeude = 0;
	}
	
	/**
	 * Methode brechnet die tatsaechliche Fleache der angeordneten Raeume im Gebaeude
	 * @param FH Genaeude mit angeordneten Raeumen
	 */
	private void tatsaechlichBenutzteGebaeudeFlaecheBerechnen(Gebaeude FH){
		
		float flaeche = 0;
		
		for(int stockwerk = 0; stockwerk < FH.stockwerkeImGebaude.size(); stockwerk++){
			for(int flur = 0; flur < FH.stockwerkeImGebaude.get(stockwerk).flureImStockwerk.size(); flur++){
				flaeche += FH.stockwerkeImGebaude.get(stockwerk).flureImStockwerk.get(flur).langeDerAngeordnetenRaumeLinks * this.meineDaten.raumtiefe;
				flaeche += FH.stockwerkeImGebaude.get(stockwerk).flureImStockwerk.get(flur).langeDerAngeordnetenRaumeRechts * this.meineDaten.raumtiefe;
			}
		}
		this.tatsaechlichGenutzeFlaecheGebaeude = flaeche;
	}
	
	
	/**
	 * Methode berechnet die Gesamtflaeche der Raueme im Cocktail
	 * @param cocktail Cocktail mit Raumen, die in der FH vorhanden sein sollen
	 * @return Gesamtflache der Raume im Cocktail
	 */
	private int gesamtflaecheDerCocktailRaumeBerechnen (Raum[] cocktail){
		
		int gesamtFlaeche = 0;
		
		for(int i=0; i < cocktail.length; i++){
			gesamtFlaeche += cocktail[i].getFlaeche();
		}

		return gesamtFlaeche;
	}
	
	
	/**
	 * Methode erstellt das Gebaeude in abhaengigkeit der Gesamtflaeche der Raeume im Cocktail und der Gesamtflaeche der im Gebaeude 
	 * angeordneten Raeume.<br>
	 * Es werden solange Stockwerke und Flure in den Stockwerken erstellt, bis alle Raeume des Cocktail angeordnet sind.<br>
	 * Datu wird zunaechst ein Stockwerk erstellt und in diesem Stcokwerk ein Flur. Es werden in diesem Flur Raeume angebracht bis der Flur belegt ist.<br>
	 * Wenn danach die Flaeche der im Gebaeude angeordneten Raeume kleiner als die Gesamtflaeche der Raeume im Cocktail ist, wird ein weiterer Flur erzeugt.<br>
	 * Sind in einem Stockwerk max. 4 Flure erstellt und die Flaeche nicht ausreichen wird ein weiteres Stockwerk erstellt, usw.
	 * @return FH Gebaeude mit den angeordneten Raeumen aus dem Cocktail
	 */
	public Gebaeude anordnen(){
		
		//Sortierung des Cocktails ansteigend nach den Raeumen mit der groessten Laenge
		this.cocktailSort(cocktail);
		
		//Gesamtflaeche der im Cocktail befindlichen Raeume wird berechnet
		this.gesamtflaecheCocktail = this.gesamtflaecheDerCocktailRaumeBerechnen(cocktail);
		
		//Instanz der Klasse Gebaeude wird erstellt
		//Dieses Objekt beinhalten die die Stockwerke (damit die Flure in den STockwerken) und die Nodes
		this.FH = new Gebaeude(this.meineDaten, this.optimaleZelle);
		
		//Solange Gesamtflaeche der Raeume im Cocktail > tatsaechliche Flaeche der im Gebaeude angeordneten Raeume wird ein Stockwerk erstellt
		while(this.gesamtflaecheCocktail > this.tatsaechlichGenutzeFlaecheGebaeude){
			
			//Erstellung eines Stockwerks und zuweisung in ArrayList des Gebaeudes, die die Stcokwerke beinhaltet
			this.FH.stockwerkeImGebaude.add(new Stockwerk(this.meineDaten));
			
			//Zaehler fuer Anzahl der Flure
			int index = 0;
			
			//Solange Gesamtflaeche der Raeume im Cocktail > tatsaechliche Flaeche der im Gebaeude angeordneten Raeume wird ein Flur erstellt
			//Maximal 4 Flure pro Stockwerk
			while(this.gesamtflaecheCocktail > this.tatsaechlichGenutzeFlaecheGebaeude && index < 4){
				
				//Esretllen eines Flures durch aufruf der Methode aus der aktuellen Klasse Stockwerk
				this.FH.stockwerkeImGebaude.get(this.FH.stockwerkeImGebaude.size() - 1).flureImStockwerkErstellen(index);
				
				//Den Cocktail durchlaufen und Raeume im Flur anordnen
				for(int i = 0; i <cocktail.length; i++){
					
					//Es werden nur Raeume angeordnet, die noch nicht angeordnet wurden
					if(cocktail[i].isAngeordnet() != true){
					
						//Wenn Raum in die linke oder rechte Flurseite passt wird er durch aufruf der Methode raumImGebaeudePlazieren angeordnet
						if(FH.stockwerkeImGebaude.get(FH.stockwerkeImGebaude.size() - 1).flureImStockwerk.get(index).restLangeFlurLinks >= cocktail[i].getLaenge()){
							this.raumImGebaudePlazieren(cocktail[i], (FH.stockwerkeImGebaude.size() - 1), index, 1, FH);
						}
						else if(FH.stockwerkeImGebaude.get(FH.stockwerkeImGebaude.size() - 1).flureImStockwerk.get(index).restLangeFlurRechts >= cocktail[i].getLaenge()){
							this.raumImGebaudePlazieren(cocktail[i], (FH.stockwerkeImGebaude.size() - 1), index, 0, FH);
						}
					}

				}//for
				
				//Zaehler fuer Flure erhoehen, wenn ein neuer Flur erstell wurde
				index++;
				
				//Nach jedem Erstellen eines Flures wird die Flaeche der derzeit angeordneten Raeume im Gebaeude berechnet,
				//wenn die Flaeche der Raeume im Gebaeude > als die Flaeche der Raeume im Cocktail werden keine Flure oder Stockwerke mehr erstellt
				this.tatsaechlichBenutzteGebaeudeFlaecheBerechnen(FH);
				
			}//while
			
			//Nach erstellung eines Stockwerks werden die benoetigten Translationen und Rotationen im aktuell erstellten Stockwerk
			//durchgefuehrt. Roatation der Flure und Translation an das Treppenhaus
			//Aufruf der Methode aus Klasse Stockwerk
			this.FH.stockwerkeImGebaude.get(this.FH.stockwerkeImGebaude.size()-1).stockwerksTranslationen();

		}//while
			
		
		 
		  //Fehler, wenn nicht alle Raume angeordnet werden
		  int anzahlNichtangeordneteRaume=0;
		  for(int i = 0; i < cocktail.length; i++){
			  if(cocktail[i].isAngeordnet()==false) 
				  anzahlNichtangeordneteRaume ++ ;
		  }
		  System.out.println("############### "+anzahlNichtangeordneteRaume+" Raume nicht angeordnet");
		  
		//Nach Erstellung aller Stockwerke werden die Nodes der einzelnen Stockwerke der Node im Gebaeude zugewiesen
		//Zusaetzlich wird eine Translation der Stockwerke in Y-Richtung vorgenommen
		this.FH.stockwerkeImGebaudeDenNodesZuweisen();
		
		//Nachbarn der Raeume im Gebaeude bestimmen
		this.nachbarBestimmen(FH);
		
		//Position der Raeume bestimmen
		this.raumPositionBerechnen(FH);

		//Erstelltes FH Gebaeude
		return FH;
	}
	
	
	
	/**
	 * Methode bestimmt die Nachbarn der angeordneten Raume im Gebaeude
	 * @param FH FH mit Raeumen, die alle ihren Nachbarn halten
	 */
	private void nachbarBestimmen (Gebaeude FH){
		
		//Bestimmung der Nummer des Nachbarn in der Liste der Raume links
				
		//Stockwerke durchlaufen
		for(int i = 0; i < FH.stockwerkeImGebaude.size(); i++){
			
			for(int j = 0; j < FH.stockwerkeImGebaude.get(i).flureImStockwerk.size(); j++){
				
				for(int e = 0; e < FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.size(); e++){
					
					if(e == 0){
						if(e == 0 && e == FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.size() - 1){
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbarLinksNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbarRechtsNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbar_links(null);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbar_rechts(null);
						}
						else{
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbarLinksNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbarRechtsNummer(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e + 1).getRaumnummer());
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbar_links(null);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbar_rechts(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e + 1));
						}
						
					}
					
					if(e == FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.size() - 1){
						if(e == 0 && e == FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.size() - 1){
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbarLinksNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbarRechtsNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbar_links(null);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbar_rechts(null);
						}
						else{
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbarRechtsNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbarLinksNummer(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e - 1).getRaumnummer());
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbar_rechts(null);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbar_links(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e - 1));
						}
					}
					if(e != FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.size() - 1 && e != 0){
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbarLinksNummer(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e - 1).getRaumnummer());
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbarRechtsNummer(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e + 1).getRaumnummer());
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbar_links(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e - 1));
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setNachbar_rechts(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e + 1));
					}
				
				}//for Raume				
				
			}//for Flure
			
		}//for STockwerke
		
		//Bestimmung der Nummer des Nachbarn in der Liste der Raume rechts
		for(int i = 0; i < FH.stockwerkeImGebaude.size(); i++){
			
			for(int j = 0; j < FH.stockwerkeImGebaude.get(i).flureImStockwerk.size(); j++){
				
				
				for(int e = 0; e < FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.size(); e++){
					
					if(e == 0){
						if(e == 0 && e == FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.size() - 1){
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbarLinksNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbarRechtsNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbar_links(null);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbar_rechts(null);
						}
						else{
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbarLinksNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbarRechtsNummer(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e + 1).getRaumnummer());
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbar_links(null);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbar_rechts(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e + 1));
						}
						
					}
					
					if(e == FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.size() - 1){
						if(e == 0 && e == FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.size() - 1){
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbarLinksNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbarRechtsNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbar_links(null);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbar_rechts(null);
						}
						else{
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbarRechtsNummer(0);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbarLinksNummer(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e - 1).getRaumnummer());
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbar_rechts(null);
							FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbar_links(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e - 1));
						}
					}
					if(e != FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.size() - 1 && e != 0){
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbarLinksNummer(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e - 1).getRaumnummer());
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbarRechtsNummer(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e + 1).getRaumnummer());
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbar_links(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e - 1));
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setNachbar_rechts(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e + 1));
					}
				
				}//for Raume				
				
			}//for Flure
			
		}//for STockwerke
		
	}//private void nachbarBestimmen
	
	
	/**
	 * Methode verschiebt die angeordneten Raeume des Gebaeudes in ihrer X-Richtung.
	 * Die Verschiebung orientiert sich an der Position des linken (vom aktuellen Objekt aus neg. X-Richtung) Nachbar des Raumes und verschiebt 
	 * den Raum um die Position des NAchbarn + die eigene Raumlaenge
	 * @param FH Mit Raeumen, die in ihre Position verschoben wurden
	 */
	private void raumPositionBerechnen(Gebaeude FH){
		
		for(int i = 0; i < FH.stockwerkeImGebaude.size(); i++){
			
			for(int j = 0; j < FH.stockwerkeImGebaude.get(i).flureImStockwerk.size(); j++){
				
				for(int e = 0; e < FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.size(); e++){
					
					if (FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).getNachbar_links() == null){
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setLocalTranslation(0,0,0);
					}
					else{
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).setLocalTranslation(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).getNachbar_links().getLocalTranslation().x + FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeLinks.get(e).getNachbar_links().getLaenge()  , 0, 0);						
					}
				
				}//for Raume				
				
			}//for Flure
			
		}//for STockwerke
		
		for(int i = 0; i < FH.stockwerkeImGebaude.size(); i++){
			
			for(int j = 0; j < FH.stockwerkeImGebaude.get(i).flureImStockwerk.size(); j++){
				
				for(int e = 0; e < FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.size(); e++){
					
					if (FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).getNachbar_links() == null){
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setLocalTranslation(0,0,0);
					}
					else{
						FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).setLocalTranslation(FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).getNachbar_links().getLocalTranslation().x + FH.stockwerkeImGebaude.get(i).flureImStockwerk.get(j).raumeRechts.get(e).getNachbar_links().getLaenge() , 0, 0);
					}
				
				}//for Raume				
				
			}//for Flure
			
		}//for STockwerke
	}
	
	
	/**
	 * Methode ordnet den Raum im durch die Methode "anordnen" bestimmten Stockwerk,Flur und Flurseite an.<br>
	 * Der Raum bekommt seine aktuelle Position zugewiesen (setFlur, setFlurSeite), eine ID (setRaumID) und er wird als "angeordnet" makiert.<br>
	 * Der Raum speichert nun seinen Flur in dem er sich befindet, die Flurseite und die Position in der ArrayList des Flures.<br><br>
	 * Gleichzeitig wird der Raum der Node seines Flures hinzugefuegt, und die Daten "langeDerAngeordnetenRaumeLinks" und "restLangeFlurLinks" seines
	 * Flures werden aktualisiert.  
	 * @param raum Raum der im Gebaeude plaziert wird
	 * @param etage Stockwerk in das der Raum plaziert wird
	 * @param flur Flur in den der Raum plaziert wird
	 * @param seite Flurseite in die der Raum plaziert wird (0 = linke Flurseite ; 1 = rechte Flurseite)
	 * @param FH FH Gebaeude mit plaziertem Raum
	 */
	private void raumImGebaudePlazieren(Raum raum, int etage, int flur, int seite, Gebaeude FH){
		
		raum.setAngeordnet(true);
		
		if(seite == 1){
			
			FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur).raumeLinks.add(raum);
			FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur).NodeRaumeLinks.attachChild(raum);
			FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur).langeDerAngeordnetenRaumeLinks += raum.getLaenge();
			FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur).restLangeFlurLinks -= raum.getLaenge();
			
			raum.setFlur(FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur));
			raum.setFlurSeite(0);
			raum.setRaumID(FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur).raumeLinks.size()-1);
			
		}
		else{
			
			FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur).raumeRechts.add(raum);
			FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur).NodeRaumeRechts.attachChild(raum);
			FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur).langeDerAngeordnetenRaumeRechts += raum.getLaenge(); 
			FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur).restLangeFlurRechts -= raum.getLaenge();
			
			raum.setFlur(FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur));
			raum.setFlurSeite(1);
			raum.setRaumID(FH.stockwerkeImGebaude.get(etage).flureImStockwerk.get(flur).raumeRechts.size()-1);
		}
	
	}
	
	/**
	 * Methode sortiert die Raeume des Cocktail absteigend nach ihrer Laenge
	 * @param cocktail Cocktail mit absteigender Sortierung der Raeume nach ihrer Laenge
	 */
	private void cocktailSort (Raum [] cocktail){
		
		int i = 1;
		while ( i < cocktail.length ) {
		    int j = cocktail.length - 1;
		    while ( j >= i ) {
			if ( cocktail[j].getLaenge() > cocktail[j-1].getLaenge() ) {
			    Raum temp = cocktail[j];
			    cocktail[j] = cocktail[j-1];
			    cocktail[j-1] = temp;
			}
			j = j-1;
		    }
		    i = i+1;
		}

	}
}
