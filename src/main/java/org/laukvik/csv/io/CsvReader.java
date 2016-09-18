
package org.laukvik.csv.io;

import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author Morten Laukvik
 */
public class CsvReader implements AbstractReader {

    private static final Logger LOG = Logger.getLogger(CsvReader.class.getName());

    private StringBuilder currentValue;
    private StringBuilder rawLine;
    private int bytesRead;
    private int lineCounter;

    private MetaData metaData;
    private Row row;
    private Character columnSeparatorChar;
    private final Character quoteChar;
    private boolean autoDetectSeperator;
    private boolean autoDetectCharset;
    private boolean autoDetectColumnSeparator;
    private boolean autoDetectQuoteChar;

    private File file;
    private BufferedReader reader;
    private Charset charset;

    public CsvReader(final File file, final Charset charset, final Character separator, final Character quote) throws IOException {
        this.autoDetectSeperator = (separator == null);
        this.autoDetectCharset = (charset == null);
        this.autoDetectColumnSeparator = (separator == null);
        this.autoDetectQuoteChar = (quote == null);
        this.file = file;
        if (separator != null) {
            this.columnSeparatorChar = separator;
        }
        this.quoteChar = quote == null ? CSV.DOUBLE_QUOTE : quote;
        if (this.autoDetectCharset) {
            // Try to find BOM signature
            BOM bom = BOM.findBom(file);
            if (bom == null) {
//                LOG.info("BOM signature not found.");
//                this.charset = Charset.forName("utf-8");
//                this.charset = Charset.forName("iso-8859-1");
            } else {
//                LOG.log(Level.INFO, "Found BOM signature {0}", bom);
                this.charset = bom.getCharset();
            }
        } else {
            this.charset = charset;
        }

        this.metaData = new MetaData();
        this.metaData.setCharset(this.charset);
        this.lineCounter = 0;
        this.bytesRead = 0;
        if (this.charset == null) {
//            this.charset = Charset.forName("utf-8");
            reader = Files.newBufferedReader(file.toPath());
        } else {
            reader = Files.newBufferedReader(file.toPath(), this.charset);
        }
//        reader = Files.newBufferedReader(file.toPath(), this.charset);
        List<String> columns = parseRow();
        for (String rawColumnName : columns) {
            this.metaData.addColumn(Column.parseName(rawColumnName));
        }
        this.metaData.setSeparator(columnSeparatorChar);
        this.metaData.setQuoteChar(this.quoteChar);
    }

    /**
     * Reads the CSV file and detects encoding and seperator characters
     * automatically
     *
     * @param file
     * @throws IOException
     */
    public CsvReader(final File file) throws IOException {
        this( file, null, null, CSV.DOUBLE_QUOTE);
    }

    /**
     * Reads the CSV file using the specified charset and automatically detects
     * seperator characters
     *
     * @param file
     * @param charset
     * @throws IOException
     */
    public CsvReader(final File file, final Charset charset) throws IOException {
        this( file, charset, CSV.COMMA, CSV.DOUBLE_QUOTE );
    }

    public CsvReader(final File file, final Charset charset, final Character columnSeparatorChar) throws IOException {
        this( file, charset, columnSeparatorChar, CSV.DOUBLE_QUOTE );
    }

    /**
     * Parses one row of data
     *
     * @return
     * @throws IOException
     */
    private List<String> parseRow() throws IOException {
        List<String> values = new ArrayList<>();

        boolean isNextLine = false;

        /* Current value */
        currentValue = new StringBuilder();

        /* The raw chars being read */
        rawLine = new StringBuilder();

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

            // Look for seperator characters in first line
            if (lineCounter == 0 && autoDetectSeperator) {
                if (currentChar == CSV.TAB || currentChar == CSV.SEMICOLON || currentChar == CSV.PIPE || currentChar == CSV.COMMA) {
                    columnSeparatorChar = currentChar;
                    autoDetectSeperator = false;
                    metaData.setSeparator(columnSeparatorChar);
                    LOG.log(Level.FINE, "Detected seperator: {0}", columnSeparatorChar);
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
     * Reads the next row
     *
     * @return a boolean whether a new row was found
     * @throws IOException
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
            if (x >= metaData.getColumnCount()) {
            } else {
                Column c = metaData.getColumn(x);


                if (c instanceof StringColumn){
                    row.update(  (StringColumn)c, value );

                } else if (c instanceof BigDecimalColumn){
                    BigDecimalColumn bc = (BigDecimalColumn)c;
//                    row.update( bc, bc.parse(value) );

                } else if (c instanceof BooleanColumn){
                    BooleanColumn bc = (BooleanColumn)c;
                    row.update(  (BooleanColumn)c, bc.parse(value) );

                } else if (c instanceof ByteColumn){
                    ByteColumn bc = (ByteColumn)c;
//                    row.update( bc, bc.parse(value) );

                } else if (c instanceof DateColumn){
                    DateColumn dc = (DateColumn)c;
                    row.update( dc, dc.parse(value) );

                } else if (c instanceof DoubleColumn){
                    DoubleColumn dc = (DoubleColumn)c;
                    row.update( dc, dc.parse(value) );

                } else if (c instanceof FloatColumn){
                    FloatColumn fc = (FloatColumn)c;
                    row.update( fc, fc.parse(value) );

                } else if (c instanceof IntegerColumn){
                    IntegerColumn ic = (IntegerColumn)c;
                    row.update(  (IntegerColumn)c, ic.parse(value) );

                } else if (c instanceof UrlColumn){
                    UrlColumn ic = (UrlColumn)c;
                    row.update( ic, ic.parse(value) );


                }


            }
        }
        return true;
    }

    public char getColumnSeparatorChar() {
        return columnSeparatorChar;
    }

    public int getBytesRead() {
        return bytesRead;
    }

    public int getLineCounter() {
        return lineCounter;
    }

    @Override
    public Row getRow() {
        return row;
    }

    @Override
    public MetaData getMetaData() {
        return metaData;
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public boolean hasNext() {
        try {
            return readRow();
        }
        catch (IOException ex) {
            return false;
        }
    }

    @Override
    public Row next() {
        return row;
    }

}
