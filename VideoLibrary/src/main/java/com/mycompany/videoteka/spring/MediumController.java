/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videoteka.spring;

import com.mycompany.videolibrary.Category;
import com.mycompany.videolibrary.GDiskManagerWeb;
import com.mycompany.videolibrary.Helper;
import com.mycompany.videolibrary.Medium;
import com.mycompany.videolibrary.Movie;
import com.mycompany.videolibrary.ODFParser;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Martin
 */
@Controller
@RequestMapping(value = "medium")
public class MediumController {
     private static Logger logger = LogManager.getLogger(MediumController.class.getName());
    
    @Autowired
    private GDiskManagerWeb manager;
    
    @Autowired
    private ODFParser parser;
    
    @RequestMapping(value = "addMedium", method = RequestMethod.POST)
    public String addMedium(@ModelAttribute Medium newMedium, Model model){
        logger.log(Level.TRACE, "nove medium: " + newMedium);
        
//        String decodedCategoryName = Helper.decodeEscapedString(categoryName);
//        if(decodedCategoryName == null){
//            model.addAttribute("msg", "Error during decoding category name from url.");
//            return "errorPage";
//        }
        
        return "redirect:/category/showAll";
    }
    
    @RequestMapping(value = "addMedium", method = RequestMethod.GET)
    public String showAddMediumForm(Model model){
        logger.log(Level.TRACE, "Vytvarim backing object");
        
        List<Movie> movies = new ArrayList<Movie>();
        for(int i=0; i < 5; i++){
            movies.add(new Movie());
        }
        Medium medium = new Medium(null, null, movies);
        medium.setCategory(new Category("tradaa"));
        
        model.addAttribute("newMedium", medium);
        
        return "addMedium";
    }
    
}
