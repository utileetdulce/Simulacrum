
package architektur.input;

import java.awt.Point;
import com.jme.scene.Node;

/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh, Matrikelnummer: 11056380, groh.markus@arcor.de
 * @version 10.06.2009
 * Klasse beschreibt das Raster, welches das Grundstuecks reprasentiert.<br>
 * Methoden der Klasse bestimmen die Geraden zwischen den Eckpunkten und die dazugehoerigen Zellen des Grundstuecksrasters<br>
 * sowie die Zellen des Grundstuecks die innerhalb der Grenzen (Zellen, die zur Geraden zwischen den Punkten gehoeren) liegen <br>
 * und daher zum "bebauen" zugelassen sind. <br>
 * Es findet eine Zuordnung der Grundstuecksrasterfelder statt (Zelle gehoert zur Grundstuecksgrenze, Zelle gehoert zum verwendbaren Teil 
 * des Grundstuecks 
 */
public class Grundstueck {

	/**
	 * Raster, welches das Grundstueck repraesentiert
	 * Zweidimansionales Array, wird gefuellt mit Instanzen des Objektes GrundstuecksZelle
	 */
	public GrundstuecksZelle[][] grundstuecksRaster;
	
	/**
	 * Breite/Tiefe einer Rasterzelle
	 */
	public float rasterBreite = 5;
	
	/**
	 * Array enthaelt die Punkte (Koordinaten der Punkte) die als Grenzpunkte im Flaecheneditor angeklickt wurden
	 */
	public Point[] eckPunkte;
	
	/**
	 * Faktor Px entspricht Meter
	 * Mit diesem Faktor ist die Umrechnung moeglich, wieviele Meter ein Pixel entsprechen 
	 */
	public float faktorPixelZuMeter;
	
	/**
	 * Groesse des Grundstuecksrasters
	 * Damit wird die Dimension des zweidimensionales Array festgelegt, welchen das Grundstuecksraster enthaelt
	 */
	public int rasterX, rasterZ;
	
	/**
	 * Array für Steigung  der Geraden zwischen zwei aufeinanderfolgenden Punkten 
	 */
	public float[] steigung;
	
	/**
	 * Array für Achsenabschnitt der Geraden zwischen zwei aufeinanderfolgenden Punkten 
	 */
	public float[] achsenabschnitt;
	
	/**
	 * Node die das Grundstueck enthaelt
	 * Enthaelt Instanzen des Objektes GrundstuecksZelle, welches von Box angeleitet ist
	 */
	public Node nodeGrundstueck = new Node();
	
	/**
	 * Konstruktor
	 * @param fe enthaelt die Daten aus dem Gelaendeinput (Flaecheneditor)
	 */
	public Grundstueck(Flaecheneditor fe){
				
		this.eckPunkte = fe.coordsArray;
		this.faktorPixelZuMeter = fe.Faktor;
		
		//Groesse des Grundstueckrasters ergibt sich aus der Hoehe/Breite des Bildes in Metern / Rasterbreite in Meter
		this.rasterX = (int) (fe.fotobreiteMeter / this.rasterBreite);
		this.rasterZ = (int) (fe.fotohoeheMeter / this.rasterBreite);
		
		this.steigung 			= new float[this.eckPunkte.length];
		this.achsenabschnitt	= new float[eckPunkte.length];
		
		this.grundstuecksRaster = new GrundstuecksZelle[rasterX][rasterZ];
				
	}
	
	/**
	 * Methode erstellt das Grundstueck aus Rasterzellen
	 * Die Rasterzellen gehoeren entweder zum "bebaubaren" Grundstueck,
	 * zur Grundstuecksgrenze (Linie zwischen den angklickten Eckpunkten) oder zum nichtnutzbaren Grundstueck (restlicher Teil des Bildes im Flaecheneditors/Gelaendinputs)
	 */
	public void grundstueckBestimmen(){
		
		//Bestimmt aus den Koordinaten der angeklickten Punkte im Flaecheneditor die zugehoerigen Rasterzellen im Grundstuck
		this.punkteInRaster();
		
		//Grundstuecks-Array mit Objekten des Typs GrundstuecksrasterZelle belegen
		for(int y = 0; y < grundstuecksRaster[0].length; y++){
			for(int x = 0; x < grundstuecksRaster.length; x++){
				this.grundstuecksRaster[x][y] = new GrundstuecksZelle(this.rasterBreite);
				this.grundstuecksRaster[x][y].setLocalTranslation(x * 5, 0, y * 5);
			}
		}
		
		
		//Steigung zwischen der Geraden zwischen zwei aufeinanderfolgenden Eckpunkten berechnen
		//Beim zuletzt angeklickten Eckpunkt wird der erste Eckpunkt als zweiter Punkt der Geraden uebergeben
		for(int i = 0; i < this.steigung.length; i++){
			if(i == this.steigung.length - 1){
				this.steigungBerechnen(eckPunkte[i], eckPunkte[0], i);
			}
			else{
				this.steigungBerechnen(eckPunkte[i], eckPunkte[i + 1], i);
			}			
		}//for
		
		//Achsenabschnitt zwischen der Geraden zwischen zwei aufeinanderfolgenden Eckpunkten berechnen
		//Beim zuletzt angeklickten Eckpunkt wird der erste Eckpunkt als zweiter Punkt der Geraden uebergeben
		for(int i = 0; i < this.achsenabschnitt.length; i++){
			if(i == this.achsenabschnitt.length - 1){
				this.achsenabschnittBerechnen(this.steigung[i], this.eckPunkte[i], this.eckPunkte[0], i);
			}
			else{
				this.achsenabschnittBerechnen(this.steigung[i], this.eckPunkte[i], this.eckPunkte[i + 1], i);
			}		
		}//for
		
		//Linienpunkte festlegen
		//Bestimmung der Rasterzellen die zur Geraden zwischen zwei Punkten gehoeren
		//Beim zuletzt angeklickten Eckpunkt wird der erste Eckpunkt als zweiter Punkt der Geraden uebergeben
		for(int i = 0; i < this.eckPunkte.length; i++){
						
			if(i == this.eckPunkte.length - 1){
				this.setzeLinienPunkte(this.eckPunkte[i], this.eckPunkte[0], this.steigung[i], this.achsenabschnitt[i]);
			}
			else{
				this.setzeLinienPunkte(this.eckPunkte[i], this.eckPunkte[i + 1], this.steigung[i], this.achsenabschnitt[i]);
			}		
		}//for
		
		//Bestimmung der Rasterzellen des Grundstuecks, die zum "bebaubaren" Grundstuecksteil gehoeren
		this.definierePunkteInnerhalb();
		
		//Objekten des Typs GrundstuecksrasterZelle in NodeGrundstueck einfuegen und verschieben
		for(int y = 0; y < grundstuecksRaster[0].length; y++){
			for(int x = 0; x < grundstuecksRaster.length; x++){
				if(this.grundstuecksRaster[x][y].gehoertZumGrundstueck == true || this.grundstuecksRaster[x][y].gehoertZurGrundstuecksgrenze == true)
					this.nodeGrundstueck.attachChild(this.grundstuecksRaster[x][y]);
			}
		}
	
	}
	
	/**
	 * Methode berechnet aus den Koordinaten der im Flaecheneditor angeklickten Eckpunkte die
	 * zugehoerigen Rasterzellen im Grundstuecksarray
	 * Dazu werden die Koordinaten mit dem faktorPixelZuMeter multipliziert um die Koordinaten in Meter zu erhalten
	 * danach werden diese Meter-Koordinaten durch die Rastergroesse geteilt (5m)
	 */
	private void punkteInRaster(){
		
		for(int i = 0; i < this.eckPunkte.length; i++){
			
			this.eckPunkte[i].x = (int) (eckPunkte[i].x * this.faktorPixelZuMeter * 0.2f);
			this.eckPunkte[i].y = (int) (eckPunkte[i].y * this.faktorPixelZuMeter * 0.2f);
		}
	}
	
	/**
	 * Methode bestimmt die Grundstueckszellen, die innerhalb des "bebaubaren" Bereichs liegen
	 * Es wird von jeder Zelle in pos. X/Z und in neg. X/Z Richtung des Grundstucksarray gelaufen und ueberprueft, ob irgendwann eine
	 * Zelle gefunden wird, die zur Grenze des "bebaubaren" Grundstuecks gehoert.
	 * Bei 4 gefundenen Zellen ist die entsprechende Zelle innerhalb des angeklickten Grundstueckbereichs
	 */
	private void definierePunkteInnerhalb(){
		
		//laufe durch alle Punkte
		for(int i = 0; i < this.grundstuecksRaster.length; i++){
			for(int j = 0; j < this.grundstuecksRaster[0].length; j++){
				
				
				//Prüfe für jeden Punkt, ob in Zeile (y nach "oben" und "unten") oder Spalte (x nach "oben" und "unten") mindestens ein Grenzobjekt vorhanden ist
				//Wenn ausgehend vom Punkt in jede Richtiung eine Grenze, dann innerhalb
				int anzahlDerGefundenenGrenzen = 0;
				int xKorPunkt = i;
				int yKorPunkt = j;
				
				for(int xtemp = xKorPunkt + 1; xtemp < this.grundstuecksRaster.length; xtemp++){
					if(this.grundstuecksRaster[xtemp][yKorPunkt].gehoertZurGrundstuecksgrenze == true){
						anzahlDerGefundenenGrenzen++;
						break;
					}
				}
				for(int xtemp = xKorPunkt - 1; xtemp >= 0; xtemp--){
					if(this.grundstuecksRaster[xtemp][yKorPunkt].gehoertZurGrundstuecksgrenze == true){
						anzahlDerGefundenenGrenzen++;
						break;
					}
				}
				for(int ytemp = yKorPunkt+ 1; ytemp < this.grundstuecksRaster[0].length; ytemp++){
					if(this.grundstuecksRaster[xKorPunkt][ytemp].gehoertZurGrundstuecksgrenze == true){
						anzahlDerGefundenenGrenzen++;
						break;
					}
				}
				for(int ytemp = yKorPunkt-1; ytemp >= 0; ytemp--){
					if(this.grundstuecksRaster[xKorPunkt][ytemp].gehoertZurGrundstuecksgrenze == true){
						anzahlDerGefundenenGrenzen++;
						break;
					}
				}
								
				if(anzahlDerGefundenenGrenzen == 4)
					this.grundstuecksRaster[i][j].gehoertZumGrundstueck = true;
				
			}//for
			
		}//for
		
	}//definierePunkteInnerhalb
	

	/**
	 * Die Methode definiert die Punkte des Rasters, die die Geraden zwischen den beiden uebergebenen Eckpunkten
	 * darstellen. <br>
	 * Dazu werden entweder die X-Werte zwischen den Punkten (wenn die Gerade in X-Richtung verlaeuft) in die Geradengleichung eingesetzt
	 * oder die Y-Werte (wenn die Gerade in Y-Richtung verlaeuft) in die Geradengleichung eingesetzt. <br>
	 * Das Drehen des Koordinatensystem (Verlauf der Geraden in X/Y-Richtung) ist notwendig, um eine nichtdefinierte Steigung (z.B. beide X-Koordinaten der Punkte gleich)
	 * zu vermeiden.
	 * @param punkt1 1. Eckpunkt
	 * @param punkt2 2. Eckpunkt
	 * @param steigung Steigung zwischen den beiden Eckpunkten
	 * @param achsenabschnitt Achsenabschnitt zwischen den beiden Eckpunkten
	 */
	private void setzeLinienPunkte(Point punkt1, Point punkt2, float steigung, float achsenabschnitt){
		
		boolean koordinatensystemdrehen;
		
		int xDif = Math.abs(punkt2.x - punkt1.x);
		int yDif = Math.abs(punkt2.y - punkt1.y);
		
		if(xDif >= yDif) koordinatensystemdrehen = false;
		else koordinatensystemdrehen = true;
		
		
		if(koordinatensystemdrehen == false && punkt1.x < punkt2.x){
			
			for(int x = (int) punkt1.x; x <= punkt2.x; x++){
				int y = Math.abs(Math.round(steigung * x + achsenabschnitt));
				this.grundstuecksRaster[x][y].gehoertZurGrundstuecksgrenze = true;
				this.grundstuecksRaster[x][y].gehoertZumGrundstueck = false;	
			
			}//for			
		}//if
		
		if(koordinatensystemdrehen == false && punkt1.x > punkt2.x){
			
			for(int x = (int) punkt2.x; x <= punkt1.x; x++){
				int y = Math.abs(Math.round(steigung * x + achsenabschnitt));
				this.grundstuecksRaster[x][y].gehoertZurGrundstuecksgrenze = true;	
				this.grundstuecksRaster[x][y].gehoertZumGrundstueck = false;	
			}//for			
		}//if
		
		if(koordinatensystemdrehen == true && punkt1.y < punkt2.y){
			
			for(int y = (int) punkt1.y; y <= punkt2.y; y++){
				int x = (Math.round(steigung * y + achsenabschnitt));
				this.grundstuecksRaster[x][y].gehoertZurGrundstuecksgrenze = true;
				this.grundstuecksRaster[x][y].gehoertZumGrundstueck = false;	
			}//for 			
		}//if
		
		if(koordinatensystemdrehen == true && punkt1.y > punkt2.y){
			
			for(int y = (int) punkt2.y; y <= (punkt1.y); y++){
				int x = Math.round(steigung * y + achsenabschnitt);
				this.grundstuecksRaster[x][y].gehoertZurGrundstuecksgrenze = true;
				this.grundstuecksRaster[x][y].gehoertZumGrundstueck = false;	
				
			}//for 			
		}//if		
	}
	
	/**
	 * Methode berechnet den Achsenabschnitt der Geraden zwischen zwei aufeinanderfolgenden Eckpunkten des Grundstuecks 
	 * @param steigung Steigung der Geraden zwischen den beiden Eckpunkten.
	 * @param punkt1 1. Eckpunkt
	 * @param punkt2 2. Eckpunkt
	 * @param i Zaehler fuer den Eintrag des Achsenabschnitts im Array der Achsenabschnitte
	 */
	private void achsenabschnittBerechnen(float steigung, Point punkt1, Point punkt2, int i){
		
		boolean koordinatensystemdrehen;
		
		//Berechnung der Differenz zwischen den X und Y Koordinaten der beiden Punkte
		int xDif = Math.abs(punkt2.x - punkt1.x);
		int yDif = Math.abs(punkt2.y - punkt1.y);
		
		//Das Koordinatensystem wird gedreht, wenn die groesste Ausdehnung der Geraden in Y-Richtung ist
		if(xDif >= yDif) koordinatensystemdrehen = false;
		else koordinatensystemdrehen = true;
		
		if(koordinatensystemdrehen == false){
			this.achsenabschnitt[i] = (punkt1.y - (steigung * punkt1.x));
		}
		else{
			this.achsenabschnitt[i] = (punkt1.x - (steigung * punkt1.y));
		}
	}
	
	
	/**
	 * Methode berechnet die Steigung der Geraden zwischen zwei aufeinanderfolgenden Eckpunkten des Grundstuecks.<br>
	 * Das Drehen des Koordinatensystem (Verlauf der Geraden in X/Y-Richtung) ist notwendig, um eine nichtdefinierte Steigung (z.B. beide X-Koordinaten der Punkte gleich)
	 * zu vermeiden.
	 * @param punkt1 1. Eckpunkt
	 * @param punkt2 2. Eckpunkt
	 * @param i Zaehler fuer den Eintrag der Steigung im Array der Steigungen 
	 */
	private void steigungBerechnen(Point punkt1, Point punkt2, int i){
		
		boolean koordinatensystemdrehen;
		
		//Berechnung der Differenz zwischen den X und Y Koordinaten der beiden Punkte
		int xDif = Math.abs(punkt2.x - punkt1.x);
		int yDif = Math.abs(punkt2.y - punkt1.y);
		
		//Das Koordinatensystem wird gedreht, wenn die groesste Ausdehnung der Geraden in Y-Richtung ist
		if(xDif >= yDif) koordinatensystemdrehen = false;
		else koordinatensystemdrehen = true;
		
		if(koordinatensystemdrehen == false){
			this.steigung[i] = (float)(punkt1.y - punkt2.y) / (punkt1.x - punkt2.x);
		}
		else{
			this.steigung[i] = ((float)(punkt1.x - punkt2.x) / (punkt1.y - punkt2.y));
		}
	}

}

