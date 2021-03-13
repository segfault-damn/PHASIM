package Model;

import javax.swing.*;
import java.awt.*;

public class MenuListCellRenderer implements ListCellRenderer {

    private DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer();
    /**
     * Return a component that has been configured to display the specified
     * value. That component's <code>paint</code> method is then called to
     * "render" the cell.  If it is necessary to compute the dimensions
     * of a list because the list cells do not have a fixed size, this method
     * is called to generate a component on which <code>getPreferredSize</code>
     * can be invoked.
     *
     * @param list         The JList we're painting.
     * @param value        The value returned by list.getModel().getElementAt(index).
     * @param index        The cells index.
     * @param isSelected   True if the specified cell was selected.
     * @param cellHasFocus True if the specified cell has the focus.
     * @return A component whose paint() method will render the specified value.
     * @see JList
     * @see ListSelectionModel
     * @see ListModel
     */
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel renderer = (JLabel) defaultListCellRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if(isSelected) {
            renderer.setBackground(new Color(236, 211, 250));
            renderer.setForeground(new Color(17, 4, 121));
        } else {
            renderer.setBackground(new Color(231, 245, 250,250));
            renderer.setForeground(new Color(87, 84, 106,150));
        }

        renderer.setHorizontalAlignment(JLabel.CENTER);
        renderer.setBounds(MainFrame.WIDTH/2-200,400 + 50,400,50);
        list.setSelectionBackground(new Color(223, 243, 248));
        list.setSelectionForeground(new Color(17, 4, 121));
        list.setBorder(null);
        return renderer;
    }
}
