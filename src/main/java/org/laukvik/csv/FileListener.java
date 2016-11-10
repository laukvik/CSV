package org.laukvik.csv;

import java.io.File;

/**
 * Listens when data is being written or read.
 *
 * @author Morten Laukvik
 */
public interface FileListener {

    /**
     * Notifies that its starting to read the file.
     *
     * @param file the file
     */
    void beginRead(File file);

    /**
     * Notifies that its finished reading the file.
     *
     * @param file the file
     */
    void finishRead(File file);

    /**
     * Notifies that its beginning to write to the file.
     *
     * @param file the file
     */
    void beginWrite(File file);

    /**
     * Notifies that its finished writing to the file.
     *
     * @param file the file
     */
    void finishWrite(File file);

}
