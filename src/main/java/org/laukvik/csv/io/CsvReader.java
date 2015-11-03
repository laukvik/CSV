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
package org.laukvik.csv.io;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.BooleanColumn;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.columns.UrlColumn;

/**
 *
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CsvReader implements AutoCloseable, Readable {

    private final BufferedInputStream is;
    private char currentChar;
    private StringBuilder currentValue;
    private StringBuilder rawLine;
    private int lineCounter;
    private MetaData metaData;
    private Row row;

    public CsvReader(InputStream is) throws IOException {
        this(is, Charset.defaultCharset());
    }

    public CsvReader(InputStream is, Charset charset) throws IOException {
        this.is = new BufferedInputStream(is);
        this.lineCounter = 0;
        this.metaData = new MetaData();
        this.metaData.setCharset(charset);
        List<String> columns = parseRow();
        for (String c : columns) {
            this.metaData.addColumn(c);
        }
    }

    public int getLineCounter() {
        return lineCounter;
    }

    private boolean readRow() throws IOException {
        if (is.available() == 0) {
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
                if (c instanceof StringColumn) {
                    row.update((StringColumn) c, value);
                } else if (c instanceof IntegerColumn) {
                    IntegerColumn ic = (IntegerColumn) c;
                    row.update(ic, ic.parse(value));
                } else if (c instanceof UrlColumn) {
                    UrlColumn uc = (UrlColumn) c;
                    row.update(uc, uc.parse(value));
                } else if (c instanceof BooleanColumn) {
                    BooleanColumn bc = (BooleanColumn) c;
                    row.update(bc, bc.parse(value));
                } else if (c instanceof DoubleColumn) {
                    DoubleColumn dc = (DoubleColumn) c;
                    row.update(dc, dc.parse(value));
                } else if (c instanceof FloatColumn) {
                    FloatColumn fc = (FloatColumn) c;
                    row.update(fc, fc.parse(value));
                } else if (c instanceof DateColumn) {
                    DateColumn dc = (DateColumn) c;
                    row.update(dc, dc.parse(value));
                }
            }
        }
        return true;
    }

    /**
     *
     * @return @throws IOException
     */
    private List<String> parseRow() throws IOException {
        List<String> values = new ArrayList<>();
        boolean isNextLine = false;

        /* Current value */
        currentValue = new StringBuilder();

        /* the current line */
//        row = new Row();

        /* The raw chars being read */
        rawLine = new StringBuilder();

        boolean isWithinQuote = false;
        int quoteCount = 0;

        /* Read until */
        while (is.available() > 0 && !isNextLine) {

            /* Read next char */
            currentChar = (char) is.read();

            /* Determines whether or not to add char */
            boolean addChar;

            /* Adds the currentValue */
            boolean addValue = false;

            /* Check char */
            switch (currentChar) {
                case CSV.RETURN: /* Found carriage return. Do nothing. */

                    addChar = false;
                    break;

                case CSV.LINEFEED: /* Found new line symbol */

                    addChar = false;
                    addValue = true;
                    isNextLine = true;
                    if (isWithinQuote) {
                        currentValue.deleteCharAt(currentValue.length() - 1);
                        isWithinQuote = false;
                    }
                    break;

                case CSV.QUOTE:

                    /*    "Venture ""Extended Edition"""  */
                    addChar = true;

                    isWithinQuote = true;

                    int read = -1;
                    while (is.available() > 0) {
                        currentChar = (char) is.read();
                        rawLine.append(currentChar);
                        if (currentChar == CSV.QUOTE) {
                            quoteCount++;
                            break;
                        } else {
                            currentValue.append(currentChar);
                        }
                    }

                    quoteCount--;

                    break;

                case CSV.COMMA:

                    addChar = false;
                    addValue = true;

                    if (isWithinQuote) {
                        currentValue.deleteCharAt(currentValue.length() - 1);
                        isWithinQuote = false;
                    }

                    break;

                default:
                    /* Everything else... */
                    addChar = true;
                    break;
            }
            if (addChar) {
                currentValue.append(currentChar);
            }
            if (!isNextLine) {
                rawLine.append(currentChar);
            }

            if (addValue || is.available() == 0) {
                if (is.available() == 0) {
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

    public Row getRow() {
        return row;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    @Override
    public void close() throws IOException {
        is.close();
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
