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
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 *
 * @author Morten Laukvik
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
    private final static char SPACE = ' ';

    private final OutputStream out;
    private File file;

    public JsonWriter(OutputStream out) {
        this.out = out;
    }

    @Override
    public void writeFile(CSV csv) throws IOException {
        Charset charset = csv.getMetaData().getCharset();
        LOG.fine("Writing CSV to JSON.");
        MetaData md = csv.getMetaData();
        out.write(BRACKET_LEFT);
        out.write(LINEFEED);
        for (int y = 0; y < csv.getRowCount(); y++) {
            LOG.log(Level.FINE, "Writing {0}/{1}", new Object[]{y + 1, csv.getRowCount()});
            if (y > 0) {
                out.write(COMMA);
                out.write(LINEFEED);
            }
            Row row = csv.getRow(y);
            out.write(SPACE);
            out.write(CURLY_LEFT);
            out.write(LINEFEED);
            for (int x = 0; x < md.getColumnCount(); x++) {
                Column c = md.getColumn(x);
                if (x > 0) {
                    out.write(COMMA);
                    out.write(LINEFEED);
                }
                out.write(SPACE);
                out.write(SPACE);
                out.write(DOUBLE_QUOTE);
                String s = md.getColumnName(x);
                writeString(s, out);
                out.write(DOUBLE_QUOTE);
                out.write(SEMICOLON);
                out.write(DOUBLE_QUOTE);
                String s2 = row.getAsString(c);

                writeString(s2, out);
                //out.writeFile(row.getAsString(x).getBytes(charset));
                out.write(DOUBLE_QUOTE);
            }
            out.write(LINEFEED);
            out.write(SPACE);
            out.write(CURLY_RIGHT);
        }
        out.write(LINEFEED);
        out.write(BRACKET_RIGHT);
        out.flush();

        LOG.fine("Finished writing CSV to JSON.");
    }

    @Override
    public File getFile() {
        return file;
    }

    private void writeString(String s, OutputStream out) throws IOException {
        if (s == null) {

        } else {
            for (int z = 0; z < s.length(); z++) {
                char c = s.charAt(z);
                if (c == '"') {
                    out.write('\\');
                    out.write(c);
                } else {
                    out.write(c);
                }

            }
        }
    }

    @Override
    public void close() throws IOException {
        out.close();
    }

}
