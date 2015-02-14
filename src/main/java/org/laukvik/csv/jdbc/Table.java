package org.laukvik.csv.jdbc;

import java.util.ArrayList;
import java.util.List;
import org.laukvik.csv.CSV;

public class Table {

    public static final Table EVERYTHING = new Table("*");

    private String name;
    private List<Column> columns;
    public Column ALL;
    private CSV csv;

    public Table(String name) {
        this.name = name.trim();
        columns = new ArrayList<>();
        ALL = new Column("*", this);
    }

    public static Table parse(String sql) throws ParseException {
        return new Table(sql);
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }

    public Column addColumn(String column) {
        return addColumn(new Column(column));
    }

    public Column addColumn(Column column) {
        column.setTable(this);
        columns.add(column);
        return column;
    }

    /**
     * Lists the columns foudn in database
     *
     * @return
     */
    public Column[] listRealColumns() {
        List<Column> items = new ArrayList<>();
        for (int x = 0; x < csv.getMetaData().getColumnCount(); x++) {
            items.add(new Column(csv.getMetaData().getColumn(x).getName(), this));
        }
        Column[] arr = new Column[items.size()];
        items.toArray(arr);
        return arr;
    }

    /**
     * Lists the columns found in query
     *
     * @return
     */
    public Column[] listColumns() {
        List<Column> items = new ArrayList<Column>();
        for (Column c : columns) {
            if (!c.isAll()) {
                items.add(c);
            }
        }
        Column[] arr = new Column[items.size()];
        items.toArray(arr);
        return arr;
    }

}
