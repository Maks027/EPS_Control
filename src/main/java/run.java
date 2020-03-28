import com.fazecast.jSerialComm.SerialPort;

public class run {

    public static void main(String[] args) {

        SerialPort serialPort = SerialPort.getCommPort("COM3");

        serialPort.openPort();
        serialPort.setComPortParameters(115200,
                                         8,
                                        SerialPort.ONE_STOP_BIT,
                                        SerialPort.NO_PARITY);

        if(serialPort.isOpen()){
            System.out.println("Opened");
        }
        else {
            System.out.println("Closed");
        }

    }

}
