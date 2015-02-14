package org.laukvik.csv.jdbc;

public class Column {

    public static final Column ALL = new Column("*");
    String name;
    Table table;
    String alias;

    public Column() {
        this.name = "*";
    }

    public Column(String name) {
        this.name = name.trim();
    }

    public Column(String name, Table table) {
        this.name = name.trim();
        this.table = table;
    }

    public Column(String name, String table) {
        this.name = name.trim();
        this.table = new Table(table);
        this.table.addColumn(this);
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public boolean isAll() {
        return name.equalsIgnoreCase("*");
    }

    public String getName() {
        return name;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Table getTable() {
        return table;
    }

    public String toString() {
        if (table == null) {
            return "" + name + (alias != null ? " AS " + alias : "");
        } else {
            return table.getName() + "." + name + (alias != null ? " AS " + alias : "");
        }
    }

    public static Column parse(String sql) throws ParseException {
        if (sql.contains(".")) {
            String[] arr = sql.split(".");
            if (arr.length == 2) {
                return new Column(arr[0], arr[1]);
            } else {
                throw new ParseException("Could not parse column");
            }
        } else {
            return new Column(sql);
        }
    }

}
