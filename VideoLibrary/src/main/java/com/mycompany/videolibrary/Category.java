/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary;

import java.util.HashMap;
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
        mediums.put(medium.getId(), medium);
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
