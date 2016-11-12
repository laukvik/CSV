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
package org.laukvik.csv;

import org.laukvik.csv.columns.Column;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Specifies information about all the columns in the data set.
 */
public final class MetaData implements Serializable {

    /**
     * The list of columns.
     */
    private final List<Column> columns;
    /**
     * The Character set.
     */
    private Charset charset;
    /**
     * The column separator character.
     */
    private Character separatorChar;
    /**
     * The quote character.
     */
    private Character quoteChar;
    /**
     * The CSV instance it belongs to.
     */
    private CSV csv;

    /**
     * Builds an empty MetaData.
     */
    public MetaData() {
        charset = Charset.defaultCharset();
        columns = new ArrayList<>();
    }

    /**
     * Informs the ChangeListener that the column was updated.
     *
     * @param column the column
     */
    public void fireColumnChanged(final Column column) {
        csv.fireColumnUpdated(column);
    }

    /**
     * Returns the CSV it belongs to.
     *
     * @return the CSV
     */
    public CSV getCSV() {
        return csv;
    }

    /**
     * Sets the CSV it belongs to.
     *
     * @param csv the CSV
     */
    public void setCSV(final CSV csv) {
        this.csv = csv;
    }









}
