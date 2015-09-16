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
import java.math.BigDecimal;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import org.laukvik.csv.columns.Column;

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
 * <li>getString
 * <li>getURL
 *
 * <li>getTimestamp
 * <li>getByte-
 * <li>getTime -
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class Row implements Serializable {

    private final List<String> values;
    private MetaData metaData;
    private String raw;

    public Row() {
        values = new ArrayList<>();
    }

    public Row(String... values) {
        this.values = new ArrayList<>();
        this.values.addAll(Arrays.asList(values));
    }

    public Row(List<String> values) {
        this.values = values;
    }

    public Row(MetaData metaData, List<String> values) {
        this.metaData = metaData;
        this.values = values;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public void setValue(int columnIndex, Object value) {
        Column c = metaData.getColumn(columnIndex);
        values.set(columnIndex, c.asString(value));
    }

    public Object getValue(String column) {
        int columnIndex = metaData.getColumnIndex(column);
        Column c = metaData.getColumn(columnIndex);
        return c.parse(values.get(columnIndex));
    }

    public Object getValue(int columnIndex) {
        Column c = metaData.getColumn(columnIndex);
        return c.parse(values.get(columnIndex));
    }

    public String getRaw() {
        return raw;
    }

    protected void add(String value) {
        this.values.add(value);
    }

    protected List<String> getValues() {
        return values;
    }

    public String getString(int column) {
        if (metaData == null) {
            throw new MetaDataNotFoundException();
        }
        if (column > metaData.getColumnCount()) {
            throw new ColumnNotFoundException(column, metaData.getColumnCount());
        }
        String v = values.get(column);
        if (v == null) {
            return "";
        }
        return new String(v.getBytes(metaData.getCharset()));
    }

    public String getString(String column) {
        if (metaData == null) {
            throw new MetaDataNotFoundException();
        }
        return getString(metaData.getColumnIndex(column));
    }

    public void setString(String value, int column) {
        values.set(column, value);
    }

    public URL getURL(String column) {
        return getURL(metaData.getColumnIndex(column));
    }

    public URL getURL(int columnIndex) {
        String value = getString(columnIndex);
        try {
            return new URL(value);
        }
        catch (Exception e) {
            throw new ConversionException(value, "URL");
        }
    }

    public Long getLong(String column) {
        return getLong(metaData.getColumnIndex(column));
    }

    public Long getLong(int columnIndex) {
        String value = getString(columnIndex);
        try {
            return new Long(value);
        }
        catch (Exception e) {
            throw new ConversionException(value, "Long");
        }
    }

    public Integer getInteger(String column) {
        return getInteger(metaData.getColumnIndex(column));
    }

    public Integer getInteger(int columnIndex) {
        String value = getString(columnIndex);
        try {
            return new Integer(value);
        }
        catch (Exception e) {
            throw new ConversionException(value, "Integer");
        }
    }

    public Float getFloat(String column) {
        return getFloat(metaData.getColumnIndex(column));
    }

    public Float getFloat(int columnIndex) {
        String value = getString(columnIndex);
        try {
            return new Float(value);
        }
        catch (Exception e) {
            throw new ConversionException(value, "Float");
        }
    }

    public Double getDouble(String column) {
        return getDouble(metaData.getColumnIndex(column));
    }

    public Double getDouble(int columnIndex) {
        String value = getString(columnIndex);
        try {
            return new Double(value);
        }
        catch (Exception e) {
            throw new ConversionException(value, "Double");
        }
    }

    public Date getDate(String column) {
        String value = getString(column);
        try {
            Long millis = Long.parseLong(value);
            return new Date(millis);
        }
        catch (Exception e) {
            throw new ConversionException(value, "Date(in milliseconds since 1970)");
        }
    }

    public Date getDate(int column) {
        String value = getString(column);
        try {
            Long millis = Long.parseLong(value);
            return new Date(millis);
        }
        catch (Exception e) {
            throw new ConversionException(value, "Date(in milliseconds since 1970)");
        }
    }

    public Date getDate(int columnIndex, String pattern) {
        return getDate(columnIndex, new SimpleDateFormat(pattern));
    }

    public Date getDate(String column, String pattern) {
        return getDate(metaData.getColumnIndex(column), new SimpleDateFormat(pattern));
    }

    public Date getDate(String column, SimpleDateFormat format) {
        return getDate(metaData.getColumnIndex(column), format);
    }

    public Date getDate(int columnIndex, DateFormat format) {
        String value = getString(columnIndex);
        try {
            return format.parse(value);
        }
        catch (Exception e) {
            throw new ConversionException(value, "Date");
        }
    }

    public Boolean getBoolean(String column) {
        return getBoolean(metaData.getColumnIndex(column));
    }

    public Boolean getBoolean(int columnIndex) {
        String value = getString(columnIndex);
        try {
            return new Boolean(value);
        }
        catch (Exception e) {
            throw new ConversionException(value, "Boolean");
        }
    }

    public BigDecimal getBigDecimal(String column) {
        return getBigDecimal(metaData.getColumnIndex(column));
    }

    public BigDecimal getBigDecimal(int columnIndex) {
        String value = getString(columnIndex);
        try {
            return new BigDecimal(value);
        }
        catch (Exception e) {
            throw new ConversionException(value, "BigDecimal");
        }
    }

    protected void insert(String value, int columnIndex) {
        values.add(columnIndex, value);
    }

    protected void remove(int columnIndex) {
        values.remove(columnIndex);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(values);
        return b.toString();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.values);
        hash = 41 * hash + Objects.hashCode(this.raw);
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
        if (!Objects.equals(this.values, other.values)) {
            return false;
        }
        if (!Objects.equals(this.raw, other.raw)) {
            return false;
        }
        return true;
    }

    public boolean isEmpty(int columnIndex) {
        return values.get(columnIndex).isEmpty();
    }

}
