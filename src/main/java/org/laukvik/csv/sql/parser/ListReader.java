package org.laukvik.csv.sql.parser;

public class ListReader extends Reader {

    Reader reader;
    Reader seperator;
    String stop;

    public ListReader() {
        super();
        ListItemReader r = new ListItemReader();
        r.setListReader(this);
        this.reader = r;
        this.seperator = new TextReader(",");
    }

    public ListReader(Reader reader) {
        this.reader = reader;
        this.seperator = new TextReader(",");
    }

    public ListReader(Reader reader, Reader seperator) {
        this.reader = reader;
        this.seperator = seperator;
    }

    /**
     * Stops consuming when it meets the stop symbol
     *
     * @param stop
     *
     * @author <a href="mailto:laukvik@morgans.no">Morten Laukvik</a>
     * @version CVS $Revision: 1.1 $ $Date: 2009/09/09 18:53:10 $
     */
    public ListReader(String stop) {
        this.stop = stop;
        ListItemReader r = new ListItemReader();
        r.setListReader(this);
        r.setStop(stop);
        this.reader = r;
        this.seperator = new TextReader(",");
    }

    public ListReader(String stop, ConditionReader reader) {
        this.stop = stop;
        ListItemReader r = new ListItemReader();
        r.setListReader(this);
        r.setStop(stop);
        this.reader = reader;
        this.seperator = new TextReader(",");
    }

    public void setReader(Reader reader) {
        this.reader = reader;
    }

    public void setListSeperator(Reader seperator) {
        this.seperator = seperator;
    }

    public Reader getListSeperator() {
        return seperator;
    }

    public String consume(String sql) throws SyntaxException {

        EmptyReader empty = new EmptyReader();
        empty.setRequired(false);
        /* Remove all unwanted space before parsing list */
        sql = empty.consume(sql);

        boolean exit = false;
        int loopCounter = 0;
        while (sql.length() > 0 && exit == false && loopCounter < 1000) {
            loopCounter++;

            /* Consume with the specified reader */
            sql = reader.consume(sql);

            if (sql.length() == 0) {
                /* No more text */
                exit = true;
            } else {
                /* Typically the separator is comma */
                try {
                    sql = seperator.consume(sql);
                }
                catch (Exception e) {
//					e.printStackTrace();
                    exit = true;
                }

            }

        }

        return sql;
    }

    public String getPurpose() {
        return "Consumes a comma separated list of items";
    }

}
