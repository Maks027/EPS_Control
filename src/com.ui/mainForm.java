import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Integer.parseInt;


public class mainForm {


    private JPanel panel1;
    private JButton ScanButton;
    private JComboBox comboBox1;
    private JTextArea textArea1;
    private JButton connectButton;
    private JButton closeButton;
    private JLabel statusLabel;
    private JTextField baudRate;
    private JPanel parametersPanel;
    private JPanel statusPanel1;
    private JPanel statusPanel2;
    private JPanel statusPanel3;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;

    SerialPort port;

    public mainForm() {
        ScanButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SerialPort[] ports = SerialPort.getCommPorts();
                comboBox1.removeAllItems();
                for (int i = 0 ; i < ports.length ; i++){
                    comboBox1.addItem(ports[i].getSystemPortName());

                }
                    //textArea1.append(ports[i].getSystemPortName() + "\n");
            }
        });

        connectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                port = SerialPort.getCommPort((String)comboBox1.getSelectedItem());
                if (port.isOpen()) {
                    textArea1.append("Port " + port.getSystemPortName() + " is already opened!\n");
                } else {
                    port.openPort();
                    port.setComPortParameters(
                            115200,
                            8,
                            SerialPort.ONE_STOP_BIT,
                            SerialPort.NO_PARITY);
                    port.setBaudRate(parseInt(baudRate.getText()));
                    textArea1.append("Port " + port.getSystemPortName() + " opened\n");
                    textArea1.append("Baud Rate: " + port.getBaudRate());
                    statusLabel.setText("Connected to " + port.getSystemPortName());
                    statusLabel.setForeground(Color.green);
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                port.closePort();
                if (!port.isOpen()){
                    textArea1.append("Port closed\n");
                    statusLabel.setText("Disconnected");
                    statusLabel.setForeground(Color.red);

                }
            }
        });
    }

    public static void main(String[] args) {



        JFrame frame = new JFrame("EPS Simulator");
        frame.setSize(1200, 700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new mainForm().panel1);
        frame.setVisible(true);
    }



}
