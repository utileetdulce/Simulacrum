
package architektur.input;

import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;

/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh, Matrikelnummer: 11056380, groh.markus@arcor.de
 * @version 12.06.2009
 *  Klasse beschreibt eine Zelle des Grundstueckrasters
 */
@SuppressWarnings("serial")
public class GrundstuecksZelle extends Box{
	
	/**
	 * Ob Zelle zur makierten Grundstuecksgrenze gehoert
	 */
	public boolean gehoertZurGrundstuecksgrenze;
	
	/**
	 * Ob Zelle zum "bebaubaren" Teil des Grundstuecks gehoert
	 */
	public boolean gehoertZumGrundstueck;
	
	/**
	 * Enthaelt den Abstand der Rasterzelle zur Grundstuecksgrenze in positiver X-Richtung 
	 * Auf dieser Laenge ist der Fluegel/Flur in seiner vollen Breite plazierbar
	 */
	public int maxLangeX;
	
	/**
	 * Enthaelt den Abstand der Rasterzelle zur Grundstuecksgrenze in positiver Z/Y-Richtung 
	 * Auf dieser Laenge ist der Fluegel/Flur in seiner vollen Breite plazierbar
	 */
	public int maxLangeY; 
	
	/**
	 * Enthaelt den Abstand der Rasterzelle zur Grundstuecksgrenze in negativer X-Richtung 
	 * Auf dieser Laenge ist der Fluegel/Flur in seiner vollen Breite plazierbar
	 */
	public int maxLangeX_; 
	
	
	/**
	 * Enthaelt den Abstand der Rasterzelle zur Grundstuecksgrenze in negativer Z/Y-Richtung 
	 * Auf dieser Laenge ist der Fluegel/Flur in seiner vollen Breite plazierbar
	 */public int maxLangeY_; 
	
	/**
	 * Summe des Abstandes zur Grundstuecksgrenze in X-Richtung (X + X_) und Z/Y-Richtung (Y + Y_)
	 */
	public int maxX, maxY;
	
	
	/**
	 * Summe des Abstandes in alle Richtungen
	 * maxX + maxY
	 */
	public int max;
	
	/**
	 * Ob diese Zelle die beste Position im Grundstuecksraster darstellt
	 */
	public boolean maxPunkt;
	
	
	/**
	 * Konstruktor
	 */
	public GrundstuecksZelle(float rasterbreite){
		//Erzeugen der Zelle zur Ausgabe (Rastergroesse in X/Z = 5m)
		super("Parzelle", new Vector3f(0, 0, 0), new Vector3f(rasterbreite, 0.1f, rasterbreite));
		
		this.gehoertZumGrundstueck = false;
		this.gehoertZurGrundstuecksgrenze = false;
		
		this.setModelBound(new BoundingBox());
		this.updateModelBound();
	}
	
}
