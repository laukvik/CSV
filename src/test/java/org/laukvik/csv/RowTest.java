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
package org.laukvik.csv;

import org.junit.Test;
import org.laukvik.csv.columns.BigDecimalColumn;
import org.laukvik.csv.columns.BooleanColumn;
import org.laukvik.csv.columns.ByteColumn;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.columns.UrlColumn;

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

        r.setBigDecimal(bdc, new BigDecimal("123"));
        r.setBoolean(bc, false);
        r.setBytes(byteC, new byte[]{1, 2, 3, 4, 5});
        r.setDate(datec, now);
        r.setDouble(dc, 178d);
        r.setFloat(fc, 99f);
        r.setInteger(ic, 64);
        r.setString(sc, "Bob");
        r.setURL(uc, url);

        assertEquals(new BigDecimal("123"), r.getBigDecimal(bdc));
        assertEquals(Boolean.FALSE, r.getBoolean(bc));
        assertTrue(Arrays.equals(new byte[]{1, 2, 3, 4, 5}, r.getBytes(byteC)));
        assertEquals(now, r.getDate(datec));
        assertEquals(178d, r.getDouble(dc));
        assertEquals(99f, r.getFloat(fc));
        assertEquals((Integer) 64, r.getInteger(ic));
        assertEquals("Bob", r.getString(sc));
        assertEquals(url, r.getURL(uc));

        r.set(bc, "true");
        assertEquals(Boolean.TRUE, r.getBoolean(bc));

        assertEquals("http://www.google.com", r.getAsString(uc));

    }

    @Test
    public void setBigDecimal() {
        CSV csv = new CSV();
        BigDecimalColumn bdc = csv.addBigDecimalColumn("bigDecimal");
        Row r = csv.addRow();
        r.setBigDecimal(bdc, new BigDecimal("123"));
        assertEquals(new BigDecimal("123"), r.getBigDecimal(bdc));
        assertEquals("123", r.getAsString(bdc));
    }

    @Test
    public void setBoolean() {
        CSV csv = new CSV();
        BooleanColumn bc = csv.addBooleanColumn("boolean");
        Row r = csv.addRow();
        r.setBoolean(bc, false);
        assertEquals(Boolean.FALSE, r.getBoolean(bc));
        assertEquals("false", r.getAsString(bc));
    }

    @Test
    public void setBytes() {
        CSV csv = new CSV();
        ByteColumn byteC = csv.addByteColumn("byte");
        Row r = csv.addRow();
        r.setBytes(byteC, new byte[]{1, 2, 3, 4, 5});
        assertTrue(Arrays.equals(new byte[]{1, 2, 3, 4, 5}, r.getBytes(byteC)));
//        assertEquals("123",r.getAsString(byteC));
    }

    @Test
    public void setDate() {
        CSV csv = new CSV();
        DateColumn datec = csv.addDateColumn("date");
        Row r = csv.addRow();
        Date now = new Date();
        r.setDate(datec, now);
        assertEquals(now, r.getDate(datec));
//        assertEquals("123",r.getAsString(datec));
    }

    @Test
    public void setDouble() {
        CSV csv = new CSV();
        DoubleColumn dc = csv.addDoubleColumn("double");
        Row r = csv.addRow();
        r.setDouble(dc, 178d);
        assertEquals(178d, r.getDouble(dc));
        assertEquals("178.0", r.getAsString(dc));
    }

    @Test
    public void setFloat() {
        CSV csv = new CSV();
        FloatColumn fc = csv.addFloatColumn("float");
        Row r = csv.addRow();
        r.setFloat(fc, 99f);
        assertEquals(99f, r.getFloat(fc));
        assertEquals("99.0", r.getAsString(fc));
    }

    @Test
    public void setInteger() {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("integer");
        Row r = csv.addRow();
        r.setInteger(ic, 64);
        assertEquals((Integer) 64, r.getInteger(ic));
        assertEquals("64", r.getAsString(ic));
    }

    @Test
    public void setString() {
        CSV csv = new CSV();
        StringColumn sc = csv.addStringColumn("string");
        Row r = csv.addRow();
        r.setString(sc, "Bob");
        assertEquals("Bob", r.getString(sc));
    }

    @Test
    public void setUrl() throws MalformedURLException {
        CSV csv = new CSV();
        UrlColumn uc = csv.addUrlColumn("url");
        Row r = csv.addRow();
        URL url = new URL("http://www.google.com");
        r.setURL(uc, url);
        assertEquals(url, r.getURL(uc));
        assertEquals("http://www.google.com", r.getAsString(uc));
    }

    @Test
    public void shouldRemove() throws MalformedURLException {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("integer");
        Row r = csv.addRow();
        r.setInteger(ic, 64);
        assertEquals((Integer) 64, r.getInteger(ic));
        r.setNull(ic);
        assertEquals(true, r.isNull(ic));
        assertNull(r.getInteger(ic));
    }

    @Test
    public void isnull() {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("i1");
        IntegerColumn ic2 = csv.addIntegerColumn("i2");
        Row r = csv.addRow().setInteger(ic, 12);
        assertFalse( r.isNull(ic));
        assertTrue( r.isNull(ic2));
    }

    @Test
    public void getAsString() {
        CSV csv = new CSV();
        IntegerColumn ic = csv.addIntegerColumn("i1");
        IntegerColumn ic2 = csv.addIntegerColumn("i2");
        Row r = csv.addRow().setInteger(ic, 1234567).setInteger(ic2, null);
        assertEquals( "1234567", r.getAsString(ic));
        assertEquals( "", r.getAsString(ic2));
    }

}
