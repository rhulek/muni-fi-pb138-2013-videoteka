/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary.experimental;

import com.mycompany.videolibrary.GDiskManager;
import com.mycompany.videolibrary.GDiskManagerImpl;
import com.mycompany.videolibrary.GDiskManagerWeb;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 *
 * @author Martin
 */
public class GDiskConnectionExample {
    private static Logger logger = LogManager.getLogger(GDiskConnectionExample.class.getName());
    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
//       GDiskManager gDiskManager = new GDiskManagerImpl(); 
//       gDiskManager.getTempFile();
       
        GDiskManagerWeb manager = new GDiskManagerWeb();
        
        
        if(!manager.checkCredentialsFile()){
            manager.openBrowser();

            System.out.println("Please insert authorization code: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String authCode = null;
            try {
                authCode = br.readLine();
            } catch (IOException ex) {
                logger.log(Level.ERROR, ex);
            }
            manager.autorizeAndSaveCredentials(authCode);
        
        } else {
            logger.log(Level.INFO, "Nahrani credentials: " + manager.loadCredentials());
            manager.refreshToken();
            manager.getTempFile();
        }
        
       System.out.println("\u001b[1;31mThis is red \n And all \n this have only \n\tblack color \n and here is colour again?\u001b[0m");
    }
}
