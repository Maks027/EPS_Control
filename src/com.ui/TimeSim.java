import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.EnumSet;

enum SolarState{
    SHADOWED,
    SUNNY
}

public class TimeSim {
    mainForm mf;

    SolarState currentSolarState = SolarState.SUNNY;

    private JPanel mainTimePanel;
    private JTabbedPane tabbedPane1;
    private JPanel statePanel;
    private JPanel timeStatePanel;
    private JPanel paramPreviewPanel;
    private JTextField textField_currentTimeDate;
    private JPanel timeContrPanel;
    private JButton button_fastBackward;
    private JButton button_StartPause;
    private JButton button_fastForward;
    private JTextField textField_currentTimeHour;
    private JButton button_stopTimer;
    private JLabel multiplierLabel;
    private JTextField textBox_orbPeriod;
    private JTextField textBox_orbVelocity;
    private JTextField textBox_orbHeight;
    private JTextField textBox_orbShadow;
    private JLabel stateLabel;
    private JTextField textField_revolutions;
    private JTextField textField_batCapacity;
    private JTextField textField_currentConsumption;
    private JTextField textField_batVoltage;
    private JProgressBar progressBar_soc;
    private JButton chargeButton;
    private JButton dischargeButton;
    private JLabel batStateLabel;
    private JTextField textField_soc;

    private Timer timer;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    DateTimeFormatter elapsedTimeFormatter;
    Battery battery;

    private long currentTimerCnt = 0;
    private long batCnt = 0;
    private int cntMult = 1;
    private int pressCnt = 0;
    private long revolutionsCnt = 0;
    private int timerState = 0;

    private final BigInteger R = new BigInteger("6371000"); //Earth radius
    private final BigDecimal G = new BigDecimal("0.0000000000667259"); // Gravitational constant
    private final BigInteger M = new BigInteger("5973600000000000000000000"); //Earth mass
    private final BigInteger GM = new BigInteger("398600000000000"); // G*M

    private final long earthRadius = 6371000;
    private long orbitHeight;
    private double orbitVelocity;
    private long orbitPeriod;
    private long orbitRadius;
    private double shadowPercent;
    private long angle;

    enum ChargeState {
        CHARGING,
        DISCHARGING
    }

    ChargeState chargeState = ChargeState.CHARGING;
    ChargeState lastChargeState = ChargeState.DISCHARGING;
    double batteryCapacity;

    public TimeSim() {

        battery = new Battery(2800, 1000);

        progressBar_soc.setMinimum(0);
        progressBar_soc.setMaximum((int)battery.getBatteryCapacity());

        timer = new Timer(1000, event -> timerAction());
        cntMult = 1;
        timer.setInitialDelay(100);

        textBox_orbHeight.setText("200");
        textField_currentConsumption.setText("500");
        textField_batCapacity.setText(String.valueOf(battery.getBatteryCapacity()));
        calcOrbHeight();

        button_fastBackward.addActionListener(e -> FastBackwardAction());
        button_fastForward.addActionListener(e -> FastForwardAction());
        button_StartPause.addActionListener(e -> StartPauseAction());
        button_stopTimer.addActionListener(e -> StopAction());


        textBox_orbPeriod.addActionListener(e -> calcOrbPeriod());
        textBox_orbVelocity.addActionListener(e -> calcOrbVelocity());
        textBox_orbHeight.addActionListener(e -> calcOrbHeight());

        textField_batCapacity.addActionListener(e -> {
            battery.setBatteryCapacity(Double.valueOf(textField_batCapacity.getText()));
            System.out.println(battery.getBatteryCapacity());
            progressBar_soc.setMaximum((int)battery.getBatteryCapacity());
        });

        chargeButton.addActionListener(e -> {
            chargeState = ChargeState.CHARGING;
            batStateLabel.setForeground(Color.GREEN);
            batStateLabel.setText("CHARGING");
        });
        dischargeButton.addActionListener(e -> {
            chargeState = ChargeState.DISCHARGING;
            batStateLabel.setForeground(Color.RED);
            batStateLabel.setText("DISCHARGING");
        });
    }

