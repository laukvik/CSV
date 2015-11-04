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

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

/**
 * OutputStream for writing CSV data
 *
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public final class CsvWriter implements Writeable {

    private final OutputStream out;
    private final MetaData metaData;

    public CsvWriter(OutputStream out, MetaData metaData) throws IOException {
        this.out = out;
        this.metaData = metaData;
        writeMetaData(metaData);
    }

    public CsvWriter(OutputStream out) throws IOException {
        this.metaData = null;
        this.out = out;
    }

    /**
     * Writes a single row of CSV data
     *
     * @param row
     * @throws IOException
     */
    public void writeRow(Row row) throws IOException {
        List<String> values = new ArrayList<>();
        for (int x = 0; x < metaData.getColumnCount(); x++) {
            Column c = metaData.getColumn(x);
            Object o = row.getAsString(c);
            values.add(c.asString(o));
        }
        writeValues(values);
    }

    public void write(CSV csv) throws IOException {
        writeMetaData(csv.getMetaData());
        for (int y = 0; y < csv.getRowCount(); y++) {
            writeRow(csv.getRow(y));
        }
    }

    public static boolean isDigitsOnly(String value) {
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

    public void writeMetaData(MetaData metaData) throws IOException {
        List<String> items = new ArrayList<>();
        for (int x = 0; x < metaData.getColumnCount(); x++) {
            items.add(metaData.getColumnName(x));
        }
        writeValues(items);
    }

    public void writeRow(String... values) throws IOException {
        writeValues(Arrays.asList(values));
    }

    private void writeValues(List<String> values) throws IOException {
        for (int x = 0; x < values.size(); x++) {
            if (x > 0) {
                out.write(CSV.COMMA);
            }
            String column = values.get(x);
            if (column == null) {
                out.write(CSV.QUOTE);
                out.write(CSV.QUOTE);
            } else if (isDigitsOnly(column)) {
                /* Digits only */
                out.write(column.getBytes());
            } else {
                /* Text */
                out.write(CSV.QUOTE);
                for (int n = 0; n < column.length(); n++) {
                    char ch = column.charAt(n);
                    if (ch == CSV.QUOTE) {
                        /* Encode quotes - write an extra quote */
                        out.write(CSV.QUOTE);
                    }
                    out.write(ch);
                }
                out.write(CSV.QUOTE);
            }
        }
        out.write(CSV.RETURN);
        out.write(CSV.LINEFEED);
    }

    @Override
    public void close() throws IOException {
        out.flush();
        out.close();
    }

    public void writeEntityRow(Object instance) throws IllegalArgumentException, IllegalAccessException, IOException {
        /* Iterate all annotated fields */
        for (Field f : instance.getClass().getDeclaredFields()) {
            List<String> values = new ArrayList<>();
            /* Set accessible to allow injecting private fields - otherwise an exception will occur*/
            f.setAccessible(true);
            /* Get field value */
            Object value = f.get(instance);
            if (value == null) {
                values.add("");
            } else {
                values.add(value.toString());
            }
            writeValues(values);
        }
    }

    public void writeMetaData(Class aClass) throws IOException {
        List<String> values = new ArrayList<>();
        for (Field f : aClass.getDeclaredFields()) {
            /* Find the name of the field - in code */
            String nameAttribute = f.getName();
            values.add(nameAttribute);
        }
        writeValues(values);
    }

}
