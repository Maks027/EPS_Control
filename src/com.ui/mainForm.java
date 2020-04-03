import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
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
    private JButton button_send;

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
                port = SerialPort.getCommPort((String) comboBox_selectCom.getSelectedItem());
                if (port.isOpen()) {
                    textArea_terminal.append("Port " + port.getSystemPortName() + " is already opened!\n");
                } else {
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
                }
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


        button_send.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                messageSend send = new messageSend();

                send.sendMessage(0x5566, 0x7788, port);
            }
        });


        textField_vbat.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                defaultParameters.modifyVal(1, Double.valueOf(textField_vbat.getText()), list);
                for(Parameter p: list){
                    storage.createElement(p);
                }

                try {
                    storage.writeToFile();
                } catch (TransformerException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        setDefaultTextFields();

    }

    public void setDefaultTextFields(){
        defaultParameters = new DefaultParameters();
        list = defaultParameters.getParameterList();

        textField_vbat.setText(String.valueOf(defaultParameters.getValFromList(1, list)));
        textField_ibat.setText(String.valueOf(defaultParameters.getValFromList(2, list)));
        textField_BCRVoltage.setText(String.valueOf(defaultParameters.getValFromList(3, list)));
        textField_BCRCurrent.setText(String.valueOf(defaultParameters.getValFromList(4, list)));
        textField_XVoltage.setText(String.valueOf(defaultParameters.getValFromList(5, list)));
        textField_XCurrentPos.setText(String.valueOf(defaultParameters.getValFromList(6, list)));
        textField_XCurrentNeg.setText(String.valueOf(defaultParameters.getValFromList(7, list)));
        textField_YVoltage.setText(String.valueOf(defaultParameters.getValFromList(8, list)));
        textField_YCurrentPos.setText(String.valueOf(defaultParameters.getValFromList(9, list)));
        textField_YCurrentNeg.setText(String.valueOf(defaultParameters.getValFromList(10, list)));
        textField_ZVoltage.setText(String.valueOf(defaultParameters.getValFromList(11, list)));
        textField_ZCurrentPos.setText(String.valueOf(defaultParameters.getValFromList(12, list)));
        textField_ZCurrentNeg.setText(String.valueOf(defaultParameters.getValFromList(13, list)));
        textField_3V3Current.setText(String.valueOf(defaultParameters.getValFromList(14, list)));
        textField__5VCurrent.setText(String.valueOf(defaultParameters.getValFromList(15, list)));

    }

    public static void main(String[] args) throws ParserConfigurationException {
        
        JFrame frame = new JFrame("EPS Simulator");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new mainForm().panel1);
        frame.setVisible(true);
    }



}
