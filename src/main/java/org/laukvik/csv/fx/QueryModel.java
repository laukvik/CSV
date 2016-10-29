package org.laukvik.csv.fx;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.query.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a selection of multiple columns and values in a flat structure
 *
 * @author Morten Laukvik
 */
public class QueryModel {

    final Main main;
    private final CSV csv;
    private final List<Selection> selections;

    public QueryModel(final CSV csv, final Main main){
        this.csv = csv;
        this.main = main;
        this.selections = new ArrayList<>();
    }

    public void clearSelections(){
        this.selections.clear();
    }

    public List<ObservableRow> buildObservableRows(){
        List<ObservableRow> list = new ArrayList<>();
        if (this.isEmpty()){
            csv.clearQuery();
            for (int y = 0; y < csv.getRowCount(); y++) {
                list.add(new ObservableRow(csv.getRow(y), main));
            }
        } else {
            buildQuery();
            for (Row r : csv.getQuery().getResultList()) {
                list.add(new ObservableRow(r, main));
            }
        }
        return list;
    }

    private void buildQuery(){
        Query.Where where = csv.findByQuery().where();
        for (Selection s : selections){
            String [] arr = new String[ s.getValues().size() ];
            int x = 0;
            for (String v : s.getValues()){
                arr[x] = v;
                x++;
            }
            where = where.column(s.getColumn()).in(arr);
        }
    }

    public Selection findSelectionByColumn(Column column){
        for (Selection s : selections){
            if (s.getColumn().equals(column)){
                return s;
            }
        }
        return null;
    }

    public void addSelection(Column column, String value){
        Selection s = findSelectionByColumn(column);
        if (s == null){
            s = new Selection(column);
            s.addValue(value);
            this.selections.add(s);
        } else {
            s.addValue(value);
        }
    }

    public void removeSelection(Column column, String value){
        Selection s = findSelectionByColumn(column);
        if (s != null){
            s.removeValue(value);
            if (s.isEmpty()){
                this.selections.remove(s);
            }
        }
    }

    public boolean isEmpty() {
        return selections.isEmpty();
    }

    public boolean isSelected(final Column column, final String value) {
        Selection s = findSelectionByColumn(column);
        if (s != null){
            for (String v : s.getValues()){
                if (v.equals(value)){
                    return true;
                }
            }
        }
        return false;
    }
}
