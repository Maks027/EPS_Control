import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;


public class messageReceiver implements SerialPortMessageListener {

    private final byte START_BYTE1 = (byte)0x4A;

    private final byte START_BYTE2 = (byte)0x8B;

    private final byte STOP_BYTE = (byte)0xCC;


    public byte[] getMessageDelimiter() {
        return new byte[]{START_BYTE1, START_BYTE2};
    }

    public boolean delimiterIndicatesEndOfMessage() {
        return false;
    }

    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
    }

    public void serialEvent(SerialPortEvent serialPortEvent) {
        byte[] message = serialPortEvent.getReceivedData();
        System.out.println("Message:");
        for (byte m : message){
            System.out.print(String.format("%X", m) + "  ");
        }
        System.out.println();
    }
}


