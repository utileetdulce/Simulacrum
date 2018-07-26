package architektur.viewer;

/**
 * Viewer
 * Klasse erstellt die Ausgabe ueber jMonkey. Hier werden die verschiedenen Texturen und Materialeigenschaften erstellt und Knoten zugewiesen
 * an welche dann die entsprechenden Raeume gehaengt werden. 
 * @author Fabian Pflaum
 * 		   Stud.Nr.: 11056940
 * 		   SS 09 
 * 		   Email: FabianPflaum@googlemail.com
 * 
 * @author Simon Engelbertz
 * 		   Stud.Nr.: 11057263
 * 		   SS 09 
 * 		   Email: simon.engelbertz@smail.fh-koeln.de
 * 
 * @version 23.06.2009
 */

import java.io.File;
import java.util.ArrayList;
import jmetest.effects.water.TestQuadWater;
import architektur.input.Flaecheneditor;
import architektur.input.Grundstueck;
import architektur.model.Optimierer;
import architektur.model.gebaude.Buero;
import architektur.model.gebaude.Gebaeude;
import architektur.model.gebaude.Hoersaal;
import architektur.model.gebaude.Raum;
import architektur.model.gebaude.Seminarraum;
import architektur.model.gebaude.Stockwerk;
import architektur.model.gebaude.Toilette;
import architektur.viewer.Viewer;

import com.jme.app.SimpleGame;
import com.jme.bounding.BoundingBox;
import com.jme.image.Texture;
import com.jme.input.KeyBindingManager;
import com.jme.input.KeyInput;
import com.jme.light.DirectionalLight;
import com.jme.math.FastMath;
import com.jme.math.Matrix3f;
import com.jme.math.Quaternion;
import com.jme.math.Vector3f;
import com.jme.renderer.ColorRGBA;
import com.jme.renderer.Renderer;
import com.jme.scene.Node;
import com.jme.scene.SceneElement;
import com.jme.scene.Skybox;
import com.jme.scene.Spatial;
import com.jme.scene.Text;
import com.jme.scene.shape.Box;
import com.jme.scene.state.AlphaState;
import com.jme.scene.state.CullState;
import com.jme.scene.state.FogState;
import com.jme.scene.state.LightState;
import com.jme.scene.state.MaterialState;
import com.jme.scene.state.RenderState;
import com.jme.scene.state.TextureState;
import com.jme.scene.state.ZBufferState;
import com.jme.system.DisplaySystem;
import com.jme.util.TextureManager;


public class Viewer extends SimpleGame {

	/**
	 * Attribute und Hilfsvariablen der Klasse Viewer
	 * 
	 * @param fhkomplex
	 *            - wird mit den Werten und Parametern des "FH-Gebaeudes" der
	 *            Klasse Gebaeude überschrieben
	 * @param cocktail
	 *            - Array, das mit den Raum-Objekten der Cocktail-Klasse
	 *            überschrieben wird
	 * @param grundstueck
	 *            - wird mit den Werten und Parametern der Klasse
	 *            "Grundstuecksdaten" überschrieben
	 * @param mainNode
	 *            - Node, der alle Knote des FH Gebaeudes beinhaltet und an den
	 *            rootNode gehaengt wird
	 * @param optimierer
	 *            - wird benoetigt, um das Optimieren des Gebaeudes während der
	 *            Ausgabe zu ermöglichen
	 * @param plusetagenspeicher
	 *            - Hilfsvariabel fuer die Anzeige der einzelenen Etagen
	 * @param minusetagenspeicher
	 *            - Hilfsvariabel fuer die Anzeige der einzelenen Etagen
	 * @param raumzaehlerplus
	 *            - Hilfsvariabel fuer die Anzeige der einzelenen Raumsorten
	 * @param raumzaehlerminus
	 *            - Hilfsvariabel fuer die Anzeige der einzelenen Raumsorten
	 * @param optimierungszaehler
	 *            - Zaehlvariabel für Optimierungsschritte
	 * @param raeume
	 *            - Enthaelt die Stockwerke des Gebaudes
	 * @param optimierungszaehler
	 *            - Zaehlvariabel für Optimierungsschritte
	 * @param abstandFassade
	 *      - Enthaelt den Abstand von der huellenden Fassade zu den Raeumen
	 * @param raumgruppe
	 *      - Textvariable fuer die Anzeige der Raumgruppen.
	 * @param flurLaenge
	 *      - Feld das die 4 unterschiedlichen Flurlaengen enthaelt
	 * @param g
	 *      - Knoten der die einzelnen Fassadenelemente zusammenfasst.
	 * @param stockwerkabstand
	 *      - Abstand der Stockwerke bei der Darstellung.
	 * @param raeume
	 *      - Feld variabler Groesse das die Raeume haelt.
	 * @param msRED
	 *            - Materials state fuer die Toiletten
	 * @param msGREEN
	 *            - Materials state fuer die Bueros
	 * @param msBLUE
	 *            - Materials state fuer die Seminarraeume
	 * @param msYELLOW
	 *            - Materials state fuer die Vorlesungsraeume
	 * @param msMAGENTA
	 *            - Materials state fuer die Gaenge
	 * @param alphaState
	 *            - alpha State fuer die Transparenz der Raeume und Flure
	 */

	
	public static Gebaeude fhkomplex;// SE
	public static Raum[] cocktail;
	public static Grundstueck grundstueck; // *******MOD
	public static Node mainNode = new Node();

	// this nodes handle the text to display
	public static Node help;
	public static Node instructions;
	public static boolean showInstructions = false;
	public static Text anzahlStudenten;
	public static Text optimierungsschritt;
	public static Text raumgruppen;

	//Optimierer
	Optimierer optimierer = new Optimierer();
	//Zeiteinheit für den Optimierer
	float time;

	int plusetagenspeicher = 0;// SE
	int minusetagenspeicher = 0;// SE
	int raumzaehlerplus = 0;
	int raumzaehlerminus = 0;
	int optimierungszaehler = 0;
	float abstandFassade = 0.15f;
	
	// Textvariabel für die die Anzeige der Raumgruppen
	String raum;

