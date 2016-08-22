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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.laukvik.csv.columns.BooleanColumn;
import org.laukvik.csv.columns.ByteColumn;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.io.CsvReader;
import org.laukvik.csv.io.CsvWriter;
import org.laukvik.csv.io.Readable;
import org.laukvik.csv.io.Writeable;
import org.laukvik.csv.query.Query;

/**
 * An API for reading and writing to Viewer. The implementation is based on the
 * specficiations from http://tools.ietf.org/rfc/rfc4180.txt
 *
 * Read whole file into memory<br>
 *
 * <code>
 * Viewer c = new Viewer( new File("nerds.csv") );
 * </code>
 *
 * Write Viewer to file
 *
 * <code>
 * Viewer c = new Viewer("First","Last");
 * c.addRow( "Bill","Gates" );
 * c.addRow( new Row("Steve","Jobs") );
 * c.removeColumn("First");
 * c.addColumn("Email);
 * c.addColumn("Address",0);
 * c.write( new File("nerds.csv") );
 * </code>
 *
 * <code>
 * Viewer c = new Viewer( Person.class );
 * c.addValue( new Person("Bill","Gates") );
 * c.write( new File("persons.csv") );
 * </code>
 *
 *
 * @author Morten Laukvik
 */
public class CSV implements Serializable {

    public final static String MIME_TYPE = "text/csv";
    public final static String FILE_EXTENSION = "csv";

    public final static char LINEFEED = 10;
    public final static char RETURN = 13;
    public final static char COMMA = ',';
    public final static char SEMINCOLON = ';';
    public final static char PIPE = '|';
    public final static char TAB = '\t';
    public final static char QUOTE = '"';
    public final static String CRLF = "\r\n";

    protected MetaData metaData;
    protected List<Row> rows;
    private Query query;

    public CSV() {
        this.metaData = new MetaData();
        this.rows = new ArrayList<>();
        this.query = null;
    }

    /**
     * Reads the CSV file
     *
     * @param file
     * @throws IOException
     */
    public void read(File file) throws IOException {
        read(new CsvReader(new FileInputStream(file)));
    }

    /**
     * Reads any readable stream
     *
     * @param reader
     */
    public void read(Readable reader) {
        this.query = null;
        rows = new ArrayList<>();
        this.metaData = reader.getMetaData();
        while (reader.hasNext()) {
            Row row = reader.getRow();
            rows.add(row);
        }
    }

    protected List<Row> getRows() {
        return rows;
    }

    public MetaData getMetaData() {
        return metaData;
    }

