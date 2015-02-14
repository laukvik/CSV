package org.laukvik.csv.jdbc.syntax;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.laukvik.csv.jdbc.Table;

public class TableReader extends Reader {

    private List<TableListener> tableListeners;

    public TableReader() {
        tableListeners = new ArrayList<TableListener>();
    }

    public String consume(String sql) throws SyntaxException {
        Pattern p = Pattern.compile("([a-zA-Z0-9]*)");
        Matcher m = p.matcher(sql);

        if (m.find()) {
            String word = m.group();
            sql = sql.substring(m.end());
            log("Found table " + word);
            fireTableFound(new Table(word));
        } else {
            if (isRequired()) {
                throw new SyntaxException("Could not find a table");
            }
        }

        return sql;
    }

    public String getPurpose() {
        return "Consumes a table name";
    }

    public TableListener addTableListener(TableListener listener) {
        tableListeners.add(listener);
        return listener;
    }

    public void removeTableListener(TableListener listener) {
        tableListeners.remove(listener);
    }

    public void fireTableFound(Table table) {
        for (TableListener l : tableListeners) {
            l.found(table);
        }
    }

}
