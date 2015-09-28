package org.laukvik.csv.sql.parser;

public class EmptyReader extends Reader {

    public EmptyReader() {
        super();
    }

    public String consume(String sql) throws SyntaxException {
        int found = 0;
        for (int x = 0; x < sql.length(); x++) {
            if (sql.charAt(x) == ' ' || sql.charAt(x) == '\t' || sql.charAt(x) == '\n' || sql.charAt(x) == '\r') {
                found++;
            } else {
                if (x == 0) {
                    if (isRequired()) {
                        throw new SyntaxException("Could not find any space in '" + sql + "'");
                    }
                    x = sql.length();
                } else {
                    x = sql.length();
                }
            }
        }
//		System.out.println( "EMPTY:" + sql.substring( found ) );
        return sql.substring(found);
    }

    public String getPurpose() {
        return "Consumes all empty spaces, tabs, linefeeds etc...";
    }

}
