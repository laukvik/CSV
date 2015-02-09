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

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.laukvik.csv.columns.StringColumn;

/**
 * <li>charset encoding
 * <li>escaping quotes
 * <li>linfeeds in values
 *
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CSVTest {

    @Test
    public void createNew() {
        CSV csv = new CSV();
        csv.addColumn("First");
        csv.addColumn("Last");
        Row r1 = csv.addRow("Bill", "Gates");
        assertEquals("Row1", 2, r1.getValues().size());
        Row r2 = csv.addRow("Steve", "Jobs");
        assertEquals("Row1", 2, r2.getValues().size());
        assertSame("RowCount", csv.getRowCount(), 2);
    }

    @Test
    public void shouldWrite() {

        CSV csv = new CSV(new StringColumn("First"), new StringColumn("Last"));
        MetaData md = csv.getMetaData();

        assertSame(md.getColumnCount(), 2);

        csv.addRow("Bill", "Gates");
        csv.addRow("Steve", "Jobs");
        assertSame("RowCount", csv.getRowCount(), 2);
        try {
            csv.write(File.createTempFile("ShouldWrite", ".csv"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    public static File getResource(String filename) {
        ClassLoader classLoader = CSVTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void readEscaped() {
        try {
            CSV csv = new CSV(getResource("escaped.csv"), Charset.forName("utf-8"));
            MetaData md = csv.getMetaData();

            assertEquals("Player Name", "Player Name", md.getColumnName(0));
            assertEquals("Position", "Position", md.getColumnName(1));
            assertEquals("Nicknames", "Nicknames", md.getColumnName(2));
            assertEquals("Years Active", "Years Active", md.getColumnName(3));

            assertSame("RowCount", 3, csv.getRowCount());
            assertSame("ColumnCount", 4, md.getColumnCount());

            Row r = csv.getRow(0);

            assertEquals("Skippy", "Skippy Peterson", r.getString(0));
            assertEquals("First Base", "First Base", r.getString(1));
            assertEquals("1908-1913", "1908-1913", r.getString(3));

            String blueDog = "\"Blue Dog\", \"The Magician\"";


            assertEquals("Blue Dog The Magician", "\"Blue Dog\", \"The Magician\"", r.getString(2));

        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readAcid() {
        try {
            CSV csv = new CSV(getResource("acid.csv"), Charset.forName("utf-8"));
            MetaData md = csv.getMetaData();

            assertEquals("Year", "Year", md.getColumnName(0));
            assertEquals("Make", "Make", md.getColumnName(1));
            assertEquals("Model", "Model", md.getColumnName(2));
            assertEquals("Description", "Description", md.getColumnName(3));
            assertEquals("Price", "Price", md.getColumnName(4));

            assertSame("ColumnCount", 5, md.getColumnCount());
            assertSame("RowCount", 4, csv.getRowCount());

            Row r = csv.getRow(1);

            assertEquals("Venture", "Venture \"Extended Edition\"", r.getString(2));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readEmbeddedCommas() {
        try {
            CSV csv = new CSV(getResource("embeddedcommas.csv"), Charset.forName("utf-8"));
            MetaData md = csv.getMetaData();

            assertEquals("Year", "Year", md.getColumnName(0));
            assertEquals("Make", "Make", md.getColumnName(1));
            assertEquals("Model", "Model", md.getColumnName(2));
            assertEquals("Description", "Description", md.getColumnName(3));
            assertEquals("Price", "Price", md.getColumnName(4));

            assertSame("ColumnCount", 5, md.getColumnCount());
            assertSame("RowCount", 1, csv.getRowCount());

            Row r = csv.getRow(0);
            assertEquals("ac, abs, moon", "ac, abs, moon", r.getString(3));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readQuoted() {
        try {
            CSV csv = new CSV(getResource("quoted.csv"), Charset.forName("utf-8"));
            MetaData md = csv.getMetaData();
            assertEquals("Lead", "Lead", md.getColumnName(0));
            assertEquals("Title", "Title", md.getColumnName(1));
            assertEquals("Phone", "Phone", md.getColumnName(2));
            assertEquals("Notes", "Notes", md.getColumnName(3));

            assertSame("ColumnCount", 4, md.getColumnCount());
            assertSame("RowCount", 3, csv.getRowCount());

            Row r = csv.getRow(0);

            assertEquals("Spoke Tuesday, he's interested", r.getString(3));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readUnqouted() {
        try {
            // Name,Class,Dorm,Room,GPA
            CSV csv = new CSV(getResource("unquoted.csv"), Charset.forName("utf-8"));
            MetaData md = csv.getMetaData();
            assertEquals("Name", "Name", md.getColumnName(0));
            assertEquals("Class", "Class", md.getColumnName(1));
            assertEquals("Dorm", "Dorm", md.getColumnName(2));
            assertEquals("Room", "Room", md.getColumnName(3));
            assertEquals("GPA", "GPA", md.getColumnName(4));

            assertSame("ColumnCount", 5, md.getColumnCount());
            assertSame("RowCount", 4, csv.getRowCount());

            Row r = csv.getRow(3);

            assertEquals("GPA", "3.48", r.getString("GPA"));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readInvalid() {
        try {
            CSV csv = new CSV(getResource("invalid.csv"), Charset.forName("utf-8"));
            MetaData md = csv.getMetaData();
            assertEquals("First", "First", md.getColumnName(0));
            assertEquals("Last", "Last", md.getColumnName(1));
            assertSame("ColumnCount", 2, md.getColumnCount());
        } catch (Exception e) {
            assertTrue("Found invalid data", (e instanceof InvalidRowDataException));
        }
    }

    @Test
    public void readEntities() {
        try {
            CSV csv = new CSV(getResource("person.csv"));
            List<Person> items = csv.findByClass(Person.class);
            int x = 1;
            for (Person p : items) {
                System.out.println(x + ":" + p);
                x++;
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
