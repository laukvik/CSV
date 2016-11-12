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

import java.io.File;


public class CsvReaderTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvReaderTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

//    @Test
//    public void readMetaData() throws IOException {
//        String filename = "metadata.csv";
//        String charset = "utf-8";
//        String pattern = "yyyy-MM-dd HH:mm:ss";
//        SimpleDateFormat f = new SimpleDateFormat(pattern);
//        long millis = 1322018752992L;
//        Date date = new Date(millis);
//
//        try (CsvReader reader = new CsvReader( Files.newBufferedReader(getResource(filename).toPath(), Charset.forName(charset)), null, null )) {
//            int rows = 0;
//            while (reader.hasNext()) {
//                rows++;
//                Row r = reader.getRow();
//            }
//            assertEquals("Row count", 44, rows);
//            assertEquals("Column count", 9, reader.getMetaData().getColumnCount());
//        }
//        catch (IOException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    private void readFile(String filename, int requiredColumns, int requiredRows, String charset) throws IOException {
//        try (CsvReader reader = new CsvReader( Files.newBufferedReader(getResource(filename).toPath(), Charset.forName(charset)), null, null )) {
//            int rows = 0;
//            while (reader.hasNext()) {
//                Row r = reader.getRow();
//                rows++;
//            }
//            assertEquals("Row count", rows, requiredRows);
//            assertEquals("Column count", reader.getMetaData().getColumnCount(), requiredColumns);
//        }
//        catch (IOException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @Test
//    public void readAcid() throws IOException {
//        readFile("acid.csv", 5, 4, "us-ascii");
//    }
//
//    @Test
//    public void readQuoteWithCommas() throws IOException {
//        readFile("quote_withcomma.csv", 5, 1, "us-ascii");
//    }
//
//    @Test
//    public void readQuotedEscaped() throws IOException {
//        readFile("quote_escaped.csv", 4, 3, "utf-8");
//    }
//
//    @Test
//    public void readQuotedSingle() throws IOException {
//        try (CsvReader reader = new CsvReader( Files.newBufferedReader(getResource("quote_single.csv").toPath()))) {
//            int y=0;
//            while (reader.hasNext()) {
//                reader.next();
//                y++;
//            }
//            assertEquals(2, y);
//            assertEquals(3, reader.getMetaData().getColumnCount());
//        }
//        catch (IOException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @Test
//    public void readQuotedDouble() throws IOException {
//        try (CsvReader reader = new CsvReader( Files.newBufferedReader(getResource("quote_double.csv").toPath()))) {
//            int y=0;
//            while (reader.hasNext()) {
//                reader.next();
//                y++;
//            }
//            assertEquals(2, y);
//            assertEquals(3, reader.getMetaData().getColumnCount());
//        }
//        catch (IOException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @Test
//    public void readQuoteNone() throws IOException {
//        readFile("quote_none.csv", 5, 4, "us-ascii");
//    }
//
//
//
//
//    @Test
//    public void readWithIterator() {
//        String filename = "acid.csv";
//        String charset = "utf-8";
//        try (CsvReader reader = new CsvReader( Files.newBufferedReader(getResource(filename).toPath(), Charset.forName(charset)), null, null )) {
//            // Find columns
//            StringColumn col1 = (StringColumn) reader.getMetaData().getColumn(0);
//            StringColumn col2 = (StringColumn) reader.getMetaData().getColumn(1);
//            StringColumn col3 = (StringColumn) reader.getMetaData().getColumn(2);
//            StringColumn col4 = (StringColumn) reader.getMetaData().getColumn(3);
//            while (reader.hasNext()) {
//                Row row = reader.next();
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//    }
//
//    @Test
//    public void readCharset() {
//        String filename = "charset.csv";
//        String charset = "ISO-8859-1";
//        String norwegian = "Norwegian æøå and ÆØÅ";
//        try (CsvReader r = new CsvReader( Files.newBufferedReader(getResource(filename).toPath(), Charset.forName(charset)) )) {
//            while (r.hasNext()) {
//                Row row = r.next();
//                //assertEquals("Norwegian chars", norwegian, row.getAsString("text"));
//            }
//        }
//        catch (IOException e) {
//            fail(e.getMessage());
//        }
//    }
//
//    @Test
//    public void detectCharsetUtf8() throws IOException {
////        CsvReader r = new CsvReader(getResource("charset_utf_8.csv"));
////        assertEquals(Charset.forName("utf-8"),r.getMetaData().getCharset());
//
//        InputStreamReader r = new InputStreamReader(new FileInputStream(getResource("charset_utf_8.csv")));
//    }
//
//    public BufferedReader getReaderByResource(String resourceName) throws FileNotFoundException {
//        return new BufferedReader(new FileReader(getResource(resourceName)));
//    }
//
////    @Test
////    public void detectCharsetUtf16BE() throws IOException {
////        CsvReader r = new CsvReader( getReaderByResource("charset_utf_16_be.csv") );
////        assertEquals(Charset.forName("utf-16be"),r.getMetaData().getCharset());
////    }
////
////    @Test
////    public void detectCharsetUtf16LE() throws IOException {
////        CsvReader r = new CsvReader( getReaderByResource("charset_utf_16_le.csv") );
//////        CsvReader r = new CsvReader(getReaderByResource("charset_utf_16_le.csv"));
////        assertEquals(Charset.forName("utf-16le"),r.getMetaData().getCharset());
////    }
////
////    @Test
////    public void detectCharsetUtf32BE() throws IOException {
////        CsvReader r = new CsvReader( getReaderByResource("charset_utf_32_be.csv"));
//////        CsvReader r = new CsvReader(getReaderByResource("charset_utf_32_be.csv"));
////        assertEquals(Charset.forName("utf-32be"),r.getMetaData().getCharset());
////    }
////
////    @Test
////    public void detectCharsetUtf32LE() throws IOException {
////        CsvReader r = new CsvReader( getReaderByResource("charset_utf_32_le.csv"));
//////        CsvReader r = new CsvReader(getReaderByResource("charset_utf_32_le.csv"));
////        assertEquals(Charset.forName("utf-32le"),r.getMetaData().getCharset());
////    }
//
//    @Test
//    public void detectTab() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("separator_tab.csv") );
////        CsvReader r = new CsvReader(getReaderByResource("separator_tab.csv"));
//        assertEquals(CSV.TAB, r.getColumnSeparatorChar());
//    }
//
//    @Test
//    public void detectPipe() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("separator_pipe.csv"));
////        CsvReader r = new CsvReader(getReaderByResource("separator_pipe.csv"));
//        assertEquals(CSV.PIPE, r.getColumnSeparatorChar());
//    }
//
//    @Test
//    public void detectSemi() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("separator_semi.csv"));
////        CsvReader r = new CsvReader(getReaderByResource("separator_semi.csv"));
//        assertEquals(CSV.SEMICOLON, r.getColumnSeparatorChar());
//    }
//
//    @Test
//    public void detectComma() throws IOException {
//        CsvReader r = new CsvReader( getReaderByResource("separator_comma.csv") );
////        CsvReader r = new CsvReader(getReaderByResource("separator_comma.csv"));
//        assertEquals(CSV.COMMA, r.getColumnSeparatorChar());
//    }
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
