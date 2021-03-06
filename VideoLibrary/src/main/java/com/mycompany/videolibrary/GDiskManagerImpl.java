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
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import java.awt.Desktop;
import java.io.BufferedReader;
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
public class GDiskManagerImpl implements GDiskManager{
    private static Logger logger = LogManager.getLogger(GDiskManagerImpl.class.getName());
    private static String TEMP_FILE = "tmpFile.odt";
    private static String SERVER_FILE_NAME = "videoteka_data";
    private static String CREDENTIALS_FILE = "credentials.txt";
    private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private static String DEFAULT_USER_ID = "defaultUser";
    private static String ODF_FORMAT_EXPORT_CONSTANT = "application/x-vnd.oasis.opendocument.spreadsheet";
    private static int FILE_BUFFER_SIZE = 2048;
    
    //TODO nacitat z properities
    private String CLIENT_ID = "702406823762.apps.googleusercontent.com";
    private String CLIENT_SECRET = "iKIHHkC-wKEC7JS9YkzmDX_n";
    
    
    private GoogleCredential credentials;
    
    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;
    private java.io.File credentialsFile;
    private java.io.File tempFile;

    public GDiskManagerImpl() {
        this.httpTransport = new NetHttpTransport();
        this.jsonFactory = new JacksonFactory();
        
        this.credentialsFile = new java.io.File(CREDENTIALS_FILE);
        this.credentials = new GoogleCredential.Builder().setJsonFactory(jsonFactory)
                                                        .setTransport(httpTransport)
                                                        .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                                                        .build();
        //this.initialize();
    }
    
    protected void setClientID(String id){ this.CLIENT_ID = id; }
    protected void setClientSecret(String secret) { this. CLIENT_SECRET = secret; }
    
