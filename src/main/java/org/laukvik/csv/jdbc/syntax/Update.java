package org.laukvik.csv.jdbc.syntax;

import java.util.List;
import org.laukvik.csv.jdbc.Condition;
import org.laukvik.csv.jdbc.Table;

/**
 * UPDATE Employee SET email='laukvik@morgans.no' WHERE employeeID=1
 *
 * @author Morten
 *
 */
public class Update extends GroupReader {

    public final static String UPDATE = "UPDATE";
    public final static String SET = "SET";
    public final static String WHERE = "WHERE";

    public Table table;
    public List<Condition> conditions;
    public List<Condition> predicates;

    public Update() {
        super();
        add(UPDATE);
        addEmpty();
        add(new WordReader());
        addEmpty();
        add(SET);
        addEmpty();
        add(new ListReader(" " + WHERE));
        addEmpty();
        add(WHERE);
        addEmpty();
        add(new ListReader(new ConditionReader(), new ConditionSeperator()));
        addOptional(";");
    }

    public String getTable() {
        WordReader wr = (WordReader) getReader(2);
        return (String) wr.getResult(0);
    }

    public ListReader getSetReader() {
        return (ListReader) getReader(6);
    }

    public ListReader getWhereReader() {
        return (ListReader) getReader(10);
    }

    public String getPurpose() {
        return "Consumes an SQL insert statment";
    }

    public String toSQL() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(UPDATE + " " + getTable() + "\n");
        buffer.append(SET);
        int x = 0;
        for (Object o : getSetReader().getResults()) {
            if (x > 0) {
                buffer.append(",");
            }
            buffer.append(" " + o);
            x++;
        }
        buffer.append("\n");
        buffer.append(WHERE);
        for (Object o : getWhereReader().getResults()) {
            if (x > 0) {
                buffer.append(",");
            }
            buffer.append(" " + o);
            x++;
        }
        buffer.append(";\n");
        return buffer.toString();
    }

    public void parse(String sql) throws SyntaxException {
        consume(sql);
    }

}
