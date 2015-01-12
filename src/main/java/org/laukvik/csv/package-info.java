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

/**
 * An easy to use API for reading and writing to CSV
 *
 * @author Morten Laukvik
 *
 * Re  * <code>
 * CSV csv = new CSV( new File("contacts.csv") );
 *
 * </code>
 *
 * <code>
 * CSV csv = new CSV();
 * csv.addColumn("First","Last");
 * csv.addRow("Bill","Gates");
 * csv.addRow("Steve","Jobs");
 * csv.write( new File("contacts.csv") );
 * </code>
 *
 */
