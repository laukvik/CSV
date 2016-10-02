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

import org.laukvik.csv.columns.BooleanColumn;
import org.laukvik.csv.columns.ByteColumn;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.io.AbstractReader;
import org.laukvik.csv.io.CsvReader;
import org.laukvik.csv.io.CsvWriter;
import org.laukvik.csv.io.Writeable;
import org.laukvik.csv.query.Query;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * An API for reading and writing to Viewer. The implementation is based on the
 * specficiations from http://tools.ietf.org/rfc/rfc4180.txt
 *
 * @author Morten Laukvik
 */
public final class CSV implements Serializable {

    public final static String MIME_TYPE = "text/csv";
    public final static String FILE_EXTENSION = "csv";

    public final static char LINEFEED = 10;
    public final static char RETURN = 13;

    public final static char COMMA = 44;
    public final static char SEMICOLON = 59;
    public final static char PIPE = 124;
    public final static char TAB = 9;

    public final static char DOUBLE_QUOTE = 34;
    public final static char SINGLE_QUOTE = 39;

    private MetaData metaData;
    private List<Row> rows;
    private Query query;
    private List<ChangeListener> listeners;
    private List<FileListener> fileListeners;
    private File file;

    public CSV() {
        metaData = new MetaData();
        rows = new ArrayList<>();
        query = null;
        listeners = new ArrayList<>();
        fileListeners = new ArrayList<>();
    }

    public File getFile() {
        return file;
    }

    /**
     * Creates new headers.
     *
     */
    public void insertHeaders(){
        Row r = addRow(0);
        for (int x=0; x<getMetaData().getColumnCount(); x++){
            StringColumn c = (StringColumn) getMetaData().getColumn(x);
            r.update(c, c.getName());
            c.setName("Column" + (x+1));
        }
        fireRowCreated(0, r);
    }

