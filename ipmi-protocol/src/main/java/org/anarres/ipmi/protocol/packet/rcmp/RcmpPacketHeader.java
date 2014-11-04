package org.anarres.ipmi.protocol.packet.rcmp;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * http://www.dmtf.org/standards/asf
 * @author shevek
 */
public abstract class RcmpPacketHeader {

    private byte sequenceNumber;

    private enum MessageClassType {

        REQ, ACK
    };
    private MessageClassType messageClassType;

    private enum MessageClass {

        ASF(6), IPMI(7), OEM(8);
        private final int value;

        private MessageClass(int value) {
            this.value = value;
        }
    }

    private MessageClass messageClass;

    @Nonnegative
    public int getWireLength() {
        return 4;
    }

    public void toWire(@Nonnull ByteBuffer buffer) {
        // DSP0136 page 22
        buffer.put((byte) 0x06); // ASF RCMP v1
        buffer.put((byte) 0x00); // ASF reserved
        buffer.put(sequenceNumber);
        byte messageClass = (byte) this.messageClass.value;
        if (messageClassType == MessageClassType.ACK)
            messageClass |= (1 << 7);
        buffer.put(messageClass);
    }

}
