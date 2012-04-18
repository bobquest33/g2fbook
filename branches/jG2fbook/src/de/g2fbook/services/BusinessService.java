/**
 * 
 */
package de.g2fbook.services;

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

public class BusinessService {
	String google_usr = null;
	String google_pwd = null;
	String google_url_pattern = null;
	String google_url = null;
	String phoneBook_google = null;
	String phoneBook_fritz = null;
	String fritz_ftp_usr = null;
	String fritz_ftp_pwd = null;
	String fritz_srv = null;
	String fritz_pth = null;
	String imageURLs = null;
	String fritz_pwd = null;
	int fritz_port;
	String fritz_prompt = null;
	String fritz_prompt_pwd = null;

	public void init() throws Exception {
		PropertyService.init("private.properties");
		// fetch some important parameter to connect to google
		google_usr = PropertyService
				.getProp(I_PropertyService.PARAM_GOOGLE_USR);
		google_pwd = PropertyService
				.getProp(I_PropertyService.PARAM_GOOGLE_PWD);
		google_url_pattern = PropertyService
				.getProp(I_PropertyService.PARAM_GOOGLE_URL_LOGIN);
		// fetch some important parameter to connect to fritz!box
		fritz_ftp_usr = PropertyService
				.getProp(I_PropertyService.PARAM_FRITZ_FTP_USR);
		fritz_ftp_pwd = PropertyService
				.getProp(I_PropertyService.PARAM_FRITZ_FTP_PWD);
		fritz_srv = PropertyService.getProp(I_PropertyService.PARAM_FRITZ_SRV);
		fritz_pth = PropertyService.getProp(I_PropertyService.PARAM_FRITZ_PATH);
		fritz_pwd = PropertyService.getProp(I_PropertyService.PARAM_FRITZ_PWD);
		fritz_port = Integer.parseInt(PropertyService
				.getProp(I_PropertyService.PARAM_FRITZ_TELNET_PORT));
		fritz_prompt = PropertyService
				.getProp(I_PropertyService.PARAM_FRITZ_PROMPT);
		fritz_prompt_pwd = PropertyService
				.getProp(I_PropertyService.PARAM_FRITZ_PROMPT_PWD);
	}

	public void getGoogleBook() throws Exception {
		// connect to google and get the phonebook
		GoogleService.logon(google_url_pattern, google_usr, google_pwd);
		google_url = PropertyService
				.getProp(I_PropertyService.PARAM_GOOGLE_URL_PBOOK);
		google_url = google_url.replaceAll("PARAM_GOOGLE_USR", google_usr);
		phoneBook_google = GoogleService.getPhoneBook(google_url);

	}

	public void transformToFritzBook() {
		phoneBook_fritz = (XMLService.transform(phoneBook_google,
				PropertyService
						.getProp(I_PropertyService.PARAM_PATH_google2fritz)));
	}

	public void transformToImageUrls() {
		imageURLs = (XMLService.transform(phoneBook_google, PropertyService
				.getProp(I_PropertyService.PARAM_PATH_google2fritz_image)));
	}

	public void transferToFritzBox() {
		InputStream in = new ByteArrayInputStream(phoneBook_fritz.getBytes());

		TransferService transferService = new TransferService(fritz_ftp_usr,
				fritz_ftp_pwd, fritz_srv);

		try {
			BufferedReader bIn = new BufferedReader(new StringReader(imageURLs));
			String imageUrl = null;
			while ((imageUrl = bIn.readLine()) != null) {
				String imageFileName = imageUrl.substring(imageUrl
						.lastIndexOf('/') + 1) + ".jpg";
				// System.out.println("Gelesene Zeile: " + imageUrl);
				// System.out.println("fileName: " + imageFileName);

				byte[] imageContent = GoogleService
						.requestBytes(imageUrl, null);
				FileOutputStream fileOutputStream = new FileOutputStream(
						PropertyService
								.getProp(I_PropertyService.PARAM_PATH_WORK)
								+ "/" + imageFileName);
				fileOutputStream.write(imageContent);
				fileOutputStream.flush();
				fileOutputStream.close();

				InputStream imageContentIS = GoogleService.requestInStream(
						imageUrl, null);
				transferService.ftpTransfer(imageContentIS, fritz_pth
						+ "/FRITZ/fonpix/" + imageFileName);

				// System.out.println(GoogleService.request(imageUrl,null));
			}
			bIn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		transferService.ftpTransfer(in, fritz_pth + "phonebook.xml");
	}

	public void activateOnFritzBox() {

		try {

			TelnetService telnetService = new TelnetService();
			telnetService.setPort(fritz_port);
			telnetService.setPROMPT(fritz_prompt);
			telnetService.setPROMPT_PWD(fritz_prompt_pwd);
			telnetService.setPwd(fritz_pwd);
			telnetService.setSrv(fritz_srv);

			telnetService.initFritzConnection();
			telnetService.sendCommand("pbd --importto --id=%u /var/media/ftp/"
					+ fritz_pth + "phonebook.xml");
			telnetService.disconnect();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
