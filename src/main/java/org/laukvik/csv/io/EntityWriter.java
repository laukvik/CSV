package org.laukvik.csv.io;

import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Morten Laukvik
 */
public class EntityWriter implements Writeable{

    private OutputStream out;

    public EntityWriter() {
    }

    public void writeEntityRow(Class instance) throws IllegalArgumentException, IllegalAccessException, IOException {
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
//            writeValues(values);
        }
    }

//    public void writeMetaData(T aClass) throws IOException {
//        List<String> values = new ArrayList<>();
//        for (Field f : aClass.getDeclaredFields()) {
//            /* Find the name of the field - in code */
//            String nameAttribute = f.getName();
//            values.add(nameAttribute);
//        }
////        writeValues(values);
//    }

    @Override
    public void writeFile(final CSV csv) throws IOException {
//        CsvWriter writer = new CsvWriter(new FileOutputStream(file));
    }

    @Override
    public void close() throws Exception {
        out.flush();
        out.close();
    }

    public static MetaData buildMetaData(Class aClass){
        MetaData md = new MetaData();
        List<String> values = new ArrayList<>();
        for (Field f : aClass.getDeclaredFields()) {
            /* Find the name of the field - in code */
            String nameAttribute = f.getName();
            values.add(nameAttribute);
        }
        return md;
    }

    public static Row buildRow(Class aClass){
        Row row = new Row();
        return row;
    }

}
