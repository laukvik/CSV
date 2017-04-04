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
package no.laukvik.csv.io;

import no.laukvik.csv.CSV;
import no.laukvik.csv.Row;
import no.laukvik.csv.columns.Column;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Writes the data setRaw in the CSV format.
 *
 * @see <a href="https://tools.ietf.org/html/rfc4180">Common Format and MIME Type for Comma-Separated
 * Values (CSV) Files</a>
 * @see <a href="https://en.wikipedia.org/wiki/Comma-separated_values">Comma Separated Values (wikipedia)</a>
 */
public final class CsvWriter implements DatasetFileWriter {

    /**
     * Returns true if value only contains digits.
     *
     * @param value the value
     * @return true if digits only
     */
    protected static boolean isDigitsOnly(final String value) {
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
     * @param csv  the CSV to write
     * @param file the file
     * @throws CsvWriterException when the file could not be written
     */
    @Override
    public void writeCSV(final File file, final CSV csv) throws CsvWriterException {
        final Charset cs = csv.getCharset() == null ? BOM.UTF8.getCharset() : csv.getCharset();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), cs))) {
            // Columns
            List<String> columns = buildColumns(csv);
            writeValues(columns, writer);
            // Rows
            for (int y = 0; y < csv.getRowCount(); y++) {
                Row r = csv.getRow(y);
                List<String> items = buildRow(r, csv);
                writeValues(items, writer);
            }
        } catch (final IOException e) {
            throw new CsvWriterException(file, e);
        }
    }


    /**
     * Builds a list of String for each value in the row.
     *
     * @param row the row
     * @param csv the csv
     * @return a list of columns
     */
    private List<String> buildRow(final Row row, final CSV csv) {
        List<String> values = new ArrayList<>();
        for (int x = 0; x < csv.getColumnCount(); x++) {
            Column c = csv.getColumn(x);
            values.add(row.getRaw(c));
        }
        return values;
    }

    /**
     * Writes the column headers.
     *
     * @param csv the csv
     * @return a list of columns
     */
    private List<String> buildColumns(final CSV csv) {
        List<String> items = new ArrayList<>();
        for (int x = 0; x < csv.getColumnCount(); x++) {
            Column c = csv.getColumn(x);
            items.add(c.toCSV());
        }
        return items;
    }

    /**
     * Writes the values.
     *
     * @param values the values
     * @param writer the writer
     * @throws IOException when the values could not be written
     */
    private void writeValues(final List<String> values, final Writer writer) throws IOException {
        for (int x = 0; x < values.size(); x++) {
            if (x > 0) {
                writer.write(CSV.COMMA);
            }
            String column = values.get(x);
            if (isDigitsOnly(column)) {
                // Digits only
                writer.write(column);
            } else {
                // Text
                writer.write(CSV.QUOTE_DOUBLE);
                for (int n = 0; n < column.length(); n++) {
                    char ch = column.charAt(n);
                    if (ch == CSV.QUOTE_DOUBLE) {
                        // Encode quotes - writeCSV an extra quote
                        writer.write(CSV.QUOTE_DOUBLE);
                    }
                    writer.write(ch);
                }
                writer.write(CSV.QUOTE_DOUBLE);
            }
        }
        writer.write(CSV.RETURN);
        writer.write(CSV.LINEFEED);
    }

}
