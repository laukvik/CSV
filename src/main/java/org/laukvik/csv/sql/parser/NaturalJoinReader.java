package org.laukvik.csv.sql.parser;

import org.laukvik.csv.sql.ParseException;
import org.laukvik.csv.sql.Table;
import org.laukvik.csv.sql.joins.NaturalJoin;

public class NaturalJoinReader extends GroupReader {

    private String table;

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
