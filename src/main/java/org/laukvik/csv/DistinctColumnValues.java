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

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.laukvik.csv.columns.Column;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class DistinctColumnValues {

    private final Column column;
    private final Map<String, Integer> map;

    public DistinctColumnValues(Column column) {
        this.column = column;
        this.map = new TreeMap<>();
    }

    public Column getColumn() {
        return column;
    }

    public Set<String> getKeys() {
        return map.keySet();
    }

    public Integer getCount(String key) {
        return map.get(key);
    }

    public void addValue(String word) {
        if (word == null || word.length() == 0) {
            return;
        }
        if (map.containsKey(word)) {
            Integer count = map.get(word) + 1;
            map.put(word, count);
        } else {
            map.put(word, 1);
        }
    }

}
