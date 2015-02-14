package org.laukvik.csv.jdbc.syntax;

public class ListItemReader extends Reader {

    ListReader listReader;
    String stop;

    public ListItemReader() {
        super();
    }

    public void setListReader(ListReader listReader) {
        this.listReader = listReader;
    }

    public String consume(String sql) throws SyntaxException {
        boolean useQuotation = sql.startsWith("\"");
        if (useQuotation) {

            int foundIndex = -1;
            StringBuffer buffer = new StringBuffer();
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
                    buffer.append(c);
                }
            }

            if (foundIndex == -1) {
                throw new SyntaxException("List item doesnt stop with a quoute");
            }

            String rest = sql.substring(foundIndex + 2);

//			log( "Found using quoatation:" + rest );
            this.listReader.addResults(rest);

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

//			log( "Found using no quotation:" + word );
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
