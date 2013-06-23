/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary.experimental;

import com.mycompany.videolibrary.Category;
import com.mycompany.videolibrary.Medium;
import com.mycompany.videolibrary.ODFParser;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Martin
 */
public class ODFParserExample {
    private static Logger logger = LogManager.getLogger(ODFParserExample.class.getName());
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ODFParser parser = new ODFParser("tmpFile.ods");
        
        List<String> categories = parser.getAllCategoryNames();
      /*  
        for(String categoryName: categories){
            System.out.println(categoryName);
        }
           
        Category category = parser.getCategory(categories.get(0));

        System.out.println(category);
        
        Category category2 = parser.getStaticTestCategory();
        logger.log(Level.INFO, "Kategorie ziskaná z getStaticTestCategory(): " + category2);
        System.out.println( category2 );
        */  
        
           Category category = parser.getCategory(categories.get(0));
 
        List<Medium> media = parser.findMediaByMovieName("Homer Doma");
        
       
        for(Medium medium : media){
            System.out.println("Medium: " + medium);
        }

     
        
        
        
        
    }
}
