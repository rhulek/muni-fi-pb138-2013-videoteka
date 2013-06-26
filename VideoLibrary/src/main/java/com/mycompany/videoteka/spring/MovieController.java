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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
    

    @RequestMapping(value = {""}, method = RequestMethod.GET)
    public String detail(@RequestParam String category, @RequestParam String mediumId, 
    @RequestParam String movieId, Model model){
        
        String pokus = "pokusnyKralik";

        model.addAttribute("pokus", pokus);
        return "movie";
        
    }
}
