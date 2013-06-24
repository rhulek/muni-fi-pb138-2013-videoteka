/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videoteka.spring;

import com.mycompany.videolibrary.GDiskManagerWeb;
import com.mycompany.videolibrary.ODFParser;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Martin
 */
@Controller
@RequestMapping("file")
public class FileController { 
    private static Logger logger = LogManager.getLogger(MediumController.class.getName());
    
    @Autowired
    private GDiskManagerWeb manager;
    
    @RequestMapping("export")
    public ModelAndView sendToClient(HttpServletRequest request, HttpServletResponse response) throws IOException{
        
        //Dvoji volani pro ziskani docasneho souboru muze byt nebezpecne zvlaste pokud by se uzivateli podarilo soubor na serveru editovat
        response.setContentType(GDiskManagerWeb.ODS_FORMAT_EXPORT_CONSTANT);
        response.setContentLength((int) manager.getTempFile().length());
        response.setHeader("Content-Disposition", "attachment");
        response.setHeader("Content-Disposition", "attachment; filename=videoknihovna.ods");
        //response.setHeader("Content-Disposition: attachment; filename=videoknihovna.ods;");
        
        FileCopyUtils.copy(manager.getTempFileInputStream(), response.getOutputStream());
        
        return null;
    }
    
    @RequestMapping(value = "saveTest")
    public String saveFile(Model model){
        
//        model.addAttribute("msg", "file:///D:/IMG_0011.JPG");
        //model.addAttribute("msg", "www.centrum.cz");
        return "redirect:http://www.google.com";
    }
}
