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
package no.laukvik.csv;

import no.laukvik.csv.columns.*;
import no.laukvik.csv.io.*;
import no.laukvik.csv.query.Query;
import no.laukvik.csv.query.ValueMatcher;
import no.laukvik.csv.statistics.FrequencyDistribution;

import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The main class for building, reading, writing and querying the CSV datasets.
 * <h3>Reading a CSV file</h3>
 * <pre>{@code
 *     CSV csv = new CSV( new File("presidents.csv") );
 *     StringColumn president = csv.getColumn("president");
 *     IntegerColumn presidency = csv.getColumn(5);
 *     List<Row> rows = csv.findRows();
 *     for (Row r : rows){
 *         System.out.println( r.getObject(president) );
 *         System.out.println( r.getObject(presidency) );
 *     }
 * }</pre>
 * <h3>Writing a CSV file</h3>
 * <pre>{@code
 *     CSV csv = new CSV();
 *     StringColumn first = csv.getColumn("first");
 *     StringColumn last = csv.getColumn("last");
 *     csv.writeFile( new File("addresses.csv") ); // Write to CSV format
 *     csv.writeHtml( new File("addresses.html") ); // Write to HTML format
 *     csv.writeJSON( new File("addresses.json") ); // Write to JSON format
 *     csv.writeXML( new File("addresses.xnk") ); // Write to XML format
 * }</pre>
 * <h3>Querying a CSV file</h3>
 * <p>The following example illustrates a simple query. To see more advanced examples please see the
 * documentation for the Query class.</p>
 * <pre>{@code
 *     CSV csv = new CSV( new File("presidents.csv") );
 *     StringColumn president = csv.getColumn("president");
 *     IntegerColumn presidency = csv.getColumn(5);
 *     Query query = new Query();
 *     query.isBetween(presidency, 1, 10);
 *     List<Row> rows = csv.findRowsByQuery( query );
 *     for (Row r : rows){
 *         System.out.println( r.getObject(president) );
 *         System.out.println( r.getObject(presidency) );
 *     }
 * }</pre>
 * <h3>Building a CSV file</h3>
 * <pre>{@code
 *     CSV csv = new CSV();
 *     StringColumn first = csv.getColumn("first");
 *     StringColumn last = csv.getColumn("last");
 *     csv.addRow().set( first, "John" ).set( last, "Doe" );
 * }</pre>
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
     * Indicates when the column could not be found.
     */
    public static final int COLUMN_NOT_FOUND = -1;

    /**
     * The list of columns.
     */
    private final List<Column> columns;
    /**
     * The list of Rows.
     */
    private final List<Row> rows;
    /**
     * The Character setRaw.
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
     * The file opened.
     */
    private File file;

    /**
     * Opens an empty file.
     */
    public CSV() {
        columns = new ArrayList<>();
        rows = new ArrayList<>();
        charset = Charset.defaultCharset();
        autoDetectCharset = true;
        autoDetectQuote = true;
        autoDetectSeparator = true;
    }

    /**
     * Opens the specified CSV file and parses it.
     *
     * @param csvFile the file to open
     * @throws CsvReaderException when the file could not be read
     */
    public CSV(final File csvFile) throws CsvReaderException {
        this();
        readFile(csvFile);
    }

    /**
     * Returns stream of rows.
     *
     * @return stream
     */
    public Stream<Row> stream() {
        return rows.stream();
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
     * @param autoDetectCharset use automatic when setRaw to true
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
     * @param autoDetectSeparator use automatic when setRaw to true
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
     * @param autoDetectQuote use automatic when setRaw to true
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
        int index = indexOf(name);
        if (index == COLUMN_NOT_FOUND) {
            return null;
        }
        return columns.get(index);
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
     * Returns the StringColumn with the specified name.
     *
     * @param name the name of the column
     * @return the StringColumn
     */
    public StringColumn getStringColumn(final String name) {
        Column c = getColumn(name);
        if (c instanceof StringColumn) {
            return (StringColumn) c;
        }
        return null;
    }

    /**
     * Returns the DateColumn with the specified name.
     *
     * @param name the name of the column
     * @return the DateColumn
     */
    public DateColumn getDateColumn(final String name) {
        Column c = getColumn(name);
        if (c instanceof DateColumn) {
            return (DateColumn) c;
        }
        return null;
    }

    /**
     * Returns the DoubleColumn with the specified name.
     *
     * @param name the name of the column
     * @return the DoubleColumn
     */
    public DoubleColumn getDoubleColumn(final String name) {
        Column c = getColumn(name);
        if (c instanceof DoubleColumn) {
            return (DoubleColumn) c;
        }
        return null;
    }

    /**
     * Returns the FloatColumn with the specified name.
     *
     * @param name the name of the column
     * @return the FloatColumn
     */
    public FloatColumn getFloatColumn(final String name) {
        Column c = getColumn(name);
        if (c instanceof FloatColumn) {
            return (FloatColumn) c;
        }
        return null;
    }

    /**
     * Returns the IntegerColumn with the specified name.
     *
     * @param name the name of the column
     * @return the IntegerColumn
     */
    public IntegerColumn getIntegerColumn(final String name) {
        Column c = getColumn(name);
        if (c instanceof IntegerColumn) {
            return (IntegerColumn) c;
        }
        return null;
    }

    /**
     * Returns the UrlColumn with the specified name.
     *
     * @param name the name of the column
     * @return the UrlColumn
     */
    public UrlColumn getUrlColumn(final String name) {
        Column c = getColumn(name);
        if (c instanceof UrlColumn) {
            return (UrlColumn) c;
        }
        return null;
    }

    /**
     * Returns the BigDecimalColumn with the specified name.
     *
     * @param name the name of the column
     * @return the BigDecimalColumn
     */
    public BigDecimalColumn getBigDecimalColumn(final String name) {
        Column c = getColumn(name);
        if (c instanceof BigDecimalColumn) {
            return (BigDecimalColumn) c;
        }
        return null;
    }

    /**
     * Returns the ByteColumn with the specified name.
     *
     * @param name the name of the column
     * @return the ByteColumn
     */
    public ByteColumn getByteColumn(final String name) {
        Column c = getColumn(name);
        if (c instanceof ByteColumn) {
            return (ByteColumn) c;
        }
        return null;
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
        if (getColumn(column.getName()) != null) {
            throw new ColumnAlreadyExistException(column);
        }
        columns.add(column);
        return column;
    }

    /**
     * Removes the column.
     *
     * @param column the column to setNull
     */
    public void removeColumn(final Column column) {
        column.setCSV(null);
        columns.remove(column);
        for (Row r : findRows()) {
            r.setNull(column);
        }
    }

    /**
     * Changes the order of a specified column to another.
     *
     * @param fromIndex the index of the column to move
     * @param toIndex   the new index of the column
     */
    public void swapColumn(final int fromIndex, final int toIndex) {
        Collections.swap(columns, fromIndex, toIndex);
    }

    /**
     * Changes the order of a specified column to another.
     *
     * @param fromIndex the index of the column to move
     * @param toIndex   the new index of the column
     */
    public void moveColumn(final int fromIndex, final int toIndex) {
        Column c1 = getColumn(fromIndex);
        columns.remove(c1);
        columns.add(toIndex, c1);
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
        return COLUMN_NOT_FOUND;
    }

    /**
     * Removes the column with the columnIndex.
     *
     * @param columnIndex the column index
     */
    public void removeColumn(final int columnIndex) {
        Column c = columns.get(columnIndex);
        columns.remove(c);
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
            r.set(c, c.getName());
            c.setName("Column" + (x + 1));
        }
    }

    /**
     * Returns the rows.
     *
     * @return the rows
     */
    public List<Row> findRows() {
        return rows;
    }

    /**
     * Returns the rows that matchesRow the query.
     *
     * @param query the query
     * @return the rows
     */
    public List<Row> findRowsByQuery(final Query query) {
        return query.getRows(this);
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
        return row;
    }

    /**
     * Removes the row at the specified index.
     *
     * @param index the index to setNull
     */
    public void removeRow(final int index) {
        rows.remove(index);
    }

    /**
     * Removes all rows within the specified range.
     *
     * @param fromRowIndex the start index
     * @param endRowIndex  the end index
     */
    public void removeRowsBetween(final int fromRowIndex, final int endRowIndex) {
        rows.subList(fromRowIndex, endRowIndex + 1).clear();
    }

    /**
     * Removes all rows.
     */
    public void removeRows() {
        rows.clear();
    }

    /**
     * Removes all rows and columns.
     */
    public void clear() {
        columns.clear();
        rows.clear();
    }

    /**
     * Moves a row to another index.
     *
     * @param fromRowIndex the row to move
     * @param toRowIndex   the destination index
     */
    public void moveRow(final int fromRowIndex, final int toRowIndex) {
        Collections.swap(rows, fromRowIndex, toRowIndex);
    }

    /**
     * Swaps two rows.
     *
     * @param fromRowIndex the first index
     * @param toRowIndex   the second index
     */
    public void swapRows(final int fromRowIndex, final int toRowIndex) {
        Collections.swap(rows, fromRowIndex, toRowIndex);
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
     * Adds a new DateColumn.
     *
     * @param columnName the name
     * @param format     the date pattern
     * @return the DateColumn being created
     */
    public DateColumn addDateColumn(final String columnName, final String format) {
        DateColumn c = new DateColumn(columnName, format);
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
     * @throws CsvReaderException when the file can't be read
     */
    private void readDatasetFile(final File csvFile, final DatasetFileReader reader) throws CsvReaderException {
        clear();
        this.file = csvFile;
        reader.readFile(csvFile, this);
    }

    /**
     * Reads the CSV file with the specified separator and quote character.
     *
     * @param csvFile the file to read
     * @throws CsvReaderException when the file could not be read
     */
    public void readFile(final File csvFile) throws CsvReaderException {
        CsvReader reader = new CsvReader(charset, separatorChar, quoteChar);
        readDatasetFile(csvFile, reader);
    }

    /**
     * Reads the csvFile and appends the rows after the last row.
     *
     * @param csvFile
     * @throws CsvReaderException
     */
    public void appendFile(final File csvFile) throws CsvReaderException {
        this.appendFile(csvFile, 1);
    }

    /**
     * Reads the csvFile from the specified rowIndex and appends the rows after the last row.
     *
     * @param csvFile
     * @param rowIndex
     * @throws CsvReaderException
     */
    public void appendFile(final File csvFile, final int rowIndex) throws CsvReaderException {
        CsvReader reader = new CsvReader(charset, separatorChar, quoteChar);
        reader.setAppendMode(true);
        reader.setSkipRows(rowIndex);
        reader.readFile(csvFile, this);
    }

    /**
     * Writes the contents to a file using the specified Writer.
     *
     * @param writer      the Writer
     * @param fileToWrite the file to write to
     * @throws CsvWriterException when the file could not be written
     */
    private void write(final DatasetFileWriter writer, final File fileToWrite) throws CsvWriterException {
        writer.writeCSV(fileToWrite, this);
    }

    /**
     * Writes the contents to a file.
     *
     * @param csvFile the file
     * @throws CsvWriterException when the file could not be written
     */
    public void writeFile(final File csvFile) throws CsvWriterException {
        write(new CsvWriter(), csvFile);
    }

    /**
     * Writes the contents to a file in XML format.
     *
     * @param xmlFile the file
     * @throws CsvWriterException when the file could not be written
     */
    public void writeXML(final File xmlFile) throws CsvWriterException {
        write(new XmlWriter(), xmlFile);
    }

    /**
     * Writes the contents to a file in JSON format.
     *
     * @param jsonFile the file
     * @throws CsvWriterException when the file could not be written
     */
    public void writeJSON(final File jsonFile) throws CsvWriterException {
        write(new JsonWriter(), jsonFile);
    }

    /**
     * Writes the contents to a file in HTML format.
     *
     * @param htmlFile the file
     * @throws CsvWriterException when the file could not be written
     */
    public void writeHtml(final File htmlFile) throws CsvWriterException {
        write(new HtmlWriter(), htmlFile);
    }

    /**
     * Builds a FrequencyDistribution table for the specified column.
     *
     * @param column the column
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution<String> buildFrequencyDistribution(final StringColumn column) {
        FrequencyDistribution<String> cv = new FrequencyDistribution<>(column);
        rows.stream().forEach(r -> cv.addValue(r.get(column)));
        return cv;
    }

    /**
     * Builds a FrequencyDistribution table for the specified column.
     *
     * @param column the column
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution<BigDecimal> buildFrequencyDistribution(final BigDecimalColumn column) {
        FrequencyDistribution<BigDecimal> cv = new FrequencyDistribution<>(column);
        rows.stream().forEach(r -> cv.addValue(r.get(column)));
        return cv;
    }

    /**
     * Builds a FrequencyDistribution table for the specified column.
     *
     * @param column the column
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution<Boolean> buildFrequencyDistribution(final BooleanColumn column) {
        FrequencyDistribution<Boolean> cv = new FrequencyDistribution<>(column);
        rows.stream().forEach(r -> cv.addValue(r.get(column)));
        return cv;
    }

    /**
     * Builds a FrequencyDistribution table for the specified column.
     *
     * @param column the column
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution<Date> buildFrequencyDistribution(final DateColumn column) {
        FrequencyDistribution<Date> cv = new FrequencyDistribution<>(column);
        rows.stream().forEach(r -> cv.addValue(r.get(column)));
        return cv;
    }

    /**
     * Builds a FrequencyDistribution table for the specified column.
     *
     * @param column the column
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution<Double> buildFrequencyDistribution(final DoubleColumn column) {
        FrequencyDistribution<Double> cv = new FrequencyDistribution<>(column);
        rows.stream().forEach(r -> cv.addValue(r.get(column)));
        return cv;
    }

    /**
     * Builds a FrequencyDistribution table for the specified column.
     *
     * @param column the column
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution<Float> buildFrequencyDistribution(final FloatColumn column) {
        FrequencyDistribution<Float> cv = new FrequencyDistribution<>(column);
        rows.stream().forEach(r -> cv.addValue(r.get(column)));
        return cv;
    }

    /**
     * Builds a FrequencyDistribution table for the specified column.
     *
     * @param column the column
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution<Integer> buildFrequencyDistribution(final IntegerColumn column) {
        FrequencyDistribution<Integer> cv = new FrequencyDistribution<>(column);
        rows.stream().forEach(r -> cv.addValue(r.get(column)));
        return cv;
    }

    /**
     * Builds a FrequencyDistribution table for the specified column.
     *
     * @param column the column
     * @return a FrequencyDistribution table
     */
    public FrequencyDistribution<URL> buildFrequencyDistribution(final UrlColumn column) {
        FrequencyDistribution<URL> cv = new FrequencyDistribution<>(column);
        rows.stream().forEach(r -> cv.addValue(r.get(column)));
        return cv;
    }

    /**
     * Builds a setRaw of distinct values.
     *
     * @param column the column
     * @return the distinct values
     */
    public Set<BigDecimal> buildDistinctValues(final BigDecimalColumn column) {
        return rows
                .stream().map(r -> r.get(column))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Builds a setRaw of distinct values.
     *
     * @param column the column
     * @return the distinct values
     */
    public Set<Boolean> buildDistinctValues(final BooleanColumn column) {
        return rows
                .stream().map(r -> r.get(column))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Builds a setRaw of distinct values.
     *
     * @param column the column
     * @return the distinct values
     */
    public Set<Date> buildDistinctValues(final DateColumn column) {
        return rows
                .stream().map(r -> r.get(column))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Builds a setRaw of distinct values.
     *
     * @param column the column
     * @return the distinct values
     */
    public Set<Double> buildDistinctValues(final DoubleColumn column) {
        return rows
                .stream().map(r -> r.get(column))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Builds a setRaw of distinct values.
     *
     * @param column the column
     * @return the distinct values
     */
    public Set<Float> buildDistinctValues(final FloatColumn column) {
        return rows
                .stream().map(r -> r.get(column))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Builds a setRaw of distinct values.
     *
     * @param column the column
     * @return the distinct values
     */
    public Set<Integer> buildDistinctValues(final IntegerColumn column) {
        return rows
                .stream().map(r -> r.get(column))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Builds a setRaw of distinct values.
     *
     * @param column the column
     * @return the distinct values
     */
    public Set<String> buildDistinctValues(final StringColumn column) {
        return rows
                .stream().map(r -> r.get(column))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Builds a setRaw of distinct values.
     *
     * @param column the column
     * @return the distinct values
     */
    public Set<URL> buildDistinctValues(final UrlColumn column) {
        return rows
                .stream().map(r -> r.get(column))
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Returns all rows that the matchers accepts.
     *
     * @param matchers the macthers
     * @return the matching rorws
     */
    public List<Row> findRowsByMatchers(final List<ValueMatcher> matchers) {
        Query q = new Query();
        for (ValueMatcher m : matchers) {
            q.addMatcher(m);
        }
        return findRowsByQuery(q);
    }
}
