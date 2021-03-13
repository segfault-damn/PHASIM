package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Template extends JPanel implements ActionListener {
    private static final int WIDTH = MainFrame.WIDTH; //1000
    private static final int HEIGHT = MainFrame.HEIGHT;

    public Template() {
        this.setBounds(0,0,WIDTH,HEIGHT);
        this.setLayout(null);
        load_image();
    }

    // paint animation
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // draw Background;
        GradientPaint gp = new GradientPaint(0,0,new Color(171, 234, 253),WIDTH,HEIGHT,Color.WHITE);

        g2d.setPaint(gp);
        g2d.fillRect(0,0,WIDTH, HEIGHT);

        // DRAW PHASIM
        PHASIM(g2d);
    }


    // draw the logo
    public void PHASIM(Graphics2D g2d) {
        Font PHASIMFont =  new Font(Font.SERIF,Font.ITALIC,40);
        g2d.setFont(PHASIMFont);
        g2d.setColor(new Color(238, 10, 10, 100));
        g2d.drawString("PHASIM",WIDTH - 180,HEIGHT-70);
    }


    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {

    }

    // load imagee
    private void load_image() {
        String sep = System.getProperty("file.separator");
//        PauseIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Image" + sep
//                + "pause.png");
//        RunIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Image" + sep
//                + "Run.png");
//        RestartIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Image" + sep
//                + "restart.png");
//
//        PauseIcon = new ImageIcon(PauseIcon.getImage().getScaledInstance(50,50,1));
//        RunIcon = new ImageIcon(RunIcon.getImage().getScaledInstance(50,50,1));
//        RestartIcon = new ImageIcon(RestartIcon.getImage().getScaledInstance(50,50,1));
    }

}
