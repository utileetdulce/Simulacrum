package architektur.model;

import architektur.input.FHdata;
import architektur.input.Grundstueck;
import architektur.input.GrundstuecksZelle;

/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh, Matrikelnummer: 11056380, groh.markus@arcor.de
 * @version 10.06.2009
 * Klasse enthaelt Methoden, die fuer jede Zelle des Grundstuecks die Entfernung ,in pos. X/Z-Richtung und neg. X/Y-Richtung, bestimmen auf der der<br>
 * Flur in seiner breite plazierbar ist, sowie Methoden, die aus diesen Informationen, die jede Zelle speichert die optimale Zelle bestimmt um das Gebaeude zu plazieren.
 */
public class Position {
	
	/**
	 * Objekt der Klasse Grundstueck, welches das Grundstuecksraster enthaelt
	 */
	public Grundstueck grundstueck;
	
	/**
	 * Daten der FH
	 */
	public FHdata meineDaten;

	/**
	 * Die minimale Flurlaenge
	 * wird aus den Daten der FH (FHdate meineDaten) bestimmt
	 */
	public int minimaleFlurlaenge;
	
	
	
	/**
	 * Konstruktor
	 * @param grundstuecksDaten Enthaelt das Grundstuecksraster
	 * @param meineDaten Enthaelt die Daten der FH (maximale Flurlaenge, Breite eines Fluegels der FH)
	 */
	public Position(Grundstueck grundstuecksDaten, FHdata meineDaten){
		this.grundstueck = grundstuecksDaten;
		this.meineDaten = meineDaten;
		this.minimaleFlurlaenge = meineDaten.minimaleFlurlaenge;
	}
	
	/**
	 * Methode bestimmt fuer jede Zelle innerhalb des bebaubaren Grundstuecks die Abstaende zur Grundstuecksgrenze in 
	 * pos X/Z und neg X/Z Richtung.<br>
	 * Fuer jede Zelle wird die Entfernung zur Grundstuecksgrenze besitmmt, indem die Anzahl der Zellen und deren Ausdehnung (5m)
	 * addiert werden. Zusaetzlich wird fuer jede moegliche Zelle bis zur Grenze ueberprueft, ob der Flur (Fluegel) in der vollen 
	 * Breite plazierbar ist.<br>
	 * Beispiel: Sind von der aktuellen Zelle 5 Zellen in pos. X-Richtung frei bis zur Grundstuecksgrenze, so wird für jede dieser
	 * 5 Zellen in beide Z-Richtungen ueberprueft, ob in der Flur auch in der breite plazierbar ist.<br>
	 * Jede Zelle haelt so die Information, in welcher Laenge der Flur in alle vier Richtungen in voller Breite plazierbar ist.<br>
	 * Nachdem diese Info fuer jede Zelle bestimmt ist wird die Methode "bestenRasterPunktBestimmen" aufgerufen, welche die optimale Zelle 
	 * bestimmt und zurueckgibt.
	 * @return Die optimale Zelle des Grundstuecksrasters zur Plazierung des Gebaeudes
	 */
	public GrundstuecksZelle optimalePositionBestimmen(){
		
		//laufe durch alle Punkte
		for(int y = 0; y < this.grundstueck.grundstuecksRaster[0].length; y++){
			for(int x = 0; x < this.grundstueck.grundstuecksRaster.length; x++){
				
				if(this.grundstueck.grundstuecksRaster[x][y].gehoertZumGrundstueck == true){
				
					//Rasterbreite
					float rasterbreite = this.grundstueck.grundstuecksRaster[x][y].xExtent * 2;
					
					boolean flurAufGanzerLaengeMoeglich = false;
					
					//Anzahl der abzufragenden Rasterpunkte in X/Y Richtung
					//ergibt sich aus Rasterbreite (i.M. festgelegt in GrundstuecksRasterParzelle = 5m)
					//und der Treppenhausbreite (2 * Raumtiefe + Flurbreite)
					//Treppenhausbreite / Rastergroesse
					int anzahlRaster;
					
					//Wenn Flurbreite / Rasterbreite ohne Rest ansonsten +1 Raster
					if((this.meineDaten.raumtiefe * 2 + this.meineDaten.flurbreite / rasterbreite) % 2 == 0)
						anzahlRaster = (int) ( ((this.meineDaten.raumtiefe * 2 + this.meineDaten.flurbreite) / rasterbreite));
					else
						anzahlRaster = 1 + (int) ( ((this.meineDaten.raumtiefe + this.meineDaten.flurbreite) / rasterbreite));
					
					
					///ABFRAGE IN POSITIVER X RICHTUNG SPEICHERN DER MAX LANGE IN this.grundstueck.grundstuecksRaster[y][x].maxLangeX
					for(int xtemp = x; xtemp < this.grundstueck.grundstuecksRaster.length; xtemp++){						
						if(this.grundstueck.grundstuecksRaster[xtemp][y].gehoertZumGrundstueck == false) break;{ //[y][x]
							
							//Abfrage fuer aktuelle Zelle, ob Flur in Y und -Y Richtung, also in voller Breite setzbar ist
							for(int ytemp = y; ytemp < y + anzahlRaster; ytemp++){
								
								if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == true){
									flurAufGanzerLaengeMoeglich = true;
									
								}
								if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == false){
									flurAufGanzerLaengeMoeglich = false;
									break;
								}
							}
							if(flurAufGanzerLaengeMoeglich == true){
								for(int ytemp = y; ytemp > y - anzahlRaster; ytemp--){
								
									if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == true){
										flurAufGanzerLaengeMoeglich = true;
										//this.grundstueck.grundstuecksRaster[y][x].maxLangeX += rasterbreite;
									}
									if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == false){
										flurAufGanzerLaengeMoeglich = false;
										break;
									}
								}
							}
							if(flurAufGanzerLaengeMoeglich == true)
								this.grundstueck.grundstuecksRaster[x][y].maxLangeX += rasterbreite;
							if(flurAufGanzerLaengeMoeglich == false){
								break;
							}
						}//IF	
					}//FOR
					
