package org.laukvik.csv.report;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.query.RowMatcher;
import org.laukvik.csv.query.SortOrder;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
     * The list of row matchers.
     */
    private final List<RowMatcher> rowMatcherList;
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
        rowMatcherList = new ArrayList<>();
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
     * Adds a matcer.
     *
     * @param rowMatcher the rowmatcher
     */
    public void addMatcher(final RowMatcher rowMatcher) {
        rowMatcherList.add(rowMatcher);
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
        for (Aggregate a : aggregateList) {
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
                if (s != null && s.equals(value)) {
                    int z = 0;
                    for (Aggregate a : aggregateList) {
                        a.aggregate(row);
                        if (a.getValue() instanceof Integer) {
                            Integer i = (Integer) a.getValue();
//                            System.out.println("Setting: " + i + " for " + value);
                            newRow.setInteger(cols.get(z), i);
                        }
                        z++;
                    }
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

    /**
     * Builds a Node from the CSV.
     *
     * @param csv the CSV
     * @return the Node
     */
    public Node buildNode(final CSV csv) {
        Node root = new Node();
        for (int y = 0; y < csv.getRowCount(); y++) {
            Row r = csv.getRow(y);
            Node extra = root;
            // Exlude non matching rows
            boolean matchesAll = true;
            for (RowMatcher rm : rowMatcherList) {
                if (!rm.matches(r)) {
                    matchesAll = false;
                }
            }
            //
            if (matchesAll) {
                for (int x = 0; x < groups.size(); x++) {
                    Column c = groups.get(x);
                    Object o = r.get(c);
                    extra = extra.add(o, c);
//                    IntegerColumn ic = null;
//                    r.setInteger(ic, extra.getCount());
                }
            }
        }
        return root;
    }

    /**
     * Builds a CSV from the values in the node.
     *
     * @param node the
     * @return the CSV
     */
    public CSV buildCSV(final Node node) {
        CSV csv = new CSV();
        System.out.println("aggregateList: " + aggregateList.size());
        for (Aggregate a : aggregateList) {
            if (a instanceof Name) {
                Name n = (Name) a;
                StringColumn sc = new StringColumn(a.getColumn().getName());
//                a.setAggregateColumn(sc);
                csv.addColumn(sc);
            } else {
                IntegerColumn ic = new IntegerColumn(a.getClass().getSimpleName()+ ":" + a.getColumn().getName());
                a.setAggregateColumn(ic);
                csv.addColumn(ic);
            }
        }
        Deque<Object> list = new ArrayDeque<>();
        build(node, csv, list);
        return csv;
    }

    /**
     * Iterates the whole node tree and adds the values in the node as rows
     * in the CSV.
     *
     * @param n     the node
     * @param csv   the csv
     * @param deque the deque of items (stack)
     */
    public void build(final Node n, final CSV csv, final Deque<Object> deque) {
        if (!n.isRoot()) {
            deque.add(n.getValue());
        }


        if (!n.isRoot()){
            Column c = csv.getColumn(n.getColumn().getName());
            if (c == null){
                c = csv.addColumn(n.getColumn().getName());
            } else {
                c = n.getColumn();
            }

            if (n.isEmpty()){
                // Display path
                for (Object o : deque){
                    System.out.print(o + " -> ");
                }
                System.out.println(n.getCount() + "\tColumns " + csv.getColumnCount() + " "  + c);
                Row r = csv.addRow();

                int x = 0;
                for (Object o : deque) {
                    Column sc = csv.getColumn(x);
                    if (sc instanceof StringColumn) {
                        r.setString((StringColumn) sc, (String) o);
                    }
                    x++;
                }
    //            for (Aggregate a : aggregateList) {
    //                if (a instanceof Count) {
    //                    Count c = (Count) a;
    //                    IntegerColumn ic = c.getAggregateColumn();
    //                    r.setInteger(a.getAggregateColumn(), n.getCount());
    //                }
    //            }
    //        if (n.isEmpty()) {
    //            for (Aggregate a : aggregateList) {
    //                if (a instanceof Count) {
    //                    Count c = (Count) a;
    //                    IntegerColumn ic = c.getAggregateColumn();
    //                    r.setInteger(a.getAggregateColumn(), n.getCount());
    //                }
    //            }
    //        }
            }
        }

        // Iterate the rest of the hierarchy
        for (Object key : n.getMap().keySet()) {
            Node subNode = n.getMap().get(key);
            build(subNode, csv, deque);
        }

        if (!n.isRoot()) {
            deque.removeLast();
        }
    }


}
