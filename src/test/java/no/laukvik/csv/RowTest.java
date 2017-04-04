/*
 * Copyright 2015 Laukviks Bedrifter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package no.laukvik.csv;

import junit.framework.TestCase;
import no.laukvik.csv.columns.BigDecimalColumn;
import no.laukvik.csv.columns.BooleanColumn;
import no.laukvik.csv.columns.ByteColumn;
import no.laukvik.csv.columns.DateColumn;
import no.laukvik.csv.columns.DoubleColumn;
import no.laukvik.csv.columns.FloatColumn;
import no.laukvik.csv.columns.IntegerColumn;
import no.laukvik.csv.columns.StringColumn;
import no.laukvik.csv.columns.UrlColumn;
import org.junit.Test;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class RowTest {

    private CSV buildCSV() {
        CSV csv = new CSV();
        return csv;
    }

    @Test
    public void shouldSetAllColumns() throws MalformedURLException {
        CSV csv = new CSV();
        BigDecimalColumn bdc = csv.addBigDecimalColumn("bigDecimal");
        BooleanColumn bc = csv.addBooleanColumn("boolean");
        ByteColumn byteC = csv.addByteColumn("byte");
        DateColumn datec = csv.addDateColumn("date");
        DoubleColumn dc = csv.addDoubleColumn("double");
        FloatColumn fc = csv.addFloatColumn("float");
        IntegerColumn ic = csv.addIntegerColumn("integer");
        StringColumn sc = csv.addStringColumn("string");
        UrlColumn uc = csv.addUrlColumn("url");
        Row r = csv.addRow();

        Date now = new Date();
        URL url = new URL("http://www.google.com");

        r.set(bdc, new BigDecimal("123"));
        r.set(bc, false);
        r.set(byteC, new byte[]{1, 2, 3, 4, 5});
        r.set(datec, now);
        r.set(dc, 178d);
        r.set(fc, 99f);
        r.set(ic, 64);
        r.set(sc, "Bob");
        r.set(uc, url);

        assertEquals(new BigDecimal("123"), r.get(bdc));
        assertEquals(Boolean.FALSE, r.get(bc));
        assertTrue(Arrays.equals(new byte[]{1, 2, 3, 4, 5}, r.get(byteC)));
        assertEquals(now, r.get(datec));
        TestCase.assertEquals(178d, r.get(dc));
        assertEquals(99f, r.get(fc));
        assertEquals((Integer) 64, r.get(ic));
        assertEquals("Bob", r.get(sc));
        assertEquals(url, r.get(uc));

        r.setRaw(bc, "true");
        assertEquals(Boolean.TRUE, r.get(bc));

        assertEquals("http://www.google.com", r.getRaw(uc));

    }

    @Test
    public void setBigDecimal() {
        CSV csv = new CSV();
        BigDecimalColumn bdc = csv.addBigDecimalColumn("bigDecimal");
        Row r = csv.addRow();
        r.set(bdc, new BigDecimal("123"));
        assertEquals(new BigDecimal("123"), r.get(bdc));
        assertEquals("123", r.getRaw(bdc));
    }

    @Test
    public void setBoolean() {
        CSV csv = new CSV();
        BooleanColumn bc = csv.addBooleanColumn("boolean");
        Row r = csv.addRow();
        r.set(bc, false);
        assertEquals(Boolean.FALSE, r.get(bc));
        assertEquals("false", r.getRaw(bc));
    }

    @Test
    public void setBytes() {
        CSV csv = new CSV();
        ByteColumn byteC = csv.addByteColumn("byte");
        Row r = csv.addRow();
        r.set(byteC, new byte[]{1, 2, 3, 4, 5});
        assertTrue(Arrays.equals(new byte[]{1, 2, 3, 4, 5}, r.get(byteC)));
//        assertEquals("123",r.getRaw(byteC));
    }

    @Test
    public void setDate() {
        CSV csv = new CSV();
        DateColumn datec = csv.addDateColumn("date");
        Row r = csv.addRow();
        Date now = new Date();
        r.set(datec, now);
        assertEquals(now, r.get(datec));
//        assertEquals("123",r.getRaw(datec));
    }

    @Test
    public void setDouble() {
        CSV csv = new CSV();
        DoubleColumn dc = csv.addDoubleColumn("double");
        Row r = csv.addRow();
        r.set(dc, 178d);
        TestCase.assertEquals(178d, r.get(dc));
        assertEquals("178.0", r.getRaw(dc));
    }

    @Test
    public void setFloat() {
        CSV csv = new CSV();
        FloatColumn fc = csv.addFloatColumn("float");
        Row r = csv.addRow();
        r.set(fc, 99f);
        assertEquals(99f, r.get(fc));
        assertEquals("99.0", r.getRaw(fc));
    }

    @Test
    public void setInteger() {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("integer");
        Row r = csv.addRow();
        r.set(ic, 64);
        assertEquals((Integer) 64, r.get(ic));
        assertEquals("64", r.getRaw(ic));
    }

    @Test
    public void setString() {
        CSV csv = new CSV();
        StringColumn sc = csv.addStringColumn("string");
        Row r = csv.addRow();
        r.set(sc, "Bob");
        assertEquals("Bob", r.get(sc));
    }

    @Test
    public void setUrl() throws MalformedURLException {
        CSV csv = new CSV();
        UrlColumn uc = csv.addUrlColumn("url");
        Row r = csv.addRow();
        URL url = new URL("http://www.google.com");
        r.set(uc, url);
        assertEquals(url, r.get(uc));
        assertEquals("http://www.google.com", r.getRaw(uc));
    }

    @Test
    public void shouldRemove() throws MalformedURLException {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("integer");
        Row r = csv.addRow();
        r.set(ic, 64);
        assertEquals((Integer) 64, r.get(ic));
        r.setNull(ic);
        assertEquals(true, r.isNull(ic));
        assertNull(r.get(ic));
    }

    @Test
    public void isnull() {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("i1");
        IntegerColumn ic2 = csv.addIntegerColumn("i2");
        Row r = csv.addRow().set(ic, 12);
        assertFalse(r.isNull(ic));
        assertTrue(r.isNull(ic2));
    }

    @Test
    public void getAsString() {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("i1");
        IntegerColumn ic2 = csv.addIntegerColumn("i2");
        Row r = csv.addRow().set(ic, 1234567).set(ic2, null);
        assertEquals("1234567", r.getRaw(ic));
        assertEquals("", r.getRaw(ic2));
    }

}
