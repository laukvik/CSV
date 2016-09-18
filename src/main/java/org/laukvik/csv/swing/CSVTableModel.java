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
package org.laukvik.csv.swing;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.query.Query;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.List;

public class CSVTableModel implements TableModel {

    private final CSV csv;
    private final List<TableModelListener> listeners;
    private final Query query;
    private List<Row> rows;

    public CSVTableModel(CSV csv) {
        this.csv = csv;
        this.listeners = new ArrayList<>();
        this.query = null;
    }

    public CSVTableModel(CSV csv, Query query) {
        this.csv = csv;
        this.listeners = new ArrayList<>();
        this.query = query;
        this.rows = query.getResultList();
    }

    public int getMaxColumnWidth(Column column) {
        int x = column.getName().length();
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row row = csv.getRow(y);
            if (column instanceof StringColumn) {
                StringColumn vcc = (StringColumn) column;
                String s = row.getString(vcc);
                if (s != null) {
                    if (s.length() > x) {
                        x = s.length();
                    }
                }
            }
        }
        return x;
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
        return csv.getMetaData().getColumnCount();
    }

    @Override
    public String getColumnName(int column) {
        return csv.getMetaData().getColumn(column).getName();
    }

    @Override
    public int getRowCount() {
        return csv.getQuery() == null ? csv.getRowCount() : rows.size();
    }

    @Override
    public Object getValueAt(int row, int columnIndex) {
        try {
            StringColumn column = (StringColumn) csv.getMetaData().getColumn(columnIndex);
            return csv.getQuery() == null ? csv.getRow(row).getString(column) : rows.get(row).getString(column);
        }
        catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return true;
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        Row r = csv.getRow(row);
        Column c = csv.getMetaData().getColumn(column);
        //r.update(c, (String) value);
    }
}
