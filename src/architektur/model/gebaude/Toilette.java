package architektur.model.gebaude;

import architektur.input.FHdata;

/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh, Matrikelnummer: 11056380, groh.markus@arcor.de
 * @version 25.05.2009
 * Klasse beschreibt Toilette 
 */
@SuppressWarnings("serial")
public class Toilette extends Raum{	
	
	/**
	 * Konstruktor, dem FHdata daten übergeben wird
	 * @param daten Daten der FH
	 */
	public Toilette(FHdata daten){
		//Erstellen der Toilette mit Groesse zur Ausgabe
		super("Toilette", daten.wc_grundflaeche / daten.raumtiefe, daten.stockwerkHoehe, daten.raumtiefe);//******** MOD
		
		//Daten der Toilette werden aus den FH Daten zugewiesen
		super.fitness_faktor_horiz 	= daten.wc_fitness_horz;
		super.fitness_faktor_vert 	= daten.wc_fitness_vert;
		super.setFlaeche(daten.wc_grundflaeche);
		super.setTiefe(daten.raumtiefe);
		super.setLaenge(daten.wc_grundflaeche/daten.raumtiefe);
		super.setHoehe(daten.stockwerkHoehe);
	}

}
