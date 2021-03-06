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

import no.laukvik.csv.columns.*;
import no.laukvik.csv.io.CsvReaderException;
import no.laukvik.csv.io.CsvWriterException;
import no.laukvik.csv.query.StringInMatcher;
import no.laukvik.csv.query.ValueMatcher;
import no.laukvik.csv.statistics.FrequencyDistribution;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class CSVTest {

    private static File getResource(String filename) {
        ClassLoader classLoader = CSVTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void appendFile() throws CsvReaderException {
        CSV csv = new CSV();
        IntegerColumn presidency = csv.addIntegerColumn("Presidency");
        StringColumn president = csv.addStringColumn("President");
        StringColumn wikipediaEntry = csv.addStringColumn("Wikipedia Entry");
        DateColumn tookOffice = csv.addDateColumn("Took office");
        DateColumn leftOffice = csv.addDateColumn("Left office");
        StringColumn party = csv.addStringColumn("Party");
        StringColumn portrait = csv.addStringColumn("Portrait");
        StringColumn thumbnail = csv.addStringColumn("Thumbnail");
        StringColumn homeState = csv.addStringColumn("Home State");
        csv.appendFile(getResource("presidents.csv"), 1);
        csv.appendFile(getResource("presidents.csv"));
        long count = csv.stream().count();
        assertEquals(88, count);
    }

    private static boolean xsd_valid(File file) {
        File schemaFile = new File("src/main/resources/csv.xsd"); // etc.
        Source xmlFile = new StreamSource(file);
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        try {
            Schema schema = schemaFactory.newSchema(schemaFile);
            Validator validator = schema.newValidator();
            validator.validate(xmlFile);
            return true;
        } catch (SAXException | IOException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    @Test
    public void setAutoDetectSeparator() throws CsvReaderException {
        CSV csv = new CSV();
        csv.setAutoDetectSeparator(true);
        csv.setAutoDetectQuote(true);
        csv.readFile(getResource("separator_pipe.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(1, csv.getRowCount());
        assertTrue(csv.isAutoDetectQuote());
        assertTrue(csv.isAutoDetectSeparator());
    }

    @Test
    public void autodetection() {
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

    // ------ Columns ------

    @Test
    public void settersGetters() {
        CSV csv = new CSV();

        Charset cs = Charset.forName("utf-8");

        assertEquals(Charset.defaultCharset(), csv.getCharset());
        assertNull(csv.getSeparatorChar());
        assertNull(csv.getQuoteChar());

        csv.setCharset(cs);
        csv.setQuoteChar(CSV.QUOTE_SINGLE);
        csv.setSeparator(CSV.PIPE);

        assertEquals(cs, csv.getCharset());
        assertEquals((Character) CSV.PIPE, csv.getSeparatorChar());
        assertEquals((Character) CSV.QUOTE_SINGLE, csv.getQuoteChar());
    }

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
        LocalDateColumn ldc = csv.addLocalDateColumn("localdatecolumn");
        LocalTimeColumn ltc = csv.addLocalTimeColumn("localtimecolumn");
        LocalDateTimeColumn ldtc = csv.addLocalDateTimeColumn("localdatetimecolumn");
        assertNotNull(bdc);
        assertNotNull(bc);
        assertNotNull(byteC);
        assertNotNull(datec);
        assertNotNull(dc);
        assertNotNull(fc);
        assertNotNull(ic);
        assertNotNull(sc);
        assertNotNull(uc);
        assertNotNull(ldc);
        assertNotNull(ltc);
        assertNotNull(ldtc);
    }

    @Test(expected = ColumnAlreadyExistException.class)
    public void addColumn() {
        CSV csv = new CSV();
        csv.addColumn(new IntegerColumn("Tall"));
        csv.addColumn(new IntegerColumn("Tall"));
    }

    @Test
    public void insertColumns() throws CsvReaderException {
        CSV csv = new CSV(getResource("noheaders.csv"));
        assertEquals(1, csv.getRowCount());
        assertEquals(2, csv.getColumnCount());
        csv.insertColumns();
        assertEquals(2, csv.getRowCount());
        assertEquals(2, csv.getColumnCount());
    }

    @Test
    public void removeColumn() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        StringColumn presidency = (StringColumn) csv.getColumn(0);
        int cols = csv.getColumnCount();
        csv.removeColumn(presidency);
        assertEquals(cols - 1, csv.getColumnCount());
        csv.removeColumn(2);
        assertEquals(cols - 2, csv.getColumnCount());
    }

    // ------ Rows ------

    @Test
    public void fireColumnMoved() throws CsvReaderException {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("first");
        StringColumn last = csv.addStringColumn("last");
        csv.moveColumn(0, 1);
        assertEquals(first, csv.getColumn(1));
        assertEquals(last, csv.getColumn(0));
    }

    @Test
    public void buildDistinctValues() throws CsvReaderException {
        CSV csv = new CSV();
        StringColumn president = csv.addStringColumn("President");
        csv.addRow().set(president, "Hillary");
        csv.addRow().set(president, "Barak");
        csv.addRow().set(president, "Hillary");
        csv.addRow().set(president, "Clinton");
        Set<String> values = csv.buildDistinctValues(president);
        Assert.assertEquals(3, values.size());

    }

    @Test
    public void addRowAt() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        Row r = csv.addRow(10);
        assertEquals(r, csv.getRow(10));
    }

    @Test
    public void shouldSwapRows() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        Row r0 = csv.getRow(0);
        Row r10 = csv.getRow(10);
        csv.swapRows(0, 10);
        assertEquals(10, csv.indexOf(r0));
        assertEquals(0, csv.indexOf(r10));
    }

    @Test
    public void shouldMoveRows() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        Row r9 = csv.getRow(9);
        csv.moveRow(0, 9);
        assertEquals(0, csv.indexOf(r9));
    }

    @Test
    public void shouldRemoveRow() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        assertEquals(44, csv.getRowCount());
        csv.removeRow(1);
        assertEquals(43, csv.getRowCount());
    }

    @Test
    public void shouldRemoveRowsBetween() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        csv.removeRowsBetween(0, 9);
        assertEquals(34, csv.getRowCount());
    }

    @Test
    public void indexOf() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        Row r = csv.getRow(10);
        assertEquals(10, csv.indexOf(r));
        assertEquals(0, csv.indexOf("Presidency"));
        assertEquals(-1, csv.indexOf("DontExist"));
    }

    // ------ div ------

    @Test
    public void stream() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        assertEquals(44, csv.stream().count());
    }

    @Test
    public void clear() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        csv.clear();
        assertEquals(0, csv.getRowCount());
    }


    // ------ Reading ------

    @Test
    public void removeRows() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        csv.removeRows();
        assertEquals(0, csv.getRowCount());
    }

    @Test
    public void getRow() throws CsvReaderException {
        CSV csv = new CSV(getResource("presidents.csv"));
        assertNull(csv.getRow(5000));
    }

    @Test
    public void readUsingConstructor() throws CsvReaderException {
        CSV csv = new CSV(getResource("presidents.csv"));
        assertNotNull(csv.getFile());
        assertSame(44, csv.getRowCount());
    }

    @Test
    public void createNew() {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        Row r1 = csv.addRow().set(first, "Bill").set(last, "Gates");
        Row r2 = csv.addRow().set(first, "Steve").set(last, "Jobs");
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
        csv.addRow().set(first, "Bill").set(last, "Gates");
        csv.addRow().set(first, "Steve").set(last, "Jobs");
        assertSame("RowCount", csv.getRowCount(), 2);
        try {
            csv.writeFile(file);
        } catch (CsvWriterException e) {
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
        csv.addRow().set(first, "Bill").set(last, "Gates");
        csv.writeXML(file);
        assertNotNull(file);
    }

    @Test
    public void writeJSON() throws Exception {
        File file = File.createTempFile("writeJSON", ".json");
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        csv.addRow().set(first, "Bill").set(last, "Gates");
        csv.addRow().set(first, "Steve").set(last, "Jobs");
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
        csv.addRow().set(first, "Bill").set(last, "Gates");
        csv.writeHtml(file);
        assertNotNull(file);
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

            assertEquals("Skippy", "Skippy Peterson", r.get(playerName));
            assertEquals("First Base", "First Base", r.get(position));
            assertEquals("1908-1913", "1908-1913", r.get(yearsActive));

            String blueDog = "\"Blue Dog\", \"The Magician\"";

            assertEquals("Blue Dog The Magician", "\"Blue Dog\", \"The Magician\"", r.get(nicknames));

        } catch (Exception e) {
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

            assertEquals("Venture", "Venture \"Extended Edition\"", r.get(model));
        } catch (Exception e) {
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

            assertEquals("ac, abs, moon", "ac, abs, moon", r.get(desc));
        } catch (Exception e) {
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

            assertEquals("GPA", "3.48", r.get(col));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readInvalid() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("invalid.csv"));
        assertEquals("First", "First", csv.getColumn(0).getName());
        assertEquals("Last", "Last", csv.getColumn(1).getName());
        assertSame("ColumnCount", 2, csv.getColumnCount());
    }

    @Test
    public void getColumn() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("separator_comma.csv"));
        assertEquals(null, csv.getColumn("DOESNT_EXIST"));
    }

    @Test
    public void writeXml() throws IOException, CsvWriterException {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("first");
        csv.addRow().set(first, "Bill");
        File file = File.createTempFile("csvxmltest", "xml");
        csv.writeXML(file);
        assertTrue(xsd_valid(file));
    }

    @Test
    public void xml_valid() {
        File xmlFile = new File("src/test/resources/csv-valid.xml");
        assertTrue(xsd_valid(xmlFile));
    }

    @Test
    public void xml_invalid() {
        File xmlFile = new File("src/test/resources/csv-invalid.xml");
        assertFalse(xsd_valid(xmlFile));
    }

    @Test
    public void getRowsByMatchers() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("separator_comma.csv"));
        StringColumn c = (StringColumn) csv.getColumn("Heading1");

        List<ValueMatcher> matchers = new ArrayList<>();
        matchers.add(new StringInMatcher(c, "First"));

        csv.findRowsByMatchers(matchers);
    }

    @Test
    public void readWithoutSeparator() throws CsvReaderException {
        CSV csv = new CSV();
        csv.readFile(getResource("separator_comma.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(2, csv.getRowCount());
    }

    @Test
    public void readCommaSeparated() throws CsvReaderException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.COMMA);
        csv.readFile(getResource("separator_comma.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(2, csv.getRowCount());
    }

    @Test
    public void readSemiColonSeparated() throws CsvReaderException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.SEMICOLON);
        csv.readFile(getResource("separator_semi.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(1, csv.getRowCount());
    }

    @Test
    public void readTabSeparated() throws CsvReaderException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.TAB);
        csv.readFile(getResource("separator_tab.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(1, csv.getRowCount());

        csv = new CSV();
//        assertEquals((Character)CSV.TAB, csv.getSeparatorChar());
        csv.readFile(getResource("separator_tab.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(1, csv.getRowCount());
    }

    @Test
    public void readPipeSeparated() throws CsvReaderException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.PIPE);
        csv.readFile(getResource("separator_pipe.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(1, csv.getRowCount());
    }

    @Test
    public void readSingleQuote() throws CsvReaderException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.COMMA);
        csv.setQuoteChar(CSV.QUOTE_SINGLE);
        csv.setAutoDetectCharset(true);
        csv.readFile(getResource("quote_single.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(2, csv.getRowCount());
        StringColumn sc = (StringColumn) csv.getColumn(0);
        assertTrue(csv.isAutoDetectCharset());
        assertEquals("Heading1", csv.getColumn(0).getName());
        assertEquals("first", csv.getRow(0).get(sc));
    }

    @Test
    public void readDoubleQuote() throws CsvReaderException {
        CSV csv = new CSV();
        csv.setSeparator(CSV.COMMA);
        csv.setQuoteChar(CSV.QUOTE_DOUBLE);
        csv.readFile(getResource("quote_double.csv"));
        assertEquals(3, csv.getColumnCount());
        assertEquals(2, csv.getRowCount());
        StringColumn sc = (StringColumn) csv.getColumn(0);
        assertEquals("Heading1", csv.getColumn(0).getName());
        assertEquals("first", csv.getRow(0).get(sc));
    }

    @Test
    public void swapColumn() throws Exception {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("first");
        StringColumn last = csv.addStringColumn("last");
        assertEquals(0, csv.indexOf(first));
        assertEquals(1, csv.indexOf(last));
        csv.swapColumn(0, 1);
        assertEquals(0, csv.indexOf(last));
        assertEquals(1, csv.indexOf(first));
    }

    @Test
    public void buildFrequencyDistribution_string() throws Exception {
        CSV csv = new CSV();
        StringColumn c = csv.addStringColumn("First");
        csv.addRow().set(c, "a");
        csv.addRow().set(c, "b");
        csv.addRow().set(c, "c");
        csv.addRow().set(c, "a");
        csv.addRow().set(c, "a");
        csv.addRow().set(c, null);
        FrequencyDistribution<String> fd = csv.buildFrequencyDistribution(c);
        assertEquals(3, fd.getKeys().size());
    }

    @Test
    public void buildFrequencyDistribution_int() throws Exception {
        CSV csv = new CSV();
        IntegerColumn c = csv.addIntegerColumn("First");
        csv.addRow().set(c, 1);
        csv.addRow().set(c, 2);
        csv.addRow().set(c, 3);
        csv.addRow().set(c, 1);
        csv.addRow().set(c, 1);
        csv.addRow().set(c, null);
        FrequencyDistribution<Integer> fd = csv.buildFrequencyDistribution(c);
        assertEquals(3, fd.getKeys().size());
        Set<Integer> distinct = csv.buildDistinctValues(c);
        assertEquals(3, distinct.size());
    }

    @Test
    public void buildFrequencyDistribution_float() throws Exception {
        CSV csv = new CSV();
        FloatColumn c = csv.addFloatColumn("First");
        csv.addRow().set(c, 1f);
        csv.addRow().set(c, 2f);
        csv.addRow().set(c, 3f);
        csv.addRow().set(c, 1f);
        csv.addRow().set(c, 1f);
        csv.addRow().set(c, null);
        FrequencyDistribution<Float> fd = csv.buildFrequencyDistribution(c);
        assertEquals(3, fd.getKeys().size());
        Set<Float> distinct = csv.buildDistinctValues(c);
        assertEquals(3, distinct.size());
    }

    @Test
    public void buildFrequencyDistribution_double() throws Exception {
        CSV csv = new CSV();
        DoubleColumn c = csv.addDoubleColumn("First");
        csv.addRow().set(c, 1d);
        csv.addRow().set(c, 2d);
        csv.addRow().set(c, 3d);
        csv.addRow().set(c, 1d);
        csv.addRow().set(c, 1d);
        csv.addRow().set(c, null);
        FrequencyDistribution<Double> fd = csv.buildFrequencyDistribution(c);
        assertEquals(3, fd.getKeys().size());
        Set<Double> distinct = csv.buildDistinctValues(c);
        assertEquals(3, distinct.size());
    }

    @Test
    public void buildFrequencyDistribution_bigdecimal() throws Exception {
        CSV csv = new CSV();
        BigDecimalColumn c = csv.addBigDecimalColumn("First");
        csv.addRow().set(c, BigDecimal.valueOf(1));
        csv.addRow().set(c, BigDecimal.valueOf(2));
        csv.addRow().set(c, BigDecimal.valueOf(3));
        csv.addRow().set(c, BigDecimal.valueOf(1));
        csv.addRow().set(c, BigDecimal.valueOf(1));
        csv.addRow().set(c, null);
        FrequencyDistribution<BigDecimal> fd = csv.buildFrequencyDistribution(c);
        assertEquals(3, fd.getKeys().size());
        Set<BigDecimal> distinct = csv.buildDistinctValues(c);
        assertEquals(3, distinct.size());
    }

//    @Test
//    public void buildFrequencyDistribution_url() throws Exception {
//        CSV csv = new CSV();
//        UrlColumn c = csv.addUrlColumn("First");
//        csv.addRow().set(c, new URI("http://localhost/a"));
//        csv.addRow().set(c, new URI("http://localhost/b"));
//        csv.addRow().set(c, new URI("http://localhost/c"));
//        csv.addRow().set(c, new URI("http://localhost/a"));
//        csv.addRow().set(c, new URI("http://localhost/a"));
//        csv.addRow().set(c, null);
//        FrequencyDistribution<URL> fd = csv.buildFrequencyDistribution(c);
//        assertEquals(3, fd.getKeys().size());
//        Set<URL> distinct = csv.buildDistinctValues(c);
//        assertEquals(3, distinct.size());
//    }

    @Test
    public void buildFrequencyDistribution_boolean() throws Exception {
        CSV csv = new CSV();
        BooleanColumn c = csv.addBooleanColumn("First");
        csv.addRow().set(c, Boolean.TRUE);
        csv.addRow().set(c, Boolean.TRUE);
        csv.addRow().set(c, Boolean.FALSE);
        csv.addRow().set(c, null);
        FrequencyDistribution<Boolean> fd = csv.buildFrequencyDistribution(c);
        assertEquals(2, fd.getKeys().size());
        Set<Boolean> distinct = csv.buildDistinctValues(c);
        assertEquals(2, distinct.size());
    }

    @Test
    public void buildFrequencyDistribution_date() throws Exception {
        CSV csv = new CSV();
        DateColumn c = csv.addDateColumn("First");
        Date d1 = new Date();
        csv.addRow().set(c, d1);
        csv.addRow().set(c, null);
        csv.addRow().set(c, null);
        FrequencyDistribution<Date> fd = csv.buildFrequencyDistribution(c);
        assertEquals(1, fd.getKeys().size());
        assertEquals(2, fd.getNullCount());
        // Set<Date> distinct = csv.buildDistinctValues(c);
        // assertEquals(2, distinct.size());
    }

}
