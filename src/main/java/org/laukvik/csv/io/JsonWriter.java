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
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class JsonWriter implements Writeable {

    private final static char CURLY_LEFT = '{';
    private final static char CURLY_RIGHT = '}';
    private final static char SEMI = ':';
    private final static char ARR_LEFT = '[';
    private final static char ARR_RIGHT = ']';
    private final static char LINEFEED = '\n';
    private final static char TAB = '\t';
    private final static char COMMA = ',';

    @Override
    public void write(CSV csv, OutputStream out, Charset charset) throws IOException {

        MetaData md = csv.getMetaData();
        out.write(CURLY_LEFT);
        out.write(LINEFEED);
        out.write(TAB);
        out.write(LINEFEED);
        out.write("rows".getBytes());
        out.write(LINEFEED);
        out.write(SEMI);
        out.write(ARR_LEFT);
        for (int y = 0; y < csv.getRowCount(); y++) {
            if (y > 0) {
                out.write(COMMA);
                out.write(LINEFEED);
            }
            Row row = csv.getRow(y);
            out.write(LINEFEED);
            out.write(TAB);
            out.write(TAB);
            out.write(CURLY_LEFT);
            out.write(LINEFEED);
            for (int x = 0; x < md.getColumnCount(); x++) {
                if (x > 0) {
                    out.write(COMMA);
                    out.write(LINEFEED);
                }
                out.write(TAB);
                out.write(TAB);
                out.write(TAB);
                out.write(LINEFEED);
                out.write(md.getColumnName(x).getBytes(charset));
                out.write(LINEFEED);
                out.write(SEMI);
                out.write(LINEFEED);
                out.write(row.getString(x).getBytes(charset));
                out.write(LINEFEED);
            }
            out.write(LINEFEED);
            out.write(TAB);
            out.write(TAB);
            out.write(CURLY_RIGHT);
            out.write(LINEFEED);
        }
        out.write(TAB);
        out.write(ARR_RIGHT);
        out.write(LINEFEED);
        out.write(CURLY_RIGHT);
        out.write(LINEFEED);
        out.flush();
        out.close();
    }

}
