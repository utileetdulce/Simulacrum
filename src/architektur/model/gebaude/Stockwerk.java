package architektur.model.gebaude;

import architektur.input.FHdata;

import com.jme.scene.shape.Box;
import java.util.ArrayList;

import com.jme.math.FastMath;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.scene.Node;

/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh, Matrikelnummer: 11056380, groh.markus@arcor.de
 * @version 25.05.2009
 * Klasse beschreibt Stockwerk
 * Stellt Methoden zur erstellung zur Verfuegung
 * Abgeleitet von Box um das Treppenhaus eines Stockwerkes darstellen zu koennen
 */
@SuppressWarnings("serial")
public class Stockwerk extends Box{
	
	/**
	 * Zaehler fuer Anzahl der Stockwerke
	 */
	public static int gesamtzahlstockwerke = 1;
	
	/**
	 * Etagennummer des Stockwerks
	 */
	public int etage;
	
	/**
	 * Gebaude zu dem das Stockwerk gehoert
	 */
	public Gebaeude gebaeude;
	
	/**
	 * Enthaelt die Flure des STockwerks
	 */
	public ArrayList<Flur> flureImStockwerk = new ArrayList<Flur>();
	
	/**
	 * Haelt die Flaeche des Stockwerks
	 */
	public float flaecheDesStockwerks;
	
	/**
	 * ID des Stockwerks
	 */
	public int stockwerkID;
	
	/**
	 * FH Datensatz
	 */
	public FHdata meineDaten;
	
	/**
	 * Node die das gesamte Stockwerk enthaelt
	 */
	public Node nodeStockwerk = new Node();

	
	/**
	 * Konstruktor
	 * @param meineDaten Daten der FH
	 */
	public Stockwerk(FHdata meineDaten){
		//Erstellen des Treppenhauses zur Ausgabe
		super("Treppenhaus", new Vector3f(0, 0, 0), new Vector3f((meineDaten.flurbreite + 2 * meineDaten.raumtiefe), meineDaten.stockwerkHoehe, (meineDaten.flurbreite + 2 * meineDaten.raumtiefe)));
		
		this.etage = gesamtzahlstockwerke;
		gesamtzahlstockwerke++;
		
		this.meineDaten = meineDaten;			
	}
	
	
	
	/**
	 * Methode erstellt die Flure im Stockwerk
	 * und fuegt die Nodes der einzelnen Flure (enthalten Nodes fuer rechte und linke Seite des Flurs) der Node hinzu,
	 * die das gesamte Stockwerk enthaelt
	 * @param gesamtflaecheCocktail
	 * @param flaecheDesGebaeudes
	 */
	public void flureImStockwerkErstellen(int index){
		
		//Nutzbare Laenge ergibt sich aus dem Abstand der optimalen Zelle zur Grundstuecksgrenze
		//abzueglich der Laenge, die das Treppenhaus nenoetigt
		float nutzbareLangeX 	= Gebaeude.grundstuecksDiagonaleX  - 0.5f * (2 * meineDaten.raumtiefe + meineDaten.flurbreite);// Diagonale - Lange/Breite des Treppenhauses
		float nutzbareLangeZ 	= Gebaeude.grundstuecksDiagonaleZ  - 0.5f *(2 * meineDaten.raumtiefe + meineDaten.flurbreite);// Diagonale - Lange/Breite des Treppenhauses
		float nutzbareLangeX_ 	= Gebaeude.grundstuecksDiagonaleX_ - 0.5f * (2 * meineDaten.raumtiefe + meineDaten.flurbreite);// Diagonale - Lange/Breite des Treppenhauses
		float nutzbareLangeZ_ 	= Gebaeude.grundstuecksDiagonaleZ_ - 0.5f *(2 * meineDaten.raumtiefe + meineDaten.flurbreite);// Diagonale - Lange/Breite des Treppenhauses
		
		//Wenn die nutzbare Laenge die maximale Flurlaenge, die in den FH Daten gehalten wird uebersteigt
		if (nutzbareLangeX > this.meineDaten.maximaleFlurlaenge)
			nutzbareLangeX = this.meineDaten.maximaleFlurlaenge;
		if (nutzbareLangeX_ > this.meineDaten.maximaleFlurlaenge)
			nutzbareLangeX_ = this.meineDaten.maximaleFlurlaenge;
		if (nutzbareLangeZ > this.meineDaten.maximaleFlurlaenge)
			nutzbareLangeZ = this.meineDaten.maximaleFlurlaenge;
		if (nutzbareLangeZ_ > this.meineDaten.maximaleFlurlaenge)
			nutzbareLangeZ_ = this.meineDaten.maximaleFlurlaenge;

		
		//Erstellen der benoetigten Flure des Stockwerks
		//Aublauf: Flur in pos X-Richtung, Flur in neg Z-Richtung, Flur in neg X-Richtung, Flur in pos Z-Richtung
		//ausgehend vom Mittelpunkt des Gebauedes (der optimalöen Zelle)
			
		//Erstellen der Flure in X-Richtung (1. und 3. Flur)
		if(index == 0){
				
			//Hinzufügen des neuen Flurs zur ArrayList der Flure
			this.flureImStockwerk.add(new Flur(meineDaten, nutzbareLangeX, this));
		}
			
		if(index == 1){
				
			//Hinzufügen des neuen Flurs zur ArrayList der Flure
			this.flureImStockwerk.add(new Flur(meineDaten, nutzbareLangeZ_, this));
		}
			
		if(index == 2){
				
			//Hinzufügen des neuen Flurs zur ArrayList der Flure
			this.flureImStockwerk.add(new Flur(meineDaten, nutzbareLangeX_, this));
		}
			
		if(index == 3){
				
			//Hinzufügen des neuen Flurs zur ArrayList der Flure
			this.flureImStockwerk.add(new Flur(meineDaten, nutzbareLangeZ, this));
		}
			
		//Hinzufuegen des erstellen Flures (und damit seiner Nodes) der Node des Stockwerks 
		this.nodeStockwerk.attachChild(this.flureImStockwerk.get(index).NodeFlur);

	}
	
	
	
