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
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import org.laukvik.csv.CSV;
import org.laukvik.csv.ChangeListener;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.StringColumn;

import java.util.List;

/**
 * Represents a row in JavaFX.
 */
class ObservableRow implements javafx.beans.value.ChangeListener<String> {

    final ChangeListener listener;
    private final List<SimpleStringProperty> items;
    private final Row row;

    /**
     * Builds a new ObservableRow from the Row
     *
     * @param row      the row
     * @param listener the listener
     */
    public ObservableRow(Row row, ChangeListener listener) {
        this.row = row;
        this.listener = listener;
        items = FXCollections.observableArrayList();
        CSV csv = row.getCSV();
        MetaData md = csv.getMetaData();
        for (int x = 0; x < md.getColumnCount(); x++) {
            Column col = md.getColumn(x);
            SimpleStringProperty ssp = new SimpleStringProperty(row.getAsString(col));
            ssp.addListener(this);
            items.add(ssp);
        }
    }

    public SimpleStringProperty getValue(int columnIndex) {
        return items.get(columnIndex);
    }

    @Override
    public boolean equals(final Object o) {
        ObservableRow or = (ObservableRow) o;
        return row.equals(or.row);
    }

    @Override
    public void changed(final ObservableValue<? extends String> observable, final String oldValue, final String newValue) {
        int columnIndex = items.indexOf(observable);
        int rowIndex = row.indexOf();
        Column column = row.getCSV().getMetaData().getColumn(columnIndex);
        if (column instanceof StringColumn) {
            row.update((StringColumn) column, newValue);
        }
        listener.cellUpdated(columnIndex, rowIndex);
    }
}
