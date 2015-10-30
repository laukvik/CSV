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

import org.laukvik.csv.io.CsvReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CsvReaderTest {

    @Test
    public void readDataTypes() {
        String filename = "datatypes.csv";
        String charset = "utf-8";

        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat f = new SimpleDateFormat(pattern);
        long millis = 1322018752992L;
        Date date = new Date(millis);

        try (CsvReader reader = new CsvReader(new FileInputStream(getResource(filename)), Charset.forName(charset))) {
            int rows = 0;
            while (reader.hasNext()) {
                Row r = reader.getRow();
                assertEquals("BigDecimal", new BigDecimal("1234567890123456789.12345"), r.getBigDecimal("BigDecimal"));
                assertEquals("Boolean", new Boolean("true"), r.getBoolean("Boolean"));
//                assertEquals("Date1", date, r.getDate("Date1", f));
//                assertEquals("Date2", date, r.getDate("Date2"));
                assertEquals("Float", new Float(123.456), r.getFloat("Float"));
                assertEquals("Double", new Double(123456.789), r.getDouble("Double"));
                assertEquals("Integer", new Integer(123), r.getInteger("Integer"));
                assertEquals("Long", new Long(123456789), r.getLong("Long"));
                assertEquals("String", new String("Hello world!"), r.getString("String"));
                assertEquals("URL", new URL("http://www.google.com/home.html"), r.getURL("URL"));
                rows++;
            }
            assertEquals("Row count", 1, rows);
            assertEquals("Column count", 10, reader.getMetaData().getColumnCount());
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    public void readFile(String filename, int requiredColumns, int requiredRows, String charset) {
        try (CsvReader reader = new CsvReader(new FileInputStream(getResource(filename)), Charset.forName(charset))) {
            int rows = 0;
            while (reader.hasNext()) {
                Row r = reader.getRow();
                assertSame("Column count for row " + (rows + 1) + ": ", requiredColumns, r.getValues().size());
                rows++;
            }
            assertEquals("Row count", rows, requiredRows);
            assertEquals("Column count", reader.getMetaData().getColumnCount(), requiredColumns);
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readAcid() {
        readFile("acid.csv", 5, 4, "us-ascii");
    }

    @Test
    public void readEmbeddedCommas() {
        readFile("embeddedcommas.csv", 5, 1, "us-ascii");
    }

    @Test
    public void readEscaped() {
        readFile("escaped.csv", 4, 3, "utf-8");
    }

    @Test
    public void readQuoted() {
        readFile("quoted.csv", 4, 3, "us-ascii");
    }

    @Test
    public void readUnquoted() {
        readFile("unquoted.csv", 5, 4, "us-ascii");
    }

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvReaderTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void readWithIterator() {
        String filename = "acid.csv";
        String charset = "utf-8";
        try (CsvReader r = new CsvReader(new FileInputStream(getResource(filename)), Charset.forName(charset))) {
            if (r.hasNext()) {
                Row row = r.next();
                assertEquals("Year", "1996", row.getString("year"));
            }
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readCharset() {
        String filename = "charset.csv";
        String charset = "ISO-8859-1";
        String norwegian = "Norwegian æøå and ÆØÅ";
        try (CsvReader r = new CsvReader(new FileInputStream(getResource(filename)), Charset.forName(charset))) {
            while (r.hasNext()) {
                Row row = r.next();
                //assertEquals("Norwegian chars", norwegian, row.getString("text"));
            }
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    //@Test
    public void readRows() {
        String filename = "countries.csv";
        String charset = "utf-8";
        try (CsvReader r = new CsvReader(new FileInputStream(getResource(filename)), Charset.forName(charset))) {
            int x = 1;
            while (r.hasNext()) {
                r.getRow().getString(0);
            }
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

}
