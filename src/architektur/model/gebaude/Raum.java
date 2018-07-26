package architektur.model.gebaude;




import com.jme.bounding.BoundingBox;
import com.jme.math.Vector3f;
import com.jme.scene.shape.Box;


/**
 * @author Thomas Retzkowski, 
 * 		   Stud.Nr.: 11057796
 * 		   SS 09 
 * 		   Email: thomas.retzkowski@smail.fh-koeln.de
 * 
 * @version 1.3
 */
@SuppressWarnings("serial")
public class Raum extends Box {


	//Zähler für eindeutige Raumnummer
	public static int zahlerRaumnummer = 1;
	
	/** @param raumnummer - Fortlaufende Raumnummer */
	private int raumnummer;
	
	/** @param flur der Flur, an dem der Raum angeordnet ist */
	private Flur flur;
	
	/** @param flurSeite des Flurs, an dem der Raum angeordnet ist, mit 1=links und 2=rechts */
	private int flurSeite;
	
	/** @param flurID der Arrayindex des Flures, an dem der Raum angeordnet ist */
	private int raumID;
	
	/** @param tiefe die Tiefe des Raumes */
	private float flaeche;
	
	/** @param tiefe die Tiefe des Raumes */
	private float tiefe;
		
	/** @param laenge die Laenge des Raumes */
	private float laenge = this.flaeche / this.tiefe;	
	
	/** @param hoehe die Hoehe des Raumes */
	private int hoehe;	
	
	/** @param position Array mit X/Y/Z - Position des Raums */
	private float[] position = new float[3];
	
	/** @param fitness_vert die vertikale Fitness des Modells */
	public float fitness_faktor_vert;	
	
	/** @param fitness_horiz die horizontale Fitness des Modells */
	public float fitness_faktor_horiz;	
	
	public float fitness_horiz = 0;
	
	public float fitness_vert = 0;
	  
	/** @param fitness_gesamt die Gesamtfitness des Modells */
	public float fitness_gesamt;
		
	/** @param nachbar_links der linke Nachbar des Raumes */
	private Raum nachbar_links;
		
	/** @param nachbar_rechts der rechte Nachbar des Raumes */
	private int nachbarRechtsNummer;	
	
	/** @param nachbar_links der linke Nachbar des Raumes */
	private int nachbarLinksNummer;
		
	/** @param nachbar_rechts der rechte Nachbar des Raumes */
	private Raum nachbar_rechts;	
	
	
	/** @param angeordnet Der Parameter gibt an, ob dieser Raum bereits angeordnet wurde */
	private boolean angeordnet;
	
	
	
	
	/**
	 * default-Konstruktor
	 */
	public Raum(){
		super("Raum", new Vector3f(0,0,0), new Vector3f(1f,1f,1f));
		angeordnet=false;
		this.raumnummer = zahlerRaumnummer;
		zahlerRaumnummer++;
	}
	
	public Raum(float laenge, float hoehe, float tiefe){//******** MOD
		super("Raum", new Vector3f(0,0,0), new Vector3f(laenge, hoehe, tiefe));//******** MOD
		this.setModelBound(new BoundingBox());
		this.updateModelBound();
		angeordnet=false;
		this.raumnummer = zahlerRaumnummer;
		zahlerRaumnummer++;
	}
	
	public Raum(String name, float laenge, float hoehe, float tiefe){//******** MOD
		super(name, new Vector3f(0,0,0), new Vector3f(laenge, hoehe, tiefe));//******** MOD
		this.setModelBound(new BoundingBox());
		this.updateModelBound();
		angeordnet=false;
		this.raumnummer = zahlerRaumnummer;
		zahlerRaumnummer++;
	}
	
