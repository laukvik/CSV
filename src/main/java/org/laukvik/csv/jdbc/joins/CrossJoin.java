package org.laukvik.csv.jdbc.joins;

import org.laukvik.csv.jdbc.Join;
import org.laukvik.csv.jdbc.Table;
import org.laukvik.csv.jdbc.data.ColumnData;
import org.laukvik.csv.jdbc.data.Data;

/**
 * A cross join returns the cartesian product of the sets of 
 * records from the two joined tables. Thus, it equates
 * to an inner join where the join condition always evaluates to TRUE
 * or join-condition is absent in statement.
 * 
 * 
 * SELECT *
 * FROM Employee
 * CROSS JOIN Department
 * 
 * SELECT *
 * FROM Employee, Department
 * 
 * @author Morten
 *
 */
public class CrossJoin extends Join {
	
	Table leftTable, rightTable;

	public CrossJoin( Table left, Table right ) {
		super( null, null );
		this.leftTable = left;
		this.rightTable = right;
	}

	public CrossJoin(String left, String right) {
		this( new Table(left), new Table(right) );
	}

	public String toString() {
		return "CROSS JOIN " + rightTable;
	}
	
	public ColumnData join( ColumnData lt, ColumnData rt ) {
		Data data = new Data( lt.listColumns(), rt.listColumns() );
		for (int ly=0; ly<lt.getRowCount(); ly++){
			for (int ry=0; ry<rt.getRowCount(); ry++){
				data.add( lt.getRow( ly ), rt.getRow( ry ) );
			}
		}
		return data;
	}
	
}