	//für die Methode fassadeErstellen 
	public float [] flurLaenge = new float [4];	
	Node g = new Node("AussenGesamtFassadeNode");
	public int stockwerkabstand = 1;
	
	// Enthalt die Stockwerke des Gebaudes
	public ArrayList<Spatial> raeume = new ArrayList<Spatial>();

	// MaterialStates
	MaterialState msRED;
	MaterialState msGREEN;
	MaterialState msBLUE;
	MaterialState msYELLOW;
	MaterialState msMAGENTA;
	MaterialState matgroup1;
	MaterialState matgroup2;
	AlphaState alphaState;

	// A sky box for our scene
	Skybox skybox;	
	Vector3f camLocation;

	/**
	 * Startet jMonkey und speichert die Übergabeparamter FH, cocktail und
	 * grundstueck in die entsprechende Attribute der Klasse Viewer
	 * 
	 * @param FH
	 *            - uebergibt ein Objekt der Klasse Gebaeude
	 * @param cocktail
	 *            - uebergibt ein Array der Klasse Raum
	 * @param grundstueck
	 *            - uebergibt ein Objekt der Klasse Grunstueck
	 */
	public void main(Gebaeude FH, Raum[] cocktail, Grundstueck grundstueck) { 
		// Uebergebe Variabeln
		Viewer.cocktail = cocktail;
		fhkomplex = FH;
		Viewer.grundstueck = grundstueck;
		
		// Füge der fhkomplex.nodeGebaude die Node des Grundstuecks hinzu
		mainNode.attachChild(fhkomplex.nodeGebaude);
		mainNode.attachChild(Viewer.grundstueck.nodeGrundstueck);
		
		Viewer app = new Viewer();
		app.setDialogBehaviour(SimpleGame.ALWAYS_SHOW_PROPS_DIALOG);
		app.start(); // Start the program
	}

	/**
	 * Erstellt Display, stellt die Kamera ein und verbindet die Keys. Wird in
	 * BaseGame.start() direkt nach der Dialog Box aufgerufen.
	 * 
	 * @see com.jme.app.AbstractGame#initSystem()
	 */
	protected void simpleInitGame() {
		// Methode erstellt Grundstück mit Textur auf dem das Gebäude stehen soll		
		grundstueck();
		// Methode gibt den Räumen aus dem Cocktail die raumspeziefischen Farben
		raumfarben();
		// Methode erstellt das Lichtsetting
		licht();
		// Methode erstellt und setzt die Kamera
		kamera();
		// Methode belegt die Tastur mit Funktionen
		tastaturbelegung();
		// Methode blendet einen Hilfstext ein
		texteinblendung();
		// Methode initialisiert den Hilfstext
		initText();
		// Skybox
		buildSkyBox();
		rootNode.attachChild(skybox);
		// Fassade erstellen
		fassadeErstellen();
		// hier werden die verschiedenen Materialknoten indirekt ueber denmainNode an den RootNode angehaengt
	 	rootNode.attachChild(mainNode);
	}

	/**
	 * Diese Methode erstellt die Fassade des Gebaeudes als ueber die Raeume gestuelpte Box-Objekte
	 * 
	 * @param Box[] flurFassaden: haelt die verschiedenen Flurfassaden Box-Objekte
	 * 
	 * @param Box[] flurBodenFassaden: haelt die verschiedenen FlurBodenFassaden Box-Objekte
	 * 
	 * @param Node n: Knoten an den die Flurfassaden Box-Objekte gehaengt werden
	 * 
	 * @param Node b: Knoten an den die FlurbodenFassaden Box-Objekte gehaengt werden
	 */
	 public void fassadeErstellen(){
		  
		  /** flurLaenge
		   *      - Feld welches die 4 verschiedenen Flurlaengen der einzelnen Fluegel haelt
		   */           
		  flurLaenge[0] = fhkomplex.stockwerkeImGebaude.get(0).flureImStockwerk.get(0).getLaenge();
		  flurLaenge[1] = fhkomplex.stockwerkeImGebaude.get(0).flureImStockwerk.get(1).getLaenge();
		  flurLaenge[2] = fhkomplex.stockwerkeImGebaude.get(0).flureImStockwerk.get(2).getLaenge();
		  flurLaenge[3] = fhkomplex.stockwerkeImGebaude.get(0).flureImStockwerk.get(3).getLaenge();

		  /**  flurFassaden 
		   *       - Feld welches die Boxobjekte der Fassade jedes Stockwerkes haelt
		   */    
		  Box [] flurFassaden = new Box [Stockwerk.gesamtzahlstockwerke*4];
		  
		  /**  flurBodenFassaden 
		   *       - Feld welches die Boxobjekte der Fassadenboeden jedes Stockwerkes haelt
		   */    
		  Box [] flurBodenFassaden = new Box [Stockwerk.gesamtzahlstockwerke*4];
		  
		  
		  /**  matgroup1 
		   *       - Materialeigenschaften fuer die Fassadenboxen
		   */    
		  matgroup1 = display.getRenderer().createMaterialState();
		  matgroup1.setDiffuse(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.8f));
		  
		  /**  matgroup1 
		   *        - Materialeigenschaften fuer die Fassadenboedenboxen
		   */    
		  matgroup2 = display.getRenderer().createMaterialState();
		  matgroup2.setDiffuse(new ColorRGBA(0.5f, 0.0f, 0.0f, 0.8f));
		  
