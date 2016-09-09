package org.laukvik.csv.io;

import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BOMTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = BOMTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void checkBytes(){
        assertTrue( BOM.UTF8.is( (byte) 0xEF, (byte) 0xBB , (byte) 0xBF, (byte)220  ));
    }

    @Test
    public void checkBytesUTF8(){
        assertTrue( BOM.UTF8.is( (byte) 0xEF, (byte) 0xBB , (byte) 0xBF ));
    }

    @Test
    public void checkBytesUTF16LE(){
        assertTrue( BOM.UTF16LE.is( (byte)0xFF ,(byte)0xFE ));
    }

    @Test
    public void checkBytesUTF16BE(){
        assertTrue( BOM.UTF16BE.is( (byte)0xFE ,(byte)0xFF ));
    }

    @Test
    public void checkBytesUTF32LE(){
        assertTrue( BOM.UTF32LE.is( (byte)0xFF ,(byte)0xFE ,(byte)0x00 ,(byte)0x00 ));
    }

    @Test
    public void checkBytesUTF32BE(){
        assertTrue( BOM.UTF32BE.is( (byte)0x00 ,(byte)0x00 ,(byte)0xFE ,(byte)0xFF ));
    }

    private File createCharsetFile(String charsetName) throws IOException {
        File file = File.createTempFile("charset_test_","csv");
        Charset cs = Charset.forName(charsetName);
        try(Writer w = new OutputStreamWriter( new FileOutputStream(file), cs)){
            w.write(new String("æøåABCDEFGæøå".getBytes(), cs));
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
        return file;
    }

    @Test
    public void utf8() throws IOException {
        assertEquals(BOM.UTF8, BOM.findBom( createCharsetFile("utf-8") ));
    }

    @Test
    public void utf32le() throws IOException {
        assertEquals(BOM.UTF32LE, BOM.findBom( createCharsetFile("utf-32le") ));
    }

    @Test
    public void utf32be() throws IOException {
        assertEquals(BOM.UTF32BE, BOM.findBom( createCharsetFile("utf-32be") ));
    }

    @Test
    public void utf16le() throws IOException {
        assertEquals(BOM.UTF16LE, BOM.findBom( createCharsetFile("utf-16le") ));
    }

    @Test
    public void utf16be() throws IOException {
        assertEquals(BOM.UTF16BE, BOM.findBom( createCharsetFile("utf-16be") ));
    }

}