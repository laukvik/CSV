package org.laukvik.csv.fx;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Remembers a list of recently opened files.
 *
 */
public final class Recent {

    /**
     * The default limit of files.
     */
    public static final int DEFAULT_LIMIT = 10;
    /**
     * The CSV instance.
     */
    private final CSV csv;
    /**
     * The file with the recent list.
     */
    private final File file;
    /**
     * The maximum number of files to remember.
     */
    private final int limit;

    /**
     * Reads the recent list from the file with the specified limit.
     *
     * @param recentFile the recent file
     * @param limit      the maximum number of files to remember
     */
    public Recent(final File recentFile, final int limit) {
        this.csv = new CSV();
        this.file = recentFile;
        this.limit = limit;
        load();
    }

    /**
     * Reads the recent list from the file with the default limit.
     *
     * @param recentFile the recent file
     */
    public Recent(final File recentFile) {
        this(recentFile, DEFAULT_LIMIT);
    }

    /**
     * Returns the Library folder for the user.
     *
     * @return the folder called Library in the users home folder
     */
    private static File getLibrary() {
        return new File(System.getProperty("user.home"), "Library");
    }

    /**
     * Returns the folder where all configuration of CSV is.
     *
     * @return the folder called org.laukvik.csv in the users library folder
     */
    private static File getHome() {
        File file = new File(getLibrary(), "org.laukvik.csv");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    /**
     * Returns the File where the remembered files are.
     * @return the file
     */
    public static File getConfigurationFile() {
        return new File(getHome(), "recent.csv");
    }

    /**
     * Loads the recent file.
     */
    private void load() {
        if (!file.exists() || file.length() == 0) {
            csv.getMetaData().addColumn("filename");
        } else {
            try {
                csv.readFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Saves the list to the file.
     * @return true if successful
     */
    public boolean save() {
        try {
            csv.writeFile(file);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Returns the list of files remembered.
     *
     * @return the list
     */
    public List<File> getList() {
        StringColumn c = (StringColumn) csv.getMetaData().getColumn(0);
        List<File> list = new ArrayList<>();
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            list.add(new File(r.getString(c)));
        }
        return list;
    }

    /**
     * Remembers the specified file.
     *
     * @param fileToRemember the file
     */
    public void open(final File fileToRemember) {
        StringColumn c = (StringColumn) csv.getMetaData().getColumn(0);
        csv.addRow().setString(c, fileToRemember.getAbsolutePath());
        if (csv.getRowCount() > limit) {
            int extra = csv.getRowCount() - limit;
            csv.removeRowsBetween(0, extra);
        }
        save();
    }

    /**
     * Clears the list of remembered files.
     */
    public void clear() {
        csv.removeRows();
    }

}
