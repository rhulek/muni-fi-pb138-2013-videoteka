/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videoteka.spring;

import com.mycompany.videolibrary.GDiskManagerWeb;
import com.mycompany.videolibrary.Movie;
import com.mycompany.videolibrary.ODFParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Ori
 */
@Controller
@RequestMapping(value = "movie")
public class MovieController {
    private static Logger logger = LogManager.getLogger(MovieController.class.getName()); 
   
    @Autowired
    private ODFParser parser;
    
    @Autowired
    private GDiskManagerWeb manager;
    
    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail(@ModelAttribute Movie movie, Model model){
        
       // parser.addMedium(newMedium, newMedium.getCategory() );
       // parser.reloadDocument();
       // manager.updateTempFileToGDrive();
        return "redirect:/movie/" + movie.getName();
    }
}
