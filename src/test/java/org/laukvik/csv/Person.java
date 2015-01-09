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

import java.net.URL;
import org.laukvik.csv.annotation.Column;
import org.laukvik.csv.annotation.Entity;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
@Entity
public class Person {

    @Column(name = "first")
    private String firstName;

    @Column(name = "last")
    private String lastName;

    @Column(name = "age", type = Integer.class)
    private int age;

    @Column(name = "homepage", type = URL.class)
    private URL homepage;

}
