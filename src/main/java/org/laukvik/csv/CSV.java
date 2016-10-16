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
import org.laukvik.csv.io.BOM;
import org.laukvik.csv.io.ClosableReader;
import org.laukvik.csv.io.CsvReader;
import org.laukvik.csv.io.CsvWriter;
import org.laukvik.csv.io.HtmlWriter;
import org.laukvik.csv.io.JavaReader;
import org.laukvik.csv.io.JsonWriter;
import org.laukvik.csv.io.Writeable;
import org.laukvik.csv.io.XmlWriter;
import org.laukvik.csv.query.Query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * An API for reading and writing to Viewer. The implementation is based on the
 * specficiations from http://tools.ietf.org/rfc/rfc4180.txt
 *
 * <code>
 *     CSV csv = new CSV( new File("presidents.csv") );
 * </code>
 *
 * <code>
 *     CSV csv = new CSV( new File("presidents.csv") );
 *     List&lt;Row&gt; rows = csv.findByQuery().where().column("Party").is("Whig").getResultList();
 * </code>
 *
 * @author Morten Laukvik
 */
public final class CSV implements Serializable {


    public final static char LINEFEED = 10;
    public final static char RETURN = 13;

    public final static char COMMA = 44;
    public final static char SEMICOLON = 59;
    public final static char PIPE = 124;
    public final static char TAB = 9;

    public final static char DOUBLE_QUOTE = 34;
    public final static char SINGLE_QUOTE = 39;
    private final List<ChangeListener> changeListeners;
    private final List<FileListener> fileListeners;
    private MetaData metaData;
    private List<Row> rows;
    private Query query;
    private File file;

    /**
     * Opens an empty file.
     */
    public CSV() {
        metaData = new MetaData();
        rows = new ArrayList<>();
        query = null;
        changeListeners = new ArrayList<>();
        fileListeners = new ArrayList<>();
    }

    /**
     * Opens the specified CSV file and parses it
     *
     * @param file the file to open
     * @throws IOException when the file could not be read
     */
    public CSV(File file) throws IOException {
        this();
        readFile(file);
    }

    /**
     * Returns an array of the supported separator characters.
     *
     * @return separator characters
     */
    public static char[] listSupportedSeparatorChars() {
        return new char []{COMMA,SEMICOLON,PIPE,TAB};
    }

    /**
     * Looks for a BOM in a file and returns its Charsets or returns the
     * System charset.
     *
     * @param file the file to analyze
     * @return the Charset
     */
    private static Charset findCharsetByBOM(final File file) {
        BOM bom = BOM.findBom(file);
        return bom == null ? Charset.defaultCharset() : bom.getCharset();
    }

    /**
     * Returns the opened file
     *
     * @return the opened file
     */
    public File getFile() {
        return file;
    }

    /**
     * Moves all rows down and inserts new columns.
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

    /**
     * Returns the rows
     *
     * @return the rows
     */
    protected List<Row> getRows() {
        return rows;
    }

    /**
     * Returns the MetaData
     *
     * @return the MetaData
     */
    public MetaData getMetaData() {
        return metaData;
    }

    /**
     * Sets the MetaData
     * @param metaData the MetaData to set
     */
    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    /**
     * Returns the amount of rows
     *
     * @return the amount of rows
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Returns the row with the specified index
     *
     * @param rowIndex the index
     * @return the row
     */
    public Row getRow(int rowIndex) {
        if (rowIndex > getRowCount()) {
            throw new RowNotFoundException(rowIndex, getRowCount());
        }
        return rows.get(rowIndex);
    }

    /**
     * Creates a new empty row
     *
     * @return the newly created row
     */
    public Row addRow() {
        Row r = new Row();
        addRow(r);
        return r;
    }

    /**
     * Creates a new empty row at the specified index
     *
     * @param rowIndex the index to insert at
     * @return the newly created row
     */
    public Row addRow(int rowIndex) {
        Row r = new Row();
        r.setCSV(this);
        rows.add(rowIndex, r);
        fireRowCreated(rowIndex, r);
        return r;
    }

    /**
     * Adds a new row
     *
     * @param row the row to add
     * @return the row being added
     */
    private Row addRow(Row row) {
        row.setCSV(this);
        rows.add(row);
        fireRowCreated(rows.size(), row);
        return row;
    }

    /**
     * Removes the row at the specified index
     *
     * @param index the index to remove
     */
    public void removeRow(int index) {
        Row row = rows.remove(index);
        fireRowRemoved(index, row );
    }

    /**
     * Removes all rows.
     *
     */
    public void removeRowsBetween() {
        int count = rows.size();
        rows.clear();
        fireRowsRemoved(0, count);
    }

    /**
     * Adds the column
     *
     * @param column the column
     * @return the newly added column
     */
    public Column addColumn(Column column) {
        metaData.addColumn(column);
        fireColumnCreated(column);
        return column;
    }

