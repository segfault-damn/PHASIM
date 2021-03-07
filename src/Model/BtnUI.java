package Model;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

public class BtnUI extends BasicButtonUI implements SwingConstants {
    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JButton button = (JButton) c;
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }
}
