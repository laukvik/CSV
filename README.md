
[![Code Climate](https://codeclimate.com/github/laukvik/LaukvikCSV/badges/gpa.svg)](https://codeclimate.com/github/laukvik/LaukvikCSV)



An easy to use API for reading and writing to CSV files.

Create an empty CSV
--------------------------------------------------------------------------------

    CSV csv = new CSV();
    
Add two columns
--------------------------------------------------------------------------------

    csv.addColumn("First");
    csv.addColumn("Last");
    
Add a new row
--------------------------------------------------------------------------------

    csv.addRow("Bill","Gates");

Write to file
--------------------------------------------------------------------------------

    csv.write( new File("contacts.csv") );
    
Reading an existing file
--------------------------------------------------------------------------------

    CSV csv = new CSV( new File("contacts.csv") );

Getting a specific row/column
--------------------------------------------------------------------------------

    System.out.println( csv.getRow(2).getString(3) );

Iterating all rows
--------------------------------------------------------------------------------

    for (int y=0; y<csv.getRowCount(); y++){
        Row row = csv.getRow(y);
    }

Using queries
--------------------------------------------------------------------------------
The API supports fluent queries similar to SQL. The first example finds all rows where President is Barack Obama

    List<Row> rows = csv.findByQuery().where().column("President").is("Barack Obama").getResultList();
    
The second example uses sorting for the results
    
    List<Row> rows = csv.findByQuery().orderBy().asc("President").getResultList();



Using annotations
--------------------------------------------------------------------------------
You can add CSV annotations to easily persist instances of the entities to
a CSV file. The CSV file will act similar to a single table in a SQL database.


    @Entity
    public class RecentFile {
        @Column
        private String path;
    }

    /* Read all entries */
    List<RecentFile> recentFiles = CSV.findByClass(RecentFile.class);