	/**
	 * Methode fuehrt die benoetigten Translationen im Stockwerk durch
	 * Die Flure werden in die entsprechende Richtung rotiert
	 * und um die halbe Laenge/Breite des Treppenhauses in die entsprechende Richtung verschoben
	 */
	public void stockwerksTranslationen(){
		
		//Das Treppenhaus wird in die Mitte der Node
		//Das Treppenhaus wird zur Ausgabe in die Node einegefuegt, die das gesamte Stockwerk enthaelt
		this.setLocalTranslation(new Vector3f(-(0.5f * meineDaten.flurbreite + meineDaten.raumtiefe), 0, -(0.5f * meineDaten.flurbreite + meineDaten.raumtiefe)));
		this.nodeStockwerk.attachChild(this);
		
		
		//Ratations Qauternions fuer die Rotation der Flure in die entsprechende Richtung
		//Flur mit der Laenge aus wird nicht rotiert, Flur mit Laenge X_ wird um 180 rotiert, usw.
		Quaternion Rotation1 = new Quaternion();
		Rotation1.fromAngles(0, FastMath.DEG_TO_RAD * 0, 0);
    	
    	Quaternion Rotation2 = new Quaternion();
		Rotation2.fromAngles(0, FastMath.DEG_TO_RAD * 90, 0);
    	
    	Quaternion Rotation3 = new Quaternion();
		Rotation3.fromAngles(0, FastMath.DEG_TO_RAD * 180, 0);
    	
    	Quaternion Rotation4 = new Quaternion();
    	Rotation4.fromAngles(0, FastMath.DEG_TO_RAD * 270, 0);
    	
    	
    	for(int i = 0; i < this.flureImStockwerk.size(); i++){
    		
    		if(i == 0){
    			this.nodeStockwerk.getChild(i).setLocalRotation(Rotation1);
    			this.nodeStockwerk.getChild(i).setLocalTranslation((0.5f * meineDaten.flurbreite + meineDaten.raumtiefe), 0, -(0.5f * meineDaten.raumtiefe));
    		}
    		if(i == 1){
    			this.nodeStockwerk.getChild(i).setLocalRotation(Rotation2);
    			this.nodeStockwerk.getChild(i).setLocalTranslation(-(0.5f * meineDaten.flurbreite) , 0, -(0.5f * meineDaten.flurbreite + meineDaten.raumtiefe));
    		}
    		if(i == 2){
    			this.nodeStockwerk.getChild(i).setLocalRotation(Rotation3);
    			this.nodeStockwerk.getChild(i).setLocalTranslation( -(0.5f * meineDaten.flurbreite + meineDaten.raumtiefe), 0, (0.5f * meineDaten.flurbreite));
    		}
    		if(i == 3){
    			this.nodeStockwerk.getChild(i).setLocalRotation(Rotation4);
    			this.nodeStockwerk.getChild(i).setLocalTranslation((0.5f * meineDaten.flurbreite), 0, (0.5f * meineDaten.flurbreite + meineDaten.raumtiefe));
    		}    		
    	}  
	}//private void stockwerksTranslationen()

	
}
