/*
 * Copyright 2015 Laukviks Bedrifter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laukvik.csv.columns;

import org.laukvik.csv.MetaData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * An abstract class for Columns.
 *
 * @param <T> the data type of Column
 */
public abstract class Column<T> implements Comparable {

    /**
     * The metaData.
     */
    private MetaData metaData;
    /**
     * Returns if the column is a primary key.
     */
    private boolean primaryKey;
    /**
     * Returns if the column allows null.
     */
    private boolean allowNulls;
    /**
     * The foreignKey.
     */
    private ForeignKey foreignKey;
    /**
     * The default value.
     */
    private String defaultValue;
    /**
     * The name of the column.
     */
    private String name;
    /**
     * Returns the visibility.
     */
    private boolean visible;
    /**
     * The maximum amount of characters.
     */
    private int width;

    /**
     * Creates a new column.
     *
     * @param columnName the name of the column
     */
    Column(final String columnName) {
        this.name = columnName;
        this.visible = true;
        this.primaryKey = false;
        this.allowNulls = false;
    }

    /**
     * Parses a column name with support for optional metadata about the column. The supported format of metadata is
     * like this:
     * <p>
     * <pre>
     * "President(type=VARCHAR,primaryKey=true,increment=true,foreignKey=null)"
     * </pre>
     *
     * @param name the name
     * @return the column
     */
    public static final Column parseName(final String name) {
        /* Extract extra information about the column*/
        String columnName = null;
        // Variables for meta values
        List<String> keys = new ArrayList<>();
        List<String> values = new ArrayList<>();
        // Look for metadata in column headers
        int firstIndex = name.indexOf("(");
        if (firstIndex == -1) {
            // No extra information
            columnName = name;
        } else {
            // Found extra information
            int lastIndex = name.indexOf(")", firstIndex);
            columnName = name.substring(0, firstIndex);
            if (lastIndex != -1) {
                // String with metadata
                String extraDetails = name.substring(firstIndex + 1, lastIndex);

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
                        keys.add(key.trim());
                        values.add(value.trim());
                    } else {
                        keys.add(keyValue.trim());
                        values.add("");
                    }
                }

            }
        }

        // Find dataType before continuing
        String dataType = "VARCHAR";
        for (int x = 0; x < keys.size(); x++) {
            String key = keys.get(x);
            String val = values.get(x);
            if (key.equalsIgnoreCase("type")) {
                dataType = val;
            }
        }

        boolean allowsNull = true;
        {
            String allowNullValue = findValue("allowNulls", keys, values);
            if (allowNullValue != null) {
                if (allowNullValue.equalsIgnoreCase("true")) {
                    allowsNull = true;
                } else if (allowNullValue.equalsIgnoreCase("false")) {
                    allowsNull = false;
                } else {
                    throw new IllegalColumnDefinitionException("allowNulls  cant be " + allowNullValue);
                }
            }
        }

        boolean primaryKey = false;
        {
            String pkValue = findValue("primaryKey", keys, values);
            if (pkValue != null) {
                if (pkValue.equalsIgnoreCase("true")) {
                    primaryKey = true;
                } else if (pkValue.equalsIgnoreCase("false")) {
                    primaryKey = false;
                } else {
                    throw new IllegalColumnDefinitionException("primaryKey cant be " + pkValue);
                }
            }
        }

        ForeignKey foreignKey = null;
        {
            String fkValue = findValue("foreignKey", keys, values);
            if (fkValue == null || fkValue.trim().isEmpty()) {

            } else {
                foreignKey = ForeignKey.parse(fkValue);
            }
        }

        String defaultValue = findValue("default", keys, values);

        // Find all key pairs
        String s = dataType.toUpperCase();
        if (s.equalsIgnoreCase("INT")) {
            IntegerColumn c = new IntegerColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("FLOAT")) {
            FloatColumn c = new FloatColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("DOUBLE")) {
            DoubleColumn c = new DoubleColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("URL")) {
            UrlColumn c = new UrlColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("BOOLEAN")) {
            BooleanColumn c = new BooleanColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("DATE")) {
            String format = findValue("format", keys, values);
            if (format == null || format.trim().isEmpty()) {
                throw new IllegalColumnDefinitionException("Format cant be empty!");
            }
            try {
                SimpleDateFormat f = new SimpleDateFormat(format);
            } catch (Exception e) {
                throw new IllegalColumnDefinitionException("Format cant be " + format + "!");
            }
            DateColumn c = new DateColumn(columnName, format);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.equalsIgnoreCase("BIGDECIMAL")) {
            BigDecimalColumn c = new BigDecimalColumn(columnName);
            c.setPrimaryKey(primaryKey);
            c.setAllowNulls(allowsNull);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            return c;
        } else if (s.startsWith("VARCHAR")) {
            StringColumn c = new StringColumn(columnName);
            c.setAllowNulls(allowsNull);
            c.setPrimaryKey(primaryKey);
            c.setDefaultValue(defaultValue);
            c.setForeignKey(foreignKey);
            int first = s.indexOf("[");
            if (first != -1) {
                int last = s.lastIndexOf("]");
                String size = s.substring(first + 1, last).trim();
                try {
                    Integer i = Integer.parseInt(size);
                    c.setSize(i);
                } catch (Exception e) {
                    throw new IllegalColumnDefinitionException(
                            "Column " + columnName + " has invalid size '" + size + "'");
                }
            }
            return c;
        }
        return new StringColumn(columnName);
    }

    /**
     * Finds the value of the named key.
     *
     * @param key    the key to find
     * @param keys   the keys to look in
     * @param values the values belonging to the key
     * @return the value
     */
    private static String findValue(final String key, final List<String> keys, final List<String> values) {
        for (int x = 0; x < keys.size(); x++) {
            String k = keys.get(x);
            if (k.equalsIgnoreCase(key)) {
                return values.get(x);
            }
        }
        return null;
    }

    /**
     * Returns whether the column is visible.
     *
     * @return the column is visible
     */
    public final boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visibility of the column.
     *
     * @param isVisible the visibility
     */
    public final void setVisible(final boolean isVisible) {
        this.visible = isVisible;
        fireColumnChanged();
    }

    /**
     * Returns the width of the column in characters.
     *
     * @return the width
     */
    public final int getWidth() {
        return width;
    }

    /**
     * Sets the width of the column.
     *
     * @param columnWidth the width
     */
    public final void setWidth(final int columnWidth) {
        this.width = columnWidth;
        fireColumnChanged();
    }

    /**
     * Returns the string representation of this column.
     *
     * @param value the value
     * @return the string representation
     */
    public abstract String asString(T value);

    /**
     * Parses the String and returns the value of it.
     *
     * @param value the string
     * @return the value
     */
    public abstract T parse(String value);

    /**
     * Compares two objects.
     *
     * @param one     one column
     * @param another another column
     * @return the Comparable value
     */
    public abstract int compare(T one, T another);

    /**
     * Returns the name of the column.
     *
     * @return the name
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the name of the column.
     *
     * @param columnName the name
     */
    public final void setName(final String columnName) {
        this.name = columnName;
        fireColumnChanged();
    }

    /**
     * Returns the MetaData.
     *
     * @return the MetaData
     */
    public final MetaData getMetaData() {
        return metaData;
    }

    /**
     * Sets the MetaData.
     *
     * @param metaData the MetaData
     */
    public final void setMetaData(final MetaData metaData) {
        this.metaData = metaData;
    }

    /**
     * Sets the ForeignKey.
     *
     * @return the ForeignKey
     */
    public final ForeignKey getForeignKey() {
        return foreignKey;
    }

    /**
     * Sets the ForeignKey.
     *
     * @param foreignKey the ForeignKey
     */
    public final void setForeignKey(final ForeignKey foreignKey) {
        this.foreignKey = foreignKey;
    }

    /**
     * Returns the default value.
     *
     * @return the default value
     */
    public final String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Sets the default value.
     *
     * @param value the default value
     */
    public final void setDefaultValue(final String value) {
        this.defaultValue = value;
    }

    /**
     * Returns if the column allows nulls.
     *
     * @return true if allows nulls
     */
    public final boolean isAllowNulls() {
        return allowNulls;
    }

    /**
     * Sets whether the column allows nulls.
     *
     * @param isAllowNulls whether the column allows nulls
     */
    public final void setAllowNulls(final boolean isAllowNulls) {
        this.allowNulls = isAllowNulls;
    }

    /**
     * Returns whether the column is a PrimaryKey.
     *
     * @return true if the column is a PrimaryKey
     */
    public final boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * Sets the PrimaryKey.
     *
     * @param isPrimaryKey the primaryKey
     */
    public final void setPrimaryKey(final boolean isPrimaryKey) {
        this.primaryKey = isPrimaryKey;
    }

    /**
     * Returns the sort order of the column.
     *
     * @return the sort order
     */
    public final int indexOf() {
        return metaData.indexOf(this);
    }

    /**
     * Fired when the column is changed.
     */
    private void fireColumnChanged() {
        metaData.fireColumnChanged(this);
    }

    /**
     * Compares with another Column.
     *
     * @param o the column
     * @return -1 or 0 or 1
     * @see java.lang.Comparable
     */
    public final int compareTo(final Object o) {
        if (o == null) {
            return -1;
        } else if (o instanceof Column) {
            Column c = (Column) o;
//            return Integer.compare(indexOf(), c.indexOf());
            return getName().compareTo(c.getName());
        }
        return -1;
    }

}
