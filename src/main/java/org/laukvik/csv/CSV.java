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
import org.laukvik.csv.io.Readable;
import org.laukvik.csv.io.ResourceBundleReader;
import org.laukvik.csv.io.ResourceBundleWriter;
import org.laukvik.csv.io.WordCountReader;
import org.laukvik.csv.io.Writeable;
import org.laukvik.csv.io.XmlWriter;
import org.laukvik.csv.query.Query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
 * specification from http://tools.ietf.org/rfc/rfc4180.txt
 * <h2>Reading files</h2>
 * <h3>Auto detection</h3>
 * <p>The easiest way to read a CSV file is to call the default constructor.
 * This method will try to auto detect separator character and encoding.</p>
 * <pre>
 * CSV csv = new CSV(new File("presidents.csv"));
 * </pre>
 * <h3>Manually specify separator character</h3>
 * <p>To read files using a SEMI COLON as separator character</p>
 * <pre>
 *     CSV csv = new CSV();
 *     csv.readFile( new File("presidents.csv"), CSV.SEMICOLON );
 * </pre>
 * <p>To read files using a semi colon as PIPE character</p>
 * <pre>
 * CSV csv = new CSV();
 * csv.readFile( new File("presidents.csv"), CSV.PIPE );
 * </pre>
 * <p>To read files using a semi colon as TAB character</p>
 * <pre>
 * CSV csv = new CSV();
 * csv.readFile( new File("presidents.csv"), CSV.TAB );
 * </pre>
 * <h3>Reading values from Java objects</h3>
 * <pre>
 * class Employee{
 *   String name;
 *   int age;
 *   boolean isWoman;
 *
 *   public Employee(final String name, final int age, final boolean isWoman) {
 *     this.name = name;
 *     this.age = age;
 *     this.isWoman = isWoman;
 *   }
 * }
 * List&lt;Employee&gt; employees = new ArrayList&lt;&gt;();
 *
 * CSV csv = new CSV();
 * csv.readJava(employees);
 * </pre>
 * <h3>Working with queries</h3>
 * <pre>
 * CSV csv = new CSV( new File("presidents.csv") );
 * List&lt;Row&gt; rows = csv.findByQuery().where().column("Party").is("Whig").getResultList();
 * </pre>
 */
public final class CSV implements Serializable {

    /**
     * The character representing LF.
     */
    public static final char LINEFEED = 10;

    /**
     * The character representing RETURN.
     */
    public static final char RETURN = 13;

    /**
     * The character representing COMMA.
     */
    public static final char COMMA = 44;

    /**
     * The character representing SEMICOLON.
     */
    public static final char SEMICOLON = 59;

    /**
     * The character representing PIPE.
     */
    public static final char PIPE = 124;

    /**
     * The character representing TAB.
     */
    public static final char TAB = 9;

    /**
     * The character representing DOUBLE QUOTE.
     */
    public static final char DOUBLE_QUOTE = 34;

    /**
     * The character representing SINGLE QUOTE.
     */
    public static final char SINGLE_QUOTE = 39;

    /**
     * The List of all ChangeListeners.
     */
    private final List<ChangeListener> changeListeners;
    /**
     * The List of all FileListeners.
     */
    private final List<FileListener> fileListeners;
    /**
     * The MetaData used.
     */
    private MetaData metaData;
    /**
     * The list of Rows.
     */
    private List<Row> rows;
    /**
     * The Query to use.
     */
    private Query query;
    /**
     * The file opened.
     */
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
     * Opens the specified CSV file and parses it.
     *
     * @param csvFile the file to open
     * @throws IOException when the file could not be read
     */
    public CSV(final File csvFile) throws IOException {
        this();
        readFile(csvFile);
    }

    /**
     * Returns an array of the supported separator characters.
     *
     * @return separator characters
     */
    public static char[] listSupportedSeparatorChars() {
        return new char[]{COMMA, SEMICOLON, PIPE, TAB};
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
        if (bom == null) {
            return Charset.defaultCharset();
        } else {
            return bom.getCharset();
        }
    }

    /**
     * Returns the opened file.
     *
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * Moves all rows down and inserts new columns.
     */
    public void insertHeaders() {
        Row r = addRow(0);
        for (int x = 0; x < getMetaData().getColumnCount(); x++) {
            StringColumn c = (StringColumn) getMetaData().getColumn(x);
            r.update(c, c.getName());
            c.setName("Column" + (x + 1));
        }
        fireRowCreated(0, r);
    }

    /**
     * Returns the rows.
     *
     * @return the rows
     */
    protected List<Row> getRows() {
        return rows;
    }

