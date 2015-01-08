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
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import org.junit.Test;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CsvInputStreamTest {


    public void readFile(String filename, int requiredColumns, int requiredRows, String charset) {
        try (CsvInputStream is = new CsvInputStream(new FileInputStream(getResource(filename)), Charset.forName(charset))) {
            int rows = 0;
            int columns = 0;
            while (is.hasMoreLines()) {
                List<String> line = is.readLine();
                if (rows == 0) {
                    columns = line.size();
                } else {
                    assertSame(is.getRaw(), line.size(), columns);
                }
                rows++;
            }
            assertSame(rows, requiredRows);
            assertSame(columns, requiredColumns);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void readAcid() {
        readFile("acid.csv", 5, 5, "us-ascii");
    }

    @Test
    public void readEmbeddedCommas() {
        readFile("embeddedcommas.csv", 5, 2, "us-ascii");
    }

    @Test
    public void readEscaped() {
        readFile("escaped.csv", 4, 4, "utf-8");
    }

    @Test
    public void readQuoted() {
        readFile("quoted.csv", 4, 4, "us-ascii");
    }

    @Test
    public void readUnquoted() {
        readFile("unquoted.csv", 5, 5, "us-ascii");
    }

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvInputStreamTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }


}
