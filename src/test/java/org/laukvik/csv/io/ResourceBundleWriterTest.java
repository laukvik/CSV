package org.laukvik.csv.io;

import org.junit.Assert;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;


/**
 * @author Morten Laukvik
 */
public class ResourceBundleWriterTest {

    @Test
    public void getBasename() throws Exception {
        Assert.assertEquals("language", ResourceBundleWriter.getBasename(new File("/Users/obama/language.properties")));
        Assert.assertEquals(null, ResourceBundleWriter.getBasename(new File("/Users/obama/languageproperties")));
        Assert.assertEquals("language.properties", ResourceBundleWriter.getBasename(new File("/Users/obama/language.properties.properties")));
    }

    @Test
    public void getFilename() throws Exception {
        Assert.assertEquals("lang.properties",ResourceBundleWriter.getFilename( new StringColumn("default"), "lang" ));
        Assert.assertEquals("lang_no.properties",ResourceBundleWriter.getFilename( new StringColumn("no"), "lang" ));
        Assert.assertEquals("lang_no_NO.properties",ResourceBundleWriter.getFilename( new StringColumn("no_NO"), "lang" ));
    }

    @Test
    public void writeAndReadBundle() throws Exception {
        File file = new File(System.getProperty("user.home"), "writebundle.properties");
        CSV csv = new CSV();
        StringColumn keyColumn     = csv.addStringColumn("key");
        StringColumn defaultColumn = csv.addStringColumn("default");
        StringColumn noColumn      = csv.addStringColumn("no");
        StringColumn seColumn      = csv.addStringColumn("se");
        csv.addRow().update(keyColumn, "add").update(defaultColumn, "Add").update(noColumn, "Legg til").update(seColumn, "Lägg till");
        csv.addRow().update(keyColumn, "remove").update(defaultColumn, "Delete").update(noColumn, "Slett").update(seColumn, "Radera");

        csv.writeResourceBundle( file );

        csv = new CSV();
        try {
            csv.readResourceBundle(file);
        } catch (Exception e) {

        } finally {
            file.delete();
        }

        Assert.assertEquals(4,csv.getMetaData().getColumnCount());
        Assert.assertEquals(2, csv.getRowCount());

        StringColumn c1 = (StringColumn) csv.getMetaData().getColumn(0);
        StringColumn c2 = (StringColumn) csv.getMetaData().getColumn(1);
        StringColumn c3 = (StringColumn) csv.getMetaData().getColumn(2);
        StringColumn c4 = (StringColumn) csv.getMetaData().getColumn(3);

        Assert.assertEquals("property", c1.getName());
        Assert.assertEquals("default", c2.getName());

    }

}