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
package org.laukvik.csv.swing;

import org.laukvik.csv.CSV;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author Morten Laukvik <morten@laukvik.no>
 */
public class RecentFileModel {

    List<RecentFile> files;
    JMenu recentMenu;
    RecentFileListener listener;
    private static final Logger LOG = Logger.getLogger(RecentFileModel.class.getName());


    public RecentFileModel(JMenu recentMenu, RecentFileListener listener) {
        this.files = CSV.findByClass(RecentFile.class);
        this.recentMenu = recentMenu;
        this.listener = listener;
        for (RecentFile rf : files) {
            add(rf, false);
        }

    }

    private void save() {
        try {
            LOG.info("Saving recent");
            CSV.saveAll(files, RecentFile.class);
            LOG.info("Saved recent!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(RecentFile file) {
        add(file, true);
    }

    private void add(RecentFile file, boolean save) {
        if (!files.contains(file)) {
            files.add(file);
            JMenuItem item = new JMenuItem();
            item.setAction(new OpenRecentFileAction(file, listener));
            item.setText(file.getPath());
            recentMenu.add(item);
            LOG.info("Adding recent!");
            if (save) {
                save();
            }
        }
    }

    public void clear() {
        this.files = new ArrayList<>();
        recentMenu.removeAll();
    }

}
