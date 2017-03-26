package org.laukvik.csv.io;

/**
 *
 *
 */
public final class CsvWriterException extends Exception {

    /**
     *
     * @param s
     * @param exception
     */
    public CsvWriterException(final String s, final Throwable exception) {
        super(s, exception);
    }
}