    /**
     * Moves a row to another index
     *
     * @param fromRowIndex the row to move
     * @param toRowIndex the destination index
     */
    public void moveRow(final int fromRowIndex, final int toRowIndex) {
        Collections.swap(rows, fromRowIndex, toRowIndex);
        fireRowMoved(fromRowIndex, toRowIndex);
    }

    /**
     * Adds a new text column with the specified name
     *
     * @param name the name of the column
     * @return the newly create column
     */
    public Column addColumn(String name) {
        return addStringColumn(name);
    }

    /**
     * Adds a new StringColumn with the specified name
     *
     * @param name the name
     * @return the StringColumn being created
     */
    public StringColumn addStringColumn(String name) {
        return addStringColumn(new StringColumn(name));
    }

    /**
     * Adds a new StringColumn with the specified name
     *
     * @param column the StringColumn
     * @return the StringColumn being created
     */
    private StringColumn addStringColumn(StringColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Adds a new IntegerColumn
     *
     * @param column the IntegerColumn
     * @return the IntegerColumn being created
     */
    public IntegerColumn addIntegerColumn(IntegerColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Adds a new FloatColumn
     *
     * @param column the FloatColumn
     * @return the FloatColumn being created
     */
    public FloatColumn addFloatColumn(FloatColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Adds a new BooleanColumn
     *
     * @param column the BooleanColumn
     * @return the BooleanColumn being created
     */
    public BooleanColumn addBooleanColumn(BooleanColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Adds a new ByteColumn
     *
     * @param column the ByteColumn
     * @return the ByteColumn being created
     */
    public ByteColumn addByteColumn(ByteColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Removes the column and all its data
     *
     * @param column the column
     */
    public void removeColumn(Column column) {
        for (Row r : rows) {
            r.remove(column);
        }
        getMetaData().removeColumn(column);
    }

    /**
     * Removes all rows within the specified range
     *
     * @param fromRowIndex the start index
     * @param endRowIndex the end index
     */
    public void removeRowsBetween(int fromRowIndex, int endRowIndex) {
        rows.subList(fromRowIndex, endRowIndex + 1).clear();
        fireRowsRemoved(fromRowIndex, endRowIndex);
    }

    /**
     * Reads the File using the specified reader. System default charset is used when the charset is not
     * specified.
     *
     * @param file
     * @param charset
     * @param reader
     */
    private void readFile(final File file, final Charset charset, final ClosableReader reader) {
        this.file = file;
        this.query = null;
        this.rows = new ArrayList<>();
        fireBeginRead(file);
        this.metaData = reader.getMetaData();
        this.metaData.setCSV(this);
        this.metaData.setCharset(charset == null ? findCharsetByBOM(file) : charset);
        fireMetaDataRead();
        long max = file.length();
        while (reader.hasNext()) {
            fireBytesRead(reader.getBytesRead(), max);
            addRow(reader.getRow());
        }
        fireFinishRead(file);
    }

    /**
     * Reads the CSV file
     *
     * @param file the file to read
     * @throws IOException when the file could not be read
     */
    public void readFile(final File file) throws IOException {
        Charset charset = findCharsetByBOM(file);
        CsvReader reader = new CsvReader(  Files.newBufferedReader(file.toPath(), charset), null, CSV.DOUBLE_QUOTE );
        readFile(file, charset, reader );
    }

    /**
     * Reads the CSV file with the specified charset
     *
     * @param file the file to read
     * @param charset the charset
     * @throws IOException when the file could not be read
     */
    public void readFile(final File file, final Charset charset) throws IOException {
        CsvReader reader = new CsvReader(Files.newBufferedReader(file.toPath(), charset), null, null);
        readFile(file, charset, reader );
    }

    /**
     * Reads the CSV file with the specified separator and quote character
     *
     * @param file the file to read
     * @param separator the separator
     * @param quote the quote
     * @throws IOException when the file could not be read
     */
    public void readFile(final File file, final Character separator, final Character quote) throws IOException {
        Charset charset = findCharsetByBOM(file);
        BufferedReader buffered = Files.newBufferedReader(file.toPath(), charset);
        CsvReader reader = new CsvReader( buffered, separator, quote );
        readFile(file, charset, reader);
    }

    /**
     * Reads the CSV file with the specified charset and separator
     *
     * @param file the file to read
     * @param charset the charset
     * @param separator the separator
     * @throws IOException when the file could not be read
     */
    public void readFile(final File file, final Charset charset, final Character separator) throws IOException {
        BufferedReader buffered = Files.newBufferedReader(file.toPath(), charset);
        CsvReader reader = new CsvReader( buffered, separator, null );
        readFile(file, charset, reader);
    }

    /**
     * Reads the CSV file with the specified separator
     *
     * @param file the file to read
     * @param separator the separator
     * @throws IOException when the file could not be read
     */
    public void readFile(final File file, final Character separator) throws IOException {
        BufferedReader buffered = Files.newBufferedReader(file.toPath());
        CsvReader reader = new CsvReader( buffered, separator, CSV.DOUBLE_QUOTE );
        readFile(file, findCharsetByBOM(file), reader);
    }

    public void readJava(List<Class> list){
        JavaReader<Class> reader = new JavaReader<>(this, list);
        rows.clear();
        metaData = reader.getMetaData();
        while (reader.hasNext()){
            Row row = reader.next();
            addRow(row);
        };
    }



    /**
     * Writes the contents to a file using the specified Writer.
     *
     * @param writer the Writer
     * @throws Exception
     */
    private void write(final Writeable writer) throws Exception {
        writer.writeCSV(this);
    }

    /**
     * Writes the contents to a file.
     *
     * @param file the file
     * @throws Exception when the file could not be written
     */
    public void writeFile(final File file) throws Exception {
        write(new CsvWriter(new FileOutputStream(file)));
    }

    /**
     * Writes the contents to a file in XML format
     *
     * @param file the file
     * @throws Exception when the file could not be written
     */
    public void writeXML(final File file) throws Exception {
        write(new XmlWriter(new FileOutputStream(file)));
    }

    /**
     * Writes the contents to a file in JSON format
     *
     * @param file the file
     * @throws Exception when the file could not be written
     */
    public void writeJSON(final File file) throws Exception {
        write(new JsonWriter(new FileOutputStream(file)));
    }

    /**
     * Writes the contents to a file in HTML format
     *
     * @param file the file
     * @throws Exception when the file could not be written
     */
    public void writeHtml(final File file) throws Exception {
        write(new HtmlWriter(new FileOutputStream(file)));
    }

    /**
     * Returns the query
     *
     * @return the query
     */
    public Query getQuery() {
        return query;
    }

    /**
     * Filters the row based on typed queries.
     *
     * @return the query being created
     */
    public Query findByQuery() {
        this.query = new Query(metaData, this);
        return this.query;
    }

    /**
     * Builds a FrequencyDistribution table for the specified column index
     *
     * @param columnIndex the column index
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution buildFrequencyDistribution(int columnIndex) {
        Column c = metaData.getColumn(columnIndex);
        return buildFrequencyDistribution(c);
    }

    /**
     * Builds a FrequencyDistribution table for the specified column
     *
     * @param column the column
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution buildFrequencyDistribution(Column column) {
        FrequencyDistribution cv = new FrequencyDistribution(column);
        for (Row r : rows) {
            cv.addValue(r.getAsString(column));
        }
        return cv;
    }

    /**
     * Builds a set of unique values for the specified column.
     *
     * @param column the column
     * @return a set of distinct values
     */
    public Set<String> buildDistinctValues(Column column) {
        Set<String> values = new TreeSet<>();
        for (Row r : rows) {
            values.add(r.getAsString(column));
        }
        return values;
    }

    /**
     * Adds a ChangeListener
     *
     * @param changeListener the ChangeListener to add
     */
    public void addChangeListener(ChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    /**
     * Removes a ChangeListener
     *
     * @param changeListener the ChangeListener to remove
     */
    public void removeChangeListener(ChangeListener changeListener) {
        changeListeners.remove(changeListener);
    }


    private void fireMetaDataRead(){
        for (ChangeListener l : changeListeners){
            l.metaDataRead(this.getMetaData());
        }
    }

    private void fireRowCreated(int rowIndex, Row row){
        for (ChangeListener l : changeListeners){
            l.rowCreated(rowIndex,row);
        }
    }

    private void fireRowRemoved(int rowIndex, Row row){
        for (ChangeListener l : changeListeners){
            l.rowRemoved(rowIndex,row);
        }
    }

    private void fireRowMoved(int fromRowIndex, int toRowIndex){
        for (ChangeListener l : changeListeners){
            l.rowMoved(fromRowIndex, toRowIndex);
        }
    }

    private void fireRowsRemoved(int fromRowIndex, int toRowIndex){
        for (ChangeListener l : changeListeners){
            l.rowsRemoved(fromRowIndex, toRowIndex);
        }
    }

    void fireColumnCreated(Column column){
        for (ChangeListener l : changeListeners){
            l.columnCreated(column);
        }
    }

    public void fireColumnUpdated(Column column){
        for (ChangeListener l : changeListeners){
            l.columnUpdated(column);
        }
    }

    void fireColumnRemoved(Column column){
        for (ChangeListener l : changeListeners){
            l.columnRemoved(column.indexOf());
        }
    }

    void fireColumnMoved(final int fromIndex, final int toIndex) {
        for (ChangeListener l : changeListeners){
            l.columnMoved(fromIndex, toIndex);
        }
    }

    /**
     * Adds a FileListener
     *
     * @param fileListener the ChangeListener to remove
     */
    public void addFileListener(FileListener fileListener) {
        fileListeners.add(fileListener);
    }

    /**
     * Removes a FileListener
     *
     * @param fileListener the FileListener to remove
     */
    public void removeFileListener(FileListener fileListener) {
        fileListeners.remove(fileListener);
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

    private void fireBytesRead(long read, long max){
        for (FileListener l : fileListeners){
            l.readBytes(read, max);
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
