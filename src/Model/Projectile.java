package Model;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static sun.swing.MenuItemLayoutHelper.max;


public class Projectile extends PanelAbstract implements ActionListener {
    private static final int WIDTH = MainFrame.WIDTH; //1000
    private static final int HEIGHT = MainFrame.HEIGHT;
    private static final int LEFT = 80;
    private static final int objectWidth = 50;
    private static final int objectHeight = 50;
    private static final int tableHeight = 120;
    private static final int X_SCALE_WIDTH = WIDTH - LEFT - 30;// 30 is the offset on the RHS of the screen
    private static final int Y_SCALE_HEIGHT = HEIGHT - tableHeight - 30;// 30 is the offset on the RHS of the screen


    // Labels
    JLabel massLabel = new JLabel("Mass:");
    JLabel massUnit = new JLabel("kg");
    JLabel thetaLabel = new JLabel(String.valueOf('\u03B8')); // theta
    JLabel thetaUnit = new JLabel(String.valueOf('\u00B0')); // degree
    JLabel velocityLabel = new JLabel("Velocity:");
    JLabel velocityUnit = new JLabel("m/s");
    JLabel gravityLabel = new JLabel("Gravity:");
    JLabel gravityUnit = new JLabel("m/s^2");

    JLabel bLabel = new JLabel("Air Drag Coefficient:");
    JLabel bEq1 = new JLabel("(Linear Model)   -");
    JLabel bEq2 = new JLabel(" v\u20D7");
    // Input
    JTextField massI;
    JTextField thetaI;
    JTextField velocityI;
    JTextField gravityI;
    JTextField bI;

    JButton detailBtn;
    private int detailSwitch = 1; // detail btn flag-- 1: on; 2:off;


    // variables
    private double t = 0.0;//time
    private double g = 9.8;//gravity > 0
    private double m = 1.0;// mass > 0
    private double b = 0;// coefficient >= 0
    private double thetaSet =Math.PI/6; // initial angle
    private double h = 0;// height >= 0

    private double x_r = 0.0;
    private double y_r = 0.0;
    private double theta = thetaSet;
    private double v = 30;
    private double vx;
    private double vy;
    private double ax = 0.0;
    private double ay = 0.0;

    private double v0 = v;


    private double XScaleFactor;
    private double YScaleFactor;

    // max scale in the real distance
    private int MAX_X;
    private int MAX_Y;
    // position
    private int x = LEFT ;
    private int y = HEIGHT - tableHeight - objectHeight/2;

    private double terminalV = -1; // terminal velocity is -1 if b = 0;



    public Projectile() {
        super();
        setInput();
        setDetailBtn();
    }

    public void setInput() {

        // initialize all input label
        setLabel();
        // initialize textfield
        massI = new JTextField("1.0",4);
        thetaI = new JTextField("30",4);
        velocityI = new JTextField("30",4);
        gravityI = new JTextField("9.8",4);
        bI = new JTextField("0",4);

        // add textfield
        this.add(massI);
        this.add(thetaI);
        this.add(velocityI);
        this.add(gravityI);
        this.add(bI);

        // set Font
        massI.setFont(InputFont);
        thetaI.setFont(InputFont);
        velocityI.setFont(InputFont);
        gravityI.setFont(InputFont);
        bI.setFont(InputFont);

        //set position
        massI.setBounds(180,50,100,50);
        gravityI.setBounds(180,150,100,50);
        thetaI.setBounds(680,50,100,50);
        velocityI.setBounds(680,150,100,50);

        bI.setBounds(680,250,100,50);
    }

