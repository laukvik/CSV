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
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes the data set in the CSV format.
 *
 * @see <a href="https://tools.ietf.org/html/rfc4180">Common Format and MIME Type for Comma-Separated
 * Values (CSV) Files</a>
 * @see <a href="https://en.wikipedia.org/wiki/Comma-separated_values">Comma Separated Values (wikipedia)</a>
 */
public final class CsvWriter implements Writeable, AutoCloseable {

    /**
     * The outputStream to write to.
     */
    private final OutputStream out;

    /**
     * Writes the data set in the CSV format to the outputStream.
     *
     * @param outputStream the outputstream
     * @throws IOException when the data could not be written
     */
    public CsvWriter(final OutputStream outputStream) throws IOException {
        this.out = outputStream;
    }

    /**
     * Returns true if value only contains digits.
     *
     * @param value the value
     * @return true if digits only
     */
    public static boolean isDigitsOnly(final String value) {
        if (value == null) {
            return false;
        }
        for (int x = 0; x < value.length(); x++) {
            char c = value.charAt(x);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    /**
     * Writes the CSV to the file.
     *
     * @param csv the CSV to write
     * @throws IOException the file could not be written
     */
    public void writeCSV(final CSV csv) throws IOException {
        BOM bom = csv.getMetaData().getBOM();
        if (bom != null) {
            out.write(bom.getBytes());
        }
        writeMetaData(csv.getMetaData());
        for (int y = 0; y < csv.getRowCount(); y++) {
            writeRow(csv.getRow(y));
        }
    }

    /**
     * Writes a single row of CSV data.
     *
     * @param row the row to write
     * @throws IOException when the row could not be written
     */
    private void writeRow(final Row row) throws IOException {
        writeRow(row, row.getCSV().getMetaData());
    }

    /**
     * Writes the row to File.
     *
     * @param row      the row
     * @param metaData the MetaData to use
     * @throws IOException when the file could not be written to.
     */
    public void writeRow(final Row row, final MetaData metaData) throws IOException {
        List<String> values = new ArrayList<>();
        for (int x = 0; x < metaData.getColumnCount(); x++) {
            Column c = metaData.getColumn(x);
            values.add(row.getAsString(c));
        }
        writeValues(values);
    }

    /**
     * Writes the MetaData.
     *
     * @param metaData the MetaData
     * @throws IOException when the MetaData could not be written
     */
    public void writeMetaData(final MetaData metaData) throws IOException {
        List<String> items = new ArrayList<>();
        for (int x = 0; x < metaData.getColumnCount(); x++) {
            Column c = metaData.getColumn(x);
            String header = c.getName() + "(" + c.getName() + ")";
            items.add(header);
        }
        writeValues(items);
    }

    /**
     * Writes the values.
     * @param values the values
     * @throws IOException when the values could not be written
     */
    private void writeValues(final List<String> values) throws IOException {
        for (int x = 0; x < values.size(); x++) {
            if (x > 0) {
                out.write(CSV.COMMA);
            }
            String column = values.get(x);
            if (column == null) {
                out.write(CSV.DOUBLE_QUOTE);
                out.write(CSV.DOUBLE_QUOTE);
            } else if (isDigitsOnly(column)) {
                /* Digits only */
                out.write(column.getBytes());
            } else {
                /* Text */
                out.write(CSV.DOUBLE_QUOTE);
                for (int n = 0; n < column.length(); n++) {
                    char ch = column.charAt(n);
                    if (ch == CSV.DOUBLE_QUOTE) {
                        /* Encode quotes - writeCSV an extra quote */
                        out.write(CSV.DOUBLE_QUOTE);
                    }
                    out.write(ch);
                }
                out.write(CSV.DOUBLE_QUOTE);
            }
        }
        out.write(CSV.RETURN);
        out.write(CSV.LINEFEED);
    }

    /**
     * Closes the outputStream.
     *
     * @throws Exception when the outputStream could not be closed
     */
    public void close() throws Exception {
        out.close();
    }
}
