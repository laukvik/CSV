package org.laukvik.csv.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.laukvik.csv.io.XmlParser.Mode.ATTR;
import static org.laukvik.csv.io.XmlParser.Mode.BODY;
import static org.laukvik.csv.io.XmlParser.Mode.EMPTY;
import static org.laukvik.csv.io.XmlParser.Mode.EQ;
import static org.laukvik.csv.io.XmlParser.Mode.QUOTE_OPEN;
import static org.laukvik.csv.io.XmlParser.Mode.QUOTE_STOP;
import static org.laukvik.csv.io.XmlParser.Mode.TAG;
import static org.laukvik.csv.io.XmlParser.Mode.TEXT;

/**
 * @author Morten Laukvik
 */
public class XmlParser {

    private final static char START_SYMBOL = '<';
    private final static char CLOSE_SYMBOL = '>';
    private final static char EQUAL_SYMBOL = '=';
    private final static char QUOTE_SYMBOL = '"';

    private final static char TAB_SYMBOL = '\t';
    private final static char NEWLINE_SYMBOL = '\n';
    private final static char RETURN_SYMBOL = '\r';
    private final static char SPACE_SYMBOL = ' ';

    private StringBuilder text, tag, attr, value, close;
    private Mode mode;

    public void parseFile(final File file) throws IOException {
        FileReader reader = new FileReader(file);
        mode = EMPTY;
        text = new StringBuilder();
        tag = new StringBuilder();
        attr = new StringBuilder();
        value = new StringBuilder();
        close = new StringBuilder();
        while (reader.ready()) {
            char c = (char) reader.read();
            if (c == START_SYMBOL) {
                // Tag start
                switch (mode) {
                    case EMPTY:
                        mode = TAG;
                        tag = new StringBuilder();
                        break;
                    case TEXT:
                        mode = TAG;
                        System.out.println("TXT: " + text);
                        text = new StringBuilder();
                        break;
                    case TAG:
                        break; // Invalid  <<
                    case BODY:
                        break; // < <
                    case ATTR:
                        break; // class<
                    case EQ:
                        break; // <=<
                    case QUOTE_OPEN:
                        break;
                    case VALUE:
                        break; // class="<
                    case QUOTE_STOP:
                        break;
                    case CLOSE:
                        break; // </>
                }
            } else if (c == CLOSE_SYMBOL) {  // <tag attr="value"> <tag>
                // Tag close
                switch (mode) {
                    case EMPTY:
                        break; // Syntax error.. Cant start with >
                    case TEXT:
                        break; // Syntax error.
                    case TAG:
                        mode = EMPTY;
                        System.out.println("TG1: " + tag);
                        tag = new StringBuilder();
                        break;
                    case BODY:
                        mode = TEXT;
                        tag = new StringBuilder();
                        break;
                    case ATTR:
                        mode = TEXT;
                        attr = new StringBuilder();
                        break;
                    case EQ:
                        break;
                    case QUOTE_OPEN:
                        break;
                    case VALUE:
                        mode = TEXT;
                        System.out.println("VAL: " + value);
                        value = new StringBuilder();
                        break;
                    case QUOTE_STOP:
                        break;
                    case CLOSE:
                        mode = TEXT;
                        break;
                }
            } else if (c == EQUAL_SYMBOL) {
                // Equals
                switch (mode) {
                    case EMPTY:
                        mode = TEXT;
                        text.append(c);
                        break;
                    case TEXT:
                        text.append(c);
                        break;
                    case TAG:
                        break; // Not allowed in tag name <tag=
                    case BODY:
                        mode = EQ;
                        break;
                    case ATTR:
                        break; // Not allowed in attribute name
                    case EQ:
                        break;
                    case QUOTE_OPEN:
                        break;
                    case VALUE:
                        value.append(c);
                        break;
                    case QUOTE_STOP:
                        break;
                    case CLOSE:
                        break; // Not allowed
                }
            } else if (c == QUOTE_SYMBOL) {
                /*
                 *  Quote
                 *
                 *  <tag attr="value">
                 *
                 **/
                switch (mode) {
                    case EMPTY:
                        mode = TEXT;
                        text.append(c);
                        break;
                    case TEXT:
                        text.append(c);
                        break;
                    case TAG:
                        break; // Quote not allowed in tag name
                    case BODY:
                        mode = ATTR;
                        break; // attr="   attr ="    attr =  "
                    case ATTR:
                        break;
                    case EQ:
                        mode = QUOTE_OPEN;
                        break;
                    case QUOTE_OPEN:
                        mode = QUOTE_STOP;
                        break;
                    case VALUE:
                        mode = BODY;
                        System.out.println("VAL: " + value);
                        value = new StringBuilder();
                        break;
                    case QUOTE_STOP:
                        break;
                    case CLOSE:
                        break; // Quote not allowed in close tag
                }
            } else if (c == SPACE_SYMBOL || c == TAB_SYMBOL || c == NEWLINE_SYMBOL || c == RETURN_SYMBOL) {
                // Whitespace  <tag attr="value">
                switch (mode) {
                    case EMPTY:
                        mode = TEXT;
                        break;
                    case TEXT:
                        if (c == SPACE_SYMBOL) {
                            text.append(c);
                        }
                        break;
                    case TAG:
                        mode = BODY;
                        System.out.println("TG2: " + tag);
                        tag = new StringBuilder();
                        break; // tag name ends
                    case BODY:
                        break; // ignore white space
                    case ATTR:
                        mode = BODY;
                        System.out.println("ATR: " + attr);
                        attr = new StringBuilder();
                        break; // attribute without value e.g <input checked>
                    case EQ:
                        break;
                    case QUOTE_OPEN:
                        break;
                    case VALUE:
                        value.append(c);
                        break;
                    case QUOTE_STOP:
                        break;
                    case CLOSE:
                        break;
                }
            } else {
                //-- Everything
                switch (mode) {
                    case EMPTY:
                        mode = TEXT;
                        text.append(c);
                        break;
                    case TEXT:
                        text.append(c);
                        break;
                    case TAG:
                        tag.append(c);
                        break;
                    case BODY:
                        mode = ATTR;
                        attr.append(c);
                        break;
                    case ATTR:
                        attr.append(c);
                        break;
                    case EQ:
                        break;
                    case QUOTE_OPEN:
                        break;
                    case VALUE:
                        value.append(c);
                        break;
                    case QUOTE_STOP:
                        break;
                    case CLOSE:
                        close.append(c);
                        break;
                }
            }
        }

    }

    enum Mode {
        EMPTY,
        TEXT,
        TAG,
        BODY,
        ATTR,
        EQ,
        QUOTE_OPEN,
        VALUE,
        QUOTE_STOP,
        CLOSE
    }


}
