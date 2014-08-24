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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class CSV implements CSVListener {

    String[] headers;
    List<String[]> rows;
    CSVReader reader;

    public CSV() {
        headers = null;
        rows = new ArrayList<String[]>();
        reader = new CSVReader();
        reader.addListener(this);
    }

    public CSV(char delimiter) {
        headers = null;
        rows = new ArrayList<String[]>();
        reader = new CSVReader(delimiter);
        reader.addListener(this);
    }

    public void parse(InputStream is) throws IOException {
        reader.read(is);
    }

    public void parse(File file) throws FileNotFoundException, IOException {
        reader.read(new FileInputStream(file));
    }

    public void write(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        write(fos);
        fos.close();
    }

    public void write(OutputStream out) throws IOException {
        if (isHeadersAvailable()) {
            for (int x = 0; x < headers.length; x++) {
                if (x > 0) {
                    out.write((CSVReader.COMMA + "").getBytes());
                }
                out.write(encode(headers[ x]).getBytes());
            }
            out.write(CSVReader.LF);
        }
        for (int y = 0; y < rows.size(); y++) {
            if (y > 0) {
//				out.write( CSVReader.LF );
            }
            String[] row = rows.get(y);
            for (int x = 0; x < row.length; x++) {
                if (x > 0) {
                    out.write((CSVReader.COMMA + "").getBytes());
                }
                out.write(encode(row[ x]).getBytes());
            }
            out.write(CSVReader.LF);
        }
    }

    public static String encode(String value) {
        if (value == null) {
            return null;
        } else if (value.length() == 0) {
            return "";
        } else {
            String newValue = value;
            if (value.contains("\"")) {
                newValue = newValue.replaceAll("\"", "\"\"");
            }
            if (value.contains(",")) {
                newValue = "\"" + newValue + "\"";
            }
            return newValue;
        }
    }

    public void foundHeaders(String[] values) {
        headers = values;
    }

    public void foundRow(int rowIndex, String[] values) {
        rows.add(values);
    }

    public int getColumnCount() {
        if (headers == null) {
            return rows.get(0).length;
        } else {
            return headers.length;
        }
    }

    public boolean isHeadersAvailable() {
        return headers != null;
    }

    public String getHeader(int columnIndex) {
        return headers[ columnIndex];
    }

    public String getCell(int column, int row) {
        if (row > rows.size()) {
            throw new CSVRowNotFoundException(row);
        } else {
            String[] r = rows.get(row);
            if (column > r.length) {
                throw new CSVColumnNotFoundException(column, row);
            } else {
                return r[ column];
            }
        }
    }

    public int getRowCount() {
        return rows.size();
    }

    public void setCell(String value, int column, int row) {
        rows.get(row)[ column] = value;
    }
}
