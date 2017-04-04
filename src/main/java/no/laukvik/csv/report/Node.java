package no.laukvik.csv.report;

import no.laukvik.csv.columns.Column;
import no.laukvik.csv.columns.IntegerColumn;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Counts the amount of instances for a given value.
 */
public final class Node {

    /**
     * The value.
     */
    private Object value;
    /**
     * The sorted map of sub elements.
     */
    private Map<Object, Node> map;
    /**
     * The column type.
     */
    private Column column;
    /**
     * How many times it contains this value.
     */
    private int count;
    /**
     * The parent node.
     */
    private Node parent;
    /** */
    private List<Aggregate> aggregateList;

    /**
     * The minimum value found.
     */
    private BigDecimal min;
    /**
     * The maximum value found.
     */
    private BigDecimal max;
    /**
     * The sum of all values found.
     */
    private BigDecimal sum;


    /**
     * Creates a new node.
     *
     * @param value  the text value
     * @param column the column
     */
    public Node(final Object value, final Column column) {
        map = new TreeMap<>();
        this.value = value;
        this.column = column;
        this.aggregateList = new ArrayList<>();
    }

    /**
     * Creates the root node.
     */
    public Node() {
        this(null, null);
        parent = null;
    }

    /**
     * Returns true if node is root.
     *
     * @return true when root
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * Returns the column for this node.
     *
     * @return the column
     */
    public Column getColumn() {
        return column;
    }

    /**
     * Returns true when no sub elements.
     *
     * @return true when no sub elements
     */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Calculate a the total sum.
     *
     * @param v      the value
     * @param column the column
     */
    public void doSUM(final Object v, final Column column) {
        if (column instanceof IntegerColumn && v instanceof Integer) {
            IntegerColumn ic = (IntegerColumn) column;
            Integer i = (Integer) v;
            if (sum == null) {
                sum = new BigDecimal(i);
            } else {
                sum = sum.add(new BigDecimal(i));
            }
        }
    }

    /**
     * Calculate a the minimum value.
     *
     * @param v      the value
     * @param column the column
     */
    public void doMin(final Object v, final Column column) {
        if (column instanceof IntegerColumn && v instanceof Integer) {
            IntegerColumn ic = (IntegerColumn) column;
            Integer i = (Integer) v;
            if (min == null) {
                min = new BigDecimal(i);
            } else {
                BigDecimal bd = new BigDecimal(i);
                if (min.compareTo(bd) > 0) {
                    min = bd;
                }
            }
        }
    }

    /**
     * Calculate a the maximum value.
     *
     * @param v      the value
     * @param column the column
     */
    public void doMax(final Object v, final Column column) {
        if (column instanceof IntegerColumn && v instanceof Integer) {
            IntegerColumn ic = (IntegerColumn) column;
            Integer i = (Integer) v;
            if (max == null) {
                max = new BigDecimal(i);
            } else {
                BigDecimal bd = new BigDecimal(i);
                if (max.compareTo(bd) < 0) {
                    max = bd;
                }
            }
        }
    }

    /**
     * Calculate a the addValue of all values.
     *
     * @param v      the value
     * @param column the column
     */
    public void doCount(final Object v, final Column column) {
    }

    /**
     * Adds a new node if it doesn't exits.
     *
     * @param value  the value
     * @param column the column
     * @return the node added
     */
    public Node add(final Object value, final Column column) {
        if (!map.containsKey(value)) {
            Node n = new Node(value, column);
            n.count++;
            n.parent = this;
            map.put(value, n);
            return n;
        } else {
            Node n = map.get(value);
            n.count++;
            return n;
        }
    }

    /**
     * Returns the value.
     *
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns the map of sub items.
     *
     * @return the map
     */
    public Map<Object, Node> getMap() {
        return map;
    }

    /**
     * Returns the addValue.
     *
     * @return the addValue
     */
    public int getCount() {
        return count;
    }

    /**
     * Adds aggregated value.
     *
     * @param aggregate doSUM
     */
    public void addAggregate(final Aggregate aggregate) {
        this.aggregateList.add(aggregate);
    }

    public List<Aggregate> getAggregates() {
        return aggregateList;
    }

    public BigDecimal getMin() {
        return min;
    }

    public BigDecimal getMax() {
        return max;
    }

    public BigDecimal getSum() {
        return sum;
    }

    public BigDecimal getAverage() {
        if (sum == null || sum.intValue() == 0) {
            return new BigDecimal(0);
        }
        return sum.divide(new BigDecimal(count), BigDecimal.ROUND_DOWN);
    }
}
