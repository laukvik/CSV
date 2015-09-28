package org.laukvik.csv.sql.joins;

import org.laukvik.csv.sql.Column;
import org.laukvik.csv.sql.Join;
import org.laukvik.csv.jdbc.data.ColumnData;
import org.laukvik.csv.jdbc.data.Data;

/**
 * A full outer join combines the results of both left and right outer 
 * joins. The joined table will contain all records from both tables, 
 * and fill in NULLs for missing matches on either side.
 * 
 * SELECT *
 * FROM Employee
 * FULL OUTER JOIN Department ON Employee.department=Department.departmentID
 * 
 * @author Morten
 *
 */
public class OuterJoin extends Join {

	public OuterJoin( Column left, Column right ) {
		super( left, right );
	}

	public String toString() {
		return "FULL OUTER JOIN " + right.getTable() + " ON " + right +  "=" + left;
	}
	
	public ColumnData join( ColumnData lt, ColumnData rt ) {
		Data data = new Data( lt.listColumns(), rt.listColumns() );
		
		for (int ly=0; ly<lt.getRowCount(); ly++){
			String lval = lt.getValue( left, ly );
			boolean found = false;			
			for (int ry=0; ry<rt.getRowCount(); ry++){
				String rval = rt.getValue( right, ry );
				if (lval !=null && rval != null && lval.equalsIgnoreCase( rval )){
					found = true;
					data.add( lt.getRow( ly ), rt.getRow( ry ) );
				}
			}
			if (!found){
				data.add( lt.getRow( ly ), new String[ rt.getColumnCount() ] );
			}
		}
		
		for (int ry=0; ry<rt.getRowCount(); ry++){
			String rval = rt.getValue( right, ry );
			boolean found = false;			
			for (int ly=0; ly<lt.getRowCount(); ly++){
				String lval = lt.getValue( left, ly );
				if (lval !=null && rval != null && rval.equalsIgnoreCase( lval )){
					found = true;
				}
			}
			if (!found){
				data.add( new String[ lt.getColumnCount() ], rt.getRow( ry ) );
			}
		}
		
		return data;
	}
	
}