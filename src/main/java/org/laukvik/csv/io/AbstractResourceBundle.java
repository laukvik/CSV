package org.laukvik.csv.io;

import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Morten Laukvik
 */
public class AbstractResourceBundle {

    public final static String COLUMN_PROPERTY = "property";
    public final static String COLUMN_DEFAULT = "default";
    public final static String EXTENSION = ".properties";


    /**
     * Returns the locale for the
     *
     * @param filename the filename to extract the locale from
     * @param basename
     * @return
     */
    public static String getLocale(String filename, String basename) {
        if (filename == null || !filename.endsWith(EXTENSION)){
            return null;
        } else {
            String baseAndLocale = filename.substring(0, filename.length()-EXTENSION.length());
            if (baseAndLocale.isEmpty()){
                return null;
            }
            String locale = baseAndLocale.substring(basename.length());
            if (locale.startsWith("_")){
                locale = locale.substring(1);
            }
            return locale.isEmpty() ? null : locale;
        }
    }

    /**
     * Extracts the basename of a properties file
     *
     * @param file the file
     * @return the basename
     */
    public static String getBasename(File file){
        String filename = file.getName();
        if (filename.toLowerCase().endsWith(EXTENSION)){
            return filename.substring(0, filename.toLowerCase().lastIndexOf(EXTENSION));
        }
        return null;
    }

    /**
     * Creates the filename for a specified column
     *
     * @param column the column
     * @param basename the basename
     * @return
     */
    public static String getFilename(StringColumn column, String basename ){
        String locale = column.getName();
        String filename;
        if (locale.equalsIgnoreCase(COLUMN_DEFAULT)){
            filename = basename;
        } else {
            filename = basename + "_" + locale;
        }
        return filename + EXTENSION;
    }

    public static class ResourceBundleFileFilter implements FileFilter {

        private String basename;

        /**
         *
         * @param basename
         */
        public ResourceBundleFileFilter(final String basename) {
            this.basename = basename;
        }

        @Override
        public boolean accept(final File file) {
            if (file == null || file.isDirectory()) {
                return false;
            }
            return AbstractResourceBundle.getLocale(file.getName(), basename) != null;
        }
    }


}
