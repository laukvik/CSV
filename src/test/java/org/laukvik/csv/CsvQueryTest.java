/*
 * Copyright 2015 Laukviks Bedrifter.
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

import org.junit.Test;
import org.laukvik.csv.columns.IntegerColumn;
import org.laukvik.csv.columns.StringColumn;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class CsvQueryTest {

    @Test
    public void readFile() throws IOException, ParseException {
        CSV csv = new CSV();
        csv.readFile(getResource("metadata.csv"));
//        Query q = csv.findByQuery();
        StringColumn party = (StringColumn) csv.getMetaData().getColumn("Party");
        IntegerColumn presidency = (IntegerColumn) csv.getMetaData().getColumn("Presidency");
        StringColumn homeState = (StringColumn) csv.getMetaData().getColumn("Home state");

        List<Row> rows = csv.findByQuery().
                select(party, homeState).
                where().
                column(presidency).
                isGreaterThan(10).
                getResultList();
    }

    public static File getResource(String filename) {
        ClassLoader classLoader = CsvQueryTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

}
