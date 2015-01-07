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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 *
 */
public class ReadWriteTest  {

    public ReadWriteTest() {
    }

    @Test
    public void shouldReadInputStream() {
        File f = new File("/Users/morten/Desktop/cars.csv");
        try (CsvInputStream is = new CsvInputStream(new FileInputStream(f))) {
            while (is.hasMoreLines()) {
                List<String> items = is.readLine();
                System.out.print("Found: ");
                for (String i : items) {
                    System.out.print(i + "_");
                }
                System.out.println();
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldWriteOutputStream() {
        File f = new File("/Users/morten/Desktop/ShouldWriteOutputStream.csv");
        try (CsvOutputStream out = new CsvOutputStream(new FileOutputStream(f))) {
            out.writeHeader("First", "Last");
            out.writeLine("Bill", "Gates");
            out.writeLine("Steve", "Jobs");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldWrite() {
        CSV csv = new CSV("First", "Last");
        Assert.assertSame(csv.getColumnCount(), 2);
        csv.addRow("Bill", "Gates");
        csv.addRow("Steve", "Jobs");
        Assert.assertSame(csv.getRowCount(), 2);
        try {
            csv.write(new File("/Users/morten/Desktop/ShouldWrite.csv"));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void shouldRead() {
        CSV csv;
        try {
            csv = new CSV(new File("/Users/morten/Desktop/executives.csv"));
            Assert.assertSame(csv.getRowCount(), 2);
            Assert.assertSame(csv.getColumnCount(), 2);
            Assert.assertSame(csv.getValue(0, 0), "Bill");
            Assert.assertSame(csv.getValue(1, 0), "Gates");
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }



}
