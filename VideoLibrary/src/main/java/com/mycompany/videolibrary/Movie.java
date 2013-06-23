package com.mycompany.videolibrary;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
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
    
    public void setMetaInfo(String metaInfoString) throws DocumentException{
        if(metaInfo == null){
            metaInfo = new HashMap<String, String>();
        }
        
        SAXReader xmlReader = new SAXReader();
        StringReader stringR = new StringReader(metaInfoString);
        Document doc = xmlReader.read(new InputSource(stringR));
        Element root = doc.getRootElement();
        
        for(Object e : doc.getRootElement().elements()){
            String key = ((Element)e).getName().trim();
            String value = ((Element)e).getText().trim();
            metaInfo.put(key, value);
        }
                
        /*
         <multimediaData>
            <multimediumType>
                Hudební klip
            </multimediumType>
            <dataFormat>
                DivX
            </dataFormat>
            <otherExtensionPoint>
                    DivX
            </otherExtensionPoint>
          </multimediaData>
         */
    }
       
    public void setMetaInfo(Map<String, String> metaInfo){
        this.metaInfo = metaInfo;
    }
    
    public Map<String, String> getMetaInfo(){
        return metaInfo;
    }
    
    @Override
    public String toString(){
        return "(" + id + " " + name + ")";
    }  
}
