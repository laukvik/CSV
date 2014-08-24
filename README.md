LaukvikCSV
==========

A Java library to parse CSV files. It can read very large files by using
an event based reader (CSVReader). This requires minimum memory. For smaller
files you can read the whole CSV file into memory (CSV).


Reading files directly to memory
--------------------------------------------------------------------------------

    CSV csv = new CSV();
    csv.parse(new File("cars.csv"));
    System.out.println(csv.getCell(2,3)); // Grand Cherokeee



Reading very large files
--------------------------------------------------------------------------------

    /* Create reader */
    CSVReader csv = new CSVReader();

    /* Add listener when header and rows are found */
    csv.addListener(new CSVListener() {
        @Override
        public void foundRow(int rowIndex, String[] values) {
            System.out.print("#" + rowIndex + "\t");
            for (String s : values) {
                System.out.print(s + "__");
            }
            System.out.println();
        }

        @Override
        public void foundHeaders(String[] values) {
            System.out.print("Headers:");
            for (String s : values) {
                System.out.print(s + ",");
            }
            System.out.println();
        }
    });

    /* Start reading cars.csv file */
    csv.read(Example.class.getResourceAsStream("cars.csv"));

CSV
--------------------------------------------------------------------------------
Year,Make,Model,Description,Price
1997,Ford,E350,"ac, abs, moon",3000.00
1999,Chevy,"Venture ""Extended Edition""","",4900.00
1999,Chevy,"Venture ""Extended Edition, Very Large""",,5000.00
1996,Jeep,Grand Cherokee,"MUST SELL!
air, moon roof, loaded",4799.00
