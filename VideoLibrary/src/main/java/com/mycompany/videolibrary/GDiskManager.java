/*
 * Interface pro praci s google dokumenty. Implementujici trida musi umoznovat
 * dve zakladni funkcionality:
 *      stahnuti souboru z GDrive a vytvoreni docasne kopie
 *      update souboru na GDrive z docasne kopie
 * 
 * Implementujici trida si drzi ukazatel na lokalni kopii. Neni Thread Safe!
 */
package com.mycompany.videolibrary;

import java.io.File;

/**
 *
 * @author Martin
 */
public interface GDiskManager {
    public File getTempFile();
    public boolean update();
}
