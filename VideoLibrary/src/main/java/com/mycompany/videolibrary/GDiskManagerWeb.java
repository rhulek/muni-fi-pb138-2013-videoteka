/*
 * Trida starajici se o pripojeni ke Google Drive, vytvareni a obnovu tokenu
 * stahovani a update zmen na serveru
 * 
 * TODO Pravdepodobne bude potreba jeste refactoring na subclassy kvuli Springovemu api
 */
package com.mycompany.videolibrary;

import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author Martin
 */
public class GDiskManagerWeb {
    private static Logger logger = LogManager.getLogger(GDiskManagerWeb.class.getName());
    private static String APPLICATION_NAME = "videoteka";
    private static String TEMP_FILE = "tmpFile.ods";
    private static String SERVER_FILE_NAME = "videoteka_data";
    private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private static String DEFAULT_USER_ID = "defaultUser";
    public static String ODS_FORMAT_EXPORT_CONSTANT = "application/x-vnd.oasis.opendocument.spreadsheet";
    public static String IMPORTED_FILE_NAME = "imported.ods";
    private static int FILE_BUFFER_SIZE = 2048;
    
    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;
    private java.io.File credentialsFile;
    private java.io.File tempFile;
    private AuthFlowAndURL flowAndAuthURL;
    private File googleFile;
    
    //TODO nacitat z properities
    private String CLIENT_ID = "702406823762.apps.googleusercontent.com";
    private String CLIENT_SECRET = "iKIHHkC-wKEC7JS9YkzmDX_n";
    private String CREDENTIALS_FILE = "credentials.txt";
    private String GOOGLE_FILE_ID = "0AotGtmQ-kiV4dGJtLXQ0R3VELWNkSWF5QkNEX1o4enc";  //ulozeni by umoznilo pracovat porad se stejnum souborem i v pripade ze uzivatel zmeni nazev, nebo prida soubor se stejnym nazvem
    
