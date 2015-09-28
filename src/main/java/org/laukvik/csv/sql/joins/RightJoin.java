package org.laukvik.csv.sql.joins;

import org.laukvik.csv.sql.Column;
import org.laukvik.csv.sql.Join;
import org.laukvik.csv.jdbc.data.ColumnData;
import org.laukvik.csv.jdbc.data.Data;

/**
 * Every row from the right table will appear in the joined table at 
 * least once. If no matching row from the left table exist, null will 
 * appear in columns from right table.
 * 
 * SELECT *
 * FROM Employee 
 * RIGHT OUTER JOIN Department ON Employee.departmentID=Department.departmentID
 * 
 * 
 * @author Morten
 *
 */
public class RightJoin extends Join {

	public RightJoin( Column left, Column right ) {
		super( left, right );
	}

	public String toString() {
		return "RIGHT OUTER JOIN " + right.getName() + " ON " + right + "=" + left;
	}
	
	public ColumnData join( ColumnData lt, ColumnData rt ) {
		Data data = new Data( lt.listColumns(), rt.listColumns() );

		for (int ry=0; ry<rt.getRowCount(); ry++){
			String rval = rt.getValue( right, ry );
			boolean found = false;
			for (int ly=0; ly<lt.getRowCount(); ly++){
				String lval = lt.getValue( left, ly );
				if (lval == null || rval == null){
					/* Dont add when nulls */
				} else if (rval.equalsIgnoreCase( lval )){
					found = true;
					data.add( lt.getRow( ly ), rt.getRow( ry ) );
				}
			}
			if (!found){
				data.add( new String[ lt.getColumnCount() ], rt.getRow( ry ) );
			}
		}
		return data;
	}
	
}