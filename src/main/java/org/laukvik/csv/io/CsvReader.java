package org.laukvik.csv.io;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * <h2>Reads a data set in the CSV format.</h2>
 * <h3>Reading a file with auto detect on:</h3>
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
 * <h3>Reading a file with specifying separator character and encoding:</h3>
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
 */
public final class CsvReader implements DatasetFileReader {

    /**
     * Automatically detect charset through BOM.
     */
    private boolean autoDetectCharset;
    /**
     * The bufferedReader.
     */
    private InputStreamReader reader;
    /**
     * The quote character to use.
     */
    private Character quoteChar;
    /**
     * Whether column separator should be automatically detected or not.
     */
    private boolean autoDetectColumnSeparator;
    /**
     * The number of lines read.
     */
    private int lineCounter;
    /**
     * The column separator character.
     */
    private Character columnSeparatorChar;

    /**
     * Reads CSV from the specified reader using the separator and quote characters.
     *
     * @param charset   the charset
     * @param separator the separator character
     * @param quote     the quote character
     */
    public CsvReader(final Charset charset, final Character separator, final Character quote) {
        this.autoDetectCharset = charset == null;
        this.autoDetectColumnSeparator = separator == null;
        if (separator != null) {
            this.columnSeparatorChar = separator;
        }
        if (quote == null) {
            this.quoteChar = CSV.QUOTE_DOUBLE;
        } else {
            this.quoteChar = quote;
        }
    }

    /**
     * Reads the file.
     *
     * @param file the file
     * @param csv  the csv
     * @throws CsvReaderException when the file could not be read
     */
    public void readFile(final File file, final CSV csv) throws CsvReaderException {
        this.lineCounter = 0;
        try {
            if (csv.isAutoDetectCharset()) {
                BOM bom = BOM.findBom(file);
                if (bom == null) {
                    reader = new InputStreamReader(new FileInputStream(file));
                } else {
                    reader = new InputStreamReader(new FileInputStream(file), bom.getCharset());
                    reader.skip(bom.getBytes().length);
                    csv.setCharset(bom.getCharset());
                }
            } else {
                reader = new InputStreamReader(new FileInputStream(file), csv.getCharset());
            }
            csv.setSeparator(columnSeparatorChar);
            csv.setQuoteChar(this.quoteChar);
            List<String> columns = parseRow(csv);
            for (String rawColumnName : columns) {
                csv.addColumn(Column.parseName(rawColumnName));
            }
            while (reader.ready()) {
                readRow(csv);
            }
        } catch (IOException e) {
            throw new CsvReaderException("Failed to read file " + file.getAbsolutePath(), e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new CsvReaderException("Failed to close file " + file.getAbsolutePath(), e);
            }
        }
    }

    /**
     * Reads the next row.
     *
     * @param csv the csv
     * @throws IOException when the row could not be read
     */
    private void readRow(final CSV csv) throws IOException {
        Row row = csv.addRow();
        List<String> values = parseRow(csv);
        for (int x = 0; x < values.size(); x++) {
            String value = values.get(x);
            Column c = csv.getColumn(x);
            row.set(c, value);
        }
    }

    /**
     * Parses one row of CSV data.
     *
     * @param csv the csv
     * @return a list of strings
     * @throws IOException when the row could not be read
     */
    private List<String> parseRow(final CSV csv) throws IOException {
        List<String> values = new ArrayList<>();

        boolean isNextLine = false;

        /* Current value */
        StringBuilder currentValue = new StringBuilder();

        boolean isWithinQuote = false;
        int quoteCount = 0;

        /* Read until */
        while (reader.ready() && !isNextLine) {

            // Read next char
            int intChar = reader.read();


            char currentChar = (char) intChar;

            boolean foundBom = false;

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
                    csv.setSeparator(columnSeparatorChar);
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

}
