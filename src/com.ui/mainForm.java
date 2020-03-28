import com.fazecast.jSerialComm.SerialPort;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class mainForm {


    private JPanel panel1;
    private JButton ScanButton;
    private JComboBox comboBox1;
    private JTextArea textArea1;
    private JButton connectButton;
    private JButton closeButton;

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
                    textArea1.append("Port " + port.getSystemPortName() + " opened\n");
                }
            }
        });

        closeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                port.closePort();
                if (!port.isOpen()){
                    textArea1.append("Port closed\n");
                }
            }
        });
    }

    public static void main(String[] args) {



        JFrame frame = new JFrame("EPS Simulator");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new mainForm().panel1);
        frame.setVisible(true);
    }



}
