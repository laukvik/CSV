package org.laukvik.csv.report;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.query.SortOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Report.
 */
public final class Report {

    /**
     * Columns to aggregate.
     */
    private final List<Aggregate> aggregateList;
    /**
     * Group by.
     */
    private final List<Column> groups;
    /**
     * Sort by.
     */
    private final List<SortOrder> sortOrderList;

    public Report() {
        aggregateList = new ArrayList<>();
        groups = new ArrayList<>();
        sortOrderList = new ArrayList<>();
    }

    public void addColumn(final Aggregate aggregate) {
        aggregateList.add(aggregate);
    }

    public void addGroup(final Column column) {
        groups.add(column);
    }

    public void addSort(final SortOrder sortOrder) {
        sortOrderList.add(sortOrder);
    }

    /**
     * Builds a new CSV with the aggregated data.
     * @param csv the csv with the data
     * @return the new csv
     */
    public CSV buildReport(final CSV csv) {
        CSV c = new CSV();
        for (int y= 0; y< csv.getRowCount(); y++){
            Row row = csv.getRow(y);
            for (Aggregate a : aggregateList){
                a.aggregate(row);
            }
        }
        for (Aggregate a : aggregateList){
            System.out.println(a.getClass().getSimpleName() + " " + a.getValue());
        }
        return c;
    }

}
