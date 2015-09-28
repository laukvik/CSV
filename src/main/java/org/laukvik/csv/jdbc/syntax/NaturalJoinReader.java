package org.laukvik.csv.jdbc.syntax;

import org.laukvik.csv.jdbc.ParseException;
import org.laukvik.csv.jdbc.Table;
import org.laukvik.csv.jdbc.joins.NaturalJoin;

public class NaturalJoinReader extends GroupReader {

    String table;

    public NaturalJoinReader() {
        super();
        add(new TextReader("NATURAL JOIN"));
        addEmpty();
        add(new WordReader()).addReaderListener(new ReaderListener() {
            public void found(String values) {
                table = values;
            }
        }
        );
    }

    public NaturalJoin getJoin() throws ParseException {
        return new NaturalJoin(null, Table.parse(table));
    }

}
