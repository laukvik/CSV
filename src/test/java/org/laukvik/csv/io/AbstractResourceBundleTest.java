package org.laukvik.csv.io;

import org.junit.Assert;
import org.junit.Test;
import org.laukvik.csv.columns.StringColumn;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author Morten Laukvik
 */
public class AbstractResourceBundleTest {

    @Test
    public void shouldFileFilter() throws FileNotFoundException {
        AbstractResourceBundle.ResourceBundleFileFilter filter = new AbstractResourceBundle.ResourceBundleFileFilter("messages");
        Assert.assertEquals(true, filter.accept(new File("/User/morten/messages_no.properties")));
        Assert.assertEquals(true, filter.accept(new File("/User/morten/messages_dk.properties")));
    }

    @Test
    public void getLocale() throws Exception {
        Assert.assertEquals("no", AbstractResourceBundle.getLocale( "bundle_no.properties", "bundle" ));
        Assert.assertEquals("no_NO", AbstractResourceBundle.getLocale( "bundle_no_NO.properties", "bundle" ));
        Assert.assertEquals(null, AbstractResourceBundle.getLocale( "bundle.properties", "bundle" ));
        Assert.assertEquals(null, AbstractResourceBundle.getLocale( "bundle_.properties", "bundle" ));
        Assert.assertEquals(null, AbstractResourceBundle.getLocale( ".properties", "bundle" ));
        Assert.assertEquals(null, AbstractResourceBundle.getLocale( "properties", "bundle" ));
        Assert.assertEquals(null, AbstractResourceBundle.getLocale( "bundle", "bundle" ));
        Assert.assertEquals(null, AbstractResourceBundle.getLocale( "", "bundle" ));
    }

    @Test
    public void getBasename() throws Exception {
        Assert.assertEquals("bundle", AbstractResourceBundle.getBasename( new File("bundle.properties") ));
        Assert.assertEquals("bundle", AbstractResourceBundle.getBasename( new File("/Users/me/bundle.properties") ));
        Assert.assertEquals("bundle_no", AbstractResourceBundle.getBasename( new File("/Users/me/bundle_no.properties") ));
        Assert.assertEquals("bundle_no_NO", AbstractResourceBundle.getBasename( new File("/Users/me/bundle_no_NO.properties") ));
    }

    @Test
    public void getFilename() throws Exception {
        Assert.assertEquals("bundle.properties", AbstractResourceBundle.getFilename( new StringColumn("default"), "bundle" ));
        Assert.assertEquals("bundle_no.properties", AbstractResourceBundle.getFilename( new StringColumn("no"), "bundle" ));
        Assert.assertEquals("bundle_no_NO.properties", AbstractResourceBundle.getFilename( new StringColumn("no_NO"), "bundle" ));
    }





}