package architektur.input;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;



/**
 * Die Klasse Flaecheneditor erzeugt eine grafische Oberfl�che (GUI - Graphical User Interface), 
 * mit dessen Hilfe man das Grundst�ck, auf dem der Neubau der Fachhochschule visualisiert werden soll, 
 * in das Programm laden kann. Die Klasse basiert dabei auf Swing, einer Bibliothek f�r die Gestaltung 
 * grafischer Benutzeroberfl�chen, erweitert die Klasse JFrame und implementiert das Interface ActionListener.<br> 
 * Nach dem Start �ffnet sich das Flaecheneditor-Fenster, und von da an sind im Prinzip 3 Schritte notwendig,
 * um das Grundst�ck in das Programm zu laden.<br>
 * 1. Die Bilddatei (eine Luftbildaufnahme der Baufl�che und ihrer Umgebung)ausw�hlen<br>
 * 2. Den Ma�stab bestimmen, damit das Programm wei�, wie es von Pixeln in Meter umrechnen kann<br>
 * 3. Die Fl�che innerhalb der Bilddatei markieren, auf der die FH angezeigt werden soll
 * Dr�ckt man nun auf den Start-Button, erh�lt der Controller die Best�tigung, dass er fortfahren kann.
 *  
 * @author Thomas Retzkowski, 
 * 		   Stud.Nr.: 11057796
 * 		   SS 09 
 * 		   Email: thomas.retzkowski@smail.fh-koeln.de
 * 		   Gruppe 4
 * 		   23.06.2009
 * 
 * @version 1.4
 */
public class Flaecheneditor extends JFrame implements ActionListener{												 
	
	/**
	 * ContentPane geh�rt zur inneren Struktur von Swing-Hauptfenstern<br>
	 * ContentPane = Container auf dem die sichtbaren Komponenten (Diaolgelemente) platziert werden<br>
	 * Container = abstrakte Klasse. Daf�r zust�ndig, innerhalb einer Komponente andere Komponenten aufzunehmen.<br>
	 */
	private Container cp = getContentPane();
	
	
	/**
	 * JPanels ordnen Komponenten unter Kontrolle eines eigenen Layoutmanagers an
	 */
	private JPanel koordinatenPanel, imagecoordsPanel, anweisungsPanel, anweisung1Panel, anweisung2Panel, 
				   anweisung3Panel, anweisung4Panel, anweisung5Panel, anweisung6Panel, abbruchStartPanel;
	
	
	/**
	 * JLabel = Komponente zur Anzeige eines Textes. Kann zus�tzlich ein Icon enthalten.
	 */
	private JLabel imageLabel, anweisung1Label, anweisung2Label, anweisung3Label, anweisung4Label, 
				   anweisung5Label, anweisung6Label;
	
	
	/**
	 * JButton = Komponente zur Darstellung von Schaltfl�chen
	 */ 
	private JButton anweisung1Button, anweisung2Button, anweisung2ResetButton, anweisung3Button, 
					anweisung5Button, anweisung5ResetButton, abbruchButton, startButton;
	
	
	/**
	 * JTextArea = Komponente zur Anzeige und Eingabe von mehrzeiligen Texten<br>
	 * 'ta' dient hier nur zur Anzeige der Position des Cursors beim Mausklick
	 */
	private JTextArea ta;
	
	
	/**
	 * Zeichenkette - bewirkt einen Zeilenumbruch in der TextArea 'ta'
	 */
	private static final String NEWLINE = System.getProperty("line.separator");
	
	
	/**
	 * JTextField = stellt ein einzeiliges Textfeld zur Eingabe von Daten dar.<br>
	 * In 'anweisung3TextField' wird der Ma�stab in Meter der Grundst�ckskarte eingetragen 
	 */
	private JTextField anweisung3TextField;
	
	
	/**
	 * ImageIcon ist eine Implementierung des Interface Icon, mit der aus einer gif- oder jpeg-Datei
	 * ein Icon erzeugt werden kann.<br>
	 * 'fotoBaugrundst�ck' = Luftaufnahme der Grundst�cksfl�che, auf dem die neue FH gebaut werden soll.
	 */
	public ImageIcon fotoBaugrundst�ck;
	
	
	/**
	 * Der Pfad der ausgew�hlten Bilddatei
	 */
	public static String pfad;
	
	
	/**
	 * Die ArrayList speichert die Koordinatenpunkte des Mauszeigers, wenn auf dem JLabel 'imageLabel', 
	 * welches als Icon die Luftaufnahme der Grundst�cksfl�che h�lt, geclickt wird.
	 */
	private ArrayList <Point> coordsArrayList = new ArrayList <Point>();
	
	
	/**
	 * Das Array h�lt die Koordinaten der vom User gew�hlten Eckpunkte der Baufl�che 
	 */
	public Point[] coordsArray;
	
	
	/**
	 * xcoordsPixel h�lt die x-Koordinaten der gew�hlten Eckpunkte in Pixel
	 */
	private int[] xcoordsPixel;
	
	
	/**
	 * ycoordsPixel h�lt die y-Koordinaten der gew�hlten Eckpunkte in Pixel
	 */
	private int[] ycoordsPixel;
	
	
	/**
	 * xcoordsMeter h�lt die x-Koordinaten der gew�hlten Eckpunkte in Meter
	 */	
	private float[] xcoordsMeter;
	
	
	/**
	 * ycoordsMeter h�lt die y-Koordinaten der gew�hlten Eckpunkte in Meter
	 */	
	private float[] ycoordsMeter;
	
	
	/**
	 * Der Fakor, um von Pixel in Meter umzurechnen.<br>
	 * Beispiel: der auf dem Foto abgebildete Ma�stab ist 67 Pixeln lang und entspricht 100 Metern.<br>
	 * Dann ist Faktor = 100 : 67 = 1,49. Ein Pixel entspricht also 1,49 Metern.
	 */
	public float Faktor;
	
	
	/**
	 * Die Breite des Attributs 'fotoBaugrundst�ck' in Metern
	 */
	public float fotobreiteMeter;
	
	
	/**
	 * Die H�he des Attributs 'fotoBaugrundst�ck' in Metern 
	 */
	public float fotohoeheMeter;
	
	
	/**
	 * Die x-Koordinate des linken Ma�stabs-Eckpunkt 
	 */
	private int xminMa�stab;
	
	
	/**
	 * Die y-Koordinate des rechten Ma�stabs-Eckpunkt
	 */
	private int xmaxMa�stab;
		
	
	/**
	 * Ma�stab in Pixel = xmaxMa�stab minus xminMa�stab 
	 */
	private int ma�stabPixel;
	
	
	/**
	 * Ma�stab in Meter = Faktor * ma�stabPixel 
	 */
	private int ma�stabMeter;
	
	
	/**
	 * x-Koordinate der Position des Mauszeigers beim Klick
	 */
	private int mouseX;
	
	
	/**
	 * y-Koordinate der Position des Mauszeigers beim Klick
	 */
	private int mouseY;
	
	
	/**
	 * Graphics: Javas Implementierung eines Device-Kontexts (auch Grafikkontext genannt).<br>
	 * Ein Device-Kontext kann als eine Art universelles Ausgabeger�t angesehen werden, 
	 * das elementare Funktionen zur Ausgabe von farbigen Grafik- und Schriftzeichen zur Verf�gung stellt. 
	 */
	private Graphics g;
	
	
	/**
	 * Das Color-Objekt wird dem Graphics-Objekt 'g' zugewiesen und bestimmt so die Farbe f�r Grafik und Schrift
	 */
	private Color Stiftfarbe = Color.YELLOW;
	
	
	/**
	 * Die Klasse Controller erzeugt ein Flaecheneditor-Objekt und wartet solange in einer while-Schleife
	 * wie der Zustand von 'starteVisualisierung' false ist.
	 * Erst wenn der User im Flaecheneditor den Start-Button dr�ckt, geht 'starteVisualisierung auf true
	 * und der Controller f�hrt mit seinen Anweisungen fort.
	 */
	public boolean starteVisualisierung = false;
	
	
	
