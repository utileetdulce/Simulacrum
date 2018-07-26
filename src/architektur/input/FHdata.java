package architektur.input;
/**
* FHdata
* Klasse haelt saemtliche Parameter die zur Erstellung des Modells notwendig sind.
* @author Fabian Pflaum
* @version 20.06.2009
*/


public class FHdata {

	/** studentenanzahl: Anzahl der Studenten*/
	public int studentenanzahl;
	
	/** wc_student_faktor: Faktor Toiletten pro Student*/
	public int wc_student_faktor;
	
	/** hšrsaal_student_faktor: Faktor Hoersaeaele pro Student*/
	public int hoersaal_student_faktor;
	
	/** seminarraum_student_faktor: Faktor Seminarraeume pro Student*/
	public int seminarraum_student_faktor;
	
	/** buero_student_faktor: Faktor Bueros pro Student*/
	public int buero_student_faktor;
	
	/** wc_fitness_vert: Bewertung der Position einer Toilette in vertikaler Richtung hinsichtlich Zentralitaet*/
	public float wc_fitness_vert;
	
	/** wc_fitness_horz: Bewertung der Position einer Toilette in horizontaler (Stockwerk) Richtung hinsichtlich Zentralitaet*/
	public float wc_fitness_horz;
	
	/** seminarraum_fitness_vert: Bewertung der Position eines Seminarraumes in vertikaler Richtung hinsichtlich Zentralitaet*/
	public float seminarraum_fitness_vert;
	
	/** wc_fitness_horz: Bewertung der Position eines Seminarraumes in horizontaler Richtung (Stockwerk) hinsichtlich Zentralitaet*/
	public float seminarraum_fitness_horz;
	
	/** buero_fitness_vert: Bewertung der Position eines Bueros in vertikaler Richtung hinsichtlich Zentralitaet*/
	public float buero_fitness_vert;
	
	/** buero_fitness_vert: Bewertung der Position eines Bueros in vertikaler Richtung hinsichtlich Zentralitaet*/
	public float buero_fitness_horz;
	
	/** hoersaal_fitness_vert: Bewertung der Position eines Hoersaales in vertikaler Richtung hinsichtlich Zentralitaet*/
	public float hoersaal_fitness_vert;
	
	/** hoersaal_fitness_vert: Bewertung der Position eines Hoersaales in horizontaler Richtung hinsichtlich Zentralitaet*/
	public float hoersaal_fitness_horz;
	
	/**  raumtiefe: Haelt die "Breite" eines Raumes vom Flur bis zur Gebaeudeaussenwand*/
	public float raumtiefe;
	
	/** hoersaal_grundflaeche: Haelt die Grundflaeche an benoetigten Hoersaeaelen insgesamt*/
	public int hoersaal_grundflaeche;
	
	/** wc_grundflaeche: Haelt die Grundflaeche an benoetigten Toilettenraum insgesamt*/
	public int wc_grundflaeche;
	
	/** buero_grundflaeche: Haelt die Grundflaeche an benoetigten Bueros insgesamt*/
	public int buero_grundflaeche;
	
	/** seminar_grundflaeche: Haelt die Grundflaeche an benoetigten Seminarraeumen*/
	public int seminar_grundflaeche;
	
	/** flurbreite: Haelt die Breite der Flure*/
	public int flurbreite;
	
	/** flurbreite: Haelt die Breite der Flure*/
	public int stockwerkHoehe;
	
	/** maximaleFlurlaenge: Haelt die maximale Laenge der Flure*/
	 public int maximaleFlurlaenge;
	 
	 /** minimaleFlurlaenge: Haelt die minimale Laenge der Flure*/
	 public int minimaleFlurlaenge;
}
