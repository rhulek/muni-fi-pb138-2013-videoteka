/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.DocumentException;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.draw.Textbox;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.text.Paragraph;
import org.odftoolkit.simple.text.Section;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Martin
 */
public class ODFParser {
    private static Logger logger = LogManager.getLogger(ODFParser.class.getName());
    private String documentPath;
    private SpreadsheetDocument document;

    public ODFParser(){
        
    }
    
    public ODFParser(String documentPath) {
        logger.log(Level.DEBUG, "Default charset:" + Charset.defaultCharset());
        logger.log(Level.DEBUG, "file.encoding=" + System.getProperty("file.encoding"));
        this.documentPath = documentPath;
    }

    public String getDocumentPath() {
        return documentPath;
    }

    public void setDocumentPath(String documentPath) {
        this.documentPath = documentPath;
    }

    public SpreadsheetDocument getDocument() {
        return document;
    }

    public void setDocument(SpreadsheetDocument document) {
        this.document = document;
    }
    
    public boolean loadDocument(){
        if(document != null){
            return true;
        }
        
        logger.log(Level.INFO, "document is null. Loading document: " + documentPath);
            
        if(documentPath == null){
            logger.log(Level.ERROR, "Document path is null!");
            return false;
        }
        
        try {
            document = SpreadsheetDocument.loadDocument(documentPath);
            return true;
        } catch (Exception ex) {
            logger.log(Level.ERROR, "Chyba pri nahravani dokumentu: '" + documentPath + "'", ex);
            return false;
        }
        
    }
    
    public boolean saveDocument() {
        if(document == null){
            logger.log(Level.ERROR, "Document neni nahran.");
            return false;
        }
        
        
            
        if(documentPath == null){
            logger.log(Level.ERROR, "Document path is null!");
            return false;
        }
        
        try {
            document.save(documentPath);
            return true;
        } catch (Exception ex) {
            logger.log(Level.ERROR, "Chyba pri ukladani dokumentu: '" + documentPath + "'", ex);
            return false;
        }
    }
    
    
    
    public List<String> getAllCategoryNames(){
        if(!loadDocument()){
            return null;
        }
        
        List<String> categorNames = new ArrayList<String>();
        List<Table> tables = document.getTableList();
        
        if(tables == null){
            logger.log(Level.ERROR, "Nepodarilo se ziskat seznam tabulek!");
        }
        
        for(Table table: tables){
            String name = table.getTableName();
            categorNames.add(name);
            logger.log(Level.TRACE, "Pridavam kategorii: " + name);
        }
        
        return categorNames;
    }

    public Category getCategory(String categoryName){
        if(!loadDocument()){
            return null;
        }
        return getCategory(categoryName, this.document);
    }
    
     public Category getCategory(String categoryName, SpreadsheetDocument document){

        Table table = document.getTableByName(categoryName);
        
        if(table == null){
            logger.log(Level.ERROR, "getTableByName returned null!");
            return null;
        }

        List<Row> rowList = table.getRowList();

        logger.log(Level.TRACE, "Pocet radku tabulky: " + rowList.size());
        Category category = new Category(categoryName);
        
        int rowCount = rowList.size();
       
        for(int i=1; i < rowCount; i++){     //Vytahnout vsechny filmy z radku a vlozit je do noveho media

            Row row = rowList.get(i);
            Cell firstCell = row.getCellByIndex(0);
            
            if(firstCell == null || firstCell.getDisplayText().equals(""))
                break;
          
            //Vytvoprit nove medium
            Medium medium = new Medium();
            medium.setId( firstCell.getDoubleValue().intValue() );  //Throw IllegalArgumentException if the cell type is not "float".
            //medium.setId( Integer.getInteger( firstCell.getDisplayText() ) );
            
            //medium.setType(); //TODO provest parsovani poznamky
       
             int collumns = row.getCellCount();
            List<Movie> movies = new ArrayList<Movie>();
           
            for(int j = 1; j < collumns; j++){  //je potreba preskocit prvni sloupec, ktery obsahuje ID
                Cell cell = row.getCellByIndex(j);

                
                if(cell == null || cell.getDisplayText().equals(""))
                    break;
                
                

               

                
                String movieName = "";
                
                
                
                String movieComment = cell.getNoteText();
                NodeList nodes = cell.getOdfElement().getChildNodes();
                for(int k = 0; k < nodes.getLength(); k++) {
                    Node node = nodes.item(k); 
                    if(nodes.item(k).getLocalName().equals("p")) {
                        movieName = node.getTextContent();
                    }
                    
                   
                }
                
                
            
                if(movieComment != null){
                     logger.log(Level.TRACE, "Comment: " + movieComment); 
                }                
                
                if( movieName.trim().length() > 0 ){
                    logger.log(Level.TRACE, "Pridavam bunku: " + movieName);
                    Movie movie = new Movie(j, movieName);
                    if(movieComment != null && !movieComment.trim().isEmpty()){
                        try{
                            movie.setMetaInfo(movieComment);
                        }catch(DocumentException ex){
                          logger.log(Level.ERROR, ex);
                        }
                    }      
                    movies.add(movie);
                    //movies.add( new Movie(j, movieName) );    //TODO je treba provest parsovani poznamky a ziskat id filmu
                }
            }
            medium.setMovies(movies);
            medium.setCategory(category);
            category.addMedium(medium);
            
        }

        return category;

    }
     
