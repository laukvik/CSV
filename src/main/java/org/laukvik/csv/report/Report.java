package org.laukvik.csv.report;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.query.SortOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    /**
     * Creates a new empty report.
     */
    public Report() {
        aggregateList = new ArrayList<>();
        groups = new ArrayList<>();
        sortOrderList = new ArrayList<>();
    }

    /**
     * Adds a new aggregated column feature.
     *
     * @param aggregate the aggregate feature
     */
    public void addColumn(final Aggregate aggregate) {
        aggregateList.add(aggregate);
    }

    /**
     * Adds a new column to aggregate by.
     *
     * @param column the column
     */
    public void addGroup(final Column column) {
        groups.add(column);
    }

    /**
     * Adds a new sort order.
     *
     * @param sortOrder the sort order
     */
    public void addSort(final SortOrder sortOrder) {
        sortOrderList.add(sortOrder);
    }

    /**
     * Builds a new CSV with the aggregated data.
     *
     * @param csv the csv with the data
     * @return the new csv
     */
    public CSV buildReport(final CSV csv) {
        CSV newCSV = new CSV();

        StringColumn sc = (StringColumn) groups.get(0);
        Set<String> unique = csv.buildDistinctValues(sc);

        StringColumn sc2 = newCSV.addStringColumn(sc.getName());

        List<IntegerColumn> cols = new ArrayList<>();
        for (Aggregate a : aggregateList){
            IntegerColumn ic = new IntegerColumn(a.getColumn().getName());
            newCSV.addColumn(ic);
            cols.add(ic);
        }
        // Iterate through each unique
        for (String value : unique) {
            // Add unique
            Row newRow = newCSV.addRow();
            newRow.setString(sc2, value);
            // Iterate all value matching each unique
            for (int y = 0; y < csv.getRowCount(); y++) {
                Row row = csv.getRow(y);
                String s = row.getString(sc);
                if (s != null && s.equals(value)){
//                    System.out.println("Unik: " + value  + " Found: " + s );
                    int z = 0;
                    for (Aggregate a : aggregateList) {
                        a.aggregate(row);
                        if (a.getValue() instanceof Integer){
                            Integer i = (Integer) a.getValue();
                            System.out.println("Setting: " + i + " for " + value);
                            newRow.setInteger(cols.get(z), i);
                        }

//                        System.out.print(a.getValue() + "\t");
                        z++;
                    }
//                    System.out.println();
                }
            }
            // Reset aggregate after each unique
            for (Aggregate a : aggregateList) {
//                a.reset();
            }
        }

        System.out.println();
        return newCSV;
    }



}
