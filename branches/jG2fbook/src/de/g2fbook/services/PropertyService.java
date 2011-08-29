package de.g2fbook.services;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertyService implements I_PropertyService {

	private static Properties properties = new Properties();
	
	public static void init(String fileName) throws Exception{
		properties.load(new FileInputStream(fileName));
	}
	
	public static String getProp(String key) {
		return properties.getProperty(key);
		
	}
}
