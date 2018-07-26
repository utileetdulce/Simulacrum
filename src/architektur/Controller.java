package architektur;


import architektur.input.FHdata;
import architektur.input.Flaecheneditor;
import architektur.input.Grundstueck;
import architektur.input.GrundstuecksZelle;
import architektur.input.Input;
import architektur.model.Anordner;
import architektur.model.Cocktail;
import architektur.model.Position;
import architektur.model.gebaude.Gebaeude;
import architektur.model.gebaude.Raum;
import architektur.viewer.Viewer;

import com.jme.scene.Node;

/**
 * Die Klasse Controller steuert den Programmablauf und bildet das Bindeglied zwischen Input, Modell und Viewer. 
 *  
 * @author Philipp Niederlag 
 * 		   Stud.Nr.: 11057798
 * 		   Gruppennummer 4
 * 		   SS 09 
 * 		   Email: wp-poduction@web.de
 * 
 * @version 1.3
 */

public class Controller {
	
	public Node mainNode = new Node();
	public Gebaeude FH;
	public Raum[] cocktail;
	

	public void startApp(){
		
		// Flächeneditor erzeugen und starten
		Flaecheneditor fe = new Flaecheneditor();
		
		// hier stoppt das Programm, solange bis im Flaecheneditor auf Start gedrückt wird
		while(fe.starteVisualisierung == false){
			// tue nichts
		}
			
		//input
		Input testInput = new Input();
		FHdata meineDaten = testInput.readdata();
		
		//Grundstueck erstellen
		Grundstueck grundstueck = new Grundstueck(fe); 
	    grundstueck.grundstueckBestimmen(); 

	    //Optimale Position des Gebaeudes bestimmen
	    Position position = new Position(grundstueck, meineDaten);
	    GrundstuecksZelle best = position.optimalePositionBestimmen();
	    		
		//Cocktail erstellen
		Cocktail cocktailMaker = new Cocktail();
		cocktail = cocktailMaker.makeCocktail(meineDaten);
		
		//Raeume des Cocktail anordnen und FH erstellen
		Anordner anordner = new Anordner(meineDaten, cocktail, best);
		this.FH = anordner.anordnen();
		
		//Darstellen
		Viewer viewer = new Viewer();
		viewer.main(this.FH, cocktail, grundstueck);
		
	}	
}
