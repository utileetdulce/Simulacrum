package architektur.model.gebaude;

import architektur.input.FHdata;


/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh, Matrikelnummer: 11056380, groh.markus@arcor.de
 * @version 25.05.2009
 * Klasse beschreibt Seminarraum 
 */
@SuppressWarnings("serial")
public class Seminarraum extends Raum{
	
	/**
	 * Konstruktor, dem FHdata daten übergeben wird
	 * @param daten Daten der FH
	 */
	public Seminarraum(FHdata daten){
		//Erstellen des Seminarraums mit Groesse zur Ausgabe
		super("Seminarraum", daten.seminar_grundflaeche/daten.raumtiefe, daten.stockwerkHoehe, daten.raumtiefe);
		
		//Daten des Seminarraums werden aus den FH Daten zugewiesen
		super.fitness_faktor_horiz 	= daten.seminarraum_fitness_horz;
		super.fitness_faktor_vert 	= daten.seminarraum_fitness_vert;
		super.setFlaeche(daten.seminar_grundflaeche);
		super.setTiefe(daten.raumtiefe);
		super.setLaenge(daten.seminar_grundflaeche / daten.raumtiefe);
		super.setHoehe(daten.stockwerkHoehe);
	}
}
