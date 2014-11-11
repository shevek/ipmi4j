package org.anarres.ipmi.protocol.packet.rmcp;

import org.anarres.ipmi.protocol.packet.common.Wireable;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;

/**
 * RMCP Packet Header.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.2.2 page 21.
 * 
 * @author shevek
 */
public class RmcpHeader implements Wireable {

    private final RmcpVersion version = RmcpVersion.ASF_RMCP_1_0;
    private byte sequenceNumber;
    private RmcpMessageClass messageClass;
    private RmcpMessageRole messageRole;

    @Nonnull
    public RmcpVersion getVersion() {
        return version;
    }

    @Override
    public int getWireLength() {
        return 4;
    }

    @Override
    public void toWire(@Nonnull ByteBuffer buffer) {
        // DSP0136 page 22
        buffer.put(getVersion().getValue());
        buffer.put((byte) 0x00); // ASF reserved
        buffer.put(sequenceNumber);
        byte messageClass = (byte) this.messageClass.value;
        if (messageRole == RmcpMessageRole.ACK)
            messageClass |= 0x80;
        buffer.put(messageClass);
    }
}
