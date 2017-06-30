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
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class XmlWriterTest {

    @Test
    public void shouldWriteReservedCharacters() throws IOException, SAXException, ParserConfigurationException {
        File file = File.createTempFile("EmptyRows", ".xml");

        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");

        csv.addRow().set(first, "&");
        csv.addRow().set(first, "\"");
        csv.addRow().set(first, ">");
        csv.addRow().set(first, "<");
        csv.addRow().set(first, "'");

        XmlWriter writer = new XmlWriter();
        writer.writeCSV(csv, new FileOutputStream(file));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        builder.parse(file);
    }

}
