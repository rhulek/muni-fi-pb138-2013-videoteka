/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary;

import com.mycompany.videoteka.spring.InitController;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Martin
 */
public class Helper {
    private static Logger logger = LogManager.getLogger(Helper.class.getName());
    
    public static String decodeEscapedString(String stringToDecode){
    logger.log(Level.TRACE, "Dekoduji string: '" + stringToDecode + "'");

    String decodedString;
    try {
        decodedString = URLDecoder.decode(stringToDecode, "UTF-8");
        logger.log(Level.TRACE, "Dekodovany string: '" + decodedString + "'");
        return decodedString;

    } catch (UnsupportedEncodingException ex) {
        logger.log(Level.ERROR, "Error during decoding category name from url.", ex);
        return null;
    }
}
}
