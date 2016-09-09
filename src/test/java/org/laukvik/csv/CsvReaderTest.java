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
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.io.CsvReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class CsvReaderTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvReaderTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void readMetaData() {
        String filename = "metadata.csv";
        String charset = "utf-8";
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat f = new SimpleDateFormat(pattern);
        long millis = 1322018752992L;
        Date date = new Date(millis);
        try (CsvReader reader = new CsvReader(getResource(filename), Charset.forName(charset))) {
            int rows = 0;
            while (reader.hasNext()) {
                rows++;
                Row r = reader.getRow();
                System.out.println( rows + " " );

            }
            assertEquals("Row count", 44, rows);
            assertEquals("Column count", 9, reader.getMetaData().getColumnCount());
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    private void readFile(String filename, int requiredColumns, int requiredRows, String charset) {
        try (CsvReader reader = new CsvReader(getResource(filename), charset == null ? null : Charset.forName(charset) )) {
            int rows = 0;
            while (reader.hasNext()) {
                Row r = reader.getRow();
//                assertSame("Column count for row " + (rows + 1) + ": ", requiredColumns, r.getValues().size());
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
    public void readQuoteWithCommas() {
        readFile("quote_withcomma.csv", 5, 1, "us-ascii");
    }

    @Test
    public void readQuotedEscaped() {
        readFile("quote_escaped.csv", 4, 3, "utf-8");
    }

    @Test
    public void readQuotedSingle() {
        readFile("quote_single.csv", 3, 2, null);
    }

    @Test
    public void readQuotedDouble() {
        readFile("quote_double.csv", 3, 2, null);
    }

    @Test
    public void readQuoteNone() {
        readFile("quote_none.csv", 5, 4, "us-ascii");
    }




    @Test
    public void readWithIterator() {
        String filename = "acid.csv";
        String charset = "utf-8";
        try (CsvReader reader = new CsvReader(getResource(filename), Charset.forName(charset))) {
            // Find columns
            StringColumn col1 = (StringColumn) reader.getMetaData().getColumn(0);
            StringColumn col2 = (StringColumn) reader.getMetaData().getColumn(1);
            StringColumn col3 = (StringColumn) reader.getMetaData().getColumn(2);
            StringColumn col4 = (StringColumn) reader.getMetaData().getColumn(3);
            while (reader.hasNext()) {
                Row row = reader.next();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

    @Test
    public void readCharset() {
        String filename = "charset.csv";
        String charset = "ISO-8859-1";
        String norwegian = "Norwegian æøå and ÆØÅ";
        try (CsvReader r = new CsvReader(getResource(filename), Charset.forName(charset))) {
            while (r.hasNext()) {
                Row row = r.next();
                //assertEquals("Norwegian chars", norwegian, row.getAsString("text"));
            }
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void detectCharsetUtf8() throws IOException {
        CsvReader r = new CsvReader(getResource("charset_utf_8.csv"));
        assertEquals(Charset.forName("utf-8"),r.getMetaData().getCharset());
    }

    @Test
    public void detectCharsetUtf16BE() throws IOException {
        CsvReader r = new CsvReader(getResource("charset_utf_16_be.csv"));
        assertEquals(Charset.forName("utf-16be"),r.getMetaData().getCharset());
    }

    @Test
    public void detectCharsetUtf16LE() throws IOException {
        CsvReader r = new CsvReader(getResource("charset_utf_16_le.csv"));
        assertEquals(Charset.forName("utf-16le"),r.getMetaData().getCharset());
    }

    @Test
    public void detectCharsetUtf32BE() throws IOException {
        CsvReader r = new CsvReader(getResource("charset_utf_32_be.csv"));
        assertEquals(Charset.forName("utf-32be"),r.getMetaData().getCharset());
    }

    @Test
    public void detectCharsetUtf32LE() throws IOException {
        CsvReader r = new CsvReader(getResource("charset_utf_32_le.csv"));
        assertEquals(Charset.forName("utf-32le"),r.getMetaData().getCharset());
    }

    @Test
    public void detectCharsetLatin() throws IOException {
        CsvReader r = new CsvReader(getResource("charset_latin1.csv"));
        assertEquals(Charset.forName("latin1"),r.getMetaData().getCharset());
    }

}
