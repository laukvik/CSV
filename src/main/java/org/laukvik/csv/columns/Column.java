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

import org.laukvik.csv.CSV;

import java.io.Serializable;
import java.text.SimpleDateFormat;

/**
 * An abstract class for Columns.
 *
 * @param <T> the data type of Column
 */
public abstract class Column<T> implements Comparable, Serializable {

    /**
     * Identifies the type attribute.
     */
    static final String TYPE = "type";
    /**
     * Identifies BigDecimalColumn.
     */
    static final String TYPE_BIGDECIMAL = "bigdecimal";
    /**
     * Identifies BooleanColumn.
     */
    static final String TYPE_BOOLEAN = "boolean";
    /**
     * Identifies ByteColumn.
     */
    static final String TYPE_BYTE = "byte";
    /**
     * Identifies DateColumn.
     */
    static final String TYPE_DATE = "date";
    /**
     * Identifies DoubleColumn.
     */
    static final String TYPE_DOUBLE = "double";
    /**
     * Identifies FloatColumn.
     */
    static final String TYPE_FLOAT = "float";
    /**
     * Identifies IntegerColumn.
     */
    static final String TYPE_INTEGER = "int";
    /**
     * Identifies StringColumn.
     */
    static final String TYPE_STRING = "varchar";
    /**
     * Identifies UrlColumn.
     */
    static final String TYPE_URL = "url";

    /**
     * The name of the width attribute.
     */
    static final String COLUMN_WIDTH = "width";
    /**
     * The name of the allowNulls attribute.
     */
    static final String ALLOW_NULLS = "allowNulls";
    /**
     * The name of the primary key attribute.
     */
    static final String PRIMARY_KEY = "primaryKey";
    /**
     * The name of the foreign key attribute.
     */
    static final String FOREIGN_KEY = "foreignKey";
    /**
     * The name of the default attribute.
     */
    static final String DEFAULT_VALUE = "default";
    /**
     * The name of the date format attribute.
     */
    static final String FORMAT = "format";

    /**
     * The CSV the column belongs to.
     */
    private CSV csv;

