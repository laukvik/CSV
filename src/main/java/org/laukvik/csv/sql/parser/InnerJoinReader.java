package org.laukvik.csv.sql.parser;

import org.laukvik.csv.sql.Column;
import org.laukvik.csv.sql.ParseException;
import org.laukvik.csv.sql.joins.InnerJoin;

public class InnerJoinReader extends JoinReader {

    String table, left, right;

    public InnerJoinReader() {
        super();
        add(new TextReader("INNER JOIN"));
        addEmpty();
        add(new WordReader()).addReaderListener(new ReaderListener() {
            public void found(String values) {
                table = values;
            }
        });
        addEmpty();
        add(new TextReader("ON"));
        addEmpty();
        add(new ColumnReader()).addReaderListener(new ReaderListener() {
            public void found(String values) {
                left = values;
            }
        });
        add(new TextReader("="));
        add(new ColumnReader()).addReaderListener(new ReaderListener() {
            public void found(String values) {
                right = values;
            }
        });
    }

    public InnerJoin getInnerJoin() throws ParseException {
        return new InnerJoin(Column.parse(left), Column.parse(right));
    }

    public String consume(String sql) throws SyntaxException {
        try {
            String sql2 = super.consume(sql);
            fireJoinFound(getInnerJoin());
            return sql2;
        }
        catch (Exception e) {
            throw new SyntaxException(e.getMessage());
        }
    }

}
