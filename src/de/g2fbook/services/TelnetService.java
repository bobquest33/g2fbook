package de.g2fbook.services;

import java.io.IOException;

import org.apache.commons.net.telnet.TelnetClient;

import examples.util.IOUtil;



public class TelnetService {

	 public final static void main(String[] args)
	    {
	        TelnetClient telnet;

	        telnet = new TelnetClient();

	        try
	        {
	            telnet.connect("fritz.box");
	            
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	            System.exit(1);
	        }

	        IOUtil.readWrite(telnet.getInputStream(), telnet.getOutputStream(),
	                         System.in, System.out);

	        try
	        {
	            telnet.disconnect();
	        }
	        catch (IOException e)
	        {
	            e.printStackTrace();
	            System.exit(1);
	        }

	        System.exit(0);
	    }


}