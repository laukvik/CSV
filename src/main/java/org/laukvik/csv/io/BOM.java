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
 * Constants indicating encoding used in text file
 *
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

    private BOM(String charset, byte... chars) {
        this.charset = charset;
        this.bytes = chars;
    }

    public Charset getCharset() {
        return Charset.forName(charset);
    }

    /**
     * Detects any encoding from file using BOM
     *
     * @param file
     * @return
     */
    public static BOM findBom(File file) {
        try (InputStream is = new FileInputStream(file)) {
            byte[] bytes = new byte[5];
            is.read(bytes, 0, 5);

            System.out.println( 1 + " - " + Integer.toHexString(bytes[0]& 0xFF));
            System.out.println( 2 + " - " + Integer.toHexString(bytes[1]& 0xFF));
            System.out.println( 3 + " - " + Integer.toHexString(bytes[2]& 0xFF));
            System.out.println( 4 + " - " + Integer.toHexString(bytes[3]& 0xFF));
            System.out.println( 5 + " - " + Integer.toHexString(bytes[4]& 0xFF));
//
            System.out.println();
            System.out.println( new String(bytes) );

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
    public static BOM parse(byte... bytes) {
        for (BOM bom : values()) {
            if (bom.is(bytes)){
                return bom;
            }
        }
        return null;
    }

    public boolean is(final byte... bytes) {
        if (bytes.length < this.bytes.length){
            return false;
        }
        byte [] sameBytes = Arrays.copyOfRange(bytes, 0, this.bytes.length);
        return Arrays.equals( sameBytes, this.bytes);
    }
}
