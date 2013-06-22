/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.odftoolkit.simple.SpreadsheetDocument;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.draw.Textbox;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.text.Paragraph;
import org.odftoolkit.simple.text.Section;

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
                String movieName = cell.getDisplayText();
                logger.log(Level.TRACE, "Bunka: " + movieName);
                
                if( movieName.trim().length() > 0 ){
                    logger.log(Level.TRACE, "Pridavam bunku: " + movieName);
                    movies.add( new Movie(j, movieName) );    //TODO je treba provest parsovani poznamky a ziskat id filmu
                }
            }
            medium.setMovies(movies);
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
        
        Medium med1 = new Medium(335, "DVD", filmy);
        
        List<Movie> filmy2 = new ArrayList<Movie>();
        filmy2.add(new Movie(7, "Cerveny trakturek"));
        filmy2.add(new Movie(7, "Darbujan a pandrhola"));
        
        Medium med2 = new Medium(55, "Blue Ray", filmy2);
        
        
        List<Movie> filmy3 = new ArrayList<Movie>();
        filmy3.add(new Movie(7, "žluťoučký"));
        filmy3.add(new Movie(7, "filmeček"));
        filmy3.add(new Movie(7, "Maňáskového divadka"));
        filmy3.add(new Movie(7, "říčníků"));
        
        Medium med3 = new Medium(55, "Blue Ray", filmy3);
        
        category.addMedium(med1);
        category.addMedium(med2);
        category.addMedium(med3);
        
        return category;
    }
//    public Category getCategory(String categoryName){
//        if(!loadDocument()){
//            return null;
//        }
//        
//        List<Table> tableList = document.getTableList();
//        
//        if(tableList == null){
//            logger.log(Level.ERROR, "getTableList() returned null!");
//            return null;
//        }
//        
//        for (Table table: tableList){
//            
//            if(table.getTableName().equals(categoryName)){
//                List<Row> rowList = table.getRowList();
//                
//                Category category = new Category(categoryName);
//                
//                int rowCount = rowList.size();
//                for(int i=1; i < rowCount; i++){     //Vytahnout vsechny filmy z radku a vlozit je do noveho media
//                    
//                    //Vytvoprit nove medium
//                    Medium medium = new Medium();
//                    
//                    Row row = rowList.get(i);
//                    Cell firstCell = row.getCellByIndex(0);
//                    
//                    medium.setId( Integer.getInteger( firstCell.getDisplayText() ) );
//                    //medium.setType(); //TODO provest parsovani poznamky
//                    
//                    int collumns = row.getCellCount();
//                    List<Movie> movies = new ArrayList<Movie>();
//                    
//                    for(int j = 1; j < collumns; j++){  //je potreba preskocit prvni sloupec, ktery obsahuje ID
//                        Cell cell = row.getCellByIndex(j);
//                        
//                        movies.add( new Movie(null, cell.getDisplayText()) );    //TODO je treba provest parsovani poznamky a ziskat id filmu
//                    }
//                    medium.setMovies(movies);
//                    category.addMedium(medium);
//                }
//                
//                return category;
//            }
//        }
//        return null;
//    }
    
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
                        //TODO: dodelat ukladani poznamky
        }
        actualCat.addMedium(medium);
       
        changeHeader(table);
        category.setMediums(actualCat.getMediums());
        
        saveDocument();
    }
    
    public void deleteMedium(Medium medium, Category category) {
        if(!loadDocument()){
            return;
        }
        Table table = document.getTableByName(category.getName());
        
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
    
    public void parse(Category category) {
    }
    
    
    //public List<Medium> findMediumsBy... 
    
    public void addCategory(Category category) {
        if(!loadDocument()){
            return;
        }
        
        logger.log(Level.TRACE, "Vytvarim kategorii: " + category.getName());
        Table.TableBuilder builder = document.getTableBuilder();
        Table table = builder.newTable(1, 1);
        table.setTableName(category.getName());
        table.getCellByPosition(0, 0).setStringValue("Id");

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
    
    public void parse(){
        try {
			TextDocument doc = TextDocument.loadDocument("DemoTemplate.odt");
			Table roomtable = doc.getTableByName("RoomTable");
			roomtable.remove();
			Section templateSection = doc.getSectionByName("SectionForm");
			
			SpreadsheetDocument data = SpreadsheetDocument.loadDocument("Passengers.ods");
			Table table = data.getTableByName("passenger");
			int count = table.getRowCount();
			int type1Count=0,type2Count=0,type3Count=0;
			for(int i=1;i<count;i++)
			{
				Row row = table.getRowByIndex(i);
				for(int j=0;j<6;j++)
				{
					Paragraph para = templateSection.getParagraphByIndex(j, false);
					Textbox nameBox = para.getTextboxIterator().next();
					String content = row.getCellByIndex(j).getDisplayText();
					nameBox.setTextContent(content);
					
					if (j==5)
					{
						if (content.equals("Deluxe Room"))
							type1Count++;
						else if (content.equals("Studio/Junior Suite"))
							type2Count++;
						else if (content.equals("Executive Suite"))
							type3Count++;
					}
				}
				doc.appendSection(templateSection, false);
				doc.addParagraph(null);
			}
			templateSection.remove();
			roomtable.getCellByPosition(2,1).setStringValue(type1Count+"");
			roomtable.getCellByPosition(2,2).setStringValue(type2Count+"");
			roomtable.getCellByPosition(2,3).setStringValue(type3Count+"");
			
			doc.getContentRoot().appendChild(roomtable.getOdfElement());
			doc.save("output.odt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