    protected List<Row> getRows() {
        return rows;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public int getRowCount() {
        return rows.size();
    }

    public Row getRow(int rowIndex) {
        if (rowIndex > getRowCount()) {
            throw new RowNotFoundException(rowIndex, getRowCount());
        }
        return rows.get(rowIndex);
    }

    public Row addRow() {
        Row r = new Row();
        addRow(r);
        return r;
    }

    public Row addRow(int rowIndex) {
        Row r = new Row();
        r.setCSV(this);
        rows.add(rowIndex, r);
        fireRowCreated(rowIndex, r);
        return r;
    }

    public Row addRow(Row row) {
        row.setCSV(this);
        rows.add(row);
        fireRowCreated(rows.size(), row);
        return row;
    }

    public void removeRow(int rowIndex) {
        Row row = rows.remove(rowIndex);
        fireRowRemoved(rowIndex, row );
    }

    public void clear() {
        rows.clear();
    }

    public Column addColumn(Column column) {
        metaData.addColumn(column);
        fireColumnCreated(column);
        return column;
    }

    /**
     * Adds a new text column with the specified name
     *
     * @param name
     * @return
     */
    public Column addColumn(String name) {
        return addStringColumn(name);
    }

    public StringColumn addStringColumn(String name) {
        return addStringColumn(new StringColumn(name));
    }

    public StringColumn addStringColumn(StringColumn column) {
        addColumn(column);
        return column;
    }

    public IntegerColumn addIntegerColumn(IntegerColumn column) {
        addColumn(column);
        return column;
    }

    public FloatColumn addFloatColumn(FloatColumn column) {
        addColumn(column);
        return column;
    }

    public BooleanColumn addBooleanColumn(BooleanColumn column) {
        addColumn(column);
        return column;
    }

    public ByteColumn addByteColumn(ByteColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Removes the column and all its data
     *
     * @param column
     */
    public void removeColumn(Column column) {
        for (Row r : rows) {
            r.remove(column);
        }
        getMetaData().removeColumn(column);
    }


    /**
     * Removes all rows
     *
     * @param fromRowIndex
     * @param endRowIndex
     */
    public void removeRows(int fromRowIndex, int endRowIndex) {
        rows.subList(fromRowIndex, endRowIndex + 1).clear();
    }

    public Row insertRow(Row row, int rowIndex) {
        row.setCSV(this);
        rows.add(rowIndex, row);
        fireRowCreated(rowIndex, row);
        return row;
    }

    /**
     * Reads the File
     *
     * @param reader
     */
    public void readFile(final File file, final AbstractReader reader) {
        this.query = null;
        rows = new ArrayList<>();
        fireBeginRead(file);
        this.metaData = reader.getMetaData();
        this.metaData.setCSV(this);
        fireMetaDataRead();
        while (reader.hasNext()) {
            addRow(reader.getRow());
        }
        fireFinishRead(file);
    }

    /**
     * Reads the CSV file
     *
     * @param file
     * @throws IOException
     */
    public void readFile(final File file) throws IOException {
        readFile(file,new CsvReader(file));
        this.file = file;
    }

    public void readFileWithSeparator(final File file, final char separator) throws IOException {
        readFile(file,new CsvReader(file, Charset.defaultCharset(), separator, CSV.DOUBLE_QUOTE ));
    }

    public void readFileWithSeparator(final File file, final char separator, final char quote) throws IOException {
        readFile(file,new CsvReader(file, Charset.defaultCharset(), separator, quote ));
    }

    public void importFile(final File file){
    }

    /**
     * Writes the contents to a file using the specified Writer.
     *
     * @param writer the Writer
     * @throws Exception
     */
    public void write(final Writeable writer) throws Exception {
        fireBeginWrite(writer.getFile());
        writer.writeFile(this);
        writer.close();
        fireFinishWrite(writer.getFile());
    }

    public void writeFile(final File file) throws Exception {
        write(new CsvWriter(file,this));
    }

    /**
     * Writes the
     * @param file
     */
    public void exportFile(final File file){
    }

    /**
     * Returns the Library folder for the user.
     *
     * @return
     */
    private static File getLibrary() {
        return new File(System.getProperty("user.home"), "Library");
    }

    /**
     * Returns the folder where all configuration of CSV is.
     *
     * @return
     */
    private static File getHome() {
        File file = new File(getLibrary(), "org.laukvik.csv");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public void clearQuery() {
        this.query = null;
    }

    public Query findByQuery() {
        this.query = new Query(metaData, this);
        return this.query;
    }

    public DistinctColumnValues getDistinctColumnValues(int columnIndex) {
        Column c = metaData.getColumn(columnIndex);
        DistinctColumnValues cv = new DistinctColumnValues(c);
        for (Row r : rows) {
            cv.addValue(r.getAsString(c));
        }
        return cv;
    }

    /**
     * Returns a set of unique values for the specified column
     *
     * @param column
     * @return
     */
    public Set<String> listDistinct(Column column) {
        Set<String> values = new TreeSet<>();
        for (Row r : rows) {
            values.add(r.getAsString(column));
        }
        return values;
    }

    /******************* Change: Listeners ***************************************************/
    public void addChangeListener(ChangeListener l) {
        listeners.add(l);
    }

    public void removeChangeListener(ChangeListener l) {
        listeners.remove(l);
    }

    private void fireMetaDataRead(){
        for (ChangeListener l : listeners){
            l.metaDataRead(this.getMetaData());
        }
    }

    private void fireRowCreated(int rowIndex, Row row){
        for (ChangeListener l : listeners){
            l.rowCreated(rowIndex,row);
        }
    }

    private void fireRowUpdated(int rowIndex, Row row){
        for (ChangeListener l : listeners){
            l.rowUpdated(rowIndex,row);
        }
    }

    private void fireRowRemoved(int rowIndex, Row row){
        for (ChangeListener l : listeners){
            l.rowRemoved(rowIndex,row);
        }
    }

    protected void fireColumnCreated(Column column){
        for (ChangeListener l : listeners){
            l.columnCreated(column);
        }
    }

    public void fireColumnUpdated(Column column){
        for (ChangeListener l : listeners){
            l.columnUpdated(column);
        }
    }

    protected void fireColumnRemoved(Column column){
        for (ChangeListener l : listeners){
            l.columnRemoved(column.indexOf());
        }
    }

    /******************* File: Listeners ***************************************************/

    public void addFileListener(FileListener l) {
        fileListeners.add(l);
    }

    public void removeFileListener(FileListener l) {
        fileListeners.remove(l);
    }

    private void fireBeginRead(File file){
        for (FileListener l : fileListeners){
            l.beginRead(file);
        }
    }

    private void fireFinishRead(File file){
        for (FileListener l : fileListeners){
            l.finishRead(file);
        }
    }

    private void fireBeginWrite(File file){
        for (FileListener l : fileListeners){
            l.beginRead(file);
        }
    }

    private void fireFinishWrite(File file){
        for (FileListener l : fileListeners){
            l.finishRead(file);
        }
    }

}
