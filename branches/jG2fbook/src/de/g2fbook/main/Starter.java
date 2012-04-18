/**
 * 
 */
package de.g2fbook.main;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.StringTokenizer;

import de.g2fbook.services.DatabaseService;
import de.g2fbook.services.GoogleService;
import de.g2fbook.services.I_PropertyService;
import de.g2fbook.services.PropertyService;
import de.g2fbook.services.TelnetService;
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

			// Initialising
			PropertyService.init("./private.properties");

			DatabaseService.init();

			// fetch some important parameter to connect to google
			String google_usr = PropertyService
					.getProp(I_PropertyService.PARAM_GOOGLE_USR);
			String google_pwd = PropertyService
					.getProp(I_PropertyService.PARAM_GOOGLE_PWD);
			String google_url = PropertyService
					.getProp(I_PropertyService.PARAM_GOOGLE_URL_LOGIN);

			// connect to google and get the phonebook
			GoogleService.logon(google_url, google_usr, google_pwd);
			google_url = PropertyService
					.getProp(I_PropertyService.PARAM_GOOGLE_URL_PBOOK);
			google_url = google_url.replaceAll("PARAM_GOOGLE_USR", google_usr);
			String phoneBook_google = GoogleService.getPhoneBook(google_url);
			String phoneBook_fritz = null;
			// put the phonebook to the database
			DatabaseService.insertPhoneBook("googleBook", phoneBook_google);
			// String
			// phoneBook_google=DatabaseService.query("SELECT phonebook from phonebook;");
			phoneBook_fritz = (XMLService.transform(phoneBook_google,
					PropertyService.getProp(I_PropertyService.PARAM_PATH_google2fritz)));
			
			
			
			// fetch some important parameter to connect to fritz!box
			String fritz_ftp_usr = PropertyService
					.getProp(I_PropertyService.PARAM_FRITZ_FTP_USR);
			String fritz_ftp_pwd = PropertyService
					.getProp(I_PropertyService.PARAM_FRITZ_FTP_PWD);
			String fritz_srv = PropertyService
					.getProp(I_PropertyService.PARAM_FRITZ_SRV);
			String fritz_pth = PropertyService
					.getProp(I_PropertyService.PARAM_FRITZ_PATH);

			InputStream in = new ByteArrayInputStream(phoneBook_fritz
					.getBytes());

			TransferService transferService = new TransferService(
					fritz_ftp_usr, fritz_ftp_pwd, fritz_srv);
			
			
			
			
			
			String imageURLs = (XMLService.transform(phoneBook_google,
					PropertyService.getProp(I_PropertyService.PARAM_PATH_google2fritz_image)));
			try {
			BufferedReader bIn = new BufferedReader(new StringReader(imageURLs));
				String imageUrl = null;
				while ((imageUrl = bIn.readLine()) != null) {
					String imageFileName=imageUrl.substring(imageUrl.lastIndexOf('/')+1)+".jpg";
					//System.out.println("Gelesene Zeile: " + imageUrl);
					//System.out.println("fileName: " + imageFileName);
					
					byte[] imageContent=GoogleService.requestBytes(imageUrl,null);
					FileOutputStream fileOutputStream = new FileOutputStream(PropertyService.getProp(I_PropertyService.PARAM_PATH_WORK)+"/"+imageFileName);
					fileOutputStream.write(imageContent);
					fileOutputStream.flush();
					fileOutputStream.close();
					
					InputStream imageContentIS = GoogleService.requestInStream(imageUrl,null);
					transferService.ftpTransfer(imageContentIS, fritz_pth+"/FRITZ/fonpix/" + imageFileName);
					
				//	System.out.println(GoogleService.request(imageUrl,null));
				}
				bIn.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			transferService.ftpTransfer(in, fritz_pth + "phonebook.xml");

			String fritz_pwd = PropertyService
					.getProp(I_PropertyService.PARAM_FRITZ_PWD);
			int fritz_port = Integer.parseInt(PropertyService
					.getProp(I_PropertyService.PARAM_FRITZ_TELNET_PORT));
			String fritz_prompt = PropertyService
					.getProp(I_PropertyService.PARAM_FRITZ_PROMPT);
			String fritz_prompt_pwd = PropertyService
					.getProp(I_PropertyService.PARAM_FRITZ_PROMPT_PWD);

			TelnetService telnetService = new TelnetService();
			telnetService.setPort(fritz_port);
			telnetService.setPROMPT(fritz_prompt);
			telnetService.setPROMPT_PWD(fritz_prompt_pwd);
			telnetService.setPwd(fritz_pwd);
			telnetService.setSrv(fritz_srv);

			telnetService.initFritzConnection();
			// telnetService.sendCommand("cp /var/media/ftp/"+fritz_pth+"phonebook1.xml /var/tmp/");
			
			telnetService.sendCommand("pbd --importto --id=%u /var/media/ftp/"
					+ fritz_pth + "phonebook.xml");
			telnetService.disconnect();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
