import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import static java.lang.Integer.parseInt;


public class mainForm {

    List<Parameter> list;
    DefaultParameters defaultParameters;
    Storage storage;

    private JPanel panel1;
    private JButton ScanButton;
    private JComboBox comboBox_selectCom;
    private JTextArea textArea_terminal;
    private JButton connectButton;
    private JButton closeButton;
    private JLabel statusLabel;
    private JTextField baudRate;
    private JPanel parametersPanel;
    private JPanel statusPanel1;
    private JPanel statusPanel2;
    private JPanel statusPanel3;
    private JTextField textField_vbat;
    private JTextField textField_measTemp1;
    private JTextField textField_minTemp1;
    private JTextField textField_maxTemp1;
    private JPanel portPanel;
    private JTextField textField_XVoltage;
    private JCheckBox checkBox_fastCharge;
    private JTextField textField_outCond;
    private JTextField textField_inputCond;
    private JCheckBox checkBox_3V3powerGood;
    private JTextField textField_ibat;
    private JTextField textField_minTemp2;
    private JTextField textField_measTemp2;
    private JTextField textField_maxTemp2;
    private JTextField textField_minTemp3;
    private JTextField textField_measTemp3;
    private JTextField textField_maxTemp3;
    private JTextField textField_minTemp4;
    private JTextField textField_measTemp4;
    private JTextField textField_maxTemp4;
    private JTextField textField_XCurrentPos;
    private JTextField textField_XCurrentNeg;
    private JTextField textField_ZCurrentNeg;
    private JTextField textField_YCurrentNeg;
    private JTextField textField_YCurrentPos;
    private JTextField textField_ZCurrentPos;
    private JTextField textField_YVoltage;
    private JTextField textField_ZVoltage;
    private JTextField textField_3V3Current;
    private JTextField textField__5VCurrent;
    private JTextField textField_BCRVoltage;
    private JTextField textField_BCRCurrent;
    private JCheckBox checkBox_3V3LUP;
    private JCheckBox checkBox_5VLUP;
    private JCheckBox checkBox_GPO1;
    private JCheckBox checkBox_GPO3;
    private JCheckBox checkBox_GPO4;
    private JCheckBox checkBox_GPO5;
    private JCheckBox checkBox_GPO6;
    private JTextField textField_underVoltage;
    private JTextField textField_shortCircuit;
    private JTextField textField_overTemp;
    private JTextField textField_tempSens5;
    private JTextField textField_tempSens6;
    private JTextField textField_tempSens7;
    private JTextField textField_tempSens8;
    private JTextField textField_tempMCU;
    private JTextField textField_onCycles;
    private JTextField textField_chargeCycles;
    private JTextField textField_swVersion;
    private JCheckBox checkBox_5VpowerGood;
    private JCheckBox checkBox_resetSelfLock;
    private JCheckBox checkBox_batStatCompl;
    private JCheckBox checkBox_batStatCharging;
    private JCheckBox checkBox_outSelfLock;
    private JCheckBox checkBox_VBATEN;
    private JCheckBox checkBox_BCREN;
    private JCheckBox checkBox_shd3V3;
    private JCheckBox checkBox_shd5V;
    private JCheckBox checkBox_SA1;
    private JCheckBox checkBox_SA2;
    private JCheckBox checkBox_SA3;
    private JCheckBox checkBox_fastSlowCharge;
    private JCheckBox checkBox_shdCharge;
    private JCheckBox checkBox_OUT1;
    private JCheckBox checkBox_OUT2;
    private JCheckBox checkBox_OUT3;
    private JCheckBox checkBox_OUT4;
    private JCheckBox OUT5;
    private JCheckBox checkBox_OUT6;
    private JPanel panel_COM;
    private JPanel statusPanel4;
    private JPanel statusPanel5;
    private JPanel panel_conditions;
    private JPanel panel_inputCond;
    private JPanel panel_outCond;
    private JComboBox comboBox1;