	// Konstruktor
	
	
	/**
	 * Konstruktor<br>
	 * - erzeugt ein neues Objekt vom Typ Flaecheneditor<br>
	 * - ruft mit super(name) den entsprechenden Konstrukor der Elternklasse JFrame auf<br>
	 * - erzeugt mit setJMenuBar eine neue Men�leiste im Hauptfenster<br>
	 * - �ndert das Look-and-Feel (=Aussehen und Bedienung einer Anwendung) auf das Windows-Look-and-Feel<br>
	 * - erzeugt ein Objekt vom Typ Hintergrund<br>
	 * - legt das Layout der ContentPane fest<br>
	 * - f�gt der ContentPane neue Komponenten hinzu, durch Aufruf der Methode neustart()<br>
	 * - legt verschiedene Anzeigeparameter des Hauptfenster fest<br>
	 */
	public Flaecheneditor(){
		super("Flaecheneditor"); // Titel des Fensters: Flaecheneditor
		
		
		// neue Menuleiste erstellen		
		this.setJMenuBar(new Menuleiste(this)); 
		
		
		// Windows Look-and-Feel setzen
		try {
			String plaf = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";		   	 
		    UIManager.setLookAndFeel(plaf);
		    SwingUtilities.updateComponentTreeUI(this);
		}catch (UnsupportedLookAndFeelException e) {
			System.err.println(e.toString());
		}catch (ClassNotFoundException e) {
			System.err.println(e.toString());
		}catch (InstantiationException e) {
		    System.err.println(e.toString());
		}catch (IllegalAccessException e) {
		    System.err.println(e.toString());
		}
		
		
		// graues, displayf�llendes HintergrundObjekt erzeugen
		Hintergrund hg = new Hintergrund(); 
		hg.setBackground(Color.gray);
		hg.setLocation(new Point(0,0));
		hg.setSize(hg.getToolkit().getScreenSize());
		hg.setVisible(true);
						
		
		// Das Hauptfenster hat ein BorderLayout mit vertikalem Abstand von 20 Pixeln zwischen den Dialogobjekten.
		// Das BorderLayout teilt den Bildschirm in die f�nf Bereiche North, EAST, SOUTH; WEST und CENTER auf.
	    // Nord- und S�delement behalten ihre gew�nschte H�he, werden aber auf die volle Fensterbreite skaliert.
	    // Ost- und Westelement behalten ihre gew�nschte Breite, werden aber in der H�he so skaliert, 
		// dass sie genau zwischen Nord- und S�delement passen.
	    // Das Mittelelement wird in der H�he und Breite so angepasst, dass es den verbleibenden freien Raum einnimmt. 
		cp.setLayout(new BorderLayout(0, 20)); 
		
		
		// neue Komponenten erzeugen und der ContentPane zuweisen
		this.neustart();
		
					
		// Anzeigeparameter des Hauptfensters festlegen
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Anwendung soll beim Schlie�en beendet werden
		this.setResizable(false); // Fenster soll nicht maximiert werden k�nnen
		this.setSize(740,600); // Startgr��e des Hauptfensters
		this.setLocationRelativeTo(null); // Fenster in der Mitte des Bildschirms �ffnen		
		this.setVisible(true); // Fenster soll sichtbar sein
	}
		
	
	/**
	 * Die Methode initialisiert die diversen Panel- und Labelelemente auf der ContentPane. <br>
	 * Die ContentPane des Hauptfensters hat ein BorderLayout und besitzt drei Panel: <br>
	 * - NORTH: imagecoordPanel<br>
	 * - CENTER: anweisungsPanel<br>
	 * - SOUTH: abbruchStartPanel<br>
	 * imagecoordPanel hat ein BorderLayout und besitzt 2 Komponenten: imageLabel und koordinatenPanel <br>
	 * anweisungsPanel hat ein GridLayout und besitzt 6 Komponenten: anweisung1Panel,...,anweisung6Panel<br>
	 * abbruchStartPanel hat ein FlowLayout und besitzt 2 Komponenten: abbruchButton und startButton<br>
	 * Die Labeltexte und Buttons, also die eigentlichen Bedienungsanweisungen des Editors, 
	 * werden jedoch erst Schritt f�r Schritt durch Best�tigen der jeweiligen Ok-Button gesetzt. 
	 * Nur Text und Button der ersten Anweisung werden direkt gesetzt und auf dem Hauptfenster dargestellt.
	 */
	public void neustart(){		
		
		//####################BorderLayout.NORTH  - imagecoordsPanel###############		
		imageLabel = new JLabel(); // Diesem Label wird das Foto (Icon) des Grundst�cks zugef�gt		
        imageLabel.addMouseListener(new MyMouseAdapter()); // als Parameter wird ein MouseListener-Objekt �bergeben,
        												   // indem die lokale Klasse MyMouseAdapter aufgerufen wird.
				
		koordinatenPanel = new JPanel(); // das Panel h�lt die TextArea f�r die Koordinatenanzeige
		koordinatenPanel.setLayout(new FlowLayout());
		ta = new JTextArea(4,30);
		ta.setEditable(false);		
		koordinatenPanel.add(new JScrollPane(ta));
				
		imagecoordsPanel = new JPanel(new BorderLayout(0, 10));
		imagecoordsPanel.add(imageLabel, BorderLayout.CENTER);
		imagecoordsPanel.add(koordinatenPanel, BorderLayout.SOUTH);
		
		
		
		//####################BorderLayout.CENTER - anweisungsPanel################		
		// Bedienungsanweisungen des Fl�cheneditors		
		// Anweisung 1
		anweisung1Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		anweisung1Label = new JLabel("W�hlen sie ihr Grundst�ck ");
		anweisung1Label.setFont(new Font("SansSerif", Font.BOLD, 15));
		anweisung1Button = new JButton("�ffnen"); // �ffnet den JFileChooser
		anweisung1Button.addActionListener(this);
		anweisung1Panel.add(anweisung1Label);
		anweisung1Panel.add(anweisung1Button);		   
		
		// Anweisung 2
		anweisung2Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		anweisung2Label = new JLabel("");		
		anweisung2Panel.add(anweisung2Label);	
		
		// Anweisung 3
		anweisung3Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		anweisung3Label = new JLabel("");
		anweisung3Panel.add(anweisung3Label);
		
		// Anweisung 4
		anweisung4Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		anweisung4Label = new JLabel("");
		anweisung4Panel.add(anweisung4Label);				
		
		// Anweisung 5
		anweisung5Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		anweisung5Label = new JLabel("");
		anweisung5Panel.add(anweisung5Label);
		
		// Anweisung 6
		anweisung6Panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
		anweisung6Label = new JLabel("");
		anweisung6Panel.add(anweisung6Label);
			
		anweisungsPanel = new JPanel(new GridLayout(0,1)); 
		anweisungsPanel.add(anweisung1Panel);
		anweisungsPanel.add(anweisung2Panel);
		anweisungsPanel.add(anweisung3Panel);
		anweisungsPanel.add(anweisung4Panel);
		anweisungsPanel.add(anweisung5Panel);
		anweisungsPanel.add(anweisung6Panel);

		
		
		
		//####################BorderLayout.SOUTH - abbruchStartPanel###############		
		abbruchButton = new JButton("Abbrechen"); // beendet das Programm
		abbruchButton.addActionListener(this);
		startButton = new JButton("Start"); // startet das Programm
		startButton.addActionListener(this);
		startButton.setHorizontalAlignment(JButton.RIGHT);
		startButton.setVerticalAlignment(JButton.BOTTOM);
		startButton.setEnabled(false);
		
		abbruchStartPanel = new JPanel();
		abbruchStartPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));		
		abbruchStartPanel.add(abbruchButton);
		abbruchStartPanel.add(startButton);
		//#########################################################################
		
		
		// Komponenten der ContentPane hinzuf�gen
		cp.add(imagecoordsPanel, BorderLayout.NORTH);
		cp.add(anweisungsPanel, BorderLayout.CENTER);
		cp.add(abbruchStartPanel, BorderLayout.SOUTH);
		Border bd = BorderFactory.createEmptyBorder(20, 20, 20, 20); // leerer Rahmen von 20 Pixeln zu allen Seiten 
		((JPanel)cp).setBorder(bd);	
						
		
		this.setSize(740,600);
		this.setLocationRelativeTo(null);
	}
	
	
	/**
	 * Diese Methode muss f�r das Interface 'ActionListener' implementiert werden. <br>
	 * In den einzelnen if-Schleifen wird gepr�ft, ob 'cmd' gleich einem der Namen der Button bzw. Men�eintr�ge ist.
	 * Ist dies der Fall, hat ein entsprechendes Ereignis (Buttonklick, Textfeldeingabe etc.) stattgefunden, 
	 * und es wird die jeweilige Methode aufgerufen, welche die auf das Ereignis reagierenden Anweisungen enth�lt.
	 * Nur beim Bet�tigen der Men�eintr�ge "Schwarz", "Rot" und "Gelb" wird direkt die neue Stiftfarbe gesetzt.
	 * @param cmd Der aktuelle ActionEvent als String
	 */
	public void actionPerformed(ActionEvent event){
				
		String cmd = event.getActionCommand();		
		
		
		// Aktionen f�r Button oder Menueintrag "�ffnen"
		if (cmd.equals("�ffnen")){//			
			oeffnen();
		}
		
		 // Aktionen f�r Button "Ok 1"
	    if (cmd.equals("Ok 1")){
	    	ok1();
	    }
	    
	    // Aktionen f�r Button "Ok 2"
	    if(cmd.equals("Ok 2")){
	    	ok2();
	    }    
	    
	    // Aktionen f�r Button "Ok 3"
	    if (cmd.equals("Ok 3")){   
	    	ok3();    	
	    }	      		
	
	    // Aktionen f�r Button "Reset 1" und "Reset 2"
	    if ((cmd.equals("Reset 1")) || (cmd.equals("Reset 2"))){	    	
	    	reset();
	    }	   
	    
	    // Aktionen f�r Button "Start"
	    if(cmd.equals("Start")){
	    	start();
	    }
	    
	    // Aktionen f�r Men�eintrag "Neu"
	    if(cmd.equals("Neu")){
	    	neu();
	    }
	    
	    // Aktionen f�r Menueintrag "Beenden" oder Button "Abbrechen"
		if ((cmd.equals("Beenden")) || (cmd.equals("Abbrechen"))){
			beenden();
		}
		
		// Aktionen f�r Menueintrag "Hilfe"
		if (cmd.equals("Hilfe")){
			hilfe();
		}
		
		// Aktionen f�r RadioButton "Schwarz"
		if(cmd.equals("Schwarz")){
			Stiftfarbe = Color.BLACK;
		}
		
		// Aktionen f�r RadioButton "Rot"
		if(cmd.equals("Rot")){
			Stiftfarbe = Color.RED;
		}
		
		// Aktionen f�r RadioButton "Gelb"
		if(cmd.equals("Gelb")){
			Stiftfarbe = Color.YELLOW;
		}		
	}
	
	
	/**
	 * Die Methode wird aufgerufen, wenn der Button oder Men�eintrag "�ffnen" gedr�ckt wird.<br>
	 * In dem sich �ffnenden Dialogfenster (JFileChooser) muss zun�chst die jpg- oder gif-Datei ausgew�hlt werden,
	 * welche als Grundst�cksfl�che fungieren soll. Danach werden durch Aufruf der Methode showAnweisung2() 
	 * der Text und die Button von Anweisung 2 aktiv, und auf dem Hauptfenster dargstellt.
	 */
	public void oeffnen(){
		JFileChooser chooser = new JFileChooser();
		// nur jpg- und gif-Dateien sollen ausw�hlbar sein
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("JPG & GIF Images", "jpg", "gif"); 
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(new Container());
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION){
	        fotoBaugrundst�ck = new ImageIcon(chooser.getSelectedFile().getAbsolutePath()); // Bilddatei zuweisen
	        pfad = chooser.getSelectedFile().getAbsolutePath(); // der Pfad der Bildatei
	        
	        // Die if-Schleide fragt die Hoehe und Breite der Bilddatei ab.
	        // Diese darf maximal 75% der jeweiligen Monitor-Aufl�sung betragen, 
	        // da ansonsten das Layout des Hauptfensters durcheinander ger�t und Button verdeckt werden.
	        if((fotoBaugrundst�ck.getIconHeight() > ((getToolkit().getScreenSize().height) / 1.8)) 
	        		|| (fotoBaugrundst�ck.getIconWidth() > (getToolkit().getScreenSize().width) / 1.5)){
	        	modalDialog d; // neues modales Dialogelement; geht auf, wenn Bilddatei zu gro� ist
		    	d = new modalDialog(this,"Bitte w�hlen sie eine Bilddatei mit kleinerer Aufl�sung!");
		    	d.setVisible(true);
		    	neu(); // setzt alle Attribute auf ihren Anfangswert zur�ck
				oeffnen(); // startet erneut die Anweisungen f�r Button "Offnen"
	        }
	        
	        imageLabel.setIcon(fotoBaugrundst�ck); // Bilddatei dem Label zuweisen	
	        imageLabel.setBorder(BorderFactory.createRaisedBevelBorder()); // erh�hte Umrahmung f�r Bilddatei
		    
	        this.pack();
	        this.setLocationRelativeTo(null); // Hauptfenster zentrieren
	    }
	   	  
	    // Die if-Abfrage ist n�tig, damit die Button von Anweisung2 nicht eventuell doppelt erzeugt werden.
	    // Dies k�nnte n�mlich passieren, wenn der User das Dialogfenster des JFileChooser abbricht.
	    // Die Komponenten von Anweisung2 werden also erst angezeigt, wenn auch wirklich eine Bilddatei
	    // ausgew�hlt und der Button nicht bereits erzeugt wurde.
	    if((anweisung2Button == null) && (fotoBaugrundst�ck != null)){
	    	 // Komponenten von Anweisung2 initialisieren und auf dem Hauptfenster darstellen
	    	showAnweisung2();
	    }
	}
	
	
	/**
	 * Die Methode stellt die Komponenten von Anweisung2 auf dem Hauptfenster dar:<br>
	 * - Labeltext: "Markieren sie auf der Karte den linken und rechten Endpunkt des Meterma�stabs"<br>
	 * - Button: "Ok 1"<br>
	 * - Button: "Reset 1"<br>
	 * Zudem wird der �ffnen-Button von Anweisung1 deaktiviert.
	 */
	public void showAnweisung2(){		
	    	anweisung2Label.setFont(new Font("SansSerif", Font.BOLD, 15));
	    	anweisung2Label.setText("Markieren sie auf der Karte den linken und rechten Endpunkt des Meterma�stabs ");	
	    	anweisung2Button = new JButton("Ok 1"); // "Ok 1" Button
	    	anweisung2Button.addActionListener(this); 
	    	anweisung2ResetButton = new JButton("Reset 1"); // "Reset 1" Button
	    	anweisung2ResetButton.addActionListener(this);
	    	anweisung2Panel.add(anweisung2Button);
	    	anweisung2Panel.add(anweisung2ResetButton);
	    	anweisung1Button.setEnabled(false); // Der "�ffnen"-Button von Anweisung1 wird deaktiviert	    		    		
	}
	
	
	/**
	 * Die Methode wird aufgerufen, wenn der Button "Ok 1" von Anweisung2 gedr�ckt wird,
	 * d.h., der User die beiden Eckpunkte des Ma�sta		bs markiert hat.<br>
	 * Die Methode mouseReleased() der lokalen Klasse MyMouseAdapter sorgt daf�r, dass die Eckpunkte
	 * als Point-Objekte in die ArrayList 'coordsArrayList' gespeichert und als rote Punkte auf
	 * dem Label gezeichnet werden.
	 * Aus diesen beiden Eckpunkten wird der Ma�stab in Pixeln berechnet. Dieser gibt an,
	 * wie viele Pixel zwischen den x-Koordinaten der beiden Eckpunkte liegen.<br> 
	 * Zur Bestimmung wird zun�chst die Differenz der x-Koordinaten der beiden Eckpunkte berechnet, 
	 * und aus diesem Ergebnis dann der Betrag gebildet.
	 * Durch die Bildung des Betrages ist es egal, welchen Eckpunkt der User zuerst markiert.<br>
	 * Nachdem der Ma�stab berechnet wurde, werden alle Punkte aus der ArrayList gel�scht.<br>
	 * Sind diese Anweisungen abgearbeitet, werden durch Aufruf der Methode showAnweisung3()
	 * das Textfeld und der Button von Anweisung3 aktiv, und auf dem Hauptfenster dargstellt.<br>
	 */
	public void ok1(){
		
		// Um den Ma�stab in Pixeln zu berechnen, werden die ersten beiden Eintr�ge der ArrayList benutzt.
		// Hat der User jedoch mehr als 2 Punkte markiert, ist das Ergebnis ein falscher Ma�stab.
		// Die if-Schleife fragt daher ab, wie viele Punkte markiert wurden. Wurden mehr als zwei Punkte 
		// markiert, geht ein modales Dialogfenster mit entsprechendem Hinweis auf.
		if(coordsArrayList.size() <= 2){
			// die x-Koordinate des ersten Punktes in der ArrayListe ist xmin des Ma�stabs in Pixeln
	    	xminMa�stab = (int) coordsArrayList.get(0).getX();
	    	// die x-Koordinate des zweiten Punktes in der ArrayListe ist xmin des Ma�stabs in Pixeln
	    	xmaxMa�stab = (int) coordsArrayList.get(1).getX();
	    	ma�stabPixel = Math.abs(xmaxMa�stab - xminMa�stab);
	    	
	    	System.out.println("xMin: " + xminMa�stab + "\txMax: " + xmaxMa�stab);
	    	System.out.println("Ma�stab in Pixeln: " + ma�stabPixel);
	    	
	    	coordsArrayList.clear(); // Inhalt (=Punkte) der ArrayList l�schen
	    	
	    	// Komponenten von Anweisung3 initialisieren und auf dem Hauptfenster darstellen
	        showAnweisung3();	    	
		}
		else{
			System.out.println("Zu viele Punkte markiert!");
			// neues modales Dialogelement; geht auf, wenn mehr als 2 Punkte markiert werden
			modalDialog d; 
			d = new modalDialog(this,"Bitte markieren sie nur die beiden Endpunkte des Ma�stabs!");
			d.setVisible(true);
    		reset();
			showAnweisung2();
		}		
	}
	
	
	/**
	 * Die Methode stellt die Komponenten von Anweisung3 auf dem Hauptfenster dar:<br>
	 * - Labeltext: "Geben sie den Ma�stab in Meter ein "<br>
	 * - Textfeld<br>
	 * - Button: "Ok 2"<br>
	 * Zudem werden die Button von Anweisung2 deaktiviert.
	 */
	public void showAnweisung3(){
		anweisung3Label.setFont(new Font("SansSerif", Font.BOLD, 15));
    	anweisung3Label.setText("Geben sie den Ma�stab in Meter ein ");			
		anweisung3TextField = new JTextField(6);
		anweisung3Button = new JButton("Ok 2");
		anweisung3Button.addActionListener(this);
		anweisung3Panel.add(anweisung3TextField);
		anweisung3Panel.add(anweisung3Button);
		anweisung3TextField.requestFocus(); // das Eingabefeld erh�lt den Fokus
		anweisung2ResetButton.setEnabled(false); // "Ok1"-Button von Anweisung2 deaktivieren
        anweisung2Button.setEnabled(false); // "Reset"-Button von Anweisung2 deaktivieren
	}
	
	
	/**
	 * Die Methode wird aufgerufen, wenn der "Ok 2"-Button von Anweisung3 gedr�ckt wird,
	 * d.h. der User den Ma�stab in Metern in das Textfeld eingegeben hat.<br>
	 * Der Ma�stab in Meter gibt an, wie vielen Metern die Pixelstrecke zwischen den Ma�stabsendpunkten
	 * entsprechen.<br>
	 * Durch den Aufruf der Methode showAnweisung4und5() werden die Anweisungen 4 und 5 mit ihren
	 * Komponenten initialisiert und auf dem Hauptfenster dargestellt.
	 */
	public void ok2(){
		this.repaint(); // l�scht die beiden Eckpunkte des Ma�stabs auf dem Icon "fotoBaugrundst�ck" 
    	ta.setText(""); // l�scht den Inhalt der TextArea "ta"
    	
		ma�stabMeter = Integer.parseInt(anweisung3TextField.getText()); // String aus dem Textfeld in Integer umwandeln
        System.out.println("Ma�stab in Meter: " + ma�stabMeter);   
        
     // Komponenten von Anweisung4 und Anweisung5 initialisieren und auf dem Hauptfenster darstellen
        showAnweisung4und5();
	}
	
	
	/**
	 * Die Methode stellt die Komponenten von Anweisung4 und Anweisung5 auf dem Hauptfenster dar:<br>
	 * Anweisung4:<br>
	 * - Labeltext: "-- ...Pixel entsprechen ungef�hr...Metern -- "<br>
	 * Anweisung5:<br>
	 * - Labeltext: "Markieren sie auf der Karte die Eckpunkte des Baugrundst�cks "<br>
	 * - Button: "Ok 3"<br>
	 * - Button: "Reset 2"<br>
	 * Zudem wird der Button und das Textfeld von Anweisung3 deaktiviert.
	 */
	public void showAnweisung4und5(){
		anweisung4Label.setFont(new Font("SansSerif", Font.PLAIN, 15));
        anweisung4Label.setText("-- " + ma�stabPixel + " Pixel entsprechen ungef�hr " + ma�stabMeter + " Metern --");
        
        anweisung3TextField.setEnabled(false);
        anweisung3Button.setEnabled(false);
        anweisung5Label.setFont(new Font("SansSerif", Font.BOLD, 15));
    	anweisung5Label.setText("Markieren sie auf der Karte die Eckpunkte des Baugrundst�cks ");			
    	anweisung5Button = new JButton("Ok 3");
    	anweisung5Button.addActionListener(this);
    	anweisung5ResetButton = new JButton("Reset 2");
    	anweisung5ResetButton.addActionListener(this);
    	anweisung5Panel.add(anweisung5Button);
    	anweisung5Panel.add(anweisung5ResetButton);		
	}
	
	
	/**
	 * Die Methode wird aufgerufen, wenn der "Ok 3"-Button von Anweisung5 gedr�ckt wird,
	 * d.h. der User die Eckpunkte des Baugrundst�cks auf der Karte markiert hat.<br>
	 * Diese sollen nun miteinander verbunden, und die daraus entstehende Fl�che farbig gef�llt werden.<br>
	 * Alle markierten Eckpunkte sind in der ArrayList coordsArrayList gespeichert.
	 * Die Methode fillPolygon() aus der Klasse Graphics braucht jedoch als Parameter eindimensionale int-Arrays.
	 * Daher werden die Arrays xcoordsPixel und ycoordsPixel mit den jeweiligen x- und y-Koordinaten der Punkte 
	 * aus der ArrayList gef�llt. Das Point-Array coordsArray wird ebenfalls mit den Punkten aus der ArrayList
	 * gef�llt. coordsArray wird im weiteren Verlauf von der Klasse Gebaude ben�tigt.<br>
	 * Die int-Arrays mit den x- und y-Koordinaten werden dann der Methode fillPolygon() �bergeben,
	 * welche die Fl�che innerhalb der Eckpunkte in der gew�hlten Stiftfarbe darstellt.<br>
	 * Durch Aufruf der Methode showAnweisung6() werden die Komponenten von Anweisung6 aktiv, und auf
	 * dem Hauptfenster dargestelt. 
	 */
	public void ok3(){
		xcoordsPixel = new int[coordsArrayList.size()];
    	ycoordsPixel = new int[coordsArrayList.size()];
    	coordsArray = new Point[coordsArrayList.size()];
    	
    	// Punkte bzw. Koordinaten der coordsArrayList in die Arrays coordsArray, xccordsPixel, ycoordsPixel �bertragen 
    	for(int i = 0; i < coordsArray.length; i++){
    		coordsArray[i] = coordsArrayList.get(i);
    		xcoordsPixel[i] = (int) (coordsArrayList.get(i).getX());
    		ycoordsPixel[i] = (int) (coordsArrayList.get(i).getY());
    	}	    	
    	
    	//Test, ob die Punkte aus coordsArray mit den Punkten aus der coordsArrayList �bereinstimmen
//    	System.out.println("L�nge coordsArrayList: " + coordsArrayList.size());
//    	System.out.println("L�nge coordsArray: " + coordsArray.length);    	
//    	for(int i = 0; i < coordsArray.length; i++){
//    		System.out.println(coordsArray[i]);
//    	}
    	
    	g = imageLabel.getGraphics();
    	g.setColor(Stiftfarbe);	
    	g.fillPolygon(xcoordsPixel, ycoordsPixel, xcoordsPixel.length); // Grundst�ck wird mit ausgef�llter Fl�che dargestellt
    	
    	 // Komponenten von Anweisung6 initialisieren und auf dem Hauptfenster darstellen
    	showAnweisung6();
	}	
	
	
	/**
	 * Die Methode stellt die Komponenten von Anweisung6 auf dem Hauptfenster dar:<br>
	 * - Labeltext: "Dr�cken sie [Start] und w�hlen sie im darauffolgenden Auswahlfenster die Konfigurationsdatei"<br>
	 * Zudem werden die Button von Anweisung5 deaktiviert und der "Start"-Button aktiviert.
	 */
	public void showAnweisung6(){
		anweisung6Label.setFont(new Font("SansSerif", Font.BOLD, 15));
        anweisung6Label.setText("Dr�cken sie [Start] und w�hlen sie im darauffolgenden Auswahlfenster die Konfigurationsdatei ");
        anweisung5Button.setEnabled(false);
        anweisung5ResetButton.setEnabled(false);
		startButton.setEnabled(true);		
	}
	
	
	/**
	 * Die Methode setzt die zum Zeitpunkt ihres Aufrufs gesetzten Werte zur�ck.<br>
	 * Genauer gesagt werden alle Punkte in der ArayList gel�scht, das Hauptfenster neu �bermalt
	 * und der Text in der TextArea gel�scht.
	 */
	public void reset(){
		coordsArrayList.clear(); // l�scht die Punkte in der ArrayList	
    	this.repaint(); // l�scht das Polygon auf dem Icon "fotoBaugrundst�ck"
    	ta.setText(""); // l�scht den Inhalt der TextArea "ta"
	}		
	
	
	/**
	 * Die Methode stellt den Flaecheneditor auf den Anfangszustand zur�ck.
	 */
	public void neu(){
		reset();
		fotoBaugrundst�ck = null;
		anweisung2Button = null;
		imageLabel.removeAll();
		imagecoordsPanel.removeAll();
		anweisungsPanel.removeAll();
		abbruchStartPanel.removeAll();
		cp.removeAll();
		neustart();
	}	
	
	
	/**
	 * Die Methode schlie�t das ge�ffnete Fenster und beendet das laufende Programm.
	 */
	public void beenden(){
		setVisible(false);// macht das Dialogfenster unsichtbar
		dispose(); //gibt die zugeordneten Windows-Ressourcen frei 
				   //und entfernt das Fenster aus der Owner-Child-Registrierung. 
	    System.exit(0); // beendet das laufende Programm
	}	
	
	
	/**
	 * Die Methode �ffnet ein pdf-Dokument, in dem wichtige Informationen zum Programm stehen.
	 */
	public void hilfe(){
		// Die Methode ben�tigt eine IOException
		try{File myFile = new File("bin/architektur/data/Anleitung.pdf");
	       System.out.println(myFile.getAbsolutePath());       
	       Desktop.getDesktop().open(new File(myFile.getAbsolutePath()));
		}
		catch(Exception e){				
		}
		
	}	
	
	
	/**
	 * Die Methode wird aufgerufen, wenn der User den "Start"-Button dr�ckt.<br>
	 * Zun�chst werden durch den Aufruf der Methode PixelToMeter() die Koordinaten der markierten Eckpunkte
	 * des Baugrundst�cks von Pixeln in Meter umgerechnet, da die Klasse Geb�ude diese Informationen braucht.<br>
	 * Danach wird das Attribut starteVisualisierung auf true gesetzt, wodurch die Instanz der Klasse Controller
	 * in ihrem Ablauf fortfahren kann. 
	 */
	public void start(){
		PixelToMeter(xcoordsPixel, ycoordsPixel);
    	starteVisualisierung = true; // hiermit wird die while-Schleife im Controller beendet, und jME dadurch gestartet
    	setVisible(false); // schlie�t den Fl�cheneditor, damit dieser im weiteren Programm nicht st�rt
	}	
			  
	
	/**
	 * Die Methode rechnet die x- und y-Koordinaten der markierten Punkte auf der Bilddatei von Pixel in Meter um,
	 * da diese Informationen in der Klasse Gebaeude ben�tigt werden.<br>
	 * Zun�chst wird der Umrechnungsfaktor berechnet. Dazu wird der Quotient aus dem Ma�stab in Metern und
	 * dem Ma�stab in Pixeln gebildet.<br> Danach werden von den Pixelpunkten die x- und y-Koordinaten 
	 * mit dem Faktor multipliziert und somit in Meterpunkte umgewandelt. Diese werden dann in den float-Arrays 
	 * xcoordsMeter und ycoordsMeter gespeichert.<br>
	 * Zuletzt wird mit dem Faktor die Breite und H�he der Bilddatei in Metern berechnet. 
	 * @param xPixel Die x-Koordinaten der markierten Eckpunkte in Pixel
	 * @param yPixel Die y-Koordinaten der markierten Eckpunkte in Pixel
	 */
	public void PixelToMeter(int[] xPixel, int[] yPixel){
		
		// Bsp.: ma�stabPixel = 67, ma�stabMeter = 100: dann entsprechen einem Pixel 1,49 Meter (=100:67)
		Faktor = ((float) ma�stabMeter) / ((float) ma�stabPixel);
		System.out.println("Umrechnungsfaktor von Pixel in Meter: " + Faktor);
		
		xcoordsMeter = new float[xPixel.length];
		ycoordsMeter = new float[yPixel.length];
		
		for(int i = 0; i < xcoordsMeter.length; i++){			
			xcoordsMeter[i] = xPixel[i] * Faktor;
			ycoordsMeter[i]	= yPixel[i] * Faktor;
			System.out.println("xMeter: " + xcoordsMeter[i] + "\tyMeter: " + ycoordsMeter[i]);
		}
		
		fotobreiteMeter = (fotoBaugrundst�ck.getIconWidth() * Faktor);
		fotohoeheMeter = (fotoBaugrundst�ck.getIconHeight() * Faktor);
		
		
		System.out.println("Breite der Bilddatei in Pixel: " + fotoBaugrundst�ck.getIconWidth() + 
						   "\tH�he der Bilddatei in Pixel: " + fotoBaugrundst�ck.getIconHeight());
		System.out.println("Breite der Bilddatei in Meter: " + fotobreiteMeter + 
						   "\tH�he der Bilddatei in Meter: " + fotohoeheMeter);
	}


	
