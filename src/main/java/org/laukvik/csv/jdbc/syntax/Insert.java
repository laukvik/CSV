package org.laukvik.csv.jdbc.syntax;

/**
 * INSERT INTO Employee (first,last) VALUES ('Morten','Laukvik');
 *
 * @author Morten
 *
 */
public class Insert extends GroupReader {

    public Insert() {
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