    MessageSend messageSend;
    SerialPort port;


    public mainForm() throws ParserConfigurationException {
        storage = new Storage();




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

        setDefaultTextFields();

        textField_vbat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.BATTERY_VOLTAGE, textField_vbat);
            }
        });

        textField_ibat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.BATTERY_CURRENT, textField_ibat);
            }
        });

        textField_BCRVoltage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.BCR_VOLTAGE, textField_BCRVoltage);
            }
        });

        textField_BCRCurrent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.BCR_CURRENT, textField_BCRCurrent);
            }
        });

        textField_XVoltage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.X_VOLTAGE, textField_XVoltage);
            }
        });

        textField_XCurrentPos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.X_POS_CURRENT, textField_XCurrentPos);
            }
        });

        textField_XCurrentNeg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.X_NEG_CURRENT, textField_XCurrentNeg);
            }
        });

        textField_YVoltage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.Y_VOLTAGE, textField_YVoltage);
            }
        });

        textField_YCurrentPos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.Y_POS_CURRENT, textField_YCurrentPos);
            }
        });

        textField_YCurrentNeg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.Y_NEG_CURRENT, textField_YCurrentNeg);
            }
        });

        textField_ZVoltage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.Z_VOLTAGE, textField_ZVoltage);
            }
        });

        textField_ZCurrentPos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.Z_POS_CURRENT, textField_ZCurrentPos);
            }
        });

        textField_ZCurrentNeg.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.Z_NEG_CURRENT, textField_ZCurrentNeg);
            }
        });

        textField_3V3Current.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.CURRENT_3V3, textField_3V3Current);
            }
        });

        textField__5VCurrent.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.CURRENT_5V, textField__5VCurrent);
            }
        });

        checkBox_3V3LUP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxChangeAction(P_ID.LUP_3V3, checkBox_3V3LUP);
            }
        });

        checkBox_5VLUP.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxChangeAction(P_ID.LUP_5V, checkBox_5VLUP);
            }
        });

        textField_tempMCU.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.MCU_TEMP, textField_tempMCU);
            }
        });

        textField_measTemp1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.BAT_TEMP_SENS1, textField_measTemp1);
            }
        });
        textField_measTemp2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.BAT_TEMP_SENS2, textField_measTemp2);
            }
        });
        textField_measTemp3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.BAT_TEMP_SENS3, textField_measTemp3);
            }
        });
        textField_measTemp4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.BAT_TEMP_SENS4, textField_measTemp4);
            }
        });

        textField_inputCond.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.INPUTS_COND, textField_inputCond);
            }
        });

        textField_outCond.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.OUTPUTS_COND, textField_outCond);
            }
        });

        textField_onCycles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.POWER_ON_CYCLES, textField_onCycles);
            }
        });

        textField_underVoltage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.BAT_UNDER_VOLTAGE, textField_underVoltage);
            }
        });
        textField_shortCircuit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.BAT_SHORT_CIRCUIT, textField_shortCircuit);
            }
        });
        textField_overTemp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.BAT_OVER_TEMP, textField_overTemp);
            }
        });

        textField_maxTemp1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.MAX_TEMP1, textField_maxTemp1);
            }
        });
        textField_maxTemp2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.MAX_TEMP2, textField_maxTemp2);
            }
        });

        textField_maxTemp3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.MAX_TEMP3, textField_maxTemp3);
            }
        });

        textField_maxTemp4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.MAX_TEMP4, textField_maxTemp4);
            }
        });

        textField_minTemp1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.MIN_TEMP1, textField_minTemp1);
            }
        });
        textField_minTemp2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.MIN_TEMP2, textField_minTemp2);
            }
        });
        textField_minTemp3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.MIN_TEMP3, textField_minTemp3);
            }
        });
        textField_minTemp4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.MIN_TEMP4, textField_minTemp4);
            }
        });
        textField_tempSens5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.TEMP_SENS5, textField_tempSens5);
            }
        });
        textField_tempSens6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.TEMP_SENS6, textField_tempSens6);
            }
        });
        textField_tempSens7.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.TEMP_SENS7, textField_tempSens7);
            }
        });
        textField_tempSens8.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.TEMP_SENS8, textField_tempSens8);
            }
        });

        textField_swVersion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.SW_VERSION, textField_swVersion);
            }
        });

        checkBox_fastCharge.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxChangeAction(P_ID.BAT_FAST_CH, checkBox_fastCharge);
            }
        });

        checkBox_GPO1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxChangeAction(P_ID.V_OUT_1_DEF, checkBox_GPO1);
            }
        });
        checkBox_GPO3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxChangeAction(P_ID.V_OUT_3_DEF, checkBox_GPO3);
            }
        });
        checkBox_GPO4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxChangeAction(P_ID.V_OUT_4_DEF, checkBox_GPO4);
            }
        });
        checkBox_GPO5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxChangeAction(P_ID.V_OUT_5_DEF, checkBox_GPO5);
            }
        });
        checkBox_GPO6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                checkBoxChangeAction(P_ID.V_OUT_6_DEF, checkBox_GPO6);
            }
        });

        textField_chargeCycles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textChangeAction(P_ID.CHARGE_CYCLES, textField_chargeCycles);
            }
        });

    }

    public void textChangeAction(P_ID id, JTextField field){
        Parameter parameter = defaultParameters.getParameterById(id);

        parameter.setDoubleValue(Double.parseDouble(field.getText()));
        parameter.calcAdcVal(1);
        messageSend.sendMessage(parameter.getAddress(), parameter.getAdcValue());

        defaultParameters.setParameterById(id, parameter);

//        defaultParameters.setValById(id, Double.parseDouble(field.getText()));
//        defaultParameters.calcAdcValById(id, 1);
//        messageSend.sendMessage(defaultParameters.getAddrById(id),defaultParameters.getADCValById(id));
    }

    public void checkBoxChangeAction(P_ID id, JCheckBox box){
        defaultParameters.setValById(id, (box.isSelected() == true ? 1 : 0));
        messageSend.sendMessage(defaultParameters.getAddrById(id),defaultParameters.getADCValById(id));
    }

    public void setDefaultTextFields(){
        defaultParameters = new DefaultParameters();

        textField_vbat.setText(defaultParameters.getStringValById(P_ID.BATTERY_VOLTAGE));
        textField_ibat.setText(defaultParameters.getStringValById(P_ID.BATTERY_CURRENT));
        textField_BCRVoltage.setText(defaultParameters.getStringValById(P_ID.BCR_VOLTAGE));
        textField_BCRCurrent.setText(defaultParameters.getStringValById(P_ID.BCR_CURRENT));
        textField_XVoltage.setText(defaultParameters.getStringValById(P_ID.X_VOLTAGE));
        textField_XCurrentPos.setText(defaultParameters.getStringValById(P_ID.X_POS_CURRENT));
        textField_XCurrentNeg.setText(defaultParameters.getStringValById(P_ID.X_NEG_CURRENT));
        textField_YVoltage.setText(defaultParameters.getStringValById(P_ID.Y_VOLTAGE));
        textField_YCurrentPos.setText(defaultParameters.getStringValById(P_ID.Y_POS_CURRENT));
        textField_YCurrentNeg.setText(defaultParameters.getStringValById(P_ID.Y_NEG_CURRENT));
        textField_ZVoltage.setText(defaultParameters.getStringValById(P_ID.Z_VOLTAGE));
        textField_ZCurrentPos.setText(defaultParameters.getStringValById(P_ID.Z_POS_CURRENT));
        textField_ZCurrentNeg.setText(defaultParameters.getStringValById(P_ID.Z_NEG_CURRENT));
        textField_3V3Current.setText(defaultParameters.getStringValById(P_ID.CURRENT_3V3));
        textField__5VCurrent.setText(defaultParameters.getStringValById(P_ID.CURRENT_5V));

        checkBox_3V3LUP.setSelected(defaultParameters.getValById(P_ID.LUP_3V3) != 0);
        checkBox_5VLUP.setSelected(defaultParameters.getValById(P_ID.LUP_5V) != 0);

        textField_tempMCU.setText(defaultParameters.getStringValById(P_ID.MCU_TEMP));
        textField_measTemp1.setText(defaultParameters.getStringValById(P_ID.BAT_TEMP_SENS1));
        textField_measTemp2.setText(defaultParameters.getStringValById(P_ID.BAT_TEMP_SENS2));
        textField_measTemp3.setText(defaultParameters.getStringValById(P_ID.BAT_TEMP_SENS3));
        textField_measTemp4.setText(defaultParameters.getStringValById(P_ID.BAT_TEMP_SENS4));

        textField_inputCond.setText(defaultParameters.getStringValById(P_ID.INPUTS_COND));
        textField_outCond.setText(defaultParameters.getStringValById(P_ID.OUTPUTS_COND));
        textField_onCycles.setText(defaultParameters.getStringValById(P_ID.POWER_ON_CYCLES));
        textField_underVoltage.setText(defaultParameters.getStringValById(P_ID.BAT_UNDER_VOLTAGE));
        textField_shortCircuit.setText(defaultParameters.getStringValById(P_ID.BAT_SHORT_CIRCUIT));
        textField_overTemp.setText(defaultParameters.getStringValById(P_ID.BAT_OVER_TEMP));

        textField_maxTemp1.setText(defaultParameters.getStringValById(P_ID.MAX_TEMP1));
        textField_maxTemp2.setText(defaultParameters.getStringValById(P_ID.MAX_TEMP2));
        textField_maxTemp3.setText(defaultParameters.getStringValById(P_ID.MAX_TEMP3));
        textField_maxTemp4.setText(defaultParameters.getStringValById(P_ID.MAX_TEMP4));

        textField_minTemp1.setText(defaultParameters.getStringValById(P_ID.MIN_TEMP1));
        textField_minTemp2.setText(defaultParameters.getStringValById(P_ID.MIN_TEMP2));
        textField_minTemp3.setText(defaultParameters.getStringValById(P_ID.MIN_TEMP3));
        textField_minTemp4.setText(defaultParameters.getStringValById(P_ID.MIN_TEMP4));

        textField_tempSens5.setText(defaultParameters.getStringValById(P_ID.TEMP_SENS5));
        textField_tempSens6.setText(defaultParameters.getStringValById(P_ID.TEMP_SENS6));
        textField_tempSens7.setText(defaultParameters.getStringValById(P_ID.TEMP_SENS7));
        textField_tempSens8.setText(defaultParameters.getStringValById(P_ID.TEMP_SENS8));

        textField_swVersion.setText(defaultParameters.getStringValById(P_ID.SW_VERSION));

        checkBox_fastCharge.setSelected(defaultParameters.getValById(P_ID.BAT_FAST_CH) != 0);
        checkBox_GPO1.setSelected(defaultParameters.getValById(P_ID.V_OUT_1_DEF) != 0);
        checkBox_GPO3.setSelected(defaultParameters.getValById(P_ID.V_OUT_3_DEF) != 0);
        checkBox_GPO4.setSelected(defaultParameters.getValById(P_ID.V_OUT_4_DEF) != 0);
        checkBox_GPO5.setSelected(defaultParameters.getValById(P_ID.V_OUT_5_DEF) != 0);
        checkBox_GPO6.setSelected(defaultParameters.getValById(P_ID.V_OUT_6_DEF) != 0);

        textField_chargeCycles.setText(defaultParameters.getStringValById(P_ID.CHARGE_CYCLES));
    }

    public static void main(String[] args) throws ParserConfigurationException {
        
        JFrame frame = new JFrame("EPS Simulator");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new mainForm().panel1);
        frame.setVisible(true);
    }



}
