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
import java.util.logging.Logger;
import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;

/**
 *
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class JsonWriter implements Writeable {

    private final static Logger LOG = Logger.getLogger(JsonWriter.class.getName());

    private final static char CURLY_LEFT = '{';
    private final static char CURLY_RIGHT = '}';
    private final static char SEMICOLON = ':';
    private final static char DOUBLE_QUOTE = '"';
    private final static char BRACKET_LEFT = '[';
    private final static char BRACKET_RIGHT = ']';
    private final static char LINEFEED = '\n';
    private final static char TAB = '\t';
    private final static char COMMA = ',';

    @Override
    public void write(CSV csv, OutputStream out, Charset charset) throws IOException {
        LOG.fine("Writing CSV to JSON.");
        MetaData md = csv.getMetaData();
        out.write(BRACKET_LEFT);
        out.write(LINEFEED);
        for (int y = 0; y < csv.getRowCount(); y++) {
            LOG.fine("Writing " + (y + 1) + "/" + csv.getRowCount());
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
                out.write(DOUBLE_QUOTE);
                out.write(md.getColumnName(x).getBytes(charset));
                out.write(DOUBLE_QUOTE);
                out.write(SEMICOLON);
                out.write(DOUBLE_QUOTE);
                out.write(row.getString(x).getBytes(charset));
                out.write(DOUBLE_QUOTE);

            }
            out.write(LINEFEED);
            out.write(TAB);
            out.write(TAB);
            out.write(CURLY_RIGHT);
            out.write(LINEFEED);
        }
        out.write(BRACKET_RIGHT);
        out.flush();
        out.close();
        LOG.fine("Finished writing CSV to JSON.");
    }

}
