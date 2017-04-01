/**
 *
 * <p>Reporting and statistics building based on aggregated data.</p>
 *
 * <h3>Example</h3>
 * <p>The follow example shows how to build a report.</p>
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
package org.laukvik.csv.report;
