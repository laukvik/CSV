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
import org.laukvik.csv.sql.parser.ListItemReader;
import org.laukvik.csv.sql.parser.SyntaxException;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class ListItemReaderTest {

    public ListItemReaderTest() {
    }

    @Test
    public void shouldReadTwoItems() throws SyntaxException {
        ListItemReader r = new ListItemReader();
        r.consume("\"Morten \"\"the cool\"\" Laukvik\", \"morten\"");
    }

}
