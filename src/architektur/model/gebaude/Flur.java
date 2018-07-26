
package architektur.model.gebaude;
import java.util.*;




import architektur.input.FHdata;

import com.jme.math.Vector3f;
import com.jme.scene.Node;
import com.jme.scene.shape.Box;

/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh, Matrikelnummer: 11056380, groh.markus@arcor.de
 * @version 25.05.2009
 * Klasse beschreibt Flur
 * enthaelt Array fuer die Flurseiten "Rechts" und "Links" sowie die dazugehoerigen Nodes 
 */
@SuppressWarnings("serial")
public class Flur extends Box{
	
	/**
	 * Zaehler zur Identifikation des Flures
	 */
	public static int gesamtzahlFlure;
	
	/**
	 * Identifiaktionsnummer des Flures
	 */
	public int identNrFlur;
	public int flurID;
	
	/**
	 * Stockwerk zu dem der Flur gehört
	 */
	public Stockwerk etage; 
	
	/**
	 * Daten der FH
	 */
	public FHdata meineDaten;
	
	/**
	 * Node die den gesamten Flur enthaelt
	 */
	public Node NodeFlur = new Node();
	
	/**
	 * Node fuer linke Flurseite
	 */
	public Node NodeRaumeLinks = new Node();
	
	/**
	 * Node fuer rechte Flurseite
	 */
	public Node NodeRaumeRechts = new Node();
	
	/**
	 * Laenge des Flures
	 */
	public float laenge;
	
	/**
	 * Aktuelle Restlaenge des linken Flures
	 * wird nach jedem hinzufuegen eines Raumes aktualisiert
	 */
	public float restLangeFlurLinks;
	
	/**
	 * Aktuelle Restlaenge des rechten Flures
	 * wird nach jedem hinzufuegen eines Raumes aktualisiert
	 */
	public float restLangeFlurRechts;
	
	/**
	 * Aktuelle Laenge der angeordneten Raeume linke Flurseite
	 * wird nach jedem hinzufuegen eines Raumes aktualisiert
	 */
	public float langeDerAngeordnetenRaumeLinks;
	
	/**
	 * Aktuelle Laenge der angeordneten Raeume rechte Flurseite
	 * wird nach jedem hinzufuegen eines Raumes aktualisiert
	 */
	public float langeDerAngeordnetenRaumeRechts;
	
	/**
	 * ArrayList für die Raume der linken Seite des Flurs
	 */
	public ArrayList <Raum>raumeLinks = new ArrayList<Raum>();
	
	/**
	 * ArrayList für die Raume der rechten Seite des Flurs
	 */
	public ArrayList <Raum>raumeRechts = new ArrayList<Raum>();
	
	/**
	 * Enthaelt die ArrayLists der linken und rechten Flurseite
	 */
	@SuppressWarnings("unchecked")
	public ArrayList[] flurseite = new ArrayList[2];
	
		
	
	/**
	 * Konstruktor
	 * initialisiert Variablen, Translation der Flure, Zuweisung zur Node
	 * @param etage - Etage die der Flur zugehoerig ist
	 * @param meineDaten - Daten der FH
	 */
	public Flur(FHdata meineDaten, float nutzbareLangeEinesFlures, Stockwerk etage){
		//Ersetllung des Flures zur Ausgabe
		super("Flur", new Vector3f(0, 0, 0), new Vector3f(nutzbareLangeEinesFlures, meineDaten.stockwerkHoehe, meineDaten.flurbreite));
		
		identNrFlur = gesamtzahlFlure;
		gesamtzahlFlure++;
		
		this.flurseite[0]=raumeLinks;
		this.flurseite[1]=raumeRechts;
		
		this.meineDaten = meineDaten;
		this.etage = etage;
		
		//Die Laenge der angeordneten Raume zu Beginn der Anordnung = 0
		this.langeDerAngeordnetenRaumeLinks  = 0;
		this.langeDerAngeordnetenRaumeRechts = 0;
		
		//Restlange ergibt sich zu Beginn aus der nutzbaren Lange fuer jeden Flur
		//Diese Laenge wird ggf. vor der Uebergabe aus Gebaeude an die maximale Flurlaenge angepasst
		this.restLangeFlurLinks  = nutzbareLangeEinesFlures;
		this.restLangeFlurRechts = nutzbareLangeEinesFlures;
		
		//Zuwesiung in die Nodes
		this.NodeFlur.attachChild(this);
		this.NodeFlur.attachChild(this.NodeRaumeLinks);
		this.NodeFlur.attachChild(this.NodeRaumeRechts);
	
		//Laenge des Flures
		this.laenge = nutzbareLangeEinesFlures;
		
		//Methode zur Translation der Nodes
		this.flurTranslationen();
	}
	
	
	/**
	 * Methode führt die Translation der beiden Flurseiten aus
	 */
	private void flurTranslationen(){
		
		//Die beiden Flure werden in Z-Ebene um die Flurbreite voneinander verschoben
		this.NodeRaumeLinks.setLocalTranslation(0, 0, (meineDaten.flurbreite));
		this.NodeRaumeRechts.setLocalTranslation(0, 0, -(meineDaten.flurbreite));		
	}
	

	
	 /**
	  * @return the laenge
	  */
	 public float getLaenge() {
	  return laenge;
	 }
	
	/**
	 * @return the restLangeFlurLinks
	 */
	public float getRestLangeFlurLinks() {
		return restLangeFlurLinks;
	}

	/**
	 * @param restLangeFlurLinks the restLangeFlurLinks to set
	 */
	public void setRestLangeFlurLinks(int restLangeFlurLinks) {
		if(restLangeFlurLinks < 0)
			this.restLangeFlurLinks = 0;
		else
			this.restLangeFlurLinks = restLangeFlurLinks;
	}

	/**
	 * @return the restLangeFlurRechts
	 */
	public float getRestLangeFlurRechts() {
		return restLangeFlurRechts;
	}

	/**
	 * @param restLangeFlurRechts the restLangeFlurRechts to set
	 */
	public void setRestLangeFlurRechts(float restLangeFlurRechts) {
		
		if(restLangeFlurRechts < 0)
			this.restLangeFlurLinks = 0;
		else
			this.restLangeFlurRechts = restLangeFlurRechts;
	}

	/**
	 * @return the langeDerAngeordnetenRaumeLinks
	 */
	public float getLangeDerAngeordnetenRaumeLinks() {
		return langeDerAngeordnetenRaumeLinks;
	}

	/**
	 * @param langeDerAngeordnetenRaumeLinks the langeDerAngeordnetenRaumeLinks to set
	 */
	public void setLangeDerAngeordnetenRaumeLinks(int langeDerAngeordnetenRaumeLinks) {
		this.langeDerAngeordnetenRaumeLinks = langeDerAngeordnetenRaumeLinks;
	}

	/**
	 * @return the langeDerAngeordnetenRaumeRechts
	 */
	public float getLangeDerAngeordnetenRaumeRechts() {
		return langeDerAngeordnetenRaumeRechts;
	}

	/**
	 * @param langeDerAngeordnetenRaumeRechts the langeDerAngeordnetenRaumeRechts to set
	 */
	public void setLangeDerAngeordnetenRaumeRechts(
			int langeDerAngeordnetenRaumeRechts) {
		this.langeDerAngeordnetenRaumeRechts = langeDerAngeordnetenRaumeRechts;
	}
	
	
	
}
