package org.laukvik.csv.jdbc.syntax;

public class WhereReader extends GroupReader {

    public WhereReader() {
        add(new TextReader("WHERE"));
        addEmpty();
        ConditionReader wr = new ConditionReader();

        add(new ListReader(wr, new ConditionSeperator()));
    }

}
