package org.laukvik.csv.io;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.laukvik.csv.io.XmlParser.Mode.ATTR;
import static org.laukvik.csv.io.XmlParser.Mode.BODY;
import static org.laukvik.csv.io.XmlParser.Mode.EMPTY;
import static org.laukvik.csv.io.XmlParser.Mode.EQ;
import static org.laukvik.csv.io.XmlParser.Mode.QUOTE_OPEN;
import static org.laukvik.csv.io.XmlParser.Mode.QUOTE_STOP;
import static org.laukvik.csv.io.XmlParser.Mode.TAG;
import static org.laukvik.csv.io.XmlParser.Mode.TEXT;
import static org.laukvik.csv.io.XmlParser.Mode.VALUE;

/**
 * @author Morten Laukvik
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
    /** The current tag. */
    private Tag current;

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
     * @param value the value
     */
    private void foundTag(final String value) {
        if (!value.isEmpty() && value.charAt(0) == '/') {
            current = current.getParent();
        } else {
            current = current.addTag(value);
        }
        fireFoundTag(current);
    }

    /**
     * Found the name of the attribute.
     *
     * @param attribute the value
     */
    private void foundAttr(final String attribute) {
        Attribute attr = current.addAttribute(attribute);
        fireAttributeTag(attr);
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
     * @param value the text
     */
    private void foundText(final String value) {
        if (value.trim().length() > 0) {
            Tag t = new Tag("text");
            t.setText(value);
            current.addTag(t);
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
        Mode mode = EMPTY;
        StringBuilder text = new StringBuilder();
        StringBuilder tag = new StringBuilder();
        StringBuilder attr = new StringBuilder();
        StringBuilder value = new StringBuilder();
        final StringBuilder close = new StringBuilder();
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
                        if (text.length() > 0) {
                            foundText(text.toString());
                        }
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
                        foundTag(tag.toString());
                        tag = new StringBuilder();
                        break;
                    case BODY:
                        mode = EMPTY;
                        foundTag(tag.toString());
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
                        foundValue(value.toString());
                        value = new StringBuilder();
                        break;
                    case QUOTE_STOP:
                        foundValue(value.toString());
                        value = new StringBuilder();
                        mode = EMPTY;
                        break;
                    case CLOSE:
//                        mode = TEXT;
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
                        mode = EQ;
                        foundAttr(attr.toString());
                        attr = new StringBuilder();
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
                        mode = QUOTE_STOP;
                        foundValue(value.toString());
                        value = new StringBuilder();
                        break;
                    case QUOTE_STOP:
                        mode = BODY;
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
                        foundTag(tag.toString());
                        tag = new StringBuilder();
                        break; // tag name ends
                    case BODY:
                        break; // ignore white space
                    case ATTR:
                        mode = BODY;
                        foundAttr(attr.toString());
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
                        mode = BODY;
                        foundValue(value.toString());
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
                        value.append(c);
                        mode = VALUE;
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
        return root;
    }

    /**
     * Indicates the state of parsing.
     */
    enum Mode {
        /** Not parsed anything yet. */
        EMPTY,
        /** In text mode. */
        TEXT,
        /** The name of the tag. */
        TAG,
        /** Inside a tag. */
        BODY,
        /** Inside attribute name. */
        ATTR,
        /** Is equal sign. */
        EQ,
        /**
         * The quote start.
         */
        QUOTE_OPEN,
        /** The value of the attribute. */
        VALUE,
        /**
         * The quote end.
         */
        QUOTE_STOP,
        /**
         * The close symbol.
         */
        CLOSE
    }

    /**
     * Listener for when reading XML.
     *
     */
    public interface XmlListener {
        /**
         * Found the tag.
         * @param tag the tag
         */
        void foundTag(Tag tag);

        /**
         * Found the attribute.
         * @param attribute the attribute
         */
        void foundAttribute(Attribute attribute);
    }

    /**
     *
     */
    static class Attribute {

        private final String name;
        private String value;

        public Attribute(String name) {
            this.name = name;
        }

        public Attribute(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public void setValue(final String value) {
            this.value = value;
        }

        public String toHtml() {
            return name + "=\"" + value + "\"";
        }

        @Override
        public String toString() {
            return toHtml();
        }
    }

    public static class Tag {

        private static final String[] SINGLE_TAGS = {"IMG", "BR", "INPUT"};
        private final String name;
        private final List<Tag> children;
        private final List<Attribute> attributeList;
        private Tag parent;
        private String text;

        public Tag(String name) {
            this.name = name;
            this.attributeList = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        public String getText() {
            return text;
        }

        public Tag getParent() {
            return parent;
        }

        public Tag addTag(String name) {
            return addTag(new Tag(name));
        }

        public Tag addTag(Tag tag) {
            tag.parent = this;
            children.add(tag);
            return tag;
        }

        public List<Attribute> getAttributes() {
            return attributeList;
        }

        public Attribute addAttribute(final String name) {
            return addAttribute(new Attribute(name));
        }

        public Attribute addAttribute(final Attribute attribute) {
            attributeList.add(attribute);
            return attribute;
        }

        public final boolean isText() {
            return text != null;
        }

        public final void setText(final String text) {
            this.text = text;
        }

        public final boolean isSingle() {
            for (int x = 0; x < SINGLE_TAGS.length; x++) {
                if (SINGLE_TAGS[x].equalsIgnoreCase(name)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public final String toString() {
            return name;
        }

        public final String toHtml() {
            StringBuilder b = new StringBuilder();
            if (isText()) {
                b.append(text);
                return b.toString();

            } else if (isSingle()) {

                b.append("<").append(name);
                for (Attribute a : attributeList) {
                    b.append(" ").append(a.toHtml());
                }

                if (text == null) {
                    for (Tag t : children) {
                        b.append(t.toHtml());
                    }
                } else {
                    b.append(text);
                }
                b.append("---/>");
                return b.toString();

            } else {
                if (parent != null) {
                    b.append("<").append(name);
                    for (Attribute a : attributeList) {
                        b.append(" ").append(a.toHtml());
                    }
                    b.append(">");
                }

                if (text == null) {
                    for (Tag t : children) {
                        b.append(t.toHtml());
                    }
                } else {
                    b.append(text);
                }

                if (parent != null) {
                    b.append("\n</").append(name).append(">");
                }
                return b.toString();
            }
        }
    }


}
