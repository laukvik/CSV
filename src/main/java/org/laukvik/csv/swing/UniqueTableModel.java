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
package org.laukvik.csv.swing;

import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.DateColumn;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 *
 * @author Morten Laukvik
 * @param <T>
 */
public class UniqueTableModel<T> implements TableModel {

    private final List<Unique<T>> uniqueValues;
    private final List<TableModelListener> listeners;
    private final List<UniqueListener> changeListeners;
    private final Column column;
    private final Map<T, Integer> map;

    public UniqueTableModel(Column column) {
        uniqueValues = new ArrayList<>();
        listeners = new ArrayList<>();
        changeListeners = new ArrayList<>();
        this.column = column;
        map = new TreeMap<>();
    }

    public void buildValues() {
        for (T key : map.keySet()) {
            uniqueValues.add(new Unique<T>(key, map.get(key)));
        }
    }

    public void addValue(T word) {
        if (word == null) {
            return;
        }
        if (map.containsKey(word)) {
            Integer count = map.get(word) + 1;
            map.put(word, count);
        } else {

            map.put(word, 1);
        }
    }

    public String getColumnName() {
        return column.getName();
    }

    public Column getColumn() {
        return column;
    }


    public void addChangeListener(UniqueListener listener) {
        changeListeners.add(listener);
    }

    public void removeChangeListener(UniqueListener listener) {
        changeListeners.remove(listener);
    }

    @Override
    public int getRowCount() {
        return uniqueValues.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return "";
            case 1:
                return "Value";
            case 2:
                return "Count";
            default:
                return "";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Boolean.class;
            case 1:
                return String.class;
            case 2:
                return Integer.class;
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return true;
            case 1:
                return false;
            case 2:
                return false;
            default:
                return false;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Unique u = uniqueValues.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return u.isSelected();
            case 1:
                Object o = u.getValue();
                if (o instanceof Date) {
                    Date d = (Date) o;
                    DateColumn dc = (DateColumn) column;
                    return dc.getDateFormat().format(d);
                }
                return u.getValue();
            case 2:
                return u.getCount();
            default:
                return null;
        }
    }

    public Set<T> getSelection() {
        Set<T> selection = new TreeSet<>();
        for (Unique<T> uq : uniqueValues) {
            if (uq.isSelected()) {
                selection.add(uq.getValue());
            }
        }
        return selection;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Unique u = uniqueValues.get(rowIndex);
        if (columnIndex == 0) {
            u.setSelected((Boolean) aValue);
            for (UniqueListener l : changeListeners) {
                l.uniqueSelectionChanged(this);
            }
        }
    }


    @Override
    public void addTableModelListener(TableModelListener l) {
        listeners.add(l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listeners.remove(l);
    }

}