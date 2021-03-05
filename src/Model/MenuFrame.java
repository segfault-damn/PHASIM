package Model;

import javax.swing.*;
import java.awt.*;

public class MenuFrame extends JFrame {

    public static int HEIGHT = 750;
    public static int WIDTH = 1000;
    MenuPanel mp = new MenuPanel();
    ConstantVelocity CVP = new ConstantVelocity();


    public MenuFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(0,0,WIDTH,HEIGHT);
        //----
        this.add(CVP);
        // ----
        this.setResizable(false);
        this.setVisible(true);
        this.setTitle("PHASIM");

        String sep = System.getProperty("file.separator");
        ImageIcon logo = new ImageIcon(System.getProperty("user.dir") + sep + "Image" + sep
                + "LOGO.jpg");
        this.setIconImage(logo.getImage());

    }

    public void PHASIM(Graphics g2d) {
       Font PHASIMFont =  new Font(Font.SERIF,Font.ITALIC,30);
       g2d.setFont(PHASIMFont);
        g2d.setColor(new Color(224, 33, 33,50));
        g2d.drawString("PHASIM",20,20);
    }


}
