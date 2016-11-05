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
package org.laukvik.csv.columns;

/**
 * Represents a link to another column.
 */
public final class ForeignKey {

    /**
     * The target table.
     */
    private String table;
    /**
     * The target column.
     */
    private String column;

    /**
     * Creates a new ForeignKey.
     *
     * @param tableName the table
     * @param columnName the column
     */
    public ForeignKey(final String tableName, final String columnName) {
        this.table = tableName;
        this.column = columnName;
    }

    /**
     * Creates a new ForeignKey.
     * @param tableName the table
     */
    public ForeignKey(final String tableName) {
        this.table = tableName;
    }

    /**
     * Returns the table it points to.
     *
     * @return the table it points to.
     */
    public String getTable() {
        return table;
    }

    /**
     * Sets the target table name.
     *
     * @param tableName the table name
     */
    public void setTable(final String tableName) {
        this.table = tableName;
    }

    /**
     * Returns the column it points to.
     * @return the column it points to.
     */
    public String getColumn() {
        return column;
    }

    /**
     * Sets the target column name.
     *
     * @param columnName the column name
     */
    public void setColumn(final String columnName) {
        this.column = columnName;
    }

}
