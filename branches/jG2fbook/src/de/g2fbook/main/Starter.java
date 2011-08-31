/**
 * 
 */
package de.g2fbook.main;

import de.g2fbook.services.DatabaseService;
import de.g2fbook.services.GoogleService;
import de.g2fbook.services.I_PropertyService;
import de.g2fbook.services.PropertyService;

/**
 * @author rico
 *
 */
public class Starter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
try {
	
	//Initialising
	PropertyService.init("private.properties");
	DatabaseService.init();
	
	//fetch some important parameter to connect to google
	String usr=PropertyService.getProp(I_PropertyService.PARAM_GOOGLE_USR);
	String pwd=PropertyService.getProp(I_PropertyService.PARAM_GOOGLE_PWD);
	String url=PropertyService.getProp(I_PropertyService.PARAM_GOOGLE_URL_LOGIN);
	
	//connect to google and get the phonebook
	GoogleService.logon(url,usr,pwd);
	url=PropertyService.getProp(I_PropertyService.PARAM_GOOGLE_URL_PBOOK);
	url=url.replaceAll("PARAM_GOOGLE_USR", usr);
	String phoneBook=GoogleService.getPhoneBook(url);
	
	//put the phonebook to the database
	DatabaseService.insertPhoneBook("googleBook", phoneBook);
	
	
	//DatabaseService.query("SELECT * FROM phonebook;");
	
	
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}

}
