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



