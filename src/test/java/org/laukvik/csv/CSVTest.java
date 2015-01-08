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

    @Test
    public void readEscaped() {
        try {
            CSV csv = new CSV(getResource("escaped.csv"), Charset.forName("utf-8"));

            assertEquals("Player Name", csv.getColumnName(0), "Player Name");
            assertEquals("Position", csv.getColumnName(1), "Position");
            assertEquals("Nicknames", csv.getColumnName(2), "Nicknames");
            assertEquals("Years Active", csv.getColumnName(3), "Years Active");

            assertSame("RowCount", csv.getRowCount(), 3);
            assertSame("ColumnCount", csv.getColumnCount(), 4);

            assertEquals("1908-1913", csv.getValue(3, 0), "1908-1913");

            assertEquals("Skippy", csv.getValue(0, 0), "Skippy Peterson");
            assertEquals("First Base", csv.getValue(1, 0), "First Base");

            String blueDog = "\"Blue Dog\", \"The Magician\"";
            System.out.println("ORIG: " + blueDog);
            System.out.println("READ: " + csv.getValue(2, 0));

            assertEquals("Blue Dog The Magician", csv.getValue(2, 0), "\"Blue Dog\", \"The Magician\"");


        } catch (IOException e) {
            fail(e.getMessage());

        }
    }

    public static File getResource(String filename) {
        ClassLoader classLoader = CSVTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

}
