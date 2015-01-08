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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CSVTest {

//    @Test
    public void shouldWrite() {
        CSV csv = new CSV("First", "Last");
        assertSame(csv.getColumnCount(), 2);
        csv.addRow("Bill", "Gates");
        csv.addRow("Steve", "Jobs");
        assertSame("RowCount", csv.getRowCount(), 2);
        try {
            csv.write(new File("/Users/morten/Desktop/ShouldWrite.csv"));
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

            assertEquals("Player Name", "Player Name", csv.getColumnName(0));
            assertEquals("Position", "Position", csv.getColumnName(1));
            assertEquals("Nicknames", "Nicknames", csv.getColumnName(2));
            assertEquals("Years Active", "Years Active", csv.getColumnName(3));

            assertSame("RowCount", 3, csv.getRowCount());
            assertSame("ColumnCount", 4, csv.getColumnCount());

            assertEquals("1908-1913", "1908-1913", csv.getValue(3, 0));

            assertEquals("Skippy", "Skippy Peterson", csv.getValue(0, 0));
            assertEquals("First Base", "First Base", csv.getValue(1, 0));

            String blueDog = "\"Blue Dog\", \"The Magician\"";
            System.out.println("ORIG: " + blueDog);
            System.out.println("READ: " + csv.getValue(2, 0));

            assertEquals("Blue Dog The Magician", "\"Blue Dog\", \"The Magician\"", csv.getValue(2, 0));

        } catch (IOException e) {
            fail(e.getMessage());

        }
    }

    @Test
    public void readAcid() {
        try {
            CSV csv = new CSV(getResource("acid.csv"), Charset.forName("utf-8"));

            assertEquals("Year", "Year", csv.getColumnName(0));
            assertEquals("Make", "Make", csv.getColumnName(1));
            assertEquals("Model", "Model", csv.getColumnName(2));
            assertEquals("Description", "Description", csv.getColumnName(3));
            assertEquals("Price", "Price", csv.getColumnName(4));

            assertSame("ColumnCount", 5, csv.getColumnCount());
            assertSame("RowCount", 4, csv.getRowCount());

            assertEquals("Venture", "Venture \"Extended Edition\"", csv.getValue(3, 0));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readEmbeddedCommas() {
        try {
            CSV csv = new CSV(getResource("embeddedcommas.csv"), Charset.forName("utf-8"));
            assertEquals("Year", "Year", csv.getColumnName(0));
            assertEquals("Make", "Make", csv.getColumnName(1));
            assertEquals("Model", "Model", csv.getColumnName(2));
            assertEquals("Description", "Description", csv.getColumnName(3));
            assertEquals("Price", "Price", csv.getColumnName(4));

            assertSame("ColumnCount", 5, csv.getColumnCount());
            assertSame("RowCount", 1, csv.getRowCount());

            assertEquals("ac, abs, moon", "ac, abs, moon", csv.getValue(3, 0));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readQuoted() {
        try {
            CSV csv = new CSV(getResource("quoted.csv"), Charset.forName("utf-8"));
            assertEquals("Lead", "Lead", csv.getColumnName(0));
            assertEquals("Title", "Title", csv.getColumnName(1));
            assertEquals("Phone", "Phone", csv.getColumnName(2));
            assertEquals("Notes", "Notes", csv.getColumnName(3));

            assertSame("ColumnCount", 4, csv.getColumnCount());
            assertSame("RowCount", 3, csv.getRowCount());

            assertEquals("Spoke Tuesday, he's interested", csv.getValue(3, 0));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readUnqouted() {
        try {
            // Name,Class,Dorm,Room,GPA
            CSV csv = new CSV(getResource("unquoted.csv"), Charset.forName("utf-8"));
            assertEquals("Name", "Name", csv.getColumnName(0));
            assertEquals("Class", "Class", csv.getColumnName(1));
            assertEquals("Dorm", "Dorm", csv.getColumnName(2));
            assertEquals("Room", "Room", csv.getColumnName(3));
            assertEquals("GPA", "GPA", csv.getColumnName(4));

            assertSame("ColumnCount", 5, csv.getColumnCount());
            assertSame("RowCount", 4, csv.getRowCount());

            assertEquals("Spoke Tuesday, he's interested", csv.getValue(3, 0));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }


}
