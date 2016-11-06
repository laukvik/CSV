package org.laukvik.csv.io;

import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a data set in the CSV format.
 * <p>
 * <h3>Reading a file with auto detect on:</h3>
 * <p>
 * <pre>
 * try (CsvReader r = new CsvReader( new File("presidents.csv"), Charset.forName(charset)) )) {
 *   while (r.hasNext()) {
 *     Row row = r.next();
 *   }
 *   }
 *   catch (IOException e) {
 *     e.printStacktrace();
 * }
 * </pre>
 * <p>
 * <h3>Reading a file with specifying separator character and encoding:</h3>
 * <p>
 * <pre>
 * try (CsvReader r = new CsvReader( new File("presidents.csv"), Charset.forName(charset)) )) {
 *   while (r.hasNext()) {
 *     Row row = r.next();
 *   }
 *   }
 *   catch (IOException e) {
 *     e.printStacktrace();
 * }
 * </pre>
 *
 * TODO - Remove File from constructor. Should only be from readFile();
 */
public final class CsvReader implements ClosableReader {

    /**
     * The bufferedReader.
     */
    private final BufferedReader reader;
    /** The MetaData. */
    private final MetaData metaData;
    /** The quote character to use. */
    private final Character quoteChar;
    /** Whether column separator should be automatically detected or not. */
    private boolean autoDetectColumnSeparator;
    /** The number of bytes read. */
    private int bytesRead;
    /** The number of lines read. */
    private int lineCounter;
    /** The current row. */
    private Row row;
    /** The column separator character. */
    private Character columnSeparatorChar;

    /**
     * Reads CSV from the specified reader using the separator and quote characters.
     *
     * @param bufferedReader    the bufferedReader
     * @param separator the separator character
     * @param quote     the quote character
     * @throws IOException when the CSV could not be fully read
     */
    public CsvReader(final BufferedReader bufferedReader,
                     final Character separator,
                     final Character quote) throws IOException {
        this.autoDetectColumnSeparator = (separator == null);
        if (separator != null) {
            this.columnSeparatorChar = separator;
        }
        this.reader = bufferedReader;
        if (quote == null) {
            this.quoteChar = CSV.DOUBLE_QUOTE;
        } else {
            this.quoteChar = quote;
        }

        this.metaData = new MetaData();
        this.lineCounter = 0;
        this.bytesRead = 0;
        List<String> columns = parseRow();
        for (String rawColumnName : columns) {
            this.metaData.addColumn(Column.parseName(rawColumnName));
        }
        this.metaData.setSeparator(columnSeparatorChar);
        this.metaData.setQuoteChar(this.quoteChar);
    }

    /**
     * Specifies the separator and quote character to use.
     *
     * @param file the file to read
     * @param separator the separator char
     * @param quote the quote char
     * @throws IOException when the file could not be read
     * TODO - Remove file from constructor
     */
    public CsvReader(final File file, final Character separator, final Character quote) throws IOException {
        this(new BufferedReader(new FileReader(file)), separator, quote);
    }

    /**
     * Reads the file.
     * @param file the file
     * @throws IOException when the file could not be read
     * TODO - Remove file from constructor
     */
    public CsvReader(final File file) throws IOException {
        this(new BufferedReader(new FileReader(file)));
    }

    /**
     * Reads CSV from the specified reader using default settings.
     *
     * @param bufferedReader the reader
     * @throws IOException when the CSV could not be fully read
     */
    public CsvReader(final BufferedReader bufferedReader) throws IOException {
        this(bufferedReader, null, null);
    }

