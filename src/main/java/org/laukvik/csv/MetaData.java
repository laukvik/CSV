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
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.columns.UrlColumn;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class MetaData implements Serializable {

    private final List<Column> columns;
    private Charset charset;

    public MetaData() {
        charset = Charset.defaultCharset();
        columns = new ArrayList<>();
    }

    public MetaData(Column... columns) {
        charset = Charset.defaultCharset();
        this.columns = new ArrayList<>(Arrays.asList(columns));
    }

    public Column getColumn(String name) {
        return columns.get(getColumnIndex(name));
    }

    public Column getColumn(int columnIndex) {
        return columns.get(columnIndex);
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public int getColumnCount() {
        return columns.size();
    }

    private String getParantheses() {
        return "";
    }

    public void addColumn(String s) {
        if (s.toUpperCase().endsWith("(INT)") || s.toUpperCase().endsWith("ID")) {
            addColumn(new IntegerColumn(s));
        } else if (s.toUpperCase().endsWith("(FLOAT)")) {
            addColumn(new FloatColumn(s));
        } else if (s.toUpperCase().endsWith("(DOUBLE)")) {
            addColumn(new DoubleColumn(s));
        } else if (s.toUpperCase().endsWith("(URL)")) {
            addColumn(new UrlColumn(s));
//        } else if (s.toUpperCase().endsWith("(BOOLEAN)")) {
//            addColumn(new BooleanColumn(s));
        } else if (s.toUpperCase().endsWith("(DATE:MM/DD/YYYY)")) {
            addColumn(new DateColumn(s, new SimpleDateFormat("MM/dd/yyyy")));
        } else {
            addColumn(new StringColumn(s));
        }
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public void removeColumn(Column column) {
        columns.remove(column);
    }

    public int indexOf(Column column) {
        return columns.indexOf(column);
    }

    public String getColumnName(int column) {
        return columns.get(column).getName();
    }

    public void setColumnName(String name, int column) {
        columns.get(column).setName(name);
    }

    public int getColumnIndex(String column) {
        int x = 0;
        for (Column c : columns) {
            if (c.getName().equalsIgnoreCase(column)) {
                return x;
            }
            x++;
        }
        throw new ColumnNotFoundException(x);
    }

    public void addColumn(String name, int columnIndex) {
//        columns.add(new StringColumn(name), name);
        throw new IllegalArgumentException("Not implemented yet");
    }

    public void removeColumn(int columnIndex) {
        columns.remove(columnIndex);
    }

    public Row createEmptyRow() {
        Row r = new Row();
        r.setMetaData(this);
        for (Column c : columns) {
            r.add("");
        }
        return r;
    }

}
