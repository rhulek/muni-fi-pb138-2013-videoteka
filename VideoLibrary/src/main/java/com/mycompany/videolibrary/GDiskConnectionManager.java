/*
 * This Class create and maintain connection to Google Disk
 * and open the template in the editor.
 */
package com.mycompany.videolibrary;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 *
 * @author Martin
 */
public class GDiskConnectionManager {
    private static final Logger logger = LogManager.getLogger(GDiskConnectionManager.class.getName());
    
    public static long FILE_SIZE_CONSTRAINT = 104857600;    //restrict size of file to 100MB
    private static String ODF_FORMAT_EXPORT_CONSTANT = "application/x-vnd.oasis.opendocument.spreadsheet";
    private static int FILE_BUFFER_SIZE = 2048;

    public GDiskConnectionManager() {
        
    }
    
 
    private static List<File> retrieveAllFiles(Drive service) throws IOException {
    List<File> result = new ArrayList<File>();
    Files.List request = service.files().list();

    do {
      try {
        FileList files = request.execute();

        result.addAll(files.getItems());
        request.setPageToken(files.getNextPageToken());
      } catch (IOException e) {
        System.out.println("An error occurred: " + e);
        request.setPageToken(null);
      }
    } while (request.getPageToken() != null &&
             request.getPageToken().length() > 0);

    return result;
  }
    
  public File getFileByName(String filename, Drive service){
      
        try {
            List<File> files = retrieveAllFiles(service);
            
            for (File f: files){
                System.out.println("Title: " + f.getTitle());
                System.out.println("ID: " + f.getId());
                System.out.println("Size: " + f.getFileSize());
                
                if(filename.equals(f.getTitle())){
                    logger.info("Soubor nalezen!");
                    return service.files().get(f.getId()).execute();
                }
            }
            logger.info("Soubor NENALEZEN!");
            
        } catch (IOException ex) {
            logger.log(Level.ERROR, ex);
        }
        
      return null;
  }
  
  
  public static InputStream downloadFileODF(Drive service, File file) {
    
    if(file == null) return null;
//    if(file.getFileSize() > FILE_SIZE_CONSTRAINT) {
//        throw new RuntimeException("File is too big! Maximal supported file size is 100 MB");
//    }
    
    //prepare export url
    String odfExportLink = file.getExportLinks().get(ODF_FORMAT_EXPORT_CONSTANT);
    if(odfExportLink == null){
        logger.error("ODF export link is null!");
        return null;
    }
    
      try {
        HttpResponse resp = service.getRequestFactory()
                                    .buildGetRequest(new GenericUrl(odfExportLink))
                                    .execute();
        
        return resp.getContent();
        
      } catch (IOException e) {
        // An error occurred.
          //TODO log it here
        e.printStackTrace();
        return null;
      }
  }
  
  /*
   * This method takes input sream and save it to local temporary file.
   * Returns this new created file.
   */
  public static java.io.File getTempFile(java.io.InputStream is) {
      byte[] buffer = new byte[FILE_BUFFER_SIZE];
      
      if(is == null){
          logger.error("Doslo k chybe pri stahovani souboru.");
          return null;
      }
      
//      InputStreamReader r = new InputStreamReader( is );
//      BufferedReader dataReader = new BufferedReader( r );
      
      java.io.File tempFile = new java.io.File("tempFile.odf");
      java.io.OutputStream outS = null;
      
      try {
         outS = new java.io.FileOutputStream(tempFile);
        
      } catch (FileNotFoundException ex) {
        logger.log(Level.ERROR, "Error while creating temporary file", ex);
        return null;
      }
      
      try{
        int read = 0;
        while( (read = is.read(buffer)) != -1 ){
            outS.write(buffer, 0, read);
        }
      } catch (java.io.IOException ex ){
          logger.error("Error while writing into temporary file: " + ex);
      }
      
      
      return tempFile;
  }
  
}
