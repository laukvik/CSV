package org.laukvik.csv.jdbc.syntax;

public class ComparisonOperatorReader extends Either implements ReaderListener {

    TextReader eq, lt, mt, ne;
    String symbol;

    public ComparisonOperatorReader() {
        super();
        eq = new TextReader("=");
        lt = new TextReader("<");
        mt = new TextReader(">");
        ne = new TextReader("<>");
        eq.addReaderListener(new ReaderListener() {
            public void found(String values) {
                fireFoundResults(values);
            }
        });
        lt.addReaderListener(new ReaderListener() {
            public void found(String values) {
                fireFoundResults(values);
            }
        });
        mt.addReaderListener(new ReaderListener() {
            public void found(String values) {
                fireFoundResults(values);
            }
        });
        ne.addReaderListener(new ReaderListener() {
            public void found(String values) {
                fireFoundResults(values);
            }
        });

        setReaders(new TextReader[]{eq, lt, mt, ne});
    }

    public void found(String values) {
//		System.err.println( "Found symbol: " + values );
        this.symbol = values;
    }

    public String getSymbol() {
        return symbol;
    }

}
