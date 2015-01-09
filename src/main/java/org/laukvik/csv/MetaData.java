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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class MetaData implements Serializable {

    private List<String> columnNames;

    public MetaData() {
        columnNames = new ArrayList<>();
    }

    public MetaData(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public MetaData(String... columns) {
        this.columnNames = new ArrayList<>();
        columnNames.addAll(Arrays.asList(columns));
    }

    public int getColumnCount() {
        return columnNames.size();
    }

    public String getColumnName(int column) {
        return columnNames.get(column);
    }

    public void setColumnName(String name, int column) {
        columnNames.set(column, name);
    }

    protected void addColumn(String columnName) {
        columnNames.add(columnName);
    }

    public int getColumnIndex(String column) {
        int x = 0;
        for (String c : columnNames) {
            if (c.equalsIgnoreCase(column)) {
                return x;
            }
            x++;
        }
        return x;
    }

    public List<String> getValues() {
        return columnNames;
    }

}
