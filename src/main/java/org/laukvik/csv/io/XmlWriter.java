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

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import org.laukvik.csv.CSV;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class XmlWriter implements Writeable {

    private final static String START = "<rows>";
    private final static String END = "</rows>";

    private final static String ROW_START = "<row ";
    private final static String ROW_END = ">";

    private final static char OPEN = '<';
    private final static char CLOSE = '<';
    private final static char LINEFEED = '\n';
    private final static char QUOTE = '"';
    private final static char SPACE = ' ';
    private final static char EQUAL = '=';

    @Override
    public void write(CSV csv, OutputStream out, Charset charset) throws IOException {
        out.write(("<?xml version=\"1.0\" encoding=\"" + charset.name() + "\"?>").getBytes());
        out.write(START.getBytes(charset));
        for (int y = 0; y < csv.getRowCount(); y++) {
            out.write(ROW_START.getBytes(charset));
            for (int x = 0; x < csv.getMetaData().getColumnCount(); x++) {
                out.write(SPACE);
                out.write(csv.getMetaData().getColumn(x).getName().getBytes(charset));
                out.write(EQUAL);
                out.write(QUOTE);
                out.write(csv.getRow(y).getString(x).getBytes(charset));
                out.write(QUOTE);
            }
            out.write(ROW_END.getBytes(charset));
        }
        out.write(END.getBytes(charset));
    }

}
