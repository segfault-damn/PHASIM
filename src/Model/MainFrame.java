package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements ActionListener {

    public static int HEIGHT = 750;
    public static int WIDTH = 1000;

    private ImageIcon backIcon;

    // Start Btn in menu
    public JButton Start = new JButton("Start");
    public JButton back = new JButton();

    MenuPanel mp = new MenuPanel(this,Start);
    PanelAbstract PlaneP = new PlanePanel();
    PanelAbstract PendulumP = new PendulumPanel();
    PanelAbstract ProjectileP = new Projectile();



    public MainFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setBounds(150,150,WIDTH,HEIGHT);

        //set back Button
        load_image();
        back.setIcon(backIcon);
        back.setUI(new BtnUI());
        back.addActionListener(this);
        back.setBounds(900,50,60,60);
        back.setBackground(new Color(1, 6, 16,250));



        //----------------------------------------

        this.setContentPane(mp);

        // --------------------------------------
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


    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();

        // control Content Panel
        if (source == Start) {
            String s = mp.getSelection();
            switch (s) {
                case "Plane" :
                    this.setContentPane(PlaneP);
                    //add back
                    PlaneP.add(back);
                    break;
                case "Pendulum" :
                    this.setContentPane(PendulumP);
                    //add back
                    PendulumP.add(back);
                    break;
                case "Projectile":
                    this.setContentPane(ProjectileP);
                    //add back
                    ProjectileP.add(back);
                    break;
            }
        } else if (source == back) {
            this.setContentPane(mp);

            // shut down running panel
            if (PlaneP.isRun()) {
                PlaneP.pause();
            } else if (PendulumP.isRun()) {
                PendulumP.pause();
            } else if (ProjectileP.isRun()) {
              //  ProjectileP.pause();
            }
        }
    }

    // load image
    private void load_image() {
        String sep = System.getProperty("file.separator");
        backIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Image" + sep
                + "back.png");
        backIcon = new ImageIcon(backIcon.getImage().getScaledInstance(50,50,1));

    }
}
