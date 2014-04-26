/*
 * Copyright 2013 Laukviks Bedrifter.
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * LF: Multics, Unix and Unix-like systems (GNU/Linux, AIX, Xenix, Mac OS X,
 * FreeBSD, etc.), BeOS, Amiga, RISC OS, and others CR+LF: DEC RT-11 and most
 * other early non-Unix, non-IBM OSes, CP/M, MP/M, DOS, OS/2, Microsoft Windows,
 * Symbian OS CR: Commodore machines, Apple II family, Mac OS up to version 9
 * and OS-9
 *
 * @author Morten Laukvik
 *
 */
public class CSVReader {

    public final static char LF = 10;
    public final static char CR = 13;
    public final static char COMMA = ',';
    public final static char SEMINCOLON = ';';
    public final static char PIPE = '|';
    public final static char TAB = '\t';
    public final static char QUOTE = '"';
    public final static char SPACE = ' ';
    public final static String CRLF = CR + "" + LF;
    public char textQualifier;
    public char fieldDelimiter;
    byte[] lineSeperator;
    boolean hasHeaders;
    int rowIndex;
    List<String> columns;
    List<CSVListener> listeners;

    public CSVReader(char fieldDelimiter) {
        this.listeners = new ArrayList<CSVListener>();
        this.lineSeperator = System.getProperty("line.separator").getBytes();
        this.textQualifier = QUOTE;
        this.fieldDelimiter = fieldDelimiter;
        this.hasHeaders = true;
    }

    public CSVReader() {
        this(COMMA);
    }

    public void setHasHeaders(boolean hasHeaders) {
        this.hasHeaders = hasHeaders;
    }

    public void setFieldDelimiter(char fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
    }

    public void addListener(CSVListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CSVListener listener) {
        listeners.remove(listener);
    }

    public void fireHeadersFound(String[] values) {
        for (CSVListener l : listeners) {
            l.foundHeaders(values);
        }
    }

    public void fireValueFound(String value, int columnIndex, int rowIndex) {
    }

    public void fireRowFound(int row, String[] values) {
        if (hasHeaders) {
            if (row == 0) {
                fireHeadersFound(values);
            } else {
                for (CSVListener l : listeners) {
                    l.foundRow(row - 1, values);
                }
            }
        } else {
            for (CSVListener l : listeners) {
                l.foundRow(row, values);
            }
        }
    }

    private void parseUntilComma(char first, BufferedInputStream is) throws IOException {
        int c;
        StringBuilder buffer = new StringBuilder();
        buffer.append((char) first);
        boolean isFound = false;
        while (is.available() > 0 && !isFound) {
            c = is.read();
            if (c == fieldDelimiter || c == LF) {
                isFound = true;
                columns.add(buffer.toString());
                fireValueFound(buffer.toString(), columns.size(), rowIndex);
                if (c == LF) {
                    rowIndex++;
                    String[] values = new String[columns.size()];
                    columns.toArray(values);
                    fireRowFound(rowIndex, values);
                    columns = new ArrayList<String>();
                }
            } else {
                buffer.append((char) c);
            }
        }
    }

    private void parseUntilQuote(BufferedInputStream is) throws IOException {
        int c;
        StringBuilder buffer = new StringBuilder();
        boolean isFound = false;
        while (is.available() > 0 && !isFound) {
            c = is.read();
            if (c == textQualifier) {
                is.mark(1);
                int cNext = is.read();
                if (cNext == textQualifier) {
                    buffer.append((char) cNext);
                } else {
                    is.reset();
                    isFound = true;
                    columns.add(buffer.toString());
                    fireValueFound(buffer.toString(), columns.size(), rowIndex);
                }

            } else {
                buffer.append((char) c);
            }
        }
    }

    /**
     * Reads and parses CSV data from an input stream
     *
     * @param is
     * @throws IOException
     */
    public void read(InputStream is) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(is);
        columns = new ArrayList<String>();
        int c;
        rowIndex = -1;
        while (bis.available() > 0) {
            c = bis.read();
            if (c == textQualifier) {
                parseUntilQuote(bis);
            } else if (c == LF) {

                rowIndex++;
                String[] arr = new String[columns.size()];
                columns.toArray(arr);

                fireRowFound(rowIndex, arr);
                columns = new ArrayList<String>();

            } else if (c == fieldDelimiter) {
                /* Empty column */
                columns.add("");
                fireValueFound("", columns.size(), rowIndex);

            } else {
                parseUntilComma((char) c, bis);
            }
        }

        if (columns.size() > 0) {
            rowIndex++;
            String[] arr = new String[columns.size()];
            columns.toArray(arr);
            fireRowFound(rowIndex, arr);
        }
    }

    public void write(File file, String[][] values) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        for (String[] row : values) {
            write(fos, row);
        }
        fos.flush();
        fos.close();
    }

    /**
     *
     *
     * @param out
     * @param values
     * @throws IOException
     */
    public void write(OutputStream out, String[] values) throws IOException {
        for (int x = 0; x < values.length; x++) {
            String v = values[ x];
            if (x > 0) {
                out.write(fieldDelimiter);
            }
            out.write(textQualifier);
            for (int n = 0; n < v.length(); n++) {
                if (v.charAt(n) == textQualifier) {
                    out.write(textQualifier);
                }
                out.write(v.charAt(n));
            }
            out.write(textQualifier);
        }
        out.write(lineSeperator);
    }
}
