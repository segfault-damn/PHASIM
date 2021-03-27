package Model;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PendulumPanel  extends PanelAbstract implements ActionListener {
    private static final int WIDTH = MainFrame.WIDTH; //1000
    private static final int HEIGHT = MainFrame.HEIGHT;


    private final static int objectWidth = 50;
    private final static int objectHeight = 50;
    private final static int WALL_HEIGHT = 10;
    private final static int WALL_WIDTH = 200;
    private final static int LOWERPOINT = 500;

    private int SCALE_FACTOR;

    // Labels
    JLabel massLabel = new JLabel("Mass:");
    JLabel massUnit = new JLabel("kg");
    JLabel thetaLabel = new JLabel(String.valueOf('\u03B8')); // theta
    JLabel thetaUnit = new JLabel(String.valueOf('\u00B0')); // degree
    JLabel lengthLabel = new JLabel("Length:");
    JLabel lengthUnit = new JLabel("m");
    JLabel gravityLabel = new JLabel("Gravity:");
    JLabel gravityUnit = new JLabel("m/s^2");

    // Input
    JTextField massI;
    JTextField thetaI;
    JTextField lengthI;
    JTextField gravityI;


    //----------------- private changing data-------------------------------
    // position on pixel dimension
    private int x;
    private int y;


    // timer t
    private double t = 0.0;

    // real position
    private double x_r =0.0;
    private double y_r = 0.0;
    // real velocity
    private double vx = 0.0;
    private double vy = 0.0;
    private double v = 0.0;
    // real acceleration
    private double ar = 0.0;
    private double atheta = 0.0;

    private double l = 1.0; // string length smaller than 500
    private double g = 9.8; // gravitational acceleration
    private double m = 1.0; // mass
    private double thetaSet = (double) 5/180*Math.PI;
    private double theta = thetaSet; // angle between string and vertical line



    private int ErrorIndex = 0; // INDEX TABLE:
    // {0  Input Invalid
    // {1  Input mass < 0
    // {2  Input gravity < 0
    // {3  Input length <= 0
    // {4  Input length > 500
    // {5  Input '\u03B8' > 60
    // {6  Input '\u03B8' < 0
    //----------------------------------------------------------------------


    public PendulumPanel() {
        super();
        setInput();
        x = WIDTH/2;
        y = LOWERPOINT;

        this.add(runB);
    }

    public void setInput() {

        // initialize all input label
        setLabel();
        // initialize textfield
        massI = new JTextField("1.0",4);
        thetaI = new JTextField("5",4);
        lengthI = new JTextField("1.0",4);
        gravityI = new JTextField("9.8",4);

        // add textfield
        this.add(massI);
        this.add(thetaI);
        this.add(lengthI);
        this.add(gravityI);

        // set Font
        massI.setFont(InputFont);
        thetaI.setFont(InputFont);
        lengthI.setFont(InputFont);
        gravityI.setFont(InputFont);

        //set position
        massI.setBounds(180,50,100,50);
        thetaI.setBounds(180,150,100,50);
        lengthI.setBounds(180,250,100,50);
        gravityI.setBounds(180,350,100,50);
    }

    public void setLabel() {

        massLabel.setFont(labelFont);
        massUnit.setFont(labelFont);
        thetaLabel.setFont(labelFont);
        thetaUnit.setFont(labelFont);
        lengthLabel.setFont(labelFont);
        lengthUnit.setFont(labelFont);
        gravityLabel.setFont(labelFont);
        gravityUnit.setFont(labelFont);

        massLabel.setBounds(30,50,300,50);
        massUnit.setBounds(300,50,100,50);

        thetaLabel.setBounds(30,150,300,50);
        thetaUnit.setBounds(300,150,100,50);

        lengthLabel.setBounds(30,250,300,50);
        lengthUnit.setBounds(300,250,100,50);

        gravityLabel.setBounds(30,350,300,50);
        gravityUnit.setBounds(300,350,300,50);


        this.add(massLabel);
        this.add(massUnit);
        this.add(thetaLabel);
        this.add(thetaUnit);
        this.add(lengthLabel);
        this.add(lengthUnit);
        this.add(gravityLabel);
        this.add(gravityUnit);

    }

    // paint animation
    public void paintComponent(Graphics gr) {
        super.paintComponent(gr);
        Graphics2D g2d = (Graphics2D) gr;

        // DRAW THE WALL
        g2d.setColor(new Color(95, 35, 8));
        g2d.setStroke(new BasicStroke(10));
        g2d.drawLine((WIDTH - WALL_WIDTH)/2,WALL_HEIGHT,(WIDTH - WALL_WIDTH)/2+WALL_WIDTH,WALL_HEIGHT);

        // DRAW THE String
        g2d.setColor(new Color(24, 9, 2));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(WIDTH/2,WALL_HEIGHT,x,y);
        g2d.setStroke(new BasicStroke());

        // DRAW OBJECT
        g2d.setColor(new Color(141, 30, 18,200));
        g2d.fillRoundRect(x - objectWidth/2,y - objectHeight/2,objectWidth,objectHeight,50,50);

        // show the running data
        g2d.setColor(new Color(12, 3, 59));
        if(timer.isRunning() || t > 0) {

            // draw outputs
            Font outputFont = new Font("Cambria",Font.ROMAN_BASELINE,40);
            g2d.setFont(outputFont);
            // round the number to 2-decimal
            g2d.drawString("Time: " + (double) Math.round(t*100) / 100 + " s",50,50);

            double velocity = (double) Math.round(Math.sqrt(vx*vx+vy*vy) *100)/100;
            g2d.drawString("v = " + velocity + " m/s",50,120);

            double Tension = (g*Math.cos(theta) + ar)*m;
            double T = (double) Math.round(Tension*100)/100;
            g2d.drawString("T = " + T + " N",50,190);

            double thetaO = (double) Math.round(theta*100/Math.PI * 180)/100;
            g2d.drawString("\u03B8 = " + thetaO + "\u00B0",50,260);

            // draw string length in the middle
            g2d.setColor(new Color(224, 122, 76,100));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(WIDTH/2,WALL_HEIGHT,WIDTH/2,LOWERPOINT);
            g2d.drawString(l + "m",WIDTH/2 - 120,(WALL_HEIGHT + LOWERPOINT)/2);
            g2d.drawLine(WIDTH/2 - 10,LOWERPOINT,WIDTH/2 + 10,LOWERPOINT);

            drawForces(g2d);
            drawEnergy(g2d);
        }
    }

    @Override
    // draw forces
    public void drawForces(Graphics2D g2d) {
        double t = 0.8;
        int legendoffset = 30;

        Font forceFont = new Font("Comic Sans MS",Font.ROMAN_BASELINE,40);
        g2d.setFont(forceFont);

        // draw the gravitational force
        g2d.setColor(new Color(161, 21, 222,220));
        int offset = (int) Math.round(m * t + 80);
        if (offset >= 135)
            offset = 135;
        g2d.drawLine(x,y,x,y + offset);
        g2d.drawLine(x ,y + offset, x - 5 ,y + offset - 20);
        g2d.drawLine(x ,y + offset, x + 5 ,y + offset - 20);
        g2d.drawString("mg",x + legendoffset ,y + offset - legendoffset);

        // draw the tension force
        g2d.setColor(new Color(5, 151, 52));
        double Toffset = offset/m/g*(m* v * v /l + m*g*Math.cos(theta));
        int ToffsetX = - (int) Math.round(Toffset * Math.sin(theta));
        int ToffsetY = (int) Math.round(Toffset * Math.cos(theta));
        g2d.drawLine(x,y,x + ToffsetX,y - ToffsetY);
        int arrow_length = 15;
        // the offset of arrow's degree
        double xoffset = 0.3;
        int left_Arrow_x;
        int left_Arrow_y;
        int right_Arrow_x;
        int right_Arrow_y;

        left_Arrow_x = (int) Math.round(arrow_length * Math.sin(-xoffset + theta));
        left_Arrow_y = (int) Math.round(arrow_length * Math.cos(xoffset - theta));
        right_Arrow_x = (int) Math.round(arrow_length * Math.sin(xoffset + theta));
        right_Arrow_y = (int) Math.round(arrow_length * Math.cos(- xoffset - theta));



        g2d.drawLine(x + ToffsetX,y - ToffsetY,x + ToffsetX + left_Arrow_x,y - ToffsetY+ left_Arrow_y);
        g2d.drawLine(x + ToffsetX,y - ToffsetY,x + ToffsetX + right_Arrow_x,y - ToffsetY + right_Arrow_y);
        g2d.drawString("T",x + ToffsetX  - 20 - legendoffset ,y - ToffsetY + legendoffset);

    }

    //draw energy
    private void drawEnergy(Graphics2D g2d) {
        int MaxBarL = 550;

        // calculate kinetic energy
        double Ek = 0.5 * m * Math.sqrt(vx*vx + vy*vy) * Math.sqrt(vx*vx + vy*vy);
        double EkM = m *g* l * (1 - Math.cos(thetaSet)); // Max Kinetic Energy;
        double EkL = MaxBarL/EkM*Ek; // bar length

        // calculate potential energy
        double Ep =  m * g * y_r;
        double EpM = EkM;
        double EpL = MaxBarL/EpM*Ep; // bar length


        // draw bars
        int left = 150;
        g2d.setFont(new Font(Font.SERIF,Font.BOLD,30));

        // kinetic
        Ek = (double) Math.round(Ek*100)/100;

        g2d.setStroke(new BasicStroke(20));
        g2d.setColor(new Color(92, 25, 199,200));
        if ((int) Math.round(EkL) != 0)
            g2d.drawLine(left,HEIGHT - 150,left + (int) Math.round(EkL),HEIGHT - 150);

        g2d.setColor(new Color(92, 25, 199));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(left-10,HEIGHT - 140,left-10,HEIGHT - 160);

        g2d.drawString("KE",left - 70,HEIGHT - 150 + 10);
        g2d.drawString(Ek + "J",left + MaxBarL + 40,HEIGHT - 150 + 10);

        //potential
        Ep = (double) Math.round(Ep*100)/100;

        g2d.setStroke(new BasicStroke(20));
        g2d.setColor(new Color(232, 127, 36,200));
        if ((int) Math.round(EpL) != 0)
            g2d.drawLine(left,HEIGHT - 120,left + (int) Math.round(EpL),HEIGHT - 120);

        g2d.setColor(new Color(232, 127, 36));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(left-10,HEIGHT - 130,left-10,HEIGHT - 110);
        g2d.drawString("PE",left - 67,HEIGHT - 120 + 10);
        g2d.drawString(Ep + "J",left + MaxBarL + 40,HEIGHT - 120 + 10);

        //total
        double ME = (double) Math.round(EkM*100)/100;
        g2d.setStroke(new BasicStroke(20));
        g2d.setColor(new Color(226, 15, 89,200));
        if (EkM != 0) {
            g2d.drawLine(left, HEIGHT - 90, left + MaxBarL, HEIGHT - 90);
        } else { // g=0;
            g2d.drawLine(left, HEIGHT - 90, left + (int) Math.round(EkL), HEIGHT - 90);
        }

        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(left-10,HEIGHT - 100,left-10,HEIGHT -80);
        g2d.setColor(new Color(226, 15, 89));
        g2d.drawString("ME",left - 70,HEIGHT - 90 + 10);

        g2d.drawString(ME + "J",left + MaxBarL + 40,HEIGHT - 90 + 10);
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
            // {0  Input Invalid
            // {1  Input mass < 0
            // {2  Input gravity < 0
            // {3  Input length <= 0
            // {4  Input length > 500
            // {5  Input '\u03B8' >= 360
            // {6  Input '\u03B8' < 0
            switch (ErrorIndex) {
                case 0:
                    errorM("Input invalid!");
                    break;
                case 1:
                    errorM("Input mass < 0");
                    break;
                case 2:
                    errorM("Input gravity < 0");
                    break;
                case 3:
                    errorM("Input length <= 0");
                    break;
                case 4:
                    errorM("Input length > 500");
                    break;
                case 5:
                    errorM("Input " + '\u03B8' + "> 60");
                    break;
                case 6:
                    errorM("Input " + '\u03B8' + "< 0");
                    break;
            }

        }
    }

    @Override
    public void run() {

        try {

            l = Double.parseDouble(lengthI.getText()); // string length smaller than 500
            g = Double.parseDouble(gravityI.getText()); // gravitational acceleration
            m = Double.parseDouble(massI.getText()); // mass
            thetaSet = (double) Integer.parseInt(thetaI.getText()) / 180 * Math.PI;
            theta = thetaSet;

            // {0  Input Invalid
            // {1  Input mass < 0
            // {2  Input gravity < 0   || l <= 0 || l > 500 || thetaSet >= 360 || thetaSet < 0
            // {3  Input length <= 0
            // {4  Input length > 500
            // {5  Input String.valueOf('\u03B8') > 60
            // {6  Input String.valueOf('\u03B8') < 0

            if (m <= 0) {
                ErrorIndex = 1;
                throw new NumberFormatException();
            } else if (g < 0){
                ErrorIndex = 2;
                throw new NumberFormatException();
            } else if (l <= 0){
                ErrorIndex = 3;
                throw new NumberFormatException();
            } else if (l > 500){
                ErrorIndex = 4;
                throw new NumberFormatException();
            } else if (thetaSet > Math.PI/3){
                ErrorIndex = 5;
                throw new NumberFormatException();
            } else if (thetaSet < 0){
                ErrorIndex = 6;
                throw new NumberFormatException();
            }

            x_r = l * Math.sin(theta);
            y_r = l - l * Math.cos(theta);
            SCALE_FACTOR = (int) Math.round(500 / l);
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



    @Override
    public void runTime() {
        double k = 0.01;
        t += k;

        //update acceleration
        ar =  v * v / l; // always positive
        atheta =  g * Math.sin(theta); // positive(right) negative(left)


        vx +=  k*(- ar * Math.sin(theta) - atheta * Math.cos(theta));
        vy +=  k*(ar * Math.cos(theta) - atheta * Math.sin(theta));

        v = Math.sqrt(vx* vx + vy * vy) * theta/Math.abs(theta);

        // update x, y distance
        x_r += k*vx;
        y_r += k*vy; // always positive
        if (y_r < 0) {
            y_r = 0;
        }

        x = WIDTH/2 + (int) Math.round(SCALE_FACTOR *x_r);// set x
        y = LOWERPOINT - (int) Math.round(SCALE_FACTOR *y_r);// set y

        // update theta
        theta = Math.asin(x_r/l);
        repaint();

        if(vx +  k*(- ar * Math.sin(theta) - atheta * Math.cos(theta)) < 0 && vx > 0) {
            theta = thetaSet;
            vx = 0;
            vy = 0;
            x_r = l*Math.sin(theta);
            y_r = l - l*Math.cos(theta);
        } else if(vx +  k*(- ar * Math.sin(theta) - atheta * Math.cos(theta)) > 0 && vx < 0) {
            theta = -thetaSet;
            vx = 0;
            vy = 0;
            x_r = l*Math.sin(theta);
            y_r = l - l*Math.cos(theta);
        }
    }

    // remove the input label,unit and text area
    public void removeInput() {
        this.remove(massI);
        this.remove(massUnit);
        this.remove(massLabel);

        this.remove(thetaI);
        this.remove(thetaUnit);
        this.remove(thetaLabel);

        this.remove(lengthI);
        this.remove(lengthUnit);
        this.remove(lengthLabel);

        this.remove(gravityI);
        this.remove(gravityLabel);
        this.remove(gravityUnit);

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


        x = WIDTH/2;
        y = LOWERPOINT;
        x_r = y_r = 0.0;

        v = vx = vy = 0;

        ar = atheta =0.0;


        repaint();
        setInput();
        this.add(runB);
    }
}
