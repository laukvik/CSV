package org.laukvik.csv.jdbc.syntax;

import org.laukvik.csv.jdbc.Column;
import org.laukvik.csv.jdbc.Join;
import org.laukvik.csv.jdbc.Table;
import org.laukvik.csv.jdbc.joins.CrossJoin;
import org.laukvik.csv.jdbc.query.SelectQuery;

public class SelectReader extends GroupReader {

    ColumnReader cr;
    MultipleJoinReader jr;
    TableReader tr;
    ConditionReader wr;
    ColumnReader or;
    NumberReader lr;
    NumberReader ofr;

    SelectQuery query;

    public SelectReader() {
        super();
        init();
    }

    public void init() {
        /* Columns */
        add(new TextReader("SELECT"));
        addEmpty();
        cr = new ColumnReader();
        add(new ListReader(cr, new CommaReader()));

        /* Tables */
        addEmpty();
        add(new TextReader("FROM"));
        addEmpty();
        tr = new TableReader();
        add(new ListReader(tr, new CommaReader()));

        /* Joins */
        GroupReader joins = new GroupReader();
        joins.addEmpty();
        jr = new MultipleJoinReader();
        joins.add(new ListReader(jr, new EmptyReader()));
        addOptional(joins);

        GroupReader where = new GroupReader();
        where.addEmpty();
        where.add(new TextReader("WHERE"));
        where.addEmpty();
        wr = new ConditionReader();
        where.add(new ListReader(wr, new ConditionSeperator()));
        addOptional(where);

        GroupReader order = new GroupReader();
        order.addEmpty();
        order.add(new TextReader("ORDER"));
        order.addEmpty();
        or = new ColumnReader();
        order.add(new ListReader(or, new EmptyReader()));
        addOptional(order);

        GroupReader limit = new GroupReader();
        limit.addEmpty();
        limit.add("LIMIT");
        limit.addEmpty();
        lr = new NumberReader();
        limit.add(lr);
        addOptional(limit);

        GroupReader offset = new GroupReader();
        offset.addEmpty();
        offset.add("OFFSET");
        offset.addEmpty();
        ofr = new NumberReader();
        offset.add(lr);
        addOptional(offset);

        /* ATTACH LISTENERS */
        cr.addColumnListener(new ColumnListener() {
            public void found(Column column) {
                query.addColumn(column);
            }
        });

        tr.addTableListener(new TableListener() {
            public void found(Table table) {
                if (query.table == null) {
                    query.table = table;
                } else {
                    Table left = null;
                    if (query.getJoins().size() == 0) {
                        left = query.table;
                    } else {
                        left = query.getJoins().get(query.getJoins().size() - 1).left.getTable();
                    }
                    query.addJoin(new CrossJoin(left, table));
                }
            }
        });

        jr.addJoinListener(new JoinReaderListener() {
            public void found(Join join) {
                query.addJoin(join);
            }
        });

        wr.addReaderListener(new ReaderListener() {
            public void found(String values) {
//				wheres.add(  values );
            }
        });
        or.addReaderListener(new ReaderListener() {
            public void found(String values) {
//				orders.add(  values );
            }
        });

        lr.addNumberListener(new NumberListener() {
            public void found(Number number) {
                System.out.println("Found: " + number);
                query.setLimit(number.intValue());
            }
        }
        );

        ofr.addNumberListener(new NumberListener() {
            public void found(Number number) {
                System.out.println("Found: " + number);
                query.setOffset(number.intValue());
            }
        }
        );
    }

    public SelectQuery parse(String sql) throws SyntaxException {
        query = new SelectQuery();
        consume(sql);
        return query;
    }

}
