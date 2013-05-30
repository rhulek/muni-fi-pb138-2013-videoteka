/*
 * Class for demonstration of connection to Google API and Google drive.
 * Get access token and store it for future use, then copy file to Google Drive.
 */
package com.mycompany.videolibrary;

import com.google.api.client.extensions.java6.auth.oauth2.FileCredentialStore;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.awt.Desktop;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Martin
 */
public class GDiskConnectionTest {

    private static String CLIENT_ID = "702406823762.apps.googleusercontent.com";
    private static String CLIENT_SECRET = "iKIHHkC-wKEC7JS9YkzmDX_n";
    private static String REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob";
    private static String CREDENTIALS_FILE_NAME = "credentials.txt";
    private static String DEFAULT_USER_ID = "defaultUser";
    
    private static Logger logger = Logger.getLogger(GDiskConnectionTest.class.getName());
    private String FILE_TO_COPY = "D:/document.txt";
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();
        java.io.File credentialsFile = new java.io.File(CREDENTIALS_FILE_NAME);
        
        //TODO bude treba jeste doplnit refresh URL
        GoogleCredential credential = credential = new GoogleCredential.Builder().setJsonFactory(jsonFactory)
                                                        .setTransport(httpTransport)
                                                        .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
                                                        .build();
        
        GoogleAuthorizationCodeFlow authorizationFlow = new GoogleAuthorizationCodeFlow.Builder(
                                    httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                                        .setAccessType("offline").setApprovalPrompt("force").build();
        
        String url = authorizationFlow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        
        System.out.println("Desktop.isDesktopSupported(): " + Desktop.isDesktopSupported() +
                    "\n Desktop.getDesktop().isSupported(Desktop.Action.BROWSE): " + Desktop.getDesktop().isSupported(Desktop.Action.BROWSE));
        
        if(!credentialsFile.exists()){
            
            logger.log(Level.WARNING, "Soubor credentials nebyl nalezen vytvarim nove uzivatelske data.");
            
            //When it is possible open web browser with url
            if(!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
                System.out.println("Please open the following URL in your browser then type the authorization code:");
                System.out.println("  " + url);

            } else {
                try {
                    Desktop.getDesktop().browse( new java.net.URI(url) );

                } catch (URISyntaxException ex) {
                    Logger.getLogger(GDiskConnectionTest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //Save authentication token
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String code = br.readLine();

            //String code = "4/ZZMRGEsjXWKjnhMvEQj3CO1SZqpW.QpggOYGeknUbOl05ti8ZT3aVi6fifQI";
            System.out.println("code: " + code);

            GoogleAuthorizationCodeTokenRequest authCodeTokeReqest =  authorizationFlow.newTokenRequest(code);
            authCodeTokeReqest.setRedirectUri(REDIRECT_URI);
            //authCodeTokeReqest.setGrantType("refresh_token");
            System.out.println("authCodeTokeReqest: " + authCodeTokeReqest);

            GoogleTokenResponse flowResponse = authCodeTokeReqest.execute();

            logger.log(Level.INFO, "flowResponse: " + flowResponse);
            
            //GoogleTokenResponse flowResponse = authorizationFlow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
            //authorizationFlow.createAndStoreCredential(null, CLIENT_ID)
            
            //totally idiotic! If you specifies accesType("offline") you have to create credentials
            //in complicated way with builder instead just calling: new GoogleCredential().setFromTokenResponse(flowResponse)
            credential.setFromTokenResponse(flowResponse);

            //credential = new GoogleCredential().setFromTokenResponse(flowResponse);

            //Savecredentials for later use
            FileCredentialStore credentialStore = new FileCredentialStore(new java.io.File(CREDENTIALS_FILE_NAME), jsonFactory);
            credentialStore.store(DEFAULT_USER_ID, credential);
            
        } else {
            
            //Nahrani credentials ze souboru
            Logger.getLogger(GDiskConnectionTest.class.getName()).log(Level.INFO, "Soubor credentials nalezen nahravam uzivatelska data.");
            
            FileCredentialStore credentialStore = new FileCredentialStore(credentialsFile, jsonFactory);
            if (!credentialStore.load(DEFAULT_USER_ID, credential)) {
                logger.log(Level.SEVERE, "Chyba pri nahravani credentials");
                return;
            } else {
                logger.log(Level.INFO, "Credentials uspesne nahrany: \n\tAccess token: "
                        + credential.getAccessToken() + "\n\tRefresh token: "
                        + credential.getRefreshToken());
            }
            
            if(credential.refreshToken()){
                logger.log(Level.INFO, "Token byl uspesne obnoven: \n\tAccess token: " 
                        + credential.getAccessToken() + "\n\tRefresh token: "
                        + credential.getRefreshToken());
            } else {
                logger.log(Level.SEVERE, "Chyba pri obnoveni tokenu!");
            }
            
//            logger.log(Level.INFO, "Refreshuji token. Puvodni token: " + credential.getAccessToken());
//            GoogleAuthorizationCodeTokenRequest authCodeTokeReqest =  authorizationFlow.newTokenRequest(code);
//            authCodeTokeReqest.setRedirectUri(REDIRECT_URI);
//            authCodeTokeReqest.setGrantType("refresh_token");
        }
        
        System.out.println("credentials: \n\tAccess token: " + credential.getAccessToken() + "\n\tRefresh token: " + credential.getRefreshToken());
        
        //Create a new authorized API client
        Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build();
        
        File body = new File();
        body.setTitle("My document");
        body.setDescription("A test document");
        body.setMimeType("text/plain");
        
        java.io.File fileContent = new java.io.File("D:/document.txt");
        FileContent mediaContent = new FileContent("text/plain", fileContent);
        
        File file = service.files().insert(body, mediaContent).execute();
        System.out.println("File ID: " + file.getId());
    }
    
    private void getFile(String filename, Drive service){
        try {
            File file = service.files().get(filename).execute();
            
        } catch (IOException ex) {
            Logger.getLogger(GDiskConnectionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
