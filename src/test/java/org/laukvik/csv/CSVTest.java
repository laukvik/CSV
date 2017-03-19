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

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;
import org.laukvik.csv.columns.BigDecimalColumn;
import org.laukvik.csv.columns.BooleanColumn;
import org.laukvik.csv.columns.ByteColumn;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.DateColumn;
import org.laukvik.csv.columns.DoubleColumn;
import org.laukvik.csv.columns.FloatColumn;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.columns.UrlColumn;
import org.laukvik.csv.query.RowMatcher;
import org.laukvik.csv.query.StringInMatcher;
import org.laukvik.csv.statistics.FrequencyDistribution;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class CSVTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = CSVTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void setAutoDetectSeparator() throws IOException {
        CSV csv = new CSV();
        csv.setAutoDetectSeparator(true);
        csv.setAutoDetectQuote(true);
        csv.readFile(getResource("separator_pipe.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(1,csv.getRowCount());
        assertTrue(csv.isAutoDetectQuote());
        assertTrue(csv.isAutoDetectSeparator());
    }

    @Test
    public void autodetection(){
        CSV csv = new CSV();
        assertEquals(true, csv.isAutoDetectCharset());
        assertEquals(true, csv.isAutoDetectQuote());
        assertEquals(true, csv.isAutoDetectSeparator());
        csv.setAutoDetectCharset(false);
        csv.setAutoDetectQuote(false);
        csv.setAutoDetectSeparator(false);
        assertEquals(false, csv.isAutoDetectCharset());
        assertEquals(false, csv.isAutoDetectQuote());
        assertEquals(false, csv.isAutoDetectSeparator());
    }

    @Test
    public void settersGetters(){
        CSV csv = new CSV();

        Charset cs = Charset.forName("utf-8");

        assertEquals( Charset.defaultCharset(), csv.getCharset());
        assertNull(  csv.getSeparatorChar());
        assertNull( csv.getQuoteChar());

        csv.setCharset(cs);
        csv.setQuoteChar(CSV.QUOTE_SINGLE);
        csv.setSeparator(CSV.PIPE);

        assertEquals(cs, csv.getCharset());
        assertEquals( (Character) CSV.PIPE, csv.getSeparatorChar());
        assertEquals( (Character) CSV.QUOTE_SINGLE, csv.getQuoteChar());
    }

    // ------ Columns ------

    @Test
    public void addColumnByType() {
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
        assertNotNull(bdc);
        assertNotNull(bc);
        assertNotNull(byteC);
        assertNotNull(datec);
        assertNotNull(dc);
        assertNotNull(fc);
        assertNotNull(ic);
        assertNotNull(sc);
        assertNotNull(uc);
    }

    @Test
    public void insertColumns() throws IOException {
        CSV csv = new CSV(getResource("noheaders.csv"));
        assertEquals(1, csv.getRowCount());
        assertEquals(2, csv.getColumnCount());
        csv.insertColumns();
        assertEquals(2, csv.getRowCount());
        assertEquals(2, csv.getColumnCount());
    }

    @Test
    public void removeColumn() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        StringColumn presidency = (StringColumn) csv.getColumn(0);
        int cols = csv.getColumnCount();
        csv.removeColumn(presidency);
        assertEquals(cols - 1, csv.getColumnCount());
        csv.removeColumn(2);
        assertEquals(cols - 2, csv.getColumnCount());
    }

    @Test
    public void fireColumnMoved() throws IOException {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("first");
        StringColumn last = csv.addStringColumn("last");
        csv.moveColumn(0, 1);
        assertEquals(first, csv.getColumn(1));
        assertEquals(last, csv.getColumn(0));
    }

    // ------ Rows ------

    @Test
    public void buildDistinctValues() throws IOException {
        CSV csv = new CSV();
        StringColumn president = csv.addStringColumn("President");
        csv.addRow().setString(president, "Hillary");
        csv.addRow().setString(president, "Barak");
        csv.addRow().setString(president, "Hillary");
        csv.addRow().setString(president, "Clinton");
        Set<String> values = csv.buildDistinctValues(0);
        Assert.assertEquals(3, values.size());

    }

    @Test
    public void addRowAt() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        StringColumn president = csv.addStringColumn("President");
        Row r = csv.addRow(10);
        assertEquals(r, csv.getRow(10));
    }

    @Test
    public void shouldSwapRows() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        Row r0 = csv.getRow(0);
        Row r10 = csv.getRow(10);
        csv.swapRows(0, 10);
        assertEquals(10, csv.indexOf(r0));
        assertEquals(0, csv.indexOf(r10));
    }

    @Test
    public void shouldMoveRows() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        Row r9 = csv.getRow(9);
        csv.moveRow(0, 9);
        assertEquals(0, csv.indexOf(r9));
    }

    @Test
    public void shouldRemoveRow() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        assertEquals(44, csv.getRowCount());
        csv.removeRow(1);
        assertEquals(43, csv.getRowCount());
    }

    @Test
    public void shouldRemoveRowsBetween() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        csv.removeRowsBetween(0, 9);
        assertEquals(34, csv.getRowCount());
    }

    @Test
    public void indexOf() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        Row r = csv.getRow(10);
        assertEquals(10, csv.indexOf(r));
        assertEquals(0, csv.indexOf("Presidency"));
        assertEquals(-1, csv.indexOf("DontExist"));
    }

