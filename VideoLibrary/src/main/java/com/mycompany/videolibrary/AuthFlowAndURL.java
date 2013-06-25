/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videolibrary;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.services.drive.DriveScopes;
import java.util.Arrays;

/**
 *
 * @author Martin
 * 
 * Wrapper Class to store and maintain authorization flow and URL needed to autorize application
 * Made to simplify code.
 * 
 */
public class AuthFlowAndURL {
    private GoogleAuthorizationCodeFlow authFlow;
    private String authUrl;
    private HttpTransport httpTransport;
    private JsonFactory jsonFactory;
    private String clientID;
    private String clientSecret;
    private String redirectURI;
            
    public AuthFlowAndURL(HttpTransport httpTransport, JsonFactory jsonFactory, String clientID, String clientSecret, String redirectURI){
        this.httpTransport = httpTransport;
        this.jsonFactory = jsonFactory;
        this.clientID = clientID;
        this.clientSecret = clientSecret;
        this.redirectURI = redirectURI;
        create();
    }
    
    //nelze udelat jako public jinak by metoda byla Override a mohlo
    //by to zpusobit problemy pri pripadnem dedeni, proto je treba rozdelit to na
    // "private create" a "public recreate" metody
    private void create(){
        authFlow = new GoogleAuthorizationCodeFlow.Builder(
                                    httpTransport, jsonFactory, clientID, clientSecret, Arrays.asList(DriveScopes.DRIVE))
                                        .setAccessType("offline").setApprovalPrompt("force").build();
        
        authUrl = authFlow.newAuthorizationUrl().setRedirectUri(redirectURI).build();
    }
    
    /*
     * Recreate authorization flow and url using set informations
     */
    public void recreate(){
        create();
    }
    
    public GoogleAuthorizationCodeFlow getAuthFlow() {
        return authFlow;
    }

    public void setAuthFlow(GoogleAuthorizationCodeFlow authFlow) {
        this.authFlow = authFlow;
    }

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public HttpTransport getHttpTransport() {
        return httpTransport;
    }

    public void setHttpTransport(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    public JsonFactory getJsonFactory() {
        return jsonFactory;
    }

    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getRedirectURI() {
        return redirectURI;
    }

    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }
    
    
}
