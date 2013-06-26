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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

/**
 *
 * @author Martin
 */
@Controller
@RequestMapping(value = "medium")
public class MediumController {
    private static Logger logger = LogManager.getLogger(MediumController.class.getName());
    
    @Autowired
    private ODFParser parser;
    
    @Autowired
    private GDiskManagerWeb manager;
    
    @RequestMapping(value = "addMedium", method = RequestMethod.POST)
    public String addMedium(@ModelAttribute Medium newMedium, Model model){
        logger.log(Level.TRACE, "nove medium: " + newMedium + " Ma nastavenou kategorii: " + newMedium.getCategory().getName());
//        logger.log(Level.TRACE, "Zvolena kategorie: " + category);
//        String decodedCategoryName = Helper.decodeEscapedString(categoryName);
//        if(decodedCategoryName == null){
//            model.addAttribute("msg", "Error during decoding category name from url.");
//            return "errorPage";
//        }
        newMedium.trimEmptyMovies();
        logger.log(Level.TRACE, "nove medium po trimu: " + newMedium);
        
        parser.addMedium(newMedium, newMedium.getCategory() );
        parser.reloadDocument();
        manager.updateTempFileToGDrive();
        return "forward:/category/" + newMedium.getCategory().getName();       //aby bylo mozne pouzit redirect bylo potreba string nejprve encodovat aby se vyescapovaly diakriticke znaky
    }
    
    /*
     * Vezme request na zobrazeni formulare pro pridani media
     * pokud je categoryName
     */
    @RequestMapping(value = {"addMedium/{categoryName}"}, method = RequestMethod.GET)
    public String showAddMediumForm(@PathVariable String categoryName, @RequestParam(required = false, value = "preselected") boolean preselected, Model model){
        logger.log(Level.TRACE, "Vytvarim backing object preselected:" + preselected);
        
        List<String> categories = parser.getAllCategoryNames();
        
        String selectedCategory = "";
        if(preselected){       //pokud je categoryName = empty znamenato ze jsme neprisli ze zadne konkretni kategorie

            selectedCategory = Helper.decodeEscapedString(categoryName);
            if(selectedCategory == null){
                model.addAttribute("msg", "Error during decoding category name from url.");
                return "errorPage";
            }
            categories.remove(selectedCategory);
            logger.log(Level.TRACE, "Byla predvybrana kategorie: " + selectedCategory);
        }
        
        List<Movie> movies = new ArrayList<Movie>();
        for(int i=0; i < 5; i++){
            movies.add(new Movie());
        }
        
        Medium medium = new Medium(null, null, movies, new Category(selectedCategory));
        
        model.addAttribute("newMedium", medium);
        model.addAttribute("categories", categories);
        model.addAttribute("selectedCategory", selectedCategory);
        return "addMedium";
    }
    
    //Pro zjednodušení se odesílá ID media a jméno kategorie jako formulář
    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public String deleteMedium(@RequestParam(required = false) String categoryName, @RequestParam(required = false) Integer mediumID, Model model){
        logger.log(Level.TRACE, "Mazu medium: " + mediumID + " z kategorie: " + categoryName);
        
        String decodedCategoryName = Helper.decodeEscapedString(categoryName);
        
        parser.deleteMedium(new Medium(mediumID, null, null, new Category(decodedCategoryName)));
        manager.updateTempFileToGDrive();
        return "redirect:/category/" + decodedCategoryName;
    }
    
    
    //Postup jak ziskat dynamicky atributy z requestu
//    public void test (HttpServletRequest request, HttpServletResponse response){
//        //request.getAttributeNames();
//        
//        request.getParameterMap();
//        List l;
//        l.retainAll(l);
//    }
}
