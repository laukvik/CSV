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

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.laukvik.csv.swing.RecentFile;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class EntityTest {

    @Test
    public void read() {
        List<RecentFile> recentFiles = CSV.findByClass(RecentFile.class);
    }

    @Test
    public void write() {
        List<RecentFile> recentFiles = new ArrayList<>();
        recentFiles.add(new RecentFile("Hello.csv"));
        recentFiles.add(new RecentFile("World.csv"));
        try {
            CSV.saveAll(recentFiles, RecentFile.class);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
    }

}