    public Category getStaticTestCategory(){
        Category category = new Category("Pejsek a kocicka");
        List<Movie> filmy = new ArrayList<Movie>();
        filmy.add(new Movie(10, "Macha sebestova"));
        filmy.add(new Movie(1, "Na samote u lesa"));
        filmy.add(new Movie(3, "Nosorozec Tom"));
        
        Medium med1 = new Medium(335, "DVD", filmy, category);
        
        List<Movie> filmy2 = new ArrayList<Movie>();
        filmy2.add(new Movie(7, "Cerveny trakturek"));
        filmy2.add(new Movie(7, "Darbujan a pandrhola"));
        
        Medium med2 = new Medium(55, "Blue Ray", filmy2, category);
        
        
        List<Movie> filmy3 = new ArrayList<Movie>();
        filmy3.add(new Movie(7, "žluťoučký"));
        filmy3.add(new Movie(7, "filmeček"));
        filmy3.add(new Movie(7, "Maňáskového divadka"));
        filmy3.add(new Movie(7, "říčníků"));
        
        Medium med3 = new Medium(55, "Blue Ray", filmy3, category);
        
        category.addMedium(med1);
        category.addMedium(med2);
        category.addMedium(med3);
        
        return category;
    }

    
//    public List<Medium> getAllFilms(){
//
//        if(!loadDocument()){
//            return null;
//        }
//
//        List<Medium> movies = new ArrayList<Medium>();
//        
//        logger.log(Level.TRACE, "document.getSheetCount(): " + document.getSheetCount());
//        document.getSheetCount();
//        
//        List<Table> tableList = document.getTableList();
//        
//        for(Table table: tableList){
//            List<Row> rowList = table.getRowList();
//            logger.log(Level.TRACE, "Table: " + table.getTableName() + " Row count: " + table.getRowCount());
//            
//            for(Row row: rowList){
//                int cellCount = row.getCellCount();
//                
//                logger.log(Level.TRACE, "Row: " + row.getRowIndex() + " Cell count: " + row.getCellCount());
//                for(int i=0; i < cellCount; i++){
//                    logger.log(Level.TRACE, " " + row.getCellByIndex(i).getDisplayText() + " ");
//                }
//                
//                
//            }
//        }
//        
//
//        
//        return new ArrayList<Medium>();
//        //return Collections.unmodifiableCollection( new ArrayList<Film>() );
////        throw new UnsupportedOperationException("Not inplemented yet!");
//    }
    
    public void addMedium(Medium medium, Category category) {
        if(!loadDocument()){
            return;
        }
        Category actualCat = getCategory(category.getName());
        Table table = document.getTableByName(actualCat.getName());
        
        Row row;
        if(actualCat.getMediums().size()+1 >= table.getRowCount()) {
            row = table.appendRow();
        } else {
            row = table.insertRowsBefore(actualCat.getMediums().size()+1, 1).get(0);
        }
        
        logger.log(Level.DEBUG, actualCat.getMediums().size());
        
        row.getCellByIndex(0).setDoubleValue((double)medium.getId());
        for(int i = 1; i < medium.getMovies().size()+1; i++) {
            row.getCellByIndex(i).setStringValue(medium.getMovies().get(i-1).getName());
            row.getCellByIndex(i).setNoteText(medium.getMovies().get(i-1).getMetaInfoXML()); 
        }
        actualCat.addMedium(medium);
       
        changeHeader(table);
        category.setMediums(actualCat.getMediums());
        
        saveDocument();
    }
    
