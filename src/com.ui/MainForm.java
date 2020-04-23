import com.fazecast.jSerialComm.SerialPort;
import org.w3c.dom.Document;


import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;


public class MainForm {

    List<Parameter> list;
    DefaultParameters defaultParameters;
    Map<P_ID, Parameter> parameterMap;

    public Map<Parameter, JTextField> fieldMap;
    Map<Parameter, JCheckBox> checkBoxMap;

    Storage storage;


    private JButton ScanButton, connectButton, closeButton;
    private JTextArea textArea_terminal;
    private JLabel statusLabel;
    private JComboBox comboBox_selectCom;

    private JPanel      panel1, parametersPanel, statusPanel1, statusPanel2, statusPanel3, portPanel, panel_COM,
                        statusPanel4, statusPanel5, panel_conditions, panel_inputCond, panel_outCond;

    private JCheckBox   checkBox_fastCharge, checkBox_3V3powerGood, checkBox_3V3LUP, checkBox_5VLUP,
	                    checkBox_GPO1, checkBox_GPO3, checkBox_GPO4, checkBox_GPO5, checkBox_GPO6,
                        checkBox_5VpowerGood, checkBox_resetSelfLock, checkBox_batStatCompl, checkBox_batStatCharging,
                        checkBox_outSelfLock, checkBox_VBATEN, checkBox_BCREN, checkBox_shd3V3, checkBox_shd5V,
                        checkBox_SA1, checkBox_SA2, checkBox_SA3, checkBox_fastSlowCharge, checkBox_shdCharge,
                        checkBox_OUT1, checkBox_OUT2, checkBox_OUT3, checkBox_OUT4, checkBox_OUT5, checkBox_OUT6;

    private JTextField 	baudRate, textField_vbat, textField_measTemp1, textField_minTemp1, textField_maxTemp1, 
						textField_XVoltage, textField_outCond, textField_inputCond, textField_ibat, textField_minTemp2,
						textField_measTemp2, textField_maxTemp2, textField_minTemp3, textField_measTemp3, textField_maxTemp3, 
						textField_minTemp4, textField_measTemp4, textField_maxTemp4, textField_XCurrentPos, 
						textField_XCurrentNeg, textField_ZCurrentNeg, textField_YCurrentNeg, textField_YCurrentPos, 
						textField_ZCurrentPos, textField_YVoltage, textField_ZVoltage, textField_3V3Current, textField__5VCurrent, 
						textField_BCRVoltage, textField_BCRCurrent, textField_underVoltage, textField_shortCircuit, 
						textField_overTemp, textField_tempSens5, textField_tempSens6, textField_tempSens7, textField_tempSens8, 
						textField_tempMCU, textField_onCycles, textField_chargeCycles, textField_swVersion;
    private JButton newWindow;

    MessageSend messageSend;
    SerialPort port;

