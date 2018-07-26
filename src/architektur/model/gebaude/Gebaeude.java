
package architektur.model.gebaude;


import java.util.ArrayList;

import architektur.input.FHdata;
import architektur.input.GrundstuecksZelle;

import com.jme.math.Vector3f;


import com.jme.scene.Node;

/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh, Matrikelnummer: 11056380, groh.markus@arcor.de
 * @version 25.05.2009
 * Klasse beschreibt Gebauede
 * enthaelt Array fuer die einzelnen Stockwerke und dazugehoerige Nodes 
 */
public class Gebaeude {
	
	/**
	 * Enthaelt die Abstaende des Gebaeudemittelpunkts zur Grundstuecksgrenze in positiver X-Richtung 
	 * Auf dieser Laenge ist der Fluegel/Flur in seiner vollen Breite plazierbar
	 */
	public static float grundstuecksDiagonaleX;
	
	/**
	 * Enthaelt die Abstaende des Gebaeudemittelpunkts zur Grundstuecksgrenze in positiver Z-Richtung 
	 * Auf dieser Laenge ist der Fluegel/Flur in seiner vollen Breite plazierbar
	 */
	public static float grundstuecksDiagonaleZ;
	
	/**
	 * Enthaelt die Abstaende des Gebaeudemittelpunkts zur Grundstuecksgrenze in negativer X-Richtung 
	 * Auf dieser Laenge ist der Fluegel/Flur in seiner vollen Breite plazierbar
	 */
	public static float grundstuecksDiagonaleX_;
	
	/**
	 * Enthaelt die Abstaende des Gebaeudemittelpunkts zur Grundstuecksgrenze in negativer Z-Richtung 
	 * Auf dieser Laenge ist der Fluegel/Flur in seiner vollen Breite plazierbar
	 */
	public static float grundstuecksDiagonaleZ_;
	
	/**
	 * Enthaelt die Daten der FH
	 */
	public FHdata meineDaten;
	
	/**
	 * Enthalt die Stockwerke des Gebaudes
	 */
	public ArrayList<Stockwerk> stockwerkeImGebaude = new ArrayList<Stockwerk>();
	
	/**
	 * Enthalt die Nodes der enzelnen Stockwerke im Gebaude
	 */
	public Node[] NodeStockwerke;
	
	/**
	 * Node enthalt das Gebaude und damit alle Nodes untergeordneten Nodes (Stockwerk, Flur,...)
	 */
	public Node nodeGebaude = new Node();
	
	/**
	 * Mittelpunkt der optimalen Parzelle des Grundstuecks
	 */
	public Vector3f mittelpunkt;
	
	/**
	 * Optimale Parzelle fuer die Plazierung des Gebaeudes
	 */
	public GrundstuecksZelle optiZelle;
	
	/**
	 * Gesamtflaeche der Raeume im Cocktail
	 */
	public float gesamtflaecheCocktail;
	
	/**
	 * Tatsaechlich genutzte Flaeche des Gebaeudes
	 * wird nach jedem Erstellen eines Flures in einem Stockwerk und dem befuellen dieses Flures mit Raeumen aus dem Cocktail aktualisiert
	 */
	public float tatsaechlichGenutzeFlaecheGebaeude;
	
	
	
	/**
	 * Konstruktor
	 * @param meineDaten Daten der FH
	 */
	public Gebaeude (FHdata meineDaten, GrundstuecksZelle best){
		
		this.meineDaten = meineDaten;
		
		//Optimale Grundstueckszelle
		this.optiZelle = best;
		
		//Koordinaten des Mittelpunkts des Gebauedes ergeben sich aus Koordinaten der optimalen Parzelle
		this.mittelpunkt = best.getLocalTranslation();
		
		
		//Bestimmung der Abstaende des Gebaeudemittelpunkts zu den Grundstuecksgrenzen
		//X  = vom Mittelpunkt in pos. X-Richtung
		//X_ = vom Mittelpunkt in neg. X-Richtung
		//Z  = vom Mittelpunkt in pos. Z-Richtung
		//X_ = vom Mittelpunkt in neg. Z-Richtung
		//Gebaeude.grundstuecksDiagonaleX ergibt sich aus dem Abstand der optimalen Grundstueckszelle zur jeweiligen Grenze (X,X_,Z,Z_)
		//abzueglich der Ausmasse der optimalen Zelle (um Mittelpunkt zu bekommen)
		Gebaeude.grundstuecksDiagonaleX 	= this.optiZelle.maxLangeX 		- this.optiZelle.xExtent;
		Gebaeude.grundstuecksDiagonaleX_ 	= this.optiZelle.maxLangeX_ 	- this.optiZelle.xExtent;
		Gebaeude.grundstuecksDiagonaleZ 	= this.optiZelle.maxLangeY 		- this.optiZelle.xExtent;
		Gebaeude.grundstuecksDiagonaleZ_ 	= this.optiZelle.maxLangeY_ 	- this.optiZelle.xExtent;


	}
	
	
	
	/**
	 * Methode weist die Nodes der einzelnen Stockwerke dem Node-Array hinzu, das alle Stockwerke haelt.<br>
	 * Methode fuehrt eine Translation der Y-Position der einzelnen Stockwerke durch.
	 * Methode verschiebt das Gebaeude an die optimale Position
	 */
	public void stockwerkeImGebaudeDenNodesZuweisen(){
		
		//Die Node des gesamten Gebaeudes wird an die optimale Position des Grundstuecks verschoben
		this.nodeGebaude.setLocalTranslation(this.mittelpunkt.x + this.optiZelle.xExtent, 0, this.mittelpunkt.z + this.optiZelle.zExtent);
		
		//Die Groesse des Node-Array fuer alle Stcokwerke ergibt sich aus der Anzahl der Stockwerke im Gebaude
		this.NodeStockwerke = new Node[this.stockwerkeImGebaude.size()];
		
		for(int i = 0; i < this.stockwerkeImGebaude.size(); i++){
			
			//Die Nodes der Stockwerke (enthalten die Nodes der Flure) werden der Node (Array) zugewiesen,
			//die alle einzelnen Nodes der Stockwerke haelt
			this.NodeStockwerke[i] = this.stockwerkeImGebaude.get(i).nodeStockwerk;
			
			//Verschiebe die Nodes der Stockwerke
			this.NodeStockwerke[i].setLocalTranslation(0, (i * (meineDaten.stockwerkHoehe + .2f)), 0);
			
			//Weise dem Stockwerk i sein (dieses) Gebaeude zu
			this.stockwerkeImGebaude.get(i).gebaeude = this;
			
			//Weise dem aktuellen Stockwerk seine ID zu
			this.stockwerkeImGebaude.get(i).stockwerkID = i;
			
			//Fuegt die aktuelle Node des Stockwerks der Gebaeuede Node hinzu
			this.nodeGebaude.attachChild(this.NodeStockwerke[i]);
		}
		
	}
	
	
}
