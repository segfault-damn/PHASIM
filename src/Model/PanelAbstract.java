package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public abstract class PanelAbstract extends JPanel implements ActionListener {
    private static final int WIDTH = MainFrame.WIDTH; //1000
    private static final int HEIGHT = MainFrame.HEIGHT;

    // IMAGEICONS
    ImageIcon PauseIcon;
    ImageIcon RunIcon;
    ImageIcon RestartIcon;

    // Buttons
    JButton runB;
    JButton pauseB;
    JButton resumeB;
    JButton restartB;

    // error Label
    protected JLabel errorLabel;

    // timers
    protected Timer timer = new Timer(10,this);
    protected Timer errorMassage = new Timer(10,this);
    protected int errort = 0;

    // Interface setting
    protected Font labelFont = new Font(Font.SERIF,Font.CENTER_BASELINE,35);
    protected Font InputFont = new Font(Font.SERIF,Font.HANGING_BASELINE,35);

    public PanelAbstract() {
        this.setBounds(0,0,WIDTH,HEIGHT);
        this.setLayout(null);
        load_image();
        setBtn();
        this.add(runB);
    }


    public void setBtn() {
        setRunButton();
        setPauseButton();
        setResumeButton();
        setRestartButton();
    }


    //-------------BTN----------------------------------------
    // set Run
    public void setRunButton() {
        runB = new JButton(RunIcon);
        runB.setUI(new BtnUI());
        runB.addActionListener(this);
        runB.setBounds(900,150,60,60);
        runB.setBackground(new Color(171, 234, 253,30));
    }

    //set pause
    public void setPauseButton() {
        pauseB = new JButton(PauseIcon);
        pauseB.setUI(new BtnUI());
        pauseB.addActionListener(this);
        pauseB.setBounds(900,150,60,60);
        pauseB.setBackground(new Color(171, 234, 253,30));

    }

    //set Resume
    public void setResumeButton() {
        resumeB = new JButton(RunIcon);
        resumeB.setUI(new BtnUI());
        resumeB.addActionListener(this);
        resumeB.setBounds(900,150,60,60);
        resumeB.setBackground(new Color(171, 234, 253,30));
    }

    //set Restart
    public void setRestartButton() {
        restartB = new JButton(RestartIcon);
        restartB.setUI(new BtnUI());
        restartB.addActionListener(this);
        restartB.setBounds(900,250,60,60);
        restartB.setBackground(new Color(171, 234, 253,30));

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
        g2d.setColor(new Color(238, 10, 10, 60));
        g2d.drawString("PHASIM",WIDTH - 180,HEIGHT-70);
    }

    public abstract void drawForces(Graphics2D g2d);

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == pauseB) {
            pause();
        } else if (source == resumeB) {
            resume();
        } else if (source == timer) {
            runTime();
        } else if (source == runB) {
            run();
        } else if (source == restartB) {
            restart();
        }
    }

    public abstract void run();
    public abstract void runTime();
    public abstract void restart();


    // pause setting
    public void pause() {
        this.remove(pauseB);
        repaint();
        timer.stop();
        this.add(resumeB);
    }

    //resume setting
    public void resume() {
        this.remove(resumeB);
        timer.restart();
        this.add(pauseB);
    }

    // print out error messages
    public void errorM(String s) {
        errort += 1;
        if(errort == 10) {
            errorLabel = new JLabel(s);
            this.add(errorLabel);

            errorLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
            errorLabel.setBounds(WIDTH/2-150, HEIGHT-120, 400, 60);
            errorLabel.setForeground(new Color(238, 46, 68,30));

        } else if (errort > 10 && errort<= 50) {
            errorLabel.setForeground(new Color(238, 46, 68,errort*5));
        } else if(errort >= 175 && errort<300) {
            errorLabel.setForeground(new Color(238, 46, 68,2*(300 - errort)));
        } else if(errort == 300) {
            this.remove(errorLabel);
            this.updateUI();
            errorMassage.stop();
            errort=0;
        }
    }

    public boolean isRun() {
        return timer.isRunning();
    }

    // load imagee
    private void load_image() {
        String sep = System.getProperty("file.separator");
        PauseIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Image" + sep
                + "pause.png");
        RunIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Image" + sep
                + "Run.png");
        RestartIcon = new ImageIcon(System.getProperty("user.dir") + sep + "Image" + sep
                + "restart.png");

        PauseIcon = new ImageIcon(PauseIcon.getImage().getScaledInstance(50,50,1));
        RunIcon = new ImageIcon(RunIcon.getImage().getScaledInstance(50,50,1));
        RestartIcon = new ImageIcon(RestartIcon.getImage().getScaledInstance(50,50,1));
    }
}
