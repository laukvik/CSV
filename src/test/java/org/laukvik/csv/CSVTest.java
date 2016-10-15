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

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


public class CSVTest {

    @Test
    public void findRows() throws IOException, ParseException {
        CSV csv = new CSV();
        csv.readFile(getResource("countries.csv"));
        assertEquals(249, csv.getRowCount());
    }

    @Test
    public void iterator() throws IOException, ParseException {
        CSV csv = new CSV();
        for (Row r : csv.getRows()) {
        }
    }

    @Test
    public void createNew() {
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        Row r1 = csv.addRow().update(first, "Bill").update(last, "Gates");
        Row r2 = csv.addRow().update(first, "Steve").update(last, "Jobs");
//        assertEquals("Row1", 2, r1.getValues().size());
//        assertEquals("Row1", 2, r2.getValues().size());
        assertSame("RowCount", csv.getRowCount(), 2);
    }

    @Test
    public void shouldWrite() throws Exception {

        File file = File.createTempFile("ShouldWrite", ".csv");

        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        MetaData md = csv.getMetaData();
        assertSame(md.getColumnCount(), 2);
        assertEquals("First should be 0", 0, first.indexOf());
        assertEquals("Last should be 1", 1, last.indexOf());

        csv.addRow().update(first, "Bill").update(last, "Gates");
        csv.addRow().update(first, "Steve").update(last, "Jobs");

        assertSame("RowCount", csv.getRowCount(), 2);
        try {
            csv.writeFile(file);
        }
        catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        CSV csv2 = new CSV();
        csv2.readFile(file);

        assertEquals(2, csv2.getRowCount());

    }

