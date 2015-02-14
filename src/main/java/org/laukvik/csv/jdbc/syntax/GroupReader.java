package org.laukvik.csv.jdbc.syntax;

import java.util.ArrayList;
import java.util.List;

public class GroupReader extends Reader {

    private List<Reader> readers;

    public GroupReader() {
        this.readers = new ArrayList<>();
    }

    public Reader add(Reader reader) {
        this.readers.add(reader);
        return reader;
    }

    public Reader getReader(int index) {
        return readers.get(index);
    }

    public Reader addOptional(Reader reader) {
        reader.setRequired(false);
        this.readers.add(reader);
        return reader;
    }

    public void addOptional(String text) {
        TextReader r = new TextReader(text);
        r.setRequired(false);
        this.readers.add(r);
    }

    public Reader addEmpty() {
        return add(new EmptyReader());
    }

    public void addEither(Reader... readers) {
        add(new Either(readers));
    }

    public void add(String text) {
        add(new TextReader(text));
    }

    /**
     * Usss
     *
     * @param left
     * @param right
     * @param reader
     */
    public void addBetween(String left, Reader reader, String right) {
        add(new Between(left, right, reader));
    }

    public String consume(String sql) throws SyntaxException {
        int max = readers.size();
        /* Iterate through all readers */
        for (int x = 0; x < max; x++) {
            Reader r = readers.get(x);
//			log( r.getClass().getSimpleName() + "\t" + sql );

            if (r.isRequired()) {
                /* Required readers */
                sql = r.consume(sql);

            } else {
                /* Optional readers */
                if (sql.length() == 0) {
                    x = readers.size();
                } else {
                    try {
                        sql = r.consume(sql);
                    }
                    catch (Exception e) {
//						e.printStackTrace();
                    }
                }
            }

//			System.out.println( "\t\tafter: " + sql );
        }
        return sql;
    }

    public String getPurpose() {
        return "Consume text for multiple readers";
    }

}
