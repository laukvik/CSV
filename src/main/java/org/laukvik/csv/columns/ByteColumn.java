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

/**
 * Column with byte array as the data type
 */
public class ByteColumn extends Column<byte[]> {

    /**
     * Column with byte array as the data type
     *
     * @param name the name of the column
     */
    public ByteColumn(String name) {
        super(name);
    }

    @Override
    public String asString(byte[] value) {
        return value.toString();
    }

    @Override
    public byte[] parse(String value) {
        return value.getBytes();
    }

    public int compare(byte[] one, byte[] another) {
        if (Arrays.equals(one, another)) {
            return 0;
        }
        return 1;
    }

    @Override
    public String toString() {
        return getName() + "(Byte)";
    }
}
