import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;


public class TimeSim {
    MainForm mainForm;
    private SolarState currentSolarState = SolarState.SUNNY;
    private JTabbedPane tabbedPane1;
    private JPanel statePanel;
    private JPanel timeContrPanel;

    private JPanel mainTimePanel;
    private JTextField textField_currentTimeDate;
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
    private JTextField textField_soc;
    private JTextField textField_5VConsPower;
    private JTextField textField_3V3ConsPower;
    private JTextField textField_5VConsCurrent;
    private JTextField textField_3V3ConsCurrent;
    private JTextField textField_GenVoltage;
    private JTextField textField_GenCurrent;
    private JTextField textField_GenPower;
    private JTextField textField_BCREff;
    private JTextField textField_5VEff;
    private JTextField textField_3V3Eff;
    private JTextField textField_RAWConsPower;
    private JTextField textField_RAWConsCurrent;
    private JTextField textField_TotalConsPower;
    private JTextField textField_TotalConsCurrent;

    private long currentTimerCnt = 0;
    private int cntMult = 1;
    private int pressCnt = 0;
    private long revolutionsCnt = 0;
    private int timerState = 0;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private Timer timer;
    private Battery battery;
    private Orbit orbit;
    private Bus bus5V;
    private Bus bus3V3;
    private Bus busRaw;
    public Panel panelX;
    public Panel panelY;
    public Panel panelZ;


    enum ChargeState {
        CHARGING,
        DISCHARGING
    }
    enum BusState {
        ON,
        OFF
    }
    enum SolarState{
        SHADOWED,
        SUNNY
    }


    private ChargeState chargeState = ChargeState.CHARGING;

    private double generatedVoltage;
    private double totalGeneratedPower;
    private double totalGeneratedCurrent;
//    private double tempTotalGeneratedPower;
    private double bcrEfficiency;
    private BusState busStateBCR = BusState.ON;

    private Storage storage;
    private Document document;

    private double batteryCurrent;

    private double totalRequiredPower;
    private double totalConsumedPower;
    private double totalConsumedCurrent;


    public TimeSim() {

        initParameters();

        button_fastBackward.addActionListener(e -> FastBackwardAction());
        button_fastForward.addActionListener(e -> FastForwardAction());
        button_StartPause.addActionListener(e -> StartPauseAction());
        button_stopTimer.addActionListener(e -> StopAction());


        textBox_orbPeriod.addActionListener(e -> {
            orbit.calcOrbitVelocityAndHeight(Long.valueOf(textBox_orbPeriod.getText()));
            displayOrbitParameters();
        });
        textBox_orbVelocity.addActionListener(e -> {
            orbit.calcOrbitPeriodAndHeight(Double.valueOf(textBox_orbVelocity.getText()));
            displayOrbitParameters();
        });
        textBox_orbHeight.addActionListener(e -> {
            orbit.calcOrbitPeriodAndVelocity(Long.valueOf(textBox_orbHeight.getText()));
            displayOrbitParameters();
        });

        textField_batCapacity.addActionListener(e -> {
            battery.setBatteryCapacity(Double.valueOf(textField_batCapacity.getText()));
            System.out.println(battery.getBatteryCapacity());
            progressBar_soc.setMaximum((int)battery.getBatteryCapacity());
        });


//        textField_GenCurrent.addActionListener(e -> {
//            totalGeneratedCurrent = Double.valueOf(textField_GenCurrent.getText());
//            calcGenVoltAndPow();
//        });

        textField_5VConsPower.addActionListener(e -> {
            bus5V.setRequiredPower();
            calcTotalPower();
        });
        textField_5VConsCurrent.addActionListener(e -> {
            bus5V.setRequiredPower();
            calcTotalPower();
        });
        textField_3V3ConsPower.addActionListener(e -> {
            bus3V3.setRequiredPower();
            calcTotalPower();
        });
        textField_3V3ConsCurrent.addActionListener(e -> {
            bus3V3.setRequiredCurrent();
            calcTotalPower();
        });

        textField_RAWConsPower.addActionListener(e -> {
            busRaw.setRequiredPower(battery.getBatteryVoltage());
            calcTotalPower();
        });

        textField_3V3Eff.addActionListener(e ->  bus3V3.setEfficiency(Double.valueOf(textField_3V3Eff.getText())));
        textField_5VEff.addActionListener(e -> bus5V.setEfficiency(Double.valueOf(textField_5VEff.getText())));
        textField_GenPower.addActionListener(e ->{
            totalGeneratedPower = Double.valueOf(textField_GenPower.getText());
            setSolarPanelsPower();
        });
        textField_BCREff.addActionListener(e -> bcrEfficiency = Double.valueOf(textField_BCREff.getText()));
    }
    private void initParameters(){

        timer = new Timer(1000, event -> timerAction());
        cntMult = 1;
        timer.setInitialDelay(100);

        battery = new Battery(2800, 1000);
        orbit = new Orbit(89);
        displayOrbitParameters();

        progressBar_soc.setMinimum(0);
        progressBar_soc.setMaximum((int)battery.getBatteryCapacity());

        textField_currentConsumption.setText("500");
        textField_batCapacity.setText(String.valueOf(battery.getBatteryCapacity()));


        bus5V = new Bus(0.2, 5, 85, textField_5VConsPower, textField_5VConsCurrent);
        bus3V3 = new Bus(0.4, 3.3, 76, textField_3V3ConsPower, textField_3V3ConsCurrent);
        busRaw = new Bus(0.1, battery.getBatteryVoltage(), 100, textField_RAWConsPower, textField_RAWConsCurrent);
        allBusesON();
        calcTotalPower();
        textField_5VEff.setText(String.valueOf(bus5V.getEfficiency()));
        textField_3V3Eff.setText(String.valueOf(bus3V3.getEfficiency()));


        bcrEfficiency = 92;
        panelX = new Panel("X", 4.6, bcrEfficiency);
        panelY = new Panel("Y", 4.6, bcrEfficiency);
        panelZ = new Panel("Z", 4.6, bcrEfficiency);
        panelX.startGeneration();
        panelY.startGeneration();
        panelZ.startGeneration();

        generatedVoltage = panelX.getGeneratedVoltage();

        totalGeneratedPower = 2.1;
//        tempTotalGeneratedPower = totalGeneratedPower;

        textField_BCREff.setText(String.format(Locale.ROOT, "%.0f", bcrEfficiency));
        textField_GenPower.setText(String.format(Locale.ROOT, "%.2f", totalGeneratedPower));
        calcGeneratedCurrent();
        setSolarPanelsPower();
  //      panelX.setGeneratedPower();

        stateLabel.setIcon(new ImageIcon("src/main/resources/icons/sun_on.png"));


    }

