package org.laukvik.csv.jdbc;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import org.laukvik.csv.CSV;
import org.laukvik.csv.swing.CSVFileFilter;
import org.laukvik.csv.ParseException;
import org.laukvik.csv.jdbc.data.ColumnData;
import org.laukvik.csv.jdbc.data.Data;
import org.laukvik.csv.jdbc.query.SelectQuery;
import org.laukvik.csv.jdbc.syntax.SelectReader;
import org.laukvik.csv.jdbc.syntax.SyntaxException;

public class Manager {

    File home;

    public Manager() {
        if (isMacOSX()) {
            setHome(new File("/Users/morten/Projects/Research/src/org/laukvik/jdbc"));
//			setHome( new File( System.getProperty("user.home"), "Desktop" ) );
        } else {
            setHome(new File("C:\\Users\\Morten\\Prosjekter\\Research\\src\\org\\laukvik\\jdbc\\"));
        }
    }

    public static boolean isMacOSX() {
        return (System.getProperty("os.name").toLowerCase()
                .startsWith("mac os x"));
    }

    public Manager(File home) {
        setHome(home);
    }

    public void setHome(File home) {
        this.home = home;
    }

    public File getHome() {
        return home;
    }

    public TextResultSet executeQuery(String sql) throws SQLException {
        SelectReader p = new SelectReader();
        SelectQuery q;
        try {
            q = p.parse(sql);
        }
        catch (SyntaxException e) {
            throw new SQLException("Could not parse SQL");
        }
        return new TextResultSet(executeQuery(q));
    }

    public TextResultSet createResultSet(SelectQuery q) throws SQLException {
        return new TextResultSet(executeQuery(q));
    }

    public int executeUpdate(String sql) {
        return 0;
    }

    public ColumnData executeQuery(SelectQuery q) throws SQLException {

        ColumnData main = getColumnData(q.table);

        for (Join j : q.joins) {
            main = j.join(main, getColumnData(j.right.getTable()));
        }

        Data d = new Data(main.listColumns());

        for (int y = q.offset; y < main.getRowCount(); y++) {

            boolean allConditionsAccepted = true;
            for (Condition con : q.conditions) {
                if (!con.accepts(main, main.getRow(y))) {
                    allConditionsAccepted = false;
                }
            }
            if (allConditionsAccepted) {
                if (q.limit == 0 || d.getRowCount() < q.limit) {
                    d.add(main.getRow(y));
                }
            }
        }

        /* Sorter her */
        return d;
//		return q.createData();
    }

    public ColumnData getColumnData(Table table) throws SQLException {
        if (table == null) {
            throw new SQLException("No schema specified!");
        }
        if (home == null) {
            throw new SQLException("Home folder not specified!");
        }
        try {
            File schema = new File(home, table.getName() + ".csv");
            File view = new File(home, table.getName() + ".sql");
            if (schema.exists()) {
                try {
                    return getColumnData(schema, table.getName());
                }
                catch (Exception e) {
                    throw new SQLException(e);
                }
            } else if (view.exists()) {

                try {
                    String sql = getString(view);
                    SelectReader p = new SelectReader();
                    SelectQuery q;
                    q = p.parse(sql);
                    return executeQuery(q);
                }
                catch (SyntaxException e) {
                    e.printStackTrace();
                    throw new SQLException("Could not parse SQL");
                }
                catch (Exception e) {
                    throw new SQLException("Could not parse SQL");
                }

//				SelectQuery q = new SelectQuery();
//				q.addColumn( Column.ALL );
//				q.setTable( new Table("Employee") );
            }

            if (view.exists()) {
                SelectQuery q = new SelectQuery();
                q.addColumn(Column.ALL);
                q.setTable(new Table("Employee"));
                return executeQuery(q);
            } else {
                throw new IOException("Dont exist!");
            }

        }
        catch (IOException e) {
            throw new SQLException("Schema " + table.getName() + " does not exist!");
        }
    }

    public String getString(File file) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        StringBuffer sb = new StringBuffer();
        byte[] buffer = new byte[1024];
        while (fis.available() > 0) {
            fis.read(buffer);
            sb.append(new String(buffer));

        }
        fis.close();
        return sb.toString();
    }

    public ColumnData getColumnData(File file, String tableName) throws SQLException {
        try {
            CSV csv = new CSV(file);
            ColumnDataAdapter adapt = new ColumnDataAdapter(csv);
            return adapt;
        }
        catch (IOException | ParseException ex) {
            throw new SQLException(ex);
        }
    }

    /**
     * Lists all available metadata
     *
     * @param home
     * @param catalog
     * @param schemaPattern
     * @param tableNamePattern
     * @param types
     * @return
     */
    public static ColumnData listTables(File home, String catalog, String schemaPattern, String tableNamePattern, String[] types) {
        Column[] columns = {new Column("Table1"), new Column("Table2"), new Column("Table3"), new Column("Table4")};
        Data data = new Data(columns);
        CSVFileFilter ff = new CSVFileFilter();
        for (String type : types) {
            if (type.equalsIgnoreCase("table")) {
                for (File f : home.listFiles((FileFilter) ff)) {
                    String name = f.getName();
                    String tableName = name.substring(0, name.length() - 4);
                    data.add(new String[]{tableName, tableName, tableName, tableName});
                }
            } else if (type.equalsIgnoreCase("view")) {
                for (File f : home.listFiles(new SqlQueryFileFilter())) {
                    String name = f.getName();
                    String tableName = name.substring(0, name.length() - 4);
                    data.add(new String[]{tableName, tableName, tableName, tableName});
                }
            }
        }

        return data;
    }

}
