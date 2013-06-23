/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary.experimental;

import com.mycompany.videolibrary.Category;
import com.mycompany.videolibrary.Medium;
import com.mycompany.videolibrary.Movie;
import com.mycompany.videolibrary.ODFParser;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        
        //List<String> categories = parser.getAllCategoryNames();
        
        
        Category category = parser.getCategory("ahoj");
        
        Movie movie1 = new Movie(1, "ahoj");
        Map<String, String> metaInfo = new HashMap<String, String>();
        metaInfo.put("Codec", "H.264");
        metaInfo.put("Container", "mkv");
        movie1.setMetaInfo(metaInfo);
        Map<String, String> metaInfo2 = new HashMap<String, String>();
        metaInfo2.put("Codec", "H.264");
        metaInfo2.put("Container", "mkv");
        
        //System.out.print(parser.findMediumByMeta(metaInfo2));
        
        //parser.addMedium(new Medium(50, null, new ArrayList(Arrays.asList(movie1))), category);
    
        //parser.getCategory("ahoj");
        /*  
        for(String categoryName: categories){
            System.out.println(categoryName);
        }
           
        Category category = parser.getCategory(categories.get(0));

        System.out.println(category);
        
        Category category2 = parser.getStaticTestCategory();
        logger.log(Level.INFO, "Kategorie ziskaná z getStaticTestCategory(): " + category2);
        System.out.println( category2 );
        
        
           Category category = parser.getCategory(categories.get(0));
 
        List<Medium> media = parser.findMediaByMovieName("Homer Doma");
        
       
        for(Medium medium : media){
            System.out.println("Medium: " + medium);
        }

     
        
        */  
        
        
    }
}
