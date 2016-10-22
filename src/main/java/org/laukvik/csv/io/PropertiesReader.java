package org.laukvik.csv.io;

import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

/**
 * @author Morten Laukvik
 */
public class PropertiesReader implements Readable {

    private MetaData metaData;
    private Row row;
    private List<Properties> propertiesList;
    private int index;
    private List<String> keys;

    @Override
    public void readFile(final File file) throws FileNotFoundException {
        String filename = file.getName();
        String base = filename.substring(0, filename.lastIndexOf(".properties"));
        File home = new File(file.getParent());
        BundleFilter bf = new BundleFilter(base);
        File[] files = home.listFiles(bf);
        if (files != null) {
            try {
                propertiesList = new ArrayList<>();
                // Build files list
                metaData = new MetaData();
                metaData.addColumn("key");
                final Set<String> keySet = new HashSet<>();

                // Add default
                propertiesList.add(addFile(file, "default", keySet));

                // Add others
                for (File f : files) {
                    String lang = BundleFilter.getLanguage(f.getName(), base);
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

    private Properties addFile(final File file, final String lang, final Set<String> keySet) throws IOException {
        metaData.addColumn(new StringColumn(lang));
        Properties p = new Properties();
        p.load(new FileInputStream(file));
        for (Object o : p.keySet()) {
            keySet.add((String) o);
        }
        return p;
    }

    @Override
    public MetaData getMetaData() {
        return metaData;
    }

    @Override
    public Row getRow() {
        return row;
    }

    @Override
    public boolean hasNext() {
        return index < keys.size() - 1;
    }

    @Override
    public Row next() {
        index++;
        row = new Row();
        String key = keys.get(index);
        StringColumn sc = (StringColumn) metaData.getColumn(0);
        row.update(sc, key);
        for (int x = 0; x < propertiesList.size(); x++) {
            StringColumn column = (StringColumn) metaData.getColumn(x + 1);
            Properties p = propertiesList.get(x);
            row.update(column, (String) p.get(key));
        }
        return row;
    }

    public static class BundleFilter implements FileFilter {

        private String basename;

        public BundleFilter(final String basename) {
            this.basename = basename;
        }

        public static String getLanguage(String filename, String basename) {
            if (filename.startsWith(basename) && filename.endsWith(".properties")) {
                if (filename.length() == (basename.length() + ".properties".length())) {
                    return null;
                }
                int index = filename.lastIndexOf(".properties"); // lang_no.properties
                return filename.substring(basename.length(), index);
            } else {
                return null;
            }
        }

        @Override
        public boolean accept(final File file) {
            if (file == null || file.isDirectory()) {
                return false;
            }
            return getLanguage(file.getName(), basename) != null;
        }
    }

}
