package org.laukvik.csv.io;

import org.junit.Assert;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.IOException;


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
    public void writeAndReadBundle() throws IOException {
        File file = new File(System.getProperty("user.home"), "writebundle.properties");
        CSV csv = new CSV();
        StringColumn keyColumn     = csv.addStringColumn("key");
        StringColumn defaultColumn = csv.addStringColumn("default");
        StringColumn noColumn      = csv.addStringColumn("no");
        StringColumn seColumn      = csv.addStringColumn("se");
        csv.addRow().setString(keyColumn, "add").setString(defaultColumn, "Add").setString(noColumn, "Legg til").setString(seColumn, "Lägg till");
        csv.addRow().setString(keyColumn, "remove").setString(defaultColumn, "Delete").setString(noColumn, "Slett").setString(seColumn, "Radera");
        csv.writeResourceBundle( file );


        CSV csv2 = new CSV();
        csv2.readResourceBundle(file);

//        Assert.assertEquals(2, csv2.getRowCount());
//        Assert.assertEquals(4, csv2.getColumnCount());

        StringColumn c1 = (StringColumn) csv2.getColumn(0);
        StringColumn c2 = (StringColumn) csv2.getColumn(1);
        StringColumn c3 = (StringColumn) csv2.getColumn(2);
        StringColumn c4 = (StringColumn) csv2.getColumn(3);

//        Assert.assertEquals("property", c1.getName());
//        Assert.assertEquals("default", c2.getName());
    }

}