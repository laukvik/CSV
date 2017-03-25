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
import org.laukvik.csv.columns.StringColumn;
import org.w3c.dom.Document;
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

        csv.addRow().setString(first, "&");
        csv.addRow().setString(first, "\"");
        csv.addRow().setString(first, ">");
        csv.addRow().setString(first, "<");
        csv.addRow().setString(first, "'");

        XmlWriter writer = new XmlWriter();
        writer.writeCSV(csv, new FileOutputStream(file));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        builder.parse(file);
    }

    @Test
    public void shouldWriteWithCustomElementNames() throws IOException, SAXException, ParserConfigurationException {
        File file = File.createTempFile("EmptyRows", ".xml");

        CSV csv = new CSV();
        StringColumn first = csv.addStringColumn("First");

        csv.addRow().setString(first, "Bob");
        csv.addRow().setString(first, "Dylan");

        XmlWriter writer = new XmlWriter("people", "person");
        writer.writeCSV(csv, new FileOutputStream(file));

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(file);

        Assert.assertEquals("people", document.getDocumentElement().getNodeName());
        Assert.assertEquals(2, document.getDocumentElement().getElementsByTagName("person").getLength());
    }

    @Test(expected=IOException.class)
    public void write() throws CsvWriterException, IOException {
        CSV csv = new CSV();
        StringColumn c = csv.addStringColumn("first");
        csv.addRow().setString(c, "Bill");

        XmlWriter writer = new XmlWriter("people", "person");
        writer.writeCSV(csv, new FileOutputStream( new File("") ));
    }

}
