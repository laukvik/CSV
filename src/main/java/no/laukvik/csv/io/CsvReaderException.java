package no.laukvik.csv.io;

import java.io.File;

/**
 * This exception occurs when the CSV could not be read.
 */
public final class CsvReaderException extends Exception {

    /**
     * The specified file could not be read.
     *
     * @param file      the file
     * @param throwable the throwable that occured
     */
    public CsvReaderException(final File file, final Throwable throwable) {
        super("Failed to read the CSV from the file " + file == null ? "null" : file.getAbsolutePath(), throwable);
    }
}
