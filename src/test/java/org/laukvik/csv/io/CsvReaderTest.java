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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.SortedMap;

import static org.junit.Assert.assertEquals;

public class CsvReaderTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvReaderTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    public void readFile(File file){
        try (FileReader inputStream = new FileReader(file)){
            int c;
            while ((c = inputStream.read()) != -1) {
                System.out.print( (char)c);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Charset guessCharset(final File file){
        SortedMap<String, Charset> map = Charset.availableCharsets();
        for (Charset iso : map.values()){
//            System.out.print("Trying " + iso.displayName() + "...");
            try {
                CSV csv = new CSV();
                csv.setCharset(iso);
                CsvReader reader = new CsvReader( iso, null, null );
                reader.readFile(file, csv);
//                System.out.println("Success");
                return iso;
            } catch (CsvReaderException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Test
    public void readMetaData() throws CsvReaderException {
        CSV csv = new CSV();
        CsvReader reader = new CsvReader( Charset.forName("utf-8"), null, null );
        reader.readFile( getResource("metadata.csv"), csv );
        assertEquals("Row addValue", 44, csv.getRowCount());
        assertEquals("Column addValue", 12, csv.getColumnCount());
    }

    private void readFile(String filename, int requiredColumns, int requiredRows, String charset) throws CsvReaderException {
        CSV csv = new CSV();
        CsvReader reader = new CsvReader( Charset.forName(charset), null, null );
        reader.readFile( getResource(filename), csv );
        assertEquals("Row addValue", requiredRows, csv.getRowCount());
        assertEquals("Column addValue", requiredColumns, csv.getColumnCount());
    }

    @Test
    public void readAcid() throws CsvReaderException {
        readFile("acid.csv", 5, 4, "us-ascii");
    }

    @Test
    public void readQuoteWithCommas() throws CsvReaderException {
        readFile("quote_withcomma.csv", 5, 1, "us-ascii");
    }

    @Test
    public void readQuotedEscaped() throws CsvReaderException {
        readFile("quote_escaped.csv", 4, 3, "utf-8");
    }

    @Test
    public void readQuotedSingle() throws CsvReaderException {
        CSV csv = new CSV();
        CsvReader reader = new CsvReader( Charset.forName("utf-8"), null, CSV.QUOTE_SINGLE);
        reader.readFile( getResource("quote_single.csv"), csv );
        assertEquals("Row addValue", 2, csv.getRowCount());
        assertEquals("Column addValue", 3, csv.getColumnCount());
    }

    @Test
    public void readQuotedDouble() throws CsvReaderException {
        CSV csv = new CSV();
        CsvReader reader = new CsvReader( Charset.forName("utf-8"), null, CSV.QUOTE_DOUBLE);
        reader.readFile( getResource("quote_double.csv"), csv );
        assertEquals("Row addValue", 2, csv.getRowCount());
        assertEquals("Column addValue", 3, csv.getColumnCount());
    }

    @Test
    public void readQuoteNone() throws CsvReaderException {
        readFile("quote_none.csv", 5, 4, "us-ascii");
    }

    @Test
    public void readCharset() throws CsvReaderException {
        Charset charset = BOM.UTF8.getCharset();
        String norwegian = "Norwegian æøå and ÆØÅ";
        CSV csv = new CSV();
        CsvReader reader = new CsvReader(charset, null, null );
        reader.readFile( getResource("charset.csv"), csv );
        StringColumn sc = (StringColumn) csv.getColumn("text");
        assertEquals("Norwegian chars", norwegian, csv.getRow(0).get(sc));

        guessCharset(getResource("charset.csv"));
    }

    public Charset findCharset(String filename) throws CsvReaderException {
        CSV csv = new CSV();
        CsvReader reader = new CsvReader( null, null, null );
        reader.readFile( getResource(filename), csv );
        return csv.getCharset();
    }

    @Test
    public void detectCharsetUtf8() throws CsvReaderException {
        assertEquals( BOM.UTF8.getCharset(), findCharset("charset_utf_8.csv"));
    }

    @Test
    public void detectCharsetUtf16BE() throws CsvReaderException {
        assertEquals( BOM.UTF16BE.getCharset(), findCharset("charset_utf_16_be.csv"));
    }

    @Test
    public void detectCharsetUtf16LE() throws CsvReaderException {
        assertEquals( BOM.UTF16LE.getCharset(), findCharset("charset_utf_16_le.csv"));
    }

    @Test
    public void detectCharsetUtf32BE() throws CsvReaderException {
        assertEquals( BOM.UTF32BE.getCharset(), findCharset("charset_utf_32_be.csv"));
    }

    @Test
    public void detectCharsetUtf32LE() throws CsvReaderException {
        assertEquals( BOM.UTF32LE.getCharset(), findCharset("charset_utf_32_le.csv"));
    }

    public Character findSeparator(String filename) throws CsvReaderException {
        CSV csv = new CSV();
        CsvReader r = new CsvReader(null, null, null);
        r.readFile(getResource(filename), csv);
        return csv.getSeparatorChar();
    }

    @Test
    public void detectTab() throws CsvReaderException {
        assertEquals((Character)CSV.TAB, findSeparator("separator_tab.csv"));
    }

    @Test
    public void detectPipe() throws CsvReaderException {
        assertEquals((Character)CSV.PIPE, findSeparator("separator_pipe.csv"));
    }

    @Test
    public void detectSemi() throws CsvReaderException {
        assertEquals((Character)CSV.SEMICOLON, findSeparator("separator_semi.csv"));
    }

    @Test
    public void detectComma() throws CsvReaderException {
        assertEquals((Character)CSV.COMMA, findSeparator("separator_comma.csv"));
    }

}
