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
package org.laukvik.csv.io;

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;


public class CsvReaderTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvReaderTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void readMetaData() throws IOException {
        CSV csv = new CSV();
        CsvReader reader = new CsvReader( Charset.forName("utf-8"), null, null );
        reader.readFile( getResource("metadata.csv"), csv );
        assertEquals("Row count", 44, csv.getRowCount());
        assertEquals("Column count", 9, csv.getColumnCount());
    }

    private void readFile(String filename, int requiredColumns, int requiredRows, String charset) throws IOException {
        CSV csv = new CSV();
        CsvReader reader = new CsvReader( Charset.forName(charset), null, null );
        reader.readFile( getResource(filename), csv );
        assertEquals("Row count", requiredRows, csv.getRowCount());
        assertEquals("Column count", requiredColumns, csv.getColumnCount());
    }

    @Test
    public void readAcid() throws IOException {
        readFile("acid.csv", 5, 4, "us-ascii");
    }

    @Test
    public void readQuoteWithCommas() throws IOException {
        readFile("quote_withcomma.csv", 5, 1, "us-ascii");
    }

    @Test
    public void readQuotedEscaped() throws IOException {
        readFile("quote_escaped.csv", 4, 3, "utf-8");
    }

    @Test
    public void readQuotedSingle() throws IOException {
        CSV csv = new CSV();
        CsvReader reader = new CsvReader( Charset.forName("utf-8"), null, CSV.QUOTE_SINGLE);
        reader.readFile( getResource("quote_single.csv"), csv );
        assertEquals("Row count", 2, csv.getRowCount());
        assertEquals("Column count", 3, csv.getColumnCount());
    }

    @Test
    public void readQuotedDouble() throws IOException {
        CSV csv = new CSV();
        CsvReader reader = new CsvReader( Charset.forName("utf-8"), null, CSV.QUOTE_DOUBLE);
        reader.readFile( getResource("quote_double.csv"), csv );
        assertEquals("Row count", 2, csv.getRowCount());
        assertEquals("Column count", 3, csv.getColumnCount());
    }

    @Test
    public void readQuoteNone() throws IOException {
        readFile("quote_none.csv", 5, 4, "us-ascii");
    }

    @Test
    public void readCharset() throws IOException {
        String filename = "charset.csv";
        String charset = "ISO-8859-1";
        String norwegian = "Norwegian æøå and ÆØÅ";
//        try (CsvReader r = new CsvReader( Files.newBufferedReader(getResource(filename).toPath(), Charset.forName(charset)) )) {
//            while (r.hasNext()) {
//                Row row = r.next();
//                //assertEquals("Norwegian chars", norwegian, row.getAsString("text"));
//            }
//        }
//        catch (IOException e) {
//            fail(e.getMessage());
//        }
        CSV csv = new CSV();
        CsvReader reader = new CsvReader( null, null, null );
        reader.readFile( getResource(filename), csv );
        StringColumn sc = (StringColumn) csv.getColumn("text");
        assertEquals("Norwegian chars", norwegian, csv.getRow(0).getString(sc));
    }

    @Test
    public void test() throws IOException {
        CSV csv = new CSV();
        CsvReader reader = new CsvReader( null, null, null );
        reader.readFile( getResource("charset_utf_16_be.csv"), csv );
        assertEquals( Charset.forName("UTF-16BE"), csv.getCharset());
    }

    @Test
    public void detectCharsetUtf8() throws IOException {
//        CsvReader r = new CsvReader(getResource("charset_utf_8.csv"));
//        assertEquals(Charset.forName("utf-8"),r.getMetaData().getCharset());
//        InputStreamReader r = new InputStreamReader(new FileInputStream(getResource("charset_utf_8.csv")));
    }

    @Test
    public void detectCharsetUtf16BE() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("charset_utf_16_be.csv") );
//        assertEquals(Charset.forName("utf-16be"),r.getMetaData().getCharset());
    }

    @Test
    public void detectCharsetUtf16LE() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("charset_utf_16_le.csv") );
//        CsvReader r = new CsvReader(getReaderByResource("charset_utf_16_le.csv"));
//        assertEquals(Charset.forName("utf-16le"),r.getMetaData().getCharset());
    }

    @Test
    public void detectCharsetUtf32BE() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("charset_utf_32_be.csv"));
//        CsvReader r = new CsvReader(getReaderByResource("charset_utf_32_be.csv"));
//        assertEquals(Charset.forName("utf-32be"),r.getMetaData().getCharset());
    }

    @Test
    public void detectCharsetUtf32LE() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("charset_utf_32_le.csv"));
//        CsvReader r = new CsvReader(getReaderByResource("charset_utf_32_le.csv"));
//        assertEquals(Charset.forName("utf-32le"),r.getMetaData().getCharset());
    }

    @Test
    public void detectTab() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("separator_tab.csv") );
//        CsvReader r = new CsvReader(getReaderByResource("separator_tab.csv"));
//        assertEquals(CSV.TAB, r.getColumnSeparatorChar());
    }

    @Test
    public void detectPipe() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("separator_pipe.csv"));
//        CsvReader r = new CsvReader(getReaderByResource("separator_pipe.csv"));
//        assertEquals(CSV.PIPE, r.getColumnSeparatorChar());
    }

    @Test
    public void detectSemi() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("separator_semi.csv"));
//        CsvReader r = new CsvReader(getReaderByResource("separator_semi.csv"));
//        assertEquals(CSV.SEMICOLON, r.getColumnSeparatorChar());
    }

    @Test
    public void detectComma() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("separator_comma.csv") );
//        CsvReader r = new CsvReader(getReaderByResource("separator_comma.csv"));
//        assertEquals(CSV.COMMA, r.getColumnSeparatorChar());
    }
//
//    @Test
//    public void shouldRead() throws Exception {
//        File file = File.createTempFile("Person", ".csv");
//        CSV csv = new CSV();
//        StringColumn first = csv.addStringColumn("First");
//        Column last = csv.addColumn("Last");
//        csv.writeFile(file);
//        CsvReader r = new CsvReader(new BufferedReader(new FileReader(file)), null, null);
//        Assert.assertEquals(2, r.getMetaData().getColumnCount());
//        while (r.hasNext()) {
//            Row row = r.next();
//        }
//    }

}
