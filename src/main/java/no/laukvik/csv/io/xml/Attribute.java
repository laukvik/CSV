package no.laukvik.csv.io.xml;

/**
 * An attribute contains a named key and a value.
 */
public final class Attribute {

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
     * Returns the attribute name.
     *
     * @return the attribute name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the attribute name.
     *
     * @param attributeName the attribute name
     */
    public void setName(final String attributeName) {
        this.name = attributeName;
    }

    /**
     * Returns the attribute value.
     *
     * @return the attribute value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the new value.
     *
     * @param attributeValue the value
     */
    public void setValue(final String attributeValue) {
        this.value = attributeValue;
    }

    /**
     * Returns the HTML representation.
     *
     * @return HTML
     */
    public String toHtml() {
        if (value == null || value.trim().isEmpty()) {
            return name + "=\"\"";
        }
        return name + "=\"" + value + "\"";
    }

}
