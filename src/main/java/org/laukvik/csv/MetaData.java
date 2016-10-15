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
import java.util.Collections;
import java.util.List;

/**
 * Specifies information about all the columns in the data set.
 *
 */
public class MetaData implements Serializable {

    private final List<Column> columns;
    private Charset charset;
    private Character separatorChar;
    private Character quoteChar;
    private CSV csv;

    /**
     * Creates a new instance.
     */
    public MetaData(){
        charset = Charset.defaultCharset();
        columns = new ArrayList<>();
    }

    public void fireColumnChanged(Column column){
        csv.fireColumnUpdated(column);
    }

    /**
     * Returns the CSV it belongs to
     *
     * @return the CSV
     */
    public CSV getCSV() {
        return csv;
    }

    /**
     * Sets the CSV it belongs to
     *
     * @param csv the CSV
     */
    public void setCSV(CSV csv) {
        this.csv = csv;
    }

    /**
     * Returns the column with the specified name
     *
     * @param name the column name
     * @return the column
     */
    public Column getColumn(String name) {
        return columns.get(indexOf(name));
    }

    /**
     * Returns the column with the specified name
     *
     * @param columnIndex the column index
     * @return the column
     */
    public Column getColumn(int columnIndex) {
        return columns.get(columnIndex);
    }

    /**
     * Returns the Charset being used
     *
     * @return the Charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Sets the Charset
     *
     * @param charset the charset
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * Returns the amount of columns
     *
     * @return the amount of columns
     */
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * Adds a new column with the specified name
     *
     * columnName(DataType=option)
     *
     * @param name the name
     * @return the column added
     */
    public Column addColumn(String name) {
        return addColumn(Column.parseName(name));
    }

    /**
     * Adds the column
     *
     * @param column the column to add
     * @return the added column
     */
    public Column addColumn(Column column) {
        column.setMetaData(this);
        columns.add(column);
        if (csv != null){
            for (int x=0; x<csv.getRowCount(); x++){
                csv.getRow(x).updateColumn(column, "");
            }
            csv.fireColumnCreated(column);
        }
        return column;
    }

    /**
     * Removes the column
     *
     * @param column the column to remove
     */
    public void removeColumn(Column column) {
        columns.remove(column);
        csv.removeColumn(column);
        column.setMetaData(null);
    }

    /**
     * Changes the order of a specified column to another
     *
     * @param fromIndex the index of the column to move
     * @param toIndex the new index of the column
     */
    public void moveColumn(int fromIndex, int toIndex){
        Collections.swap(columns, fromIndex, toIndex);
        csv.fireColumnMoved(fromIndex, toIndex);
    }

    /**
     * Returns the index of the specified Column
     *
     * @param column the column
     * @return the index
     */
    public int indexOf(Column column) {
        return columns.indexOf(column);
    }

    /**
     * Returns the column index of the column with the specified column name
     *
     * @param columnName the column
     * @return the index
     */
    private int indexOf(String columnName) {
        int x = 0;
        for (Column c : columns) {
            if (c.getName().equalsIgnoreCase(columnName)) {
                return x;
            }
            x++;
        }
        throw new ColumnNotFoundException(x);
    }

    /**
     * Removes the column with the columnIndex
     *
     * @param columnIndex the column index
     */
    public void removeColumn(int columnIndex) {
        Column c = columns.get(columnIndex);
        c.setMetaData(null);
        columns.remove(c);
        csv.fireColumnRemoved(c);
    }

    /**
     * Returns the separator character
     *
     * @return the separator character
     */
    public Character getSeparatorChar() {
        return separatorChar;
    }

    /**
     * Sets the separator character
     *
     * @param separatorChar the separator character
     */
    public void setSeparator(final Character separatorChar) {
        this.separatorChar = separatorChar;
    }

    /**
     * Returns the quote character
     *
     * @return the quote character
     */
    public char getQuoteChar() {
        return quoteChar;
    }

    /**
     * Sets the quote character
     *
     * @param quoteChar the quote character
     */
    public void setQuoteChar(final char quoteChar) {
        this.quoteChar = quoteChar;
    }

    /**
     * Returns the BOM for the charset used
     *
     * @return the BOM
     */
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
