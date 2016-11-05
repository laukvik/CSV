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
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An interface for writing data sets.
 *
 *
 */
public interface Writeable {

    /**
     * Writes the CSV to the file.
     *
     * @param csv the CSV to write
     * @param file the file
     * @throws IOException when the CSV could not be written
     */
    void writeCSV(CSV csv, File file);

    /**
     * Writes the csv to the outputStream.
     *
     * @param csv          the CSV
     * @param outputStream the outputStream
     * @throws IOException when the csv could not be written
     */
    void writeCSV(CSV csv, OutputStream outputStream) throws IOException;

    /**
     * Writes the single row to the outputStream.
     *
     * @param row          the row
     * @param outputStream the outputStream
     * @throws IOException when the row can't be written
     */
    void writeCSV(Row row, OutputStream outputStream) throws IOException;

    /**
     * Writes the metadata to the outputStream.
     *
     * @param metaData     the metadata
     * @param outputStream the outputStream
     * @throws IOException when the row can't be written
     */
    void writeCSV(MetaData metaData, OutputStream outputStream) throws IOException;

}
