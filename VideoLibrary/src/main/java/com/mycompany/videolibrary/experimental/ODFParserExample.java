/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary.experimental;

import com.mycompany.videolibrary.Category;
import com.mycompany.videolibrary.ODFParser;
import java.util.List;

/**
 *
 * @author Martin
 */
public class ODFParserExample {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ODFParser parser = new ODFParser("tmpFile.ods");
        
        List<String> categories = parser.getAllCategoryNames();
        
        for(String categoryName: categories){
            System.out.println(categoryName);
        }
               
        Category category = parser.getCategory(categories.get(0));

        
 
        
        System.out.println(category);
    }
}
