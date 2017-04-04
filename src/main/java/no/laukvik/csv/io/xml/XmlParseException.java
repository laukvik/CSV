package no.laukvik.csv.io.xml;

import java.io.File;
import java.io.IOException;

/**
 * Indicates that an XML file could not be parsed.
 */
public class XmlParseException extends Throwable {

    /**
     * Indicates that an XML file could not be parsed.
     *
     * @param file      the file
     * @param exception the exception that occured
     */
    public XmlParseException(final File file, final IOException exception) {
        super("Failed to parse XML file " + file, exception);
    }
}
