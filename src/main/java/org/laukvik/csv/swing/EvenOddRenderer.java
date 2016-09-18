package org.laukvik.csv.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 *
 * @author Morten Laukvik
 */
public class EvenOddRenderer implements TableCellRenderer {

    public static final DefaultTableCellRenderer DEFAULT_RENDERER = new DefaultTableCellRenderer();

    final static Color ODD = new Color(255, 255, 255);
    final static Color EVEN = new Color(245, 245, 245);

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        Component renderer = DEFAULT_RENDERER.getTableCellRendererComponent(
                table, value, isSelected, hasFocus, row, column);
        ((JLabel) renderer).setOpaque(true);
        Color foreground, background;
        if (isSelected) {
//            foreground = Color.yellow;
//            background = Color.green;
        } else {
            if (row % 2 == 0) {
//                foreground = ODD;
//                background = Color.white;
                renderer.setBackground(ODD);

            } else {
                renderer.setBackground(EVEN);
//            renderer.setBackground(background);
            }
        }
//        renderer.setForeground(foreground);
//        renderer.setBackground(background);
        return renderer;
    }
}
