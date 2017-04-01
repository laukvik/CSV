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
package org.laukvik.csv;

import org.laukvik.csv.columns.BigDecimalColumn;
import org.laukvik.csv.columns.BooleanColumn;
import org.laukvik.csv.columns.ByteColumn;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.columns.UrlColumn;

import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * Represents a single row entry in the CSV.
 *
 * <h3>Setting values in a row</h3>
 * <pre>{@code
 * CSV csv = new CSV();
 * StringColumn first = csv.addStringColumn("first");
 * IntegerColumn salary = csv.addIntegerColumn("first");
 * Row = csv.addRow();
 * row.set( first, "Bill" ).set( salary, 250000 );
 * }</pre>
 *
 *
 * <h3>Getting values in a row</h3>
 * <pre>{@code
 * CSV csv = new CSV();
 * StringColumn first = csv.addStringColumn("first");
 * IntegerColumn salary = csv.addIntegerColumn("first");
 * Row = csv.addRow();
 * for (Row r : csv.findRows()){
 *     System.out.println( r.get(first) + "\t" + r.get(salary) );
 * }
 * }</pre>
 *
 */
public final class Row implements Serializable {

    /**
     * The Map containing the column data.
     */
    private final Map<Column, Object> map;

    /**
     * Creates a new Row.
     */
    public Row() {
        this.map = new TreeMap<>();
    }

    /**
     * Sets the value for the column.
     *
     * @param column the column to set
     * @param value the value
     * @return the row
     */
    public Row setRaw(final Column column, final String value) {
        map.put(column, column.parse(value));
        return this;
    }

    /**
     * Set the column with the value.
     *
     * @param column the column to set
     * @param value  the value
     * @return the row
     */
    public Row set(final ByteColumn column, final byte[] value) {
        map.put(column, value);
        return this;
    }

    /**
     * Set the column with the value.
     *
     * @param column the column to set
     * @param value  the value
     * @return the row
     */
    public Row set(final BigDecimalColumn column, final BigDecimal value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to set
     * @param value  the value
     * @return the row
     */
    public Row set(final IntegerColumn column, final Integer value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to set
     * @param value  the value
     * @return the row
     */
    public Row set(final FloatColumn column, final Float value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to set
     * @param value  the value
     * @return the row
     */
    public Row set(final DoubleColumn column, final Double value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to set
     * @param value  the value
     * @return the row
     */
    public Row set(final BooleanColumn column, final Boolean value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to set
     * @param value  the value
     * @return the row
     */
    public Row set(final StringColumn column, final String value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to set
     * @param value  the value
     * @return the row
     */
    public Row set(final UrlColumn column, final URL value) {
        map.put(column, value);
        return this;
    }

    /**
     * Updates the column with the value.
     *
     * @param column the column to set
     * @param value  the value
     * @return the row
     */
    public Row set(final DateColumn column, final Date value) {
        map.put(column, value);
        return this;
    }

    /**
     * Returns true if the column is null.
     *
     * @param column the column
     * @return true if column is null
     */
    public boolean isNull(final Column column) {
        return map.get(column) == null;
    }

    /**
     * Returns the column value as String.
     *
     * @param column the column
     * @return the column as a String
     */
    public String getRaw(final Column column) {
        Object value = map.get(column);
        if (value == null) {
            return "";
        } else {
            return value.toString();
        }
    }

    /**
     * Returns the value of the column.
     *
     * @param stringColumn the column
     * @return the value
     */
    public String get(final StringColumn stringColumn) {
        return (String) map.get(stringColumn);
    }

    /**
     * Returns the date of the column.
     *
     * @param dateColumn the column
     * @return the value
     */
    public Date get(final DateColumn dateColumn) {
        return (Date) map.get(dateColumn);
    }

    /**
     * Returns the value of the column.
     *
     * @param floatColumn the column
     * @return the value
     */
    public Float get(final FloatColumn floatColumn) {
        return (Float) map.get(floatColumn);
    }

    /**
     * Returns the value of the column.
     *
     * @param bigDecimalColumn the column
     * @return the value
     */
    public BigDecimal get(final BigDecimalColumn bigDecimalColumn) {
        return (BigDecimal) map.get(bigDecimalColumn);
    }

    /**
     * Returns the value of the column.
     *
     * @param integerColumn the column
     * @return the value
     */
    public Integer get(final IntegerColumn integerColumn) {
        return (Integer) map.get(integerColumn);
    }

    /**
     * Sets the value to be null.
     *
     * @param column the column
     */
    public void setNull(final Column column) {
        map.remove(column);
    }

    /**
     * Returns the value of the column.
     *
     * @param column the column
     * @return the value
     */
    public Object getObject(final Column column) {
        return map.get(column);
    }

    /**
     * Returns the value of the column.
     *
     * @param booleanColumn the column
     * @return the value
     */
    public Boolean get(final BooleanColumn booleanColumn) {
        return (Boolean) map.get(booleanColumn);
    }

    /**
     * Returns the value of the column.
     *
     * @param byteColumn the column
     * @return the value
     */
    public byte[] get(final ByteColumn byteColumn) {
        return (byte[]) map.get(byteColumn);
    }

    /**
     * Returns the value of the column.
     *
     * @param doubleColumn the column
     * @return the value
     */
    public Double get(final DoubleColumn doubleColumn) {
        return (Double) map.get(doubleColumn);
    }

    /**
     * Returns the value of the column.
     *
     * @param urlColumn the column
     * @return the value
     */
    public URL get(final UrlColumn urlColumn) {
        return (URL) map.get(urlColumn);
    }
}
