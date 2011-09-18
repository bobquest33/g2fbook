/*
#########################################################################################
#     
#     Copyright (C) 2011 R. Engelmann
#     This program is free software: you can redistribute it and/or modify
#     it under the terms of the GNU General Public License as published by
#     the Free Software Foundation, either version 3 of the License, or
#     (at your option) any later version.
#  
#     This program is distributed in the hope that it will be useful,
#     but WITHOUT ANY WARRANTY; without even the implied warranty of
#     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
#     GNU General Public License for more details.
#  
#     You should have received a copy of the GNU General Public License
#     along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
#########################################################################################
*/
package de.g2fbook.services;

import org.apache.commons.net.telnet.*;
import java.io.*;

public class TelnetService

{

	private TelnetClient telnet = new TelnetClient();
	private InputStream in;
	private PrintStream out;
	private String PROMPT = "#";
	public String getPROMPT() {
		return PROMPT;
	}

	public void setPROMPT(String pROMPT) {
		PROMPT = pROMPT;
	}

	public String getUsr() {
		return usr;
	}

	public void setUsr(String usr) {
		this.usr = usr;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getSrv() {
		return srv;
	}

	public void setSrv(String srv) {
		this.srv = srv;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPROMPT_PWD() {
		return PROMPT_PWD;
	}

	public void setPROMPT_PWD(String pROMPTPWD) {
		PROMPT_PWD = pROMPTPWD;
	}

	private String usr=null;
	private String pwd=null;
	private String srv=null;
	private int port=23;
	private String PROMPT_PWD="Fritz!Box web password: ";

	public TelnetService() {
	}
	
	public void initFritzConnection(){
		try {

			// Connect to the specified server
			telnet.connect(srv, port);

			// Get input and output stream references
			in = telnet.getInputStream();
			out = new PrintStream(telnet.getOutputStream());

			// Log the user on
			readUntil(PROMPT_PWD);
			write(pwd);

			// Advance to a prompt
			readUntil(PROMPT + " ");

		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void su(String password) {
		try {
			write("su");
			readUntil("Password: ");
			write(password);
			
			readUntil(PROMPT + " ");

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String readUntil(String pattern) {

		try {
			char lastChar = pattern.charAt(pattern.length() - 1);
			StringBuffer sb = new StringBuffer();
			char ch = (char) in.read();
			while (true) {
				System.out.print(ch);
				sb.append(ch);
				if (ch == lastChar) {
					if (sb.toString().endsWith(pattern)) {
						return sb.toString();
					}
				}
				ch = (char) in.read();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public void write(String value) {

		try {

			out.println(value);
			out.flush();
			//System.out.println(value);

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String sendCommand(String command) {

		try {

			write(command);
			Thread.currentThread().sleep(500);
			return readUntil(PROMPT + " ");

		}

		catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

	public void disconnect() {

		try {
			telnet.disconnect();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	


}
