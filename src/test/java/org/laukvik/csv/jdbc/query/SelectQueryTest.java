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
package org.laukvik.csv.jdbc.query;

import org.junit.Test;
import org.laukvik.csv.jdbc.Column;
import org.laukvik.csv.jdbc.Sort;
import org.laukvik.csv.jdbc.Table;
import org.laukvik.csv.jdbc.joins.LeftJoin;
import org.laukvik.csv.jdbc.joins.OuterJoin;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class SelectQueryTest {

    public SelectQueryTest() {
    }

    @Test
    public void testSomeMethod() {
        Table employee = new Table("Employee");
        employee.addColumn("email");
        Column departmentID = employee.addColumn("departmentID");

        Table department = new Table("Department");
        Column departmentDepartmentID = department.addColumn("departmentID");
        Column departmentCompanyID = department.addColumn("companyID");

        Table company = new Table("Company");
        Column companyID = company.addColumn(new Column("companyID"));

        /* Create a new query */
        SelectQuery q = new SelectQuery();
//		q.addColumn( employee.ALL );
        q.addColumn(department.ALL);
        q.addColumn(Column.ALL);
        q.setTable(employee);
        q.addJoin(new LeftJoin(departmentID, departmentDepartmentID));
        q.addJoin(new OuterJoin(departmentCompanyID, companyID));

        /* Example using departmendID=1 */
//		q.setCondition( new Equals( departmentDepartmentID, new SmallInt(33) ) );
        /* Example using lastName='Laukvik' */
//		q.setCondition( new Equals( lastName, new VarChar("Jones") ) );
        /* Example using IN ('Laukvik','�stensen','Pedersen') */
//		q.setCondition( new In( lastName, new VarChar("Steinberg"), new VarChar("Rafferty"), new VarChar("Jones") ) );
        /* Example using departmentID=employeeID */
//		q.setCondition( new Equals( departmentID, departmentDepartmentID ) );
        /* Example using departmentID > 12 */
//		q.setCondition( new Greater( departmentID, new SmallInt(12) ) );
        /* Example using lastName='Laukvik' or lastName='Pedersen' */
//		q.setCondition( new Or( new Equals( lastName, new VarChar("Laukvik") ), new Equals( lastName, new VarChar("Pedersen") )  ) );
        q.addSort(new Sort(departmentID, Sort.ASCENDING));
//		q.setOffset( -5 );
        q.setLimit(2);

//		new ResultSetViewer( new ColumnDataModel( q.createData() ), q.toSQL() );
    }

}
