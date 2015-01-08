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
import java.util.regex.Pattern;

/**
 * OutputStream for writing CSV data
 *
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CsvOutputStream implements AutoCloseable {

    private final OutputStreamWriter out;
    private char separator = CSV.COMMA;
    private final Pattern pattern;

    public CsvOutputStream(OutputStream out) {
        this.out = new OutputStreamWriter(out);
        pattern = Pattern.compile("[0-9]+");

    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }

    public char getSeparator() {
        return separator;
    }

    public void writeHeader(String... headers) throws IOException {
        writeLine(headers);
    }

    public void writeLine(String... columns) throws IOException {
        for (int x = 0; x < columns.length; x++) {
            if (x > 0) {
                out.write(separator);
            }
            String column = columns[x];
            if (pattern.matcher(column).find()) {
                /* Digits only */
                out.write(column);
            } else {
                /* Text */
                out.write(CSV.QUOTE);
                for (int n = 0; n < column.length(); n++) {
                    char ch = column.charAt(n);
                    if (ch == CSV.QUOTE) {
                        out.write(CSV.QUOTE);
                    }
                    out.write(ch);
                }
                out.write(column);
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
