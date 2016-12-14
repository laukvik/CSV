package org.laukvik.csv.report;

import org.laukvik.csv.CSV;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;
import org.laukvik.csv.query.RowMatcher;
import org.laukvik.csv.query.SortOrder;

import java.math.BigDecimal;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Report.
 */
public final class Report {

    /**
     * Columns to doSUM.
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
     * @param aggregate the doSUM feature
     */
    public void addColumn(final Aggregate aggregate) {
        aggregateList.add(aggregate);
    }

    /**
     * Adds a new column to doSUM by.
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
        // Build unique node hierarchy
        Node root = buildNode(csv);
        CSV newCSV = buildCSV(root);
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
                }
                for (Aggregate a : aggregateList) {
                    Column c = a.getColumn();
                    Object v = r.get(c);
                    if (a instanceof Sum) {
                        extra.doSUM(v, c);
                    } else if (a instanceof Min) {
                        extra.doMin(v, c);
                    } else if (a instanceof Max) {
                        extra.doMax(v, c);
                    } else if (a instanceof Count) {
                        extra.doCount(v, c);
                    } else if (a instanceof Name) {
//                        extra.doName(v, c);
                    }
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
        for (Column c : groups) {
            csv.addColumn(c);
        }
        for (Aggregate a : aggregateList) {
            if (a instanceof Name) {
                StringColumn ic = csv.addStringColumn(a.getColumn().getName());
                a.setAggregateColumn(ic);
            } else {
                IntegerColumn ic = csv.addIntegerColumn(a.getClass().getSimpleName());
                a.setAggregateColumn(ic);
            }
        }
        build(node, csv, new ArrayDeque<>());
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
        if (!n.isRoot()) {
            Column c = null;
            if (csv.indexOf(n.getColumn()) < 0) {
                c = csv.addColumn(n.getColumn());
            } else {
                c = n.getColumn();
            }
            if (n.isEmpty()) {
                Row r = csv.addRow();

                int x = 0;
                for (Object o : deque) {
                    Column sc = csv.getColumn(x);
                    if (sc instanceof StringColumn) {
                        r.setString((StringColumn) sc, (String) o);
                    }
                    x++;
                }

                for (Aggregate a : aggregateList) {
                    Column col = a.getAggregateColumn();

                    if (a instanceof Count) {
                        IntegerColumn ic = (IntegerColumn) col;
                        r.setInteger(ic, n.getCount());

                    } else if (a instanceof Min) {
                        IntegerColumn ic = (IntegerColumn) col;
                        BigDecimal bd = n.getMin();
                        if (bd != null) {
                            r.setInteger(ic, bd.intValue());
                        }
                    } else if (a instanceof Max) {
                        IntegerColumn ic = (IntegerColumn) col;
                        BigDecimal bd = n.getMax();
                        if (bd != null) {
                            r.setInteger(ic, bd.intValue());
                        }
                    } else if (a instanceof Sum) {
                        IntegerColumn ic = (IntegerColumn) col;
                        BigDecimal bd = n.getSum();
                        if (bd != null) {
                            r.setInteger(ic, bd.intValue());
                        }
                    } else if (a instanceof Avg) {
                        IntegerColumn ic = (IntegerColumn) col;
                        BigDecimal bd = n.getAverage();
                        if (bd != null) {
                            r.setInteger(ic, bd.intValue());
                        }
                    } else if (a instanceof Name) {
                        Name name = (Name) a;
                        r.setString((StringColumn) a.getAggregateColumn(), (String) n.getValue());

                    }
                }
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