    /**
     * Returns the MetaData.
     *
     * @return the MetaData
     */
    public MetaData getMetaData() {
        return metaData;
    }

    /**
     * Sets the MetaData.
     *
     * @param metaData the MetaData to set
     */
    public void setMetaData(final MetaData metaData) {
        this.metaData = metaData;
    }

    /**
     * Returns the amount of rows.
     *
     * @return the amount of rows
     */
    public int getRowCount() {
        return rows.size();
    }

    /**
     * Returns the row with the specified index.
     *
     * @param rowIndex the index
     * @return the row
     */
    public Row getRow(final int rowIndex) {
        if (rowIndex > getRowCount()) {
            throw new RowNotFoundException(rowIndex);
        }
        return rows.get(rowIndex);
    }

    /**
     * Returns the index of the Row.
     *
     * @param row the row
     * @return the index
     */
    public int indexOf(final Row row) {
        return rows.indexOf(row);
    }

    /**
     * Creates a new empty row.
     *
     * @return the newly created row
     */
    public Row addRow() {
        Row r = new Row();
        addRow(r);
        return r;
    }

    /**
     * Creates a new empty row at the specified index.
     *
     * @param rowIndex the index to insert at
     * @return the newly created row
     */
    public Row addRow(final int rowIndex) {
        Row r = new Row();
        r.setCSV(this);
        rows.add(rowIndex, r);
        fireRowCreated(rowIndex, r);
        return r;
    }

    /**
     * Adds a new row.
     *
     * @param row the row to add
     * @return the row being added
     */
    private Row addRow(final Row row) {
        row.setCSV(this);
        rows.add(row);
        fireRowCreated(rows.size(), row);
        return row;
    }

    /**
     * Removes the row at the specified index.
     *
     * @param index the index to remove
     */
    public void removeRow(final int index) {
        Row row = rows.remove(index);
        fireRowRemoved(index, row);
    }

    /**
     * Removes all rows.
     */
    public void removeRows() {
        int count = rows.size();
        rows.clear();
        fireRowsRemoved(0, count);
    }

    /**
     * Removes all rows and columns.
     */
    public void clear() {
        metaData = new MetaData();
        rows.clear();
    }


    /**
     * Adds the column.
     *
     * @param column the column
     * @return the newly added column
     */
    public Column addColumn(final Column column) {
        metaData.addColumn(column);
        fireColumnCreated(column);
        removeRows();
        return column;
    }

    /**
     * Moves a row to another index.
     *
     * @param fromRowIndex the row to move
     * @param toRowIndex   the destination index
     */
    public void moveRow(final int fromRowIndex, final int toRowIndex) {
        Collections.swap(rows, fromRowIndex, toRowIndex);
        fireRowMoved(fromRowIndex, toRowIndex);
    }

    /**
     * Swaps two rows.
     *
     * @param fromRowIndex the first index
     * @param toRowIndex   the second index
     */
    public void swapRows(final int fromRowIndex, final int toRowIndex) {
        Collections.swap(rows, fromRowIndex, toRowIndex);
        fireRowMoved(fromRowIndex, toRowIndex);
    }

    /**
     * Adds a new text column with the specified name.
     *
     * @param name the name of the column
     * @return the newly create column
     */
    public Column addColumn(final String name) {
        return addStringColumn(name);
    }

    /**
     * Adds a new StringColumn with the specified name.
     *
     * @param name the name
     * @return the StringColumn being created
     */
    public StringColumn addStringColumn(final String name) {
        return addStringColumn(new StringColumn(name));
    }

