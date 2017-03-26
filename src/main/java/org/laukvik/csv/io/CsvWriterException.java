package org.laukvik.csv.io;

import java.io.File;

/**
 * This exception occurs when the CSV could not be written.
 *
 */
public final class CsvWriterException extends Exception {

    /**
     * The specified file could not be written.
     *
     * @param file the file
     * @param throwable the throwable that occured
     */
    public CsvWriterException(final File file, final Throwable throwable) {
        super("Failed to read the CSV from the file " + file == null ? "null" : file.getAbsolutePath(), throwable);
    }



}