		  //Durchlauf für alle 4 Gebaeude Teile die einen Flur enthalten
		  for (int i = 0; i < 4; i++){
		   
		   /**  n 
		    *    - SeitenfluegelFassadeKnoten an den die Fassade eines Seitenfluegels gehaengt werden
		    */    
		   Node n = new Node ("SeitenfluegelFassadeNode");
		   
		   /**  b 
		    *     - SeitenfluegelbodenFassadeKnoten an den die Fassade eines Seitenfluegelbodens gehaengt werden
		    */ 
		   Node b = new Node ("SeitenfluegelFensterNode");
		   
		   //Rotation der Knoten um 90 Grad
		   n.setLocalRotation(new Matrix3f(FastMath.cos(FastMath.PI/2*i), -FastMath.sin(FastMath.PI/2*i), 0.0f, 
		          FastMath.sin(FastMath.PI/2*i), FastMath.cos(FastMath.PI/2*i), 0.0f, 
		          0.0f,        0.0f,      1.0f));
		   
		   b.setLocalRotation(new Matrix3f(FastMath.cos(FastMath.PI/2*i), -FastMath.sin(FastMath.PI/2*i), 0.0f, 
		     FastMath.sin(FastMath.PI/2*i), FastMath.cos(FastMath.PI/2*i), 0.0f, 
		     0.0f,        0.0f,      1.0f));
		   
		   //Hier werden die Einzelnen Stockwerke durchlaufen und jeweils eine Hüllende Fassade Box erzeugt
		   for(int j = 0; j < Stockwerk.gesamtzahlstockwerke-1; j++){  

		    if(j < Stockwerk.gesamtzahlstockwerke-2){
		     flurFassaden [j] = new Box("stockwerksbox", new Vector3f(0, 0, (j*(fhkomplex.meineDaten.stockwerkHoehe+0.2f))), new Vector3f((flurLaenge[i]+abstandFassade), ((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2)), (((j+1)*(fhkomplex.meineDaten.stockwerkHoehe+0.2f))-0.2f)));  
		     flurFassaden [j].setModelBound(new BoundingBox());
		     flurFassaden [j].updateModelBound();
		     flurFassaden [j].setLocalTranslation((((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2))/2), -(((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2))/2), 0);
		     flurFassaden [j].setRenderState(matgroup1);
		     }
		    else{
		      flurFassaden [j] = new Box("stockwerksbox", new Vector3f(0, 0, (j*(fhkomplex.meineDaten.stockwerkHoehe+0.2f))), new Vector3f((flurLaenge[i]+abstandFassade), ((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2)), (((j+1)*(fhkomplex.meineDaten.stockwerkHoehe+0.2f)))));  
		      flurFassaden [j].setModelBound(new BoundingBox());
		      flurFassaden [j].updateModelBound();
		      flurFassaden [j].setLocalTranslation((((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2))/2), -(((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2))/2), 0);
		      flurFassaden [j].setRenderState(matgroup1);
		     }
		  
		       flurBodenFassaden [j] = new Box ("fensterFront", new Vector3f(0, 0, ((j*(fhkomplex.meineDaten.stockwerkHoehe+0.2f))-0.2f)), new Vector3f((flurLaenge[i]+abstandFassade), ((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2)), (j*(fhkomplex.meineDaten.stockwerkHoehe+0.2f))));
		       flurBodenFassaden [j].setModelBound(new BoundingBox());
		       flurBodenFassaden [j].updateModelBound();
		       flurBodenFassaden [j].setLocalTranslation((((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2))/2), -(((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2))/2), 0);
		       flurBodenFassaden [j].setRenderState(matgroup2);

		      
		    //Anhaengen der Erstellten Stockwerksfassadenbox an den SeitenfluegelNode
		    n.attachChild(flurFassaden [j]);
		    b.attachChild(flurBodenFassaden [j]);
		   }
		          
		   /**  treppenhausFassade 
		    *            - Box Objekt das die Fassade des Treppenhauses bildet
		    */ 
		  Box treppenhausFassade = new Box("Treppenhausbox",new Vector3f(0, 0, 0), new Vector3f(((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2)), ((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2)), (((fhkomplex.meineDaten.stockwerkHoehe+0.2f)*Stockwerk.gesamtzahlstockwerke)+2)));
		  treppenhausFassade.setLocalTranslation(-(((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2))/2), -(((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2))/2), 0.0f);
		  n.attachChild(treppenhausFassade);
		  
		  /**  treppenhausFassadenBoden 
		   *            - Box Objekt das den Fassadenboden des Treppenhauses bildet
		   */ 
		  Box treppenhausFassadenBoden = new Box("Treppenhausbox",new Vector3f(0, 0, -0.2f), new Vector3f(((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2)), ((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2)), 0.2f));
		  treppenhausFassadenBoden.setLocalTranslation(-(((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2))/2), -(((fhkomplex.meineDaten.flurbreite+(2*fhkomplex.meineDaten.raumtiefe))+(abstandFassade*2))/2), 0.0f);
		  b.attachChild(treppenhausFassadenBoden);
		  
		  //
		  n.setRenderState(matgroup1);  
		  b.setRenderState(matgroup2);
		  
		  /**  alphaState 
		   *            - Transparenz Objekt
		   */ 
		  alphaState = DisplaySystem.getDisplaySystem().getRenderer().createAlphaState();
		  alphaState.setBlendEnabled(true);
		  alphaState.setTestEnabled(true);
		  alphaState.setEnabled(true);
		  g.setRenderState(alphaState);
		  
		  //Anhaengen des SeitenfluegelNode an den AussengesamtFassadeNode
		  g.attachChild(n);
		  g.attachChild(b);  
		  }

