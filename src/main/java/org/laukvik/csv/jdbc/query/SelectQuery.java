package org.laukvik.csv.jdbc.query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.laukvik.csv.jdbc.Column;
import org.laukvik.csv.jdbc.Condition;
import org.laukvik.csv.jdbc.Join;
import org.laukvik.csv.jdbc.Manager;
import org.laukvik.csv.jdbc.Sort;
import org.laukvik.csv.jdbc.Table;
import org.laukvik.csv.jdbc.data.ColumnData;
import org.laukvik.csv.jdbc.data.Data;

/**
 * A class to programatically create SQL queries
 *
 *
 * @author Morten
 *
 */
public class SelectQuery {

    public Table table;
    public List<Column> columns;
    public List<Join> joins;
    public List<Column> groupBys;
    public List<Condition> conditions;
    public List<Sort> sorts;
    public int limit;
    public int offset;
    String sql;

    public SelectQuery() {
        columns = new ArrayList<>();
        joins = new ArrayList<>();
        groupBys = new ArrayList<>();
        conditions = new ArrayList<>();
        sorts = new ArrayList<>();
    }

    public void setOffset(int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException("Offset cant be a negative number");
        }
        this.offset = offset;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public void addJoin(Join join) {
        joins.add(join);
    }

    public List<Join> getJoins() {
        return joins;
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public void setCondition(Condition condition) {
        conditions.clear();
        conditions.add(condition);
    }

    public void addSort(Sort sort) {
        sorts.add(sort);
    }

    public void addGroupBy(Column column) {
        groupBys.add(column);
    }

    public void setLimit(int limit) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must be 1 or greater!");
        }
        this.limit = limit;
    }

    public String toString() {
        return toSQL();
    }

    /**
     * Returns the query as it would by SQL standards.
     *
     */
    public String toSQL() {
        StringBuffer buffer = new StringBuffer();

        buffer.append("SELECT ");

        for (int x = 0; x < columns.size(); x++) {
            buffer.append((x > 0 ? "," : "") + "\n  " + columns.get(x));
        }

        buffer.append("\nFROM ");

        buffer.append("\n  " + table);

        for (Join j : joins) {
            buffer.append("\n" + j);
        }

        if (conditions.size() > 0) {
            buffer.append("\nWHERE ");
            for (Condition c : conditions) {
                buffer.append("\n  " + c);
            }
        }

        buffer.append("\nORDER BY ");
        for (Sort s : sorts) {
            buffer.append("\n  " + s);
        }

        if (offset > 0) {
            buffer.append("\nOFFSET " + offset);
        }

        if (limit > 0) {
            buffer.append("\nLIMIT " + limit);
        }

        return buffer.toString();
    }

    public ColumnData createData() throws SQLException {

        Manager mgr = new Manager();
        ColumnData main = mgr.getColumnData(table);

        for (Join j : joins) {
            main = j.join(main, mgr.getColumnData(j.right.getTable()));
        }

        Data d = new Data(main.listColumns());

        for (int y = offset; y < main.getRowCount(); y++) {

            boolean allConditionsAccepted = true;
            for (Condition con : conditions) {
                if (!con.accepts(main, main.getRow(y))) {
                    allConditionsAccepted = false;
                }
            }
            if (allConditionsAccepted) {
                if (limit == 0 || d.getRowCount() < limit) {
                    d.add(main.getRow(y));
                }
            }
        }

        /* Sorter her */
        return d;
    }

}
