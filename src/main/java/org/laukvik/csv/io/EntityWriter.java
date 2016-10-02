package org.laukvik.csv.io;

import org.laukvik.csv.CSV;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Morten Laukvik
 */
public class EntityWriter implements Writeable{

    private File file;

    public EntityWriter(final File file) {
        this.file = file;
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
//            writeValues(values);
        }
    }

    public void writeMetaData(Class aClass) throws IOException {
        List<String> values = new ArrayList<>();
        for (Field f : aClass.getDeclaredFields()) {
            /* Find the name of the field - in code */
            String nameAttribute = f.getName();
            values.add(nameAttribute);
        }
//        writeValues(values);
    }

    @Override
    public void writeFile(final CSV csv) throws IOException {

    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public void close() throws Exception {

    }
}
