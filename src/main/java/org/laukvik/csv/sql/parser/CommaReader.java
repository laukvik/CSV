package org.laukvik.csv.sql.parser;

public class CommaReader extends GroupReader {

    public CommaReader() {
        super();
        EmptyReader empty = new EmptyReader();
        empty.setRequired(false);
        addOptional(empty);
        add(",");
        addOptional(empty);
    }

}
