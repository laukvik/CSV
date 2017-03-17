package org.laukvik.csv.columns;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 */
public class StringColumnTest {

    @Test
    public void getSize() throws Exception {
        StringColumn c = new StringColumn("hospital");
        c.setSize(10);
        assertEquals(10, c.getSize());
        assertEquals("hospital", c.getName());
    }

    @Test
    public void asString() throws Exception {
        StringColumn c = new StringColumn("hospital");
        assertEquals("Fake road 12", c.asString("Fake road 12"));
        assertEquals("", c.asString(null));
    }

    @Test
    public void parse() throws Exception {
        StringColumn c = new StringColumn("hospital");
        assertEquals("Fake road 12", c.parse("Fake road 12"));
        assertEquals("", c.parse(null));
    }

    @Test
    public void compare() throws Exception {
        StringColumn c = new StringColumn("hospital");
        assertEquals(-1, c.compare("A","B"));
        assertEquals(0, c.compare("A","A"));
        assertEquals(1, c.compare("B","A"));
        assertEquals(-1, c.compare(null,"A"));
        assertEquals(1, c.compare("B",null));
        assertEquals(0, c.compare(null,null));
    }

    @Test
    public void getFirstLetter() throws Exception {
        assertEquals(null, StringColumn.getFirstLetter(null));
        assertEquals("a", StringColumn.getFirstLetter("a"));
        assertEquals("b", StringColumn.getFirstLetter("ba"));
        assertEquals(null, StringColumn.getFirstLetter(""));
    }

    @Test
    public void getLength() throws Exception {
        assertEquals(0, StringColumn.getLength(null));
        assertEquals(1, StringColumn.getLength("a"));
        assertEquals(2, StringColumn.getLength("ba"));
        assertEquals(0, StringColumn.getLength(""));
    }

    @Test
    public void getWordCount() throws Exception {
        assertEquals((Integer) 0, StringColumn.getWordCount(null));
        assertEquals((Integer) 3, StringColumn.getWordCount("a b c"));
        assertEquals((Integer) 4, StringColumn.getWordCount("Lorem ipsum sit amet."));
        assertEquals((Integer) 0, StringColumn.getWordCount(""));
        assertEquals((Integer) 0, StringColumn.getWordCount(" "));
    }

    @Test
    public void getPrefix() throws Exception {
        assertEquals(null, StringColumn.getPrefix(null));
        assertEquals("morten", StringColumn.getPrefix("morten.jpg"));
        assertEquals("morten", StringColumn.getPrefix("morten.jpeg"));
        assertEquals("", StringColumn.getPrefix(".gif"));
        assertEquals(null, StringColumn.getPrefix("gif"));
        assertEquals(null, StringColumn.getPrefix("morten"));
        assertEquals(null, StringColumn.getPrefix(""));
    }

    @Test
    public void getPostfix() throws Exception {
        assertEquals(null, StringColumn.getPostfix(null));
        assertEquals("jpg", StringColumn.getPostfix("morten.jpg"));
        assertEquals("jpeg", StringColumn.getPostfix("morten.jpeg"));
        assertEquals("gif", StringColumn.getPostfix(".gif"));
        assertEquals(null, StringColumn.getPostfix("gif"));
        assertEquals(null, StringColumn.getPostfix("morten"));
        assertEquals(null, StringColumn.getPostfix(""));
    }

}