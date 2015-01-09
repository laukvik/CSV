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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * http://tools.ietf.org/rfc/rfc4180.txt
 *
 * Read whole file into memory<br>
 *
 * <code>
 * CSV c = new CSV( new File("nerds.csv") );
 * </code>
 *
 * Write CSV to file
 *
 * <code>
 * CSV c = new CSV("First","Last");
 * c.addRow( "Bill","Gates" );
 * c.addRow( "Steve","Jobs" );
 * c.removeColumn("First");
 * c.removeColumn(2);
 * c.addColumn("Email);
 * c.addColumn(0);
 * c.write( new File("nerds.csv") );
 * </code>
 *
 * <code>
 * CSV c = new CSV();
 *
 * </code>
 *
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CSV implements Serializable {

    public final static String MIME_TYPE = "text/csv";
    public final static Charset CHARSET_DEFAULT = Charset.forName("utf-8");

    public final static char LINEFEED = 10;
    public final static char RETURN = 13;
    public final static char COMMA = ',';
    public final static char SEMINCOLON = ';';
    public final static char PIPE = '|';
    public final static char TAB = '\t';
    public final static char QUOTE = '"';
    public final static String CRLF = "\r\n";

    private MetaData metaData;
    private List<Row> rows;
    private Charset charset;

    public CSV(MetaData metaData) {
        this.metaData = metaData;
        this.rows = new ArrayList<>();
        this.charset = CHARSET_DEFAULT;
    }

    public CSV(String... headers) {
        this(new MetaData(headers));
    }

    public CSV(File file) throws IOException {
        this(new FileInputStream(file), CHARSET_DEFAULT);
    }

    public CSV(File file, Charset charset) throws IOException {
        this(new FileInputStream(file), charset);
    }

    public CSV(InputStream inputStream, Charset charset) throws IOException {
        rows = new ArrayList<>();
        this.charset = charset;
        try (CsvReader reader = new CsvReader(inputStream, charset)) {
            this.metaData = reader.getMetaData();
            while (reader.hasNext()) {
                Row row = reader.getRow();
                row.setMetaData(metaData);
                rows.add(row);
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public int getRowCount() {
        return rows.size();
    }

    public Row getRow(int rowIndex) {
        if (rowIndex > rows.size()) {
            throw new RowNotFoundException(rowIndex, rows.size());
        }
        return rows.get(rowIndex);
    }

    public void addRow(Row row) {
        row.setMetaData(metaData);
        rows.add(row);
    }

    public void addRow(String... values) {
        Row row = new Row();
        for (String v : values) {
            row.add(v);
        }
        addRow(row);
    }

    public void removeRow(int rowIndex) {
        rows.remove(rowIndex);
    }

    public void removeAllRows() {
        rows.clear();
    }

    /**
     * Writes the contents to file
     *
     * @param file
     * @throws IOException
     */
    public void write(File file) throws IOException {
        try (CsvWriter writer = new CsvWriter(new FileOutputStream(file), charset)) {
            writer.writeMetaData(metaData);
            for (Row row : rows) {
                writer.writeRow(row);
            }
        } catch (IOException e) {
            throw e;
        }
    }



}
