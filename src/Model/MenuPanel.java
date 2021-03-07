package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel{
    private static final int WIDTH = MenuFrame.WIDTH; //1000
    private static final int HEIGHT = MenuFrame.HEIGHT;
    String[] Menu = { "Plane", "Pendulum"};
    private JComboBox menuList;

    ImageIcon Logo;
    public MenuPanel(ActionListener frame, JButton Start) {
        this.setBounds(0,0,WIDTH,HEIGHT);
        this.setLayout(null);
        load_image();

        addMenuBar();

        // add start panel

        Start.setUI(new BtnUI());
        Start.addActionListener(frame);
        Start.setFont(new Font("Lucida Calligraphy",Font.PLAIN,50));
        Start.setBounds(400,425,200,200);
        Start.setForeground(new Color(10, 166, 205));
        Start.setBackground(new Color(171, 234, 253,30));

        this.add(Start);
    }

    private void addMenuBar() {
        //Create the combo box, select item at index 4.
        //Indices start at 0

        menuList = new JComboBox(Menu);
        menuList.setUI(new MenuComboBoxUI());
        menuList.setFont(new Font("Lucida Calligraphy",Font.PLAIN,40));
        menuList.setSelectedIndex(-1);
        menuList.setMaximumRowCount(4);
        menuList.setEditable(false);
        menuList.setBounds(WIDTH/2-170,400,400,50);
        this.add(menuList);

    }


    public void paintComponent(Graphics g) {

        // paint background
        super.paintComponent(g);
        GradientPaint gp = new GradientPaint(0,0,new Color(171, 234, 253),WIDTH,HEIGHT,Color.WHITE);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setPaint(gp);
        g2d.fillRect(0,0,WIDTH, HEIGHT);
        g2d.drawImage(Logo.getImage(),30,HEIGHT- 200,934/8,1000/8,null);

        PHASIM(g2d);
        TITLE(g2d);


    }

    // load image
    private void load_image() {
        String sep = System.getProperty("file.separator");
        Logo = new ImageIcon(System.getProperty("user.dir") + sep + "Image" + sep
                + "Menu_logo3.png");

    }

    private void PHASIM(Graphics g2d) {
        Font PHASIMFont =  new Font(Font.SERIF,Font.ITALIC,40);
        g2d.setFont(PHASIMFont);
        g2d.setColor(new Color(238, 10, 10, 100));
        g2d.drawString("PHASIM",WIDTH - 180,HEIGHT-70);
    }

    private void TITLE(Graphics g2d) {
        Font PHASIMFont =  new Font("Ink Free",Font.BOLD,175);
        g2d.setFont(PHASIMFont);
        g2d.setColor(new Color(3, 46, 210, 150));
        g2d.drawString("PHASIM",WIDTH/2-360,300);
    }

    // return selected object to main frame
    public String getSelection() {
        if(menuList.getSelectedItem() != null) {
            String s = (String) menuList.getSelectedItem();
            return s;
        } else {
            return "";
        }
    }
}
