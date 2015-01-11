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
package org.laukvik.csv.swing;

import java.io.File;
import org.junit.Assert;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.laukvik.csv.CSV;
import org.laukvik.csv.CSVTableModel;
import org.laukvik.csv.CsvReaderTest;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class ViewerTest {

    Viewer view;

    public ViewerTest() {
    }

    @Before
    public void setUp() throws Exception {
        view = new Viewer();
    }

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvReaderTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

    @Test
    public void readTableData() {
        try {
            CSV csv = new CSV(getResource("quoted.csv"));
            view = new Viewer();
            view.setSize(700, 400);
            view.setLocationRelativeTo(null);
            view.setVisible(true);
            view.openCSV(csv);
            CSVTableModel model = view.getModel();
            Assert.assertEquals("Header1", "Lead", model.getColumnName(0));
            Assert.assertEquals("Header2", "Title", model.getColumnName(1));
            Assert.assertEquals("Header3", "Phone", model.getColumnName(2));
            Assert.assertEquals("Header4", "Notes", model.getColumnName(3));

            Assert.assertEquals("Column1", new String("Jim Grayson"), model.getValueAt(0, 0));
            Assert.assertEquals("Column2", new String("Senior Manager"), model.getValueAt(0, 1));
            Assert.assertEquals("Column3", new String("(555)761-2385"), model.getValueAt(0, 2));
            Assert.assertEquals("Column4", new String("Spoke Tuesday, he's interested"), model.getValueAt(0, 3));

            Assert.assertEquals("RowCount", 3, model.getRowCount());
            Assert.assertEquals("ColumnCount", 4, model.getColumnCount());


        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
