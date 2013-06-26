/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videoteka.spring;

import com.mycompany.videolibrary.GDiskManagerWeb;
import com.mycompany.videolibrary.ODFParser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
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
    
    @Autowired
    private ODFParser parser;
    
    //Odesle soubor klientovi
    @RequestMapping("export")
    public String sendToClient(HttpServletRequest request, HttpServletResponse response) throws IOException{
        
        //Dvoji volani pro ziskani docasneho souboru muze byt nebezpecne zvlaste pokud by se uzivateli podarilo soubor na serveru editovat
        response.setContentType(GDiskManagerWeb.ODS_FORMAT_EXPORT_CONSTANT);
        response.setContentLength((int) manager.getTempFile().length());
        response.setHeader("Content-Disposition", "attachment");
        response.setHeader("Content-Disposition", "attachment; filename=videoknihovna.ods");
        //response.setHeader("Content-Disposition: attachment; filename=videoknihovna.ods;");
        
        FileCopyUtils.copy(manager.getTempFileInputStream(), response.getOutputStream());
        
        return null;
    }
  
    //Naplni a zobrazi stranku pro zadani udaju a vyber souboru pro import
    @RequestMapping(value = "import", method = RequestMethod.GET)
    public String showImportFileForm(Model model){
        logger.log(Level.TRACE, "Nastavuji beanu pro inport.");
        ImportFileBackingBean importBean = new ImportFileBackingBean();
        
        Map<String, String> importOptions = new HashMap<String, String>();
        importOptions.put(ImportFileBackingBean.REPLACE_FILE_OPTION, "Nahradit");
        importOptions.put(ImportFileBackingBean.APPEND_TO_FILE_OPTION, "Přidat");
        
        importBean.setImportOption(ImportFileBackingBean.REPLACE_FILE_OPTION);  //Nastaveni defaultni polozky
        importBean.setImportOptions(importOptions);
        
        model.addAttribute("importBean", importBean);        
        return "fileInput";
    }
    
    //provede ulozeni samotneho souboru
    @RequestMapping(value = "import", method = RequestMethod.POST)
    public String importFile(Model model, HttpServletRequest request){
//        logger.log(Level.TRACE, "Provadim import soubor: '" + importBean.getFilePath() + "' volba: " + importBean.getImportOption());
        
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartRequest.getFile("filePath");
        
        String importOption = multipartRequest.getParameter("importOption");
        logger.log(Level.TRACE, "Informace o prijmanem souboru: getOriginalFilename(): "
                            + multipartFile.getOriginalFilename() + "\r getContentType(): "
                            + multipartFile.getContentType() + "\r import option: "
                            + importOption);
    
        java.io.File importedFile = null;
        try {
            //Ulozi soubor na disk pro dalsi zpracovani
            importedFile = GDiskManagerWeb.createFile( multipartFile.getInputStream(), GDiskManagerWeb.IMPORTED_FILE_NAME);
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Chyba pri ukladani importovaneho souboru:", ex);
        }
        
        if(ImportFileBackingBean.APPEND_TO_FILE_OPTION.equals(importOption)){
            //Pripojime obsah souboru k aktualnimu souboru
            parser.merge(importedFile);
        } else {
            //nahradime puvodni soubor
            parser.closeDocument();
            manager.replaceTempFile(importedFile);
            parser.reloadDocument();
        }
        
        manager.updateTempFileToGDrive();
        return "redirect:/category/showAll";
    }
    
//    @RequestMapping(value = "saveTest")
//    public String saveFile(Model model){
//        
////        model.addAttribute("msg", "file:///D:/IMG_0011.JPG");
//        //model.addAttribute("msg", "www.centrum.cz");
//        return "redirect:http://www.google.com";
//    }
}
