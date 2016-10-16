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


public class HtmlWriter implements Writeable, AutoCloseable {

    private final OutputStream out;

    public HtmlWriter(OutputStream out){
        this.out = out;
    }

    @Override
    public void writeFile(CSV csv) throws IOException {
        Charset charset = csv.getMetaData().getCharset();

        out.write("<html>\n".getBytes());
        out.write("<head>\n".getBytes());

        out.write("</head>\n".getBytes());
        out.write("<body>\n".getBytes());

        // Root
        out.write("<table>\n".getBytes());

        // Header
        out.write("<thead>\n<tr>\n".getBytes());
        for (int x = 0; x < csv.getMetaData().getColumnCount(); x++) {
            Column col = csv.getMetaData().getColumn(x);
            out.write("<th>".getBytes());
            out.write( col.getName().getBytes() );
            out.write("</th>".getBytes());
        }
        out.write("</tr>\n</thead>\n".getBytes());

        // Iterate rows
        out.write("<tbody>\n".getBytes());
        for (int y = 0; y < csv.getRowCount(); y++) {
            out.write("<tr>\n".getBytes());

            Row r = csv.getRow(y);
            for (int x = 0; x < csv.getMetaData().getColumnCount(); x++) {
                out.write("<td>".getBytes());
                Column col = csv.getMetaData().getColumn(x);
                String s = r.getAsString(col);
                if (s == null) {
                } else {
                    out.write(s.getBytes());
                }
                out.write("</td>".getBytes());
            }

            out.write("</tr>\n".getBytes());
        }
        out.write("</tbody>\n".getBytes());

        // Close root element
        out.write("</table>\n".getBytes());
        out.write("</body>\n".getBytes());
        out.write("</html>\n".getBytes());
        out.flush();

    }

    @Override
    public void close() throws Exception {
        out.flush();
        out.close();
    }

}