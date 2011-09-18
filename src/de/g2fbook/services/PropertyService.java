package de.g2fbook.services;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class PropertyService implements I_PropertyService {

	private static Properties properties = new Properties();
	
	public static void init(String fileName) throws Exception{
		properties.load(new FileInputStream(fileName));
		File workPath = new File(getProp(I_PropertyService.PARAM_PATH_WORK));
		if(!workPath.exists())
			workPath.mkdir();
	}
	
	public static String getProp(String key) {
		return properties.getProperty(key);
		
	}
}
