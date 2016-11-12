package org.laukvik.csv.io;

import org.laukvik.csv.CSV;
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

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Reads a data set by a list of objects.
 *
 * @param <T> The instance of an Object.
 */
public final class JavaReader<T> implements DatasetReader {

    /**
     * The list of objects found.
     */
    private final List<T> list;
    /** The CSV. */
    private CSV csv;
    /** The number of objects read. */
    private int index;
    /**
     * The current row.
     */
    private Row currentRow;

    /**
     * Reads the list of Java objects into a data set.
     *
     * @param list the list
     */
    public JavaReader(final List<T> list) {
        this.list = list;
        this.index = 0;
    }

    /**
     * Returns the appropriate column for the specified Field.
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
     * Updates the instance with the properties from the specified instance.
     *
     * @param row      the row
     * @param column   the column
     * @param instance the object to
     * @throws NoSuchFieldException   when the field cant be found
     * @throws IllegalAccessException when the field cant be accessed
     */
    public static void updateByColumn(final Row row, final Column column, final Object instance)
            throws NoSuchFieldException, IllegalAccessException {
        Field f = instance.getClass().getField(column.getName());
        if (column instanceof StringColumn) {
            StringColumn c = (StringColumn) column;
            row.setString(c, (String) f.get(instance));
        } else if (column instanceof IntegerColumn) {
            IntegerColumn c = (IntegerColumn) column;
            row.setInteger(c, (Integer) f.get(instance));
        } else if (column instanceof BooleanColumn) {
            BooleanColumn c = (BooleanColumn) column;
            row.setBoolean(c, (Boolean) f.get(instance));
        } else if (column instanceof FloatColumn) {
            FloatColumn c = (FloatColumn) column;
            row.setFloat(c, (Float) f.get(instance));
        } else if (column instanceof ByteColumn) {
            ByteColumn c = (ByteColumn) column;
        } else if (column instanceof DateColumn) {
            DateColumn c = (DateColumn) column;
            row.setDate(c, (Date) f.get(instance));
        } else if (column instanceof DoubleColumn) {
            DoubleColumn c = (DoubleColumn) column;
            row.setDouble(c, (Double) f.get(instance));
        } else if (column instanceof UrlColumn) {
            UrlColumn c = (UrlColumn) column;
            row.setURL(c, (URL) f.get(instance));
        } else if (column instanceof BigDecimalColumn) {
            BigDecimalColumn c = (BigDecimalColumn) column;
            row.setBigDecimal(c, (BigDecimal) f.get(instance));
        }
    }

    /**
     * Builds a MetaData object from a class.
     *
     * @param instance the class
     * @return returns the MetaData
     */
    public static List<Column> buildMetaData(final Class instance, final CSV csv) {
        List<Column> cols = new ArrayList<>();

        for (Field f : instance.getDeclaredFields()) {
            // Set accessible to allow injecting private fields - otherwise an exception will occur
            f.setAccessible(true);
            // Find the name of the field - in code
            Column c = findColumnByField(f);
            if (c != null) {
                cols.add(c);
            }
            f.setAccessible(false);
        }
        return cols;
    }

    /**
     * @param csv
     */
    public void readDataset(final CSV csv) {
        this.csv = csv;
        for (Column c : buildMetaData(list.get(0).getClass(), csv)) {
            csv.addColumn(c);
        }
    }

    public Row next() {
        Object instance = list.get(index);
        currentRow = csv.addRow();
        for (int x = 0; x < csv.getColumnCount(); x++) {
            Column c = csv.getColumn(x);
            try {
                updateByColumn(currentRow, c, instance);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        index++;
        return currentRow;
    }


}
