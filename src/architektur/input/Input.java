package architektur.input;

import java.awt.Container;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFileChooser;


/**
 * Input
 * Einlesen und Zuweisen der einzelnen Parameter aus der Textdatei FH_Daten.txt
 * @author Fabian Pflaum
 * @version 22.06.2009
 */


public class Input {


	/**
	 * Methode liest Daten aus der Textdatei FH_Daten ein und weisst diese den
	 * entsprechenden Attributen zu
	 */

	public FHdata readdata() {

		// Erzeugung eines FHdata Objektes mittels Default Konstruktor der
		// Klasse FHdata
		FHdata data;
		data = new FHdata();

		// Erzeugung eines Scanner-Objektes zum Einlesen der Textdatei FH_Daten
		/** scanner: Zum Einlesen des Eingabestroms als Textdatei*/
		Scanner scanner = new Scanner("");

		try {
			/** chooser: Objekt zum Angeben eines Pfades zur Konfigurationstextdatei*/
			JFileChooser chooser = new JFileChooser();
			
			/** returnVal: Speichert den Rückgabewert des  chooser*/
			int returnVal = chooser.showOpenDialog(new Container());
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				System.out.println("Sie haben diese Datei gewählt: "
						+ chooser.getSelectedFile().getAbsolutePath());
				//http://java.sun.com/developer/JDCTechTips/2004/tt1201.html#1
				scanner = new Scanner(chooser.getSelectedFile());
				scanner.useDelimiter(System.getProperty("line.separator")); 
				
				/** lineScanner1: Objekt das die erste Zeile der Eingabedatei haelt*/
						   Scanner lineScanner1 = new Scanner(scanner.next());
			       			  lineScanner1.useDelimiter("\\s*:\\s*");
				      		  lineScanner1.next();
						data.studentenanzahl = lineScanner1.nextInt();
						
				/** lineScanner2: Objekt das die zweite Zeile der Eingabedatei haelt*/	
					       Scanner lineScanner2 = new Scanner(scanner.next());
						      lineScanner2.useDelimiter("\\s*:\\s*");
						      lineScanner2.next();
						data.wc_student_faktor = lineScanner2.nextInt();
						
				/** lineScanner3: Objekt das die dritte Zeile der Eingabedatei haelt*/	
					       Scanner lineScanner3 = new Scanner(scanner.next());
						      lineScanner3.useDelimiter("\\s*:\\s*");
						      lineScanner3.next();
						data.hoersaal_student_faktor = lineScanner3.nextInt();
						
				/** lineScanner4: Objekt das die vierte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner4 = new Scanner(scanner.next());
						      lineScanner4.useDelimiter("\\s*:\\s*");
						      lineScanner4.next();
						data.seminarraum_student_faktor = lineScanner4.nextInt();
						
				/** lineScanner5: Objekt das die fünfte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner5 = new Scanner(scanner.next());
						      lineScanner5.useDelimiter("\\s*:\\s*");
						      lineScanner5.next();
						data.buero_student_faktor = lineScanner5.nextInt();
						
				/** lineScanner6: Objekt das die sechste Zeile der Eingabedatei haelt*/
					       Scanner lineScanner6 = new Scanner(scanner.next());
						      lineScanner6.useDelimiter("\\s*:\\s*");
						      lineScanner6.next();
						data.wc_fitness_vert = lineScanner6.nextFloat();
						
				/** lineScanner7: Objekt das die siebte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner7 = new Scanner(scanner.next());
						      lineScanner7.useDelimiter("\\s*:\\s*");
						      lineScanner7.next();
						data.wc_fitness_horz = lineScanner7.nextFloat();
						
				/** lineScanner8: Objekt das die achte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner8 = new Scanner(scanner.next());
						      lineScanner8.useDelimiter("\\s*:\\s*");
						      lineScanner8.next();
						data.seminarraum_fitness_vert = lineScanner8.nextFloat();
						
				/** lineScanner9: Objekt das die neunte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner9 = new Scanner(scanner.next());
						      lineScanner9.useDelimiter("\\s*:\\s*");
						      lineScanner9.next();
						data.seminarraum_fitness_horz = lineScanner9.nextFloat();
						
				/** lineScanner10: Objekt das die zehnte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner10 = new Scanner(scanner.next());
						      lineScanner10.useDelimiter("\\s*:\\s*");
						      lineScanner10.next();
						data.buero_fitness_vert = lineScanner10.nextFloat();
						
				/** lineScanner11: Objekt das die elfte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner11 = new Scanner(scanner.next());
						      lineScanner11.useDelimiter("\\s*:\\s*");
						      lineScanner11.next();
						data.buero_fitness_horz = lineScanner11.nextFloat();
						
				/** lineScanner12: Objekt das die zwölfte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner12 = new Scanner(scanner.next());
						      lineScanner12.useDelimiter("\\s*:\\s*");
						      lineScanner12.next();
						data.hoersaal_fitness_vert = lineScanner12.nextFloat();
						
				/** lineScanner13: Objekt das die dreizehnte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner13 = new Scanner(scanner.next());
						      lineScanner13.useDelimiter("\\s*:\\s*");
						      lineScanner13.next();
						data.hoersaal_fitness_horz = lineScanner13.nextFloat();
						
				/** lineScanner14: Objekt das die vierzehnte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner14 = new Scanner(scanner.next());
						      lineScanner14.useDelimiter("\\s*:\\s*");
						      lineScanner14.next();
						data.raumtiefe = lineScanner14.nextInt();
						
				/** lineScanner15: Objekt das die fünfzehnte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner15 = new Scanner(scanner.next());
						      lineScanner15.useDelimiter("\\s*:\\s*");
						      lineScanner15.next();
						data.hoersaal_grundflaeche = lineScanner15.nextInt();
						
				/** lineScanner16: Objekt das die sechzehnte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner16 = new Scanner(scanner.next());
						      lineScanner16.useDelimiter("\\s*:\\s*");
						      lineScanner16.next();
						data.wc_grundflaeche = lineScanner16.nextInt();
						
				/** lineScanner17: Objekt das die siebzehnte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner17 = new Scanner(scanner.next());
						      lineScanner17.useDelimiter("\\s*:\\s*");
						      lineScanner17.next();
						data.buero_grundflaeche = lineScanner17.nextInt();
						
				/** lineScanner18: Objekt das die achzehnte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner18 = new Scanner(scanner.next());
						      lineScanner18.useDelimiter("\\s*:\\s*");
						      lineScanner18.next();
						data.seminar_grundflaeche = lineScanner18.nextInt();
						
				/** lineScanner19: Objekt das die neunzehnte Zeile der Eingabedatei haelt*/
					       Scanner lineScanner19 = new Scanner(scanner.next());
						      lineScanner19.useDelimiter("\\s*:\\s*");
						      lineScanner19.next();
						data.flurbreite = lineScanner19.nextInt();
						
				/** lineScanner20: Objekt das die zwanzigste Zeile der Eingabedatei haelt*/
					       Scanner lineScanner20 = new Scanner(scanner.next());
						      lineScanner20.useDelimiter("\\s*:\\s*");
						      lineScanner20.next();
						data.stockwerkHoehe = lineScanner20.nextInt();
						
				/** lineScanner21: Objekt das die einundzwanzigste Zeile der Eingabedatei haelt*/
						Scanner lineScanner21 = new Scanner(scanner.next());
				           lineScanner21.useDelimiter("\\s*:\\s*");
				           lineScanner21.next();
				     data.maximaleFlurlaenge = lineScanner21.nextInt();
				     
				/** lineScanner22: Objekt das die zweiundzwanzigste Zeile der Eingabedatei haelt*/
				     Scanner lineScanner22 = new Scanner(scanner.next());
				          lineScanner22.useDelimiter("\\s*:\\s*");
				          lineScanner22.next();
				    data.minimaleFlurlaenge = lineScanner22.nextInt();
				      
		         scanner.close();

			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/**
		 * @return 
		 * 			-gibt ein Objekt vom Typ FHdata an den Controller zurück in dem alle Daten gehalten werden
		 */
		return data;
	}

}
