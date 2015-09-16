/*
 * Copyright 2013 Laukviks Bedrifter.
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
package org.laukvik.csv.swing;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import org.laukvik.csv.CSV;

public class CSVFileFilter extends javax.swing.filechooser.FileFilter implements FileFilter, FilenameFilter {

    public CSVFileFilter() {
        super();
    }

    @Override
    public boolean accept(File f) {
        String filename = f.getName().toLowerCase();
        if (f.isHidden()) {
            return false;
        }
        return (filename.endsWith("." + CSV.FILE_EXTENSION));
    }

    public String getDescription() {
        return "Comma Separated Files (*.csv)";
    }

    @Override
    public boolean accept(File dir, String name) {
        return (name.endsWith("." + CSV.FILE_EXTENSION));
    }
}
