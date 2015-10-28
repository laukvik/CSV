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
import org.laukvik.csv.sql.parser.NumberReader;
import org.laukvik.csv.sql.parser.SyntaxException;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class NumberReaderTest {

    public NumberReaderTest() {
    }

    @Test
    public void testSomeMethod() throws SyntaxException {

        NumberReader r = new NumberReader();
        //System.out.println(r.consume("250.79 Morten"));

    }

}
