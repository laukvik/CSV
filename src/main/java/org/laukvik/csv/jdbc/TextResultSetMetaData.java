package org.laukvik.csv.jdbc;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.laukvik.csv.jdbc.data.ColumnData;

public class TextResultSetMetaData implements ResultSetMetaData {
	
	ColumnData data;
	
	public TextResultSetMetaData( ColumnData data ){
		this.data = data;
	}

	public String getCatalogName(int column) throws SQLException {
		return null;
	}

	public String getColumnClassName(int column) throws SQLException {
		return "varchar";
	}

	public int getColumnCount() throws SQLException {
//		System.out.println( "getColumnCount:" +  data.getColumnCount() );
		return data.getColumnCount();
	}

	public int getColumnDisplaySize(int column) throws SQLException {
		return 10;
	}

	public String getColumnLabel(int column) throws SQLException {
		return getColumnName( column );
	}

	public String getColumnName(int columnIndex) throws SQLException {
		if (columnIndex > data.getColumnCount()){
			return null;
		}
		/* Remove 1 because database columns starts with 1 and not 0 */
		return data.getColumn( columnIndex-1 ).getName();
	}

	public int getColumnType(int column) throws SQLException {
		return java.sql.Types.VARCHAR;
	}

	public String getColumnTypeName(int column) throws SQLException {
		return "varchar";
	}

	public int getPrecision(int column) throws SQLException {
		return 0;
	}

	public int getScale(int column) throws SQLException {
		return 0;
	}

	public String getSchemaName(int column) throws SQLException {
		return null;
	}

	public String getTableName(int column) throws SQLException {
		return null;
	}

	public boolean isAutoIncrement(int column) throws SQLException {
		return false;
	}

	public boolean isCaseSensitive(int column) throws SQLException {
		return false;
	}

	public boolean isCurrency(int column) throws SQLException {
		return false;
	}

	public boolean isDefinitelyWritable(int column) throws SQLException {
		return false;
	}

	public int isNullable(int column) throws SQLException {
		return 0;
	}

	public boolean isReadOnly(int column) throws SQLException {
		return false;
	}

	public boolean isSearchable(int column) throws SQLException {
		return false;
	}

	public boolean isSigned(int column) throws SQLException {
		return false;
	}

	public boolean isWritable(int column) throws SQLException {
		return false;
	}

	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return false;
	}

	public <T> T unwrap(Class<T> iface) throws SQLException {
		
		return null;
	}

}