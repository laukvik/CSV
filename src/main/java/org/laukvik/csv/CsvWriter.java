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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * OutputStream for writing CSV data
 *
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CsvWriter implements AutoCloseable {

    private final OutputStreamWriter out;
    private final Pattern pattern;

    public CsvWriter(OutputStream out) {
        this(out, CSV.CHARSET_DEFAULT);
    }

    public CsvWriter(OutputStream out, Charset charset) {
        this.out = new OutputStreamWriter(out, charset);
        pattern = Pattern.compile("[0-9]+");
    }

    private boolean isDigitsOnly(String value) {
        return pattern.matcher(value).find();
    }

    public void writeMetaData(MetaData metaData) throws IOException {
        writeValues(metaData.getValues());
    }

    public void writeRow(Row row) throws IOException {
        writeValues(row.getValues());
    }

    public void writeRow(String... values) throws IOException {
        writeValues(Arrays.asList(values));
    }

    private void writeValues(List<String> values) throws IOException {
        for (int x = 0; x < values.size(); x++) {
            if (x > 0) {
                out.write(CSV.COMMA);
            }
            String column = values.get(x);
            if (isDigitsOnly(column)) {
                /* Digits only */
                out.write(column);
            } else {
                /* Text */
                out.write(CSV.QUOTE);
                for (int n = 0; n < column.length(); n++) {
                    char ch = column.charAt(n);
                    if (ch == CSV.QUOTE) {
                        /* Encode quotes - write an extra quote */
                        out.write(CSV.QUOTE);
                    }
                    out.write(ch);
                }
                out.write(CSV.QUOTE);
            }
        }
        out.write(CSV.CRLF);
    }

    @Override
    public void close() throws IOException {
        out.flush();
        out.close();
    }

}