    public void setLabel() {

        massLabel.setFont(labelFont);
        massUnit.setFont(labelFont);
        thetaLabel.setFont(labelFont);
        thetaUnit.setFont(labelFont);
        velocityLabel.setFont(labelFont);
        velocityUnit.setFont(labelFont);
        gravityLabel.setFont(labelFont);
        gravityUnit.setFont(labelFont);
        bLabel.setFont(labelFont);
        bEq1.setFont(labelFont);
        bEq2.setFont(labelFont);


        massLabel.setBounds(30,50,300,50);
        massUnit.setBounds(300,50,100,50);

        gravityLabel.setBounds(30,150,300,50);
        gravityUnit.setBounds(300,150,300,50);

        thetaLabel.setBounds(520,50,300,50);
        thetaUnit.setBounds(800,50,100,50);

        velocityLabel.setBounds(520,150,300,50);
        velocityUnit.setBounds(800,150,100,50);

        bLabel.setBounds(30,250,600,50);
        bEq1.setBounds(410,250,350,50);
        bEq2.setBounds(785,250,100,50);

        this.add(massLabel);
        this.add(massUnit);
        this.add(thetaLabel);
        this.add(thetaUnit);
        this.add(velocityLabel);
        this.add(velocityUnit);
        this.add(gravityLabel);
        this.add(gravityUnit);
        this.add(bLabel);
        this.add(bEq1);
        this.add(bEq2);

    }

    private void setDetailBtn() {
        detailBtn = new JButton(); // initialing with detail on
        detailBtn.setUI(new BtnUI());
        detailBtn.addActionListener(this);
        detailBtn.setFont(new Font("Cambria",Font.PLAIN,25));
        detailBtn.setBounds(750,10,300,30);
        detailBtn.setForeground(new Color(79, 113, 236,200));
        detailBtn.setBackground(new Color(186, 185, 184,100));
        detailBtn.setLabel("Hide Details");
        this.add(detailBtn);
    }

    // paint animation
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        Graphics2D g2d = (Graphics2D) gr;


        // DRAW OBJECT
        g2d.setColor(new Color(141, 30, 18,200));
        g2d.fillRoundRect(x - objectHeight/2,y - objectHeight/2,objectWidth,objectHeight,50,50);


        // DRAW PLATFORM
        g2d.setStroke(new BasicStroke(3));
        g2d.setColor(new Color(14, 10, 10));
        g2d.drawLine(LEFT - objectWidth/2 + 10,HEIGHT - tableHeight, LEFT - objectWidth/2 + 10, (int) Math.round(HEIGHT - h -tableHeight));
        g2d.drawLine(LEFT + objectWidth/2 - 10,HEIGHT - tableHeight, LEFT + objectWidth/2 - 10, (int) Math.round(HEIGHT - h -tableHeight));
        g2d.drawLine(LEFT - objectWidth/2,(int) Math.round(HEIGHT - h -tableHeight), LEFT + objectWidth/2, (int) Math.round(HEIGHT - h -tableHeight));
        g2d.setStroke(new BasicStroke(0));



