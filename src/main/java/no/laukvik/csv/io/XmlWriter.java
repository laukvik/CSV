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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Writes the data setRaw in the XML format.
 *
 * @see <a href="https://en.wikipedia.org/wiki/XML">XML (wikipedia)</a>
 */
public final class XmlWriter implements DatasetFileWriter {

    /**
     * Character for beginning of a tag.
     */
    private static final char OPEN = '<';
    /**
     * Character for closing of a tag.
     */
    private static final char CLOSE = '>';
    /**
     * Character for linefeed.
     */
    private static final char LINEFEED = '\n';
    /**
     * Character for carriage return.
     */
    private static final char CR = '\r';
    /**
     * Character for starting a closing tag.
     */
    private static final char SLASH = '/';
    /**
     * Character for enclosing attributes.
     */
    private static final char QUOTATION_MARK = '"';
    /**
     * Character for apostrophe.
     */
    private static final char APOSTROPHE = '\'';
    /**
     * Character for ampersand.
     */
    private static final char AMPERSAND = '&';
    /**
     * Character for space.
     */
    private static final char SPACE = ' ';
    /**
     * Character for equal sign.
     */
    private static final char EQUAL = '=';
    /**
     * Character for tab.
     */
    private static final char TAB = '\t';
    /**
     * The name of the root element to write.
     */
    private static final String ROOT_ELEMENT_NAME = "csv";

    /**
     * The name of the rows element to write.
     */
    private static final String ROWS_ELEMENT_NAME = "rows";

    /**
     * The name of the row element to write.
     */
    private static final String ROW_ELEMENT_NAME = "row";

    /**
     * The name of the columns element to write.
     */
    private static final String COLUMNS_ELEMENT_NAME = "columns";

    /**
     * The name of the column element to write.
     */
    private static final String COLUMN_ELEMENT_NAME = "column";

    /**
     * The name of the attribute for a column name.
     */
    private static final String ATTR_NAME = "name";

    /**
     * The name of the attribute for a column type.
     */
    private static final String ATTR_TYPE = "type";

    /**
     * Writes the CSV to the file.
     *
     * @param file the file
     * @param csv  the CSV to write
     * @throws CsvWriterException when the file could not be written
     */
    @Override
    public void writeCSV(final File file, final CSV csv) throws CsvWriterException {
        try (FileOutputStream out = new FileOutputStream(file)) {
            writeCSV(csv, out);
        } catch (final IOException e) {
            throw new CsvWriterException(file, e);
        }
    }

    /**
     * Writes the CSV.
     *
     * @param csv the CSV to write
     * @param out the outputStream
     * @throws IOException when the file cant be written
     */
    public void writeCSV(final CSV csv, final OutputStream out) throws IOException {
        Charset charset = csv.getCharset();
        out.write(("<?xml version=\"1.0\" encoding=\"" + charset.name() + "\" standalone=\"yes\"?>").getBytes());
        out.write(CR);
        out.write(LINEFEED);
        // Root
        out.write(OPEN);
        out.write(ROOT_ELEMENT_NAME.getBytes());
        out.write(CLOSE);

        // Columns
        out.write(LINEFEED);
        out.write(TAB);
        out.write(OPEN);
        out.write(COLUMNS_ELEMENT_NAME.getBytes());
        out.write(CLOSE);
        out.write(LINEFEED);

        for (int x = 0; x < csv.getColumnCount(); x++) {
            out.write(TAB);
            out.write(TAB);
            out.write(OPEN);
            out.write(COLUMN_ELEMENT_NAME.getBytes());

            out.write(SPACE);

            out.write(ATTR_NAME.getBytes());
            out.write(EQUAL);
            out.write(QUOTATION_MARK);
            out.write(csv.getColumn(x).getName().getBytes());
            out.write(QUOTATION_MARK);

            out.write(SPACE);

            out.write(ATTR_TYPE.getBytes());
            out.write(EQUAL);
            out.write(QUOTATION_MARK);
            out.write(csv.getColumn(x).getClass().getSimpleName().getBytes());
            out.write(QUOTATION_MARK);

            out.write(SLASH);

            out.write(CLOSE);
            out.write(LINEFEED);
        }

        out.write(TAB);
        out.write(OPEN);
        out.write(SLASH);
        out.write(COLUMNS_ELEMENT_NAME.getBytes());
        out.write(CLOSE);

        out.write(LINEFEED);
        out.write(TAB);
        out.write(OPEN);
        out.write(ROWS_ELEMENT_NAME.getBytes());
        out.write(CLOSE);

        // Iterate rows
        for (int y = 0; y < csv.getRowCount(); y++) {
            out.write(CR);
            out.write(LINEFEED);
            out.write(TAB);
            out.write(TAB);
            out.write(OPEN);
            out.write(ROW_ELEMENT_NAME.getBytes());
            Row r = csv.getRow(y);
            for (int x = 0; x < csv.getColumnCount(); x++) {
                Column col = csv.getColumn(x);
                out.write(SPACE);
                out.write(csv.getColumn(x).getName().getBytes(charset));
                out.write(EQUAL);
                out.write(QUOTATION_MARK);
                String s = r.getRaw(col);
                for (int n = 0; n < s.length(); n++) {
                    char c = s.charAt(n);
                    if (c == QUOTATION_MARK) {
                        out.write("&quot;".getBytes());
                    } else if (c == OPEN) {
                        out.write("&lt;".getBytes());
                    } else if (c == CLOSE) {
                        out.write("&gt;".getBytes());
                    } else if (c == APOSTROPHE) {
                        out.write("&apos;".getBytes());
                    } else if (c == AMPERSAND) {
                        out.write("&amp;".getBytes());
                    } else {
                        out.write(c);
                    }
                }
                out.write(QUOTATION_MARK);
            }
            out.write(SLASH);
            out.write(CLOSE);
            out.write(CR);
            out.write(LINEFEED);
        }

        out.write(TAB);
        out.write(OPEN);
        out.write(SLASH);
        out.write(ROWS_ELEMENT_NAME.getBytes());
        out.write(CLOSE);
        out.write(LINEFEED);

        // Close root element
        out.write(OPEN);
        out.write(SLASH);
        out.write(ROOT_ELEMENT_NAME.getBytes());
        out.write(CLOSE);
        out.flush();
        out.close();
    }

}
