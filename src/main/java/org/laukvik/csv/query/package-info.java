/**
 * <p>Contains classes for working with queries.</p>
 * <p>
 * <pre>
 * .select()
 * .where()
 * .column("BookID").is(5);
 * .select()
 * .where()
 * .findResults();
 *
 * .where
 * .column("BookID").is(5)
 * .column("Chapter").isGreaterThan(3)
 * .column("Verse").isBetween(10,20)
 * .orderBy
 * .asc("BookID")
 * .desc("Verse")
 * .findResults(1,10);
 * </pre>
 */
package org.laukvik.csv.query;
