package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConstantVelocity extends JPanel implements ActionListener {
    private double x_velocity = 0;
    private double y_velocity = 0;
    private double x_distance = 0;
    private double y_distance;
    private double mass = 1;
    private double force = 0;
    private int forceDir = 0;

    private int objectWidth = 48;
    private int objectHeight = 50;
    private int MAXSCALE = 50; // increase when the block reach the right

    private static final int WIDTH = MenuFrame.WIDTH; //1000
    private static final int HEIGHT = MenuFrame.HEIGHT;
    private static final int TABLE_HEIGHT = 550;
    private static final int SCALE_HEIGHT = 30;

    private int x = 0;
    private int y = TABLE_HEIGHT-objectHeight - 1;

    // IMAGEICONS
    ImageIcon PauseIcon;
    ImageIcon RunIcon;
    ImageIcon RestartIcon;


    // timers
    Timer timer = new Timer(10,this);
    Timer errorMassage = new Timer(10,this);
    double t = 0.0;
    int errort =0;

    // Interface setting

    private Font labelFont = new Font(Font.SERIF,Font.CENTER_BASELINE,35);
    private Font InputFont = new Font(Font.SERIF,Font.HANGING_BASELINE,35);


    JTextField massI;
    JTextField velocityI;
    JTextField forceI;
    JTextField forceDirI;

    JLabel massLabel = new JLabel("Mass:");
    JLabel massUnit = new JLabel("kg");
    JLabel velocityLabel = new JLabel("Velocity:");
    JLabel velocityUnit = new JLabel("m/s");
    JLabel forceLabel = new JLabel("Force:");
    JLabel forceUnit = new JLabel("N");
    JLabel forceDirLabel = new JLabel("Force Dir:");
    JLabel forceDirUnit = new JLabel(String.valueOf('\u00B0'));
    JLabel errorM;

    JButton runB;
    JButton pauseB;
    JButton resumeB;
    JButton restartB;

    public ConstantVelocity() {
        this.setBounds(0,0,WIDTH,HEIGHT);
        this.setLayout(null);
        load_image();
        setInput();
        setBtn();
        this.add(runB);

    }

    public void setInput() {

        // initialize all input label
        setLabel();
        // initialize textfield
        massI = new JTextField("1",4);
        velocityI = new JTextField("0",4);
        forceI = new JTextField("0",4);
        forceDirI = new JTextField("0",4);

        // add textfield
        this.add(massI);
        this.add(velocityI);
        this.add(forceI);
        this.add(forceDirI);

        // set Font
        massI.setFont(InputFont);
        velocityI.setFont(InputFont);
        forceI.setFont(InputFont);
        forceDirI.setFont(InputFont);

        //set position
        massI.setBounds(180,50,100,50);
        velocityI.setBounds(180,150,100,50);
        forceI.setBounds(680,50,100,50);
        forceDirI.setBounds(680,150,100,50);
    }

    public void setLabel() {

        massLabel.setFont(labelFont);
        massUnit.setFont(labelFont);
        velocityLabel.setFont(labelFont);
        velocityUnit.setFont(labelFont);
        forceLabel.setFont(labelFont);
        forceUnit.setFont(labelFont);
        forceDirLabel.setFont(labelFont);
        forceDirUnit.setFont(labelFont);

        massLabel.setBounds(30,50,300,50);
        massUnit.setBounds(300,50,100,50);

        velocityLabel.setBounds(30,150,300,50);
        velocityUnit.setBounds(300,150,100,50);

        forceLabel.setBounds(480,50,300,50);
        forceUnit.setBounds(800,50,100,50);

        forceDirLabel.setBounds(480,150,300,50);
        forceDirUnit.setBounds(800,150,300,50);

        this.add(massLabel);
        this.add(massUnit);
        this.add(velocityLabel);
        this.add(velocityUnit);
        this.add(forceLabel);
        this.add(forceUnit);
        this.add(forceDirLabel);
        this.add(forceDirUnit);
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
        runB.addActionListener(this);
        runB.setBounds(900,150,60,60);
        runB.setBackground(new Color(171, 234, 253,30));
    }

    //set pause
    public void setPauseButton() {
        pauseB = new JButton(PauseIcon);
        pauseB.addActionListener(this);
        pauseB.setBounds(900,150,60,60);
        pauseB.setBackground(new Color(171, 234, 253,30));

    }

    //set Resume
    public void setResumeButton() {
        resumeB = new JButton(RunIcon);
        resumeB.addActionListener(this);
        resumeB.setBounds(900,150,60,60);
        resumeB.setBackground(new Color(171, 234, 253,30));
    }

    //set Restart
    public void setRestartButton() {
        restartB = new JButton(RestartIcon);
        restartB.addActionListener(this);
        restartB.setBounds(900,250,60,60);
        restartB.setBackground(new Color(171, 234, 253,30));

    }

    //----------------------------------------------------------------


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

        // DRAW OBJECT
        g2d.setColor(new Color(141, 30, 18,200));
        g2d.fillRect(x,y,objectWidth,objectHeight);

        // DRAW THE DESK
        g2d.setColor(new Color(24, 9, 2));
        g2d.setStroke(new BasicStroke(10));
        g2d.drawLine(0,TABLE_HEIGHT + 5,WIDTH,TABLE_HEIGHT + 5);


        //DRAW SCALE
        g2d.setStroke(new BasicStroke());
        drawlineS(g2d,0);
        drawlineS(g2d,1);
        drawlineS(g2d,2);
        drawlineS(g2d,3);
        drawlineS(g2d,4);
        drawlineS(g2d,5);

        // show the running data
        if(timer.isRunning() || t > 0) {
            Font outputFont = new Font(Font.SERIF,Font.ROMAN_BASELINE,40);
            g2d.setFont(outputFont);
            // round the number to 2-decimal
            g2d.drawString("Time: " + (double) Math.round(t*100) / 100 + " s",50,50);

            double v = (double) Math.round(x_velocity*100)/100;
            g2d.drawString("Vx = " + v + " m/s",50,120);

            double d = (double) Math.round(x_distance*100)/100;
            g2d.drawString("Dx = " + d + " m",50,190);

            drawForces(g2d);

        }

    }


    // draw the scale line with initial x + dx * 19 pixel/m and scale number below
    public void drawlineS(Graphics2D g2d,int dx) {
        g2d.drawLine(objectWidth/2 + dx * 190, TABLE_HEIGHT,objectWidth/2 + dx * 190,
                TABLE_HEIGHT + SCALE_HEIGHT);
        g2d.setFont(new Font("name",Font.BOLD,20));
        g2d.drawString(String.valueOf(dx*MAXSCALE/5) + "m",objectWidth/2 + dx * 180+10 ,TABLE_HEIGHT + SCALE_HEIGHT + 35);
    }

    // draw the forces on the objected
    public void drawForces(Graphics2D g2d) {
        double t = 0.8;
        int object_Px = x + objectWidth/2;
        int object_Py = y + objectHeight/2;
        g2d.setStroke(new BasicStroke(5));


        // draw the gravitational force
        g2d.setColor(new Color(161, 21, 222,220));
        int offset = (int) Math.round(mass * t + 80);
        if (offset >= 135)
            offset = 135;
        g2d.drawLine(object_Px ,object_Py,object_Px ,object_Py + offset);
        g2d.drawLine(object_Px ,object_Py + offset, object_Px - 5 ,object_Py + offset - 20);
        g2d.drawLine(object_Px ,object_Py + offset, object_Px + 5 ,object_Py + offset - 20);
        g2d.drawString("mg",object_Px ,object_Py + offset + 30);

        // draw the Normal force
        // check whether there is a normal force(it may fly)
        if (force/mass*Math.sin((double) forceDir/180*Math.PI) <= mass * 9.8) {
            g2d.setColor(new Color(61, 203, 30, 220));
            double forceOffset = force * Math.sin((double) forceDir / 180 * Math.PI)/9.8;

            int Noffset = (int)Math.round(((mass-forceOffset)/mass)* offset);
            if (Noffset < 40)
                Noffset = 40;
            g2d.drawLine(object_Px, object_Py, object_Px, object_Py - Noffset);
            g2d.drawLine(object_Px, object_Py - Noffset, object_Px - 5, object_Py - Noffset + 20);
            g2d.drawLine(object_Px, object_Py - Noffset, object_Px + 5, object_Py - Noffset + 20);
            g2d.drawString("N",object_Px, object_Py - Noffset - 20);
        }

        // draw the applied force
        // check whether there is a normal force(it may fly)
            if(force > 0) {
                g2d.setColor(new Color(238, 15, 49, 220));


                offset = (int)Math.round(force * t + 50);
                if (offset >= 120)
                    offset = 120;
                int x_offset = (int) Math.round(offset * Math.cos((double) forceDir / 180 * Math.PI));
                int y_offset = (int) Math.round(offset * Math.sin((double) forceDir / 180 * Math.PI));

                g2d.drawLine(object_Px, object_Py, object_Px + x_offset, object_Py - y_offset);
                double x = 0.3;
                double arrow_length = 15;
                int left_Arrow_x = (int) Math.round(arrow_length * Math.sin(Math.PI / 2 - x - (double) forceDir / 180 * Math.PI));
                int left_Arrow_y = (int) Math.round(arrow_length * Math.cos(Math.PI / 2 - x - (double) forceDir / 180 * Math.PI));
                int right_Arrow_x = (int) Math.round(arrow_length * Math.sin(Math.PI / 2 + x - (double) forceDir / 180 * Math.PI));
                int right_Arrow_y = (int) Math.round(arrow_length * Math.cos(Math.PI / 2 + x - (double) forceDir / 180 * Math.PI));

                g2d.drawLine(object_Px + x_offset, object_Py - y_offset, object_Px + x_offset - left_Arrow_x, object_Py - y_offset + left_Arrow_y);
                g2d.drawLine(object_Px + x_offset, object_Py - y_offset, object_Px + x_offset - right_Arrow_x, object_Py - y_offset + right_Arrow_y);

                int legendoffset = 20;
                if(x_offset < 0) {
                    legendoffset = -legendoffset;
                }
                g2d.drawString("F",object_Px + x_offset + legendoffset,object_Py - y_offset);
            }
        g2d.setStroke(new BasicStroke());
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
        Object source = e.getSource();
        if (source == timer) {
            runTime();
        } else if (source == runB) {
            run();
        } else if (source == pauseB) {
            pause();
        } else if (source == resumeB) {
            resume();
        } else if (source == restartB) {
            restart();
        } else if (source == errorMassage) {
            errorM("Input invalid!");
        }
    }

    public void runTime() {
        double k = 0.01;
        t += k;

        //update x_velocity;
        double acceleration = force / mass;
        x_velocity =  k*acceleration*Math.cos((double) forceDir/180*Math.PI) + x_velocity;

        // update y_velocity
        y_velocity = ((acceleration)*Math.sin((double) forceDir/180*Math.PI)-9.8)*k + y_velocity;
        if(y_velocity < 0) {
            y_velocity = 0;
        }

        // update Distance
        x_distance = x_distance + k * x_velocity; // actual distance
        x = (int) Math.round(x_distance * 950 / MAXSCALE);// set x

        // update object height if it has y_velocity
        y_distance = y_distance + k * y_velocity;
        y = TABLE_HEIGHT-objectHeight - 1 - (int) Math.round(y_distance * 10);

        // update scale if needed
        if (x_distance >= MAXSCALE) {
            x = WIDTH/4;
            MAXSCALE = MAXSCALE * 4;
        } else if (x_distance < MAXSCALE/4 && x_velocity<0 && MAXSCALE>50) {
            x = WIDTH*4;
            MAXSCALE = MAXSCALE/4;
        }
        repaint();
    }

    public void run() {
        try{
            x_velocity = Double.parseDouble(velocityI.getText());
            mass = Double.parseDouble(massI.getText());
            force = Double.parseDouble(forceI.getText());
            forceDir = Integer.parseInt(forceDirI.getText());
            if (mass <= 0||x_velocity < 0 || forceDir<0 || forceDir >=360) {
                throw new NumberFormatException();
            }

            timer.start();

            removeInput();
            addOutputBtn();

        } catch (NumberFormatException e) {
            if(this.isAncestorOf(errorM)) {
                this.remove(errorM);
                this.updateUI();
            }

            errort = 0;
            errorMassage.start();
        }

    }

    public void errorM(String s) {
        errort += 1;
        if(errort == 10) {
            errorM = new JLabel(s);
            this.add(errorM);

            errorM.setFont(new Font(Font.MONOSPACED, Font.BOLD, 30));
            errorM.setBounds(WIDTH/2-150, HEIGHT-120, 400, 60);
            errorM.setForeground(new Color(238, 46, 68,30));

        } else if (errort > 10 && errort<= 51) {
            errorM.setForeground(new Color(238, 46, 68,errort*5));
        } else if(errort >= 175 && errort<300) {
            errorM.setForeground(new Color(238, 46, 68,2*(300 - errort)));
        } else if(errort == 300) {
            this.remove(errorM);
           this.updateUI();
            errorMassage.stop();
            errort=0;
        }
    }


    // remove the input label,unit and text area
    public void removeInput() {
        this.remove(massI);
        this.remove(massUnit);
        this.remove(massLabel);

        this.remove(velocityI);
        this.remove(velocityUnit);
        this.remove(velocityLabel);

        this.remove(forceI);
        this.remove(forceUnit);
        this.remove(forceLabel);

        this.remove(forceDirI);
        this.remove(forceDirLabel);
        this.remove(forceDirUnit);
        this.remove(runB);

    }

    // add pause and restart
    public void addOutputBtn() {
        this.add(pauseB);
        this.add(restartB);
    }

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

    // restart setting
    public void restart() {
        remove(resumeB);
        remove(pauseB);
        remove(restartB);
        timer.stop();

        t = 0.0;

        x_distance = y_distance = 0.0;
        x = 0;
        y = TABLE_HEIGHT - objectHeight - 1;

        x_velocity = y_velocity = 0;
        force = 0.0;
        forceDir = 0;
        MAXSCALE = 50;

        repaint();
        setInput();
        setLabel();
        this.add(runB);
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
