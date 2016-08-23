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

import org.junit.Assert;
import org.junit.Test;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.io.CsvWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


public class CsvWriterTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvWriterTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void writeAndRead() throws IOException {

        File f = File.createTempFile("CsvWriter", ".csv");

        MetaData md = new MetaData();

        StringColumn first = (StringColumn) md.addColumn(new StringColumn("First"));
        StringColumn last = (StringColumn) md.addColumn(new StringColumn("Last"));

        try (CsvWriter w = new CsvWriter(new FileOutputStream(f), Charset.defaultCharset())) {
            //
            w.writeRow(new Row().update(first, "Bill").update(last, "Gates"));
            w.writeRow(new Row().update(first, "Steve").update(last, "Jobs"));
        }
        catch (IOException e) {
            fail("Failed to write CSV file!");
        }

        try {
            CSV csv = new CSV();
            csv.readFile(f);
//            StringColumn last = (StringColumn) csv.getMetaData().getColumn("Last");
//                    assertEquals("Correct column count", 2, csv.getMetaData().getColumnCount());
            assertEquals("Correct row count", 2, csv.getRowCount());
            assertEquals("First", "First", csv.getMetaData().getColumnName(0));
            assertEquals("Last", "Last", csv.getMetaData().getColumnName(1));
            assertEquals("Find by row index and index", "Bill", csv.getRow(0).getString(first));
            assertEquals("Find by row index and column name", "Gates", csv.getRow(0).getString(last));
        }
        catch (IOException ex) {
            fail("Failed to readFile CSV file!");
        }
    }

    @Test
    public void shouldByDigitsOnly() {
        Assert.assertEquals(true, CsvWriter.isDigitsOnly("123"));
    }

    @Test
    public void shouldFail() {
        Assert.assertEquals("Can't start with space", false, CsvWriter.isDigitsOnly(" 123"));
        Assert.assertEquals("Can't end with space", false, CsvWriter.isDigitsOnly("123 "));
        Assert.assertEquals("Can't have space on left and right", false, CsvWriter.isDigitsOnly("123 "));
    }

}
