package org.anarres.ipmi.protocol.packet.rmcp;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiContext;

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

    public static final int PORT = 0x26F;

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
    public int getWireLength(IpmiContext context) {
        int length = getRawWireLength(context);
        if (isPaddingRequired(length))
            length++;
        return length;
    }

    @Override
    protected void toWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        toWireRaw(context, buffer);
        // TODO: Compute this based on buffer position rather than re-walking the packet.
        if (isPaddingRequired(getRawWireLength(context)))
            buffer.put((byte) 0);
    }

    @Override
    protected final void fromWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        fromWireRaw(context, buffer);
        // There may or may not be one additional byte here.
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, "RmcpHeader");
        super.toStringBuilder(buf, depth + 1);  // Header
        appendChild(buf, depth, "RmcpData", getData());
    }
}