    public MainForm() {
        storage = new Storage();
        defaultParameters = new DefaultParameters();

        parameterMap = defaultParameters.getParameterMap();

        fieldMap = new HashMap<>();
        checkBoxMap = new HashMap<>();

        fillMaps();
        addAllListeners();

        File file = new File("F:\\parameters.xml");

        Document doc = storage.createDocument();

        storage.ParseFile(file, parameterMap);

        int PowerOnCount = (int)parameterMap.get(P_ID.POWER_ON_CYCLES).getDoubleValue();

        parameterMap.get(P_ID.POWER_ON_CYCLES).setDoubleValue(++PowerOnCount);

        System.out.println(parameterMap.get(P_ID.POWER_ON_CYCLES).getDoubleValue());

        storage.mapToFile(parameterMap, file);

        textField_onCycles.setText(String.valueOf(PowerOnCount));

 //       System.out.println("vbat " + parameterMap.get(P_ID.BATTERY_VOLTAGE).getDoubleValue());

        ScanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SerialPort[] ports = SerialPort.getCommPorts();
                comboBox_selectCom.removeAllItems();
                for (int i = 0 ; i < ports.length ; i++){
                    comboBox_selectCom.addItem(ports[i].getSystemPortName());

                }
                    //textArea1.append(ports[i].getSystemPortName() + "\n");
            }
        });

        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                port = SerialPort.getCommPort(String.valueOf(comboBox_selectCom.getSelectedItem()));

                port.openPort();
                port.setComPortParameters(
                            115200,
                            8,
                            SerialPort.ONE_STOP_BIT,
                            SerialPort.NO_PARITY);
                port.setBaudRate(parseInt(baudRate.getText()));
                textArea_terminal.append("Port " + port.getSystemPortName() + " opened\n");
                textArea_terminal.append("Baud Rate: " + port.getBaudRate());
                statusLabel.setText("Connected to " + port.getSystemPortName());
                statusLabel.setForeground(Color.green);

                messageSend = new MessageSend(port);
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                port.closePort();
                if (!port.isOpen()){
                    textArea_terminal.append("Port closed\n");
                    statusLabel.setText("Disconnected");
                    statusLabel.setForeground(Color.red);

                }
            }
        });

        newWindow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("D/N");
                frame.setSize(600, 400);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setContentPane(new TimeSim().getMainTimePanel());
                frame.setVisible(true);
            }
        });
    }

    public void fillMaps(){
        fieldMap.put(parameterMap.get(P_ID.BATTERY_VOLTAGE), textField_vbat);
        fieldMap.put(parameterMap.get(P_ID.BATTERY_CURRENT), textField_ibat);
        fieldMap.put(parameterMap.get(P_ID.BCR_VOLTAGE), textField_BCRVoltage);
        fieldMap.put(parameterMap.get(P_ID.BCR_CURRENT), textField_BCRCurrent);
        fieldMap.put(parameterMap.get(P_ID.X_VOLTAGE), textField_XVoltage);
        fieldMap.put(parameterMap.get(P_ID.X_POS_CURRENT), textField_XCurrentPos);
        fieldMap.put(parameterMap.get(P_ID.X_NEG_CURRENT), textField_XCurrentNeg);
        fieldMap.put(parameterMap.get(P_ID.Y_VOLTAGE), textField_YVoltage);
        fieldMap.put(parameterMap.get(P_ID.Y_POS_CURRENT), textField_YCurrentPos);
        fieldMap.put(parameterMap.get(P_ID.Y_NEG_CURRENT), textField_YCurrentNeg);
        fieldMap.put(parameterMap.get(P_ID.Z_VOLTAGE), textField_ZVoltage);
        fieldMap.put(parameterMap.get(P_ID.Z_POS_CURRENT), textField_ZCurrentPos);
        fieldMap.put(parameterMap.get(P_ID.Z_NEG_CURRENT), textField_ZCurrentNeg);
        fieldMap.put(parameterMap.get(P_ID.CURRENT_3V3), textField_3V3Current);
        fieldMap.put(parameterMap.get(P_ID.CURRENT_5V), textField__5VCurrent);

        fieldMap.put(parameterMap.get(P_ID.MCU_TEMP), textField_tempMCU);
        fieldMap.put(parameterMap.get(P_ID.BAT_TEMP_SENS1), textField_measTemp1);
        fieldMap.put(parameterMap.get(P_ID.BAT_TEMP_SENS2), textField_measTemp2);
        fieldMap.put(parameterMap.get(P_ID.BAT_TEMP_SENS3), textField_measTemp3);
        fieldMap.put(parameterMap.get(P_ID.BAT_TEMP_SENS4), textField_measTemp4);
        fieldMap.put(parameterMap.get(P_ID.INPUTS_COND), textField_inputCond);
        fieldMap.put(parameterMap.get(P_ID.OUTPUTS_COND), textField_outCond);
        fieldMap.put(parameterMap.get(P_ID.POWER_ON_CYCLES), textField_onCycles);
        fieldMap.put(parameterMap.get(P_ID.BAT_UNDER_VOLTAGE), textField_underVoltage);
        fieldMap.put(parameterMap.get(P_ID.BAT_SHORT_CIRCUIT), textField_shortCircuit);
        fieldMap.put(parameterMap.get(P_ID.BAT_OVER_TEMP), textField_overTemp);
        fieldMap.put(parameterMap.get(P_ID.MAX_TEMP1), textField_maxTemp1);
        fieldMap.put(parameterMap.get(P_ID.MAX_TEMP2), textField_maxTemp2);
        fieldMap.put(parameterMap.get(P_ID.MAX_TEMP3), textField_maxTemp3);
        fieldMap.put(parameterMap.get(P_ID.MAX_TEMP4), textField_maxTemp4);
        fieldMap.put(parameterMap.get(P_ID.MIN_TEMP1), textField_minTemp1);
        fieldMap.put(parameterMap.get(P_ID.MIN_TEMP2), textField_minTemp2);
        fieldMap.put(parameterMap.get(P_ID.MIN_TEMP3), textField_minTemp3);
        fieldMap.put(parameterMap.get(P_ID.MIN_TEMP4), textField_minTemp4);
        fieldMap.put(parameterMap.get(P_ID.TEMP_SENS5), textField_tempSens5);
        fieldMap.put(parameterMap.get(P_ID.TEMP_SENS6), textField_tempSens6);
        fieldMap.put(parameterMap.get(P_ID.TEMP_SENS7), textField_tempSens7);
        fieldMap.put(parameterMap.get(P_ID.TEMP_SENS8), textField_tempSens8);
        fieldMap.put(parameterMap.get(P_ID.SW_VERSION), textField_swVersion);
        fieldMap.put(parameterMap.get(P_ID.CHARGE_CYCLES), textField_chargeCycles);

        checkBoxMap.put(parameterMap.get(P_ID.LUP_3V3), checkBox_3V3LUP);
        checkBoxMap.put(parameterMap.get(P_ID.LUP_5V), checkBox_5VLUP);
        checkBoxMap.put(parameterMap.get(P_ID.BAT_FAST_CH), checkBox_fastCharge);
        checkBoxMap.put(parameterMap.get(P_ID.V_OUT_1_DEF), checkBox_GPO1);
        checkBoxMap.put(parameterMap.get(P_ID.V_OUT_3_DEF), checkBox_GPO3);
        checkBoxMap.put(parameterMap.get(P_ID.V_OUT_4_DEF), checkBox_GPO4);
        checkBoxMap.put(parameterMap.get(P_ID.V_OUT_5_DEF), checkBox_GPO5);
        checkBoxMap.put(parameterMap.get(P_ID.V_OUT_6_DEF), checkBox_GPO6);
    }

    public void addTextChangeListener(P_ID id){
        Parameter parameter = parameterMap.get(id);
        fieldMap.get(parameter).addActionListener(e -> textChangeAction(id));

    }

    public void addCheckBoxChangeListener(P_ID id){
        Parameter parameter = parameterMap.get(id);
        checkBoxMap.get(parameter).addActionListener(e -> textChangeAction(id));
    }


    public void addAllListeners(){
        fieldMap.forEach((k, v) -> addTextChangeListener(k.getId()));
        checkBoxMap.forEach((k, v) -> addCheckBoxChangeListener(k.getId()));
    }

    public void textChangeAction(P_ID id){
        Parameter parameter = parameterMap.get(id);
        parameter.setDoubleValue(fieldMap.get(parameter).getText());

        try{
            messageSend.sendMessage(parameter.getAddress(), parameter.getAdcValue());
        } catch (Exception e) {
            textArea_terminal.append("No connection!\n");
        }

        System.out.println(
                        "Changed parameter: " + parameter.getId().name() +
                        "; Double value: " + parameter.getDoubleValue() +
                        "; ADC Value: " + parameter.getAdcValue()
        );
    }

    public void checkBoxChangeAction(P_ID id, JCheckBox box){
//        defaultParameters.setValById(id, (box.isSelected() == true ? 1 : 0));
//        messageSend.sendMessage(defaultParameters.getAddrById(id),defaultParameters.getADCValById(id));
    }


    public static void main(String[] args) throws ParserConfigurationException {
        
        JFrame frame = new JFrame("EPS Simulator");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MainForm().panel1);
        frame.setVisible(true);
    }



}
