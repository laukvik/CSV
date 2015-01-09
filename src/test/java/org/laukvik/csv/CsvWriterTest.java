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
import java.io.FileOutputStream;
import java.io.IOException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CsvWriterTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvWriterTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void writeAndRead() {
        File f = new File("/Users/morten/Desktop/test.csv");
//        File f = File.createTempFile("CsvWriter", ".csv");

        try (CsvWriter w = new CsvWriter(new FileOutputStream(f))) {
            w.writeMetaData(new MetaData("First", "Last"));
            w.writeRow(new Row("Bill", "Gates"));
            w.writeRow(new Row("Steve", "Jobs"));

        } catch (IOException e) {
            fail("Failed to write CSV file!");
        }


        try {
            CSV csv = new CSV(f);
//                    assertEquals("Correct column count", 2, csv.getMetaData().getColumnCount());
            assertEquals("Correct row count", 2, csv.getRowCount());
            assertEquals("First", "First", csv.getMetaData().getColumnName(0));
            assertEquals("Last", "Last", csv.getMetaData().getColumnName(1));
            assertEquals("Find by row index and index", "Bill", csv.getRow(0).getString(0));
            assertEquals("Find by row index and column name", "Gates", csv.getRow(0).getString("Last"));
        } catch (IOException ex) {
            fail("Failed to read CSV file!");
        }

    }

}
