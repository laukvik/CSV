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

import org.junit.Assert;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");

        try (FileOutputStream out = new FileOutputStream(f)) {
            CsvWriter w = new CsvWriter();
            MetaData md = csv.getMetaData();
            w.writeCSV(md, out);
            w.writeCSV(csv.buildRow().setString(first, "Bill").setString(last, "Gates"), md, out);
            w.writeCSV(csv.buildRow().setString(first, "Steve").setString(last, "Jobs"), md, out);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            CSV csv2 = new CSV();
            csv2.readFile(f);
            assertEquals("Correct row count", 2, csv2.getRowCount());
            assertEquals("First", "First", csv2.getMetaData().getColumn(0).getName());
            assertEquals("Last", "Last", csv2.getMetaData().getColumn(1).getName());
            assertEquals("Find by row index and index", "Bill", csv2.getRow(0).getString(first));
            assertEquals("Find by row index and column name", "Gates", csv2.getRow(0).getString(last));
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
