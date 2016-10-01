package org.laukvik.csv.swing;

import org.laukvik.csv.CSV;
import org.laukvik.csv.CSVTest;
import org.laukvik.csv.ui.ExplorerFrame;

import java.io.File;
import java.io.IOException;

/**
 * @author Morten Laukvik
 */
public class ExplorerFrameTest {

    public static File getResource(String filename) {
        ClassLoader classLoader = CSVTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    public static void main(String[]args) throws IOException {
        CSV csv = new CSV();
        csv.readFile(getResource("presidents.csv"));
        ExplorerFrame frame = new ExplorerFrame(csv);
        frame.setVisible(true);
    }


}