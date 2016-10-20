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
    private int index;
    private Tag root;
    private Tag current;

    private void foundTag(StringBuilder value) {
        if (value.charAt(0) == '/') {
//            foundCloseTag(value);
            current = current.getParent();
            index--;
        } else {
            index++;
            current = current.addTag(value.toString());
//            found(value, "Tag");
        }
    }

    private void foundAttr(StringBuilder value) {
        current.addAttribute(value.toString());
//        found(value, "Attribute");
    }

    private void foundValue(StringBuilder value) {
        if (value.toString().trim().length() > 0) {
            current.getAttributes().get(current.getAttributes().size() - 1).setValue(value.toString());

        }

//        System.out.println(value.toString());
//        found(value, "Value");
    }

    private void foundText(StringBuilder value) {
        if (value.toString().trim().length() > 0) {
//            current.setText(value.toString());
            Tag t = new Tag("text");
            t.setText(value.toString());
            current.addTag(t);
//            found(value, "Text");
        }
    }

    private void found(StringBuilder value, String type) {
        for (int x = 0; x < index; x++) {
            System.out.print("\t");
        }
//        System.out.format( "\t\t\t\tFound: %s  (%s) %n", value, type);
        System.out.println(value);
    }

    private void log(char c) {
//        System.out.format( "%s %s %n", mode, c);
    }

    private void print(int level, String value) {
        for (int x = 0; x < index; x++) {
            System.out.print("\t");
        }
        System.out.println(value);
    }

    public void parseFile(final File file) throws IOException {
        FileReader reader = new FileReader(file);
        root = new Tag("document");
        current = root;
        index = -1;
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
                        if (text.length() > 0) {
                            foundText(text);
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
                        foundTag(tag);
                        tag = new StringBuilder();
                        break;
                    case BODY:
                        mode = EMPTY;
                        foundTag(tag);
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
                        foundValue(value);
                        value = new StringBuilder();
                        break;
                    case QUOTE_STOP:
                        foundValue(value);
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
                        foundAttr(attr);
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
                        foundValue(value);
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
                        foundTag(tag);
                        tag = new StringBuilder();
                        break; // tag name ends
                    case BODY:
                        break; // ignore white space
                    case ATTR:
                        mode = BODY;
                        foundAttr(attr);
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
                        foundValue(value);
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
            log(c);
        }
        System.out.println(root.toHtml());

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

    static class Attribute {

        private String name;
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
//            return value == null ? name : name + "=\"" + value + "\"";
            return name + "=\"" + value + "\"";
        }
    }

    static class Tag {

        private Tag parent;
        private String name;
        private List<Tag> children;
        private List<Attribute> attributeList;
        private String text;

        public Tag(String name) {
            this.name = name;
            this.attributeList = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        public String getText() {
            return text;
        }

        public void setText(final String text) {
            this.text = text;
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

        public Attribute addAttribute(String name) {
            return addAttribute(new Attribute(name));
        }

        public Attribute addAttribute(Attribute attribute) {
            attributeList.add(attribute);
            return attribute;
        }

        public String toHtml() {
            StringBuilder b = new StringBuilder();
            if (name.equalsIgnoreCase("text")) {
                b.append(text);
                for (Tag t : children) {
                    b.append("\n" + t.toHtml());
                }
                return b.toString();

            } else {
                b.append("<" + name);
                for (Attribute a : attributeList) {
                    b.append(" " + a.toHtml());
                }
                b.append(">");

                if (text == null) {
                    for (Tag t : children) {
                        b.append("\n" + t.toHtml());
                    }
                } else {
                    b.append(text);
                }


                b.append("</" + name + ">");
                return b.toString() + "\n";
            }
        }
    }


}
