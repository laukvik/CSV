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
package org.laukvik.csv.io;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Writes the data set to the ResourceBundle.
 */
public final class ResourceBundleWriter extends AbstractResourceBundle implements DatasetFileWriter {

    /**
     * The folder to read from.
     */
    private File folder;
    /**
     * The base filename to read. The bundle name.
     */
    private String basename;

    /**
     * Writes the data set to the ResourceBundle.
     * <p>
     * language.properties
     */
    public ResourceBundleWriter() {
    }

    /**
     * Writes the CSV to the file.
     *
     * @param file the file
     * @param csv  the CSV
     * @throws IOException when the resourcebundle could not be written
     */
    public void writeCSV(final File file, final CSV csv) throws IOException {
        this.folder = file.getParentFile();
        this.basename = getBasename(file);
        for (int x = 1; x < csv.getColumnCount(); x++) {
            StringColumn column = (StringColumn) csv.getColumn(x);
            writeBundle(csv, column);
        }
    }

    /**
     * Writes a single language to file.
     *
     * @param csv    the CSV
     * @param column the column with the language
     * @throws IOException when the file could not be written
     */
    private void writeBundle(final CSV csv, final StringColumn column) throws IOException {
        String filename = getFilename(column, basename);
        File bundleFile = new File(folder, filename);
        StringColumn keyColumn = (StringColumn) csv.getColumn(0);
        FileWriter writer = new FileWriter(bundleFile);
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            writer.write(r.getString(keyColumn));
            writer.write("=");
            writer.write(r.getString(column));
            writer.write("\r\n");
        }
    }

}
