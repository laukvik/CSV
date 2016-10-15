package org.laukvik.csv.fx;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Remembers to recently opened files.
 *
 * @author Morten Laukvik
 */
public final class Recent {

    final private CSV csv;
    final private File file;
    final private int size = 20;

    public Recent() {
        this.csv = new CSV();
        this.file = getRecentFileList();
        load();
    }

    public Recent(final File file) {
        this.csv = new CSV();
        this.file = file;
        load();
    }

    public boolean load(){
        if (file.exists() && file.length() > 0 ){
            try {
                csv.readFile(file);
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {

            csv.getMetaData().addColumn(new StringColumn("file2"));
            return true;
        }
    }

    public boolean save(){
        try {
            csv.writeFile(file);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public List<File> getList(){
        List<File> list = new ArrayList<>();
        StringColumn sc = (StringColumn) csv.getMetaData().getColumn(0);
        for (int y=0; y<csv.getRowCount(); y++){
            Row r = csv.getRow(y);
            list.add( new File(r.getString(sc)) );
        }
        return list;
    }

    public void open(final File file){
        StringColumn sc = (StringColumn) csv.getMetaData().getColumn(0);
        csv.addRow().update(sc, file.getAbsolutePath());
        save();
    }



    /**
     * Returns the Library folder for the user.
     *
     * @return
     */
    private static File getLibrary() {
        return new File(System.getProperty("user.home"), "Library");
    }

    /**
     * Returns the folder where all configuration of CSV is.
     *
     * @return
     */
    private static File getHome() {
        File file = new File(getLibrary(), "org.laukvik.csv");
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    public File getRecentFileList(){
        return new File(getHome(), "recent.csv");
    }

    public void clear() {
        csv.clear();
    }
}
