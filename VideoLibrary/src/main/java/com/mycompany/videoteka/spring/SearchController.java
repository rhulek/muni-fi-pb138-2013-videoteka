/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videoteka.spring;

import com.mycompany.videolibrary.Medium;
import com.mycompany.videolibrary.ODFParser;
import com.mycompany.videolibrary.SearchBackingBean;
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
@RequestMapping(value = "search")
public class SearchController {
    private static Logger logger = LogManager.getLogger(SearchController.class.getName());
    
    @Autowired
    private ODFParser parser;
        
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String showSearchForm(Model model){
        
        model.addAttribute("categoriesList", parser.getAllCategoryNames());
        model.addAttribute("searchBean", new SearchBackingBean());
        return "search";
    }
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public String showSearchResult(@ModelAttribute SearchBackingBean searchBean, Model model){
        logger.log(Level.DEBUG, "Hledám film: '" + searchBean.getMovieName() + "' v kategorii: '" + searchBean.getCategoryName() + "'");
        
        List<Medium> mediumsList  = parser.findMediaByMovieName(searchBean.getMovieName());
        if(mediumsList == null || mediumsList.isEmpty()){
            model.addAttribute("msg","Hledaný film: '" + searchBean.getMovieName() + "' nebyl nalezen.");
            logger.log(Level.DEBUG, "Hledaný film: '" + searchBean.getMovieName() + "' nebyl nalezen.");
        }
        
        for(Medium medium: mediumsList){
            logger.log(Level.TRACE, medium);
        }
        
        model.addAttribute("foundMediums", mediumsList);
        model.addAttribute("searchBean", searchBean);
        return "search";
    }
}
