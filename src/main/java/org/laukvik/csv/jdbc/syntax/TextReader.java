package org.laukvik.csv.jdbc.syntax;

public class TextReader extends Reader {

    String text;

    public TextReader(String text) {
        super();
        this.text = text;
    }

    public String consume(String sql) throws SyntaxException {
        if (sql.length() >= text.length()) {
            String first = sql.substring(0, text.length());
            if (first.equalsIgnoreCase(text)) {
                fireFoundResults(text);
                return sql.substring(text.length());
            } else {
                throw new SyntaxException("Could not find text '" + text + "' in '" + sql + "'");
            }
        } else {
            throw new SyntaxException("Could not find text '" + text + "' in '" + sql + "'");
        }
    }

    public String getPurpose() {
        return "Consumes a specific text";
    }

}
