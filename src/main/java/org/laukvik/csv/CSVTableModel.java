/*
 * Copyright 2013 Laukviks Bedrifter.
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

import java.util.ArrayList;
import java.util.List;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class CSVTableModel implements TableModel {

    private CSV csv;
    private List<Row> rows;
    private final List<TableModelListener> listeners;
    private MetaData metaData;

    public CSVTableModel(CSV csv) {
        this.csv = csv;
        this.listeners = new ArrayList<>();
        this.rows = csv.getRows();
        this.metaData = csv.getMetaData();
    }

    public CSVTableModel(List<Row> rows, MetaData md) {
        this.rows = rows;
        this.metaData = md;
        this.listeners = new ArrayList<>();
    }

    public CSVTableModel() {
        this.rows = rows = new ArrayList<>();
        this.listeners = new ArrayList<>();
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
        this.listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        this.listeners.remove(l);
    }

    public CSV getCSV() {
        return csv;
    }

    @Override
    public Class<?> getColumnClass(int column) {
        return String.class;
    }

    @Override
    public int getColumnCount() {
        return metaData.getColumnCount();
    }

    @Override
    public String getColumnName(int column) {
        return metaData.getColumn(column).getName();
    }

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public Object getValueAt(int row, int column) {
        try {
            Row r = rows.get(row);
//            return csv.getRow(row).getString(column);
            return r.getString(column);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
//        csv.getRow(row).setString(value.toString(), column);
        Row r = rows.get(row);
        r.setString((String) value, column);
    }
}
