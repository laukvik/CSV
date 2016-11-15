package org.laukvik.csv.io;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
        assertTrue(Arrays.equals(new byte[]{(byte) 0xFF, (byte) 0xFE, (byte) 0x00, (byte) 0x00}, BOM.UTF32LE.getBytes()));
    }

    @Test
    public void checkBytesUTF32BE(){
        assertTrue( BOM.UTF32BE.is( (byte)0x00 ,(byte)0x00 ,(byte)0xFE ,(byte)0xFF ));
    }

    private File createCharsetFile(BOM bom) throws IOException {
        File file = File.createTempFile("charset_test",".csv");
        try (
                FileOutputStream outputStream = new FileOutputStream(file);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, bom.getCharset());
                BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter)
        ) {
            outputStream.write(bom.getBytes());
            bufferedWriter.write("ABA");
            bufferedWriter.newLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        return file;
    }

    @Test
    public void utf8() throws IOException {
        assertEquals(BOM.UTF8, BOM.findBom( createCharsetFile(BOM.UTF8) ));
        assertNull(BOM.findBom(getResource("charset_utf_8.csv")));
    }

    @Test
    public void utf32le() throws IOException {
        assertEquals(BOM.UTF32LE, BOM.findBom( createCharsetFile(BOM.UTF32LE) ));
    }

    @Test
    public void utf32be() throws IOException {
        assertEquals(BOM.UTF32BE, BOM.findBom( createCharsetFile(BOM.UTF32BE) ));
    }

    @Test
    public void utf16le() throws IOException {
        assertEquals(BOM.UTF16LE, BOM.findBom( createCharsetFile(BOM.UTF16LE) ));
    }

    @Test
    public void utf16be() throws IOException {
        assertEquals(BOM.UTF16BE, BOM.findBom( createCharsetFile( BOM.UTF16BE ) ));
    }

    @Test
    public void testNulls() throws IOException {
        assertFalse(BOM.UTF8.is());
    }

    @Test
    public void test() throws IOException {
        assertNull(BOM.findBom( new File("doesnt_exist") ));
    }

}