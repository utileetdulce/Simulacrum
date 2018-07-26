package architektur.model.gebaude;

import architektur.input.FHdata;

/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh, Matrikelnummer: 11056380, groh.markus@arcor.de
 * @version 25.05.2009
 * Klasse beschreibt Hörsaal 
 */
@SuppressWarnings("serial")
public class Hoersaal extends Raum{
	
	/**
	 * Konstruktor, dem FHdata daten übergeben wird
	 * @param daten Daten der FH
	 */
	public Hoersaal(FHdata daten){
		//Erstellen des Hoersaals mit Groesse zur Ausgabe
		super("Hoersaal", daten.hoersaal_grundflaeche/daten.raumtiefe, daten.stockwerkHoehe, daten.raumtiefe);
		
		//Daten des Hoersaals werden aus den FH Daten zugewiesen
		super.setFlaeche(daten.hoersaal_grundflaeche);
		super.fitness_faktor_horiz 	= daten.hoersaal_fitness_horz;
		super.fitness_faktor_vert 	= daten.hoersaal_fitness_vert;
		super.setTiefe(daten.raumtiefe);
		super.setLaenge(daten.hoersaal_grundflaeche/daten.raumtiefe);
		super.setHoehe(daten.stockwerkHoehe);
	}

}