					///ABFRAGE IN NEGATIVER X RICHTUNG SPEICHERN DER MAX LANGE IN this.grundstueck.grundstuecksRaster[y][x].maxLangeX_
					for(int xtemp = x; xtemp >= 0; xtemp--){						
						if(this.grundstueck.grundstuecksRaster[xtemp][y].gehoertZumGrundstueck == false) break; { //[y][x]
							
							//Abfrage fuer aktuelle Zelle, ob Flur in Y und -Y Richtung, also in voller Breite setzbar ist
							for(int ytemp = y; ytemp < y + anzahlRaster; ytemp++){
								
								if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == true){
									flurAufGanzerLaengeMoeglich = true;
									
								}
								if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == false){
									flurAufGanzerLaengeMoeglich = false;
									break;
								}
							}
							if(flurAufGanzerLaengeMoeglich == true){
								for(int ytemp = y; ytemp > y - anzahlRaster; ytemp--){
								
									if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == true){
										flurAufGanzerLaengeMoeglich = true;
										//this.grundstueck.grundstuecksRaster[y][x].maxLangeX += rasterbreite;
									}
									if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == false){
										flurAufGanzerLaengeMoeglich = false;
										break;
									}
								}
							}
							if(flurAufGanzerLaengeMoeglich == true)
								this.grundstueck.grundstuecksRaster[x][y].maxLangeX_ += rasterbreite;
							if(flurAufGanzerLaengeMoeglich == false){
								break;
							}
						
						}//FOR
	
					}//FOR	
					
					
					///ABFRAGE IN POSITIVER Y RICHTUNG SPEICHERN DER MAX LANGE IN this.grundstueck.grundstuecksRaster[y][x].maxLangeX
					for(int ytemp = y; ytemp < this.grundstueck.grundstuecksRaster.length; ytemp++){						
						
						if(this.grundstueck.grundstuecksRaster[x][ytemp].gehoertZumGrundstueck == false) //[y][x]
						 break;	
						
						//Abfrage fuer aktuelle Zelle, ob Flur in X und -X Richtung, also in voller Breite setzbar ist
						for(int xtemp = x; xtemp < x + anzahlRaster; xtemp++){
								
								if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == true){
									flurAufGanzerLaengeMoeglich = true;
									
								}
								if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == false){
									flurAufGanzerLaengeMoeglich = false;
									break;
								}
							}
							
							if(flurAufGanzerLaengeMoeglich == true){
								for(int xtemp = x; xtemp > x - anzahlRaster; xtemp--){
								
									if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == true){
										flurAufGanzerLaengeMoeglich = true;
										//this.grundstueck.grundstuecksRaster[y][x].maxLangeX += rasterbreite;
									}
									if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == false){
										flurAufGanzerLaengeMoeglich = false;
										break;
									}
								}
							}
							if(flurAufGanzerLaengeMoeglich == true)
								this.grundstueck.grundstuecksRaster[x][y].maxLangeY += rasterbreite;
							if(flurAufGanzerLaengeMoeglich == false){
								break;
							}
							
					}//FOR
					
					
					///ABFRAGE IN NEGATIVER Y RICHTUNG SPEICHERN DER MAX LANGE IN this.grundstueck.grundstuecksRaster[y][x].maxLangeY_
					for(int ytemp = y; ytemp > 0; ytemp--){						
						if(this.grundstueck.grundstuecksRaster[x][ytemp].gehoertZumGrundstueck == false) break;{ //[y][x]
							
							//Abfrage fuer aktuelle Zelle, ob Flur in X und -X Richtung, also in voller Breite setzbar ist
							for(int xtemp = x; xtemp < x + anzahlRaster; xtemp++){
								
								if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == true){
									flurAufGanzerLaengeMoeglich = true;
									
								}
								if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == false){
									flurAufGanzerLaengeMoeglich = false;
									break;
								}
							}
							if(flurAufGanzerLaengeMoeglich == true){
								for(int xtemp = x; xtemp > x - anzahlRaster; xtemp--){
								
									if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == true){
										flurAufGanzerLaengeMoeglich = true;
										//this.grundstueck.grundstuecksRaster[y][x].maxLangeX += rasterbreite;
									}
									if(this.grundstueck.grundstuecksRaster[xtemp][ytemp].gehoertZumGrundstueck == false){
										flurAufGanzerLaengeMoeglich = false;
										break;
									}
								}
							}
							if(flurAufGanzerLaengeMoeglich == true)
								this.grundstueck.grundstuecksRaster[x][y].maxLangeY_ += rasterbreite;
							if(flurAufGanzerLaengeMoeglich == false){
								break;
							}
						}//FOR	
					}//FOR
					
					
					
				}//if(this.grundstueck.grundstuecksRaster[y][x].gehoertZumGrundstueck == true)
			}//FOR
		}//FOR
		
		//Es wird die optimale Zelle zurueckgegeben, die durch den Aufruf der Methode "bestenRasterPunktBestimmen" ermittel wird
		return this.bestenRasterPunktBestimmen();
	}
	
	
	/**
	 * Methode berechnet fuer jeden Punkt die maximale Ausdehnung, indem die Summe der moeglichen Flurlaengen in X/Z und -X/-Z Richtung gebildet wird. <br>
	 * Um einen gewissen Abstand des Gebaeudes zur Grundstuecksgrenze zu gewaehrleisten wird diese Summe nur für Zellen 
	 * gebildet, die mindestens 25m vom Grundstuecksrand entfernt sind. <br>
	 * Sind mehrere Zellen mit der gleichen Summe vorhanden wird eine per Zufall ausgewaehlt.
	 * @return Die optimale Zelle zur plazierung des Gebaeudes
	 */
	private GrundstuecksZelle bestenRasterPunktBestimmen(){
		
		GrundstuecksZelle[] alleRasterPunkte = new GrundstuecksZelle[this.grundstueck.grundstuecksRaster.length * this.grundstueck.grundstuecksRaster[0].length];
		
		//Fuer alle Zellen im Grundstuecksraster die mehr als die minimale Flurlaenge von der Grundstuecksgrenze entfernt sind wird die Summe der Entfernungen gebildet
		//Gleichzeitig wird die Zelle zur spaeteren Sortierung in eine eindimensionales Array geschrieben, das alle Zellen des Grundstuecks haelt
		int index = 0;
		for(int i = 0; i < this.grundstueck.grundstuecksRaster.length; i++){
			for(int j = 0; j < this.grundstueck.grundstuecksRaster[0].length; j++){
				
				if(this.grundstueck.grundstuecksRaster[i][j].maxLangeX >= this.minimaleFlurlaenge && this.grundstueck.grundstuecksRaster[i][j].maxLangeX_ >= this.minimaleFlurlaenge)
					this.grundstueck.grundstuecksRaster[i][j].maxX = this.grundstueck.grundstuecksRaster[i][j].maxLangeX + this.grundstueck.grundstuecksRaster[i][j].maxLangeX_;
				if(this.grundstueck.grundstuecksRaster[i][j].maxLangeY >= this.minimaleFlurlaenge && this.grundstueck.grundstuecksRaster[i][j].maxLangeY_ >= this.minimaleFlurlaenge)
					this.grundstueck.grundstuecksRaster[i][j].maxY = this.grundstueck.grundstuecksRaster[i][j].maxLangeY + this.grundstueck.grundstuecksRaster[i][j].maxLangeY_;
				
				this.grundstueck.grundstuecksRaster[i][j].max = this.grundstueck.grundstuecksRaster[i][j].maxX + this.grundstueck.grundstuecksRaster[i][j].maxY;
				
				alleRasterPunkte[index] = this.grundstueck.grundstuecksRaster[i][j];
				index++;
			}
		}
		
		//Sortierung der absteigend nach der Variablen max , die die Summe der moeglichen Entfernungen in alle Richtungen beinhaltet
		this.sort(alleRasterPunkte);
		
		
		
		//Bestimmung wieviele Zellen mit gleichen mx-Wert vorhanden sind
		int temp = 0;
		
		for(int i = 0; i < alleRasterPunkte.length; i++){
			if(i < alleRasterPunkte.length - 1){
				if(alleRasterPunkte[i + 1].max < alleRasterPunkte[i].max ){
					temp = i;
					break;
				}
			}
		}

		//Zufaellige Auswahl der optimalen Zelle, wenn mehrere Zellen mit gleichem max-Wert
		int randomCenter = (int) (Math.random() * (temp + 1));
		
		//Kennzeichenen der Zelle als optimale Zelle des Grundstuecks
		alleRasterPunkte[randomCenter].maxPunkt = true;
		
		//Rueckgabe der optimalen Zelle
		return alleRasterPunkte[randomCenter];
		
	}
	
	/**
	 * Methode sortiert die Rasterzellen des Grundstuecks nach der maximal moeglichen Ausdehnung in X und Y/Z - Richtung
	 * @param alleRasterPunkte Array, welches alle Rasterzellen des Grundstuecks enthaelt
	 */
	private void sort (GrundstuecksZelle[] alleRasterPunkte){
		
		int i = 1;
		while ( i < alleRasterPunkte.length ) {
		    int j = alleRasterPunkte.length - 1;
		    while ( j >= i ) {
		    	if ( alleRasterPunkte[j].max > alleRasterPunkte[j-1].max ) {
		    		GrundstuecksZelle temp = alleRasterPunkte[j];
		    		alleRasterPunkte[j] = alleRasterPunkte[j-1];
		    		alleRasterPunkte[j-1] = temp;
		    	}//if
		    	j = j-1;
		    }//while
		    i = i+1;
		}//while
	}//private void sort
	
}	

