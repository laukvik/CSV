package org.laukvik.csv.report;

import org.laukvik.csv.columns.Column;
import java.util.Map;
import java.util.TreeMap;

/**
 * Counts the amount of instances for a given value.
 *
 */
public final class Node {

    /**
     * The value.
     */
    private Object value;
    /** The sorted map of sub elements. */
    private Map<Object, Node> map;
    /** The column type. */
    private Column column;
    /** How many times it contains this value. */
    private int count;
    /** The parent node. */
    private Node parent;

    /**
     * Creates a new node.
     *
     * @param value the text value
     * @param column the column
     */
    public Node(final Object value, final Column column) {
        map = new TreeMap<>();
        this.value = value;
        this.column = column;

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
     * @return true when root
     */
    public boolean isRoot(){
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
     * @return true when no sub elements
     *
     */
    public boolean isEmpty(){
        return map.isEmpty();
    }

    /**
     * Adds a new node if it doesn't exits.
     * @param value the value
     * @param column the column
     * @return the node added
     */
    public Node add(final Object value, final Column column) {
        count++;
        if (!map.containsKey(value)) {
            Node n = new Node(value, column);
            n.parent = this;
            map.put(value, n);
            return n;
        } else {
            return map.get(value);
        }
    }

    /**
     * Returns the value.
     * @return the value
     */
    public Object getValue() {
        return value;
    }

    /**
     * Returns the map of sub items.
     * @return the map
     */
    public Map<Object, Node> getMap() {
        return map;
    }

    /**
     * Returns the count.
     * @return the count
     */
    public int getCount() {
        return count;
    }
}
