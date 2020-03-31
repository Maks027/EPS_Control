import com.fazecast.jSerialComm.SerialPort;

public class messageSend {

    private final int PACKET_LENGTH = 8;

    private final byte START_BYTE1 = (byte)0x4A;
    private final byte START_BYTE2 = (byte)0x8B;
    private final byte STOP_BYTE = (byte)0xCC;

    private final int
            POS_START_BYTE1 = 0,
            POS_START_BYTE2 = 1,
            POS_RW_BYTE		= 2,
            POS_ADR_MSB 	= 3,
            POS_ADR_LSB 	= 4,
            POS_DATA_MSB	= 5,
            POS_DATA_LSB 	= 6,
            POS_STOP_BYTE 	= 7;

    public void sendMessage(int address, int data, SerialPort port){

        byte[] buffer = new byte[PACKET_LENGTH];

        buffer[POS_START_BYTE1] = START_BYTE1;
        buffer[POS_START_BYTE2] = START_BYTE2;
        buffer[POS_RW_BYTE] = (byte)0x00;
        buffer[POS_ADR_MSB] = (byte)(address >> 8);
        buffer[POS_ADR_LSB] = (byte)address;
        buffer[POS_DATA_MSB] = (byte)(data >> 8);
        buffer[POS_DATA_LSB] = (byte)data;
        buffer[POS_STOP_BYTE] = STOP_BYTE;

        port.writeBytes(buffer, PACKET_LENGTH);
    }

}
