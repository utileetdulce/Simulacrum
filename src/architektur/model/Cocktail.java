package architektur.model;

import architektur.input.FHdata;
import architektur.model.gebaude.Buero;
import architektur.model.gebaude.Hoersaal;
import architektur.model.gebaude.Raum;
import architektur.model.gebaude.Seminarraum;
import architektur.model.gebaude.Toilette;

/**
 * FH Köln IMP - Computeranimation SS 09 - Projekt
 * @author Markus Groh, Matrikelnummer: 11056380, groh.markus@arcor.de
 * @version 25.05.2009
 * 
 * Klasse enthaelt Methode, die den Cocktail an verschiedenen Raumarten erstellt.
 * Die Anzahl der verschiedenen Raeumen ist abhaenging vom Faktor fuer jeden Raumtyp 
 */
public class Cocktail {
	
	
	/**
	 * Methode erstellt den Cocktail an Räumen, die für die FH benötigt wird
	 * @param daten Daten die zur Erstellung des Cocktial nötig sind
	 * @return Array mit den verschieden Raeumen aus denen die FH besteht
	 */
	public Raum[] makeCocktail(FHdata daten){
		
		//Berechnung der Zaehler in anhaengigkeit der Studentenanzahl und des Faktors
		int zaehlerToilette 	= (daten.studentenanzahl / daten.wc_student_faktor) + 1;
		int zaehlerHoersaal		= (daten.studentenanzahl / daten.hoersaal_student_faktor) + 1;
		int zaehlerSeminarraum 	= (daten.studentenanzahl / daten.seminarraum_student_faktor) + 1;
		int zaehlerBuero	 	= (daten.studentenanzahl / daten.buero_student_faktor) + 1;
		
		int anzahlRaeume		= zaehlerBuero + zaehlerSeminarraum + zaehlerHoersaal + zaehlerToilette;
		
		
		Raum[] cocktail = new Raum[anzahlRaeume];
		
		int i = 0;
		
		for(int j = 0; j < zaehlerToilette; j++){
			cocktail[i] = new Toilette(daten);
			i++;
		}
		
		for(int j = 0; j < zaehlerHoersaal; j++){
			cocktail[i] = new Hoersaal(daten);
			i++;
		}
		
		for(int j = 0; j < zaehlerSeminarraum; j++){
			cocktail[i] = new Seminarraum(daten);
			i++;
		}
		
		for(int j = 0; j < zaehlerBuero; j++){
			cocktail[i] = new Buero(daten);
			i++;
		}
		
		return cocktail;
	}

}