    public void initialize(){
        
        GoogleAuthorizationCodeFlow authorizationFlow = new GoogleAuthorizationCodeFlow.Builder(
                                    httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                                        .setAccessType("offline").setApprovalPrompt("force").build();
        
        String url = authorizationFlow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        
        logger.trace("Desktop.isDesktopSupported(): " + Desktop.isDesktopSupported() +
                    "\rDesktop.Action.BROWSE: " + Desktop.getDesktop().isSupported(Desktop.Action.BROWSE));
        
        //If credentials file not exist it is probably first run
        if(!credentialsFile.exists()){
            logger.log(Level.INFO, "Soubor credentials nebyl nalezen vytvarim nove uzivatelske data.");
            
            //When it is possible open web browser with url
            if(!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
                logger.log(Level.WARN, "Open autorization URL in browser is not supported");
                
                System.out.println("Please open the following URL in your browser then type the authorization code:");
                System.out.println("  " + url);

            } else {
                try {
                   Desktop.getDesktop().browse( new java.net.URI(url) );

                } catch (URISyntaxException ex) {
                    logger.log(Level.ERROR, ex);
                    
                    System.out.println("Please open the following URL in your browser then type the authorization code:");
                    System.out.println("  " + url);
                    
                } catch (java.io.IOException ex) {
                    logger.log(Level.ERROR, ex);
                }
            }
            
            //Get code from user
            System.out.println("Please insert authorization code: ");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String authCode = null;
            try {
                authCode = br.readLine();
            } catch (IOException ex) {
                logger.log(Level.ERROR, ex);
            }
            
            GoogleAuthorizationCodeTokenRequest authCodeTokeReqest =  authorizationFlow.newTokenRequest(authCode);
            authCodeTokeReqest.setRedirectUri(REDIRECT_URI);
            
            //Get authentication Token
            GoogleTokenResponse flowResponse = null;
            try {
                flowResponse = authCodeTokeReqest.execute();
            } catch (IOException ex) {
                logger.log(Level.ERROR, "Chyba pri ziskavani authentizacniho tokenu!", ex);
            }
            
            //Create credentials for future use
            //credentials.setFromTokenResponse(flowResponse);

            //Savecredentials for later use
            FileCredentialStore credentialStore;
            try {
                credentialStore = new FileCredentialStore(credentialsFile, jsonFactory);
                credentialStore.store(DEFAULT_USER_ID, credentials);
            } catch (IOException ex) {
                logger.log(Level.ERROR, ex);
            }
            
            logger.log(Level.INFO, "Credentials uspesne vytvoreny a ulozeny v '" + credentialsFile.getAbsolutePath() + "'");
        } else {
            
        //Credentials exist so load informations
            logger.log(Level.DEBUG, "Credentials file was found loading data.");
            
            FileCredentialStore credentialStore = null;
            try {
                credentialStore = new FileCredentialStore(credentialsFile, jsonFactory);
            } catch (IOException ex) {
                logger.log(Level.ERROR, "Chyba pri otevirani credentials file: ", ex);
            }
            
            if (!credentialStore.load(DEFAULT_USER_ID, credentials)) {
                logger.log(Level.FATAL, "Chyba pri nahravani credentials");
                return;   //TODO Tady by se mela vratit hodnota umoznujici procest volani funkce pro prvni inicializaci
            } else {
                logger.log(Level.TRACE, "Credentials uspesne nahrany: \r\tAccess token: "
                        + credentials.getAccessToken() + "\r\tRefresh token: "
                        + credentials.getRefreshToken());
            }
            
            try {
                if( credentials.refreshToken() ){
                    logger.log(Level.TRACE, "Token byl uspesne obnoven: \r\tAccess token: " 
                                + credentials.getAccessToken() + "\r\tRefresh token: "
                                + credentials.getRefreshToken());
                } else {
                    logger.log(Level.FATAL, "Chyba pri obnoveni tokenu!");
                }
            } catch (IOException ex) {
                logger.log(Level.FATAL, "Chyba pri obnoveni tokenu: ", ex);
            }
        }
    }
    
    /*
     * Used at first run or when credentials file is not found.
     * Open browser
     */
    public void createAndSetCredentials(){
        
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
     * This method make first initialization of connection to google drive by
     * requestiong access and refresh tokens throught opening web browser with URL if possible.
     * Otherwise print request for manually opening URL at users' browser.
     */
//    private String firstRun(boolean printMSG){
//        
//    }
//    
//    private void getToken(){
//        
//    }
    
    private List<File> retrieveAllFiles(Drive service) {
        List<File> result = new ArrayList<File>();
        Files.List request = null;;
        try {
            request = service.files().list();
        } catch (IOException ex) {
            logger.log(Level.ERROR, "Chyba pri ziskavani seznamu souboru: ", ex);
        }

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
        
    private void synchronize(){
        //provede synchronizaci lokalniho souboru a souboru na GDrive
    }
    
    /*
     * This method gets Input stream with content of file.
     */
    public static InputStream downloadFileODF(Drive service, File file) {
    
    if(file == null) return null;
    
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
     * Save content of InputStream to temporary file.
     */
    private java.io.File createTempFile(java.io.InputStream is) {
        byte[] buffer = new byte[FILE_BUFFER_SIZE];

        if(is == null){
            logger.error("Doslo k chybe pri stahovani souboru.");
            return null;
        }

        this.tempFile = new java.io.File(TEMP_FILE);
        java.io.OutputStream outS = null;

        try {
            outS = new java.io.FileOutputStream(tempFile);

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
        } catch (java.io.IOException ex ){
            logger.error("Error while writing into temporary file: " + ex);
        }
        
        return tempFile;
    }
    
    @Override
    public java.io.File getTempFile() {
        if(credentials != null){
            File videoFile = null;
            
            //Create a new authorized API client
            Drive service = new Drive.Builder(httpTransport, jsonFactory, credentials).build();

            //get list of all files and get full informations of requested file
            List<File> files = retrieveAllFiles(service);
            
            for (File f: files){
                logger.log(Level.TRACE, "Title: " + f.getTitle() + "\tID: " + f.getId());

                
                if(f.getTitle().equals(SERVER_FILE_NAME)){
                    logger.info("Soubor nalezen!");
                    try {
                        videoFile = service.files().get(f.getId()).execute();
                    } catch (IOException ex) {
                        logger.log(Level.ERROR, "Chyba pri ziskavani detailu o souboru z GDrive: ", ex);
                    }
                }
            }

            InputStream is = downloadFileODF(service, videoFile);

            return createTempFile(is);
        }
        return null;
    }

    @Override
    public boolean update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
