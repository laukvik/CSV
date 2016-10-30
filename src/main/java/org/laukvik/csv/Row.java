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
import java.util.Objects;
import java.util.TreeMap;

/**
 * Represents a single Row in a CSV data set.
 *
 * @author Morten Laukvik
 */
public final class Row implements Serializable {

    /**
     * The timestamp it was created. Used only internally to separate rows.
     */
    protected final long timestamp;
    /**
     * The Map containing the column data.
     */
    private final Map<Column, Object> map;
    /** The CSV the row belongs to. */
    private CSV csv;

    /**
     * Creates a new Row.
     */
    public Row() {
        timestamp = System.nanoTime();
        this.map = new TreeMap<>();
    }

    /**
     * Returns the row formatted as a String.
     * @return the row as STring
     */
    public String toString() {
        StringBuilder b = new StringBuilder();
        int x = 0;
        for (Column c : map.keySet()) {
            if (x > 0) {
                b.append(",");
            }
            Object o = map.get(c);
            b.append(c.getName()).append("=").append(o);
            x++;
        }
        return b.toString();
    }

    /**
     * Returns the CSV it belongs to.
     * @return the CSV
     */
    public CSV getCSV() {
        return csv;
    }

    /**
     * Sets the CSV it belongs to.
     *
     * @param csv
     */
    public void setCSV(final CSV csv) {
        this.csv = csv;
    }

    /**
     * Updates the column with the value
     *
     * @param column the column to update
     * @param value
     * @return
     */
    public Row updateColumn(Column column, String value) {
        map.put(column, column.parse(value));
        return this;
    }

    public Row update(ByteColumn column, Byte value) {
        map.put(column, value);
        return this;
    }

    public Row update(BigDecimalColumn column, BigDecimal value) {
        map.put(column, value);
        return this;
    }

    public Row update(IntegerColumn column, Integer value) {
        map.put(column, value);
        return this;
    }

    public Row update(FloatColumn column, Float value) {
        map.put(column, value);
        return this;
    }

    public Row update(DoubleColumn column, Double value) {
        map.put(column, value);
        return this;
    }

    public Row update(BooleanColumn column, Boolean value) {
        map.put(column, value);
        return this;
    }

    public Row update(StringColumn column, String value) {
        map.put(column, value);
        return this;
    }

    public Row update(UrlColumn column, URL value) {
        map.put(column, value);
        return this;
    }

    public Row update(DateColumn column, Date value) {
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
     * Returns the column as String
     *
     * @param column the column
     * @return the column as a String
     */
    public String getAsString(final Column column) {
        return map.get(column) + "";
    }

    public String getString(StringColumn column) {
        return (String) map.get(column);
    }

    public Date getDate(DateColumn column) {
        return (Date) map.get(column);
    }

    public Float getFloat(FloatColumn column) {
        return (Float) map.get(column);
    }

    public Integer getInteger(IntegerColumn column) {
        return (Integer) map.get(column);
    }

    /**
     * Returns the hashCode
     * @return the hashCode
     */
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.csv);
        return hash;
    }

    /**
     * Returns true if the objects are equals
     *
     * @param obj the object
     * @return true when equals
     */
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Row otherRow = (Row) obj;
        return timestamp == otherRow.timestamp;
    }

    /**
     * Removes the value for the column
     *
     * @param column the column
     */
    public void remove(final Column column) {
        map.remove(column);
    }

    /**
     * Returns the row index.
     * @return the row index
     */
    public int indexOf() {
        return csv.indexOf(this);
    }
}
