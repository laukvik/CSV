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

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Assert;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.columns.StringColumn;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class JsonWriterTest {

    @Test
    public void writeJson() throws IOException, ParseException {
        File file = File.createTempFile("EmptyRows", ".json");

        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");

        csv.addRow().update(first, "Morten");
        csv.addRow().update(first, "\"");
        csv.addRow().update(first, " ");

        JsonWriter writer = new JsonWriter(new FileOutputStream(file));
        writer.write(csv);

        JSONParser parser = new JSONParser();

        Object obj = parser.parse(new FileReader(file));
        JSONArray arr = (JSONArray) obj;

        Assert.assertEquals(3, arr.size());

    }

}
