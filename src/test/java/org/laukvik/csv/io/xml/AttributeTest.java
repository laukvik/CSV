package org.laukvik.csv.io.xml;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 */
public class AttributeTest {

    @Test
    public void toHtml() throws Exception {
        Attribute a = new Attribute("class");
        a.setValue("employee");
        assertEquals("class=\"employee\"", a.toHtml());
        a.setValue("");
        assertEquals("class=\"\"", a.toHtml());
        a.setValue(null);
        assertEquals("class=\"\"", a.toHtml());
    }

    @Test
    public void should() throws Exception {
        Attribute a = new Attribute("border", "1");
        assertEquals("1", a.getValue());
        assertEquals("border", a.getName());
        a.setName("width");
        assertEquals("width", a.getName());
    }

}