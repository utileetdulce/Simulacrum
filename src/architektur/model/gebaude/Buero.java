
package architektur.model.gebaude;

import architektur.input.FHdata;


/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh, Matrikelnummer: 11056380, groh.markus@arcor.de
 * @version 25.05.2009
 * Klasse beschreibt Büro 
 */
@SuppressWarnings("serial")
public class Buero extends Raum{
	
	/**
	 * Konstruktor, dem FHdata daten übergeben wird
	 * @param daten Daten der FH
	 */
	public Buero(FHdata daten){
		//Erstellen des Bueros mit Groesse zur Ausgabe
		super("Büro", daten.buero_grundflaeche/daten.raumtiefe,daten.stockwerkHoehe, daten.raumtiefe);
		
		//Daten des Bueros werden aus den FH Daten zugewiesen
		super.setFlaeche(daten.buero_grundflaeche);
		super.fitness_faktor_horiz 	= daten.buero_fitness_horz;
		super.fitness_faktor_vert 	= daten.buero_fitness_vert;
		super.setTiefe(daten.raumtiefe);
		super.setLaenge(daten.buero_grundflaeche/daten.raumtiefe);
		super.setHoehe(daten.stockwerkHoehe);

	}
	
}