//########################################lokale Klasse Anfang######################################################	
	/**
	 * Die lokale Klasse erweitert die Klasse JMenuBar und erzeugt f�r das Hauptfenster 
	 * eine Men�zeile mit verschiedenen Men�s und entsprechenden Men�eintr�gen.
	 */
	class Menuleiste extends JMenuBar{
		
		
		/**
		 * Der Konstruktor erzeugt eine neue Instanz einer Men�zeile.<br>
		 * Die Men�zeile hat die Men�s "Datei" und "Optionen".<br>
		 * Neue Men�eintr�ge werden durch die Methode addNewMenuItem() hinzugef�gt
		 * @param listener Der ActionListener des Hauptfensters
		 */
		public Menuleiste(ActionListener listener){
			
		    JMenu m;
		
		    //Men� "Datei"
		    m = new JMenu("Datei");
		    addNewMenuItem(m, "Neu", listener); // neuen Men�eintrag erzeugen
		    addNewMenuItem(m, "�ffnen", listener);
		    m.addSeparator(); // Trennlinie zwischen "�ffnen" und "Beenden"
		    addNewMenuItem(m, "Beenden", listener);
		    this.add(m); // this = Objekt Flaecheneditor, vom Typ JFrame
		    
		
		    //Men� "Optionen"
		    m = new JMenu("Optionen");
		    createToolsSubMenu(m, "Farbe", listener); // neues Untermen� erzeugen
		    m.addSeparator();
		    addNewMenuItem(m, "Hilfe", listener);
		    this.add(m);
		}
	
		
		/**
		 * Die Methode erzeugt einn neuen Men�eintrag f�r ein Men� der Men�zeile.<br>
		 * @param menu das zugeh�rige Men� (in diesem Fall "Datei" oder "Optionen") 
		 * @param name der Name des Men�eintrags
		 * @param listener der ActionListener des Hauptfenster
		 */
		private void addNewMenuItem(JMenu menu, String name, ActionListener listener){
			
			JMenuItem menuItem;
			
			// Der Men�eintrag "�ffnen" soll zus�tzlich mit einem Icon dargestellt werden 
			if(name == "�ffnen"){				
				File myFile = new File("bin/architektur/data/OpenIcon.gif");
			    System.out.println(myFile.getAbsolutePath());       
				menuItem = new JMenuItem(name, new ImageIcon(myFile.getAbsolutePath()));	
				menuItem.setActionCommand(name);
				menuItem.addActionListener(listener);
				
				menu.add(menuItem);
			}
			else{
				menuItem = new JMenuItem(name);	
				menuItem.setActionCommand(name);
				menuItem.addActionListener(listener);
				
				menu.add(menuItem);
			}	   
		}
		
		
		/**
		 * Die Methode erzeugt ein Untermen� f�r den Men�eintrag "Farbe" des Men�s "Optionen".<br>
		 * Das Untermen� besteht aus einer Gruppe von drei RadioButton. Diese haben die Eigenschaft, 
		 * dass sie wahlweise an- oder ausgeschaltet werden k�nnen. In einer Gruppe von Radiobuttons 
		 * kann allerdings immer nur ein Button zur Zeit aktiviert sein, alle anderen sind deaktiviert.<br>
		 * Es kann also immer nur eine der Farben Schwarz, Rot und Gelb als Stiftfarbe ausgew�hlt werden.
		 */
		private void createToolsSubMenu(JMenu menu, String name, ActionListener listener){	  
			
		    ButtonGroup bg = new ButtonGroup();
		    
		    JMenuItem mi1 = new JRadioButtonMenuItem("Schwarz"); // neue Men�eintr�ge    
		    JMenuItem mi2 = new JRadioButtonMenuItem("Rot");	   
		    JMenuItem mi3 = new JRadioButtonMenuItem("Gelb", true);
		    
		    mi1.addActionListener(listener);
		    mi2.addActionListener(listener);
		    mi3.addActionListener(listener);
		    
		    bg.add(mi1);
		    bg.add(mi2);
		    bg.add(mi3);
		    
		    JMenu submenu = new JMenu(name); // neues Untermen�
		    submenu.add(mi1); // Men�eintr�ge dem Untermen� zuweisen
		    submenu.add(mi2);
		    submenu.add(mi3);
		    
		    menu.add(submenu); // Untermen� dem zugeh�rigen Men� zuweisen
		}	
	}
