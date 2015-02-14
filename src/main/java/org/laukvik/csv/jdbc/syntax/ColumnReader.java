package org.laukvik.csv.jdbc.syntax;

import java.util.ArrayList;
import java.util.List;
import org.laukvik.csv.jdbc.Column;
import org.laukvik.csv.jdbc.Table;

/**
 * Column Column AS "test" Column AS test
 *
 * Table.Column Table.Column AS "test" Table.Column AS test
 *
 * Table.* *
 *
 * function()
 *
 *
 * @author morten
 *
 */
public class ColumnReader extends Either {

    String table, column, as;
    int type = -1;
    final static int WILDCARD = 1;
    final static int WILDCARDTABLE = 2;
    final static int TABLE = 3;
    List<ColumnListener> columnListeners;

    public ColumnReader() {
        columnListeners = new ArrayList<>();

        /* WILDCARD ONLY */
        TextReader wildcardOnly = new TextReader("*");
        wildcardOnly.addReaderListener(new ReaderListener() {
            public void found(String values) {
                table = "*";
                column = "*";
                type = WILDCARD;
            }
        });

        /* NO WILDCARDS **/
        GroupReader noWildcard = new GroupReader();

        noWildcard.add(new WordReader()).addReaderListener(new ReaderListener() {
            public void found(String values) {
                column = values;
                table = "*";
                type = TABLE;
            }
        });

        GroupReader g1 = new GroupReader();
        g1.add(new TextReader("."));
        g1.addOptional(new WordReader()).addReaderListener(new ReaderListener() {
            public void found(String values) {
                table = column;
                column = values;
                type = TABLE;
            }
        });
        noWildcard.addOptional(g1);

        GroupReader g2 = new GroupReader();
        g2.add(new EmptyReader());
        g2.add(new TextReader("AS"));
        g2.add(new EmptyReader());
        g2.add(new WordReader()).addReaderListener(new ReaderListener() {
            public void found(String values) {
                as = values;
                type = TABLE;
            }
        });
        noWildcard.addOptional(g2);

        /* TABLE AND WILDCARD */
        GroupReader tableWildcard = new GroupReader();

        tableWildcard.add(new WordReader()).addReaderListener(new ReaderListener() {
            public void found(String values) {
                table = values;
                type = WILDCARDTABLE;
            }
        });

        GroupReader g1b = new GroupReader();
        g1b.add(new TextReader("."));
        g1b.addOptional(new TextReader("*")).addReaderListener(new ReaderListener() {
            public void found(String values) {
                column = values;
                type = WILDCARDTABLE;
            }
        });
        tableWildcard.addOptional(g1b);

        /* SPECIFY READERS */
        setReaders(wildcardOnly, noWildcard, tableWildcard);
    }

    public ColumnListener addColumnListener(ColumnListener listener) {
        columnListeners.add(listener);
        return listener;
    }

    public void removeColumnListener(ColumnListener listener) {
        columnListeners.remove(listener);
    }

    public void fireColumnFound(Column column) {
        for (ColumnListener l : columnListeners) {
            l.found(column);
        }
    }

    public Column getColumn() {
//		System.out.println("table: " + table + " column: " + column + " type: " + type );
        if (type == WILDCARD) {
            Column c = Column.ALL;
            c.setTable(Table.EVERYTHING);
            return c;
        } else if (type == WILDCARDTABLE) {
            Table t = new Table(table);
            Column c = Column.ALL;
            c.setTable(t);
            return c;
        } else if (type == TABLE) {
            Column c = new Column(column);
            c.setTable(new Table(table));
            c.setAlias(as);
            return c;
        } else {
            return null;
        }
    }

    public String consume(String sql) throws SyntaxException {
        table = null;
        column = null;
        as = null;
        try {
            sql = super.consume(sql);
            fireColumnFound(getColumn());
            return sql;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new SyntaxException(e.getMessage());
        }
    }

}
