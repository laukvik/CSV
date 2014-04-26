LaukvikCSV
==========

Java library to parse CSV files with ease.

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
