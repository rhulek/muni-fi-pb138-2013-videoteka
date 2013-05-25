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
        GoogleCredential credential = new GoogleCredential();
        
        
        GoogleAuthorizationCodeFlow authorizationFlow = new GoogleAuthorizationCodeFlow.Builder(
                                    httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
                                        .setAccessType("online").setApprovalPrompt("auto").build();
        
        String url = authorizationFlow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
        
        System.out.println("Desktop.isDesktopSupported(): " + Desktop.isDesktopSupported() +
                    "\n Desktop.getDesktop().isSupported(Desktop.Action.BROWSE): " + Desktop.getDesktop().isSupported(Desktop.Action.BROWSE));
        
        if(!credentialsFile.exists()){
            
            logger.log(Level.WARNING, "Soubor credentials nebyl nalezen vytvarim nove uzivatelske data.");
            
            //If it is possible open web browser with url
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

            //String code = "4/DUEoZT9RWlD71Kc4klHYlBJFRCu5.UgZmoQ_8UpwXOl05ti8ZT3af2Fu6fQI";
            //String code = "4/NzYmTvhgB6PoDVixjVabTYbq9nXR.soLAaBCUFy4bOl05ti8ZT3bTulC6fQI";
            System.out.println("code: " + code);

            GoogleAuthorizationCodeTokenRequest authCodeTokeReqest =  authorizationFlow.newTokenRequest(code);
            authCodeTokeReqest.setRedirectUri(REDIRECT_URI);
            //authCodeTokeReqest.setGrantType("refresh_token");
            System.out.println("authCodeTokeReqest: " + authCodeTokeReqest);

            GoogleTokenResponse flowResponse = authCodeTokeReqest.execute();

            //GoogleTokenResponse flowResponse = authorizationFlow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
            credential = new GoogleCredential().setFromTokenResponse(flowResponse);


            //Savecredentials for later use
            FileCredentialStore credentialStore = new FileCredentialStore(new java.io.File(CREDENTIALS_FILE_NAME), jsonFactory);
            credentialStore.store(DEFAULT_USER_ID, credential);
            
        } else {
            
            Logger.getLogger(GDiskConnectionTest.class.getName()).log(Level.WARNING, "Soubor credentials nalezen nahravam uzivatelska data.");
            
            FileCredentialStore credentialStore = new FileCredentialStore(credentialsFile, jsonFactory);
            if (!credentialStore.load(DEFAULT_USER_ID, credential)) {
                logger.log(Level.SEVERE, "Chyba pri nahravani credentials");
            }
        }
        
        System.out.println("credentials: " + credential.getAccessToken());
        
        
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
}
