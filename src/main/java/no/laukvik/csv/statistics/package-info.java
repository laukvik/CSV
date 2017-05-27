/**
 * The statistics package contains classes for calculating frequency distribution and divide numbers
 * into labelled range of numbers.
 * <h3>Frequency distribution</h3>
 * <p>Example: </p>
 * <pre>{@code
 * StringColumn c = new StringColumn("president");
 * FrequencyDistribution<String> distribution = new FrequencyDistribution(c);
 * distribution.addValue("Barack Obama");
 * distribution.addValue("Donald Trump");
 * distribution.addValue(null);
 * distribution.addValue("Barack Obama");
 * for (String key : distribution.getKeys()){
 *    System.out.println(key + ": " + distribution.getCount(key));
 * }
 * }</pre>
 * <p>The output of the example above:</p>
 * <pre>{@code
 * Barack Obama: 2
 * Donald Trump: 1
 * }</pre>
 * <h3>Ranged distribution</h3>
 * <p>Example: Using four ranges of integers and accumulate values</p>
 * <pre>{@code
 * IntegerDistribution distribution = new IntegerDistribution();
 * // Define four ranges
 * distribution.addRange(new IntegerRange("Q1", 0, 25));
 * distribution.addRange(new IntegerRange("Q2", 25, 50));
 * distribution.addRange(new IntegerRange("Q3", 50, 75));
 * distribution.addRange(new IntegerRange("Q4", 75, 100));
 * // Add test values
 * distribution.addValue(0);
 * distribution.addValue(24);
 * distribution.addValue(25);
 * distribution.addValue(44);
 * distribution.addValue(55);
 * distribution.addValue(110);
 * distribution.addValue(null);
 * distribution.addValue(null);
 *
 * for (IntegerRange r : distribution.getRanges()){
 * System.out.println(r.getLabel() + ": " + r.getCount());
 * }
 * }</pre>
 * <p>The output of the example above:</p>
 * <pre>{@code
 * Q1: 2
 * Q2: 2
 * Q3: 1
 * Q4: 0
 * }</pre>
 */
package no.laukvik.csv.statistics;
