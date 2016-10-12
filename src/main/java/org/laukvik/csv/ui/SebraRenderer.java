package org.laukvik.csv.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.Color;
import java.awt.Component;

/**
 *
 * @author Morten Laukvik
 */
public class SebraRenderer implements TableCellRenderer {

    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    final static Color ODD = new Color(255, 255, 255);
    final static Color EVEN = new Color(245, 245, 245);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent( table, value, isSelected, hasFocus, row, column);
        ((JLabel) renderer).setOpaque(true);
//        Color foreground, background;
        // a
        if (isSelected) {
        } else {
            if (row % 2 == 0) {
                renderer.setBackground(ODD);

            } else {
                renderer.setBackground(EVEN);
            }
        }
        return renderer;
    }
}
