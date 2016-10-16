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
 * @author Morten Laukvik
 */
public enum BOM {

    UTF32BE("utf-32be", (byte) 0x00, (byte) 0x00, (byte) 0xFE, (byte) 0xFF),
    UTF32LE("utf-32le", (byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00),
    UTF16BE("utf-16be", (byte) 0xFE, (byte) 0xFF),
    UTF16LE("utf-16le", (byte) 0xFF, (byte) 0xFE),
    UTF8("utf-8", (byte) 0xEF, (byte) 0xBB, (byte) 0xBF);

    private final byte[] bytes;
    private final String charset;

    BOM(String charset, byte... chars) {
        this.charset = charset;
        this.bytes = chars;
    }

    /**
     * Detects any encoding from file using BOM
     *
     * @param file the file
     * @return the BOM
     */
    public static BOM findBom(File file) {
        try (InputStream is = new FileInputStream(file)) {
            byte[] bytes = new byte[8];
            is.read(bytes, 0, 8);
            return BOM.parse(bytes);
        }
        catch (Exception e) {
            return null;
        }
    }

    /**
     * Parses and detects any encoding if present
     *
     * @param bytes
     * @return
     */
    private static BOM parse(byte... bytes) {
        for (BOM bom : values()) {
            if (bom.is(bytes)){
                return bom;
            }
        }
        return null;
    }

    /**
     * Returns the Charset
     *
     * @return the Charset
     */
    public Charset getCharset() {
        return Charset.forName(charset);
    }

    /**
     * Returns whether the bytes matches the current
     *
     * @param bytes the bytes
     * @return true if it matches
     */
    public boolean is(final byte... bytes) {
        if (bytes.length < this.bytes.length){
            return false;
        }
        byte [] sameBytes = Arrays.copyOfRange(bytes, 0, this.bytes.length);
        return Arrays.equals( sameBytes, this.bytes);
    }

    /**
     * Returns the bytes for this BOM
     *
     * @return the bytes
     */
    public byte[] getBytes() {
        return bytes;
    }
}
