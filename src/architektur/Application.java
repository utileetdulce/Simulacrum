package architektur;

/**
 * Die Klasse Application startet den Programmablauf durch Initialisierung 
 * des Controllers und Aufruf seiner Hauptmethode. 
 *  
 * @author Philipp Niederlag 
 * 		   Stud.Nr.: 11057798
 * 		   Gruppennummer 4
 * 		   SS 09 
 * 		   Email: wp-poduction@web.de
 * 
 * @version 1.0
 */
public class Application {

	 /**
	  * @param args
	  */
	 public static void main(String[] args) {
	  // TODO Auto-generated method stub
	  Controller controller = new Controller();
	  controller.startApp();  
	 }

}