    private void setSolarPanelsPower(){
        panelX.setGeneratedPower(totalGeneratedPower / 3);
        panelY.setGeneratedPower(totalGeneratedPower / 3);
        panelZ.setGeneratedPower(totalGeneratedPower / 3);
    }

    private void getTotalGeneratedPower(){
        totalGeneratedPower = panelX.getGeneratedPower() + panelY.getGeneratedPower() + panelZ.getGeneratedPower();
        totalGeneratedPower = totalGeneratedPower * bcrEfficiency / 100;
    }

    private void calcGeneratedCurrent(){
        generatedVoltage = this.panelX.getGeneratedVoltage();
        if (generatedVoltage > 0 ){
            totalGeneratedCurrent = totalGeneratedPower / generatedVoltage;
        } else {
            totalGeneratedCurrent = 0;
        }

        textField_GenPower.setText(String.format(Locale.ROOT, "%.2f",totalGeneratedPower));
        textField_GenVoltage.setText(String.format(Locale.ROOT, "%.2f",generatedVoltage));
        textField_GenCurrent.setText(String.format(Locale.ROOT, "%.2f", totalGeneratedCurrent));
    }


    public void getValues(Map<P_ID, Parameter> parameterMap){
        parameterMap.get(P_ID.BATTERY_VOLTAGE).setDoubleValue(this.battery.getBatteryVoltage());
        parameterMap.get(P_ID.BATTERY_CURRENT).setDoubleValue(this.totalConsumedPower);
        parameterMap.get(P_ID.BCR_VOLTAGE).setDoubleValue(this.battery.getBatteryVoltage());
        parameterMap.get(P_ID.BCR_CURRENT).setDoubleValue(this.totalGeneratedCurrent);
        parameterMap.get(P_ID.CURRENT_5V).setDoubleValue(this.bus5V.getConsumedCurrent());
        parameterMap.get(P_ID.CURRENT_3V3).setDoubleValue(this.bus3V3.getConsumedCurrent());

        parameterMap.get(P_ID.X_VOLTAGE).setDoubleValue(this.panelX.getGeneratedVoltage());
        parameterMap.get(P_ID.X_POS_CURRENT).setDoubleValue(this.panelX.getGeneratedPositiveCurrent());
        parameterMap.get(P_ID.X_NEG_CURRENT).setDoubleValue(this.panelX.getGeneratedNegativeCurrent());
        parameterMap.get(P_ID.Y_VOLTAGE).setDoubleValue(this.panelY.getGeneratedVoltage());
        parameterMap.get(P_ID.Y_POS_CURRENT).setDoubleValue(this.panelY.getGeneratedPositiveCurrent());
        parameterMap.get(P_ID.Y_NEG_CURRENT).setDoubleValue(this.panelY.getGeneratedNegativeCurrent());
        parameterMap.get(P_ID.Z_VOLTAGE).setDoubleValue(this.panelZ.getGeneratedVoltage());
        parameterMap.get(P_ID.Z_POS_CURRENT).setDoubleValue(this.panelZ.getGeneratedPositiveCurrent());
        parameterMap.get(P_ID.Z_NEG_CURRENT).setDoubleValue(this.panelZ.getGeneratedNegativeCurrent());
    }

