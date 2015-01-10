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
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An API for reading and writing to CSV. The implementation is based on the
 * specficiations from http://tools.ietf.org/rfc/rfc4180.txt
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
 * c.addRow( new Row("Steve","Jobs") );
 * c.removeColumn("First");
 * c.addColumn("Email);
 * c.addColumn("Address",0);
 * c.write( new File("nerds.csv") );
 * </code>
 *
 * <code>
 * CSV c = new CSV( Person.class );
 * c.add( new Person("Bill","Gates") );
 * c.write( new File("persons.csv") );
 * </code>
 *
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CSV implements Serializable {

    public final static String MIME_TYPE = "text/csv";
    public final static String FILE_EXTENSION = "csv";
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


    public <T> List<T> readEntities(Class<T> aClass) {
        List<T> items = new ArrayList<>();
        try {
            /* Iterate all rows */
            for (Row r : rows) {
                /* Create new Entity */
                Object instance = aClass.newInstance();
                items.add((T) instance);
                /* Iterate all fields in object*/
                for (Field f : instance.getClass().getDeclaredFields()) {
                    /* Set accessible to allow injecting private fields - otherwise an exception will occur*/
                    f.setAccessible(true);
                    /* Get field value */
                    Object value = f.get(instance);
                    /* Find the name of the field - in code */
                    String nameAttribute = f.getName();

                    if (f.getType() == String.class) {
                        f.set(instance, r.getString(nameAttribute));
                    } else if (f.getType() == Integer.class) {
                        f.set(instance, r.getInteger(nameAttribute));
                    } else if (f.getType() == URL.class) {
                        f.set(instance, r.getURL(nameAttribute));
                    }

                    f.setAccessible(false);
                }
            }
        } catch (InstantiationException ex) {
            Logger.getLogger(CSV.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(CSV.class.getName()).log(Level.SEVERE, null, ex);
        }

        return items;
    }

    /**
     * Removes all rows
     *
     * @param fromRowIndex
     * @param endRowIndex
     */
    public void removeRows(int fromRowIndex, int endRowIndex) {
        rows.subList(fromRowIndex, endRowIndex + 1).clear();
    }

    public void insertRow(Row row, int rowIndex) {
        row.setMetaData(metaData);
        rows.add(rowIndex, row);
    }

    public void addColumn(String name) {
        metaData.addColumn(name);
        for (Row r : rows) {
            r.add("");
        }
    }

    public void insertColumn(String name, int columnIndex) {
        metaData.addColumn(name, columnIndex);
        for (Row r : rows) {
            r.add("");
            r.insert("", columnIndex);
            r.setMetaData(metaData);
        }
    }

    public void removeColumn(int columnIndex) {
        metaData.removeColumn(columnIndex);
        for (Row r : rows) {
            r.remove(columnIndex);
        }
    }

}
