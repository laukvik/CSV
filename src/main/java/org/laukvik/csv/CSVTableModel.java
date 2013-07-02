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
    private List<TableModelListener> listeners;

    public CSVTableModel(CSV csv) {
        this.csv = csv;
        this.listeners = new ArrayList<TableModelListener>();
    }

    public void addTableModelListener(TableModelListener l) {
        this.listeners.add(l);
    }

    public void removeTableModelListener(TableModelListener l) {
        this.listeners.remove(l);
    }

    public CSV getCSV() {
        return csv;
    }

    public Class<?> getColumnClass(int column) {
        return String.class;
    }

    public int getColumnCount() {
        return csv.getColumnCount();
    }

    public String getColumnName(int column) {
        if (csv.isHeadersAvailable()) {
            return csv.getHeader(column);
        } else {
            return null;
        }
    }

    public int getRowCount() {
        return csv.getRowCount();
    }

    public Object getValueAt(int row, int column) {
        try {
            return csv.getCell(column, row);
        }
        catch (Exception e) {
            return null;
        }
    }

    public boolean isCellEditable(int row, int column) {
        return true;
    }

    public void setValueAt(Object value, int row, int column) {
        csv.setCell((String) value, column, row);
    }
}