    public void deleteMedium(Medium medium) {
        if(!loadDocument()){
            return;
        }
        Table table = document.getTableByName(medium.getCategory().getName());
        
        List<Row> rowList = table.getRowList();

        int rowCount = rowList.size();
        for(int i=1; i < rowCount; i++) {
            Cell cell = rowList.get(i).getCellByIndex(0);
            if(cell == null || cell.getStringValue().equals("")) {
                break;
            }
            
            if(cell.getDoubleValue().intValue() == medium.getId()) {

                table.removeRowsByIndex(i, 1);
                changeHeader(table);
                saveDocument();
                break;
            }
                
        }    
    }
    
    /*
     * Vrati vsechny media obsahujici zadany film
     */
    public List<Medium> findMediaByMovieName(String name) {
        if(!loadDocument()){
            return null;
        } 
        List<Table> tables = document.getTableList();
      
        if(tables == null){            
            logger.log(Level.ERROR, "Nepodarilo se ziskat seznam tabulek!");
        }
        List<Medium> media = new ArrayList<Medium>();
        for(Table table: tables){
            String catName = table.getTableName();
            List<Medium> categoryMedia = getCategory(catName).getAllMedia();
            for(Medium medium : categoryMedia){

                if(medium.containsMovie(name)){
                    media.add(medium);
                }
            }
        }
        return media;  
    }
    
    
    /*
     * Vrati prvni nalezeny film
     */
    public Movie findMovieByName(String name) {
        if(!loadDocument()){
            return null;
        } 
        List<Table> tables = document.getTableList();
      
        if(tables == null){            
            logger.log(Level.ERROR, "Nepodarilo se ziskat seznam tabulek!");
        }
        for(Table table: tables){
            String catName = table.getTableName();
            List<Medium> categoryMedia = getCategory(catName).getAllMedia();
            for(Medium medium : categoryMedia){
                if(medium.containsMovie(name)){
                    return medium.getMovie(name);
                }
            }
        }
        return null;  
    }
    
    public List<Medium> findMediaByMovieNameInCategory(String name, Category cat) {
        if(!loadDocument()){
            return null;
        } 
        Table table = document.getTableByName(cat.getName());
      
        if(table == null){            
            logger.log(Level.ERROR, "Nepodarilo se ziskat tabulku!");
        }
        List<Medium> media = new ArrayList<Medium>();

        String catName = table.getTableName();
        List<Medium> categoryMedia = getCategory(catName).getAllMedia();
        for (Medium medium : categoryMedia) {

            if (medium.containsMovie(name)) {
                media.add(medium);
            }
        }

        return media;  
    }
    
      public Movie findMovieByNameInCategory(String name, Category cat) {
        if(!loadDocument()){
            return null;
        } 
        Table table = document.getTableByName(cat.getName());
      
        if(table == null){            
            logger.log(Level.ERROR, "Nepodarilo se ziskat tabulku!");
          }

          String catName = table.getTableName();
          List<Medium> categoryMedia = getCategory(catName).getAllMedia();
          for (Medium medium : categoryMedia) {
              if (medium.containsMovie(name)) {
                  return medium.getMovie(name);
              }
          }

          return null;  
    }
    