    private void displayOrbitParameters(){
        textBox_orbPeriod.setText(String.valueOf(orbit.getOrbitPeriod() / 60));
        textBox_orbHeight.setText(String.valueOf(orbit.getOrbitHeight() / 1000));
        textBox_orbVelocity.setText(String.valueOf(orbit.getOrbitVelocity() / 1000));
        textBox_orbShadow.setText(String.valueOf((long)(orbit.getShadowPercent() * 100)));
    }


    public void generationON(){
        panelX.startGeneration();
        panelY.startGeneration();
        panelZ.startGeneration();
        calcTotalPower();
        calcGeneratedCurrent();
    }

    public void generationOFF(){
        panelX.stopGeneration();
        panelY.stopGeneration();
        panelZ.stopGeneration();
        calcTotalPower();
        calcGeneratedCurrent();
    }

    private void calcTotalPower(){

        totalConsumedPower =
                bus5V.getConsumedPower() * 100 / bus5V.getEfficiency() +
                        bus3V3.getConsumedPower() * 100 / bus3V3.getEfficiency() +
                        busRaw.getConsumedPower();

        totalRequiredPower =
                bus5V.getRequiredPower() * 100 / bus5V.getEfficiency() +
                bus3V3.getRequiredPower() * 100 / bus3V3.getEfficiency() +
                busRaw.getRequiredPower();

        textField_TotalConsPower.setText(String.format("%.2f", totalRequiredPower));

        if(totalRequiredPower != totalConsumedPower){
            textField_TotalConsPower.setBackground(Color.red);
            textField_TotalConsCurrent.setBackground(Color.red);
        } else {
            textField_TotalConsPower.setBackground(Color.white);
            textField_TotalConsCurrent.setBackground(Color.white);
        }
    }



    private void allBusesON(){
        bus5V.turnON();
        bus3V3.turnON();
        busRaw.turnON();
    }
    private void allBusesOFF(){
        bus5V.turnOFF();
        bus3V3.turnOFF();
        busRaw.turnOFF();
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
            mainForm.getTimer().start();
            timer.start();
        } else {
            timer.stop();
            mainForm.getTimer().stop();
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

        if ((currentTimerCnt % orbit.getOrbitPeriod()) <= (orbit.getOrbitPeriod() * (1 - orbit.getShadowPercent()))){
            if (currentSolarState == SolarState.SHADOWED){
                currentSolarState = SolarState.SUNNY;
                revolutionsCnt++;
                textField_revolutions.setText(String.valueOf(revolutionsCnt));
                stateLabel.setForeground(Color.YELLOW);
                stateLabel.setText("Sunny");
                stateLabel.setIcon(new ImageIcon("src/main/resources/icons/sun_on.png"));
                chargeState = ChargeState.CHARGING;
                generationON();
            }
        } else {
            if (currentSolarState == SolarState.SUNNY){
                currentSolarState = SolarState.SHADOWED;
                stateLabel.setForeground(Color.BLACK);
                stateLabel.setText("Shadow");
                stateLabel.setIcon(new ImageIcon("src/main/resources/icons/sun_off.png"));
                chargeState = ChargeState.DISCHARGING;
                generationOFF();
            }
        }

        getTotalGeneratedPower();
        calcGeneratedCurrent();

        batteryCurrent = (totalGeneratedPower - totalConsumedPower) / battery.getBatteryVoltage() * 1000;

        batteryCurrent = battery.ModifySOC(cntMult, batteryCurrent);
        textField_currentConsumption.setText(String.valueOf((long)batteryCurrent));

        textField_batVoltage.setText(String.valueOf(battery.getBatteryVoltage()));

        long SOC = (long)(battery.getStateOfCharge());
        textField_soc.setText(String.valueOf(SOC));

        busRaw.setRequiredPower(battery.getBatteryVoltage());

        if ((SOC > 0) || (totalRequiredPower < totalGeneratedPower)){
            totalConsumedCurrent =  totalRequiredPower / battery.getBatteryVoltage();
        } else {
            allBusesOFF();
        }

        if ((bus5V.getBusState() == BusState.OFF) || (bus3V3.getBusState()== BusState.OFF) || (busRaw.getBusState() == BusState.OFF)){
            if (SOC > (0.2 * battery.getBatteryCapacity())){
                allBusesON();
            }
        }
        textField_TotalConsCurrent.setText(String.format("%.3f", totalConsumedCurrent));
        calcTotalPower();

        progressBar_soc.setValue((int)SOC);
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
        jFrame.setMinimumSize(new Dimension(1000, 300));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setContentPane(new TimeSim().getMainTimePanel());

        jFrame.setVisible(true);
    }

}
