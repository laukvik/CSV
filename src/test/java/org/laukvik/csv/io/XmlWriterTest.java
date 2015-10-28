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
import java.io.IOException;
import java.nio.charset.Charset;
import org.junit.Test;
import org.laukvik.csv.CSV;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class XmlWriterTest {

    public XmlWriterTest() {
    }

    @Test
    public void emptyRows() throws IOException {
        File file = File.createTempFile("EmptyRows", ".xml");

        CSV csv = new CSV();
        csv.addColumn("First");
        csv.addColumn("Last");

        csv.addRow("Morten", "Laukvik");

        XmlWriter writer = new XmlWriter();
        writer.write(csv, new FileOutputStream(file), Charset.defaultCharset());


    }

}
