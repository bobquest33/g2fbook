package de.g2fbook.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.StringTokenizer;

public class GoogleService {

	private static String authTag = null;
	public static String getAuthTag() {
		return authTag;
	}

	public static void setAuthTag(String authTag) {
		GoogleService.authTag = authTag;
	}

	private static String phoneBook = null;

	public static String getPhoneBook() {
		return phoneBook;
	}

	public static void setPhoneBook(String phoneBook) {
		GoogleService.phoneBook = phoneBook;
	}

	public static String getPhoneBook(String url) throws Exception {

		phoneBook = request(url, null);
		return phoneBook;

	}

	public static void logon(String url, String usr, String pwd)
			throws Exception {
		// Form the POST parameters
		StringBuilder content = new StringBuilder();
		content.append("Email=").append(URLEncoder.encode(usr, "UTF-8"));
		content.append("&Passwd=").append(URLEncoder.encode(pwd, "UTF-8"));
		content.append("&source=")
				.append(URLEncoder.encode("g2fbook", "UTF-8"));
		content.append("&service=").append(URLEncoder.encode("cp", "UTF-8"));
		String tmpString = request(url, content.toString());
		authTag = getAuthString(tmpString);
		System.out.print("-.-." + authTag);
	}

	public static String request(String url, String content) throws IOException {
		// Create a login request. A login request is a POST request that looks
		// like
		// POST /accounts/ClientLogin HTTP/1.0
		// Content-type: application/x-www-form-urlencoded
		// Email=lala@gmail.com&Passwd=huhu&service=gbase&source=Insert
		// Example

		// Open connection
		HttpURLConnection urlConnection = (HttpURLConnection) new URL(url)
				.openConnection();

		// Set properties of the connection
		urlConnection.setRequestMethod("GET");
		urlConnection.setDoInput(true);
		urlConnection.setDoOutput(true);
		urlConnection.setUseCaches(false);
		urlConnection.setRequestProperty("Authorization", "GoogleLogin auth="
				+ authTag);
		// urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
		urlConnection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded");

		if (content != null) {
			OutputStream outputStream = urlConnection.getOutputStream();
			outputStream.write(content.getBytes("UTF-8"));
			outputStream.close();
		}
		// Retrieve the output
		int responseCode = urlConnection.getResponseCode();
		InputStream inputStream;
		if (responseCode == HttpURLConnection.HTTP_OK) {
			inputStream = urlConnection.getInputStream();
		} else {
			inputStream = urlConnection.getErrorStream();
		}

		String tmpString = toString(inputStream);
		return tmpString;
	}

	/**
	 * Writes the content of the input stream to a <code>String<code>.
	 */
	private static String toString(InputStream inputStream) throws IOException {
		String string;
		StringBuilder outputBuilder = new StringBuilder();
		if (inputStream != null) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream));
			while (null != (string = reader.readLine())) {
				outputBuilder.append(string).append('\n');
			}
		}
		return outputBuilder.toString();
	}

	/**
	 * Retrieves the authentication token for the provided set of credentials.
	 * 
	 * @return the authorization token that can be used to access authenticated
	 *         Google Base data API feeds
	 */
	private static String getAuthString(String tmpString) {

		// Parse the result of the login request. If everything went fine, the
		// response will look like
		// HTTP/1.0 200 OK
		// Server: GFE/1.3
		// Content-Type: text/plain
		// SID=DQAAAGgA...7Zg8CTN
		// LSID=DQAAAGsA...lk8BBbG
		// Auth=DQAAAGgA...dk3fA5N
		// so all we need to do is look for "Auth" and get the token that comes
		// after it

		StringTokenizer tokenizer = new StringTokenizer(tmpString, "=\n ");
		String token = null;

		while (tokenizer.hasMoreElements()) {
			if (tokenizer.nextToken().equals("Auth")) {
				if (tokenizer.hasMoreElements()) {
					token = tokenizer.nextToken();
				}
				break;
			}
		}
		if (token == null) {
			System.out.println("Authentication error. Response from server:\n"
					+ tmpString);
			System.exit(1);
		}
		return token;
	}

}