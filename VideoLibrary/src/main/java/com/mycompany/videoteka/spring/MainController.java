/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videoteka.spring;

import com.mycompany.videolibrary.Category;
import com.mycompany.videolibrary.GDiskManagerWeb;
import com.mycompany.videolibrary.Medium;
import com.mycompany.videolibrary.Movie;
import com.mycompany.videolibrary.ODFParser;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Martin
 */
@Controller
@RequestMapping(value = "/")
public class MainController {
    private static Logger logger = LogManager.getLogger(InitController.class.getName());
    
    @Autowired
    private GDiskManagerWeb manager;
    
    @Autowired
    private ODFParser parser;
    
    @RequestMapping(value = {"/showCategories"}, method = RequestMethod.GET)
    public ModelAndView showIndex(){
        
        if(parser == null){
            logger.log(Level.ERROR, "Parser je null!");
            return new ModelAndView("errorPage").addObject("msg", "MainController - showIndex(): parser je null!");
        }
        
        List<String> categoriesNames = parser.getAllCategoryNames();
        ModelAndView index = new ModelAndView("index");
        
        index.addObject("categoriesList", categoriesNames);
        return index;
    }
    
    @RequestMapping(value = "category/{categoryName}", method = {RequestMethod.GET, RequestMethod.PUT})
    public String showCategory(@PathVariable String categoryName, Model model){
        
        if(categoryName == null){
            logger.log(Level.ERROR, "Requested parameter categoryName is missing!");
            model.addAttribute("msg", "Requested parameter categoryName is missing!");
        }
        
        
        String decodedCategoryName;
        try {
            decodedCategoryName = URLDecoder.decode(categoryName, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.ERROR, "Error during decoding category name from url.", ex);
            model.addAttribute("msg", "Error during decoding category name from url.");
            return "errorPage";
        }
        
        logger.log(Level.TRACE, "dekodovane jmeno kategorie z url: " + decodedCategoryName);
//        Category category = parser.getCategory(decodedCategoryName);
        Category category = new Category("Pejsek a kocicka");
        List<Movie> filmy = new ArrayList<Movie>();
        filmy.add(new Movie(10, "Macha sebestova"));
        filmy.add(new Movie(1, "Na samote u lesa"));
        filmy.add(new Movie(3, "Nosorozec Tom"));
        
        Medium med1 = new Medium(335, "DVD", filmy);
        
        List<Movie> filmy2 = new ArrayList<Movie>();
        filmy2.add(new Movie(7, "Cerveny trakturek"));
        filmy2.add(new Movie(7, "Darbujan a pandrhola"));
        
        Medium med2 = new Medium(55, "Blue Ray", filmy2);
        
        category.addMedium(med1);
        category.addMedium(med2);
        
        
        logger.log(Level.TRACE, "Posilam kategorii: \r" + category);
        
        model.addAttribute("category", category);
        return "showCategory";
    }
}
