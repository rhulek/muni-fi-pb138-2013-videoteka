package com.mycompany.videolibrary;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Martin
 */
public class Movie {
    private Integer id;
    private String name;
    private Map<String, String> metaInfo;

    public Movie(){
    
    }
    
    public Movie(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Parsuje text strukturovany jako xml na hashMapu obsahujici jednotlive vlastnosti a hodnoty.
     * 
     * @param metaInfoString
     * @throws DocumentException 
     */
    public void setMetaInfo(String metaInfoString) throws DocumentException{
        if(metaInfo == null){
            metaInfo = new HashMap<String, String>();
        }
        
        SAXReader xmlReader = new SAXReader();
        StringReader stringR = new StringReader(metaInfoString);
        Document doc = xmlReader.read(new InputSource(stringR));
        Element root = doc.getRootElement();
        
        for(Object e : doc.getRootElement().elements()){
            String key = ((Element)e).attributeValue("name").trim();
            String value = ((Element)e).getText().trim();
            metaInfo.put(key, value);
        }
    }
       
    public void setMetaInfo(Map<String, String> metaInfo){
        this.metaInfo = metaInfo;
    }
    
    public Map<String, String> getMetaInfo(){
        return metaInfo;
    }
    
    /**
     * Vyhleda v hashMape pozadovanou meta informaci a v pripade existence vrati jeji hodnotu
     * @param propertyName
     * @return 
     */
    public String getNoteProperty(String propertyName){
        if(metaInfo != null){
            return metaInfo.get(propertyName);
        }
        return null;
    }
    
    /**
     * Zjistuje zda hashMapa obsahuje danou meta informaci
     * @param propertyName
     * @return 
     */
    public Boolean hasNoteProperty(String propertyName){
        if(metaInfo != null){
            return metaInfo.containsKey(propertyName);
        }
        return false;
    }
    
    public String getMetaInfoXML(){
        if(metaInfo == null){
            return null;
        }
        Iterator it = metaInfo.entrySet().iterator();
        String output = "<multimediaData>";
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            pair.getValue();
            output = output + "<data name=\"" + pair.getKey() + "\">" + pair.getValue() + "</data>";      
            it.remove(); // avoids a ConcurrentModificationException
        }
         output = output + "</multimediaData>";
        return output;
    }
    
    @Override
    public String toString(){
        return "(" + id + " " + name + ")";
    }  
}
