package org.laukvik.csv.io;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Constants indicating encoding used in text file.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Byte_order_mark">Byte Order Mark (wikipedia)</a>
 */
public enum BOM {

    /**
     * BOM for UTF32BE.
     */
    UTF32BE("utf-32be", (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF),
    /** BOM for UTF32LE. */
    UTF32LE("utf-32le", (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00),
    /** BOM for UTF16BE. */
    UTF16BE("utf-16be", (byte) 0xFE, (byte) 0xFF),
    /** BOM for UTF16LE. */
    UTF16LE("utf-16le", (byte) 0xFF, (byte) 0xFE),
    /** BOM for UTF8. */
    UTF8("utf-8", (byte) 0xEF, (byte) 0xBB, (byte) 0xBF);
    /**
     * Maximum BOM size.
     */
    private static final int MAX_BOM_SIZE = 8;
    /** The bytes that recognizes the BOM. */
    private final byte[] bytes;
    /** The associated Charset for the BOM. */
    private final String charset;

    /**
     * Creates a new BOM with the specified BOM and bytes.
     *
     * @param charsetName the charset
     * @param chars   the chars
     * @see Charset
     */
    BOM(final String charsetName, final byte... chars) {
        this.charset = charsetName;
        this.bytes = chars;
    }

    /**
     * Detects any encoding from file using BOM.
     *
     * @param file the file
     * @return the BOM
     */
    public static BOM findBom(final File file) {
        try (InputStream is = new FileInputStream(file)) {
            byte[] bytes = new byte[MAX_BOM_SIZE];
            is.read(bytes, 0, MAX_BOM_SIZE);
            return BOM.parse(bytes);
        } catch (Exception e) {
            return null;
        }
    }



    /**
     * Parses and detects any BOM if present.
     *
     * @param bytes the bytes to check
     * @return the BOM found
     */
    private static BOM parse(final byte... bytes) {
        for (BOM bom : values()) {
            if (bom.is(bytes)) {
                return bom;
            }
        }
        return null;
    }

    /**
     * Returns the Charset.
     *
     * @return the Charset
     */
    public Charset getCharset() {
        return Charset.forName(charset);
    }

    /**
     * Returns whether the bytes matches the current.
     *
     * @param values the bytes
     * @return true if it matches
     */
    public boolean is(final byte... values) {
        if (values.length == 0 || values.length < this.bytes.length) {
            return false;
        }
        byte[] sameBytes = Arrays.copyOfRange(values, 0, this.bytes.length);
        return Arrays.equals(sameBytes, this.bytes);
    }

    /**
     * Returns the bytes for this BOM.
     *
     * @return the bytes
     */
    public byte[] getBytes() {
        return bytes;
    }
}
