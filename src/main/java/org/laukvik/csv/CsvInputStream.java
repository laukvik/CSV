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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CsvInputStream implements AutoCloseable {

    private final BufferedInputStream is;
    private Charset charset;
    private char currentChar;
    private StringBuilder currentValue;
    private List<String> columns;

    private StringBuilder rawLine;
    private int lineCounter;

    public CsvInputStream(InputStream is) {
        this(is, Charset.forName("utf-8"));
    }

    public CsvInputStream(InputStream is, Charset charset) {
        this.is = new BufferedInputStream(is);
        this.charset = charset;
        lineCounter = 1;
    }

    public boolean hasMoreLines() throws IOException {
        return is.available() > 0;
    }

    public String getRaw() {
        return rawLine.toString();
    }

    /**
     * Read

 CR LINEFEED LINEFEED
     *
     * @return
     * @throws IOException
     */
    public List<String> readLine() throws IOException {
//        System.out.println("--- " + lineCounter + " -----------------------------------------");

        boolean isNextLine = false;

        /* Current value */
        currentValue = new StringBuilder();
        /* the current line */
        columns = new ArrayList<>();
        /* The raw chars being read */
        rawLine = new StringBuilder();

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
                    break;

                case CSV.QUOTE:

                    addChar = true;

                    while (is.available() > 0) {
                        currentChar = (char) is.read();
                        rawLine.append(currentChar);
                        if (currentChar == CSV.QUOTE) {
                            break;
                        } else {
                            currentValue.append(currentChar);
                        }
                    }
//                    currentValue.append(CSV.QUOTE);
                    break;

                case CSV.COMMA:

                    addChar = false;
                    addValue = true;
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
                columns.add(currentValue.toString());
//                System.out.println(currentValue);
                currentValue = new StringBuilder();

            }
        }

//        System.out.println("* " + lineCounter + ":" + rawLine);
//        System.out.println("(RAW:" + rawLine + ")");
        lineCounter++;
        return columns;
    }

    @Override
    public void close() throws IOException {
        is.close();
    }

}
