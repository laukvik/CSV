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

import java.io.File;
import java.io.FileOutputStream;
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
public final class CsvWriter implements DatasetFileWriter, DatasetOutputStream {


    /**
     * Writes the data set in the CSV format to the outputStream.
     */
    public CsvWriter() {
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
     * @param file the file
     */
    public void writeCSV(final CSV csv, final File file) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            writeCSV(csv, out);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Writes the CSV to the file.
     *
     * @param csv the CSV to write
     * @param out outputStream
     * @throws IOException the file could not be written
     */
    public void writeCSV(final CSV csv, final OutputStream out) throws IOException {
        BOM bom = csv.getMetaData().getBOM();
        if (bom != null) {
            out.write(bom.getBytes());
        }
        writeMetaData(csv.getMetaData(), out);
        for (int y = 0; y < csv.getRowCount(); y++) {
            writeCSV(csv.getRow(y), csv.getMetaData(), out);
        }
    }

    /**
     * Writes the row to the outputStream.
     *
     * @param row          the row
     * @param metaData     the metadata
     * @param outputStream the outputStream
     * @throws IOException when the row could not be written
     */
    public void writeCSV(final Row row, final MetaData metaData, final OutputStream outputStream) throws IOException {
        writeRow(row, row.getCSV().getMetaData(), outputStream);
    }

    /**
     * Writes the metadata to the outputStream.
     * @param metaData     the metadata
     * @param outputStream the outputStream
     * @throws IOException when the metaData could not be written
     */
    public void writeCSV(final MetaData metaData, final OutputStream outputStream) throws IOException {
        writeMetaData(metaData, outputStream);
    }

    /**
     * Writes the row to File.
     *
     * @param row      the row
     * @param metaData the MetaData to use
     * @param out      outputStream
     * @throws IOException when the file could not be written to.
     */
    private void writeRow(final Row row, final MetaData metaData, final OutputStream out) throws IOException {
        List<String> values = new ArrayList<>();
        for (int x = 0; x < metaData.getColumnCount(); x++) {
            Column c = metaData.getColumn(x);
            values.add(row.getAsString(c));
        }
        writeValues(values, out);
    }

    /**
     * Writes the MetaData.
     *
     * @param metaData the MetaData
     * @param out      outputStream
     * @throws IOException when the MetaData could not be written
     */
    private void writeMetaData(final MetaData metaData, final OutputStream out) throws IOException {
        List<String> items = new ArrayList<>();
        for (int x = 0; x < metaData.getColumnCount(); x++) {
            Column c = metaData.getColumn(x);
            String header = c.getName() + "(" + c.getName() + ")";
            items.add(header);
        }
        writeValues(items, out);
    }

    /**
     * Writes the values.
     *
     * @param values the values
     * @param out    outputStream
     * @throws IOException when the values could not be written
     */
    private void writeValues(final List<String> values, final OutputStream out) throws IOException {
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

}
