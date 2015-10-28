package org.laukvik.csv.sql.parser;

import org.laukvik.csv.sql.Column;
import org.laukvik.csv.sql.Condition;

public class ConditionReader extends GroupReader {

    final static int NUMBER = 1;
    final static int COLUMN = 2;
    final static int STRING = 3;

    int type1, type2;
    Number n1, n2;
    Column c1, c2;
    String s1, s2;

    Condition condition;

    String symbol;

    public ConditionReader() {
        NumberReader nr1 = new NumberReader();
        nr1.addNumberListener(new NumberListener() {
            public void found(Number number) {
                n1 = number;
                type1 = NUMBER;
            }
        });
        ColumnReader cr1 = new ColumnReader();
        cr1.addColumnListener(new ColumnListener() {
            public void found(Column column) {
                c1 = column;
                type1 = COLUMN;
            }
        });
        StringReader sr1 = new StringReader();
        sr1.addReaderListener(new ReaderListener() {
            public void found(String values) {
                s1 = values;
                type1 = STRING;
            }
        });

        NumberReader nr2 = new NumberReader();
        nr2.addNumberListener(new NumberListener() {
            public void found(Number number) {
                n2 = number;
                type2 = NUMBER;
            }
        });
        ColumnReader cr2 = new ColumnReader();
        cr2.addColumnListener(new ColumnListener() {
            public void found(Column column) {
                c2 = column;
                type2 = COLUMN;
            }
        });
        StringReader sr2 = new StringReader();
        sr2.addReaderListener(new ReaderListener() {
            public void found(String values) {
                s2 = values;
                type2 = STRING;
            }
        });

        add(new Either(sr1, nr1, cr1));
        addEmpty().setRequired(false);
        add(new ComparisonOperatorReader()).addReaderListener(new ReaderListener() {
            public void found(String values) {
                symbol = values;
            }
        });

        addEmpty().setRequired(false);
        add(new Either(sr2, nr2, cr2));

    }

    public String consume(String sql) throws SyntaxException {
        sql = super.consume(sql);

        if (type1 == COLUMN) {

        }

        return sql;
    }

    public String getPurpose() {
        return "Consumes one condition";
    }

}
