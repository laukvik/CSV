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

import java.io.Serializable;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import org.laukvik.csv.columns.BooleanColumn;
import org.laukvik.csv.columns.ByteColumn;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.columns.UrlColumn;

/**
 * Date created = is.readDate("created");<br>
 * Float percent = is.readFloat("sallary");<br>
 * int sallary = is .readInt("sallary");<br>
 *
 * <li>getBigDecimal
 * <li>getBoolean
 * <li>getDate
 * <li>getDouble
 * <li>getFloat
 * <li>getInt
 * <li>getLong
 * <li>getAsString
 * <li>getURL
 *
 * <li>getTimestamp
 * <li>getByte-
 * <li>getTime -
 *
 * CSV.addRow().add("First");
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class Row implements Serializable {

    private CSV csv;
    private final Map<Column, Object> map;

    public Row() {
        this.map = new TreeMap<>();
    }

    @Override
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

    public CSV getCSV() {
        return csv;
    }

    public void setCSV(CSV csv) {
        this.csv = csv;
    }

    public Row update(ByteColumn column, Byte value) {
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

    public boolean isNull(Column column) {
        return map.get(column) == null;
    }

    public String getAsString(Column column) {
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.csv);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Row other = (Row) obj;
        return true;
    }

    public void remove(Column column) {
        map.remove(column);
    }

}
