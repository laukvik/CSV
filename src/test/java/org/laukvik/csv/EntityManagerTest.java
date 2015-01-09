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

import java.io.File;
import org.junit.Test;
import org.laukvik.csv.db.EntityManager;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class EntityManagerTest {

    public EntityManagerTest() {
    }

    @Test
    public void testSomeMethod() {
        EntityManager em = new EntityManager(getResource("person.csv"));
//        List<Person> items = em.findAll(Person.class);

    }

    public static File getResource(String filename) {
        ClassLoader classLoader = EntityManagerTest.class.getClassLoader();
        return new File(classLoader.getResource(filename).getFile());
    }

}
