package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlanePanel extends PanelAbstract implements ActionListener {
    private double x_velocity = 0;
    private double y_velocity = 0;
    private double x_distance = 0;
    private double y_distance;
    private double mass = 1;
    private double force = 0;
    private double mu = 0;
    private int forceDir = 0;
    private double firctionF = 0.0;

    private int objectWidth = 48;
    private int objectHeight = 50;
    private int MAXSCALE = 50; // increase when the block reach the right

    private static final int WIDTH = MainFrame.WIDTH; //1000
    private static final int HEIGHT = MainFrame.HEIGHT;
    private static final int TABLE_HEIGHT = 550;
    private static final int SCALE_HEIGHT = 30;

    private int x = 0;
    private int y = TABLE_HEIGHT-objectHeight - 1;

    private double t = 0.0;

    JTextField massI;
    JTextField velocityI;
    JTextField forceI;
    JTextField forceDirI;
    JTextField muI;

    JLabel massLabel = new JLabel("Mass:");
    JLabel massUnit = new JLabel("kg");
    JLabel velocityLabel = new JLabel("Velocity:");
    JLabel velocityUnit = new JLabel("m/s");
    JLabel forceLabel = new JLabel("Force:");
    JLabel forceUnit = new JLabel("N");
    JLabel forceDirLabel = new JLabel("Force Dir:");
    JLabel forceDirUnit = new JLabel(String.valueOf('\u00B0'));
    JLabel muLabel = new JLabel('\u00B5' + ": ");

    public PlanePanel() {
        super();
        setInput();
    }

    public void setInput() {

        // initialize all input label
        setLabel();
        // initialize textfield
        massI = new JTextField("1",4);
        velocityI = new JTextField("0",4);
        forceI = new JTextField("0",4);
        forceDirI = new JTextField("0",4);
        muI = new JTextField("0",4);

        // add textfield
        this.add(massI);
        this.add(velocityI);
        this.add(forceI);
        this.add(forceDirI);
        this.add(muI);

        // set Font
        massI.setFont(InputFont);
        velocityI.setFont(InputFont);
        forceI.setFont(InputFont);
        forceDirI.setFont(InputFont);
        muI.setFont(InputFont);

        //set position
        massI.setBounds(180,50,100,50);
        velocityI.setBounds(180,150,100,50);
        forceI.setBounds(680,50,100,50);
        forceDirI.setBounds(680,150,100,50);
        muI.setBounds(180,250,100,50);
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
        muLabel.setFont(labelFont);

        massLabel.setBounds(30,50,300,50);
        massUnit.setBounds(300,50,100,50);

        velocityLabel.setBounds(30,150,300,50);
        velocityUnit.setBounds(300,150,100,50);

        forceLabel.setBounds(480,50,300,50);
        forceUnit.setBounds(800,50,100,50);

        forceDirLabel.setBounds(480,150,300,50);
        forceDirUnit.setBounds(800,150,300,50);

        muLabel.setBounds(30,250,300,50);

        this.add(massLabel);
        this.add(massUnit);
        this.add(velocityLabel);
        this.add(velocityUnit);
        this.add(forceLabel);
        this.add(forceUnit);
        this.add(forceDirLabel);
        this.add(forceDirUnit);
        this.add(muLabel);
    }

    @Override
    // paint animation
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

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
            Font outputFont = new Font("Cambria",Font.ROMAN_BASELINE,40);
            g2d.setFont(outputFont);
            // round the number to 2-decimal
            g2d.drawString("Time: " + (double) Math.round(t*100) / 100 + " s",50,50);

            double v = (double) Math.round(x_velocity*100)/100;
            g2d.drawString("Vx = " + v + " m/s",50,120);

            double d = (double) Math.round(x_distance*100)/100;
            g2d.drawString("Dx = " + d + " m",50,190);

            firctionF = (double) Math.round(firctionF*100)/100;
            g2d.drawString("fc = " + firctionF + " N",50,260);

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

    @Override
    // draw the forces on the objected
    public void drawForces(Graphics2D g2d) {
        double t = 0.8;
        int object_Px = x + objectWidth/2;
        int object_Py = y + objectHeight/2;
        int legendoffset = 20;

        Font forceFont = new Font("Comic Sans MS",Font.ROMAN_BASELINE,40);
        g2d.setFont(forceFont);

        g2d.setStroke(new BasicStroke(5));


        // draw the gravitational force
        g2d.setColor(new Color(161, 21, 222,220));
        int offset = (int) Math.round(mass * t + 80);
        if (offset >= 135)
            offset = 135;
        g2d.drawLine(object_Px ,object_Py,object_Px ,object_Py + offset);
        g2d.drawLine(object_Px ,object_Py + offset, object_Px - 5 ,object_Py + offset - 20);
        g2d.drawLine(object_Px ,object_Py + offset, object_Px + 5 ,object_Py + offset - 20);
        g2d.drawString("mg",object_Px ,object_Py + offset + legendoffset + 10);

        // draw the Normal force
        // check whether there is a normal force(it may fly)
        if (force/mass*Math.sin((double) forceDir/180*Math.PI) <= mass * 9.8) {
            g2d.setColor(new Color(61, 203, 30, 220));
            double forceOffset = force * Math.sin((double) forceDir / 180 * Math.PI)/9.8;

            int Noffset = (int)Math.round(((mass-forceOffset)/mass)* offset);
            if (Noffset < 40)
                Noffset = 40;
            if (Noffset > 200)
                Noffset = 200;
            g2d.drawLine(object_Px, object_Py, object_Px, object_Py - Noffset);
            g2d.drawLine(object_Px, object_Py - Noffset, object_Px - 5, object_Py - Noffset + 20);
            g2d.drawLine(object_Px, object_Py - Noffset, object_Px + 5, object_Py - Noffset + 20);
            g2d.drawString("N",object_Px, object_Py - Noffset - legendoffset);
        }

        // draw the applied force
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


                if(x_offset < 0) {
                    legendoffset = -legendoffset;
                }
                g2d.drawString("F",object_Px + x_offset + legendoffset,object_Py - y_offset);

                if(x_offset < 0) {
                    legendoffset = -legendoffset;
                }
            }

        // draw the applied force
        // check whether there is a normal force(it may fly)
        if (force/mass*Math.sin((double) forceDir/180*Math.PI) <= mass * 9.8 && mu != 0) {
            g2d.setColor(new Color(93, 15, 238, 220));
            double F_acceleration = force / mass;
            double f_acceleration = (9.8 - F_acceleration * Math.sin((double) forceDir / 180 * Math.PI)) * mu;

            int x_offset = (int) Math.round(f_acceleration*t + 50);
            if (x_offset >= 200) {
                x_offset = 200;
            }

            if (x_velocity > 0) {
                if(forceDir <= 185 && forceDir >= 175) {
                    // move down a little bit
                    object_Py = object_Py + 10;
                    g2d.drawLine(object_Px, object_Py, object_Px - x_offset, object_Py);
                    g2d.drawLine(object_Px - x_offset, object_Py, object_Px - x_offset + 20, object_Py + 5);
                    g2d.drawLine(object_Px - x_offset, object_Py, object_Px - x_offset + 20, object_Py - 5);
                    g2d.drawString("fc",object_Px - x_offset - legendoffset,object_Py);
                    object_Py = object_Py - 10;
                } else {
                    g2d.drawLine(object_Px, object_Py, object_Px - x_offset, object_Py);
                    g2d.drawLine(object_Px - x_offset, object_Py, object_Px - x_offset + 20, object_Py + 5);
                    g2d.drawLine(object_Px - x_offset, object_Py, object_Px - x_offset + 20, object_Py - 5);
                    g2d.drawString("fc",object_Px - x_offset - legendoffset,object_Py);
                }
            } else if (x_velocity == 0) {
                if (f_acceleration * f_acceleration >= F_acceleration * Math.cos((double) forceDir / 180 * Math.PI) * F_acceleration * Math.cos((double) forceDir / 180 * Math.PI)) {
                    if(force > 0 && forceDir != 270 && forceDir != 90) {
                        offset = (int) Math.round(force * t + 50);
                        x_offset = (int) Math.round(offset * Math.cos((double) forceDir / 180 * Math.PI));
                        int arrow = 20;
                        if(x_offset < 0) {
                            arrow = - arrow;
                        }
                        g2d.drawLine(object_Px, object_Py, object_Px - x_offset, object_Py);
                        g2d.drawLine(object_Px - x_offset, object_Py, object_Px - x_offset + arrow, object_Py + 5);
                        g2d.drawLine(object_Px - x_offset, object_Py, object_Px - x_offset + arrow, object_Py - 5);
                        g2d.drawString("fc",object_Px - x_offset - legendoffset,object_Py);
                    }
                } else if (F_acceleration * Math.cos((double) forceDir / 180 * Math.PI) > 0) {
                    g2d.drawLine(object_Px, object_Py, object_Px - x_offset, object_Py);
                    g2d.drawLine(object_Px - x_offset, object_Py, object_Px - x_offset + 20, object_Py + 5);
                    g2d.drawLine(object_Px - x_offset, object_Py, object_Px - x_offset + 20, object_Py - 5);
                    g2d.drawString("fc",object_Px - x_offset - legendoffset,object_Py);
                }
            } else if (x_velocity < 0) {
                g2d.drawLine(object_Px, object_Py, object_Px + x_offset, object_Py);
                g2d.drawLine(object_Px + x_offset, object_Py, object_Px + x_offset - 20, object_Py + 5);
                g2d.drawLine(object_Px + x_offset, object_Py, object_Px + x_offset - 20, object_Py - 5);
                g2d.drawString("fc",object_Px + x_offset + legendoffset,object_Py);

            }


        }
        g2d.setStroke(new BasicStroke());
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);
        Object source = e.getSource();
        if (source == errorMassage) {
            errorM("Input invalid!");
        }
    }

    @Override
    public void runTime() {
        double k = 0.01;
        t += k;

        //------------update x_velocity;--------------------
        //Applied Force acceleration
        double F_acceleration = force / mass;
        //Frictional force acceleration
        int x_choices;
        double f_acceleration = (9.8 - F_acceleration*Math.sin((double) forceDir/180*Math.PI))*mu;

        //(alternative method) x_choices is a variable helping the decision of velocity more accurate by judging the object's acceleration

//        if  (F_acceleration*Math.cos((double) forceDir/180*Math.PI) - f_acceleration <= -1000) {
//            x_choices = (int) Math.round(x_velocity / 100);
//        } else if  (F_acceleration*Math.cos((double) forceDir/180*Math.PI) - f_acceleration <= -100) {
//            x_choices = (int) Math.round(x_velocity / 10);
//        } else {
//            x_choices = (int) Math.round(x_velocity);
//        }
        // (may have bug)

        double x_appliedforceAcceleration;
        if (forceDir == 90 || forceDir == 270) {
            x_appliedforceAcceleration = 0;
        } else {
            x_appliedforceAcceleration = F_acceleration*Math.cos((double) forceDir/180*Math.PI);
        }



        if (x_velocity > 0) {
            f_acceleration = -f_acceleration;
            // case moving to the right
            // update velocity with frictional force
            x_velocity =  (x_appliedforceAcceleration + f_acceleration)*k + x_velocity ;

        } else if (x_velocity == 0) {
            if (f_acceleration * f_acceleration >= x_appliedforceAcceleration * x_appliedforceAcceleration){
            // case stationary and won't move
                x_velocity = 0;
                f_acceleration = -x_appliedforceAcceleration;
            } else if(x_appliedforceAcceleration > 0){
                f_acceleration = -f_acceleration;
                // case stationary and tend to move right
                // update velocity with frictional force
                x_velocity =  k* (x_appliedforceAcceleration+ f_acceleration) + x_velocity;
            } else if(x_appliedforceAcceleration < 0){
                // case stationary and tend to move left
                // update velocity with frictional force
                x_velocity =  k*(x_appliedforceAcceleration + f_acceleration) + x_velocity;
            }
        } else {
            // case moving to the left
            x_velocity =  k*(x_appliedforceAcceleration + f_acceleration) + x_velocity;
        }
        firctionF = f_acceleration;
        // determine to not skip the stationary status
        if (x_velocity>0 && x_velocity +  k*(x_appliedforceAcceleration + f_acceleration) < 0){
            x_velocity = 0;
        }




        //------------update y_velocity-----------------------
        y_velocity = (F_acceleration*Math.sin((double) forceDir/180*Math.PI)-9.8)*k + y_velocity;
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

    @Override
    public void run() {
        try{
            x_velocity = Double.parseDouble(velocityI.getText());
            mass = Double.parseDouble(massI.getText());
            force = Double.parseDouble(forceI.getText());
            forceDir = Integer.parseInt(forceDirI.getText());
            mu = Double.parseDouble(muI.getText());
            if (mass <= 0||x_velocity < 0 || forceDir<0 || forceDir >=360 || mu<0 || mu > 1 || force < 0) {
                throw new NumberFormatException();
            }

            timer.start();

            removeInput();
            addOutputBtn();

        } catch (NumberFormatException e) {
            if(this.isAncestorOf(errorLabel)) {
                this.remove(errorLabel);
                this.updateUI();
            }

            errort = 0;
            errorMassage.start();
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

        this.remove(muI);
        this.remove(muLabel);

        this.remove(runB);

    }


    // add pause and restart
    public void addOutputBtn() {
        this.add(pauseB);
        this.add(restartB);
    }

    @Override
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
        this.add(runB);
    }
}
