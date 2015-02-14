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
public class InsertTest {

    public InsertTest() {
    }

    @Test
    public void testSomeMethod() {
        try {
            String sql = "INSERT INTO Employee (first,last,email) VALUES (\"Morten \"\"cool\"\"\",Laukvik,laukvik@morgans.no)";
            Insert i = new Insert();
            i.consume(sql);
        }
        catch (SyntaxException e) {
            e.printStackTrace();
        }
        // "Hello "world" "
    }

}
