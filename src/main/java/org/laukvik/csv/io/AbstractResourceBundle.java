package org.laukvik.csv.io;

import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.FileFilter;

/**
 * Helper class for reading and writing ResourceBundles.
 *
 */
public class AbstractResourceBundle {

    /**
     * The column name.
     */
    static final String COLUMN_PROPERTY = "property";
    /**
     * The column name for default language.
     */
    static final String COLUMN_DEFAULT = "default";
    /**
     * The file extension.
     */
    static final String EXTENSION = ".properties";


    /**
     * Returns the locale for the filename.
     *
     * @param filename the filename to extract the locale from
     * @param basename the basename of the bundle
     * @return the new filename
     */
    public static String getLocale(final String filename, final String basename) {
        if (filename == null || !filename.endsWith(EXTENSION)) {
            return null;
        } else {
            String baseAndLocale = filename.substring(0, filename.length() - EXTENSION.length());
            if (baseAndLocale.isEmpty()) {
                return null;
            }
            String locale = baseAndLocale.substring(basename.length());
            if (locale.startsWith("_")) {
                locale = locale.substring(1);
            }
            return locale.isEmpty() ? null : locale;
        }
    }

    /**
     * Extracts the basename of a properties file.
     *
     * @param file the file
     * @return the basename
     */
    public static String getBasename(final File file) {
        String filename = file.getName();
        if (filename.toLowerCase().endsWith(EXTENSION)) {
            return filename.substring(0, filename.toLowerCase().lastIndexOf(EXTENSION));
        }
        return null;
    }

    /**
     * Creates the filename for a specified column.
     *
     * @param column the column
     * @param basename the basename
     * @return the filename
     */
    public static String getFilename(final StringColumn column, final String basename) {
        String locale = column.getName();
        String filename;
        if (locale.equalsIgnoreCase(COLUMN_DEFAULT)) {
            filename = basename;
        } else {
            filename = basename + "_" + locale;
        }
        return filename + EXTENSION;
    }

    /**
     * FileFilter class that accepts only ResourceBundle files.
     *
     */
    public static class ResourceBundleFileFilter implements FileFilter {

        /**  */
        private final String bundleBaseName;

        /**
         * Creates a new instance which only accepts filename matching the specified basename.
         *
         * @param basename the basename
         */
        public ResourceBundleFileFilter(final String basename) {
            this.bundleBaseName = basename;
        }

        /**
         * Returns true if the file is accepted.
         *
         * @param file the file to check
         * @return true if accepted
         */
        public boolean accept(final File file) {
            return !(file == null || file.isDirectory()) && AbstractResourceBundle.getLocale(file.getName(), bundleBaseName) != null;
        }
    }


}