//#############################################lokale Klasse Ende##################################################		
		
	
	
//###############################################lokale Klasse Anfang##############################################	
	/**
	 * MyMouseAdapter ist eine lokale Klasse in 'Flaecheneditor' und erweitert die Klasse MouseAdapter.
	 * M�chte man in einem GUI (Graphical User Inerface) Mausevents (Mausklicks etc.) verarbeiten,
	 * so muss die Klasse MouseListener implementiert werden. Diese wiederum fordert, dass f�r alle 
	 * m�glichen Mouseevents die entsprechenden Methoden �berlagert werden m�ssen. 
	 * Bei einer lokalen Klasse, welche die Klasse MouseAdapter implementiert, reicht es dagegen aus,
	 * wenn nur die ben�tigten Methoden �berlagert werden.
	 * In diesem Fall ist das die Methode mouseReleased(), da das Programm nur Anweisungen braucht,
	 * f�r den Fall das auf imageLabel die linke Maustaste losgelassen wird.<br>
	 */
	class MyMouseAdapter extends MouseAdapter{
	   
		
		/**
	     * Die Methode beschreibt, was passiert, wenn die linke Maustaste auf imageLabel losgelassen wird.
	     * Zun�chst wird den Attributen mouseX und mouseY die jeweilige Koordinate der Mausposition �bergeben.
	     * Das Koordinatenpaar wird dann als String in der TextArea angezeigt, so dass der User einen �berblick
	     * hat, welche Koordinaten er markiert hat. Das Koordinatenpaar wird zus�tzlich einem Point-Objekt
	     * zugewiesen, welches wiederum in der ArrayList coordsArrayList gespeichert wird.<br>
	     * Mit Hilfe der Methode fillOval() der Klasse Graphics wird nun an besagter Koordinate auf dem imageLabel 
	     * (welches die Bilddatei anzeigt) ein Punkt mit festgelegtem Radius und Farbe gezeichnet.
	     * @param e Ein MouseEvent Objekt, hervorgerufen durch einen Mausklick auf imageLabel 
	     */
	    public void mouseReleased(MouseEvent e) {     
	    	
	    	mouseX = e.getX();
	    	mouseY = e.getY();
	    	int radius = 4; // Durchmesser der gezeichneten Eckpunkte
	    	     
	    	ta.append("Punkt: (" + mouseX + " , " + mouseY + ")" + NEWLINE); // Koordinaten in der TextArea anzeigen
	    	
	    	Point p = new Point(mouseX, mouseY); 
	    	coordsArrayList.add(p);
	
	    	g = imageLabel.getGraphics();
	    	g.setColor(Stiftfarbe);		   
	    	
	    	// zeichne einen Punkt mit einem Durchmesser von ( Pixeln an der aktuellen Cursorposition
	    	// Damit die Mitte des Punktes genau an der Spitze des Mauszeigers liegt, muss
	    	// jeweils der Radius von der x- und y-Koordinate abgezogen werden.
	    	g.fillOval(mouseX - radius, mouseY - radius, radius*2, radius*2); // (radius * 2) = Durchmesser
	    }    
	}
