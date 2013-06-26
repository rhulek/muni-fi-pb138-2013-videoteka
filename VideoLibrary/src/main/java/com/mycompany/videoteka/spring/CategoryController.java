/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videoteka.spring;

import com.mycompany.videolibrary.Category;
import com.mycompany.videolibrary.GDiskManagerWeb;
import com.mycompany.videolibrary.Helper;
import com.mycompany.videolibrary.ODFParser;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Martin
 */
@Controller
@RequestMapping(value = "/category")
public class CategoryController {
    private static Logger logger = LogManager.getLogger(InitController.class.getName());
    
    @Autowired
    private GDiskManagerWeb manager;
    
    @Autowired
    private ODFParser parser;
    
    @RequestMapping(value = {"", "showAll"}, method = RequestMethod.GET)
    public ModelAndView showCategories(){
        
        if(parser == null){
            logger.log(Level.ERROR, "Parser je null!");
            return new ModelAndView("errorPage").addObject("msg", "MainController - showIndex(): parser je null!");
        }
        
        parser.reloadDocument();
        List<String> categoriesNames = parser.getAllCategoryNames();
        ModelAndView index = new ModelAndView("index");
        
        index.addObject("categoriesList", categoriesNames);
        return index;
    }
    
    //Je potreba davat bacha! Protoze vsechno co bude mit adresu ../category/* a nebude zachyceno nekde jinde pujde sem!!!
    @RequestMapping(value = "{categoryName}", method = {RequestMethod.GET, RequestMethod.PUT})
    public String showCategory(@PathVariable String categoryName, Model model){
        
        if(categoryName == null){
            logger.log(Level.ERROR, "Requested parameter categoryName is missing!");
            model.addAttribute("msg", "Requested parameter categoryName is missing!");
        }
        logger.log(Level.TRACE, "Jmeno kategorie před dkodováním: '" + categoryName + "'");
        
        String decodedCategoryName = Helper.decodeEscapedString(categoryName);
        if(decodedCategoryName == null){
            model.addAttribute("msg", "Error during decoding category name from url.");
            return "errorPage";
        }
        
        parser.reloadDocument();
        logger.log(Level.TRACE, "dekodovane jmeno kategorie z url: '" + decodedCategoryName + "'");
        Category category = parser.getCategory(decodedCategoryName);
        
//        Category category = parser.getStaticTestCategory();
        
//       Category category = new Category("Pejsek a kocicka");
//        List<Movie> filmy = new ArrayList<Movie>();
//        filmy.add(new Movie(10, "Macha sebestova"));
//        filmy.add(new Movie(1, "Na samote u lesa"));
//        filmy.add(new Movie(3, "Nosorozec Tom"));
//        
//        Medium med1 = new Medium(335, "DVD", filmy);
//        
//        List<Movie> filmy2 = new ArrayList<Movie>();
//        filmy2.add(new Movie(7, "Cerveny trakturek"));
//        filmy2.add(new Movie(7, "Darbujan a pandrhola"));
//        
//        Medium med2 = new Medium(55, "Blue Ray", filmy2);
//        
//        
//        List<Movie> filmy3 = new ArrayList<Movie>();
//        filmy3.add(new Movie(7, "žluťoučký"));
//        filmy3.add(new Movie(7, "filmeček"));
//        filmy3.add(new Movie(7, "Maňáskového divadka"));
//        filmy3.add(new Movie(7, "říčníků"));
//        
//        Medium med3 = new Medium(55, "Blue Ray", filmy3);
//        
//        category.addMedium(med1);
//        category.addMedium(med2);
//        category.addMedium(med3);     
        
        logger.log(Level.TRACE, "Posilam kategorii: \r" + category);
        
        model.addAttribute("category", category);
        return "showCategory";
    }
    
    @RequestMapping(value = "rename/{categoryName}")
    public String renameCategory(@PathVariable String categoryName, @RequestParam String newName, Model model){
        logger.log(Level.TRACE, "Prejenovavam kategorii: '" + categoryName + "' na: '" + newName + "'");
        
        String decodedCategoryName = Helper.decodeEscapedString(categoryName);
        String decodedNewName = Helper.decodeEscapedString(newName);
        if(decodedCategoryName == null || newName == null){
            model.addAttribute("msg", "Error during decoding category name from url.");
            return "errorPage";
        }
        
        parser.renameCategory(new Category(decodedCategoryName), decodedNewName);
        manager.updateTempFileToGDrive();
        return "redirect:/category/showAll";
    }
    
    @RequestMapping(value = "showRenameForm/{categoryName}")
    public String showRenameForm(@PathVariable String categoryName, Model model){
        model.addAttribute("oldCategoryName", categoryName);
        return "renameCategory";
    }
    
    @RequestMapping(value = {"delete/{categoryName}"})
    public String deleteCategory(@PathVariable String categoryName, @RequestParam(required = false) String delete, Model model){

        logger.log(Level.TRACE, "delete: " + delete);
        
        String decodedCategoryName = Helper.decodeEscapedString(categoryName);
            if(decodedCategoryName == null){
            model.addAttribute("msg", "Error during decoding category name from url.");
            return "errorPage";
        }

        //Zobrazit upozorneni o smazani a potvrzeni
        if(delete == null){
            model.addAttribute("categoryName", decodedCategoryName);
            return "deleteCategoryConfirm";
        } else {
            if (delete.equals("delete"));
            parser.deleteCategory( new Category(decodedCategoryName) );
        }
        manager.updateTempFileToGDrive();
        return "redirect:/category/showAll";
    }
    
    @RequestMapping(value = "addCategory")
    public String addCategory(@RequestParam(required = false) String categoryName, Model model){
        if(categoryName == null){
            return "addCategory";
        }
        
        logger.log(Level.TRACE, "Ziskan nazev kategorie: " + categoryName);
        String decodedCategoryName = Helper.decodeEscapedString(categoryName);
            if(decodedCategoryName == null){
            model.addAttribute("msg", "Error during decoding category name from url.");
            return "errorPage";
        }
        logger.log(Level.TRACE, "Vytvarim kategorii: " + decodedCategoryName);
        parser.addCategory(new Category(decodedCategoryName));
        
        manager.updateTempFileToGDrive();
        return "redirect:/category/showAll";
    }
    
}
