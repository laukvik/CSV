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
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class Manager<T> {

    final Class<T> typeParameterClass = null;

    public Manager() {
    }

    public void saveAll(List<T> objects) throws IllegalArgumentException, IllegalAccessException {
        File file = CSV.getFile(typeParameterClass.getClass());
        try (CsvWriter writer = new CsvWriter(new FileOutputStream(file), CSV.CHARSET_DEFAULT)) {
            writer.writeMetaData(objects.getClass());
            for (Object o : objects) {
                writer.writeEntityRow(o);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
