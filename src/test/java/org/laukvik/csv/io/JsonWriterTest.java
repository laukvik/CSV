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

import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class JsonWriterTest {

    @Test
    public void writeJson() throws IOException {
        File file = File.createTempFile("EmptyRows", ".json");

        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");

        csv.addRow().setString(first, "Morten");
        csv.addRow().setString(first, "\"");
        csv.addRow().setString(first, " ");

        JsonWriter writer = new JsonWriter();
        writer.writeCSV(csv, new FileOutputStream(file));

//        JSONParser parser = new JSONParser( csv );
//
//        Object obj = parser.parse(new FileReader(file));
//        JSONArray arr = (JSONArray) obj;
//
//        Assert.assertEquals(3, arr.size());

    }

    @Test
    public void emptyRows() throws IOException {
        File file = File.createTempFile("EmptyRows", ".json");

        CSV csv = new CSV();
        csv.addColumn("First");
        csv.addColumn("Last");

        JsonWriter writer = new JsonWriter();
        writer.writeCSV(csv, new FileOutputStream(file));

    }

    @Test
    public void singleRow() throws IOException {
        File file = File.createTempFile("SingleRow", ".json");

        CSV csv = new CSV();

        StringColumn first = csv.addStringColumn("First");
        StringColumn last = csv.addStringColumn("Last");

        csv.addRow().setString(first, "Morten").setString(last, "Laukvik");

        JsonWriter writer = new JsonWriter();
        writer.writeCSV(csv, new FileOutputStream(file));
    }

}
