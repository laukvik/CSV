package org.laukvik.csv.jdbc;

public class Sort {

    Column column;
    boolean sortOrder;
    public final static boolean ASCENDING = true;
    public final static boolean DESCENDING = true;
    public final static String ASC = "ASC";
    public final static String DESC = "DESC";

    public Sort(Column column, boolean sortOrder) {
        this.column = column;
        this.sortOrder = sortOrder;
    }

    public Column getColumn() {
        return column;
    }

    public boolean isAscending() {
        return sortOrder;
    }

    public boolean isDescending() {
        return !sortOrder;
    }

    public String toString() {
        return column.toString() + " " + (sortOrder ? "ASC" : "DESC");
    }

    public static Sort parse(String sql) throws ParseException {
        sql = sql.trim();
        boolean sortOrder = ASCENDING;
        String name = null;
        if (sql.endsWith(" " + DESC)) {
            sortOrder = DESCENDING;
            name = sql.substring(0, sql.indexOf(" " + DESC));
        } else if (sql.endsWith(" " + ASC)) {
            sortOrder = ASCENDING;
            name = sql.substring(0, sql.indexOf(" " + ASC));
        }
        if (name == null) {
            throw new ParseException("No sort column specified");
        }
        return new Sort(new Column(name.trim()), sortOrder);
    }

}
