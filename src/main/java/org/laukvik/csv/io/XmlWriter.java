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

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;


public class XmlWriter implements Writeable, AutoCloseable {

    private final static char OPEN = '<';
    private final static char CLOSE = '>';
    private final static char LINEFEED = '\n';
    private final static char CR = '\r';
    private final static char SLASH = '/';
    private final static char QUOTATION_MARK = '"';
    private final static char APOSTROPHE = '\'';
    private final static char AMPERSAND = '&';
    private final static char SPACE = ' ';
    private final static char EQUAL = '=';
    private final static char TAB = '\t';

    private final OutputStream out;
    private String rootElementName;
    private String rowElementName;

    public XmlWriter(OutputStream out, String rootName, String rowName){
        this.out = out;
        this.rootElementName = rootName;
        this.rowElementName = rowName;
    }

    public XmlWriter(OutputStream out){
        this(out, "rows", "row");
    }

    @Override
    public void writeFile(CSV csv) throws IOException {
        Charset charset = csv.getMetaData().getCharset();
        out.write(("<?xml version=\"1.0\" encoding=\"" + charset.name() + "\"?>").getBytes());

        out.write(CR);
        out.write(LINEFEED);
        // Root
        out.write(OPEN);
        out.write(this.rootElementName.getBytes());
        out.write(CLOSE);

        // Iterate rows
        for (int y = 0; y < csv.getRowCount(); y++) {
            out.write(CR);
            out.write(LINEFEED);
            out.write(TAB);
            out.write(OPEN);
            out.write(this.rowElementName.getBytes());
            Row r = csv.getRow(y);
            for (int x = 0; x < csv.getMetaData().getColumnCount(); x++) {
                Column col = csv.getMetaData().getColumn(x);
                out.write(SPACE);
                out.write(csv.getMetaData().getColumn(x).getName().getBytes(charset));
                out.write(EQUAL);
                out.write(QUOTATION_MARK);
                String s = r.getAsString(col);
                if (s == null) {
                } else {
                    for (int n = 0; n < s.length(); n++) {
                        char c = s.charAt(n);
                        if (c == QUOTATION_MARK) {
                            out.write("&quot;".getBytes());
                        } else if (c == OPEN) {
                            out.write("&lt;".getBytes());
                        } else if (c == CLOSE) {
                            out.write("&gt;".getBytes());
                        } else if (c == APOSTROPHE) {
                            out.write("&apos;".getBytes());
                        } else if (c == AMPERSAND) {
                            out.write("&amp;".getBytes());
                        } else {
                            out.write(c);
                        }
                    }
                }
                out.write(QUOTATION_MARK);
            }
            out.write(SLASH);
            out.write(CLOSE);
            out.write(CR);
            out.write(LINEFEED);
        }

        // Close root element
        out.write(OPEN);
        out.write(SLASH);
        out.write(this.rootElementName.getBytes());
        out.write(CLOSE);
        out.flush();

    }

    @Override
    public void close() throws Exception {
        out.flush();
        out.close();
    }

}