//#############################################lokale Klasse Ende##################################################
	

	
//#############################################lokale Klasse Anfang##################################################
	/**
	 * Die lokale Klasse modalDialog erweitert die Klasse JDialog und implementiert das Interface ActionListener.<br>
	 * Die Klasse erzeugt ein modales Dialogfenster. Modal hei�t, das alle anderen Komponenten 
	 * des Hauptfensters solange gesperrt bleiben, bis die Nachricht im Dialogfenster best�tigt wird.<br>
	 * In der Klasse Flaecheneditor gibt es zwei Ereignisse, bei denen ein modales Dialogfenster aufgeht:<br>
	 * - Wenn die ausgew�hlte Bilddatei eine zu hohe Aufl�sung aufweist<br>
	 * - Wenn mehr als 2 Punkte bei der Markierung der Ma�stabsendpunkte markiert werden.<br>
	 * In beiden F�llen geht ein Dialogfenster mit einem entsprechenden Hinweis auf, dass durch die Bet�tigung
	 * des Ok-Buttons best�tigt werden muss. 
	 */
	class modalDialog extends JDialog implements ActionListener{
		
		boolean result;
		
		
		/**
		 * Konstruktor f�r ein modales Dialogfenster
		 * Das Dialogfenster hat als Komponenten ein Label, welches die Nachricht f�r den User h�lt,
		 * und einen "OK"-Button, der zum Best�tigen des Dialogfenster da ist.
		 * @param Hauptfenster
		 * @param Mitteilung
		 */
		public modalDialog(JFrame Hauptfenster, String Mitteilung){
			super(Hauptfenster, "ACHTUNG", true); // Name des Dialogfensters
			this.setBackground(Color.lightGray);
			this.setLayout(new BorderLayout());
			
		
			JLabel l = new JLabel(Mitteilung); // Das Label h�lt die Nachricht des Dialogfensters
			l.setFont(new Font("SansSerif", Font.PLAIN, 15));
			l.setBorder(BorderFactory.createEtchedBorder());
			
			JPanel dialogPanel = new JPanel();
	    	dialogPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
	    	JButton button = new JButton("OK");
	    	button.addActionListener(this);
	    	dialogPanel.add(button);
	    	
	    	this.add(l, BorderLayout.CENTER);
	    	this.add(dialogPanel, BorderLayout.SOUTH);
	    	
	    	this.pack();	    	
			this.setResizable(false);
			this.setLocation(Hauptfenster.getWidth() / 2, Hauptfenster.getHeight() / 2); // Position des Dialogfensters
		}
		
		/**
		 * Diese Methode muss f�r das Interface 'ActionListener' implementiert werden. <br>
		 * Die Methode beinhaltet, dass das Dialogfenster geschlossen wird, sobald der "OK"-Button
		 * gedr�ckt wird.
		 */
		public void actionPerformed(ActionEvent event){
			result = event.getActionCommand().equals("OK");
			this.setVisible(false); // macht das Dialogfenster unsichtbar
			this.dispose(); //gibt die zugeordneten Windows-Ressourcen frei 
						   //und entfernt das Fenster aus der Owner-Child-Registrierung.			
		}
	}
//#############################################lokale Klasse Ende##################################################

	
	
//#############################################lokale Klasse Anfang##################################################
	/**
	 * Die lokale Klasse Hintergrund erweitert die Klasse Window und erzeugt ein rahmenloses Fenster.
	 */
	class Hintergrund extends Window{
		
		/**
		 * Konstruktor
		 * ruft Konstruktor der Elternklasse mit super() auf und �bergibt dabei ein Objekt vom Typ Frame.
		 */
		public Hintergrund(){
			super(new Frame());	
		}
	}
//#############################################lokale Klasse Ende##################################################
}