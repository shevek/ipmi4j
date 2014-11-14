package org.anarres.ipmi.protocol.packet.rmcp;

import java.nio.ByteBuffer;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;

/**
 * RMCP Packet.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.3 page 23.
 * 
 * Uses UDP port 0x026F.
 *
 * @author shevek
 */
public class RmcpPacket extends AbstractPacket {

    private RmcpHeader header;
    /** Data for a message, null for an ack. */
    private RmcpData data;

    @Nonnull
    public RmcpHeader getHeader() {
        return header;
    }

    @CheckForNull
    public RmcpData getData() {
        return data;
    }

    @Override
    public int getWireLength() {
        return header.getWireLength()
                + ((data == null) ? 0 : data.getWireLength());
    }

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        header.toWire(buffer);
        if (data != null)
            data.toWire(buffer);
    }
}