    public List<Movie> findMoviesByNameAndMeta(String movieName, Map<String, String> meta) {
        if(!loadDocument()){
            return null;
        } 
        List<Table> tables = document.getTableList();
      
        if(tables == null){            
            logger.log(Level.ERROR, "Nepodarilo se ziskat seznam tabulek!");
        }
        List<Movie> movies = new ArrayList<Movie>();
        
        for(Table table: tables){
            String catName = table.getTableName();
            List<Medium> categoryMedia = getCategory(catName).getAllMedia();
            for(Medium medium : categoryMedia){
                for(Movie movie : medium.getMovies()) {
                    //boolean check = false;
                    
                    if (movieName.equals("") || movie.getName().equals(movieName)) {
                        if(movie.getMetaInfo().values().containsAll(meta.values())) {
                            movies.add(movie);
                        }
                    }
                    
                    /*
                    for(Entry<String, String> entry : meta.entrySet()){ 
                        if(movie.hasNoteProperty(entry.getKey())) {
                            if(movie.getNoteProperty(entry.getKey()).equals(entry.getValue())) {
                                check = true;
                                continue;
                            }
                        }
                        check = false;
                        break;
                    }
                    
                    if(check) {
                        movies.add(movie);
                    }
                    */
                }
            }
        }
        return movies; 
    }
    
    
    public List<Medium> findMediumByNameAndMeta(String movieName, Map<String, String> meta) {
        if(!loadDocument()){
            return null;
        } 
        List<Table> tables = document.getTableList();
      
        if(tables == null){            
            logger.log(Level.ERROR, "Nepodarilo se ziskat seznam tabulek!");
        }
        List<Medium> mediums = new ArrayList<Medium>();
        
        for(Table table: tables){
            String catName = table.getTableName();
            List<Medium> categoryMedia = getCategory(catName).getAllMedia();
            for(Medium medium : categoryMedia){
                boolean check = false;
                for(Movie movie : medium.getMovies()) {
                    
                    if (movieName.equals("") || movie.getName().equals(movieName)) {
                        if(movie.getMetaInfo().values().containsAll(meta.values())) {
                            check = true;
                        }
                    }
                     
                    /*
                    if (movieName.equals("") || movie.getName().equals(movieName)) {
                        for (Entry<String, String> entry : meta.entrySet()) {
                            if (movie.hasNoteProperty(entry.getKey())) {
                                if (movie.getNoteProperty(entry.getKey()).equals(entry.getValue())) {
                                    check = true;
                                    continue;
                                }
                            }
                            check = false;
                            break;
                        }
                    }
                    */
                }
                
                if(check) {
                    mediums.add(medium);
                }
            }
        }
        return mediums; 
    }
       /* 
        public String findMetaInfoAboutMovie(String name) {
        if(!loadDocument()){
            return null;
        } 
        List<Table> tables = document.getTableList();
      
        if(tables == null){            
            logger.log(Level.ERROR, "Nepodarilo se ziskat seznam tabulek!");
        }
        for(Table table: tables){
            String catName = table.getTableName();
            List<Medium> categoryMedia = getCategory(catName).getAllMedia();
            for(Medium medium : categoryMedia){
                if(medium.containsMovie(name)){
                    return "";
                }
            }
        }
        return null;  
    }   
    * */
        
    
    

    /*
     * Vytvori kategorii pokud neexistuje a prida media
     * nebo jen prida media pokud kategorie existuje
     */
    public void addCategory(Category category) {
        if(!loadDocument()){
            return;
        }


        if (document.getTableByName(category.getName()) == null) {

            logger.log(Level.TRACE, "Vytvarim kategorii: " + category.getName());
            Table.TableBuilder builder = document.getTableBuilder();
            Table table = builder.newTable(1, 1);
            table.setTableName(category.getName());
            table.getCellByPosition(0, 0).setStringValue("Id");

        }

        List<Medium> mediums = category.getAllMedia();

        for (Medium medium : mediums) {
            addMedium(medium, category);
        }

        saveDocument();
    }
    
    public void renameCategory(Category category, String categoryNewName) {
        if(!loadDocument()){
            return;
        }
        
        document.getTableByName(category.getName()).setTableName(categoryNewName);
        category.setName(categoryNewName);
        saveDocument();
    }
    
    public void deleteCategory(Category category) {
        if(!loadDocument()){
            return;
        }
        
        document.getTableByName(category.getName()).remove();
        saveDocument();
    }
    
    private void changeHeader(Table table) {
        //uprava hlavicky
        Category cat = getCategory(table.getTableName());
        
        int mediumMaxCount = 0;
        for(Map.Entry<Integer, Medium> entry : cat.getMediums().entrySet()) {
            if(entry.getValue().getMovies().size() > mediumMaxCount)
                mediumMaxCount = entry.getValue().getMovies().size();
        }   
  
        
        Row firstRow = table.getRowByIndex(0);  
        for(int i = 1; i < firstRow.getCellCount(); i++) {
            if(i < mediumMaxCount+1)
                firstRow.getCellByIndex(i).setStringValue("Film "+i);
            else
                firstRow.getCellByIndex(i).setStringValue("");
        }
        
    }
    
    /*
     * Imports content of file passed as attribute
     */
    public void merge(java.io.File fileToImport){
        logger.log(Level.DEBUG, "Importuju data ze souboru: " + fileToImport.getAbsolutePath());
        
        SpreadsheetDocument importedDocument;
        try {
            importedDocument = SpreadsheetDocument.loadDocument(fileToImport);
        } catch (Exception ex) {
            logger.log(Level.ERROR, "Chyba pri nahravani importovaneho souboru souboru: " + fileToImport.getName(), ex);
            return;
        }
        
        loadDocument();
        
        List<Table> importTables = importedDocument.getTableList();
        
        for(Table table: importTables){
            //do aktuálně nastaveného souboru v this.document přidá kategorii z importedDocument
            Category cat = getCategory(table.getTableName(), importedDocument);
            logger.log(Level.TRACE, "kopiruji kategorii:_______________________ \r"
                    + cat);
            
            addCategory( cat );
        }
    }
}
    