    private GoogleCredential credentials;
    

    
    public GDiskManagerWeb() {
        logger.log(Level.TRACE, "Constructor invoke.");
        this.httpTransport = new NetHttpTransport();
        this.jsonFactory = new JacksonFactory();
        
        this.credentialsFile = new java.io.File(CREDENTIALS_FILE);
        this.credentials = new GoogleCredential.Builder().setJsonFactory(jsonFactory)
                                                        .setTransport(httpTransport)
                                                        .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                                                        .build();
        
        flowAndAuthURL = new AuthFlowAndURL(httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
        //this.initialize();
    }
    
//    protected void setClientID(String id){ this.CLIENT_ID = id; }
//    protected void setClientSecret(String secret) { this. CLIENT_SECRET = secret; }
    
    /*
     * Returns authorization URL
     */
    public String getAuthorizationURL(){
        if(flowAndAuthURL == null){
            logger.log(Level.ERROR, "AuthFlowAndURL hasn't been set!");
            return null;
        }
        
        return flowAndAuthURL.getAuthUrl();
    }
    
    /*
     * Opens web browser with authorization URL
     */
    public boolean openBrowser(){
        String url = getAuthorizationURL();
        if(url == null){
            logger.log(Level.ERROR, "Authorization url in AuthFlowAndURL hasn't been set");
            return false;
        }
        return openBrowser(url);
    }
    
    /*
     * Opens Web browser with specified URL if this is supported by OS
     */
    public boolean openBrowser(String url){
        //When it is possible open web browser with url
            if(!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
                logger.log(Level.WARN, "Open autorization URL in browser is not supported");
                
            } else {
                try {
                   Desktop.getDesktop().browse( new java.net.URI(url) );
                   return true;
                } catch (URISyntaxException ex) {
                    logger.log(Level.ERROR, "Error while opening browser:", ex);
                    
                } catch (java.io.IOException ex) {
                    logger.log(Level.ERROR, "Error while opening browser:", ex);
                
                }
            }
            return false;
    }
    
    /*
     * Provede autorizaci na zaklade predaneho tokenu a ziskane data ulozi o souboru
     */
    public boolean autorizeAndSaveCredentials(String authorizationCode){
         
        GoogleAuthorizationCodeFlow authorizationFlow = flowAndAuthURL.getAuthFlow();
        
        GoogleAuthorizationCodeTokenRequest authCodeTokeReqest =  authorizationFlow.newTokenRequest(authorizationCode);
        authCodeTokeReqest.setRedirectUri(REDIRECT_URI);

        //Get authentication Token
        GoogleTokenResponse flowResponse = null;
        try {
            flowResponse = authCodeTokeReqest.execute();
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Chyba pri ziskavani authentizacniho tokenu!", ex);
        }

        //Create credentials for future use
        credentials.setFromTokenResponse(flowResponse);

        //Save credentials for later use
        FileCredentialStore credentialStore;
        try {
            credentialStore = new FileCredentialStore(credentialsFile, jsonFactory);
            credentialStore.store(DEFAULT_USER_ID, credentials);
        } catch (IOException ex) {
            logger.log(Level.ERROR, ex);
        }

        logger.log(Level.INFO, "Credentials uspesne vytvoreny a ulozeny v '" + credentialsFile.getAbsolutePath() + "'");
        
        return true;
    }
    
   
    public String getGoogleFileID(){
        return GOOGLE_FILE_ID;
    }
    
    public void setGoogleFileID(String googleFileID){
        this.GOOGLE_FILE_ID = googleFileID;
    }
    
    public GoogleCredential getCredentials(){
        return this.credentials;
    }
    
    public void setCredentials(GoogleCredential cred){
        this.credentials = cred;
    }

    public java.io.File getCredentialsFile() {
        return credentialsFile;
    }

    public void setCredentialsFile(java.io.File credentialsFile) {
        this.credentialsFile = credentialsFile;
    }
    
    /*
     * Loads credentials from file specified inside this class
     */
    @SuppressWarnings({"null", "ConstantConditions"})
    public GoogleCredential loadCredentials(){
        if(!checkCredentialsFile()) {
           return null;
        }
        
        logger.log(Level.DEBUG, "Credentials file was found loading data.");
            
        FileCredentialStore credentialStore = null;
        try {
            credentialStore = new FileCredentialStore(credentialsFile, jsonFactory);
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Chyba pri otevirani credentials file: ", ex);
        }

        if(credentials == null){
            logger.log(Level.ERROR, "Error while loading credentials. Credentials variable is null!");
            return null;
        }

        if (!credentialStore.load(DEFAULT_USER_ID, credentials)) {
            logger.log(Level.FATAL, "Chyba pri nahravani credentials");
            return null;
        } else {
            logger.log(Level.TRACE, "Credentials uspesne nahrany: \r\tAccess token: "
                    + credentials.getAccessToken() + "\r\tRefresh token: "
                    + credentials.getRefreshToken());
        }
        return credentials;
    }
    
    /*
     * Returns true if credentials file is present and contain correct informations
     */
    public boolean checkCredentialsFile(){
        if(credentialsFile != null){
            if(!credentialsFile.exists()){
                logger.log(Level.WARN, "Credential file doesn't exist in path: '"
                        + credentialsFile.getAbsolutePath() + "'");
                return false;
            }
            
            if(!credentialsFile.canRead()){
                logger.log(Level.ERROR, "Cannot read credentials file in path: '"
                        + credentialsFile.getAbsolutePath() + "'");
                return false;
            }
            
            return true;
        }
        logger.log(Level.ERROR, "Credentials file hasn't been set!");
        return false;
    }
    
    /*
     * Provede refresh tokenu
     * Musi byt nahrany validni credentials
     */
    public boolean refreshToken(){
        try {
                if( credentials.refreshToken() ){
                    logger.log(Level.TRACE, "Token byl uspesne obnoven: \r\tAccess token: " 
                                + credentials.getAccessToken() + "\r\tRefresh token: "
                                + credentials.getRefreshToken());
                    return true;
                } else {
                    logger.log(Level.FATAL, "Chyba pri obnoveni tokenu!");
                    return false;
                }
            } catch (IOException ex) {
                logger.log(Level.FATAL, "Chyba pri obnoveni tokenu: ", ex);
                return false;
            }
    }
    
    /*
     * Return all files which contains account on Google Drive
     * @return files which contains account on Google Drive
     */
    public static List<File> retrieveAllFiles(Drive service) {
        logger.log(Level.TRACE, "Ziskavam seznam vsech souboru na Google Drive muze to chvili trvat.");
        List<File> result = new ArrayList<File>();
        Files.List request;
        try {
            request = service.files().list();
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Chyba pri ziskavani seznamu souboru: ", ex);
            return null;
        }

        do {
          try {
            FileList files = request.execute();

            result.addAll(files.getItems());                //pridej stranku
            request.setPageToken(files.getNextPageToken()); //bez na dalsi stranku
          } catch (IOException e) {
            System.out.println("An error occurred: " + e);
            request.setPageToken(null);
          }
        } while (request.getPageToken() != null &&
                 request.getPageToken().length() > 0);

        return result;
    }
        
    
    /*
     * This method gets InputStream with content of file.
     * @return InputStream containing file content
     */
    public static InputStream getFileContentODF(Drive service, File file) {
    
    if(file == null) {
        logger.log(Level.ERROR, "File is null");
        return null;
    }
    
    if(service == null) {
        logger.log(Level.ERROR, "Service is null");
        return null;
    }
    
    //prepare export url
    String odfExportLink = file.getExportLinks().get(ODS_FORMAT_EXPORT_CONSTANT);
    if(odfExportLink == null){
        logger.error("ODF export link is null!");
        return null;
    }
    logger.log(Level.TRACE, "Export link: " + odfExportLink);
    
      try {
        //Prepare response  to server for file content
        HttpResponse resp = service.getRequestFactory()
                                    .buildGetRequest(new GenericUrl(odfExportLink))
                                    .execute();
        
        return resp.getContent();
        
      } catch (IOException e) {
        logger.log(Level.ERROR, "Chyba pri ziskavani obsahu souboru na Google Drive", e);
        return null;
      }
    }
    
     /*
     * Save content of InputStream to temporary file.
     */
    public static java.io.File createFile(java.io.InputStream is, String filePath) {
        logger.log(Level.TRACE, "Vytvarim docasny soubor: " + filePath);
        byte[] buffer = new byte[FILE_BUFFER_SIZE];

        if(is == null){
            logger.error("Doslo k chybe pri stahovani souboru. InputSteram is null!");
            return null;
        }

        java.io.File outFile = new java.io.File(filePath);
        java.io.OutputStream outS;

        try {
            outS = new java.io.FileOutputStream(outFile);

        } catch (FileNotFoundException ex) {
            logger.log(Level.ERROR, "Error while creating temporary file", ex);
            return null;
        }

        //copy content from input stream
        try{
            int read = 0;
                while( (read = is.read(buffer)) != -1 ){
                outS.write(buffer, 0, read);
            }
            outS.close();
        } catch (java.io.IOException ex ){
            logger.error("Error while writing into temporary file: " + ex);
        }
        
        logger.log(Level.TRACE, "Byl stazen docasny soubor a ulozen do: '" + outFile.getAbsolutePath() + "'");
        return outFile;
    }
    
    
    /*
     * Provede kontrolu jestli soubor na serveru je novejsi nez docasny soubor
     * Pred volanim metody by bylo vhodne otestovat jestli je nastavene spravne GOOGLE_FILE_ID
     */
    public boolean isServerFileNewer(){
        if(GOOGLE_FILE_ID == null) return false;
        
        File serverFile = getFileFromGDriveByID(GOOGLE_FILE_ID);     //nelze pouzit lokalni promennou
        DateTime time = serverFile.getModifiedDate();
        
        logger.debug("\rServer file modified time: \t" + time.getValue() 
                        + "\rLocal modified time: \t\t" + tempFile.lastModified());
        return( time.getValue() > tempFile.lastModified() );
    }
    
    /*
     * Create InpuStream from temporary file - just wrapping method
     * return InputStream from temporary file
     */
    public InputStream getTempFileInputStream(){
        java.io.File file = getTempFile();
        
        if(file == null){
            logger.log(Level.ERROR, "Nepodarilo se ziskat docasny soubor!");
        } else {
            try {
                return new FileInputStream(file);
            } catch (FileNotFoundException ex) {
                logger.log(Level.ERROR, "Soubor: '" + file.getAbsolutePath() + "' nebyl nalezen", ex);
            }
        }
        
        return null;
    }
    
    /*
     * Returns local temporary file. If not exists, or is older than file on Google Drive
     * downloads actual file from Google Drive
     */
    public java.io.File getTempFile() {
        if(tempFile == null){
            tempFile = new java.io.File(TEMP_FILE);
        }
        
        if(tempFile.exists()) {
            if(!isServerFileNewer()){
                logger.log(Level.DEBUG, "Docasny soubor existuje a neni starsi nez soubor na serveru vracim tento soubor: " + tempFile.getAbsolutePath());
                return tempFile;
            }
            logger.debug("Docasny soubor je starsi nez soubor na serveru stahji aktualni verzi.");
            
        } else {
            logger.log(Level.DEBUG, "Docasny soubor neexistuje stahuji soubor z Google Drive.");
        }
        
        if(credentials != null){
            Drive service = new Drive.Builder(httpTransport, jsonFactory, credentials).setApplicationName(APPLICATION_NAME).build();
            File videoFile;
            if(GOOGLE_FILE_ID != null){
                videoFile = getFileFromGDriveByID(GOOGLE_FILE_ID);
            } else {
                videoFile = getFileFromGDriveByName(SERVER_FILE_NAME);
            }
            
            if(videoFile != null){
                InputStream is = getFileContentODF(service, videoFile);
                GOOGLE_FILE_ID = videoFile.getId();
                googleFile = videoFile;
                
                tempFile = createFile(is, TEMP_FILE);
                return tempFile;
            }
            return null;
        }
        
        
        return null;
    }

    /*
     * Update temporary file to google drive
     *      V pripade potreby (pokud neni nastaveno GOOGLE_FILE_ID nebo googleFile je null)
     *      provede vyhledani souboru podle jmena na serveru
     */
    public void updateTempFileToGDrive(){
        logger.log(Level.TRACE, "Provadim update souboru.");
        
        if(GOOGLE_FILE_ID == null){
            logger.log(Level.WARN, "ID souboru na GDrive je null!!");
            
            if(googleFile != null){
                GOOGLE_FILE_ID = googleFile.getId();
            } else {
                googleFile = getFileFromGDriveByName(SERVER_FILE_NAME);
                if (googleFile == null) return; //doslo k chybe
                GOOGLE_FILE_ID = googleFile.getId();
            }
        }
        
        if(credentials != null){
            Drive service = new Drive.Builder(httpTransport, jsonFactory, credentials).setApplicationName(APPLICATION_NAME).build();
            if (updateFile(service, GOOGLE_FILE_ID, tempFile) == null) {
                logger.log(Level.ERROR, "Doslo k chybe pri update souboru na serveru.");
            }
        }
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /*
     * General method. Update on Google Drive (any) file with contetent passed as arguments.
     * 
     * @param service - service containing valid connection to Google Drive
     * @param fileID - id of file at Google Drive to be updated
     * @param fileContent - content which will be sent to Google Drive
     */
    public static File updateFile(Drive service, String fileId, java.io.File fileContent) {
        try {
            // First retrieve the file from the API.
            //File file = service.files().get(fileId).execute();
            
            // File's new content.
            FileContent mediaContent = new FileContent(ODS_FORMAT_EXPORT_CONSTANT, fileContent);

            // Send the request to the API.
            Files.Update update = service.files().update(fileId, null, mediaContent);
            update.getConvert();
            logger.trace("Hodnota nastaveni konverze: " +  update.getConvert());
            update.setConvert(Boolean.TRUE);
            
            
            File updatedFile = update.execute();
            return updatedFile;
            
        } catch (IOException e) {
            System.out.println("An error occurred: " + e);
            return null;
        }
    }
    
    /*
     * Common function for retrieving information about file from Google Drive.
     * 
     * @return file specified by parameter or null if not found or error occurs. Contains only file infomration NO Content
     * @param fileID Id of file at Google Drive
     */
    public static File getFileFromGDriveByID(String fileID, GoogleCredential credentials, HttpTransport httpTransport, JsonFactory jsonFactory){
        if(fileID == null) {
            logger.log(Level.ERROR, "FileID is null!");
            return null;
        }
        
        Drive service = new Drive.Builder(httpTransport, jsonFactory, credentials).setApplicationName(APPLICATION_NAME).build();
        try {
            return service.files().get(fileID).execute();
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Error while trying to get file from Google Drive by ID.", ex);
        }
        return null;
    }
    
    public File getFileFromGDriveByID(String fileID){
        return getFileFromGDriveByID(fileID, credentials, httpTransport, jsonFactory);
    }
    
    /*
    * Retrieve list of all files at Google drive and returns first found file of specified name in parameter
    * 
    * @return first found file specified by parameter or null if not found or error occurs
    * @param filename name of file to find
    */
    public File getFileFromGDriveByName(String filename){
        return getFileFromGDriveByName(filename, credentials, httpTransport, jsonFactory);
    }
            
    /*
     * Retrieve list of all files at Google drive and returns first found file of specified name in parameter
     * 
     * @return first found file specified by parameter or null if not found or error occurs
     * @param filename name of file to find
     */
    public static File getFileFromGDriveByName(String filename, GoogleCredential credentials, HttpTransport httpTransport, JsonFactory jsonFactory){
            
        if(credentials != null){
            File videoFile = null;
            
            //Create a new authorized API client
            Drive service = new Drive.Builder(httpTransport, jsonFactory, credentials).build();

            //get list of all allDriveFiles and get full informations of requested file
            List<File> allDriveFiles = retrieveAllFiles(service);
            
            for (File f: allDriveFiles){
                logger.log(Level.TRACE, "Title: " + f.getTitle() + "\tID: " + f.getId());

                
                if(f.getTitle().equals(filename)){
                    logger.info("Soubor nalezen!");
                    try {
                        videoFile = service.files().get(f.getId()).execute();
                        return videoFile;
                    } catch (IOException ex) {
                        logger.log(Level.ERROR, "Chyba pri ziskavani detailu o souboru z GDrive: ", ex);
                        return null;
                    }
                }
            }
        } else {
            logger.log(Level.WARN, "Credentials is null!");
        }
        return null;
    }
    
    /*
     * This method delete old temporary file.
     * Rename new file passed in parameter and set it as actual temp file.
     * Also sets inner variable "java.io.File tempFile".
     * It's good to call updateTempFileToGDrive() to propagate changes to Google Drive.
     * 
     * 
     */
    public void replaceTempFile(java.io.File newTempFile){
        if(newTempFile == null){
            logger.log(Level.ERROR, "New temporary file passed in parameter is null!");
            return;
        }
        
        if(!tempFile.delete()){
            logger.log(Level.ERROR, "Failed to delete temporary file: " + tempFile.getAbsolutePath());
            return;
        }
        
        java.io.File newNamedFile = new java.io.File(tempFile.getName());
        if( !newTempFile.renameTo(newNamedFile) ){
            logger.log(Level.ERROR, "Failed to rename file.");
        }

        tempFile = newNamedFile;
        //tempFile = newTempFile;
    }
}
