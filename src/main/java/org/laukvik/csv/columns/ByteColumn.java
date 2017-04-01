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
package org.laukvik.csv.columns;

import java.util.Arrays;
import java.util.Base64;

/**
 * Column with byte array as the data type.
 */
public final class ByteColumn extends Column<byte[]> {

    /**
     * Column with byte array as the data type.
     *
     * @param name the name of the column
     */
    public ByteColumn(final String name) {
        super(name);
    }

    /**
     * Represents the value as a String.
     *
     * @param value the value
     * @return the value as a String
     */
    @Override
    public String asString(final byte[] value) {
        return new String(Base64.getMimeEncoder().encode(value));
    }

    /**
     * Parses the string and returns a byte array.
     *
     * @param value the string
     * @return byte array
     */
    @Override
    public byte[] parse(final String value) {
        return Base64.getMimeDecoder().decode(value);
    }

    /**
     * Compares two byte arrays.
     *
     * @param one     one column
     * @param another another column
     * @return the comparable value
     */
    @Override
    public int compare(final byte[] one, final byte[] another) {
        if (one == null || another == null) {
            if (one == null && another == null) {
                return 0;
            }
            if (one == null) {
                return -1;
            } else {
                return 1;
            }
        } else {
            if (Arrays.equals(one, another)) {
                return 0;
            } else if (one.length < another.length) {
                return -1;
            } else {
                return 1;
            }
        }
    }

}
