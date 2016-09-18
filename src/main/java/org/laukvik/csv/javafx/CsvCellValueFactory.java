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

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class CsvCellValueFactory extends PropertyValueFactory<Row, String> {

    int columnIndex;
    CSV csv;
    TableView<Row> tableView;

//    public CsvCellValueFactory(int columnIndex, CSV csv, TableView<Row> tableView) {
//        this.columnIndex = columnIndex;
//        this.csv = csv;
//        this.tableView = tableView;
//    }
    public CsvCellValueFactory(String property) {
        super(property);
    }
}
