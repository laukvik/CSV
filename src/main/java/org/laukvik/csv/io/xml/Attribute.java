package org.laukvik.csv.io.xml;

/**
 *
 */
public class Attribute {

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