    public void setMetaData(MetaData metaData) {
        this.metaData = metaData;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

    public int getRowCount() {
        return rows.size();
    }

    public Row getRow(int rowIndex) {
        if (rowIndex > getRowCount()) {
            throw new RowNotFoundException(rowIndex, getRowCount());
        }
        return rows.get(rowIndex);
    }

    public Row addRow() {
        Row r = new Row();
        addRow(r);
        return r;
    }

    public Row addRow(Row row) {
        row.setCSV(this);
        rows.add(row);
        return row;
    }

    public void removeRow(int rowIndex) {
        rows.remove(rowIndex);
    }

    public void removeAllRows() {
        rows.clear();
    }

    public void write(Writeable writer) throws IOException, Exception {
        writer.write(this);
        writer.close();
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

    public Row insertRow(Row row, int rowIndex) {
        row.setCSV(this);
        rows.add(rowIndex, row);
        return row;
    }

    public Column addColumn(Column column) {
        metaData.addColumn(column);
        return column;
    }

    public StringColumn addStringColumn(StringColumn column) {
        addColumn(column);
        return column;
    }

    public IntegerColumn addIntegerColumn(IntegerColumn column) {
        addColumn(column);
        return column;
    }

    public FloatColumn addFloatColumn(FloatColumn column) {
        addColumn(column);
        return column;
    }

    public BooleanColumn addBooleanColumn(BooleanColumn column) {
        addColumn(column);
        return column;
    }

    public ByteColumn addByteColumn(ByteColumn column) {
        addColumn(column);
        return column;
    }

    /**
     * Adds a new text column with the specified name
     *
     * @param name
     * @return
     */
    public Column addColumn(String name) {
        return metaData.addColumn(name);
    }

    public StringColumn addStringColumn(String name) {
        return (StringColumn) metaData.addColumn(new StringColumn(name));
    }

    public String insertColumn(String name, int columnIndex) {
        metaData.addColumn(name, columnIndex);
        return name;
    }

    /**
     * Removes the column and all its data
     *
     * @param column
     */
    public void removeColumn(Column column) {
        for (Row r : rows) {
            r.remove(column);
        }
        getMetaData().removeColumn(column);
    }

    private static File getLibrary() {
        return new File(System.getProperty("user.home"), "Library");
    }

    private static File getHome() {
        File file = new File(getLibrary(), "org.laukvik.csv");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public static File getFile(Class aClass) {
        File file = new File(getHome(), aClass.getCanonicalName() + ".csv");
        return file;
    }

    /**
     * Find an object directly
     *
     * @param <T>
     * @param aClass
     * @return
     */
    public static <T> T find(Class<T> aClass) {
        return null;
    }

    private static <T> T createInstance(Row row, Class<T> aClass) throws InstantiationException, IllegalAccessException {
        Object instance = aClass.newInstance();

        /* Iterate all fields in object*/
        for (Field f : instance.getClass().getDeclaredFields()) {
            /* Set accessible to allow injecting private fields - otherwise an exception will occur*/
            f.setAccessible(true);
            /* Find the name of the field - in code */
            String nameAttribute = f.getName();

            if (f.getType() == String.class) {
//                f.set(instance, row.getAsString(nameAttribute));
            } else if (f.getType() == Integer.class) {
//                f.set(instance, row.getInteger(nameAttribute));
            } else if (f.getType() == URL.class) {
//                f.set(instance, row.getURL(nameAttribute));
            }

            f.setAccessible(false);
        }
        return (T) instance;
    }

    public static <T> List<T> findByClass(Class<T> aClass) {
        File file = getFile(aClass);
        try {
            return findByClass(new FileInputStream(file), Charset.defaultCharset(), aClass);
        }
        catch (FileNotFoundException ex) {
            List<T> items = new ArrayList<>();
            return items;
        }
    }

    public static <T> List<T> findByClass(InputStream inputStream, Charset charset, Class<T> aClass) {
        List<T> items = new ArrayList<>();
        try (CsvReader reader = new CsvReader(inputStream, charset)) {
            while (reader.hasNext()) {
                Row row = reader.getRow();
//                row.setMetaData(reader.getMetaData());
                items.add(createInstance(row, aClass));
            }
        }
        catch (IOException e) {
            Logger.getLogger(CSV.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (InstantiationException ex) {
            Logger.getLogger(CSV.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex) {
            Logger.getLogger(CSV.class.getName()).log(Level.SEVERE, null, ex);
        }
        return items;
    }

    public static <T> void saveAll(List<?> objects, Class<T> aClass) throws IllegalArgumentException, IllegalAccessException {
        File file = CSV.getFile(aClass);
        try (CsvWriter writer = new CsvWriter(new FileOutputStream(file))) {
            writer.writeMetaData(aClass);
            for (Object o : objects) {
                writer.writeEntityRow(o);
            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Query findByQuery() {
        this.query = new Query(metaData, this);
        return this.query;
    }

    public DistinctColumnValues getDistinctColumnValues(int columnIndex) {
        Column c = metaData.getColumn(columnIndex);
        DistinctColumnValues cv = new DistinctColumnValues(c);
        for (Row r : rows) {
            cv.addValue(r.getAsString(c));
        }
        return cv;
    }

    /**
     * Returns a set of unique values for the specified column
     *
     * @param columnIndex
     * @return
     */
    public Set<String> listDistinct(Column column) {
        Set<String> values = new TreeSet<>();
        for (Row r : rows) {
            values.add(r.getAsString(column));
        }
        return values;
    }

//    public Set<String> listDistinct(String column) {
//        return listDistinct(getMetaData().getColumnIndex(column));
//    }
    public void close() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