	public void fitness_berechnen(){
		  float fitness_horiz = this.getLocalTranslation().x * fitness_faktor_horiz;
		  float fitness_vert = this.flur.etage.etage * fitness_faktor_vert *1.6f;
		  fitness_gesamt = fitness_vert+fitness_horiz;
		  //Fitness enorm verschlechtern, falls der raum über die flurgrenzen kommt
		  if((this.getLocalTranslation().x + this.getLaenge()-5) > this.getFlur().getLaenge()){
		   fitness_gesamt = -50000;
		   if(this.getFlur().etage.etage==4){
			  }
		  }
		 }
	
	
//	public void fitness_berechnen(){
//		  float fitness_horiz = this.getLocalTranslation().x * fitness_faktor_horiz;
//		  float fitness_vert = this.flur.etage.etage * fitness_faktor_vert;
//		  fitness_gesamt = fitness_vert+fitness_horiz-1;
//		  //Fitness enorm verschlechtern, falls der raum über die flurgrenzen kommt
//		  if(this.getLocalTranslation().x+this.getLaenge()/2 > this.getFlur().getLaenge()){
//			  fitness_gesamt=fitness_gesamt * 4 *( this.getLocalTranslation().x+this.getLaenge() -this.getFlur().getLaenge() );
//		  }
//	}
	
	public void updatePosition(){
//		this.setLocalTranslation(this.getFlur().getLaenge(),0,0);
		if (!(this.nachbar_links instanceof Raum) ){
			//this.setCenter(new Vector3f(this.laenge, this.getCenter().y, this.getCenter().z));
			//this.setLocalTranslation(this.laenge * 0.5f, 0, 0);
			
			//***** MOD MARKUS *****
			this.setLocalTranslation(0,0,0);
		}
		else{
			float raumabstand=0.01f;
			//this.setLocalTranslation((this.laenge * 0.5f) + this.nachbar_links.getLocalTranslation().x + (this.nachbar_links.laenge * 0.5f) + 1, 0, 0);
			
			
			//***** MOD MARKUS *****
			this.setLocalTranslation(this.getNachbar_links().getLocalTranslation().x + this.getNachbar_links().getLaenge() +raumabstand, 0, 0);

		}
	}
	
	
	/**
	 * @return the zahlerRaumnummer
	 */
	public static int getZahlerRaumnummer() {
		return zahlerRaumnummer;
	}

	/**
	 * @param zahlerRaumnummer the zahlerRaumnummer to set
	 */
	public static void setZahlerRaumnummer(int zahlerRaumnummer) {
		Raum.zahlerRaumnummer = zahlerRaumnummer;
	}

	/**
	 * @return the raumnummer
	 */
	public int getRaumnummer() {
		return raumnummer;
	}

	/**
	 * @param raumnummer the raumnummer to set
	 */
	public void setRaumnummer(int raumnummer) {
		this.raumnummer = raumnummer;
	}

	/**
	 * @return the flur
	 */
	public Flur getFlur() {
		return flur;
	}

	/**
	 * @param flur the flur to set
	 */
	public void setFlur(Flur flur) {
		this.flur = flur;
	}

	/**
	 * @return the flurSeite
	 */
	public int getFlurSeite() {
		return flurSeite;
	}

	/**
	 * @param flurSeite the flurSeite to set
	 */
	public void setFlurSeite(int flurSeite) {
		this.flurSeite = flurSeite;
	}

	/**
	 * @return the raumID
	 */
	public int getRaumID() {
		return raumID;
	}

	/**
	 * @param raumID the raumID to set
	 */
	public void setRaumID(int raumID) {
		this.raumID = raumID;
	}

	/**
	 * @return the flaeche
	 */
	public float getFlaeche() {
		return flaeche;
	}

	/**
	 * @param flaeche the flaeche to set
	 */
	public void setFlaeche(float flaeche) {
		this.flaeche = flaeche;
	}

	/**
	 * @return the tiefe
	 */
	public float getTiefe() {
		return tiefe;
	}

	/**
	 * @param tiefe the tiefe to set
	 */
	public void setTiefe(float tiefe) {
		this.tiefe = tiefe;
	}

	/**
	 * @return the laenge
	 */
	public float getLaenge() {
		return laenge;
	}

	/**
	 * @param laenge the laenge to set
	 */
	public void setLaenge(float laenge) {
		this.laenge = laenge;
	}

