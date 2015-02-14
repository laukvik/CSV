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
import org.laukvik.csv.jdbc.query.SelectQuery;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class SelectReaderTest {

    public SelectReaderTest() {
    }

    @Test
    public void testSomeMethod() {
        String sql = "SELECT calllist.calllistid, calllist.name AS listName, callproduct.name, 	newsletter.subject, newsletterhistory.scheduled,newsletterhistory.started,newsletterhistory.finished,	newsletterverification.firstname,	newsletterverification.lastname, 	newsletterverification.cancelled,	newsletterverification.newsletterverificationid, 	calllist.customerid, 	newsletter.newsletterid, 	callproduct.callproductid,	newsletterhistory.newsletterhistoryid,	newsletterverification.accepted,	newsletterverification.denied "
                + "FROM CallProduct "
                + "INNER JOIN newsletter ON newsletter.newsletterid=callproduct.newsletterid "
                + "INNER JOIN newsletterhistory ON newsletterhistory.newsletterid = newsletter.newsletterid "
                + "INNER JOIN newsletterverification ON newsletterverification.newsletterhistoryid = newsletterhistory.newsletterhistoryid "
                + "WHERE NewsletterVerification.cancelled IS NULL AND NewsletterHistory.finished IS NULL "
                + "ORDER BY calllist.calllistid ASC, listName ASC, callproduct.name ASC "
                + "LIMIT 10 "
                + "OFFSET 5";

//		sql = "SELECT Employee.*,Customer.name FROM Employee INNER JOIN Customer ON Employee.customerID=Customer.customerID";
        try {
            SelectReader select = new SelectReader();
            SelectQuery q = select.parse(sql);

            System.out.println(q);

//			new ResultSetViewer( new ColumnDataModel( q.createData() ), q.toSQL() );
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
