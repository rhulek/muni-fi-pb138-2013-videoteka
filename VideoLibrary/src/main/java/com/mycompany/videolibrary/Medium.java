/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary;

import java.util.List;

/**
 *
 * @author Martin
 */
public class Medium {
    private Integer id;
    private String type;
    private List<Movie> movies;

    public Medium(){
        
    }
    
    public Medium(Integer id, String type, List<Movie> movies) {
        this.id = id;
        this.type = type;
        this.movies = movies;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Movie> getMovies() {
        return movies;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
    }
    
    public void addMovie(Movie movie){
        movies.add(movie);
    }
    
    public boolean containsMovie(String name){
        if(name == null){
            return false;
        }
        for(Movie movie : movies){
            return movie.getName().equals(name);
        }
        return false;
    }
    
    @Override
    public String toString(){
        StringBuilder builder = new StringBuilder();
        
        builder.append("medium[");
        
        builder.append("id(" + id + "),");
        builder.append("type(" + type + "),");
        builder.append("movies(");
        
        for(Movie m: movies){
            builder.append(m.toString());
            builder.append(",");
        }
        
        builder.append(")");
        
        builder.append("]");
        
        return builder.toString();
    }
    
}
