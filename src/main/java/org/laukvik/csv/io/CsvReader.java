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

/**
 *
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CsvReader implements AutoCloseable, Readable {

    private final BufferedInputStream is;
    //private Charset charset;
    private char currentChar;
    private StringBuilder currentValue;
    private StringBuilder rawLine;
    private int lineCounter;
    private MetaData metaData;
    private Row row;
    private List<String> values;

    public CsvReader(InputStream is) throws IOException {
        this(is, Charset.defaultCharset());
    }

    public CsvReader(InputStream is, Charset charset) throws IOException {
        this.is = new BufferedInputStream(is);
        this.lineCounter = 0;
        this.metaData = null;
        this.values = new ArrayList<>();
        parseRow();
        this.metaData.setCharset(charset);
    }

//    public Charset getCharset() {
//        return charset;
//    }
    public String getUnprocessedRow() {
        return rawLine.toString();
    }

    private boolean readRow() throws IOException {
        row = null;
        values = new ArrayList<>();
        if (is.available() == 0) {
            return false;
        }
        parseRow();
        if (values.isEmpty()) {
            return false;
        }
        row = new Row(values);
        row.setMetaData(metaData);
        return true;
    }

    public Row getRow() {
        return row;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    /**
     *
     * @return @throws IOException
     */
    private void parseRow() throws IOException {
        boolean isNextLine = false;

        /* Current value */
        currentValue = new StringBuilder();

        /* the current line */
        row = new Row();
        if (lineCounter > 0) {
            row.setMetaData(metaData);
        }

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

        if (lineCounter == 0) {
            if (metaData == null) {
                /* Metadata not provided. Use strings only */
                this.metaData = new MetaData();
                for (String s : values) {
                    metaData.addColumn(s);
                }
            } else {
                /* User specified columns */
                for (int x = 0; x < values.size(); x++) {
                    String s = values.get(x);
                    metaData.getColumn(x).setName(s);
                }
            }
        }
        lineCounter++;
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
