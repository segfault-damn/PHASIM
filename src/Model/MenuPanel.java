package Model;

import javax.swing.*;
import java.awt.*;

public class MenuPanel extends JPanel {


    public MenuPanel() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        GradientPaint gp = new GradientPaint(0,0,new Color(171, 234, 253),0,HEIGHT,Color.WHITE);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(gp);
        g2d.fillRect(0,0,WIDTH, HEIGHT);



    }
}
