/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videoteka.spring;

import com.mycompany.videolibrary.GDiskManagerWeb;
import com.mycompany.videolibrary.Helper;
import com.mycompany.videolibrary.Movie;
import com.mycompany.videolibrary.ODFParser;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.Level;
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
import org.springframework.web.bind.annotation.SessionAttributes;
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
    @RequestParam String movieName, Model model){
        
        Movie movie = parser.findMovie(movieName, mediumId, category);
        
        if(movie != null){
            model.addAttribute("movieName", movie.getName());
            model.addAttribute("category", category);
            model.addAttribute("mediumId", mediumId);
            model.addAttribute("metaInfo", movie.getMetaInfo());
        }
        return "movie";   
    }
        
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveMovie(Model model){
        logger.log(Level.TRACE, "SaveMovie Controller");
        
        return "redirect:/category/";
    }
}
