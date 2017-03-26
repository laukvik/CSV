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
 * Represents a single row in the CSV.
 *
 * <h3>Examples</h3>
 * <pre>
 * CSV csv = new CSV();
 * StringColumn first = csv.addStringColumn("first");
 * Row = csv.addRow();
 * row.setString( first, "Bill" );
 * </pre>
 *
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
     * @param column the column to setDate
     * @param value the value
     * @return the row
     */
    public Row set(final Column column, final String value) {
        map.put(column, column.parse(value));
        return this;
    }

    /**
     * Set the column with the value.
     *
     * @param column the column to setDate
     * @param value  the value
     * @return the row
     */
    public Row setBytes(final ByteColumn column, final byte[] value) {
        map.put(column, value);
        return this;
    }

    /**
     * Set the column with the value.
     *
     * @param column the column to setDate
     * @param value  the value
     * @return the row
     */
    public Row setBigDecimal(final BigDecimalColumn column, final BigDecimal value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to setDate
     * @param value  the value
     * @return the row
     */
    public Row setInteger(final IntegerColumn column, final Integer value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to setDate
     * @param value  the value
     * @return the row
     */
    public Row setFloat(final FloatColumn column, final Float value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to setDate
     * @param value  the value
     * @return the row
     */
    public Row setDouble(final DoubleColumn column, final Double value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to setDate
     * @param value  the value
     * @return the row
     */
    public Row setBoolean(final BooleanColumn column, final Boolean value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to setDate
     * @param value  the value
     * @return the row
     */
    public Row setString(final StringColumn column, final String value) {
        map.put(column, value);
        return this;
    }

    /**
     * Sets the column with the value.
     *
     * @param column the column to setDate
     * @param value  the value
     * @return the row
     */
    public Row setURL(final UrlColumn column, final URL value) {
        map.put(column, value);
        return this;
    }

    /**
     * Updates the column with the value.
     *
     * @param column the column to setDate
     * @param value  the value
     * @return the row
     */
    public Row setDate(final DateColumn column, final Date value) {
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
    public String getAsString(final Column column) {
        Object value = map.get(column);
        if (value == null){
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
    public String getString(final StringColumn stringColumn) {
        return (String) map.get(stringColumn);
    }

    /**
     * Returns the date of the column.
     *
     * @param dateColumn the column
     * @return the value
     */
    public Date getDate(final DateColumn dateColumn) {
        return (Date) map.get(dateColumn);
    }

    /**
     * Returns the value of the column.
     *
     * @param floatColumn the column
     * @return the value
     */
    public Float getFloat(final FloatColumn floatColumn) {
        return (Float) map.get(floatColumn);
    }

    /**
     * Returns the value of the column.
     *
     * @param bigDecimalColumn the column
     * @return the value
     */
    public BigDecimal getBigDecimal(final BigDecimalColumn bigDecimalColumn) {
        return (BigDecimal) map.get(bigDecimalColumn);
    }


    /**
     * Returns the value of the column.
     *
     * @param integerColumn the column
     * @return the value
     */
    public Integer getInteger(final IntegerColumn integerColumn) {
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
    public Object get(final Column column) {
        return map.get(column);
    }

    /**
     * Returns the value of the column.
     *
     * @param booleanColumn the column
     * @return the value
     */
    public Boolean getBoolean(final BooleanColumn booleanColumn) {
        return (Boolean) map.get(booleanColumn);
    }

    /**
     * Returns the value of the column.
     *
     * @param byteColumn the column
     * @return the value
     */
    public byte[] getBytes(final ByteColumn byteColumn) {
        return (byte[]) map.get(byteColumn);
    }

    /**
     * Returns the value of the column.
     *
     * @param doubleColumn the column
     * @return the value
     */
    public Double getDouble(final DoubleColumn doubleColumn) {
        return (Double) map.get(doubleColumn);
    }

    /**
     * Returns the value of the column.
     *
     * @param urlColumn the column
     * @return the value
     */
    public URL getURL(final UrlColumn urlColumn) {
        return (URL) map.get(urlColumn);
    }
}
