package org.anarres.ipmi.protocol.packet.rmcp;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;

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

    /** [IPMI2] Page 134, footnote 1. */
    private static boolean isPaddingRequired(@Nonnegative int length) {
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
        int length = getRawWireLength();
        if (isPaddingRequired(length))
            length++;
        return length;
    }

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        toWireRaw(buffer);
        if (isPaddingRequired(getRawWireLength()))
            buffer.put((byte) 0);
    }

    @Override
    protected final void fromWireUnchecked(ByteBuffer buffer) {
        fromWireRaw(buffer);
        // There may or may not be one additional byte here.
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, "RmcpHeader");
        super.toStringBuilder(buf, depth + 1);  // Header
        appendChild(buf, depth, "RmcpData", getData());
    }
}
