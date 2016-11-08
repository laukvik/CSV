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
     * TODO - Remove visibility - this is only interesting in JavaFX app
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
     * Parses a column name with support for optional metadata about the column. The supported format of metadata is
     * like this:
     * <pre>
     * "President(type=VARCHAR,primaryKey=true,increment=true,foreignKey=null)"
     * </pre>
     *
     * @param name the name
     * @return the column
     */
    public static Column parseName(final String name) {
        ColumnDefinition cd = new ColumnDefinition(name);
        return parseColumnDefinition(cd);
    }

    /**
     * Analyzes the column definition an builds a column.
     *
     * @param columnDefinition the columnDefinition
     * @return the column
     */
    public static Column parseColumnDefinition(final ColumnDefinition columnDefinition) {
        ColumnDefinition.Attribute attrType = columnDefinition.get("type");
        String columnName = columnDefinition.getColumnName();
        Column c = null;

        if (attrType == null) {
            c = new StringColumn(columnName);
        } else {
            String typeName = attrType.getValue();
            if (typeName == null) {
                c = new StringColumn(columnName);
            } else if (typeName.equalsIgnoreCase("INT")) {
                c = new IntegerColumn(columnName);
            } else if (typeName.equalsIgnoreCase("FLOAT")) {
                c = new FloatColumn(columnName);
            } else if (typeName.equalsIgnoreCase("DOUBLE")) {
                c = new DoubleColumn(columnName);
            } else if (typeName.equalsIgnoreCase("BIGDECIMAL")) {
                c = new BigDecimalColumn(columnName);
            } else if (typeName.equalsIgnoreCase("URL")) {
                c = new UrlColumn(columnName);
            } else if (typeName.equalsIgnoreCase("BOOLEAN")) {
                c = new BooleanColumn(columnName);
            } else if (typeName.equalsIgnoreCase("DATE")) {
                ColumnDefinition.Attribute attr = columnDefinition.get("format");

                if (attr == null || attr.getValue().isEmpty()) {
                    throw new IllegalColumnDefinitionException("");
                } else {
                    try {
                        new SimpleDateFormat(attr.getValue());
                        c = new DateColumn(columnName, attr.getValue());
                    } catch (final IllegalArgumentException e) {
                        throw new IllegalColumnDefinitionException(attr.getValue());
                    }
                }

            } else if (typeName.startsWith("VARCHAR")) {
                StringColumn sc = new StringColumn(columnName);
                String w = attrType.getOptional();
                if (w != null) {
                    String v2 = w.trim();
                    try {
                        sc.setSize(Integer.parseInt(v2));
                    } catch (final NumberFormatException e) {
                        throw new IllegalColumnDefinitionException(w);
                    }
                }
                c = sc;
            }
            boolean allowsNull = columnDefinition.getBoolean("allowNulls");
            c.setAllowNulls(allowsNull);

            boolean primaryKey = columnDefinition.getBoolean("primaryKey");
            c.setPrimaryKey(primaryKey);

            ColumnDefinition.Attribute attrFk = columnDefinition.get("foreignKey");
            if (attrFk != null) {
                c.setForeignKey(new ForeignKey(attrFk.getValue(), attrFk.getOptional()));
            }

            ColumnDefinition.Attribute attrDefault = columnDefinition.get("default");
            if (attrDefault != null) {
                c.setDefaultValue(attrDefault.getValue());
            }
        }
        return c;
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
        if (metaData != null) {
            metaData.fireColumnChanged(this);
        }
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

    /**
     * Returns the ColumnDefinition this column.
     *
     * @return the formatted version
     */
    public final ColumnDefinition toColumnDefinition() {
        ColumnDefinition cd = new ColumnDefinition(getName());
        cd.setAttribute("type", "BigDecimal");
        if (getDefaultValue() != null) {
            cd.setAttribute("default", getDefaultValue());
        }
        ForeignKey fk = getForeignKey();
        if (fk != null) {
            cd.setAttribute("foreignKey", fk.getTable(), fk.getColumn());
        }
        if (getWidth() > 0) {
            cd.setAttribute("width", getWidth() + "");
        }
        if (!isAllowNulls()) {
            cd.setAttribute("allowNulls", "false");
        }
        if (isPrimaryKey()) {
            cd.setAttribute("primaryKey", "true");
        }
        return cd;
    }

    /**
     * Returns the compressed version of this column.
     *
     * @return the compressed version
     */
    public final String toCSV() {
        ColumnDefinition cd = toColumnDefinition();
        return cd.toCompressed();
    }


}