    /**
     * Parses one row of CSV data.
     *
     * @return a list of strings
     * @throws IOException when the row could not be read
     */
    private List<String> parseRow() throws IOException {
        List<String> values = new ArrayList<>();

        boolean isNextLine = false;

        /* Current value */
        StringBuilder currentValue = new StringBuilder();

        /* The raw chars being read */
        final StringBuilder rawLine = new StringBuilder();

        boolean isWithinQuote = false;
        int quoteCount = 0;

        /* Read until */
        while (reader.ready() && !isNextLine) {

            // Read next char
            int intChar = reader.read();

            char currentChar = (char) intChar;

            boolean foundBom = false;
            // Increase number of bytes read
            bytesRead++;

            // Determines whether or not to add char
            boolean addChar = false;

            // Adds the currentValue
            boolean addValue = false;

            // Look for separator characters in first line
            if (lineCounter == 0 && autoDetectColumnSeparator) {
                if (currentChar == CSV.TAB
                        || currentChar == CSV.SEMICOLON
                        || currentChar == CSV.PIPE
                        || currentChar == CSV.COMMA) {
                    columnSeparatorChar = currentChar;
                    autoDetectColumnSeparator = false;
                    metaData.setSeparator(columnSeparatorChar);
                }
            }

            // Check char
            if (currentChar == CSV.RETURN) {
                addChar = false;

            } else if (currentChar == CSV.LINEFEED) {

                addChar = false;
                addValue = true;
                isNextLine = true;
                if (isWithinQuote) {
                    currentValue.deleteCharAt(currentValue.length() - 1);
                    isWithinQuote = false;
                }

            } else if (currentChar == this.quoteChar) {
                addChar = true;

                isWithinQuote = true;
                while (reader.ready()) {
                    currentChar = (char) reader.read();
                    rawLine.append(currentChar);
                    if (currentChar == this.quoteChar) {
                        quoteCount++;
                        break;
                    } else {
                        currentValue.append(currentChar);
                    }
                }

                quoteCount--;

            } else if (columnSeparatorChar != null && currentChar == columnSeparatorChar) {
                addChar = false;
                addValue = true;

                if (isWithinQuote) {
                    currentValue.deleteCharAt(currentValue.length() - 1);
                    isWithinQuote = false;
                }

            } else {
                addChar = true;
            }

            if (addChar) {
                currentValue.append(currentChar);
            }
            if (!isNextLine) {
                rawLine.append(currentChar);
            }

            if (!reader.ready()) {
                addValue = true;
            }

            if (addValue) {
                if (!reader.ready()) {
                    if (isWithinQuote) {
                        currentValue.deleteCharAt(currentValue.length() - 1);
                        isWithinQuote = false;
                    }
                }
                values.add(currentValue.toString());
                currentValue = new StringBuilder();
            }
        }
        lineCounter++;
        return values;
    }

    /**
     * Reads the next row.
     *
     * @return a boolean whether a new row was found
     * @throws IOException when the row could not be read
     */
    private boolean readRow() throws IOException {
        if (!reader.ready()) {
            return false;
        }
        row = new Row();
        List<String> values = parseRow();
        if (values.isEmpty()) {
            return false;
        }

        for (int x = 0; x < values.size(); x++) {
            String value = values.get(x);
            if (x < metaData.getColumnCount()) {
                Column c = metaData.getColumn(x);
                row.set(c, value);
            }
        }
        return true;
    }

    /**
     * Returns the column separator character.
     * @return the column separator character.
     */
    public char getColumnSeparatorChar() {
        return columnSeparatorChar;
    }

    /**
     * Returns the amount of bytes read.
     * @return the amount of bytes read
     */
    public int getBytesRead() {
        return bytesRead;
    }

    /**
     * Returns how many lines read.
     * @return the number of lines read
     */
    public int getLineCounter() {
        return lineCounter;
    }

    /**
     * Returns the current row.
     * @return the row
     */
    public Row getRow() {
        return row;
    }

    /**
     * Reads the file.
     *
     * @param file the file
     */
    public void readFile(final File file) {
    }

    /**
     * Returns the MetaData found.
     * @return the MetaData
     */
    public MetaData getMetaData() {
        return metaData;
    }

    /**
     * Closes the bufferedReader.
     * @throws IOException when an IOException occurs
     */
    public void close() throws IOException {
        reader.close();
    }

    /**
     * Returns true if more rows are available.
     * @return true if more rows
     */
    public boolean hasNext() {
        try {
            return readRow();
        } catch (IOException ex) {
            return false;
        }
    }

    /**
     * Returns the next row.
     *
     * @return the next row
     */
    public Row next() {
        return row;
    }

}
