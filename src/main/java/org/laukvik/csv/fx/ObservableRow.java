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
package org.laukvik.csv.fx;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import org.laukvik.csv.CSV;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

import java.util.List;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
class ObservableRow {

    private final List<SimpleStringProperty> items;
    private Row row;

    public ObservableRow(Row row) {
        this.row = row;
        items = FXCollections.observableArrayList();
        CSV csv = row.getCSV();
        MetaData md = csv.getMetaData();
        for (int x = 0; x < md.getColumnCount(); x++) {
            Column col = md.getColumn(x);
            items.add(new SimpleStringProperty(row.getAsString(col)));
        }
    }

    public SimpleStringProperty getValue(int columnIndex) {
        return items.get(columnIndex);
    }

    @Override
    public boolean equals(final Object o) {
        ObservableRow or = (ObservableRow)o;
        return row.equals( or.row );
    }

}
