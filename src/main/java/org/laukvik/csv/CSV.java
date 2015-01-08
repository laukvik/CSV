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
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CSV implements Serializable {

    public final static String MIME_TYPE = "text/csv";


    public final static char LINEFEED = 10;
    public final static char RETURN = 13;
    public final static char COMMA = ',';
    public final static char SEMINCOLON = ';';
    public final static char PIPE = '|';
    public final static char TAB = '\t';
    public final static char QUOTE = '"';
    public final static String CRLF = "\r\n";


    private String[] columns;
    private List<String[]> rows;

    public CSV(String[] headers, List<String[]> rows) {
        this.columns = headers;
        this.rows = rows;
    }

    public CSV(String... headers) {
        this.columns = headers;
        this.rows = new ArrayList<>();
    }

    public CSV(File file) throws IOException {
        this(new FileInputStream(file), Charset.forName("utf-8"));
    }

    public CSV(File file, Charset charset) throws IOException {
        this(new FileInputStream(file), charset);
    }

    public CSV(InputStream inputStream, Charset charset) throws IOException {
        rows = new ArrayList<>();
        columns = null;
        try (CsvInputStream is = new CsvInputStream(inputStream, charset)) {
            int x = 0;
            while (is.hasMoreLines()) {
                List<String> items = is.readLine();
                String[] arr = new String[items.size()];
                arr = items.toArray(arr);
                if (x == 0) {
                    /* Found columns */
                    columns = arr;
                } else {
                    /* Found row */
                    rows.add(arr);
                }
                x++;
            }
        } catch (IOException e) {
            throw e;
        }
    }

    public int getColumnCount() {
        return columns.length;
    }

    public String getColumnName(int column) {
        return columns[column];
    }

    /**
     * Returns the index of the column. If it isnt found it returns -1
     *
     * @param name
     * @return
     */
    public int getColumnIndex(String name) {
        int index = -1;
        for (int x = 0; x < columns.length; x++) {
            String h = columns[x];
            if (h.equalsIgnoreCase(name)) {
                return x;
            }
        }
        return index;
    }

    public int getRowCount() {
        return rows.size();
    }

    public void setValue(String value, int column, int row) {
        if (column > columns.length) {
            throw new ColumnNotFoundException(column, columns.length);
        }
        if (row > rows.size()) {
            throw new RowNotFoundException(row, rows.size());
        }
        String[] values = this.rows.get(row);
        values[column] = value;
    }

    public String getValue(int column, int row) {
        if (column > columns.length) {
            throw new ColumnNotFoundException(column, columns.length);
        }
        if (row > rows.size()) {
            throw new RowNotFoundException(row, rows.size());
        }
        return rows.get(row)[column];
    }

    public void addRow(String... values) {
        if (values.length != columns.length) {
            throw new AddRowException(values.length, columns.length);
        }
        rows.add(values);
    }

    /**
     * Writes the contents to file
     *
     * @param file
     * @throws IOException
     */
    public void write(File file) throws IOException {
        try (CsvOutputStream out = new CsvOutputStream(new FileOutputStream(file))) {
            out.writeHeader(columns);
            for (String[] row : rows) {
                out.writeLine(row);
            }
        } catch (IOException e) {
            throw e;
        }
    }

}
