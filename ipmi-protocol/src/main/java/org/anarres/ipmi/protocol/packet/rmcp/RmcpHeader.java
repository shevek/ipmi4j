package org.anarres.ipmi.protocol.packet.rmcp;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;

/**
 * RMCP Packet Header.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * 
 * @author shevek
 */
public abstract class RmcpHeader implements Wireable {

    private byte sequenceNumber;

    private enum MessageClass {

        ASF(6), IPMI(7), OEM(8);
        private final byte value;

        private MessageClass(int value) {
            this.value = UnsignedBytes.checkedCast(value);
        }

        public byte getValue() {
            return value;
        }
    }
    private MessageClass messageClass;

    private enum MessageClassType {

        REQ, ACK
    };
    private MessageClassType messageClassType;

    @Override
    public int getWireLength() {
        return 4;
    }

    @Override
    public void toWire(@Nonnull ByteBuffer buffer) {
        // DSP0136 page 22
        buffer.put((byte) 0x06); // ASF RCMP v1
        buffer.put((byte) 0x00); // ASF reserved
        buffer.put(sequenceNumber);
        byte messageClass = (byte) this.messageClass.value;
        if (messageClassType == MessageClassType.ACK)
            messageClass |= 0x80;
        buffer.put(messageClass);
    }
}
