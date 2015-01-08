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
import static org.junit.Assert.fail;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CsvOutputStreamTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvOutputStreamTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

//    @Test
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

}
