import com.fazecast.jSerialComm.SerialPort;


public class run {

    public static void main(String[] args) {



        final SerialPort serialPort = SerialPort.getCommPort("COM3");

        serialPort.openPort();
        serialPort.setComPortParameters(115200,
                                         8,
                                        SerialPort.ONE_STOP_BIT,
                                        SerialPort.NO_PARITY);

        messageReceiver receiver = new messageReceiver();

        serialPort.addDataListener(receiver);

        messageSend send = new messageSend();

        send.sendMessage(0x5566,0x7788, serialPort);

//        serialPort.addDataListener(new SerialPortDataListener() {
//            public int getListeningEvents() {
//                return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
//            }
//
//            public void serialEvent(SerialPortEvent serialPortEvent) {
//                if (serialPortEvent.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
//                    return;
//
//                byte[] newData = new byte[serialPort.bytesAvailable()];
//                int numRead = serialPort.readBytes(newData, newData.length);
//                System.out.println("Read " + numRead + " bytes;");
//                for (int i = 0; i < numRead ; i++){
//                    System.out.print((char) newData[i]);
//                }
//
//            }
//        });



    }

}
