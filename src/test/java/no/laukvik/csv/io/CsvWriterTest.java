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
package no.laukvik.csv.io;

import no.laukvik.csv.CSV;
import no.laukvik.csv.columns.StringColumn;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CsvWriterTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvWriterTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void writeAndRead() throws CsvWriterException, IOException, CsvReaderException {
        File f = File.createTempFile("CsvWriter", ".csv");
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");
        csv.addRow().setRaw(first, "Bill").setRaw(last, "Gates");
        csv.addRow().setRaw(first, "Steve").setRaw(last, "Jobs");
        CsvWriter w = new CsvWriter();
        w.writeCSV(f, csv);

        CSV csv2 = new CSV();
        csv2.readFile(f);
        assertEquals(2, csv2.getRowCount());
        assertEquals("First", csv2.getColumn(0).getName());
        assertEquals("Last", csv2.getColumn(1).getName());
        assertEquals("Bill", csv2.getRow(0).get(first));
        assertEquals("Gates", csv2.getRow(0).get(last));
    }

    @Test
    public void shouldByDigitsOnly() {
        assertTrue(CsvWriter.isDigitsOnly("123"));
        assertTrue(CsvWriter.isDigitsOnly(""));
        assertFalse(CsvWriter.isDigitsOnly(null));
    }

    @Test
    public void shouldFail() {
        Assert.assertEquals("Can't start with space", false, CsvWriter.isDigitsOnly(" 123"));
        Assert.assertEquals("Can't end with space", false, CsvWriter.isDigitsOnly("123 "));
        Assert.assertEquals("Can't have space on left and right", false, CsvWriter.isDigitsOnly("123 "));
    }

    @Test(expected = CsvWriterException.class)
    public void write() throws CsvWriterException {
        CSV csv = new CSV();
        csv.writeFile(new File(""));
    }

}
