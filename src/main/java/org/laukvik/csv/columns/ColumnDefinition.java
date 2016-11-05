package org.laukvik.csv.columns;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Defines the features of a column. In simplest form it's only the column name. It more complex it can contain
 * one or more attributes. An attribute is a key and a value.
 */
public final class ColumnDefinition {

    /**
     * The name of the column.
     */
    private String columnName;
    /**
     * The attributeMap with keys and values.
     */
    private Map<String, Attribute> attributeMap;

    /**
     * Parses a column definition stored in a line of string.
     *
     * @param data the definition
     */
    public ColumnDefinition(final String data) {
        parse(data);
    }

    /**
     * Returns the string representation of this instance.
     *
     * @return the string
     */
    public String toCompressed() {
        StringBuilder b = new StringBuilder();
        b.append(columnName);
        if (!isEmpty()) {
            b.append("(");
            int x = 0;
            for (String key : attributeMap.keySet()) {
                if (x > 0) {
                    b.append(",");
                }
                b.append(key);
                b.append("=");
                b.append(attributeMap.get(key).toCompressed());
                x++;
            }
            b.append(")");
        }
        return b.toString();
    }

    /**
     * Parses the column definition string and extracts column name and all attributes.
     *
     * @param compressedColumnDefinition the compressed column to read
     */
    private void parse(final String compressedColumnDefinition) {
        attributeMap = new HashMap<>();
        /* Extract extra information about the column*/
        columnName = null;
        // Look for metadata in column headers
        int firstIndex = compressedColumnDefinition.indexOf("(");
        if (firstIndex == -1) {
            // No extra information
            columnName = compressedColumnDefinition;
        } else {
            // Found extra information
            int lastIndex = compressedColumnDefinition.indexOf(")", firstIndex);
            columnName = compressedColumnDefinition.substring(0, firstIndex);
            if (lastIndex != -1) {
                // String with metadata
                String extraDetails = compressedColumnDefinition.substring(firstIndex + 1, lastIndex);
                String[] keyValues;
                if (extraDetails.contains(",")) {
                    keyValues = extraDetails.split(",");
                } else {
                    keyValues = new String[]{extraDetails};
                }
                // Extract all key/value pairs from metadata
                for (String keyValue : keyValues) {
                    if (keyValue.contains("=")) {
                        String[] arr = keyValue.split("=");
                        String key = arr[0];
                        String value = arr[1];
                        setAttribute(key, value);
                    } else {
                        setAttribute(keyValue, "");
                    }
                }
            }
        }
    }

    /**
     * Sets an attribute.
     *
     * @param name  the name
     * @param value the value
     */
    public void setAttribute(final String name, final String value) {
        if (!name.trim().isEmpty()) {
            attributeMap.put(name.toLowerCase(), new Attribute(value));
        }
    }

    /**
     * Sets an attribute.
     *
     * @param name the name
     */
    public void removeAttribute(final String name) {
        attributeMap.remove(name.toLowerCase());
    }

    /**
     * Returns the column name.
     *
     * @return the column name
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * Returns the value of the attribute.
     *
     * @param attributeName the name of the attribute
     * @return the value
     */
    public Attribute get(final String attributeName) {
        return attributeMap.get(attributeName.toLowerCase());
    }

    /**
     * Returns the boolean value of an attribute.
     *
     * @param attributeName the name of the attribute
     * @return the value
     */
    public boolean getBoolean(final String attributeName) {
        Attribute v = get(attributeName);
        if (v == null || v.getValue() == null || v.getValue().trim().isEmpty()) {
            return false;
        }
        return v.getValue().trim().equalsIgnoreCase("true");
    }

    /**
     * Returns true if there are no attributes.
     *
     * @return true when no attributes.
     */
    public boolean isEmpty() {
        return attributeMap.isEmpty();
    }

    /**
     * Returns the amount of attributes.
     *
     * @return the amount
     */
    public int getAttributeCount() {
        return attributeMap.keySet().size();
    }

    /**
     * Returns a set of all attribute names.
     *
     * @return the names
     */
    public Set<String> getAttributeNames() {
        return attributeMap.keySet();
    }


}
