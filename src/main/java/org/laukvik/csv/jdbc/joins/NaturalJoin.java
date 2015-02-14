package org.laukvik.csv.jdbc.joins;

import java.util.ArrayList;
import java.util.List;
import org.laukvik.csv.jdbc.Column;
import org.laukvik.csv.jdbc.Join;
import org.laukvik.csv.jdbc.ParseException;
import org.laukvik.csv.jdbc.Table;
import org.laukvik.csv.jdbc.data.ColumnData;
import org.laukvik.csv.jdbc.data.Data;

/**
 * A natural join offers a further specialization of quie joins. The join
 * predicate arises implicity by comparing all columns in both tables that have
 * the same column-name in the joined tables. The resulting joined table
 * contains only one column for each pair of equally-named columns.
 *
 * Example:
 *
 * SELECT * FROM Employee NATURAL JOIN Department
 *
 * @author Morten
 *
 */
public class NaturalJoin extends Join {

    Table right, left;

    public NaturalJoin(Table left, Table table) {
        super(null, null);
        this.right = table;
        this.left = left;
    }

    public NaturalJoin(String left, String right) throws ParseException {
        super(null, null);
        this.left = Table.parse(left);
        this.right = Table.parse(right);
    }

    public String toString() {
        return "NATURAL JOIN " + right;
    }

    public ColumnData join(ColumnData lt, ColumnData rt) {
        Data data = new Data(lt.listColumns(), rt.listColumns());

        /* Create a vector of the share columns */
        List<Column> sharedLeft = new ArrayList<>();
        List<Column> sharedRight = new ArrayList<>();
        for (Column lc : lt.listColumns()) {
            for (Column rc : rt.listColumns()) {
                if (lc.getName().equalsIgnoreCase(rc.getName())) {
                    if (!sharedLeft.contains(lc)) {
                        sharedLeft.add(lc);
                        sharedRight.add(rc);
                    }
                }
            }
        }

        for (int ly = 0; ly < lt.getRowCount(); ly++) {
            for (int ry = 0; ry < rt.getRowCount(); ry++) {
                int hits = 0;
                for (int x = 0; x < sharedLeft.size(); x++) {
                    Column l = sharedLeft.get(x);
                    Column r = sharedRight.get(x);

                    String lval = lt.getValue(l, ly);
                    String rval = rt.getValue(r, ry);

                    if (lval != null && rval != null) {
                        if (lval.equalsIgnoreCase(rval)) {
                            hits++;
                        }
                    }
                }

                if (hits == sharedLeft.size()) {
                    data.add(lt.getRow(ly), rt.getRow(ry));
                }
            }
        }
        return data;
    }

}
