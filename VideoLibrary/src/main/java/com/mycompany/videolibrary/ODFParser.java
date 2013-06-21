/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        
        
        int collumns = 0;
        Row head = rowList.get(0);
        
        for(int i = 0; i < head.getCellCount(); i++){
            if(head.getCellByIndex(i) == null || head.getCellByIndex(i).getDisplayText().equals("")) {
                break;
            }
            collumns++;
            logger.log(Level.DEBUG, head.getCellByIndex(i).getDisplayText());
        }
        logger.log(Level.DEBUG, "Collumns: " + collumns);

        int rowCount = rowList.size();
        for(int i=0; i < rowCount; i++){     //Vytahnout vsechny filmy z radku a vlozit je do noveho media

            logger.log(Level.TRACE, "Zpracovavam " + i + " radek.");
            //Vytvoprit nove medium
            Medium medium = new Medium();

            Row row = rowList.get(i);
            Cell firstCell = row.getCellByIndex(0);
            
            if(firstCell == null || firstCell.getDisplayText().equals(""))
                break;

            medium.setId( Integer.getInteger( firstCell.getDisplayText() ) );
            //medium.setType(); //TODO provest parsovani poznamky

            //int collumns = row.getCellCount();
            List<Movie> movies = new ArrayList<Movie>();

            for(int j = 1; j < collumns; j++){  //je potreba preskocit prvni sloupec, ktery obsahuje ID
                Cell cell = row.getCellByIndex(j);
                
                if(cell == null || cell.getDisplayText().equals(""))
                    continue;
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
        throw new UnsupportedOperationException("Not inplemented yet!");
    }
    
    public void deleteMedium(Medium medium, Category category) {
        throw new UnsupportedOperationException("Not inplemented yet!");
    }
    
    public List<Medium> findMediumsByMovieName(String name) {
        throw new UnsupportedOperationException("Not inplemented yet!");
    } 
    //public List<Medium> findMediumsBy... 
    
    public void addCategory(Category category) {
        throw new UnsupportedOperationException("Not inplemented yet!");
    }
    
    public void renameCategory(Category category, String categoryNewName) {
        throw new UnsupportedOperationException("Not inplemented yet!");
    }
    
    public void deleteCategory(Category category) {
        throw new UnsupportedOperationException("Not inplemented yet!");
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
