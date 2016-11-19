package org.laukvik.csv.io.xml;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * @author Morten Laukvik
 */
public class TagTest {

    @Test
    public void getText() throws Exception {
        Tag t = new Tag("a");
        assertNull(t.getText());
    }

    @Test
    public void getParent() throws Exception {
        Tag div = new Tag("div");
        Tag a = div.addTag("a");
        assertNotNull(a.getParent());
    }

    @Test
    public void addTag() throws Exception {
        Tag div = new Tag("div");
        assertNull(div.getParent());
    }

    @Test
    public void getAttributes() throws Exception {
        Tag div = new Tag("div");
        div.addAttribute("align").setValue("left");
        assertEquals(1, div.getAttributes().size());
    }

    @Test
    public void addAttribute() throws Exception {
        Tag div = new Tag("div");
        assertNotNull(div.addAttribute("class"));
    }

    @Test
    public void isText() throws Exception {
        Tag div = new Tag("div");
        assertFalse(div.isText());
    }

    @Test
    public void setText() throws Exception {
        Tag div = new Tag("div");
        div.setText("");
    }

    @Test
    public void isSingle() throws Exception {
        assertFalse(new Tag("div").isSingle());
        assertTrue(new Tag("img").isSingle());
    }

    @Test
    public void attributes() throws Exception {
        Tag div = new Tag("div");
        div.addAttribute("align").setValue("left");
        div.addAttribute("style").setValue("display: none;");
        assertEquals(2, div.getAttributes().size());
    }

    @Test
    public void toHtml_IMG() throws Exception {
        Tag img = new Tag("img");
        img.addAttribute("src").setValue("thumb.gif");
        assertEquals("<img src=\"thumb.gif\">", img.toHtml());
        //
    }
    @Test
    public void toHtml_DIV() throws Exception {
        Tag div = new Tag("div");
        div.addAttribute("align").setValue("left");
        div.addAttribute("style").setValue("display: none");
        assertEquals("<div align=\"left\" style=\"display: none\"></div>", div.toHtml());
    }

    @Test
    public void toHtml_DIV2() throws Exception {
        Tag div = new Tag("div");
        div.addTag("text").setText("Hello world");
        assertEquals("<div>Hello world</div>", div.toHtml());
    }

}