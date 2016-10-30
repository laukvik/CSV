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
     * @param name the value
     */
    public Attribute(final String name) {
        this.name = name;
    }

    /**
     * Creates a new attribute with name and value.
     *
     * @param name  the name
     * @param value the value
     */
    public Attribute(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Sets the new value.
     *
     * @param value the value
     */
    public final void setValue(final String value) {
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
     * Returns HTML
     *
     * @return HTML
     */
    public final String toString() {
        return toHtml();
    }
}

