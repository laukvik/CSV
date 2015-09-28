package org.laukvik.csv.sql.parser;

public class StringReader extends Reader {

    public final static char QUOTE = '"';

    public StringReader() {
        super();
    }

    public String consume(String sql) throws SyntaxException {
        System.out.println("charAt 0= " + sql.charAt(0));
        boolean useQuotation = (sql.charAt(0) == QUOTE);
        if (!useQuotation && isRequired()) {
            throw new SyntaxException("String doesn't start with a qoute");
        }

        StringBuffer buffer = new StringBuffer();
        boolean isStillSearching = true;
        boolean foundQuote = false;
        int x = 1;
        while (isStillSearching && x < sql.length()) {
            char c = sql.charAt(x);
//			log( c );
            if (c == QUOTE) {
                if (x < sql.length() - 1 && sql.charAt(x + 1) == QUOTE) {
                    isStillSearching = true;
                    x++;
                } else {
                    isStillSearching = false;
                    foundQuote = true;
                }
            } else {
                isStillSearching = true;
            }

            if (isStillSearching) {
                buffer.append(c);
                x++;
            }
        }
        if (!foundQuote) {
            throw new SyntaxException("Could not find an ending quote");
        }
        String rest = sql.substring(x + 1);
        String result = buffer.toString();
        fireFoundResults(result);
//		System.err.println( result );
//		log( "String:" + result );
//		log( "Remainder:" + rest );
        return rest;

    }

    public String getPurpose() {
        return "Consumes a string with qoutes on both sides";
    }

}
