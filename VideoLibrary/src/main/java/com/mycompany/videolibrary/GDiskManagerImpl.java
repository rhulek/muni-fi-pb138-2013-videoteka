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
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.DriveScopes;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.Arrays;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 *
 * @author Martin
 */
public class GDiskManagerImpl implements GDiskManager{
    private static Logger logger = LogManager.getLogger(GDiskConnectionTest.class.getName());
    private static String TEMP_FILE = "tmpFile.odt";
    private static String CREDENTIALS_FILE = "credentials.txt";
    private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private static String DEFAULT_USER_ID = "defaultUser";
    
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
        this.initialize();
    }
    
    protected void setClientID(String id){ this.CLIENT_ID = id; }
    protected void setClientSecret(String secret) { this. CLIENT_SECRET = secret; }
    
    private void initialize(){
        
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
                java.util.logging.Logger.getLogger(GDiskManagerImpl.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            
            //Create credentials for future use
            credentials.setFromTokenResponse(flowResponse);

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
    
    @Override
    public java.io.File getTempFile() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
