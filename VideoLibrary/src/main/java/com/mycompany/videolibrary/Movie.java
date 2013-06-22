package com.mycompany.videolibrary;

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
    private String comment;

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
    
    public void setComment(String comment){
        this.comment = comment;
    }
    
    public String getComment(){
        return comment;
    }
    
    @Override
    public String toString(){
        return "(" + id + " " + name + ")" + "comment " + comment;
    }
}
