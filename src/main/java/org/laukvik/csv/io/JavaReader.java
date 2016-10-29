package org.laukvik.csv.io;

import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.BigDecimalColumn;
import org.laukvik.csv.columns.BooleanColumn;
import org.laukvik.csv.columns.ByteColumn;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.columns.UrlColumn;

import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 * Reads a data set by a list of objects.
 *
 * @author Morten Laukvik
 */
public class JavaReader<T> implements Readable {

    private final List<T> list;
    private final CSV csv;
    private int index;
    private Row row;

    /**
     * Reads the list of Java objects into a data set
     *
     * @param csv the csv
     * @param list the list
     */
    public JavaReader(CSV csv, List<T> list) {
        this.csv = csv;
        this.csv.setMetaData(buildMetaData(list.get(0).getClass()));
        this.list = list;
        this.index = 0;
    }


    /**
     * Returns the appropriate column for the specified Field
     *
     * @param field the reflection field
     * @return the column
     */
    public static Column findColumnByField(final Field field) {
        String fieldName = field.getName();
        if (field.getType() == String.class) {
            return new StringColumn(fieldName);

        } else if (field.getType() == BigDecimal.class) {
            return new BigDecimalColumn(fieldName);

        } else if (field.getType() == Byte.class) {
            return new ByteColumn(fieldName);

        } else if (field.getType() == Integer.class) {
            return new IntegerColumn(fieldName);

        } else if (field.getType() == Float.class) {
            return new FloatColumn(fieldName);

        } else if (field.getType() == Double.class) {
            return new DoubleColumn(fieldName);

        } else if (field.getType() == Boolean.class) {
            return new BooleanColumn(fieldName);

        } else if (field.getType() == Date.class) {
            return new DateColumn(fieldName);

        } else if (field.getType() == URL.class) {
            return new UrlColumn(fieldName);
        } else {
            return null;
        }
    }

    /**
     * Updates the instance with the properties from the specified instance
     *
     * @param row the row
     * @param column the column
     * @param instance the object to
     * @throws NoSuchFieldException when the field cant be found
     * @throws IllegalAccessException when the field cant be accessed
     */
    public static void updateByColumn(final Row row, final Column column, final Object instance) throws NoSuchFieldException, IllegalAccessException {
        Field f = instance.getClass().getField(column.getName());
        if (column instanceof StringColumn){
            StringColumn c = (StringColumn) column;
            row.update(c, (String) f.get(instance));
        } else if (column instanceof IntegerColumn){
            IntegerColumn c = (IntegerColumn) column;
            row.update(c, (Integer) f.get(instance));
        } else if (column instanceof BooleanColumn){
            BooleanColumn c = (BooleanColumn) column;
            row.update(c, (Boolean) f.get(instance));
        } else if (column instanceof FloatColumn){
            FloatColumn c = (FloatColumn) column;
            row.update(c, (Float) f.get(instance));
        } else if (column instanceof ByteColumn){
            ByteColumn c = (ByteColumn) column;
        } else if (column instanceof DateColumn){
            DateColumn c = (DateColumn) column;
            row.update(c, (Date) f.get(instance));
        } else if (column instanceof DoubleColumn){
            DoubleColumn c = (DoubleColumn) column;
            row.update(c, (Double) f.get(instance));
        } else if (column instanceof UrlColumn){
            UrlColumn c = (UrlColumn) column;
            row.update(c, (URL) f.get(instance));
        } else if (column instanceof BigDecimalColumn){
            BigDecimalColumn c = (BigDecimalColumn) column;
            row.update(c, (BigDecimal) f.get(instance));
        }
    }

    /**
     * Builds a MetaData object from a class
     *
     * @param instance the class
     * @return returns the MetaData
     */
    public static MetaData buildMetaData(Class instance){
        MetaData metaData = new MetaData();
        for (Field f : instance.getDeclaredFields()) {
            // Set accessible to allow injecting private fields - otherwise an exception will occur
            f.setAccessible(true);
            // Find the name of the field - in code
            Column c = findColumnByField(f);
            if (c != null){
                metaData.addColumn(c);
            }
            f.setAccessible(false);
        }
        return metaData;
    }


    @Override
    public void readFile(final File file) {

    }

    @Override
    public MetaData getMetaData() {
        return csv.getMetaData();
    }

    @Override
    public boolean hasNext() {
        return index < list.size();
    }

    @Override
    public Row next() {
        Object instance = list.get(index);
        Row row = csv.addRow();
        MetaData metaData = csv.getMetaData();
        for (int x=0; x<metaData.getColumnCount(); x++){
            Column c = metaData.getColumn(x);
            try {
                updateByColumn(row, c, instance);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        index++;
        return row;
    }

    @Override
    public Row getRow() {
        return row;
    }

}