		  //Rotation des Knontens für die Fassade zur Anpassung an das Koordinatensystem der Raeume
		  g.setLocalRotation(new Matrix3f(1.0f,    0.0f,        0.0f,
		     0.0f, -FastMath.cos(FastMath.PI/2), FastMath.sin(FastMath.PI/2), 
		     0.0f, -FastMath.sin(FastMath.PI/2), -FastMath.cos(FastMath.PI/2) 
		    ));
		  //Anhaengen des AussengesamtFassadeNode an den Gebaeude Node 
		  fhkomplex.nodeGebaude.attachChild(g);
		 }
	
	/**
	 * Diese Methode wird für jeden Frame aufgerufen, um eventuelle
	 * Veraenderungen während der Animation zu atualisieren
	 */
	public void simpleUpdate() {
		// Update Text 
		updateBuffer.setLength( 0 );
		updateBuffer.append( "Raumgruppe: " ).append(raum).append( "  Optimierungsschritt: " ).append( optimierer.getOptimierungsschritt() );
		// Update Skybox
		skybox.getLocalTranslation().set(cam.getLocation());
		skybox.updateGeometricState(0.0f, true);
		
		// Methode beinhaltet die Funktionen für die entsprechenden Tastaturbefehle
		checkInput();
		fps.updateRenderState();
		
		if(optimierer.isFertig()==false){
			optimierer.optimieren(100, fhkomplex);}
	}

	/**
	 * Diese Methode belegt einzelne Tasten der Tastatur mit Funktionen
	 */
	private void tastaturbelegung() {
		// Belegt Tasturtasten mit Funktionen
		// F1 -> Ansicht der einzelnen Etagen wechseln (+)
		KeyBindingManager.getKeyBindingManager().set("Stock_plus",
				KeyInput.KEY_ADD);

		// F2 -> Ansicht der einzelnen Etagen wechseln (-)
		KeyBindingManager.getKeyBindingManager().set("Stock_minus",
				KeyInput.KEY_SUBTRACT);

		// PHIL MOD START **********************************************
		 // F2 -> Gebäude nochmal durch den Optimierer laufen lassen
		 KeyBindingManager.getKeyBindingManager().set("Fassade_100",
		 KeyInput.KEY_F2);
				
		 // F3 -> Gebäude nochmal durch den Optimierer laufen lassen
		 KeyBindingManager.getKeyBindingManager().set("Fassade_10",
		 KeyInput.KEY_F3);

		// F4 -> Gebäude nochmal durch den Optimierer laufen lassen
		KeyBindingManager.getKeyBindingManager().set("optimieren_100",
				KeyInput.KEY_F4);

		// F1 -> Steurungsinstruktionen aufrufen und wieder abblenden
		KeyBindingManager.getKeyBindingManager().set("instructions",
				KeyInput.KEY_F1);

		// Enter -> FH Gebaeude wieder in Ursprunszustand zurück bringen
		KeyBindingManager.getKeyBindingManager().set("Normal-Ansicht",
				KeyInput.KEY_RETURN);

		// 1 -> Einzelne Raumgruppen anzeigen
		KeyBindingManager.getKeyBindingManager().set("Raumklassen_Plus",
				KeyInput.KEY_1);

		// 2 -> Einzelne Raumgruppen anzeigen
		KeyBindingManager.getKeyBindingManager().set("Raumklassen_Minus",
				KeyInput.KEY_2);

		// 3 -> Grundstueck anzeigen
		KeyBindingManager.getKeyBindingManager().set("Grunstueck_an",
				KeyInput.KEY_3);

		// 4 -> Grundstueck ausblenden
		KeyBindingManager.getKeyBindingManager().set("Grunstueck_aus",
				KeyInput.KEY_4);
	}

	/**
	 * Verarbeitet Key-Aktionen
	 */
	public void checkInput() {
		// Ansicht einzelner Etagen (aufwaerts)
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"Stock_plus", false)) {			
			for (int i = 0; i < fhkomplex.NodeStockwerke.length; i++) {
				fhkomplex.nodeGebaude.detachChild(fhkomplex.NodeStockwerke[i]);
			}
			if (plusetagenspeicher > fhkomplex.NodeStockwerke.length - 1) {
				plusetagenspeicher = 0;
			}
			fhkomplex.nodeGebaude
					.attachChild(fhkomplex.NodeStockwerke[plusetagenspeicher]);
			minusetagenspeicher = plusetagenspeicher - 1;
			plusetagenspeicher++;
			fhkomplex.nodeGebaude.detachChild(g);
			System.out
					.println("------------> Sie haben die Taste + gedrueckt <-------------");
		}
		
		// Ansicht einzelner Etagen (abwaerts)
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"Stock_minus", false)) {
			
			for (int i = 0; i < fhkomplex.NodeStockwerke.length; i++) {
				fhkomplex.nodeGebaude.detachChild(fhkomplex.NodeStockwerke[i]);
			}
			if (minusetagenspeicher < 0) {
				minusetagenspeicher = fhkomplex.NodeStockwerke.length - 1;
			}
			fhkomplex.nodeGebaude
					.attachChild(fhkomplex.NodeStockwerke[minusetagenspeicher]);
			plusetagenspeicher = minusetagenspeicher + 1;
			minusetagenspeicher--;
			fhkomplex.nodeGebaude.detachChild(g);
			System.out
					.println("------------> Sie haben die Taste - gedrueckt <-------------");
		}

		// Bringt FH-Gebaeude in die Uersprungsform zurueck
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"Normal-Ansicht", true)) {			
			msRED.setDiffuse(new ColorRGBA(0.5f, 0.1f, 0, 0.9f));
			msGREEN.setDiffuse(new ColorRGBA(0.5f, 0.4f, 0, 0.9f));
			msBLUE.setDiffuse(new ColorRGBA(0.2f, 0.2f, 0, 0.9f));
			msYELLOW.setDiffuse(new ColorRGBA(0.25f, 0.25f, 0.15f, 0.9f));
			msMAGENTA.setDiffuse(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.4f));

			for (int i = 0; i < fhkomplex.NodeStockwerke.length; i++) {
				fhkomplex.nodeGebaude.attachChild(fhkomplex.NodeStockwerke[i]);
			}
			fhkomplex.nodeGebaude.attachChild(g);
			System.out
					.println("------------> Sie haben die Taste Enter gedrueckt <-------------");
		}

		// Fassade wird opak geschaltet 
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
		 "Fassade_100", true)) {
			 matgroup1.setDiffuse(new ColorRGBA(0.05f, 0.05f, 0.05f, 1f));
			 matgroup2.setDiffuse(new ColorRGBA(0.5f, 0.0f, 0.0f, 1f));

		 System.out.println("------------> Sie haben die Taste F2 gedrueckt <-------------");
		 }
		// Fassade wird transparent geschaltet
		 if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				 "Fassade_10", true)) {
					 matgroup1.setDiffuse(new ColorRGBA(0.5f, 0.5f, 0.5f, 0.2f));
					 matgroup2.setDiffuse(new ColorRGBA(0.5f, 0.0f, 0.0f, 0.8f));

				 System.out.println("------------> Sie haben die Taste F3 gedrueckt <-------------");
		 }	 
		// Führt Optimierungsschritte aus, bis eine ausreichende Qualitaet erzielt ist
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"optimieren_100", false)) {
			//Optimierer starten, wenn er noch nicht gestartet wurde
			if(optimierer.getMisserfolgKritisch()==0)
			optimierer.setFertig(false);
			
			System.out
					.println("------------> Sie haben die Taste F7 gedrueckt <-------------");
		}
	
		// Ansicht einzelner Raumgruppen (abwaerts)
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"Raumklassen_Minus", false)) {
			//Auswahl Toiletten
			if (raumzaehlerplus == 0) {
				msRED.setDiffuse(new ColorRGBA(0.5f, 0.1f, 0, 1f));
				msGREEN.setDiffuse(new ColorRGBA(0.5f, 0.4f, 0, 0.1f));
				msBLUE.setDiffuse(new ColorRGBA(0.2f, 0.2f, 0, 0.1f));
				msYELLOW.setDiffuse(new ColorRGBA(0.25f, 0.25f, 0.15f, 0.1f));
				msMAGENTA.setDiffuse(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.4f));
				
				raum = "Toiletten";
			}
			//Auswahl BUEROS
			if (raumzaehlerplus == 1) {
				msRED.setDiffuse(new ColorRGBA(0.5f, 0.1f, 0, 0.1f));
				msGREEN.setDiffuse(new ColorRGBA(0.5f, 0.4f, 0, 1f));
				msBLUE.setDiffuse(new ColorRGBA(0.2f, 0.2f, 0, 0.1f));
				msYELLOW.setDiffuse(new ColorRGBA(0.25f, 0.25f, 0.15f, 0.1f));
				msMAGENTA.setDiffuse(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.4f));
				
				raum = "Bueros";
			}
			//Auswahl Seminaraeume
			if (raumzaehlerplus == 2) {
				msRED.setDiffuse(new ColorRGBA(0.5f, 0.1f, 0, 0.1f));
				msGREEN.setDiffuse(new ColorRGBA(0.5f, 0.4f, 0, 0.1f));
				msBLUE.setDiffuse(new ColorRGBA(0.2f, 0.2f, 0.0f, 1f));
				msYELLOW.setDiffuse(new ColorRGBA(0.25f, 0.25f, 0.15f, 0.1f));
				msMAGENTA.setDiffuse(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.4f));
				
				raum = "Semianarraeume";
			}
			//Auswahl Vorlesungsaele
			if (raumzaehlerplus == 3) {
				msRED.setDiffuse(new ColorRGBA(0.5f, 0.1f, 0, 0.1f));
				msGREEN.setDiffuse(new ColorRGBA(0.5f, 0.4f, 0, 0.1f));
				msBLUE.setDiffuse(new ColorRGBA(0.2f, 0.2f, 0, 0.1f));
				msYELLOW.setDiffuse(new ColorRGBA(0.25f, 0.25f, 0.15f, 1f));
				msMAGENTA.setDiffuse(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.4f));
				
				raum = "Vorlesungsaele";
			}
			raumzaehlerminus = raumzaehlerplus - 1;
			raumzaehlerplus++;
			if (raumzaehlerplus > 3) {
				raumzaehlerplus = 0;
			}
			if (raumzaehlerminus < 0) {
				raumzaehlerminus = 3;
			}

		}

		// Ansicht einzelner Raumgruppen (aufwaerts)
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"Raumklassen_Plus", false)) {
			//Auswahl Toiletten
			if (raumzaehlerminus == 0) {
				msRED.setDiffuse(new ColorRGBA(0.5f, 0.1f, 0, 1f));
				msGREEN.setDiffuse(new ColorRGBA(0.5f, 0.4f, 0, 0.1f));
				msBLUE.setDiffuse(new ColorRGBA(0.2f, 0.2f, 0, 0.1f));
				msYELLOW.setDiffuse(new ColorRGBA(0.25f, 0.25f, 0.15f, 0.1f));
				msMAGENTA.setDiffuse(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.1f));
				
				raum = "Toiletten";
			}
			//Auswahl Bueros
			if (raumzaehlerminus == 1) {
				msRED.setDiffuse(new ColorRGBA(0.5f, 0.1f, 0, 0.1f));
				msGREEN.setDiffuse(new ColorRGBA(0.5f, 0.4f, 0, 1f));
				msBLUE.setDiffuse(new ColorRGBA(0.2f, 0.2f, 0, 0.1f));
				msYELLOW.setDiffuse(new ColorRGBA(0.25f, 0.25f, 0.15f, 0.1f));
				msMAGENTA.setDiffuse(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.4f));
				
				raum = "Bueros";
			}
			//Auswahl Seminaraeume
			if (raumzaehlerminus == 2) {
				msRED.setDiffuse(new ColorRGBA(0.5f, 0.1f, 0, 0.1f));
				msGREEN.setDiffuse(new ColorRGBA(0.5f, 0.4f, 0, 0.1f));
				msBLUE.setDiffuse(new ColorRGBA(0.2f, 0.2f, 0.0f, 1f));
				msYELLOW.setDiffuse(new ColorRGBA(0.25f, 0.25f, 0.15f, 0.1f));
				msMAGENTA.setDiffuse(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.4f));
				
				raum = "Semianarraeume";
			}
			//Auswahl Vorlesungsaele
			if (raumzaehlerminus == 3) {
				msRED.setDiffuse(new ColorRGBA(0.5f, 0.1f, 0, 0.1f));
				msGREEN.setDiffuse(new ColorRGBA(0.5f, 0.4f, 0, 0.1f));
				msBLUE.setDiffuse(new ColorRGBA(0.2f, 0.2f, 0, 0.1f));
				msYELLOW.setDiffuse(new ColorRGBA(0.25f, 0.25f, 0.15f, 1f));
				msMAGENTA.setDiffuse(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.4f));
				
				raum = "Vorlesungsaele";
			}
			raumzaehlerplus = raumzaehlerminus + 1;
			raumzaehlerminus--;
			if (raumzaehlerminus < 0) {
				raumzaehlerminus = 3;
			}
			if (raumzaehlerplus > 3) {
				raumzaehlerplus = 0;
			}
		}

		// Macht Grundstueck sichtbar
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"Grunstueck_an", false)) {
			mainNode.attachChild(Viewer.grundstueck.nodeGrundstueck);
			System.out
					.println("------------> Sie haben die Taste 6 gedrueckt <-------------");
		}

		// Macht Grundstueck unsichtbar
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"Grunstueck_aus", false)) {
			mainNode.detachChild(Viewer.grundstueck.nodeGrundstueck);
			System.out
					.println("------------> Sie haben die Taste 7 gedrueckt <-------------");
		}

		// Ueberprueft, ob die Steuerungs-Anzeige eingeblendet werden soll
		if (KeyBindingManager.getKeyBindingManager().isValidCommand(
				"instructions", false)) {
			if (showInstructions) {
				fpsNode.attachChild(help);
				fpsNode.detachChild(instructions);
				showInstructions = false;
			} else {
				fpsNode.attachChild(instructions);
				fpsNode.detachChild(help);
				showInstructions = true;
			}
		}
		
	}
	
	/**
	 * Erstellt Grundstueck mit Textur der Bilddatei des Users
	 */
	private void grundstueck() {
        
		  TextureState ts=display.getRenderer().createTextureState();
		  // Use the TextureManager to load a texture
		  Texture t=TextureManager.loadTexture(Flaecheneditor.pfad,Texture.MM_LINEAR,Texture.FM_LINEAR);
		  // Assign the texture to the TextureState
		  ts.setTexture(t);
		   
		 // Erstelle Grundstueck als Box 
		  Box grundstueck = new Box("Grundstueck", new Vector3f(0, 0, 0), new Vector3f(Viewer.grundstueck.rasterBreite * Viewer.grundstueck.grundstuecksRaster[0].length, 0.1f, Viewer.grundstueck.rasterBreite * Viewer.grundstueck.grundstuecksRaster.length));
		//  grundstueck.setLocalTranslation(0, -0.5f * cocktail[3].getHoehe(), 0);
		  
		  Quaternion Rotation1 = new Quaternion(); Rotation1.fromAngles(0, -FastMath.DEG_TO_RAD * 90, 0);
		  

		 grundstueck.setLocalRotation(Rotation1);
		 grundstueck.setLocalTranslation(2 * (grundstueck.xExtent * 2), -0.5f, 0);
		 
		  grundstueck.setRenderState(ts);
		  mainNode.attachChild(grundstueck);
		  }

	/**
	 * Gibt den Raeumen und Fluren ihre entsprechende Farbe und Transparenz.
	 */
	private void raumfarben() {
		// MaterialState fuer Raeume und Flure
		msRED = display.getRenderer().createMaterialState();
		msGREEN = display.getRenderer().createMaterialState();
		msBLUE = display.getRenderer().createMaterialState();
		msYELLOW = display.getRenderer().createMaterialState();
		msMAGENTA = display.getRenderer().createMaterialState();

		// Stellt Farbe und Transparenz ein
		msRED.setDiffuse(new ColorRGBA(0.5f, 0.1f, 0, 0.9f));
		msGREEN.setDiffuse(new ColorRGBA(0.5f, 0.4f, 0, 0.9f));
		msBLUE.setDiffuse(new ColorRGBA(0.2f, 0.2f, 0, 0.9f));
		msYELLOW.setDiffuse(new ColorRGBA(0.25f, 0.25f, 0.15f, 0.9f));
		msMAGENTA.setDiffuse(new ColorRGBA(0.1f, 0.1f, 0.1f, 0.4f));

		// MaterialSTate fuer Grundstuck
		MaterialState msGREENGrund = display.getRenderer()
				.createMaterialState();
		MaterialState msDARKGRAYGrund = display.getRenderer()
				.createMaterialState();
		MaterialState msBLACKGrund = display.getRenderer()
				.createMaterialState();

		// Farben für MS Grundstueck
		msDARKGRAYGrund.setDiffuse(ColorRGBA.darkGray);
		msBLACKGrund.setDiffuse(ColorRGBA.black);
		msGREENGrund.setDiffuse(ColorRGBA.green);

		// AlphaState für die Tarnsparenz der Raeume und Flure
		alphaState = DisplaySystem.getDisplaySystem().getRenderer().createAlphaState();
		alphaState.setBlendEnabled(true);
		alphaState.setTestEnabled(true);
		alphaState.setEnabled(true);

		//Textur für die ausgewaehlte flaeche
		TextureState rasen1=display.getRenderer().createTextureState();
		File myFile = new File("bin/architektur/data/Rasen.jpg");
		Texture rasen1_t=TextureManager.loadTexture(myFile.getAbsolutePath(),Texture.MM_LINEAR,Texture.FM_LINEAR);
		rasen1.setTexture(rasen1_t);
		  
		//Textur für den Rand der ausgewaehlte flaeche
		TextureState rasen2=display.getRenderer().createTextureState();
		File myFile2 = new File("bin/architektur/data/Rasen2.jpg");
		Texture rasen2_t=TextureManager.loadTexture(myFile2.getAbsolutePath(),Texture.MM_LINEAR,Texture.FM_LINEAR);
		rasen2.setTexture(rasen2_t);

		// Durch Grundstuck laufen und MS zuweisen
		for (int i = 0; i < Viewer.grundstueck.grundstuecksRaster.length; i++) {
			for (int j = 0; j < Viewer.grundstueck.grundstuecksRaster[0].length; j++) {

				// System.out.println(Viewer.grundstueck.grundstuecksRaster[i][j].gehoertZurGrundstuecksgrenze);

				if (Viewer.grundstueck.grundstuecksRaster[i][j].gehoertZurGrundstuecksgrenze == true)
					Viewer.grundstueck.grundstuecksRaster[i][j]
							.setRenderState(rasen2);
				else if (Viewer.grundstueck.grundstuecksRaster[i][j].gehoertZumGrundstueck == false
						&& Viewer.grundstueck.grundstuecksRaster[i][j].gehoertZurGrundstuecksgrenze == false)
					Viewer.grundstueck.grundstuecksRaster[i][j]
							.setRenderState(rasen2);
				else if (Viewer.grundstueck.grundstuecksRaster[i][j].gehoertZumGrundstueck == true)
					Viewer.grundstueck.grundstuecksRaster[i][j]
							.setRenderState(rasen1);

				else if (Viewer.grundstueck.grundstuecksRaster[i][j].maxPunkt == true)
					Viewer.grundstueck.grundstuecksRaster[i][j]
							.setRenderState(rasen1);
			}
		}

		// Texturfarbe und Transparenz fuer Flure
		for (int j = 0; j < fhkomplex.NodeStockwerke.length; j++) {
			fhkomplex.NodeStockwerke[j].setRenderState(msMAGENTA);
			fhkomplex.NodeStockwerke[j].setRenderState(alphaState);
		}

		// Hier wird das CocktailArray durchlaufen, um den Raeumen die jeweilige Farbe und Transparenz zu geben

		for (int i = 0; i < cocktail.length; i++) {

			if (cocktail[i] instanceof Toilette) {
				cocktail[i].setRenderState(msRED);
				cocktail[i].setRenderState(alphaState);
			}

			if (cocktail[i] instanceof Hoersaal) {
				cocktail[i].setRenderState(msYELLOW);
				cocktail[i].setRenderState(alphaState);
			}
			if (cocktail[i] instanceof Seminarraum) {
				cocktail[i].setRenderState(msBLUE);
				cocktail[i].setRenderState(alphaState);
			}
			if (cocktail[i] instanceof Buero) {
				cocktail[i].setRenderState(msGREEN);
				cocktail[i].setRenderState(alphaState);
			}
		}
	}

	/**
	 * Richtet das Licht für die Simulation ein
	 */
	private void licht() {
		// Erzeugung der 5 diffusen Lichtquellen 
		DirectionalLight l = new DirectionalLight();
		DirectionalLight l2 = new DirectionalLight();
		DirectionalLight l3 = new DirectionalLight();
		DirectionalLight l4 = new DirectionalLight();
		DirectionalLight l5 = new DirectionalLight();

		// Bestimmung der Lichtrichtung desr Diffusen Lichtquellen
		// Licht von ObenZentrum
		l.setDirection(new Vector3f(0, -1, 0));
		// Licht von ObenHintenRechts
		l2.setDirection(new Vector3f(-1, -1, 1));
		// Licht von ObenVorneRechts
		l3.setDirection(new Vector3f(-1, -1, -1));
		// Licht von ObenVorneLinks
		l4.setDirection(new Vector3f(1, -1, -1));
		// Licht von ObenHIntenLinks
		l5.setDirection(new Vector3f(1, -1, 1));

		// Farbgebung fuer die verschiedenen Knoten */
		l.setAmbient(new ColorRGBA(.5f, .5f, .5f, 1f));
		l2.setAmbient(new ColorRGBA(.05f, .05f, .05f, 1));
		l3.setAmbient(new ColorRGBA(.05f, .05f, .05f, 1));
		l4.setAmbient(new ColorRGBA(.05f, .05f, .05f, 1));
		l5.setAmbient(new ColorRGBA(.05f, .05f, .05f, 1));

		// Aktivieren der einzelnen Knoten
		l.setEnabled(true);
		l2.setEnabled(true);
		l3.setEnabled(true);
		l4.setEnabled(true);
		l5.setEnabled(true);

		// Erzeugung eines zusammenfassenden "HauptLichtknotens"
		LightState ls = display.getRenderer().createLightState();

		// Anhaengen der einzelnen Knoten an den HauptLichtknoten
		ls.attach(l);
		ls.attach(l2);
		ls.attach(l3);
		ls.attach(l4);
		ls.attach(l5);

		// Hier werden die StandardLichtquellen von SimpleGame deaktiviert
		lightState.detachAll();

		// Hier wird der mainNode und alles was an ihm haengt mit den Renderer verbunden
		mainNode.setRenderState(ls);
	}

	/**
	 * Erzeugung der Kamera & Festlegung der Koordinaten fuer die
	 * Positionierung.
	 */
	private void kamera() {
		cameraPerspective();
		Vector3f loc = new Vector3f(150f, 170f, 350f);
		Vector3f left = new Vector3f(-1.0f, 0.0f, 0.0f);
		Vector3f up = new Vector3f(0f, 1.0f, 0.0f);
		Vector3f dir = new Vector3f(0.0f, 0.0f, -1.0f);

		/** Positionierung und Ausrichtung der Kamera. */
		cam.setFrame(loc, left, up, dir);

		cam.lookAt(new Vector3f(500.0f, 0.0f, 100.0f), new Vector3f(0.0f, 1.0f,
				0.0f));

		/** Staendige Aktualisierung der Kameraparameter. */
		cam.update();
		/** Kamera mit dem Renderer verknuepfen. */
		display.getRenderer().setCamera(cam);
	}

	/**
	 *Blendet Informationstext ein
	 */
	private void texteinblendung() {
		// Texeinblendung von BseGame
		// This is what will actually have the text at the bottom. 
		Text fps_new = Text.createDefaultTextLabel("CA test");
		fps_new.setCullMode(SceneElement.CULL_NEVER);
		fps_new.setTextureCombineMode(TextureState.REPLACE);

		// Stand alone node (mit Absicht nicht an den rootNode gehaengt)
		Node fpsNode_new = new Node("FPS node");
		fpsNode_new
				.setRenderState(fps_new.getRenderState(RenderState.RS_ALPHA));
		fpsNode_new.setRenderState(fps_new
				.getRenderState(RenderState.RS_TEXTURE));
		fpsNode_new.attachChild(fps_new);
		fpsNode_new.setCullMode(SceneElement.CULL_NEVER);

		Renderer testR = display.getRenderer();
		testR.draw(fpsNode_new);
	}

	/**
	 * Initialisiert den Instruktionstext
	 */
	private void initText() {
		int height = display.getHeight();

		// Hilfknoten haelt den Text
		// "Druecken Sie die Taste 3 fuer die Bedienungsanzeige"
		help = new Node("help");
		Text helperText = new Text("help",
				"Druecken Sie die Taste F1 fuer die Bedienungsanzeige");
		helperText.setLocalTranslation(new Vector3f(10, height - 30, 0));
		help.attachChild(helperText);

		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		instructions = new Node("instructions");
		Text inst1 = new Text("instruction1",
				"Taste + u. Taste -: Ansicht der einzelnen Etagen");
		inst1.setLocalTranslation(new Vector3f(10, height - 30, 0));
		instructions.attachChild(inst1);
		
		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst4 = new Text("instruction4",
				"Taste 1 u. Taste 2: Ansicht der einzelnen Raumgruppen");
		inst4.setLocalTranslation(new Vector3f(10, height - 55, 0));
		instructions.attachChild(inst4);
		
		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst2 = new Text("instruction4",
				"Taste 3 u. Taste 4: Grundflaeche ein- und ausblenden");
		inst2.setLocalTranslation(new Vector3f(10, height - 80, 0));
		instructions.attachChild(inst2);
		
		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst5 = new Text("instruction5",
				"Taste F4: Gebaeude optimieren");
		inst5.setLocalTranslation(new Vector3f(10, height - 155, 0));
		instructions.attachChild(inst5);
		
		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst11 = new Text("instruction5",
				"Taste F2: Gebaeudehuelle opak schalten");
		inst11.setLocalTranslation(new Vector3f(10, height - 105, 0));
		instructions.attachChild(inst11);
		
		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst12 = new Text("instruction5",
				"Taste F3: Gebaeudehuelle transparent schalten");
		inst12.setLocalTranslation(new Vector3f(10, height - 130, 0));
		instructions.attachChild(inst12);

		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst3 = new Text("instruction3",
				"Return-Taste: Gebaeude wird in Ursprungsform angezeigt");
		inst3.setLocalTranslation(new Vector3f(10, height - 180, 0));
		instructions.attachChild(inst3);

		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst6 = new Text("instruction6",
				"Taste A: Kamerasteurung nach links");
		inst6.setLocalTranslation(new Vector3f(10, height - 205, 0));
		instructions.attachChild(inst6);

		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst7 = new Text("instruction7",
				"Taste D: Kamerasteurung nach rechts");
		inst7.setLocalTranslation(new Vector3f(10, height - 230, 0));
		instructions.attachChild(inst7);

		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst8 = new Text("instruction8",
				"Taste W: Kamerasteurung nach vorne");
		inst8.setLocalTranslation(new Vector3f(10, height - 255, 0));
		instructions.attachChild(inst8);

		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst9 = new Text("instruction9",
				"Taste S: Kamerasteurung nach hinten");
		inst9.setLocalTranslation(new Vector3f(10, height - 280, 0));
		instructions.attachChild(inst9);

		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst10 = new Text("instruction10",
				"Taste Q: Kamerasteurung nach oben");
		inst10.setLocalTranslation(new Vector3f(10, height - 305, 0));
		instructions.attachChild(inst10);

		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst13 = new Text("instruction11",
				"Taste E: Kamerasteurung nach unten");
		inst13.setLocalTranslation(new Vector3f(10, height - 330, 0));
		instructions.attachChild(inst13);

		// Instructions Knoten haelt alle weiteren Steuerungs-Instruktionen
		Text inst14 = new Text("instruction12",
				"Taste ESC: Beenden der Simulation");
		inst14.setLocalTranslation(new Vector3f(10, height - 355, 0));
		instructions.attachChild(inst14);

		// Gibt den Hilfstext als Standardtext aus
		fpsNode.attachChild(help);
		
		// Fuegt fpsNode rootNode hinzu
		rootNode.attachChild(fpsNode);
	}

		/**
		 * a helper class to create our skybox
		 * @param node we will attach our skybox to this node
		 * @param camLocation we want to track the camera so we store its location
		 * @return 
		 */
	private void buildSkyBox() {
        skybox = new Skybox("skybox", 10, 10, 10);

        String dir = "jmetest/data/skybox1/";
        Texture north = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "1S.jpg"),
                Texture.MM_LINEAR, Texture.FM_LINEAR);
        Texture south = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "3S.jpg"),
                Texture.MM_LINEAR, Texture.FM_LINEAR);
        Texture east = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "2S.jpg"),
                Texture.MM_LINEAR, Texture.FM_LINEAR);
        Texture west = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "4S.jpg"),
                Texture.MM_LINEAR, Texture.FM_LINEAR);
        Texture up = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "6S.jpg"),
                Texture.MM_LINEAR, Texture.FM_LINEAR);
        Texture down = TextureManager.loadTexture(TestQuadWater.class
                .getClassLoader().getResource(dir + "5S.jpg"),
                Texture.MM_LINEAR, Texture.FM_LINEAR);

        skybox.setTexture(Skybox.NORTH, north);
        skybox.setTexture(Skybox.WEST, west);
        skybox.setTexture(Skybox.SOUTH, south);
        skybox.setTexture(Skybox.EAST, east);
        skybox.setTexture(Skybox.UP, up);
        skybox.setTexture(Skybox.DOWN, down);
        skybox.preloadTextures();

        CullState cullState = display.getRenderer().createCullState();
        cullState.setCullMode(CullState.CS_NONE);
        cullState.setEnabled(true);
        skybox.setRenderState(cullState);

        ZBufferState zState = display.getRenderer().createZBufferState();
        zState.setEnabled(false);
        skybox.setRenderState(zState);

        FogState fs = display.getRenderer().createFogState();
        fs.setEnabled(false);
        skybox.setRenderState(fs);

        skybox.setLightCombineMode(LightState.OFF);
        skybox.setCullMode(SceneElement.CULL_NEVER);
        skybox.setTextureCombineMode(TextureState.REPLACE);
        skybox.updateRenderState();

        skybox.lockBounds();
        skybox.lockMeshes();
    }	
}