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
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class GDiskConnectionManager {
    private static final Logger logger = Logger.getLogger(GDiskConnectionManager.class.getName());
    
    public static long FILE_SIZE_CONSTRAINT = 104857600;    //restrict size of file to 100MB
    private static String ODF_FORMAT_EXPORT_CONSTANT = "application/x-vnd.oasis.opendocument.spreadsheet";

    public GDiskConnectionManager() {
        logger.setLevel(Level.ALL);
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
            Logger.getLogger(GDiskConnectionManager.class.getName()).log(Level.SEVERE, null, ex);
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
        logger.severe("ODF export link is null!");
        return null;
    }
    
      try {
        HttpResponse resp =
            service.getRequestFactory().buildGetRequest(new GenericUrl(odfExportLink))
                .execute();
        return resp.getContent();
        
      } catch (IOException e) {
        // An error occurred.
          //TODO log it here
        e.printStackTrace();
        return null;
      }

    
  }
    
}
