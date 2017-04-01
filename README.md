LaukvikCSV
==========

**Continuous Integration:** [![Build Status](https://travis-ci.org/laukvik/LaukvikCSV.svg?branch=master)](https://travis-ci.org/laukvik/LaukvikCSV) <br/>
**License:** [Apache 2.0](http://www.apache.org/licenses/LICENSE-2.0)<br/>


An easy to use API for reading, writing, querying and exporting data.


## Reading files

The easiest way to read a CSV file is to call the default constructor. This method will try to auto detect separator character and encoding.

    CSV csv = new CSV(new File("presidents.csv"));
    
To read files using a SEMI COLON as separator character

    CSV csv = new CSV();
    csv.readFile( new File("presidents.csv"), CSV.SEMICOLON );

To read files using a semi colon as PIPE character

    CSV csv = new CSV();
    csv.readFile( new File("presidents.csv"), CSV.PIPE );

To read files using a semi colon as TAB character

    CSV csv = new CSV();
    csv.readFile( new File("presidents.csv"), CSV.TAB );

## Writing files

    csv.writeFile( file, CSV.PIPE );

## Querying files

    csv.find().column( first

## Working with rows and columns







## Export files

To write the contents to a CSV file

    csv.writeFile( new File("presidents.csv") );

To write the contents to a XML file

    csv.writeXML( new File("presidents.xml") );

To write the contents to a JSON file

    csv.writeXML( new File("presidents.json") );
    
To write the contents to a HTML file

    csv.writeXML( new File("presidents.html") );
    
To write the contents to ResourceBundle(s)

    csv.writeXML( new File("presidents.properties") );



## Working with columns

Creating a CSV with two columns

    CSV csv = new CSV();
    csv.addColumn("President");
    csv.addColumn("Party");
    
Reordering columns. The following example moves the column "Party" from index 1 to index 0 

    csv.getMetaData().moveColumn(1,0);
    
Removes the first column (President).

    csv.getMetaData().removeColumn(0);

    
    
    

## Working with rows

Adding a new row with data

    CSV csv = new CSV();
    StringColumn president = csv.addColumn("President");
    StringColumn party = csv.addColumn("Party");
    
    csv.addRow().update(president, "Barack Obama").update(party, "Democratic");

Moving a row up or down

    csv.moveRow( 1, 2 );
    
Swapping two rows

    csv.swapRows( 1, 2 );
    
Removing rows

    csv.removeRow( 5 );
    
Removing rows between range

    csv.removeRows( 5, 10 );
    
Finding the index

    csv.indexOf( row );

Inserting row at a specific index

    CSV csv = new CSV();
    StringColumn president = csv.addStringColumn("President");
    csv.addRow(0).setString(president, "Barak Obama");



## Iterating rows

Iterating all rows

    CSV csv = new CSV();
    for (int y=0; y<csv.getRowCount(); y++){
        Row row = csv.getRow(y);
    }

Iterate using stream
    
    // Insert example



## Using queries 

The API supports fluent queries similar to SQL. The first example finds all rows where President is Barack Obama

    List<Row> rows = csv.findByQuery().where().column("President").is("Barack Obama").getResultList();
    
The second example uses sorting for the results
    
    List<Row> rows = csv.findByQuery().orderBy().asc("President").getResultList();



## FrequencyDistribution

Builds a FrequencyDistribution for column with index 2;

    FrequencyDistribution fd = csv.buildFrequencyDistribution( 2 );


## Distinct Values

    Set<String> values = csv.buildDistinctValues(1);



## TODOs

Coverage
JavaDocs
Eksempler
Rename repo
importere flere data til eksisterende
fixed width import
Lese filer uten headere
Importere ved å spesifisere kolonnetypene først
Query
Annotering

Nexus søknad
Deploy første versjon