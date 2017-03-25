package org.laukvik.csv.io;

import java.io.IOException;

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
    public CsvWriterException(final String s, final Exception exception) {
        super(s, exception);
    }
}
