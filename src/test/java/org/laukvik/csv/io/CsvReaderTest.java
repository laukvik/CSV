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
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.FileOutputStream;

/**
 *
 * Separator: TAB, comma, semikolon eller CUSTOM
 *
 *
 *
 */
public class CsvReaderTest {

    @Test
    public void shouldRead() throws Exception {
        File file = File.createTempFile("Person", ".csv");
        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");
        Column last = csv.addColumn("Last");
        csv.write(new CsvWriter(new FileOutputStream(file), csv.getMetaData()));
        CsvReader r = new CsvReader(file);
        Assert.assertEquals(2, r.getMetaData().getColumnCount());
        while (r.hasNext()) {
            Row row = r.next();
        }
    }

}
