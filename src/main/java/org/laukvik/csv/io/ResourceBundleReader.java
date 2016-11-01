package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
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
public final class ResourceBundleReader extends AbstractResourceBundle implements Readable {

    /**
     * The MetaData with the locales.
     */
    private MetaData metaData;
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
     * @throws FileNotFoundException when the file wasn't found
     */
    public void readFile(final File file) throws FileNotFoundException {
        String filename = file.getName();
        String base = filename.substring(0, filename.lastIndexOf(EXTENSION));
        File home = new File(file.getParent());
        ResourceBundleFileFilter bf = new ResourceBundleFileFilter(base);
        File[] files = home.listFiles(bf);
        if (files != null) {
            try {
                propertiesList = new ArrayList<>();
                // Build files list
                metaData = new MetaData();
                metaData.addColumn(ResourceBundleWriter.COLUMN_PROPERTY);
                final Set<String> keySet = new HashSet<>();

                // Add default
                propertiesList.add(addFile(file, ResourceBundleWriter.COLUMN_DEFAULT, keySet));

                // Add others
                for (File f : files) {
                    String lang = getLocale(f.getName(), base);
                    propertiesList.add(addFile(f, lang, keySet));
                }
                keys = new ArrayList<>();
                keys.addAll(keySet);

                index = -1;
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reads the file and extracts all used property values.
     *
     * @param file the file
     * @param lang the language
     * @param keySet the keys to read
     * @return the Properties
     * @throws IOException when the file could not be read
     */
    private Properties addFile(final File file, final String lang, final Set<String> keySet) throws IOException {
        metaData.addColumn(new StringColumn(lang));
        Properties p = new Properties();
        p.load(new FileInputStream(file));
        for (Object o : p.keySet()) {
            keySet.add((String) o);
        }
        return p;
    }

    /**
     * Returns the MetaData.
     * @return the MetaData
     */
    public MetaData getMetaData() {
        return metaData;
    }

    /**
     * Returns the row.
     *
     * @return the row
     */
    public Row getRow() {
        return currentRow;
    }

    /**
     * Returns true if more rows are available.
     *
     * @return true if more rows are available
     */
    public boolean hasNext() {
        return index < keys.size() - 1;
    }

    /**
     * Returns the next row.
     *
     * @return the next row
     */
    public Row next() {
        index++;
        currentRow = new Row();
        String key = keys.get(index);
        StringColumn sc = (StringColumn) metaData.getColumn(0);
        currentRow.update(sc, key);
        for (int x = 0; x < propertiesList.size(); x++) {
            StringColumn column = (StringColumn) metaData.getColumn(x + 1);
            Properties p = propertiesList.get(x);
            currentRow.update(column, (String) p.get(key));
        }
        return currentRow;
    }



}
