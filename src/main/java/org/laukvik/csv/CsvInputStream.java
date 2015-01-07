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

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CsvInputStream implements AutoCloseable {

    private final InputStream is;
    private final char quote = '"';
    private final char comma = ',';
    private final char lineFeed = '\n';
    private final char carriageReturn = '\r';


    private char currentChar;
    private char nextChar;
    private StringBuilder currentWord;
    private List<String> columns;
    boolean useBufferedChar;

    public CsvInputStream(InputStream is) {
        this.is = is;
    }

    public boolean hasMoreLines() throws IOException {
        return is.available() > 0;
    }

    /**
     *
     * @throws IOException
     */
    private void readChar() throws IOException {
        if (useBufferedChar) {
            currentChar = nextChar;
            nextChar = (char) is.read();
        } else {
            currentChar = (char) is.read();
            if (is.available() > 0) {
                nextChar = (char) is.read();
            } else {
                nextChar = 0;
            }
        }
        useBufferedChar = true;
    }


    /**
     * Read
     *
     * CR LF LF
     *
     * @return
     * @throws IOException
     */
    public List<String> readLine() throws IOException {
        boolean isInsideQuotes = false;
        boolean isNextLine = false;

        useBufferedChar = true;
        /*  */
        currentWord = new StringBuilder();

        columns = new ArrayList<>();

        /* Read until */
        while (is.available() > 0 && !isNextLine) {
            /* Read next char */
            readChar();

            /* Check char */
            switch (currentChar) {
                case carriageReturn: /* Found carriage return. Do nothing. */

                    break;

                case lineFeed: /* Found new line symbol */

                    if (isInsideQuotes) {
                        if (nextChar == carriageReturn) {
                            useBufferedChar = false;
                        }
                        appendChar();
                    } else {
                        if (currentWord.length() > 0) {
                            closeWord();
                        }
                        isNextLine = true;
                    }
                    break;

                case quote:

                    if (isInsideQuotes) {
                        if (nextChar == quote) {
                            /* Found escaped quote */
                            appendChar();
                            useBufferedChar = false;
                        } else {
                            /* Found end quote */
                            isInsideQuotes = false;
                            appendChar();
                            closeWord();
                            currentWord = new StringBuilder();
                        }
                    } else {
                        /* Found start quote */
                        isInsideQuotes = true;
                    }
                    break;

                case comma:
                    if (isInsideQuotes) {
                        /* Found separator inside quotes */
                        appendChar();
                    } else {
                        /* Found separator. Add current currentCell */
                        if (currentWord.length() > 0) {
                            closeWord();
                        }
                        currentWord = new StringBuilder();
                    }
                    break;

                default:
                    /* Everything else... */
                    appendChar();
                    break;
            }
        }
        return columns;
    }

    private void appendChar() {
        currentWord.append(currentChar);
    }

    private void closeWord() {
        columns.add(currentWord.toString());
    }

    @Override
    public void close() throws IOException {
        is.close();
    }


}
