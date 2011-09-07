package de.g2fbook.services;

import java.io.InputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class TransferService {
	private String usr = null;
	private String pwd = null;
	private String server = null;

	public TransferService(String usr, String pwd, String server) {
		this.usr = usr;
		this.pwd = pwd;
		this.server = server;
	}

	/**
	 * ftpTransfer
	 * @param in Inputstream
	 * @param destinationfile targetFilename
	 * @return true if all ok
	 */
	public boolean ftpTransfer(InputStream in, String destinationfile) {

		try {
			//new FTPClient
			FTPClient ftp = new FTPClient();
			ftp.connect(server);

			//login
			if (!ftp.login(usr, pwd)) {
				ftp.logout();
				return false;
			}
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return false;
			}
		
			//transferFile
			ftp.setFileType(FTPClient.BINARY_FILE_TYPE);
			boolean Store = ftp.storeFile(destinationfile, in);
			
			//cleanup - close - disconnect
			in.close();
			ftp.logout();
			ftp.disconnect();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
		return true;
	}
}
