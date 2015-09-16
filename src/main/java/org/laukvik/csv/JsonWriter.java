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

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class JsonWriter implements AutoCloseable {

    private final OutputStreamWriter out;

    public JsonWriter(OutputStream out) {
        this(out, Charset.defaultCharset());
    }

    public JsonWriter(OutputStream out, Charset charset) {
        this.out = new OutputStreamWriter(out, charset);
    }

    public void write(CSV csv) throws IOException {
        MetaData md = csv.getMetaData();
        out.write("{");
        out.write(CSV.CRLF);
        out.write(CSV.TAB);
        out.write("\"");
        this.out.write("rows");
        out.write("\"");
        this.out.write(":");
        this.out.write(" [");
        for (int y = 0; y < csv.getRowCount(); y++) {
            if (y > 0) {
                out.write(",");
                out.write(CSV.CRLF);
            }
            Row row = csv.getRow(y);
            out.write(CSV.CRLF);
            out.write(CSV.TAB);
            out.write(CSV.TAB);
            out.write("{");
            out.write(CSV.CRLF);
            for (int x = 0; x < md.getColumnCount(); x++) {
                if (x > 0) {
                    out.write(",");
                    out.write(CSV.CRLF);
                }
                out.write(CSV.TAB);
                out.write(CSV.TAB);
                out.write(CSV.TAB);
                out.write("\"");
                out.write(md.getColumnName(x));
                out.write("\"");
                out.write(":");
                out.write("\"");
                out.write(row.getString(x));
                out.write("\"");
            }
            out.write(CSV.CRLF);
            out.write(CSV.TAB);
            out.write(CSV.TAB);
            out.write("}");
            out.write(CSV.CRLF);
        }
        out.write(CSV.TAB);
        this.out.write("]");
        out.write(CSV.CRLF);
        this.out.write("}");
        out.write(CSV.CRLF);
    }

    @Override
    public void close() throws IOException {
        out.flush();
        out.close();
    }

}
