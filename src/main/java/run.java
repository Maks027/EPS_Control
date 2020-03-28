import jssc.SerialPort;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortList;


public class run {

    public static void main(String[] args) throws SerialPortException {

//        SerialPort a = new SerialPort("COM3");

        String[] ports = SerialPortList.getPortNames();

        for (int i = 0 ; i < ports.length ; i++) {
            System.out.println(ports[i]);
        }

        SerialPort serialPort = new SerialPort("COM3");

        serialPort.openPort();

        serialPort.setParams(SerialPort.BAUDRATE_115200,
                             SerialPort.DATABITS_8,
                             SerialPort.STOPBITS_1,
                             SerialPort.PARITY_NONE);

        if (serialPort.isOpened()){
            System.out.println("Port opened");
        }
        else {
            System.out.println("Port closed");
        }

        serialPort.closePort();

        if (serialPort.isOpened()){
            System.out.println("Port opened");
        }
        else {
            System.out.println("Port closed");
        }
    }
}