    public static File getResource(String filename) {
        ClassLoader classLoader = CSVTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void readEscaped() {
        try {

            CSV csv = new CSV();
            csv.readFile(getResource("quote_escaped.csv"));

            MetaData md = csv.getMetaData();

            StringColumn playerName = (StringColumn) md.getColumn(0);
            StringColumn position = (StringColumn) md.getColumn(1);
            StringColumn nicknames = (StringColumn) md.getColumn(2);
            StringColumn yearsActive = (StringColumn) md.getColumn(3);

            assertEquals("Player Name", "Player Name", playerName.getName());
            assertEquals("Position", "Position", position.getName());
            assertEquals("Nicknames", "Nicknames", nicknames.getName());
            assertEquals("Years Active", "Years Active", yearsActive.getName());

            assertSame("RowCount", 3, csv.getRowCount());
            assertSame("ColumnCount", 4, md.getColumnCount());

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
            MetaData md = csv.getMetaData();

            assertEquals("Year", "Year", md.getColumn(0).getName());
            assertEquals("Make", "Make", md.getColumn(1).getName());
            assertEquals("Model", "Model", md.getColumn(2).getName());
            assertEquals("Description", "Description", md.getColumn(3).getName());
            assertEquals("Price", "Price", md.getColumn(4).getName());

            assertSame("ColumnCount", 5, md.getColumnCount());
            assertSame("RowCount", 4, csv.getRowCount());

            Row r = csv.getRow(1);

            StringColumn model = (StringColumn) md.getColumn("Model");

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
            MetaData md = csv.getMetaData();

            assertEquals("Year", "Year", md.getColumn(0).getName());
            assertEquals("Make", "Make", md.getColumn(1).getName());
            assertEquals("Model", "Model", md.getColumn(2).getName());
            assertEquals("Description", "Description", md.getColumn(3).getName());
            assertEquals("Price", "Price", md.getColumn(4).getName());

            assertSame("ColumnCount", 5, md.getColumnCount());
            assertSame("RowCount", 1, csv.getRowCount());

            Row r = csv.getRow(0);
            System.out.println(r.toString());
            StringColumn desc = (StringColumn) md.getColumn("Description");

            assertEquals("ac, abs, moon", "ac, abs, moon", r.getString(desc));
        }
        catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

//    @Test
//    public void readQuoted() {
//        try {
//            CSV csv = new CSV();
//            csv.readFile(getResource("quote_double.csv"));
//            MetaData md = csv.getMetaData();
//            assertEquals("Lead", md.getColumnName(0));
//            assertEquals("Title", md.getColumnName(1));
//            assertEquals("Phone", md.getColumnName(2));
//            assertEquals("Notes", md.getColumnName(3));
//
//            assertSame("ColumnCount", 4, md.getColumnCount());
//            assertSame("RowCount", 2, csv.getRowCount());
//
//            Row r = csv.getRow(0);
//
//            StringColumn notes = (StringColumn) md.getColumn("Notes");
//
//            assertEquals("Spoke Tuesday, he's interested", r.getString(notes));
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            fail(e.getMessage());
//        }
//    }

    @Test
    public void readUnqouted() {
        try {
            // Name,Class,Dorm,Room,GPA
            CSV csv = new CSV();
            csv.readFile(getResource("quote_none.csv"));
            MetaData md = csv.getMetaData();
            assertEquals("Name", "Name", md.getColumn(0).getName());
            assertEquals("Class", "Class", md.getColumn(1).getName());
            assertEquals("Dorm", "Dorm", md.getColumn(2).getName());
            assertEquals("Room", "Room", md.getColumn(3).getName());
            assertEquals("GPA", "GPA", md.getColumn(4).getName());

            assertSame("ColumnCount", 5, md.getColumnCount());
            assertSame("RowCount", 4, csv.getRowCount());

            Row r = csv.getRow(3);

            StringColumn col = (StringColumn) md.getColumn("GPA");

            assertEquals("GPA", "3.48", r.getString(col));
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readInvalid() {
        try {
            CSV csv = new CSV();
            csv.readFile(getResource("invalid.csv"));
            MetaData md = csv.getMetaData();
            assertEquals("First", "First", md.getColumn(0).getName());
            assertEquals("Last", "Last", md.getColumn(1).getName());
            assertSame("ColumnCount", 2, md.getColumnCount());
        }
        catch (Exception e) {
            assertTrue("Found invalid data", (e instanceof InvalidRowDataException));
        }
    }

//    @Test
//    public void readEntities() {
//        try {
//            CSV csv = new CSV();
//            csv.readFile(getResource("person.csv"));
//            List<Person> items = CSV.findByClass(Person.class);
//            int x = 1;
//            for (Person p : items) {
//                x++;
//            }
//        }
//        catch (Exception e) {
//            fail(e.getMessage());
//        }
//    }

    @Test
    public void readWithoutSeparator() throws IOException {
        CSV csv = new CSV();
        csv.readFile( getResource("separator_comma.csv")  );
        MetaData md = csv.getMetaData();
        assertEquals(3,md.getColumnCount());
        assertEquals(2,csv.getRowCount());
    }

    @Test
    public void readCommaSeparated() throws IOException {
        CSV csv = new CSV();
        csv.readFile( getResource("separator_comma.csv"), CSV.COMMA );
        MetaData md = csv.getMetaData();
        assertEquals(3,md.getColumnCount());
        assertEquals(2,csv.getRowCount());
    }

    @Test
    public void readSemiColonSeparated() throws IOException {
        CSV csv = new CSV();
        csv.readFile( getResource("separator_semi.csv"), CSV.SEMICOLON  );
        MetaData md = csv.getMetaData();
        assertEquals(3,md.getColumnCount());
        assertEquals(1,csv.getRowCount());
    }

    @Test
    public void readTabSeparated() throws IOException {
        CSV csv = new CSV();
        csv.readFile( getResource("separator_tab.csv"), CSV.TAB  );
        MetaData md = csv.getMetaData();
        assertEquals(3,md.getColumnCount());
        assertEquals(1,csv.getRowCount());
    }

    @Test
    public void readPipeSeparated() throws IOException {
        CSV csv = new CSV();
        csv.readFile( getResource("separator_pipe.csv"), CSV.PIPE  );
        MetaData md = csv.getMetaData();
        assertEquals(3,md.getColumnCount());
        assertEquals(1,csv.getRowCount());
    }

    @Test
    public void readSingleQuote() throws IOException {
        CSV csv = new CSV();
        csv.readFile( getResource("quote_single.csv"), CSV.COMMA, CSV.SINGLE_QUOTE );
        MetaData md = csv.getMetaData();
        assertEquals(3,md.getColumnCount());
        assertEquals(2,csv.getRowCount());
        StringColumn sc = (StringColumn) md.getColumn(0);
        assertEquals("Heading1",md.getColumn(0).getName());
        assertEquals("first", csv.getRow(0).getString(sc));
    }

    @Test
    public void readDoubleQuote() throws IOException {
        CSV csv = new CSV();
        csv.readFile( getResource("quote_double.csv"), CSV.COMMA, CSV.DOUBLE_QUOTE );
        MetaData md = csv.getMetaData();
        assertEquals(3,md.getColumnCount());
        assertEquals(2,csv.getRowCount());
        StringColumn sc = (StringColumn) md.getColumn(0);
        assertEquals("Heading1",md.getColumn(0).getName());
        assertEquals("first", csv.getRow(0).getString(sc));
    }

    @Test
    public void shouldFindByStringQuery() throws IOException {
        CSV csv = new CSV();
        csv.readFile( getResource("presidents.csv") );
        List<Row> rows = csv.findByQuery().where().column("Party").is("Whig").getResultList();
        assertEquals(4, rows.size());

        rows = csv.findByQuery().where().column("Home State").is("Ohio").getResultList();
        assertEquals(7, rows.size());

        rows = csv.findByQuery().where().column("Home State").is("Ohio").column("Party").is("Whig").getResultList();
        assertEquals(1, rows.size());
    }

    @Test
    public void shouldFindByIntQuery() throws IOException {
        CSV csv = new CSV();
        csv.readFile( getResource("presidents.csv") );
        List<Row> rows = csv.findByQuery().where().column("Presidency").in("29","30").getResultList();
        assertEquals(2, rows.size());
    }

}
