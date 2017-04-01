LaukvikCSV
==========

**Continuous Integration:** [![Build Status](https://travis-ci.org/laukvik/LaukvikCSV.svg?branch=master)](https://travis-ci.org/laukvik/LaukvikCSV) <br/>
**License:** [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)<br/>


LaukvikCSV is a powerful API for reading, writing and querying tabular data stored in the CSV format. In contrast to other API it lets you specify data types for each column using meta data. It automatically detects delimiters so you don't have to worry about delimiters being comma, tab, pipe, semicolon etc. Run powerful queries to filter your data easily with a fluid query language thats type safe. Export your tabular data to CSV, JSON, XML and HTML.


## Reading a CSV file

The easiest way to read a CSV file is to call the default constructor. This method will try to auto detect separator character and encoding.

```java
CSV csv = new CSV( new File("presidents.csv") );
StringColumn president = csv.getColumn("president");
IntegerColumn presidency = csv.getColumn(5);
List<Row> rows = csv.findRows();
for (Row r : rows){
    System.out.println( r.getObject(president) );
    System.out.println( r.getObject(presidency) );
}
```

## Writing files
```java
CSV csv = new CSV();
StringColumn first = csv.getColumn("first");
StringColumn last = csv.getColumn("last");
csv.writeFile( new File("addresses.csv") ); // Write to CSV format
```

## Exporting files
```java
csv.writeHtml( new File("addresses.html") ); // Write to HTML format
csv.writeJSON( new File("addresses.json") ); // Write to JSON format
csv.writeXML( new File("addresses.xnk") ); // Write to XML format
```

## Querying a CSV file
```java
CSV csv = new CSV( new File("presidents.csv") );
StringColumn president = csv.getColumn("president");
IntegerColumn presidency = csv.getColumn(5);
Query query = new Query();
query.isBetween(presidency, 1, 10);
List<Row> rows = csv.findRowsByQuery( query );
for (Row r : rows){
    System.out.println( r.getObject(president) );
    System.out.println( r.getObject(presidency) );
}
```

## Building a CSV file
```java
CSV csv = new CSV();
StringColumn first = csv.getColumn("first");
StringColumn last = csv.getColumn("last");
csv.addRow().set( first, "John" ).set( last, "Doe" );  
```

## Working with columns

Creating a CSV with two columns
```java
CSV csv = new CSV();
csv.addStringColumn("President");
csv.addStringColumn("Party");
```

Reordering columns. The following example moves the column "Party" from index 1 to index 0 
```java
csv.moveColumn(1,0);
```

Removes the first column (President).
```java
csv.removeColumn(0);
```

## Working with rows

Adding a new row with data
```java
CSV csv = new CSV();
StringColumn president = csv.addColumn("President");
StringColumn party = csv.addColumn("Party");

csv.addRow()
    .set(president, "Barack Obama")
    .set(party, "Democratic");
```

Moving a row up or down
```java
csv.moveRow( 1, 2 );
```

Swapping two rows
```java
csv.swapRows( 1, 2 );
```

Removing rows
```java
csv.removeRow( 5 );
```

Removing rows between range
```java
csv.removeRows( 5, 10 );
```

Finding the index
```java
csv.indexOf( row );
```

Inserting row at a specific index
```java
CSV csv = new CSV();
StringColumn president = csv.addStringColumn("President");
csv.addRow(0).setString(president, "Barak Obama");
```

## Iterating rows

Iterating all rows
```java
CSV csv = new CSV( new File("presidents.csv") );
for (Row row : csv.findRows()){
    
}
```

Iterate rows using stream
```java
CSV csv = new CSV( new File("presidents.csv") );
csv.stream();
```

## Using queries 

Finds all rows where the presidency is between 1 and 10
```java
Query query = new Query();
query.isBetween( presidency, 1, 10 ); // Find all rows with value 1 to 10
List<Row> rows = csv.findRowsByQuery( query ); // Returns two rows
```

Finds all rows above and sorts it descending order
```java
Query query = new Query()
    .isBetween( presidency, 1, 10 )
    .descending( presidency );
List<Row> rows = csv.findByQuery(query);
```

## FrequencyDistribution

Builds a FrequencyDistribution for an integer column
```java
FrequencyDistribution<Integer> fd = csv.buildFrequencyDistribution( presidency );
```

## Distinct Values
```java
Set<String> values = csv.buildDistinctValues( name );
```


