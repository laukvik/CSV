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
package org.laukvik.csv.query;

import java.util.Comparator;
import java.util.List;
import org.laukvik.csv.MetaData;
import org.laukvik.csv.Row;
import org.laukvik.csv.columns.Column;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class RowSorter implements Comparator<Row> {

    List<SortOrder> sortOrders;
    MetaData metaData;

    public RowSorter(List<SortOrder> sortOrders, MetaData metaData) {
        this.sortOrders = sortOrders;
        this.metaData = metaData;
    }

    @Override
    public int compare(Row r1, Row r2) {
        for (SortOrder sortOrder : sortOrders) {
            int columnIndex = sortOrder.getColumnIndex();

            Object o1 = r1.getValue(columnIndex);
            Object o2 = r2.getValue(columnIndex);

            Column c = metaData.getColumn(columnIndex);

            int result = 0;
            if (sortOrder.getType() == SortOrder.Type.ASC) {
                result = c.compare(o1, o2);
            } else {
                result = c.compare(o2, o1);
            }

            if (result == 0) {
                /* Values are the same so continue sorting */
            } else {
                return result;
            }
        }
        return 0;
    }

}