    /**
     * The name of the column.
     */
    private String name;
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
        this.primaryKey = false;
        this.allowNulls = false;
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
        return parseColumnDefinition(ColumnDefinition.parse(name));
    }

    /**
     * Analyzes the column definition an builds a column.
     *
     * @param columnDefinition the columnDefinition
     * @return the column
     */
    public static Column parseColumnDefinition(final ColumnDefinition columnDefinition) {
        ColumnDefinition.Attribute attrType = columnDefinition.get(TYPE);
        String columnName = columnDefinition.getColumnName();
        Column c = null;

        if (attrType == null) {
            c = new StringColumn(columnName);
        } else {
            String typeName = attrType.getValue();
            if (typeName.equalsIgnoreCase(TYPE_INTEGER)) {
                c = new IntegerColumn(columnName);

            } else if (typeName.equalsIgnoreCase(TYPE_FLOAT)) {
                c = new FloatColumn(columnName);

            } else if (typeName.equalsIgnoreCase(TYPE_DOUBLE)) {
                c = new DoubleColumn(columnName);

            } else if (typeName.equalsIgnoreCase(TYPE_BIGDECIMAL)) {
                c = new BigDecimalColumn(columnName);

            } else if (typeName.equalsIgnoreCase(TYPE_URL)) {
                c = new UrlColumn(columnName);

            } else if (typeName.equalsIgnoreCase(TYPE_BOOLEAN)) {
                c = new BooleanColumn(columnName);

            } else if (typeName.equalsIgnoreCase(TYPE_BYTE)) {
                c = new ByteColumn(columnName);

            } else if (typeName.equalsIgnoreCase(TYPE_DATE)) {
                ColumnDefinition.Attribute attr = columnDefinition.get(FORMAT);

                if (attr == null) {
                    c = new DateColumn(columnName);
                } else {
                    try {
                        new SimpleDateFormat(attr.getValue());
                        c = new DateColumn(columnName, attr.getValue());
                    } catch (final IllegalArgumentException e) {
                        throw new IllegalColumnDefinitionException(attr.getValue());
                    }
                }
            } else if (typeName.equalsIgnoreCase(TYPE_STRING)) {
                StringColumn sc = new StringColumn(columnName);
                c = sc;
                String w = attrType.getOptional();
                if (w != null) {
                    String v2 = w.trim();
                    try {
                        sc.setSize(Integer.parseInt(v2));
                    } catch (final NumberFormatException e) {
                        throw new IllegalColumnDefinitionException(w);
                    }
                }
            } else {
                c = new StringColumn(columnName);
            }
            boolean allowsNull = columnDefinition.getBoolean(ALLOW_NULLS);
            c.setAllowNulls(allowsNull);
            boolean primaryKey = columnDefinition.getBoolean(PRIMARY_KEY);
            c.setPrimaryKey(primaryKey);
            ColumnDefinition.Attribute attrFk = columnDefinition.get(FOREIGN_KEY);
            if (attrFk != null) {
                c.setForeignKey(new ForeignKey(attrFk.getValue(), attrFk.getOptional()));
            }
            ColumnDefinition.Attribute attrDefault = columnDefinition.get(DEFAULT_VALUE);
            if (attrDefault != null) {
                c.setDefaultValue(attrDefault.getValue());
            }
        }
        return c;
    }

    /**
     * Compares a value with another.
     *
     * @param one     a value
     * @param another a value
     * @return comparable value
     */
    public static final int compareWith(final Comparable one, final Comparable another) {
        if (one == null || another == null) {
            if (one == null && another == null) {
                return 0;
            }
            if (one == null) {
                return -1;
            } else {
                return 1;
            }
        }
        return one.compareTo(another);
    }

    /**
     * Returns the CSV the column belongs to.
     *
     * @return the csv
     */
    public final CSV getCSV() {
        return csv;
    }

    /**
     * Sets the CSV the column belongs to.
     *
     * @param csv the csv
     */
    public final void setCSV(final CSV csv) {
        this.csv = csv;
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
     * Compares with another Column.
     *
     * @param o the column
     * @return -1 or 0 or 1
     * @see java.lang.Comparable
     */
    @Override
    public final int compareTo(final Object o) {
        if (o == null) {
            return 1;
        } else if (o instanceof Column) {
            Column c = (Column) o;
            return getName().compareTo(c.getName());
        } else {
            return -1;
        }
    }

    /**
     * Returns the ColumnDefinition this column.
     *
     * @return the formatted version
     */
    public final ColumnDefinition toColumnDefinition() {
        ColumnDefinition cd = ColumnDefinition.parse(getName());
        if (this instanceof StringColumn) {
            StringColumn sc = (StringColumn) this;
            if (sc.getSize() > 0) {

                cd.setAttribute(TYPE, new ColumnDefinition.Attribute(TYPE_STRING,  Integer.toString(sc.getSize())));
            }
        } else if (this instanceof BigDecimalColumn) {
            cd.setAttribute(TYPE, new ColumnDefinition.Attribute(TYPE_BIGDECIMAL));
        } else if (this instanceof BooleanColumn) {
            cd.setAttribute(TYPE, new ColumnDefinition.Attribute(TYPE_BOOLEAN));
        } else if (this instanceof ByteColumn) {
            cd.setAttribute(TYPE, new ColumnDefinition.Attribute(TYPE_BYTE));
        } else if (this instanceof DateColumn) {
            cd.setAttribute(TYPE, new ColumnDefinition.Attribute(TYPE_DATE));
            DateColumn dc = (DateColumn) this;
            cd.setAttribute(FORMAT, new ColumnDefinition.Attribute(dc.getFormat()));
        } else if (this instanceof DoubleColumn) {
            cd.setAttribute(TYPE, new ColumnDefinition.Attribute(TYPE_DOUBLE));
        } else if (this instanceof FloatColumn) {
            cd.setAttribute(TYPE, new ColumnDefinition.Attribute(TYPE_FLOAT));
        } else if (this instanceof IntegerColumn) {
            cd.setAttribute(TYPE, new ColumnDefinition.Attribute(TYPE_INTEGER));
        } else if (this instanceof UrlColumn) {
            cd.setAttribute(TYPE, new ColumnDefinition.Attribute(TYPE_URL));
        }
        if (getDefaultValue() != null) {
            cd.setAttribute(DEFAULT_VALUE, new ColumnDefinition.Attribute(getDefaultValue()));
        }
        ForeignKey fk = getForeignKey();
        if (fk != null) {
            cd.setAttribute(FOREIGN_KEY, new ColumnDefinition.Attribute(fk.getTable(), fk.getColumn()));
        }
        if (getWidth() > 0) {
            cd.setAttribute(COLUMN_WIDTH, new ColumnDefinition.Attribute(Integer.toString(getWidth())));
        }
        if (isAllowNulls()) {

            cd.setAttribute(ALLOW_NULLS, new ColumnDefinition.Attribute(Boolean.toString(true)));
        }
        if (isPrimaryKey()) {
            cd.setAttribute(PRIMARY_KEY, new ColumnDefinition.Attribute(Boolean.toString(true)));
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
