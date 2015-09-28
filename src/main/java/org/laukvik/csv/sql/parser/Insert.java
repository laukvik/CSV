package org.laukvik.csv.sql.parser;

/**
 * INSERT INTO Employee (first,last) VALUES ('Morten','Laukvik');
 *
 * @author Morten
 *
 */
public class Insert extends GroupReader {

    public Insert() {
        super();
        add("INSERT INTO");
        addEmpty();
        add(new WordReader());
        addEmpty();
        add("(");
        add(new ListReader(new WordReader()));
        add(")");
        addEmpty();
        add("VALUES");
        addEmpty();
        add("(");
        add(new ListReader(")"));
        add(")");
        addOptional(";");
    }

    public String getPurpose() {
        return "Consumes an SQL insert statment";
    }

}
