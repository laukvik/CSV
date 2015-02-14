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
package org.laukvik.csv.jdbc.syntax;

import org.junit.Test;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class GroupReaderTest {

    public GroupReaderTest() {
    }

    @Test
    public void testSomeMethod() {
        try {
            GroupReader group = new GroupReader();
            group.add(new TextReader("SELECT"));
            group.addEmpty();
            group.add(new ListReader(new ColumnReader()));
            group.addEmpty();
            group.add(new TextReader("FROM"));
            group.addEmpty();
            group.add(new TableReader());

            GroupReader joins = new GroupReader();
            joins.addEmpty();
            joins.add(new MultipleJoinReader());
            group.addOptional(joins);

            GroupReader where = new GroupReader();
            where.addEmpty();
            where.add(new TextReader("WHERE"));
            where.addEmpty();
            where.add(new ListReader(new ConditionReader(), new ArrayReader("AND")));
            group.addOptional(where);

            System.out.println(group.consume("SELECT	 email, first,last     FROM Employee INNER JOIN Employee ON Employee.customerID=Customer.customerID WHERE customerID>5 OR email=morten AND employeeID=12 AND first=Janne OR lastName=Laukvik"));

        }
        catch (SyntaxException e) {
            e.printStackTrace();
        }
    }

}
