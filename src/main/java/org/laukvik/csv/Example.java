/*
 * Copyright 2013 Laukviks Bedrifter.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.laukvik.csv;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author morten
 */
public class Example {

    public static String getCarsCSV() {
        return "Year,Make,Model,Description,Price\n"
                + "1997,Ford,E350,\"ac, abs, moon\",3000.00\n"
                + "1999,Chevy,\"Venture \"\"Extended Edition\"\"\",\"\",4900.00\n"
                + "1999,Chevy,\"Venture \"\"Extended Edition, Very Large\"\"\",,5000.00\n"
                + "1996,Jeep,Grand Cherokee,\"MUST SELL!\n"
                + "air, moon roof, loaded\",4799.00";
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {
        CSVReader csv = new CSVReader();
        csv.addCSVListener(new CSVListener() {
            public void foundRow(int rowIndex, String[] values) {
                System.out.print("#" + rowIndex + "\t");
                for (String s : values) {
                    System.out.print(s + "__");
                }
                System.out.println();
            }

            public void foundHeaders(String[] values) {
                System.out.print("Headers:");
                for (String s : values) {
                    System.out.print(s + ",");
                }
                System.out.println();
            }
        });


        csv.read(Example.class.getResourceAsStream("cars.csv"));

    }
}
