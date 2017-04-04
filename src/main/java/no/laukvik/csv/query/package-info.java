/**
 * <p>Contains classes for working with queries.</p>
 * <p>
 * <h3>Using a query</h3>
 * <p>The following example illustrates how to perform a simple query.</p>
 * <pre>{@code
 *     CSV csv = new CSV();
 *     IntegerColumn presidency = csv.addIntegerColumn("Presidency");
 *     csv.addRow().set(1);
 *     csv.addRow().set(5);
 *     csv.addRow().set(11);
 *     Query query = new Query();
 *     query.isBetween(presidency, 1, 10); // Find all rows with value 1 to 10
 *     List<Row> rows = csv.findRowsByQuery( query ); // Returns two rows
 * }</pre>
 */
package no.laukvik.csv.query;
