package de.g2fbook.services;

import java.io.File;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLService {
	/**
	 * Simple transformation method.
	 * 
	 * @param source - source xml .
	 * @param xslt - xsltPath .
	 * @param result - result .
	 */
	public static String transform(String source, String xsltPath) {
		TransformerFactory tFactory = TransformerFactory.newInstance();
		String result=null;
		try {
			Transformer transformer = tFactory.newTransformer(new StreamSource(
					new File(xsltPath)));
			StringReader reader = new StringReader(source);
			StringWriter writer = new StringWriter();

			transformer.transform(new StreamSource(reader), new StreamResult(
					writer));
			writer.flush();
			result = writer.getBuffer().toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
