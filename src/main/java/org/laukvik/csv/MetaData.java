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

import org.laukvik.csv.columns.Column;
import org.laukvik.csv.io.BOM;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class MetaData implements Serializable {

    private final List<Column> columns;
    private Charset charset;
    private char separatorChar;
    private char quoteChar;
    private CSV csv;

    public MetaData(){
        charset = Charset.defaultCharset();
        columns = new ArrayList<>();
    }

    public void fireColumnChanged(Column column){
        csv.fireColumnUpdated(column);
    }

    public CSV getCSV() {
        return csv;
    }

    public void setCSV(CSV csv) {
        this.csv = csv;
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

    /**
     *
     * columnName(DataType=option)
     *
     * @param columnName
     * @return
     */
    public Column addColumn(String columnName) {
        return addColumn(Column.parseName(columnName));
    }

    public Column addColumn(Column column) {
        column.setMetaData(this);
        columns.add(column);
        if (csv != null){
            csv.fireColumnCreated(column);
        }
        return column;
    }

    public void removeColumn(Column column) {
        columns.remove(column);
        csv.fireColumnRemoved(column);
        column.setMetaData(null);
    }

    public int indexOf(Column column) {
        return columns.indexOf(column);
    }

    /**
     * @todo Remove this convenience method
     * @param column
     * @return
     */
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

    public void removeColumn(int columnIndex) {
        Column c = columns.get(columnIndex);
        c.setMetaData(null);
        columns.remove(c);
        csv.fireColumnRemoved(c);
    }

    public char getSeparatorChar() {
        return separatorChar;
    }

    public void setSeparator(final Character separatorChar) {
        this.separatorChar = separatorChar;
    }

    public char getQuoteChar() {
        return quoteChar;
    }

    public void setQuoteChar(final char quoteChar) {
        this.quoteChar = quoteChar;
    }

    public BOM getBOM(){
        if (charset == null){
            return null;
        }
        for (BOM b : BOM.values()){
            if (charset.equals(b)){
                return b;
            }
        }
        return null;
    }
}
