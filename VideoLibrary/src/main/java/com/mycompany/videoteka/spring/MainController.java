/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videoteka.spring;

import com.mycompany.videolibrary.GDiskManagerWeb;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Martin
 */

@Controller
public class MainController {
    private static Logger logger = LogManager.getLogger(MainController.class.getName());
    
    @Autowired
    private GDiskManagerWeb manager;
    
    //Provede registraci aplikace, pripadne nahrani credentials a refresh tokenu
    @RequestMapping(value= {"/", "hello" }, method = RequestMethod.GET)
    public ModelAndView startVideostore(){
        logger.log(Level.DEBUG, "startVideostore(): Logujeme Vstup do funkce!");
        
        if(manager == null){
            logger.log(Level.ERROR, "GDiskManager je null!");
            return new ModelAndView("errorPage").addObject("msg", "GdiskManager je null!!!");
            
        }
        logger.log(Level.INFO, "GDiskManager je ok!");
        
        if(manager.loadCredentials() == null){
            manager.openBrowser();
            return new ModelAndView("insertCode");
        } else {
            
            if(!manager.refreshToken()){
                return new ModelAndView("errorPage").addObject("msg", "Chyba pri obnovovani tokenu");
            }
        }

        
        return new ModelAndView("newTestPage").addObject("msg", "Token byl uspesne nahran a obnoven. Nyni muzete pracovat s aplikaci. \r" + manager.getCredentials().getRefreshToken());
//        return new ModelAndView("errorPage").addObject("msg", "file://localhost/D:/Fotky/bonsai_by_johnbruk03_normalni_verze.JPG");
    }
    
    //TODO Provest autorizaci a ulozeni credentials pro budouci pouziti
    @RequestMapping(value = "autorize", method = RequestMethod.POST)
    public ModelAndView autorizeApp(@RequestParam("authCode") String code){
        logger.log(Level.INFO, "Byl ziskan autorizacni kod: '" + code + "'");
        
        if(manager.autorizeAndSaveCredentials(code)){
            return new ModelAndView("newTestPage").addObject("msg", "Aplikace uspesne autorizovana.");
        } else {
            return new ModelAndView("errorPage").addObject("msg", "Chyba pri autorizaci aplikace!");
        }
    }
    
    
    @RequestMapping(value = "*", method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
    public ModelAndView fallbackMethod(){
        logger.log(Level.DEBUG, "fallbackMethod(): Je proveden nezachyceny request!!!");
        return new ModelAndView("newTestPage").addObject("msg", "Byla zavolana fallback metoda.");
    }
}
