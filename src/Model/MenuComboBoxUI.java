package Model;

import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import java.awt.*;

public class MenuComboBoxUI extends BasicComboBoxUI {
    private ImageIcon Down;
    public MenuComboBoxUI() {
        String sep = System.getProperty("file.separator");
        Down= new ImageIcon(System.getProperty("user.dir") + sep + "Image" + sep
                + "MenuDown.png");
        Down = new ImageIcon(Down.getImage().getScaledInstance(18,18,1));
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JComboBox comboBox = (JComboBox) c;
        comboBox.setFocusable(true);
        comboBox.setOpaque(true);

        comboBox.setRenderer(new MenuListCellRenderer());
    }

    @Override
    public void paint(Graphics g, JComponent c) {
        JComboBox comboBox = (JComboBox) c;

        hasFocus = comboBox.hasFocus();

        Rectangle r = rectangleForCurrentValue();

        Graphics2D g2d = (Graphics2D) g;
        if ( !comboBox.isEditable() ) {
            paintCurrentValueBackground(g2d,r,hasFocus);
            paintCurrentValue(g,r,hasFocus);
        } else {
            paintCurrentValueBackground(g2d,r,hasFocus);
        }

        if(comboBox.hasFocus()) {
            g2d.setColor(new Color(181, 235, 253));
        } else {
            g2d.setColor(Color.GREEN);
        }

        g2d.drawRoundRect(0,0,comboBox.getWidth() - 2,comboBox.getHeight() -2, 2,2);
    }

    @Override
    protected JButton createArrowButton(){
        JButton arrow = new JButton(Down);
        arrow.setUI(new BtnUI());
        arrow.setFocusable(false);
        arrow.setMargin(new Insets(0,0,0,0));


        return arrow;
    }

    @Override
    public ComboPopup createPopup() {
        BasicComboPopup popup = (BasicComboPopup) super.createPopup();
        popup.setBorder(BorderFactory.createLineBorder(new Color(218, 243, 250)));
        return popup;
    }

}
