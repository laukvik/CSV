package org.laukvik.csv.io.xml;

/**
 * An attribute contains a named key and a value.
 *
 */
public class Attribute {

    /**
     * The name.
     */
    private String name;
    /**
     * The value.
     */
    private String value;

    /**
     * Creates a new attribute without a value.
     *
     * @param attributeName the value
     */
    public Attribute(final String attributeName) {
        this.name = attributeName;
    }

    /**
     * Creates a new attribute with name and value.
     *
     * @param attributeName  the name
     * @param attributeValue the value
     */
    public Attribute(final String attributeName, final String attributeValue) {
        this.name = attributeName;
        this.value = attributeValue;
    }

    /**
     * Sets the new value.
     *
     * @param attributeValue the value
     */
    public final void setValue(final String attributeValue) {
        this.value = value;
    }

    /**
     * Returns the HTML representation.
     *
     * @return HTML
     */
    public final String toHtml() {
        return name + "=\"" + value + "\"";
    }

    /**
     * Returns HTML.
     *
     * @return HTML
     */
    public final String toString() {
        return toHtml();
    }
}

