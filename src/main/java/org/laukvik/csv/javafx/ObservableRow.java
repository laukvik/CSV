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
package org.laukvik.csv.javafx;

import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import org.laukvik.csv.Row;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class ObservableRow implements Observable {

    private Row row;
    private List<SimpleStringProperty> items;

    public ObservableRow(Row row) {
        items = FXCollections.observableArrayList();
        for (int x = 0; x < row.getMetaData().getColumnCount(); x++) {
            items.add(new SimpleStringProperty(row.getString(x)));
        }
    }

    public SimpleStringProperty getValue(int columnIndex) {
        return items.get(columnIndex);
    }

    @Override
    public void addListener(InvalidationListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
