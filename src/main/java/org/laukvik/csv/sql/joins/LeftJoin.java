package org.laukvik.csv.sql.joins;

import org.laukvik.csv.sql.Column;
import org.laukvik.csv.sql.Join;
import org.laukvik.csv.jdbc.data.ColumnData;
import org.laukvik.csv.jdbc.data.Data;

/**
 * The result of a left outer join (or simply left join)
 * for table A and B always contains all records of the 
 * left table A, even if the join-condition does not
 * find any matching record in the right table B.
 * 
 * Same as LEFT OUTER JOIN
 * 
 * SELECT *
 * FROM Employee 
 * LEFT OUTER JOIN Department ON Employee.departmentID=Department.departmentID
 * 
 */
public class LeftJoin extends Join {

	public LeftJoin( Column left, Column right ) {
		super( left, right );
	}

	public String toString() {
		return "LEFT OUTER JOIN " + right.getTable() + " ON " + right + "=" + left;
	}
	
	public ColumnData join( ColumnData lt, ColumnData rt ) {
		Data data = new Data( lt.listColumns(), rt.listColumns() );
		for (int ly=0; ly<lt.getRowCount(); ly++){
			String lval = lt.getValue( left, ly );
			boolean found = false;
			for (int ry=0; ry<rt.getRowCount(); ry++){
				String rval = rt.getValue( right, ry );
				if (lval == null || rval == null){
					/* Dont add when nulls */
				} else if (lval.equalsIgnoreCase( rval )){
					found = true;
					data.add( lt.getRow( ly ), rt.getRow( ry ) );
				}
			}
			if (!found){
				data.add( lt.getRow( ly ), new String[ rt.getColumnCount() ] );
			}
		}
		return data;
	}

	
}