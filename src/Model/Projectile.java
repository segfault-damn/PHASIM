package Model;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


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

    public Projectile() {
        super();
        setInput();

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

        g2d.setColor(new Color(14, 10, 10,150));

        if(timer.isRunning() || t > 0) {
            drawScaleX(g2d, MAX_X);
            drawScaleY(g2d, MAX_Y);

            drawForces(g2d);
        }
    }

    @Override
    public void drawForces(Graphics2D g2d) {

    }

    public void drawScaleX(Graphics2D g2d,int X_scale) {
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

    //TODO
    // cal the max distance from left
    // x = -m/bC(e^(-bt/m)-1)
    // C = v0 cos theta
    // y = -m/bC(e^(-bt/m)-1) - mgt/b
    // C = v0 sin theta + mg/b
    // h = m/bC(e^(-bt/m)-1) + mgt/b
    private double calMaxX() {
       // -h  = - 0.5 * g * t ^2 + v*Math.sin(theta)*t;
        double MaxX;
        if (b!= 0) {
            MaxX = m / b * v0 * Math.cos(theta);
        } else {
           // -h = - 0.5 * g * t* t + v0 * sin(theta) * t;
            double tMax = (-v0 * Math.sin(theta) - Math.sqrt(v0 * Math.sin(theta) * v0 * Math.sin(theta) + 2 * g * h))/(-g);
            MaxX = tMax * v0 * Math.cos(theta);
        }
        return MaxX;
    }

    //TODO
    // cal the max height it can reach
    private double calMaxH() {
        double MaxH;
        if (b!= 0) {
            double C = v0* Math.sin(thetaSet) + m* g/b;
            MaxH = -m /b * C * (m*g/b/C - 1) + m*m*g/b/b * Math.log(m*g/b/C) + h;
        } else {
          double tApproximate = v*Math.sin(thetaSet)/g;
          MaxH = - 0.5 * g * tApproximate * tApproximate + v*Math.sin(theta)*tApproximate;
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
            System.out.println(m*g/b);
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


        x = LEFT + (int) Math.round(XScaleFactor *x_r);// set x
        y = HEIGHT - tableHeight - (int) Math.round(YScaleFactor*y_r);// set y

        repaint();


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
        setInput();
        this.add(runB);
    }
}
