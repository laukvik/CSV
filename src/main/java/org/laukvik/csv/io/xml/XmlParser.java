package org.laukvik.csv.io.xml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.laukvik.csv.io.xml.Mode.ATTR;
import static org.laukvik.csv.io.xml.Mode.BODY;
import static org.laukvik.csv.io.xml.Mode.EMPTY;
import static org.laukvik.csv.io.xml.Mode.EQ;
import static org.laukvik.csv.io.xml.Mode.QUOTE_OPEN;
import static org.laukvik.csv.io.xml.Mode.QUOTE_STOP;
import static org.laukvik.csv.io.xml.Mode.TAG;
import static org.laukvik.csv.io.xml.Mode.TEXT;
import static org.laukvik.csv.io.xml.Mode.VALUE;


/**
 * Parses XML.
 */
public class XmlParser {

    /**
     * The character for opening a tag.
     */
    private static final char START_SYMBOL = '<';
    /**
     * The character for closing a tag.
     */
    private static final char CLOSE_SYMBOL = '>';
    /**
     * The character for equal.
     */
    private static final char EQUAL_SYMBOL = '=';
    /**
     * The character for qoute.
     */
    private static final char QUOTE_SYMBOL = '"';
    /**
     * The character for TAB.
     */
    private static final char TAB_SYMBOL = '\t';
    /**
     * The character for newline.
     */
    private static final char NEWLINE_SYMBOL = '\n';
    /**
     * The character for return.
     */
    private static final char RETURN_SYMBOL = '\r';
    /**
     * The character for space.
     */
    private static final char SPACE_SYMBOL = ' ';
    /**
     * The listeners.
     */
    private final List<XmlListener> listeners;
    /**
     * The current tag.
     */
    private Tag current;
    /**
     * The mode the reading state is in.
     */
    private Mode mode;

    /**
     * Variable for appending text.
     */
    private StringBuilder textBuilder;
    /**
     * Variable for appending tag name.
     */
    private StringBuilder tagBuilder;
    /**
     * Variable for appending attribute name.
     */
    private StringBuilder attrBuilder;
    /**
     * Variable for appending attribute value.
     */
    private StringBuilder valueBuilder;


    /**
     * Creates a new XmlParser.
     */
    public XmlParser() {
        listeners = new ArrayList<>();
    }

    /**
     * Adds an XmlListener.
     *
     * @param listener the listener to add
     */
    public final void addListener(final XmlListener listener) {
        listeners.add(listener);
    }

    /**
     * Fires when it has found the tag.
     *
     * @param tag the tag
     */
    private void fireFoundTag(final Tag tag) {
        for (XmlListener l : listeners) {
            l.foundTag(tag);
        }
    }

    /**
     * Fires when it has found an attribute.
     *
     * @param attribute the attribute
     */
    private void fireAttributeTag(final Attribute attribute) {
        for (XmlListener l : listeners) {
            l.foundAttribute(attribute);
        }
    }

    /**
     * The tag with the value was found.
     *
     * @param tagValue the value
     */
    private void foundTag(final String tagValue) {
        if (!tagValue.isEmpty() && tagValue.charAt(0) == '/') {
            current = current.getParent();
        } else {
            current = current.addTag(tagValue);
            fireFoundTag(current);
        }
    }

    /**
     * Found the name of the attribute.
     *
     * @param attribute the value
     */
    private void foundAttr(final String attribute) {
        fireAttributeTag(current.addAttribute(attribute));
    }

    /**
     * Found the value of the attribute.
     *
     * @param attributeValue the value of the attribute
     */
    private void foundValue(final String attributeValue) {
        if (attributeValue.trim().length() > 0) {
            current.getAttributes().get(current.getAttributes().size() - 1).setValue(attributeValue);
        }
    }

    /**
     * Found a text node.
     *
     * @param textValue the text
     */
    private void foundText(final String textValue) {
        if (!textValue.trim().isEmpty()) {
            Tag t = new Tag("text");
            t.setText(textValue);
            current.addTag(t);
        }
    }

    /**
     * Found a start symbol.
     */
    private void parseStartSymbol() {
        // Tag start
        switch (mode) {
            case EMPTY:
                mode = TAG;
                tagBuilder = new StringBuilder();
                break;
            case TEXT:
                mode = TAG;
                if (textBuilder.length() > 0) {
                    foundText(textBuilder.toString());
                }
                textBuilder = new StringBuilder();
                break;
            default:
                break;
        }
    }

