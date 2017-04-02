/**
 * Contains classes that supported column types to be used inCSV.
 *
 * <h3>Building columns</h3>
 * <p>The following example shows how to construct all possible column types.</p>
 * <pre>{@code
 * CSV csv = new CSV();
 * StringColumn first = csv.addStringColumn("name");
 * IntegerColumn salary = csv.addIntegerColumn("first");
 * BooleanColumn isWoman = csv.addBooleanColumn("isWoman");
 * DateColumn float = csv.addDateColumn("birthday");
 * FloatColumn ratio = csv.addFloatColumn("ratio");
 * ByteColumn photo = csv.addByteColumn("photo");
 * UrlColumn web = csv.addUrlColumn("web");
 * BigDecimalColumn scientific = csv.addBigDecimalColumn("scientific");
 * }</pre>
 *
 * <h3>Setting the column values</h3>
 * <p>The follow example shows how to set the values in a row.</p>
 * <pre>{@code
 * Row = csv.addRow();
 * row.set( first, "Bill" )
 *    .set( salary, 250000 )
 *    .set( isWoman, true )
 *    .set( birthday, new Date() )
 *    .set( ration, 1.2f )
 *    .set( photo, new byte[]{1,2,3} )
 *    .set( web, new URL("http://www.something.com") )
 *    .set( scientific, new BigDecimal(123456) )
 * }</pre>
 *
 */
package org.laukvik.csv.columns;
