package de.g2fbook.services;

import java.io.File;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public class XMLService {
    /** 
     * Simple transformation method. 
     * @param sourcePath - Absolute path to source xml file. 
     * @param xsltPath - Absolute path to xslt file. 
     * @param resultDir - Directory where you want to put resulting files. 
     */  
    public static void simpleTransform(String sourcePath, String xsltPath,  
                                       String resultDir) {  
        TransformerFactory tFactory = TransformerFactory.newInstance();  
        try {  
            Transformer transformer =  
                tFactory.newTransformer(new StreamSource(new File(xsltPath)));  
  
            transformer.transform(new StreamSource(new File(sourcePath)),  
                                  new StreamResult(new File(resultDir)));  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}
