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

/**
 * Constants indicating encoding used in text file
 *
 * @author Morten Laukvik
 */
public enum BOM {

    UTF8("utf-8", (byte) 239, (byte) 187, (byte) 191),
    UTF16BE("utf-16be", (byte) 254, (byte) 255),
    UTF16LE("utf-16le", (byte) 255, (byte) 254),
    UTF32BE("utf-32be", (byte) 0, (byte) 0, (byte) 254, (byte) 255),
    UTF32LE("utf-32le", (byte) 0, (byte) 0, (byte) 255, (byte) 254);

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
            boolean isMatching = true;
            for (int x = 0; x < bom.bytes.length; x++) {
                byte b1 = bom.bytes[x];
                if (x < bytes.length) {
                    byte b2 = bytes[x];
                    if (b1 != b2) {
                        isMatching = false;
                    }
                } else {
                    isMatching = false;
                }
            }
            if (isMatching) {
                return bom;
            }
        }
        return null;
    }

}
