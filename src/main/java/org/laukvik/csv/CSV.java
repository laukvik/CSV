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
import org.laukvik.csv.io.BOM;
import org.laukvik.csv.io.CsvReader;
import org.laukvik.csv.io.CsvWriter;
import org.laukvik.csv.io.HtmlWriter;
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
    private List<ChangeListener> changeListeners;
    private List<FileListener> fileListeners;
    private File file;

    public CSV() {
        metaData = new MetaData();
        rows = new ArrayList<>();
        query = null;
        changeListeners = new ArrayList<>();
        fileListeners = new ArrayList<>();
    }

    public static char[] getSupportedSeparatorChars() {
        return new char []{COMMA,SEMICOLON,PIPE,TAB};
    }

    public static List<Charset> getSupportedCharsets(){
        List<Charset> sets = new ArrayList<>();
        for (BOM b : BOM.values()){
            sets.add(b.getCharset());
        }
        return sets;
    }

    public List<ChangeListener> getChangeListeners() {
        return changeListeners;
    }

    public void setChangeListeners(final List<ChangeListener> changeListeners) {
        this.changeListeners = changeListeners;
    }

    public List<FileListener> getFileListeners() {
        return fileListeners;
    }

    public void setFileListeners(final List<FileListener> fileListeners) {
        this.fileListeners = fileListeners;
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

    public void moveRow(final int fromRowIndex, final int toRowIndex) {
        Collections.swap(rows, fromRowIndex, toRowIndex);
        fireRowMoved(fromRowIndex, toRowIndex);
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
     * Reads the File using the specified reader. System default charset is used when the charset is not
     * specified.
     *
     * @param file
     * @param charset
     * @param reader
     */
    public void readFile(final File file, final Charset charset, final AbstractReader reader) {
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

    protected static Charset findCharsetByBOM(final File file){
            BOM bom = BOM.findBom(file);
        return bom == null ? Charset.defaultCharset() : bom.getCharset();
    }

    /**
     * Reads the CSV file
     *
     * @param file
     * @throws IOException
     */
    public void readFile(final File file) throws IOException {
        Charset charset = findCharsetByBOM(file);
        CsvReader reader = new CsvReader(  Files.newBufferedReader(file.toPath(), charset), null, CSV.DOUBLE_QUOTE );
        readFile(file, charset, reader );
    }

    public void readFile(final File file, final Charset charset) throws IOException {
        CsvReader reader = new CsvReader(Files.newBufferedReader(file.toPath(), charset), null, null);
        readFile(file, charset, reader );
    }

    public void readFile(final File file, final Character separator, final Character quote) throws IOException {
        Charset charset = findCharsetByBOM(file);
        BufferedReader buffered = Files.newBufferedReader(file.toPath(), charset);
        CsvReader reader = new CsvReader( buffered, separator, quote );
        readFile(file, charset, reader);
    }

    public void readFile(final File file, final Charset charset, final Character separator) throws IOException {
        BufferedReader buffered = Files.newBufferedReader(file.toPath(), charset);
        CsvReader reader = new CsvReader( buffered, separator, null );
        readFile(file, charset, reader);
    }

    public void readFile(final File file, final Character separator) throws IOException {
        BufferedReader buffered = Files.newBufferedReader(file.toPath());
        CsvReader reader = new CsvReader( buffered, separator, CSV.DOUBLE_QUOTE );
        readFile(file, findCharsetByBOM(file), reader);
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
        writer.writeFile(this);
        writer.close();
    }

    public void writeFile(final File file) throws Exception {
        write(new CsvWriter(new FileOutputStream(file)));
    }

    public void writeXML(final File file) throws Exception {
        write(new XmlWriter(new FileOutputStream(file)));
    }

    public void writeJSON(final File file) throws Exception {
        write(new JsonWriter(new FileOutputStream(file)));
    }

    public void writeHtml(final File file) throws Exception {
        write(new HtmlWriter(new FileOutputStream(file)));
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
        changeListeners.add(l);
    }

    public void removeChangeListener(ChangeListener l) {
        changeListeners.remove(l);
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

    private void fireRowUpdated(int rowIndex, Row row){
        for (ChangeListener l : changeListeners){
            l.rowUpdated(rowIndex,row);
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

    protected void fireColumnCreated(Column column){
        for (ChangeListener l : changeListeners){
            l.columnCreated(column);
        }
    }

    public void fireColumnUpdated(Column column){
        for (ChangeListener l : changeListeners){
            l.columnUpdated(column);
        }
    }

    protected void fireColumnRemoved(Column column){
        for (ChangeListener l : changeListeners){
            l.columnRemoved(column.indexOf());
        }
    }

    protected void fireColumnMoved(final int fromIndex, final int toIndex) {
        for (ChangeListener l : changeListeners){
            l.columnMoved(fromIndex, toIndex);
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