//    @Test
//    public void findRows() throws IOException {
//        CSV csv = new CSV();
//        csv.readFile(getResource("countries.csv"));
//        assertEquals(249, csv.getRowCount());
//    }

    // ------ div ------

    @Test
    public void clear() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        csv.clear();
        assertEquals(0, csv.getRowCount());
    }

    @Test
    public void removeRows() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        csv.removeRows();
        assertEquals(0, csv.getRowCount());
    }



    // ------ Reading ------

    @Test
    public void getRow() throws IOException {
        CSV csv = new CSV(getResource("presidents.csv"));
        assertNull(csv.getRow(5000));
    }


    @Test
    public void readUsingConstructor() throws IOException {
        CSV csv = new CSV(getResource("presidents.csv"));
        assertNotNull(csv.getFile());
        assertSame(44, csv.getRowCount());
    }

    @Test
    public void iterator() throws IOException {
        CSV csv = new CSV();
        for (Row r : csv.getRows()) {
        }
    }

    @Test
    public void createNew() {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        Row r1 = csv.addRow().setString(first, "Bill").setString(last, "Gates");
        Row r2 = csv.addRow().setString(first, "Steve").setString(last, "Jobs");
//        assertEquals("Row1", 2, r1.getValues().size());
//        assertEquals("Row1", 2, r2.getValues().size());
        assertSame("RowCount", csv.getRowCount(), 2);
    }

    @Test
    public void writeFile() throws Exception {
        File file = File.createTempFile("ShouldWrite", ".csv");
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        assertSame(csv.getColumnCount(), 2);
        assertEquals("First should be 0", 0, csv.indexOf(first));
        assertEquals("Last should be 1", 1, csv.indexOf(last));
        csv.addRow().setString(first, "Bill").setString(last, "Gates");
        csv.addRow().setString(first, "Steve").setString(last, "Jobs");
        assertSame("RowCount", csv.getRowCount(), 2);
        try {
            csv.writeFile(file);
        }
        catch (IOException e) {
            fail(e.getMessage());
        }
        CSV csv2 = new CSV();
        csv2.readFile(file);
        assertEquals(2, csv2.getRowCount());
    }

    public void writeXML() throws Exception {
        File file = File.createTempFile("writeXML", ".xml");
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        csv.addRow().setString(first, "Bill").setString(last, "Gates");
        csv.writeXML(file);
        assertNotNull(file);
    }

    @Test
    public void writeJSON() throws Exception {
        File file = File.createTempFile("writeJSON", ".json");
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        csv.addRow().setString(first, "Bill").setString(last, "Gates");
        csv.addRow().setString(first, "Steve").setString(last, "Jobs");
        csv.writeJSON(file);
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader(file));
        JSONArray jsonArray = (JSONArray) obj;
        assertNotNull(jsonArray);
    }

    @Test
    public void writeHTML() throws Exception {
        File file = File.createTempFile("writeJSON", ".json");
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        csv.addRow().setString(first, "Bill").setString(last, "Gates");
        csv.writeHtml(file);
        assertNotNull(file);
    }

    @Test
    public void writeResourceBundle() throws Exception {
        File file = File.createTempFile("writeJSON", ".json");
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        csv.addRow().setString(first, "Bill").setString(last, "Gates");
        csv.writeResourceBundle(file);
        assertNotNull(file);
    }

    @Test
    public void buildFrequencyDistribution() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        csv.addRow().setString(first, "Gates");
        csv.addRow().setString(first, "Jobs");
        FrequencyDistribution fd1 = csv.buildFrequencyDistribution(first);
        FrequencyDistribution fd2 = csv.buildFrequencyDistribution(0);
        assertNotNull(fd1);
        assertNotNull(fd2);
    }

    @Test
    public void readEscaped() {
        try {

            CSV csv = new CSV();
            csv.readFile(getResource("quote_escaped.csv"));

            StringColumn playerName = (StringColumn) csv.getColumn(0);
            StringColumn position = (StringColumn) csv.getColumn(1);
            StringColumn nicknames = (StringColumn) csv.getColumn(2);
            StringColumn yearsActive = (StringColumn) csv.getColumn(3);

            assertEquals("Player Name", "Player Name", playerName.getName());
            assertEquals("Position", "Position", position.getName());
            assertEquals("Nicknames", "Nicknames", nicknames.getName());
            assertEquals("Years Active", "Years Active", yearsActive.getName());

            assertSame("RowCount", 3, csv.getRowCount());
            assertSame("ColumnCount", 4, csv.getColumnCount());

            Row r = csv.getRow(0);

            assertEquals("Skippy", "Skippy Peterson", r.getString(playerName));
            assertEquals("First Base", "First Base", r.getString(position));
            assertEquals("1908-1913", "1908-1913", r.getString(yearsActive));

            String blueDog = "\"Blue Dog\", \"The Magician\"";

            assertEquals("Blue Dog The Magician", "\"Blue Dog\", \"The Magician\"", r.getString(nicknames));

        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readAcid() {
        try {
            CSV csv = new CSV();
            csv.readFile(getResource("acid.csv"));

            assertEquals("Year", "Year", csv.getColumn(0).getName());
            assertEquals("Make", "Make", csv.getColumn(1).getName());
            assertEquals("Model", "Model", csv.getColumn(2).getName());
            assertEquals("Description", "Description", csv.getColumn(3).getName());
            assertEquals("Price", "Price", csv.getColumn(4).getName());

            assertSame("ColumnCount", 5, csv.getColumnCount());
            assertSame("RowCount", 4, csv.getRowCount());

            Row r = csv.getRow(1);

            StringColumn model = (StringColumn) csv.getColumn("Model");

            assertEquals("Venture", "Venture \"Extended Edition\"", r.getString(model));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readEmbeddedCommas() {
        try {
            CSV csv = new CSV();
            File f = getResource("quote_withcomma.csv");
            if (!f.exists()) {
                fail("Could not readFile test file:");
            }

            csv.readFile(getResource("quote_withcomma.csv"));

            assertEquals("Year", "Year", csv.getColumn(0).getName());
            assertEquals("Make", "Make", csv.getColumn(1).getName());
            assertEquals("Model", "Model", csv.getColumn(2).getName());
            assertEquals("Description", "Description", csv.getColumn(3).getName());
            assertEquals("Price", "Price", csv.getColumn(4).getName());

            assertSame("ColumnCount", 5, csv.getColumnCount());
            assertSame("RowCount", 1, csv.getRowCount());

            Row r = csv.getRow(0);
            StringColumn desc = (StringColumn) csv.getColumn("Description");

            assertEquals("ac, abs, moon", "ac, abs, moon", r.getString(desc));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readUnqouted() {
        try {
            // Name,Class,Dorm,Room,GPA
            CSV csv = new CSV();
            csv.readFile(getResource("quote_none.csv"));
            assertEquals("Name", "Name", csv.getColumn(0).getName());
            assertEquals("Class", "Class", csv.getColumn(1).getName());
            assertEquals("Dorm", "Dorm", csv.getColumn(2).getName());
            assertEquals("Room", "Room", csv.getColumn(3).getName());
            assertEquals("GPA", "GPA", csv.getColumn(4).getName());

            assertSame("ColumnCount", 5, csv.getColumnCount());
            assertSame("RowCount", 4, csv.getRowCount());

            Row r = csv.getRow(3);

            StringColumn col = (StringColumn) csv.getColumn("GPA");

            assertEquals("GPA", "3.48", r.getString(col));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readInvalid() throws IOException {
            CSV csv = new CSV();
            csv.readFile(getResource("invalid.csv"));
        assertEquals("First", "First", csv.getColumn(0).getName());
        assertEquals("Last", "Last", csv.getColumn(1).getName());
        assertSame("ColumnCount", 2, csv.getColumnCount());
    }

    @Test
    public void getColumn() throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("separator_comma.csv"));
        assertEquals(null, csv.getColumn("DOESNT_EXIST"));
    }

    @Test
    public void writeXml() throws IOException {
        CSV csv = new CSV();
        csv.addColumn("first");
        File file = File.createTempFile("csvxmltest", "xml");
        csv.writeXML(file);
    }

    @Test
    public void getRowsByMatchers() throws IOException {
        CSV csv = new CSV();
        csv.readFile( getResource("separator_comma.csv")  );
        StringColumn c = (StringColumn) csv.getColumn("Heading1");

        List<RowMatcher> matchers = new ArrayList<>();
        matchers.add( new StringInMatcher(c, "First"));

        csv.getRowsByMatchers(matchers);
    }

    @Test
    public void readWithoutSeparator() throws IOException {
        CSV csv = new CSV();
        csv.readFile( getResource("separator_comma.csv")  );
        assertEquals(3, csv.getColumnCount());
        assertEquals(2, csv.getRowCount());
    }

    @Test
    public void readCommaSeparated() throws IOException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.COMMA);
        csv.readFile(getResource("separator_comma.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(2, csv.getRowCount());
    }

    @Test
    public void readSemiColonSeparated() throws IOException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.SEMICOLON);
        csv.readFile(getResource("separator_semi.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(1, csv.getRowCount());
    }

    @Test
    public void readTabSeparated() throws IOException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.TAB);
        csv.readFile(getResource("separator_tab.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(1,csv.getRowCount());

        csv = new CSV();
//        assertEquals((Character)CSV.TAB, csv.getSeparatorChar());
        csv.readFile(getResource("separator_tab.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(1,csv.getRowCount());
    }

    @Test
    public void readPipeSeparated() throws IOException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.PIPE);
        csv.readFile(getResource("separator_pipe.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(1,csv.getRowCount());
    }

    @Test
    public void readSingleQuote() throws IOException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.COMMA);
        csv.setQuoteChar(CSV.QUOTE_SINGLE);
        csv.setAutoDetectCharset(true);
        csv.readFile(getResource("quote_single.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(2,csv.getRowCount());
        StringColumn sc = (StringColumn) csv.getColumn(0);
        assertTrue(csv.isAutoDetectCharset());
        assertEquals("Heading1", csv.getColumn(0).getName());
        assertEquals("first", csv.getRow(0).getString(sc));
    }

    @Test
    public void readDoubleQuote() throws IOException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.COMMA);
        csv.setQuoteChar(CSV.QUOTE_DOUBLE);
        csv.readFile(getResource("quote_double.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(2,csv.getRowCount());
        StringColumn sc = (StringColumn) csv.getColumn(0);
        assertEquals("Heading1", csv.getColumn(0).getName());
        assertEquals("first", csv.getRow(0).getString(sc));
    }

    @Test
    public void shouldReadWords() throws IOException {
        CSV csv = new CSV();
        csv.readWordCountFile( getResource("words.txt") );
        assertEquals(2, csv.getColumnCount());
        assertEquals(8, csv.getRowCount());
    }

    @Test
    public void shouldFireFileEvent() throws Exception {
        CSV csv = new CSV();
        FileListen l = new FileListen();
        csv.addFileListener(l);
        csv.readFile(getResource("presidents.csv"));
        assertEquals(1, l.beginRead);
        assertEquals(1, l.finishRead);
        assertEquals(0, l.beginWrite);
        assertEquals(0, l.finishWrite);
        csv.writeFile(File.createTempFile("presidents", ".csv"));
        assertEquals(1, l.beginRead);
        assertEquals(1, l.finishRead);
        assertEquals(1, l.beginWrite);
        assertEquals(1, l.finishWrite);
    }

    @Test
    public void shouldRemoveFileListener() throws Exception {
        CSV csv = new CSV();
        FileListen l = new FileListen();
        csv.addFileListener(l);
        csv.removeFileListener(l);
        csv.readFile(getResource("presidents.csv"));
        assertEquals(0, l.beginRead);
        assertEquals(0, l.finishRead);
        assertEquals(0, l.beginWrite);
        assertEquals(0, l.finishWrite);
    }

    @Test
    public void addChangeListener(){
        CSV csv = new CSV();
        ChangeListen cl = new ChangeListen();
        csv.addChangeListener(cl);
        StringColumn first = (StringColumn) csv.addColumn("First");
        assertEquals(1, cl.columnCreated);
        first.setName("first");
        assertEquals(1, cl.columnUpdated);
        StringColumn last = (StringColumn) csv.addColumn("Last");
        assertEquals(2, cl.columnCreated);
        csv.removeColumn(first);
        assertEquals(1, cl.columnRemoved);

        csv.addRow();
        assertEquals(1, cl.rowCreated);
        csv.removeRow(0);
        assertEquals(1, cl.rowRemoved);

        csv.addRow();
        csv.addRow();
        csv.moveRow(0,1);
        assertEquals(1, cl.rowMoved);
        csv.removeRows();
        assertEquals(1, cl.rowsRemoved);

        csv.addColumn("Test");
        csv.moveColumn(0,1);
        assertEquals(1, cl.columnMoved);

        csv.removeChangeListener(cl);
        csv.addColumn("email");
        assertEquals(3, cl.columnCreated);
    }

    static class Employee {
        public String name;
        public int age;
        public boolean isWoman;

        public Employee(final String name, final int age, final boolean isWoman) {
            this.name = name;
            this.age = age;
            this.isWoman = isWoman;
        }
    }

    // ------ Events -----
    class ChangeListen implements ChangeListener{

        public int columnCreated;
        public int columnUpdated;
        public int columnRemoved;
        public int columnMoved;
        public int rowRemoved;
        public int rowCreated;
        public int rowMoved;
        public int rowsRemoved;
        public int cellUpdated;

        @Override
        public void columnCreated(Column column) {
            columnCreated++;
        }

        @Override
        public void columnUpdated(Column column) {
            columnUpdated++;
        }

        @Override
        public void columnRemoved(int columnIndex) {
            columnRemoved++;
        }

        @Override
        public void columnMoved(int fromRowIndex, int toRowIndex) {
            columnMoved++;
        }

        @Override
        public void rowRemoved(int rowIndex, Row row) {
            rowRemoved++;
        }

        @Override
        public void rowCreated(int rowIndex, Row row) {
            rowCreated++;
        }

        @Override
        public void rowMoved(int fromRowIndex, int toRowIndex) {
            rowMoved++;
        }

        @Override
        public void rowsRemoved(int fromRowIndex, int toRowIndex) {
            rowsRemoved++;
        }

        @Override
        public void cellUpdated(int columnIndex, int rowIndex) {
            cellUpdated++;
        }
    }

    class FileListen implements FileListener {

        int beginRead = 0;
        int finishRead = 0;
        int beginWrite = 0;
        int finishWrite = 0;

        @Override
        public void beginRead(File file) {
            beginRead++;
        }

        @Override
        public void finishRead(File file) {
            finishRead++;
        }

        @Override
        public void beginWrite(File file) {
            beginWrite++;
        }

        @Override
        public void finishWrite(File file) {
            finishWrite++;
        }
    }


}
