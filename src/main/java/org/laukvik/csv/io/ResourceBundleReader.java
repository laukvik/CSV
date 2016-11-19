package org.laukvik.csv.io;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * Reads a properties and all other properties with the same base name which matches
 * the ResourceBundle concept.
 *
 */
public final class ResourceBundleReader extends AbstractResourceBundle implements DatasetFileReader {

    /**
     * The current row.
     */
    private Row currentRow;
    /**
     * The list of property files.
     */
    private List<Properties> propertiesList;
    /** The current row index. */
    private int index;
    /** The list of keys. */
    private List<String> keys;

    /**
     * Reads the file.
     *
     * @param file the file
     * @param csv the csv
     *
     * @throws FileNotFoundException when the file wasn't found
     */
    public void readFile(final File file, final CSV csv) throws FileNotFoundException {
        String filename = file.getName();
        String base = filename.substring(0, filename.lastIndexOf(EXTENSION));
        File home = new File(file.getParent());
        ResourceBundleFileFilter bf = new ResourceBundleFileFilter(base);
        File[] files = home.listFiles(bf);
        if (files != null) {
            try {
                propertiesList = new ArrayList<>();
                // Build files list
                csv.addColumn(ResourceBundleWriter.COLUMN_PROPERTY);
                final Set<String> keySet = new HashSet<>();

                // Add default
                propertiesList.add(addFile(file, csv, ResourceBundleWriter.COLUMN_DEFAULT, keySet));

                // Add others
                for (File f : files) {
                    String lang = getLocale(f.getName(), base);
                    propertiesList.add(addFile(f, csv, lang, keySet));
                }
                keys = new ArrayList<>();
                keys.addAll(keySet);

                index = -1;
            } catch (final IOException e) {
            }
        }
    }

    /**
     * Reads the file and extracts all used property values.
     *
     * @param file   the file
     * @param csv    the csv
     * @param lang   the language
     * @param keySet the keys to read
     * @return the Properties
     * @throws IOException when the file could not be read
     */
    private Properties addFile(final File file, final CSV csv,
                               final String lang, final Set<String> keySet) throws IOException {
        csv.addColumn(new StringColumn(lang));
        Properties p = new Properties();
        p.load(new FileInputStream(file));
        for (Object o : p.keySet()) {
            keySet.add((String) o);
        }
        return p;
    }

}