	/**
	 * @return the hoehe
	 */
	public int getHoehe() {
		return hoehe;
	}

	/**
	 * @param hoehe the hoehe to set
	 */
	public void setHoehe(int hoehe) {
		this.hoehe = hoehe;
	}

	/**
	 * @return the position X
	 */
	public float getPositionX() {
		return position[0];
	}
	
	/**
	 * @return the position Y
	 */
	public float getPositionY() {
		return position[1];
	}
	
	/**
	 * @return the position Z
	 */
	public float getPositionZ() {
		return position[2];
	}
	
	/**
	 * @return the position
	 */
	public float[] getPosition() {
		return position;
	}

	/**
	 * @param position the positionX to set
	 */
	public void setPositionX(float position) {
		this.position[0] = position;
	}
	
	/**
	 * @param position the positionY to set
	 */
	public void setPositionY(float position) {
		this.position[1] = position;
	}

	/**
	 * @param position the positionZ to set
	 */
	public void setPositionZ(float position) {
		this.position[2] = position;
	}

	/**
	 * @return the fitness_faktor_vert
	 */
	public float getFitness_faktor_vert() {
		return fitness_faktor_vert;
	}

	/**
	 * @param fitness_faktor_vert the fitness_faktor_vert to set
	 */
	public void setFitness_faktor_vert(float fitness_faktor_vert) {
		this.fitness_faktor_vert = fitness_faktor_vert;
	}

	/**
	 * @return the fitness_faktor_horiz
	 */
	public float getFitness_faktor_horiz() {
		return fitness_faktor_horiz;
	}

	/**
	 * @param fitness_faktor_horiz the fitness_faktor_horiz to set
	 */
	public void setFitness_faktor_horiz(float fitness_faktor_horiz) {
		this.fitness_faktor_horiz = fitness_faktor_horiz;
	}

	/**
	 * @return the fitness_gesamt
	 */
	public float getFitness_gesamt() {
		return fitness_gesamt;
	}

	/**
	 * @param fitness_gesamt the fitness_gesamt to set
	 */
	public void setFitness_gesamt(float fitness_gesamt) {
		this.fitness_gesamt = fitness_gesamt;
	}

	/**
	 * @return the nachbar_links
	 */
	public Raum getNachbar_links() {
		return nachbar_links;
	}

	/**
	 * @param nachbar_links the nachbar_links to set
	 */
	public void setNachbar_links(Raum nachbar_links) {
		this.nachbar_links = nachbar_links;
	}

	/**
	 * @return the nachbarRechtsNummer
	 */
	public int getNachbarRechtsNummer() {
		return nachbarRechtsNummer;
	}

	/**
	 * @param nachbarRechtsNummer the nachbarRechtsNummer to set
	 */
	public void setNachbarRechtsNummer(int nachbarRechtsNummer) {
		this.nachbarRechtsNummer = nachbarRechtsNummer;
	}

	/**
	 * @return the nachbarLinksNummer
	 */
	public int getNachbarLinksNummer() {
		return nachbarLinksNummer;
	}

	/**
	 * @param nachbarLinksNummer the nachbarLinksNummer to set
	 */
	public void setNachbarLinksNummer(int nachbarLinksNummer) {
		this.nachbarLinksNummer = nachbarLinksNummer;
	}

	/**
	 * @return the nachbar_rechts
	 */
	public Raum getNachbar_rechts() {
		return nachbar_rechts;
	}

	/**
	 * @param nachbar_rechts the nachbar_rechts to set
	 */
	public void setNachbar_rechts(Raum nachbar_rechts) {
		this.nachbar_rechts = nachbar_rechts;
	}

	/**
	 * @return the angeordnet
	 */
	public boolean isAngeordnet() {
		return angeordnet;
	}

	/**
	 * @param angeordnet the angeordnet to set
	 */
	public void setAngeordnet(boolean angeordnet) {
		this.angeordnet = angeordnet;
	}


	
	
	
	
}
