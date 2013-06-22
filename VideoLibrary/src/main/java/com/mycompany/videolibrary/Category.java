/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin
 */
public class Category {
    private String name;
    private Map<Integer, Medium> mediums;

    public Category() {
        this(null);
    }

    public Category(String name) {
        this(name, new HashMap<Integer, Medium>());
    }

    public Category(String Name, Map<Integer, Medium> mediums) {
        this.name = Name;
        this.mediums = mediums;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public Map<Integer, Medium> getMediums() {
        return mediums;
    }

    public void setMediums(Map<Integer, Medium> mediums) {
        this.mediums = mediums;
    }
    
    public void addMedium(Medium medium){
        if(medium.getId() == null){
            System.err.println("addMedium medium with null id");
        }
        mediums.put(medium.getId(), medium);
        
    }
    
    public List<Medium> getAllMedia(){
        return new ArrayList<Medium>(mediums.values());    
    }
    
    public List<Medium> getAllMediaOfCategoryContainingMovie(String movieName){
        if(mediums == null){
            return null;
        }
        if(mediums.isEmpty()){
            return null;
        }
        if(movieName == null){
            return null;
        }
        Iterator it = mediums.entrySet().iterator();
        List<Medium> media = new ArrayList<Medium>();
        while (it.hasNext()) {
            Map.Entry pairs = (Map.Entry)it.next();
            Medium medium = (Medium)pairs.getValue();
            if(medium.containsMovie(movieName)){
                media.add(medium);
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
        return media;       
    }
    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder(name);
        
        builder.append("\r[");
        for(Medium medium: mediums.values()){
            builder.append(medium.toString());
        }
        builder.append("\r]");
        
        return builder.toString();
    }
}
