package org.anarres.ipmi.protocol.packet.rmcp;

import java.nio.ByteBuffer;

/**
 * RMCP Packet.
 * 
 * [ASF2] Section 3.2.3 page 23.
 * 
 * Uses UDP port 0x026F.
 *
 * @author shevek
 */
public class RmcpPacket extends AbstractPacket {

    private int getNativeWireLength() {
        RmcpData data = getData();
        return getHeader().getWireLength()
                + ((data == null) ? 0 : data.getWireLength());
    }

    /** Intel IPMI spec page 134 footnote 1. */
    private static boolean isPaddingRequired(int length) {
        switch (length) {
            case 56:
            case 84:
            case 112:
            case 128:
            case 156:
                return true;
            default:
                return false;
        }
    }

    @Override
    public int getWireLength() {
        int length = getNativeWireLength();
        if (isPaddingRequired(length))
            length++;
        return length;
    }

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        getHeader().toWire(buffer);
        RmcpData data = getData();
        if (data == null)
            return;
        data.toWire(buffer);
        if (isPaddingRequired(getNativeWireLength()))
            buffer.put((byte) 0);
    }

    @Override
    public void fromWireHeader(ByteBuffer buffer) {
        getHeader().fromWire(buffer);
    }

    @Override
    public void fromWireBody(ByteBuffer buffer, int start) {
        RmcpData data = getData();
        if (data == null)
            return;
        data.fromWire(buffer);
    }
}
