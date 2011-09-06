/**
 * 
 */
package de.g2fbook.main;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import de.g2fbook.services.DatabaseService;
import de.g2fbook.services.GoogleService;
import de.g2fbook.services.I_PropertyService;
import de.g2fbook.services.PropertyService;
import de.g2fbook.services.TransferService;
import de.g2fbook.services.XMLService;

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
	String google_usr=PropertyService.getProp(I_PropertyService.PARAM_GOOGLE_USR);
	String google_pwd=PropertyService.getProp(I_PropertyService.PARAM_GOOGLE_PWD);
	String google_url=PropertyService.getProp(I_PropertyService.PARAM_GOOGLE_URL_LOGIN);
	
	//connect to google and get the phonebook
	GoogleService.logon(google_url,google_usr,google_pwd);
	google_url=PropertyService.getProp(I_PropertyService.PARAM_GOOGLE_URL_PBOOK);
	google_url=google_url.replaceAll("PARAM_GOOGLE_USR", google_usr);
	String phoneBook_google=GoogleService.getPhoneBook(google_url);
	String phoneBook_fritz=GoogleService.getPhoneBook(google_url);
	//put the phonebook to the database
	DatabaseService.insertPhoneBook("googleBook", phoneBook_google);
	
	
	
	
	phoneBook_fritz=(XMLService.transform(phoneBook_google, "xslt/googleContacts-2-fritzPhoneBook.xsl"));
	
	//fetch some important parameter to connect to fritz!box
	String fritz_usr=PropertyService.getProp(I_PropertyService.PARAM_FRITZ_USR);
	String fritz_pwd=PropertyService.getProp(I_PropertyService.PARAM_FRITZ_PWD);
	String fritz_srv=PropertyService.getProp(I_PropertyService.PARAM_FRITZ_SRV);
	String fritz_pth=PropertyService.getProp(I_PropertyService.PARAM_FRITZ_PATH);
	InputStream in =  new ByteArrayInputStream(phoneBook_fritz.getBytes());
	
	TransferService transferService = new TransferService(fritz_usr, fritz_pwd, fritz_srv);
	transferService.ftpTransfer(in, fritz_pth+"phonebook1.xml");
	
	//DatabaseService.query("SELECT * FROM phonebook;");
	
	
} catch (Exception e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
	}

}
