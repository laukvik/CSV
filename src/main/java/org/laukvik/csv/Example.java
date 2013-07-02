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

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author morten
 */
public class Example {

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