    public void calcOrbHeight(){
        orbitHeight = Long.valueOf(textBox_orbHeight.getText()) * 1000; //meters
        orbitRadius = orbitHeight + earthRadius;

        orbitVelocity = GM.divide(BigInteger.valueOf(orbitRadius)).sqrt().doubleValue(); //V = sqrt(G * M / R) (m/s)
        textBox_orbVelocity.setText(String.valueOf(orbitVelocity / 1000)); //km/s

        orbitPeriod = (long)(2 * Math.PI * (R.doubleValue() + orbitHeight) / orbitVelocity); // T = 2 * pi() * Rorb / V
        textBox_orbPeriod.setText((String.valueOf(orbitPeriod / 60))); //min

        calcShadowPercent();

    }
    public void calcOrbPeriod(){

        orbitPeriod = (long)(Double.valueOf(textBox_orbPeriod.getText()) * 60); //seconds

        orbitRadius = new CubicRoot().root(3, new BigDecimal(GM.multiply(BigInteger.valueOf(orbitPeriod * orbitPeriod)))
                .divide(BigDecimal.valueOf(39.4784), 2, RoundingMode.HALF_DOWN)).longValue();
        // R = (T^2 * G * M / 4 * pi()^2)^(1 / 3)

        orbitHeight = orbitRadius - earthRadius;

        textBox_orbHeight.setText(String.valueOf(orbitHeight / 1000));

        orbitVelocity = GM.divide(R.add(BigInteger.valueOf(orbitHeight))).sqrt().doubleValue();
        // V = sqrt(G * M / R)

        textBox_orbVelocity.setText(String.valueOf(orbitVelocity / 1000));

        calcShadowPercent();

    }
    public  void calcOrbVelocity(){

        orbitVelocity = Double.valueOf(textBox_orbVelocity.getText()) * 1000;

        orbitRadius = GM.divide(BigInteger.valueOf((long)(orbitVelocity * orbitVelocity))).longValue();
        // Rorb = (G * M) / (V^2)
        orbitHeight = orbitRadius - earthRadius;
        // Horb = Rorb - Rearth
        textBox_orbHeight.setText(String.valueOf(orbitHeight / 1000));

        orbitPeriod = (long)(6.28 * orbitRadius / orbitVelocity);
        // T = 2 * pi() * R / V
        textBox_orbPeriod.setText(String.valueOf(orbitPeriod / 60));

        calcShadowPercent();
    }
    private void calcShadowPercent(){
        angle = 2 * (long) Math.toDegrees(Math.asin((double) earthRadius / orbitRadius));
        shadowPercent = (double) angle / 360;
        textBox_orbShadow.setText(String.valueOf((long)(shadowPercent * 100)));
    }
    public void FastForwardAction() {
        if (++pressCnt >= 7)
            pressCnt = 7;

        SetMultiplier(pressCnt);
    }
    public void FastBackwardAction() {
        if (--pressCnt <= 0)
            pressCnt = 0;

        SetMultiplier(pressCnt);
    }
    public void StartPauseAction() {
        if (timerState == 0){
            timer.start();
        } else {
            timer.stop();
        }
        timerState ^= 1;
    }
    public void StopAction() {
        timer.stop();
        revolutionsCnt = 0;
        currentTimerCnt = 0;
        timerState = 0;
        textField_revolutions.setText(String.valueOf(revolutionsCnt));
        displayMsAsTime(currentTimerCnt);
    }
    public void SetMultiplier(int multiplier) {
        switch (multiplier){
            case 0:
                timer.setDelay(1000);
                cntMult = 1;
                multiplierLabel.setText("X1");
                break;
            case 1:
                timer.setDelay(100);
                cntMult = 1;
                multiplierLabel.setText("X10");
                break;
            case 2:
                timer.setDelay(10);
                cntMult = 1;
                multiplierLabel.setText("X100");
                break;
            case 3:
                timer.setDelay(10);
                cntMult = 10;
                multiplierLabel.setText("X1000");
                break;
            case 4:
                timer.setDelay(10);
                cntMult = 20;
                multiplierLabel.setText("X2000");
                break;

            case 5:
                timer.setDelay(10);
                cntMult = 50;
                multiplierLabel.setText("X5000");
                break;

            case 6:
                timer.setDelay(10);
                cntMult = 100;
                multiplierLabel.setText("X10000");
                break;
            case 7:
                timer.setDelay(10);
                cntMult = 1000;
                multiplierLabel.setText("X100000");
                break;
        }
    }

    public void timerAction(){
        currentTimerCnt += cntMult;
        displayMsAsTime(currentTimerCnt);

        if ((currentTimerCnt % orbitPeriod) <= (orbitPeriod * (1 - shadowPercent))){
            if (currentSolarState == SolarState.SHADOWED){
                currentSolarState = SolarState.SUNNY;
                revolutionsCnt++;
                textField_revolutions.setText(String.valueOf(revolutionsCnt));
                stateLabel.setForeground(Color.YELLOW);
                stateLabel.setText("Sunny");
            }
        } else {
            if (currentSolarState == SolarState.SUNNY){
                currentSolarState = SolarState.SHADOWED;
                stateLabel.setForeground(Color.BLACK);
                stateLabel.setText("Shadow");
            }
        }

        if (chargeState == ChargeState.CHARGING){
            battery.BatteryCharge(cntMult, Double.valueOf(textField_currentConsumption.getText()));
        } else if (chargeState == ChargeState.DISCHARGING){
            battery.BatteryDischarge(cntMult, Double.valueOf(textField_currentConsumption.getText()));
        }

        textField_batVoltage.setText(String.valueOf(battery.getBatteryVoltage()));


        long SOC = (long)(battery.getStateOfCharge());
        textField_soc.setText(String.valueOf(SOC));



//        progressBar_soc.setValue((int) map(SOC, 0 , (long)battery.getBatteryCapacity(), 0, progressBar_soc.getMaximum()));

        progressBar_soc.setValue((int)SOC);

    }

    long map(long x, long in_min, long in_max, long out_min, long out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public void displayMsAsTime(long ms){
        LocalDateTime time = LocalDateTime.ofEpochSecond(ms, 0, ZoneOffset.UTC);
        textField_currentTimeDate.setText(String.valueOf(time.getDayOfYear() - 1));
        textField_currentTimeHour.setText(timeFormatter.format(time));
    }

    public JPanel getMainTimePanel() {
        return mainTimePanel;
    }

    public void setMainTimePanel(JPanel mainTimePanel) {
        this.mainTimePanel = mainTimePanel;
    }

    public static void main(String[] args) {

        JFrame jFrame = new JFrame("D/N");
        jFrame.setSize(800, 400);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setContentPane(new TimeSim().getMainTimePanel());

        jFrame.setVisible(true);
    }

}
