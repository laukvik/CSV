package org.laukvik.csv.sql.parser;

import org.laukvik.csv.sql.Column;
import org.laukvik.csv.sql.ParseException;
import org.laukvik.csv.sql.joins.RightJoin;

public class RightOuterJoinReader extends GroupReader {

    String table, left, right;

    public RightOuterJoinReader() {
        super();
        addEither(new TextReader("RIGHT OUTER JOIN"), new TextReader("RIGHT JOIN"));
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

    public RightJoin getJoin() throws ParseException {
        return new RightJoin(Column.parse(left), Column.parse(right));
    }

}
