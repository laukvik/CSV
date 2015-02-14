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
public class UpdateTest {

    public UpdateTest() {
    }

    @Test
    public void testSomeMethod() {
        try {
            String sql = "UPDATE Employee SET email='laukvik@morgans.no',last='Laukvik' WHERE employeeID=1 AND last=laukvik";
            Update u = new Update();
            u.consume(sql);

//			System.out.println( "Table: " + u.getTable() );
//
//			for (Object o :u.getSetReader().getResults()){
//				System.out.println( o );
//			}
//
//			for (Object o :u.getWhereReader().getResults()){
//				System.out.println( o );
//			}
            System.out.println(u.toSQL());

        }
        catch (SyntaxException e) {
            e.printStackTrace();
        }
    }

}
