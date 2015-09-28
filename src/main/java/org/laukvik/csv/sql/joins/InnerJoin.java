package org.laukvik.csv.sql.joins;

import org.laukvik.csv.sql.Column;
import org.laukvik.csv.sql.Join;
import org.laukvik.csv.jdbc.data.ColumnData;
import org.laukvik.csv.jdbc.data.Data;

/**
 * "An inner join requires each record in the two joined tables
 * to have a matching record. An inner join essentially combines
 * the records from two tables based on a given join-predicate. 
 * The result of the join can be defined as the outcome of first
 * taking the Cartesian product (or cross join) of
 * all records in the tables (combining every record in table A with
 * every record in table b) - then return all records which satisfy
 * the join predicate." - Wikipedia
 * <br>
 * Example
 * <br>
 * SELECT *
 * FROM Employee
 * INNER JOIN Department ON Employee.departmendID=Department.departmentID
 * 
 * @author Morten
 *
 */
public class InnerJoin extends Join {

	public InnerJoin( Column left, Column right ) {
		super( left, right );
	}

	public String toString() {
		return "INNER JOIN " + right.getTable() + " ON " + right + "=" + left;
	}

	public ColumnData join( ColumnData lt, ColumnData rt ) {
		Data data = new Data( lt.listColumns(), rt.listColumns() );

		for (int ly=0; ly<lt.getRowCount(); ly++){
			String lval = lt.getValue( left, ly );
			for (int ry=0; ry<rt.getRowCount(); ry++){
				String rval = rt.getValue( right, ry );
				if (lval == null || rval == null){
					/* Dont add when nulls */
				} else if (lval.equalsIgnoreCase( rval )){
					data.add( lt.getRow( ly ), rt.getRow( ry ) );
				}
			}
		}
		return data;
	}
 
}