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

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Writes the data set in the JSON format.
 *
 * @see <a href="https://en.wikipedia.org/wiki/JSON">JSON (wikipedia)</a>
 */
public final class JsonWriter implements DatasetFileWriter {

    /**
     * Character for curly left bracket.
     */
    private static final char CURLY_LEFT = '{';
    /**
     * Character for curly right bracket.
     */
    private static final char CURLY_RIGHT = '}';
    /**
     * Character for semi colon.
     */
    private static final char SEMICOLON = ':';
    /**
     * Character for double quote.
     */
    private static final char DOUBLE_QUOTE = '"';
    /**
     * Character for left bracket.
     */
    private static final char BRACKET_LEFT = '[';
    /**
     * Character for right bracket.
     */
    private static final char BRACKET_RIGHT = ']';
    /**
     * Character for linefeed.
     */
    private static final char LINEFEED = '\n';
    /**
     * Character for tab.
     */
    private static final char TAB = '\t';
    /**
     * Character for comma.
     */
    private static final char COMMA = ',';
    /**
     * Character for space.
     */
    private static final char SPACE = ' ';

    /**
     * Creates a new instance.
     */
    public JsonWriter() {
    }

    /**
     * Writes the CSV to the file.
     *
     * @param file the file
     * @param csv  the CSV to write
     * @throws CsvWriterException when the file could not be written to
     */
    public void writeCSV(final File file, final CSV csv) throws CsvWriterException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            writeCSV(csv, out);
        } catch (final IOException e) {
            throw new CsvWriterException(file, e);
        }
    }

    /**
     * Writes the CSV to the file.
     *
     * @param csv the CSV to write
     * @param out outputStream
     * @throws IOException when the file could not be written to
     */
    public void writeCSV(final CSV csv, final OutputStream out) throws IOException {
        out.write(BRACKET_LEFT);
        out.write(LINEFEED);
        for (int y = 0; y < csv.getRowCount(); y++) {
            if (y > 0) {
                out.write(COMMA);
                out.write(LINEFEED);
            }
            Row row = csv.getRow(y);
            out.write(SPACE);
            out.write(CURLY_LEFT);
            out.write(LINEFEED);
            for (int x = 0; x < csv.getColumnCount(); x++) {
                Column c = csv.getColumn(x);
                if (x > 0) {
                    out.write(COMMA);
                    out.write(LINEFEED);
                }
                out.write(SPACE);
                out.write(SPACE);
                out.write(DOUBLE_QUOTE);
                writeString(csv.getColumn(x).getName(), out);
                out.write(DOUBLE_QUOTE);
                out.write(SEMICOLON);
                out.write(DOUBLE_QUOTE);
                String s2 = row.getAsString(c);
                writeString(s2, out);
                out.write(DOUBLE_QUOTE);
            }
            out.write(LINEFEED);
            out.write(SPACE);
            out.write(CURLY_RIGHT);
        }
        out.write(LINEFEED);
        out.write(BRACKET_RIGHT);
        out.flush();
    }

    /**
     * Writes the string to the outputStream.
     *
     * @param s            the string to write
     * @param outputStream the outputStream
     * @throws IOException when the string could not be written
     */
    private void writeString(final String s, final OutputStream outputStream) throws IOException {
        if (s != null) {
            for (int z = 0; z < s.length(); z++) {
                char c = s.charAt(z);
                if (c == DOUBLE_QUOTE) {
                    outputStream.write('\\');
                    outputStream.write(c);
                } else {
                    outputStream.write(c);
                }
            }
        }
    }

}