        if(timer.isRunning() || t > 0) {
            drawForces(g2d);

            drawScaleX(g2d, MAX_X);
            drawScaleY(g2d, MAX_Y);
            if (detailSwitch == 1) {
                drawOutput(g2d);
                drawEnergy(g2d);
            }
        }


    }

    public void drawOutput(Graphics2D g2d) {
        g2d.setColor(new Color(14, 10, 10,200));
        // draw outputs
        Font outputFont = new Font("Cambria",Font.ROMAN_BASELINE,40);
        g2d.setFont(outputFont);
        // round the number to 2-decimal
        g2d.drawString("Time: " + (double) Math.round(t*100) / 100 + " s",90,80);

        double velocity = (double) Math.round(v *100)/100;
        g2d.drawString("v = " + velocity + " m/s",90,150);

        double x_pos = (double) Math.round(x_r*100)/100;
        g2d.drawString("x = " + x_pos + " m",90,220);

        double y_pos = (double) Math.round(y_r*100)/100;

        g2d.drawString("y = " + y_pos + " m",90,290);

        if (b != 0) {
            double f = (double) Math.round(b * v * 100) / 100;
            g2d.drawString("Air Drag Force = " + f + " N", 90, 360);
        }
    }

    @Override
    public void drawForces(Graphics2D g2d) {
        double t = 0.8;
        int legendoffset = 15;

        Font forceFont = new Font("Comic Sans MS",Font.ROMAN_BASELINE,40);
        g2d.setFont(forceFont);
        g2d.setStroke(new BasicStroke(3));
        // draw the gravitational force
        g2d.setColor(new Color(5, 169, 35,220));
        int offset = (int) Math.round(m * t + 80);
        if (offset >= 135)
            offset = 135;
        g2d.drawLine(x,y,x,y + offset);
        g2d.drawLine(x ,y + offset, x - 5 ,y + offset - 20);
        g2d.drawLine(x ,y + offset, x + 5 ,y + offset - 20);
        g2d.drawString("mg",x + legendoffset ,y + offset);

        // draw the Air drag force (if b !=0)
        if (b != 0 && v!= 0) {

            int alphaF;
            int lengthF;

            double maxV = v0;
            if (terminalV > v0) {
                maxV = terminalV;
            }

            if (v > maxV) { // guard
                alphaF = 255;
                lengthF = 135;
            } else {
                alphaF = (int) Math.round(225/maxV * v + 30);
                lengthF = (int) Math.round((offset - 40)/terminalV * v + 40);
            }

            g2d.setColor(new Color(109, 4, 218,alphaF));


            int foffsetX = -(int) Math.round(lengthF * Math.cos(theta));
            int foffsetY = -(int) Math.round(lengthF * Math.sin(theta));
            g2d.drawLine(x, y, x + foffsetX, y - foffsetY);
            int arrow_length = 15;
            // the offset of arrow's degree
            double xoffset = 0.3;
            int left_Arrow_x;
            int left_Arrow_y;
            int right_Arrow_x;
            int right_Arrow_y;

            left_Arrow_x = (int) Math.round(arrow_length * Math.cos(-xoffset + theta));
            left_Arrow_y = (int) Math.round(arrow_length * Math.sin(xoffset - theta));
            right_Arrow_x = (int) Math.round(arrow_length * Math.cos(xoffset + theta));
            right_Arrow_y = (int) Math.round(arrow_length * Math.sin(-xoffset - theta));


            g2d.drawLine(x + foffsetX, y - foffsetY, x + foffsetX + left_Arrow_x, y - foffsetY + left_Arrow_y);
            g2d.drawLine(x + foffsetX, y - foffsetY, x + foffsetX + right_Arrow_x, y - foffsetY + right_Arrow_y);

            if (alphaF < 50) {
                g2d.setColor(new Color(109, 4, 218,40));
            } else {
                g2d.setColor(new Color(109, 4, 218));
            }
            g2d.drawString("f", x + foffsetX - 20 - legendoffset, y - foffsetY + legendoffset);
        }
    }

    public void drawScaleX(Graphics2D g2d,int X_scale) {
        g2d.setColor(new Color(14, 10, 10,150));
        g2d.setStroke(new BasicStroke(3));
        drawlineS_X(g2d,0,X_scale);
        drawlineS_X(g2d,1,X_scale);
        drawlineS_X(g2d,2,X_scale);
        drawlineS_X(g2d,3,X_scale);
        drawlineS_X(g2d,4,X_scale);
        drawlineS_X(g2d,5,X_scale);
        g2d.setStroke(new BasicStroke(7));
        g2d.drawLine(LEFT,HEIGHT -tableHeight+2,LEFT + X_SCALE_WIDTH,HEIGHT -tableHeight+2);
        g2d.setStroke(new BasicStroke(0));
    }

    public void drawScaleY(Graphics2D g2d,int Y_scale) {
        g2d.setColor(new Color(14, 10, 10,150));
        g2d.setStroke(new BasicStroke(3));
        drawlineS_Y(g2d,0,Y_scale);
        drawlineS_Y(g2d,1,Y_scale);
        drawlineS_Y(g2d,2,Y_scale);
        drawlineS_Y(g2d,3,Y_scale);
        drawlineS_Y(g2d,4,Y_scale);
        drawlineS_Y(g2d,5,Y_scale);
        g2d.setStroke(new BasicStroke(7));
        g2d.drawLine(0,HEIGHT -tableHeight,0,HEIGHT -tableHeight - Y_SCALE_HEIGHT);
        g2d.setStroke(new BasicStroke(0));
    }

    // draw the scale line and scale number below x-axis
    // MAXSCALE is the max scaled width
    public void drawlineS_X(Graphics2D g2d, int dx, int MAXSCALE) {
        int SCALE_HEIGHT = 20;
        g2d.drawLine(LEFT + dx * (X_SCALE_WIDTH/5),HEIGHT - tableHeight,LEFT + dx * (X_SCALE_WIDTH/5),
                HEIGHT-tableHeight+SCALE_HEIGHT);
        g2d.setFont(new Font("name",Font.BOLD,20));
        g2d.drawString(dx*MAXSCALE/5 + "m",LEFT - 40 + dx * (X_SCALE_WIDTH/5),HEIGHT - tableHeight + SCALE_HEIGHT + 35);
    }

    // draw the scale line and scale number beside y-axis
    // MAXSCALE is the max scaled height
    public void drawlineS_Y(Graphics2D g2d, int dx, int MAXSCALE) {
        g2d.drawLine(0,HEIGHT - tableHeight -dx * Y_SCALE_HEIGHT/5 ,LEFT - 60,
                HEIGHT-tableHeight -dx * Y_SCALE_HEIGHT/5);
        g2d.setFont(new Font("name",Font.BOLD,20));
        if (MAXSCALE <5) {
            double height = (double) dx * MAXSCALE / 5;
            g2d.drawString(height + "m", 10, HEIGHT - tableHeight - dx * Y_SCALE_HEIGHT / 5 - 10);
        } else {
            int height = dx * MAXSCALE / 5;
            g2d.drawString(height + "m", 10, HEIGHT - tableHeight - dx * Y_SCALE_HEIGHT / 5 - 10);

        }
    }

    public void drawEnergy(Graphics2D g2d) {
        int MaxBarL = 160;

        // calculate kinetic energy

        double Ek = 0.5 * m * v * v;
        double maxV = v0;
        if (terminalV > v0) {
            maxV = terminalV;
        }
        double EkM;
        if (b <= 1) { // if b<1 maxV is inaccurate since it takes too long to get terminal velocity
            EkM = 0.5 * m * v0 * v0 + m * g * h;
        } else {
            EkM = 0.5 * m * maxV * maxV; // Max Kinetic Energy;
        }
        double EkL= MaxBarL/EkM*Ek; // bar length


        // calculate potential energy
        double Ep =  m * g * y_r;
        double EpM = calMaxH() * m * g;
        double EpL = MaxBarL/EpM*Ep; // bar length

        // calculate total energy
        double TotE;
        double TotalL;
        if(b != 0) {
            TotE =  Ek + Ep;
            double TotEM = 0.5 * m * v0 * v0 + m * g* h;
            TotalL = MaxBarL/TotEM*TotE;
        } else {
            TotE = 0.5 * m * v0 * v0 + m * g* h;
            TotalL = MaxBarL;
        }

        // Case touches ground
        if (y_r ==0 && vy <0){
            Ek = TotE;
            EkL= MaxBarL/EkM*Ek;
        }



        // draw bars
        int left = 550;
        g2d.setFont(new Font(Font.SERIF,Font.BOLD,30));

        // kinetic
        g2d.setColor(new Color(92, 25, 199));
        if (Ek <= 100000) {
            Ek = (double) Math.round(Ek*100)/100;
            g2d.drawString(Ek + "J",left + MaxBarL + 40,80);
        } else {
            Ek = (double) Math.round(Ek/10000)/10;
            g2d.drawString(Ek + "MJ",left + MaxBarL + 40,80);
        }


        g2d.setStroke(new BasicStroke(20));
        g2d.setColor(new Color(92, 25, 199,150));
        if ((int) Math.round(EkL) != 0)
            g2d.drawLine(left,80,left + (int) Math.round(EkL),80);

        g2d.setColor(new Color(92, 25, 199));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(left-10,80+10,left-10,80 - 10);

        g2d.drawString("KE",left - 70,80);

        //potential
        g2d.setColor(new Color(232, 127, 36));
        if (Ep <= 100000) {
            Ep = (double) Math.round(Ep*100)/100;
            g2d.drawString(Ep + "J",left + MaxBarL + 40,110);
        } else {
            Ep = (double) Math.round(Ep/10000)/10;
            g2d.drawString(Ep + "MJ",left + MaxBarL + 40,110);
        }

        g2d.setStroke(new BasicStroke(20));
        g2d.setColor(new Color(232, 127, 36,150));
        if ((int) Math.round(EpL) != 0)
            g2d.drawLine(left,110,left + (int) Math.round(EpL),110);

        g2d.setColor(new Color(232, 127, 36));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(left-10,110 - 10,left-10,110 +10);
        g2d.drawString("PE",left - 67,110);

    // total

        double ME;
        g2d.setColor(new Color(226, 15, 89));
        if (TotE <= 100000) {
            ME = (double) Math.round(TotE * 100) / 100;
            g2d.drawString(ME + "J",left + MaxBarL + 40,140);
        } else {
            ME = (double) Math.round(TotE/10000) / 10;
            g2d.drawString(ME + "MJ",left + MaxBarL + 40,140);
        }
        g2d.setStroke(new BasicStroke(20));
        g2d.setColor(new Color(226, 15, 89,150));
        if (EkM != 0) {
            g2d.drawLine(left, 140, left + (int) Math.round(TotalL), 140);
        } else { // g=0;
            g2d.drawLine(left, 140, left + (int) Math.round(TotalL), 140);
        }

        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(left-10,140 -10,left-10,140 + 10);
        g2d.setColor(new Color(226, 15, 89));
        g2d.drawString("ME",left - 70,140);
    }

    // return max optimized scale of the input scale
    private int scaleSet (double scaleN){
        double resolution = -2;
        while (scaleN > Math.pow(10,resolution + 1)) {
            resolution ++;
        }
        double n = 5 * Math.pow(10,resolution - 1);
        while (n < scaleN) {
             n = n + 5* Math.pow(10,resolution - 1);
        }
        return (int) Math.round(n);
    }


    // cal the max distance from left
    // x = -m/bC(e^(-bt/m)-1)
    // C = v0 cos theta
    // y = -m/bC(e^(-bt/m)-1) - mgt/b
    // C = v0 sin theta + mg/b
    // h = m/bC(e^(-bt/m)-1) + mgt/b
    private double calMaxX() {
       // -h  = - 0.5 * g * t ^2 + v*Math.sin(theta)*t;
        double MaxX;
        if (b >= 1) {
            MaxX = m / b * v0 * Math.cos(theta);
        } else {
           // -h = - 0.5 * g * t* t + v0 * sin(theta) * t;
            double tMax = (-v0 * Math.sin(theta) - Math.sqrt(v0 * Math.sin(theta) * v0 * Math.sin(theta) + 2 * g * h))/(-g);
            MaxX = tMax * v0 * Math.cos(theta);
        }
        return MaxX;
    }


    // cal the max height it can reach
    private double calMaxH() {
        double MaxH;
        if (b!= 0) {
            double C = v0* Math.sin(thetaSet) + m* g/b;
            MaxH = -m /b * C * (m*g/b/C - 1) + m*m*g/b/b * Math.log(m*g/b/C) + h;
        } else {
          double tApproximate = v0*Math.sin(thetaSet)/g;
          MaxH = - 0.5 * g * tApproximate * tApproximate + v0*Math.sin(thetaSet)*tApproximate;
        }
        return MaxH;
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
        } else if (source == detailBtn) {
            if (detailBtn.getLabel() == "Hide Details") {
                detailSwitch = 0;
                detailBtn.setLabel("Show Details");
            } else if (detailBtn.getLabel() == "Show Details") {
                detailSwitch = 1;
                detailBtn.setLabel("Hide Details");
            }
            repaint();
        }
    }

    @Override
    public void run() {
        try{
            v = Double.parseDouble(velocityI.getText());
            m = Double.parseDouble(massI.getText());
            g = Double.parseDouble(gravityI.getText());
            thetaSet = Math.PI * Integer.parseInt(thetaI.getText()) / 180;
            b = Double.parseDouble(bI.getText());

            theta = thetaSet;
            v0 = v;
            vx = v * Math.cos(theta);
            vy = v * Math.sin(theta);
            if (m <= 0||v < 0 || g <= 0 || thetaSet >= 90 || b < 0 || b > 10 || m*g/b < 0.01) {
                throw new NumberFormatException();
            }

            // get scale numbers
            double xM = calMaxX();
            MAX_X = scaleSet(xM);
            XScaleFactor = (double) X_SCALE_WIDTH/MAX_X;
            double yM = calMaxH();
            MAX_Y = scaleSet(yM);
            YScaleFactor = (double) Y_SCALE_HEIGHT/MAX_Y;

            // calculate terminal V
            if (b != 0)
            terminalV = m * g/ b;

            timer.start();



            addOutputBtn();
            removeInput();

        } catch (NumberFormatException e) {
            if(this.isAncestorOf(errorLabel)) {
                this.remove(errorLabel);
                this.updateUI();
            }

            errort = 0;
            errorMassage.start();
        }


    }

    @Override
    public void runTime() {
        double k = 0.01;
        t += k;

        // linear model
        double f = b * v;
        double fa = f/m;


        ax = - Math.cos(theta) * fa;
        ay = - Math.sin(theta) * fa - g;
        vx += ax*k;
        if (vx <0) {
            vx =0;
        }
        if(vy <0 && vy + ay*k >0) {
            vy = -m *g/b;
        } else {
            vy += ay * k;
        }

        v = Math.sqrt(vx* vx + vy*vy);
        if (theta <= -1.56)
            theta = -1.56;
        else
            theta = Math.asin(vy/v);


        x_r += vx * k;
        y_r += vy * k;

        if (y_r < 0) {
            y_r = 0;
        }

        x = LEFT + (int) Math.round(XScaleFactor *x_r);// set x
        y = HEIGHT - tableHeight - (int) Math.round(YScaleFactor*y_r) - objectHeight/2;// set y

        repaint();
        // stop if touching ground
        if (y_r <= 0 && vy < 0) {
            stop();
        }

    }

    // add pause and restart
    public void addOutputBtn() {
        this.add(pauseB);
        this.add(restartB);
    }

    // remove the input label,unit and text area
    public void removeInput() {
        this.remove(massI);
        this.remove(massUnit);
        this.remove(massLabel);

        this.remove(velocityI);
        this.remove(velocityUnit);
        this.remove(velocityLabel);

        this.remove(thetaI);
        this.remove(thetaUnit);
        this.remove(thetaLabel);

        this.remove(gravityI);
        this.remove(gravityLabel);
        this.remove(gravityUnit);

        this.remove(bI);
        this.remove(bLabel);
        this.remove(bEq1);
        this.remove(bEq2);

        this.remove(runB);

    }

    @Override
    public void restart() {
        remove(resumeB);
        remove(pauseB);
        remove(restartB);
        timer.stop();

        t = 0.0;

        x_r = y_r = 0.0;
        x = LEFT;
        y = HEIGHT - tableHeight - objectHeight/2;

        v = 30;
        vx = vy = 0;
        ax = ay = 0.0;
        thetaSet = Math.PI/6;

        repaint();
        addInput();
        this.add(runB);
    }

    // helper function of restart (aim to conserve the input from the last turn)
    private void addInput() {
        this.add(massI);
        this.add(massUnit);
        this.add(massLabel);

        this.add(velocityI);
        this.add(velocityUnit);
        this.add(velocityLabel);

        this.add(thetaI);
        this.add(thetaUnit);
        this.add(thetaLabel);

        this.add(gravityI);
        this.add(gravityLabel);
        this.add(gravityUnit);

        this.add(bI);
        this.add(bLabel);
        this.add(bEq1);
        this.add(bEq2);

        this.add(runB);
    }


    public void stop() {
        timer.stop();
        remove(pauseB);

    }

}
