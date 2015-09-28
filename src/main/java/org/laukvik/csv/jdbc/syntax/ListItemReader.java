package org.laukvik.csv.jdbc.syntax;

import java.util.logging.Level;

public class ListItemReader extends Reader {

    private ListReader listReader;
    private String stop;

    public ListItemReader() {
        super();
        LOG.log(Level.FINE, "Creating reading");
    }

    public void setListReader(ListReader listReader) {
        this.listReader = listReader;
    }

    public String consume(String sql) throws SyntaxException {
        LOG.log(Level.FINE, "Consuming: {0}", sql);
        boolean useQuotation = sql.startsWith("\"");
        if (useQuotation) {
            int foundIndex = -1;
            StringBuilder sb = new StringBuilder();
            for (int x = 1; x < sql.length() - 1; x++) {
                char c = sql.charAt(x);
                char n = sql.charAt(x + 1);
                boolean isQoute = (c == '"');
                boolean isDouble = (c == '"' && n == c);
                boolean isDelimiter = (!isDouble && isQoute);
//				System.out.println( "Char: " + x + "=\t" + c + "\t" + isDouble + "\t" + isDelimiter );
                /* Skip next char if double quote */
                if (isDouble) {
                    x++;
                }
                /* Exit for loop if a delimiter was found */
                if (isDelimiter) {
                    foundIndex = x - 1;
                    x = sql.length();
                } else {
                    sb.append(c);
                }
            }
            if (foundIndex == -1) {
                throw new SyntaxException("List item doesnt stop with a quoute");
            }
            String rest = sql.substring(foundIndex + 2);
            LOG.log(Level.FINE, "Found using quoatation:{0}", rest);
            if (this.listReader != null) {
                this.listReader.addResults(rest);
            }

            return rest;

        } else {

            int index = sql.indexOf(",");
            if (index == -1) {
                /* No more commas found */
                if (stop == null) {
                    index = sql.length();
                } else {
                    index = sql.indexOf(stop);
                }
            } else {
                /* Found a comma */
            }

            String word;
            String rest;
            if (index == -1) {
                word = sql;
                rest = "";
            } else {
                word = sql.substring(0, index);
                rest = sql.substring(index);
            }

            LOG.info("Found using no quotation:" + word);
            this.listReader.addResults(word);
            return rest;
        }
    }

    public String getPurpose() {
        return "Consumes the contents between the separators";
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

}