    /**
     * Found a close symbol.
     */
    private void parseCloseSymbol() {
        switch (mode) {
            case TAG:
                mode = EMPTY;
                foundTag(tagBuilder.toString());
                tagBuilder = new StringBuilder();
                break;
            case BODY:
                mode = EMPTY;
                foundTag(tagBuilder.toString());
                tagBuilder = new StringBuilder();
                break;
            case ATTR:
                mode = TEXT;
                attrBuilder = new StringBuilder();
                break;
            case VALUE:
                mode = TEXT;
                foundValue(valueBuilder.toString());
                valueBuilder = new StringBuilder();
                break;
            case QUOTE_STOP:
                foundValue(valueBuilder.toString());
                valueBuilder = new StringBuilder();
                mode = EMPTY;
                break;
            default:
                break;
        }
    }

    /**
     * Found an equal sign.
     */
    private void parseEqualSymbol() {
        switch (mode) {
            case EMPTY:
                mode = TEXT;
                textBuilder.append('=');
                break;
            case TEXT:
                textBuilder.append('=');
                break;
            case BODY:
                mode = EQ;
                break;
            case ATTR:
                mode = EQ;
                foundAttr(attrBuilder.toString());
                attrBuilder = new StringBuilder();
                break; // Not allowed in attribute name
            case VALUE:
                valueBuilder.append('=');
                break;
            default:
                break;
        }
    }

    /**
     * Found a quote symbol.
     */
    private void parseQuoteSymbol() {
        switch (mode) {
            case EMPTY:
                mode = TEXT;
                textBuilder.append('"');
                break;
            case TEXT:
                textBuilder.append('"');
                break;
            case BODY:
                mode = ATTR;
                break; // attr="   attr ="    attr =  "
            case EQ:
                mode = QUOTE_OPEN;
                break;
            case QUOTE_OPEN:
                mode = QUOTE_STOP;
                break;
            case VALUE:
                mode = QUOTE_STOP;
                foundValue(valueBuilder.toString());
                valueBuilder = new StringBuilder();
                break;
            case QUOTE_STOP:
                mode = BODY;
                break;
            default:
                break;
        }
    }

    /**
     * Found whitespace character.
     *
     * @param c the character
     */
    private void parseWhitespace(final char c) {
        switch (mode) {
            case EMPTY:
                mode = TEXT;
                break;
            case TEXT:
                if (c == SPACE_SYMBOL) {
                    textBuilder.append(c);
                }
                break;
            case TAG:
                mode = BODY;
                foundTag(tagBuilder.toString());
                tagBuilder = new StringBuilder();
                break; // tag name ends
            case ATTR:
                mode = BODY;
                foundAttr(attrBuilder.toString());
                attrBuilder = new StringBuilder();
                break; // attribute without value e.g <input checked>
            case VALUE:
                valueBuilder.append(c);
                break;
            case QUOTE_STOP:
                mode = BODY;
                foundValue(valueBuilder.toString());
                break;
            default:
                break;
        }
    }

    /**
     * Found a non control character.
     *
     * @param c the character
     */
    private void parseEverything(final char c) {
        switch (mode) {
            case EMPTY:
                mode = TEXT;
                textBuilder.append(c);
                break;
            case TEXT:
                textBuilder.append(c);
                break;
            case TAG:
                tagBuilder.append(c);
                break;
            case BODY:
                mode = ATTR;
                attrBuilder.append(c);
                break;
            case ATTR:
                attrBuilder.append(c);
                break;
            case QUOTE_OPEN:
                valueBuilder.append(c);
                mode = VALUE;
                break;
            case VALUE:
                valueBuilder.append(c);
                break;
            default:
                break;
        }
    }

    /**
     * Parses the file.
     *
     * @param file the file
     * @return the Tag
     * @throws IOException when the file can't read
     */
    public final Tag parseFile(final File file) throws IOException {
        FileReader reader = new FileReader(file);
        final Tag root = new Tag("document");
        current = root;
        mode = EMPTY;
        textBuilder = new StringBuilder();
        tagBuilder = new StringBuilder();
        attrBuilder = new StringBuilder();
        valueBuilder = new StringBuilder();
        while (reader.ready()) {

            char c = (char) reader.read();
            if (c == START_SYMBOL) {
                parseStartSymbol();
            } else if (c == CLOSE_SYMBOL) {  // <tag attr="value"> <tag>
                // Tag close
                parseCloseSymbol();
            } else if (c == EQUAL_SYMBOL) {
                // Equals
                parseEqualSymbol();
            } else if (c == QUOTE_SYMBOL) {
                parseQuoteSymbol();
            } else if (c == SPACE_SYMBOL || c == TAB_SYMBOL || c == NEWLINE_SYMBOL || c == RETURN_SYMBOL) {
                // Whitespace  <tag attr="value">
                parseWhitespace(c);
            } else {
                parseEverything(c);
            }
        }
        return root.getChildren().get(0);
    }

}
