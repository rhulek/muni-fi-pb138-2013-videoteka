/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.videoteka.spring;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Martin
 */
public class ImportFileBackingBean {
    public static String APPEND_TO_FILE_OPTION = "append";
    public static String REPLACE_FILE_OPTION = "replace";
    private String filePath;
    private String importOption;
    private Map<String, String> importOptions;

    

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getImportOption() {
        return importOption;
    }

    public void setImportOption(String importOption) {
        this.importOption = importOption;
    }

    public Map<String, String> getImportOptions() {
        return importOptions;
    }

    public void setImportOptions(Map<String, String> importOptions) {
        this.importOptions = importOptions;
    }
    
}