    /**
     * Adds a new StringColumn with the specified name.
     *
     * @param column the StringColumn
     * @return the StringColumn being created
     */
    private StringColumn addStringColumn(final StringColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Adds a new IntegerColumn.
     *
     * @param column the IntegerColumn
     * @return the IntegerColumn being created
     */
    public IntegerColumn addIntegerColumn(final IntegerColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Adds a new FloatColumn.
     *
     * @param column the FloatColumn
     * @return the FloatColumn being created
     */
    public FloatColumn addFloatColumn(final FloatColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Adds a new BooleanColumn.
     *
     * @param column the BooleanColumn
     * @return the BooleanColumn being created
     */
    public BooleanColumn addBooleanColumn(final BooleanColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Adds a new ByteColumn.
     *
     * @param column the ByteColumn
     * @return the ByteColumn being created
     */
    public ByteColumn addByteColumn(final ByteColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Removes the column and all its data.
     *
     * @param column the column
     */
    public void removeColumn(final Column column) {
        for (Row r : rows) {
            r.remove(column);
        }
        getMetaData().removeColumn(column);
    }

    /**
     * Removes all rows within the specified range.
     *
     * @param fromRowIndex the start index
     * @param endRowIndex  the end index
     */
    public void removeRowsBetween(final int fromRowIndex, final int endRowIndex) {
        rows.subList(fromRowIndex, endRowIndex + 1).clear();
        fireRowsRemoved(fromRowIndex, endRowIndex);
    }

    /**
     * Reads the File using the specified reader. System default charset is used when the charset is not
     * specified.
     *
     * @param csvFile    the file to read
     * @param charset the charset used to read
     * @param reader  the reader to use
     */
    private void readFile(final File csvFile, final Charset charset, final ClosableReader reader) {
        this.file = csvFile;
        this.query = null;
        this.rows = new ArrayList<>();
        fireBeginRead();
        this.metaData = reader.getMetaData();
        this.metaData.setCSV(this);

        if (charset == null) {
            this.metaData.setCharset(findCharsetByBOM(file));
        } else {
            this.metaData.setCharset(charset);
        }
        fireMetaDataRead();
        long max = file.length();
        while (reader.hasNext()) {
            fireBytesRead(reader.getBytesRead(), max);
            addRow(reader.getRow());
        }
        fireFinishRead();
    }

    /**
     * Reads the CSV file.
     *
     * @param csvFile the file to read
     * @throws IOException when the file could not be read
     */
    public void readFile(final File csvFile) throws IOException {
        this.file = csvFile;
        Charset charset = findCharsetByBOM(file);
        CsvReader reader = new CsvReader(Files.newBufferedReader(file.toPath(), charset), null, CSV.DOUBLE_QUOTE);
        readFile(file, charset, reader);
    }

    /**
     * Reads the CSV file with the specified charset.
     *
     * @param csvFile    the file to read
     * @param charset the charset
     * @throws IOException when the file could not be read
     */
    public void readFile(final File csvFile, final Charset charset) throws IOException {
        this.file = csvFile;
        CsvReader reader = new CsvReader(Files.newBufferedReader(file.toPath(), charset), null, null);
        readFile(file, charset, reader);
    }

    /**
     * Reads the CSV file with the specified separator and quote character.
     *
     * @param csvFile      the file to read
     * @param separator the separator
     * @param quote     the quote
     * @throws IOException when the file could not be read
     */
    public void readFile(final File csvFile, final Character separator, final Character quote) throws IOException {
        this.file = csvFile;
        Charset charset = findCharsetByBOM(file);
        BufferedReader buffered = Files.newBufferedReader(file.toPath(), charset);
        CsvReader reader = new CsvReader(buffered, separator, quote);
        readFile(file, charset, reader);
    }

    /**
     * Reads the CSV file with the specified charset and separator.
     *
     * @param csvFile      the file to read
     * @param charset   the charset
     * @param separator the separator
     * @throws IOException when the file could not be read
     */
    public void readFile(final File csvFile, final Charset charset, final Character separator) throws IOException {
        this.file = csvFile;
        BufferedReader buffered = Files.newBufferedReader(file.toPath(), charset);
        CsvReader reader = new CsvReader(buffered, separator, null);
        readFile(file, charset, reader);
    }

    /**
     * Reads the CSV file with the specified separator.
     *
     * @param csvFile      the file to read
     * @param separator the separator
     * @throws IOException when the file could not be read
     */
    public void readFile(final File csvFile, final Character separator) throws IOException {
        this.file = csvFile;
        BufferedReader buffered = Files.newBufferedReader(file.toPath());
        CsvReader reader = new CsvReader(buffered, separator, CSV.DOUBLE_QUOTE);
        readFile(file, findCharsetByBOM(file), reader);
    }

    /**
     * Imports Java beans property values from the list.
     *
     * @param list the list of Java beans
     */
    public void readJava(final List<Class> list) {
        rows.clear();
        metaData = new MetaData();
        if (!list.isEmpty()) {
            JavaReader<Class> reader = new JavaReader<Class>(this, list);
            metaData = reader.getMetaData();
            while (reader.hasNext()) {
                Row row = reader.next();
                addRow(row);
            }
        }
    }

    /**
     * Reads a text file and builds a Frequency Distribution table data
     * of how many times each word has been used.
     *
     * @param textFile the file
     * @throws FileNotFoundException when the file could not be found
     */
    public void readWordCountFile(final File textFile) throws FileNotFoundException {
        readFile(new WordCountReader(), textFile);
    }

    /**
     * Reads a ResourceBundle file.
     *
     * @param resourceBundleFile the file
     * @throws FileNotFoundException when the file could not be found
     */
    public void readResourceBundle(final File resourceBundleFile) throws FileNotFoundException {
        readFile(new ResourceBundleReader(), resourceBundleFile);
    }

    /**
     * Reads a data set using the specified Readable.
     *
     * @param readable the readable
     * @param csvFile     the file to read
     * @throws FileNotFoundException when the file can't be found
     */
    private void readFile(final Readable readable, final File csvFile) throws FileNotFoundException {
        this.file = csvFile;
        fireBeginRead();
        rows.clear();
        readable.readFile(file);
        setMetaData(readable.getMetaData());
        fireMetaDataRead();
        while (readable.hasNext()) {
            Row row = readable.next();
            addRow(row);
        }
        fireFinishRead();
    }

    /**
     * Writes the contents to a file using the specified Writer.
     *
     * @param writer the Writer
     * @throws Exception when the file could not be written
     */
    private void write(final Writeable writer) throws Exception {
        fireBeginWrite();
        writer.writeCSV(this);
        fireFinishWrite();
    }

    /**
     * Writes the contents to a file.
     *
     * @param csvFile the file
     * @throws Exception when the file could not be written
     */
    public void writeFile(final File csvFile) throws Exception {
        fireBeginWrite();
        write(new CsvWriter(new FileOutputStream(csvFile)));
        fireFinishWrite();
    }

    /**
     * Writes the contents to a file in XML format.
     *
     * @param xmlFile the file
     * @throws Exception when the file could not be written
     */
    public void writeXML(final File xmlFile) throws Exception {
        fireBeginWrite();
        write(new XmlWriter(new FileOutputStream(xmlFile)));
        fireFinishWrite();
    }

    /**
     * Writes the contents to a file in JSON format.
     *
     * @param jsonFile the file
     * @throws Exception when the file could not be written
     */
    public void writeJSON(final File jsonFile) throws Exception {
        fireBeginWrite();
        write(new JsonWriter(new FileOutputStream(jsonFile)));
        fireFinishWrite();
    }

    /**
     * Writes the contents to a file in HTML format.
     *
     * @param htmlFile the file
     * @throws Exception when the file could not be written
     */
    public void writeHtml(final File htmlFile) throws Exception {
        fireBeginWrite();
        write(new HtmlWriter(new FileOutputStream(htmlFile)));
        fireFinishWrite();
    }

    /**
     * Writes the contents to multiple files.
     *
     * @param resourceBundleFile the file
     * @throws Exception when the file could not be written
     */
    public void writeResourceBundle(final File resourceBundleFile) throws Exception {
        fireBeginWrite();
        write(new ResourceBundleWriter(resourceBundleFile));
        fireFinishWrite();
    }

    /**
     * Returns the query.
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
        this.query = new Query(this);
        return this.query;
    }

    /**
     * Returns true if there is already a query.
     *
     * @return true when a query has been started
     */
    public boolean hasQuery() {
        return this.query != null;
    }

    /**
     * Removes any query.
     */
    public void clearQuery() {
        this.query = null;
    }

    /**
     * Builds a FrequencyDistribution table for the specified column index.
     *
     * @param columnIndex the column index
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution buildFrequencyDistribution(final int columnIndex) {
        Column c = metaData.getColumn(columnIndex);
        return buildFrequencyDistribution(c);
    }

    /**
     * Builds a FrequencyDistribution table for the specified column.
     *
     * @param column the column
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution buildFrequencyDistribution(final Column column) {
        FrequencyDistribution cv = new FrequencyDistribution(column);
        for (Row r : rows) {
            cv.addValue(r.getAsString(column));
        }
        return cv;
    }

    /**
     * Builds a set of unique values for the specified columnIndex.
     *
     * @param columnIndex the column index
     * @return a set of distinct values
     */
    public Set<String> buildDistinctValues(final int columnIndex) {
        Column c = getMetaData().getColumn(columnIndex);
        return buildDistinctValues(c);
    }


    /**
     * Builds a set of unique values for the specified column.
     *
     * @param column the column
     * @return a set of distinct values
     */
    public Set<String> buildDistinctValues(final Column column) {
        Set<String> values = new TreeSet<>();
        for (Row r : rows) {
            values.add(r.getAsString(column));
        }
        return values;
    }

    /**
     * Adds the ChangeListener.
     *
     * @param changeListener the ChangeListener to add
     */
    public void addChangeListener(final ChangeListener changeListener) {
        changeListeners.add(changeListener);
    }

    /**
     * Removes the ChangeListener.
     *
     * @param changeListener the ChangeListener to remove
     */
    public void removeChangeListener(final ChangeListener changeListener) {
        changeListeners.remove(changeListener);
    }

    /**
     * Informs all ChangeListeners that the MetaData has been read.
     */
    private void fireMetaDataRead() {
        for (ChangeListener l : changeListeners) {
            l.metaDataRead(this.getMetaData());
        }
    }

    /**
     * Informs all ChangeListeners that the Row has been read.
     *
     * @param rowIndex the index of the row
     * @param row      the row itself
     */
    private void fireRowCreated(final int rowIndex, final Row row) {
        for (ChangeListener l : changeListeners) {
            l.rowCreated(rowIndex, row);
        }
    }

    /**
     * Informs all ChangeListeners that the Row has been removed.
     *
     * @param rowIndex the index of the row
     * @param row      the row itself
     */
    private void fireRowRemoved(final int rowIndex, final Row row) {
        for (ChangeListener l : changeListeners) {
            l.rowRemoved(rowIndex, row);
        }
    }

    /**
     * Informs all ChangeListeners that the Row has been moved.
     *
     * @param fromRowIndex the index of the row
     * @param toRowIndex   the row itself
     */
    private void fireRowMoved(final int fromRowIndex, final int toRowIndex) {
        for (ChangeListener l : changeListeners) {
            l.rowMoved(fromRowIndex, toRowIndex);
        }
    }

    /**
     * Informs all ChangeListeners that the rows has been removed.
     *
     * @param fromRowIndex from index
     * @param toRowIndex   to index
     */
    private void fireRowsRemoved(final int fromRowIndex, final int toRowIndex) {
        for (ChangeListener l : changeListeners) {
            l.rowsRemoved(fromRowIndex, toRowIndex);
        }
    }

    /**
     * Informs all ChangeListeners that the Column has been created.
     *
     * @param column the Column
     */
    void fireColumnCreated(final Column column) {
        for (ChangeListener l : changeListeners) {
            l.columnCreated(column);
        }
    }

    /**
     * Informs all ChangeListeners that the Column has been updated.
     *
     * @param column the Column
     */
    public void fireColumnUpdated(final Column column) {
        for (ChangeListener l : changeListeners) {
            l.columnUpdated(column);
        }
    }

    /**
     * Informs all ChangeListeners that the Column has been removed.
     *
     * @param column the Column
     */
    void fireColumnRemoved(final Column column) {
        for (ChangeListener l : changeListeners) {
            l.columnRemoved(column.indexOf());
        }
    }

    /**
     * Informs all ChangesListeners that the Column has been moved to a new
     * index.
     *
     * @param fromIndex from index
     * @param toIndex   to index
     */
    void fireColumnMoved(final int fromIndex, final int toIndex) {
        for (ChangeListener l : changeListeners) {
            l.columnMoved(fromIndex, toIndex);
        }
    }

    /**
     * Adds a FileListener.
     *
     * @param fileListener the ChangeListener to remove
     */
    public void addFileListener(final FileListener fileListener) {
        fileListeners.add(fileListener);
    }

    /**
     * Removes a FileListener.
     *
     * @param fileListener the FileListener to remove
     */
    public void removeFileListener(final FileListener fileListener) {
        fileListeners.remove(fileListener);
    }

    /**
     * Informs all FileListeners that the reading process has been started.
     *
     */
    private void fireBeginRead() {
        for (FileListener l : fileListeners) {
            l.beginRead(file);
        }
    }

    /**
     * Informs all FileListeners that the reading process has been finished.
     *
     */
    private void fireFinishRead() {
        for (FileListener l : fileListeners) {
            l.finishRead(file);
        }
    }

    /**
     * Informs all FileListeners how many bytes have been read.
     *
     * @param read how many bytes have been read
     * @param max  the maximum number of bytes available
     */
    private void fireBytesRead(final long read, final long max) {
        for (FileListener l : fileListeners) {
            l.readBytes(read, max);
        }
    }

    /**
     * Informs all FileListeners that writing process has been started.
     *
     */
    private void fireBeginWrite() {
        for (FileListener l : fileListeners) {
            l.beginRead(file);
        }
    }

    /**
     * Informs all FileListeners that writing process has been finished.
     *
     */
    private void fireFinishWrite() {
        for (FileListener l : fileListeners) {
            l.finishRead(file);
        }
    }


}
