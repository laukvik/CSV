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
import org.laukvik.csv.io.CsvReader;
import org.laukvik.csv.io.CsvWriter;
import org.laukvik.csv.io.DatasetFileReader;
import org.laukvik.csv.io.DatasetFileWriter;
import org.laukvik.csv.io.HtmlWriter;
import org.laukvik.csv.io.JsonWriter;
import org.laukvik.csv.io.ResourceBundleReader;
import org.laukvik.csv.io.ResourceBundleWriter;
import org.laukvik.csv.io.WordCountReader;
import org.laukvik.csv.io.XmlWriter;
import org.laukvik.csv.query.Query;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
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
    public static final char QUOTE_DOUBLE = 34;

    /**
     * The character representing SINGLE QUOTE.
     */
    public static final char QUOTE_SINGLE = 39;

    /**
     * The list of columns.
     */
    private final List<Column> columns;
    /**
     * The List of all ChangeListeners.
     */
    private final List<ChangeListener> changeListeners;
    /**
     * The List of all FileListeners.
     */
    private final List<FileListener> fileListeners;
    /**
     * The Character set.
     */
    private Charset charset;
    /**
     * The column separator character.
     */
    private Character separatorChar;
    /**
     * The quote character.
     */
    private Character quoteChar;
    /**
     * Automatically detects charset.
     */
    private boolean autoDetectCharset;
    /**
     * Automatically detects separator.
     */
    private boolean autoDetectSeparator;
    /**
     * Automatically detects quote.
     */
    private boolean autoDetectQuote;
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
        columns = new ArrayList<>();
        rows = new ArrayList<>();
        query = null;
        changeListeners = new ArrayList<>();
        fileListeners = new ArrayList<>();
        charset = Charset.defaultCharset();
        autoDetectCharset = true;
        autoDetectQuote = true;
        autoDetectSeparator = true;
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
     * Automatically detects charset using BOM (Byte Order Mark).
     *
     * @return true when automatic detection
     */
    public boolean isAutoDetectCharset() {
        return autoDetectCharset;
    }

    /**
     * Set automatic detection of charset using BOM.
     *
     * @param autoDetectCharset use automatic when set to true
     */
    public void setAutoDetectCharset(final boolean autoDetectCharset) {
        this.autoDetectCharset = autoDetectCharset;
    }

    /**
     * Automatically detects separator based.
     *
     * @return true when automatic detection
     */
    public boolean isAutoDetectSeparator() {
        return autoDetectSeparator;
    }

    /**
     * Sets automatic detection of separator.
     *
     * @param autoDetectSeparator use automatic when set to true
     */
    public void setAutoDetectSeparator(final boolean autoDetectSeparator) {
        this.autoDetectSeparator = autoDetectSeparator;
    }

    /**
     * Automatically detects quote character.
     *
     * @return true when automatic detection
     */
    public boolean isAutoDetectQuote() {
        return autoDetectQuote;
    }

    /**
     * Set automatic detection of quotes.
     *
     * @param autoDetectQuote use automatic when set to true
     */
    public void setAutoDetectQuote(final boolean autoDetectQuote) {
        this.autoDetectQuote = autoDetectQuote;
    }

    /**
     * Returns the column with the specified name.
     *
     * @param name the column name
     * @return the column
     */
    public Column getColumn(final String name) {
        return columns.get(indexOf(name));
    }

    /**
     * Returns the column with the specified name.
     *
     * @param columnIndex the column index
     * @return the column
     */
    public Column getColumn(final int columnIndex) {
        return columns.get(columnIndex);
    }

    /**
     * Returns the Charset being used.
     *
     * @return the Charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * Sets the Charset.
     *
     * @param charset the charset
     */
    public void setCharset(final Charset charset) {
        this.charset = charset;
    }

    /**
     * Returns the amount of columns.
     *
     * @return the amount of columns
     */
    public int getColumnCount() {
        return columns.size();
    }

    /**
     * Adds the column.
     *
     * @param column the column to add
     * @return the added column
     */
    public Column addColumn(final Column column) {
        column.setCSV(this);
        columns.add(column);
        fireColumnCreated(column);
        return column;
    }

    /**
     * Removes the column.
     *
     * @param column the column to remove
     */
    public void removeColumn(final Column column) {
        column.setCSV(null);
        columns.remove(column);
        for (Row r : getRows()) {
            r.remove(column);
        }
        fireColumnRemoved(column);
    }

    /**
     * Changes the order of a specified column to another.
     *
     * @param fromIndex the index of the column to move
     * @param toIndex   the new index of the column
     */
    public void swapColumn(final int fromIndex, final int toIndex) {
        Collections.swap(columns, fromIndex, toIndex);
        fireColumnMoved(fromIndex, toIndex);
    }

    /**
     * Changes the order of a specified column to another.
     *
     * @param fromIndex the index of the column to move
     * @param toIndex   the new index of the column
     */
    public void moveColumn(final int fromIndex, final int toIndex) {
        Column c1 = getColumn(fromIndex);
        Column c2 = getColumn(toIndex);
        columns.remove(c1);
        columns.add(toIndex, c1);
        fireColumnMoved(fromIndex, toIndex);
    }

    /**
     * Returns the index of the specified Column.
     *
     * @param column the column
     * @return the index
     */
    public int indexOf(final Column column) {
        return columns.indexOf(column);
    }

    /**
     * Returns the column index of the column with the specified column name.
     *
     * @param columnName the column
     * @return the index
     */
    public int indexOf(final String columnName) {
        int x = 0;
        for (Column c : columns) {
            if (c.getName().equalsIgnoreCase(columnName)) {
                return x;
            }
            x++;
        }
        return -1;
    }

    /**
     * Removes the column with the columnIndex.
     *
     * @param columnIndex the column index
     */
    public void removeColumn(final int columnIndex) {
        Column c = columns.get(columnIndex);
        columns.remove(c);
        fireColumnRemoved(c);
    }

    /**
     * Returns the separator character.
     *
     * @return the separator character
     */
    public Character getSeparatorChar() {
        return separatorChar;
    }

    /**
     * Sets the separator character.
     *
     * @param character the separator character
     */
    public void setSeparator(final Character character) {
        this.separatorChar = character;
    }

    /**
     * Returns the quote character.
     *
     * @return the quote character
     */
    public Character getQuoteChar() {
        return quoteChar;
    }

    /**
     * Sets the quote character.
     *
     * @param quoteChar the quote character
     */
    public void setQuoteChar(final char quoteChar) {
        this.quoteChar = quoteChar;
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
     * Insert column names when a CSV has been read without their column names.
     */
    public void insertColumns() {
        Row r = addRow(0);
        for (int x = 0; x < getColumnCount(); x++) {
            StringColumn c = (StringColumn) getColumn(x);
            r.setString(c, c.getName());
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
            return null;
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
        columns.clear();
        rows.clear();

        query = null;
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
     * @param columnName the name
     * @return the StringColumn being created
     */
    public StringColumn addStringColumn(final String columnName) {
        StringColumn c = new StringColumn(columnName);
        addColumn(c);
        return c;
    }

    /**
     * Adds a new IntegerColumn.
     *
     * @param columnName the name
     * @return the IntegerColumn being created
     */
    public IntegerColumn addIntegerColumn(final String columnName) {
        IntegerColumn c = new IntegerColumn(columnName);
        addColumn(c);
        return c;
    }

    /**
     * Adds a new FloatColumn.
     *
     * @param columnName the name
     * @return the FloatColumn being created
     */
    public FloatColumn addFloatColumn(final String columnName) {
        FloatColumn c = new FloatColumn(columnName);
        addColumn(c);
        return c;
    }

    /**
     * Adds a new BooleanColumn.
     *
     * @param columnName the name
     * @return the BooleanColumn being created
     */
    public BooleanColumn addBooleanColumn(final String columnName) {
        BooleanColumn c = new BooleanColumn(columnName);
        addColumn(c);
        return c;
    }

    /**
     * Adds a new ByteColumn.
     *
     * @param columnName the name
     * @return the ByteColumn being created
     */
    public ByteColumn addByteColumn(final String columnName) {
        ByteColumn c = new ByteColumn(columnName);
        addColumn(c);
        return c;
    }

    /**
     * Adds a new BigDecimalColumn.
     *
     * @param columnName the name
     * @return the BigDecimalColumn being created
     */
    public BigDecimalColumn addBigDecimalColumn(final String columnName) {
        BigDecimalColumn c = new BigDecimalColumn(columnName);
        addColumn(c);
        return c;
    }

    /**
     * Adds a new DateColumn.
     *
     * @param columnName the name
     * @return the DateColumn being created
     */
    public DateColumn addDateColumn(final String columnName) {
        DateColumn c = new DateColumn(columnName);
        addColumn(c);
        return c;
    }

    /**
     * Adds a new DoubleColumn.
     *
     * @param columnName the name
     * @return the DoubleColumn being created
     */
    public DoubleColumn addDoubleColumn(final String columnName) {
        DoubleColumn c = new DoubleColumn(columnName);
        addColumn(c);
        return c;
    }

    /**
     * Adds a new UrlColumn.
     *
     * @param columnName the name
     * @return the UrlColumn being created
     */
    public UrlColumn addUrlColumn(final String columnName) {
        UrlColumn c = new UrlColumn(columnName);
        addColumn(c);
        return c;
    }

    /**
     * Reads the File using the specified reader. System default charset is used when the charset is not
     * specified.
     *
     * @param csvFile the file to read
     * @param reader  the reader to use
     * @throws IOException when the file can't be read
     */
    private void readDatasetFile(final File csvFile, final DatasetFileReader reader) throws IOException {
        clear();
        this.file = csvFile;
        fireBeginRead();
        reader.readFile(csvFile, this);
        fireFinishRead();
    }

    /**
     * Reads the CSV file with the specified separator and quote character.
     *
     * @param csvFile the file to read
     * @throws IOException when the file could not be read
     */
    public void readFile(final File csvFile) throws IOException {
        CsvReader reader = new CsvReader(charset, separatorChar, quoteChar);
        readDatasetFile(csvFile, reader);
    }

    /**
     * Reads a text file and builds a Frequency Distribution table data
     * of how many times each word has been used.
     *
     * @param textFile the file
     * @throws IOException when the file could not be read
     */
    public void readWordCountFile(final File textFile) throws IOException {
        readDatasetFile(textFile, new WordCountReader());
    }

    /**
     * Reads a ResourceBundle file.
     *
     * @param resourceBundleFile the file
     * @throws IOException when the file could not be read
     */
    public void readResourceBundle(final File resourceBundleFile) throws IOException {
        readDatasetFile(resourceBundleFile, new ResourceBundleReader());
    }


    /**
     * Writes the contents to a file using the specified Writer.
     *
     * @param writer      the Writer
     * @param fileToWrite the file to write to
     * @throws IOException when the file could not be written
     */
    private void write(final DatasetFileWriter writer, final File fileToWrite) throws IOException {
        fireBeginWrite();
        writer.writeCSV(fileToWrite, this);
        fireFinishWrite();
    }

    /**
     * Writes the contents to a file.
     *
     * @param csvFile the file
     * @throws IOException when the file could not be written
     */
    public void writeFile(final File csvFile) throws IOException {
        write(new CsvWriter(), csvFile);
    }

    /**
     * Writes the contents to a file in XML format.
     *
     * @param xmlFile the file
     * @throws IOException when the file could not be written
     */
    public void writeXML(final File xmlFile) throws IOException {
        write(new XmlWriter(), xmlFile);
    }

    /**
     * Writes the contents to a file in JSON format.
     *
     * @param jsonFile the file
     * @throws IOException when the file could not be written
     */
    public void writeJSON(final File jsonFile) throws IOException {
        write(new JsonWriter(), jsonFile);
    }

    /**
     * Writes the contents to a file in HTML format.
     *
     * @param htmlFile the file
     * @throws IOException when the file could not be written
     */
    public void writeHtml(final File htmlFile) throws IOException {
        write(new HtmlWriter(), htmlFile);
    }

    /**
     * Writes the contents to multiple files.
     *
     * @param resourceBundleFile the file
     * @throws IOException when the file could not be written
     */
    public void writeResourceBundle(final File resourceBundleFile) throws IOException {
        write(new ResourceBundleWriter(), resourceBundleFile);
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
        Column c = getColumn(columnIndex);
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
        Column c = getColumn(columnIndex);
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
            l.columnRemoved(columns.indexOf(column));
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
     */
    private void fireBeginRead() {
        for (FileListener l : fileListeners) {
            l.beginRead(file);
        }
    }

    /**
     * Informs all FileListeners that the reading process has been finished.
     */
    private void fireFinishRead() {
        for (FileListener l : fileListeners) {
            l.finishRead(file);
        }
    }

    /**
     * Informs all FileListeners that writing process has been started.
     */
    private void fireBeginWrite() {
        for (FileListener l : fileListeners) {
            l.beginWrite(file);
        }
    }

    /**
     * Informs all FileListeners that writing process has been finished.
     */
    private void fireFinishWrite() {
        for (FileListener l : fileListeners) {
            l.finishWrite(file);
        }
    }

}
