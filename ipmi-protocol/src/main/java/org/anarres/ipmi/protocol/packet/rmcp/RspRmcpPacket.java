package org.anarres.ipmi.protocol.packet.rmcp;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;

/**
 * RMCP Security Extensions Protocol.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.3 pages 23-25.
 * 
 * Uses UDP port 0x0298.
 *
 * @author shevek
 */
public class RspRmcpPacket extends AbstractPacket {

    @Nonnegative
    private static int PAD(@Nonnegative int length) {
        return (4 - length) & 0x03;
    }
    private int sessionId;
    private int sequenceNumber;
    private RmcpPacket packet;
    // For this specification, the mandatory-to-implement integrity algorithm is HMAC-SHA1-96 defined in [RFC2404]. 
    private byte[] integrityData;

    @Override
    public int getWireLength() {
        int length = 4 // sessionId
                + 4 // sequenceNumber
                + packet.getWireLength();
        return length
                + PAD(length) // Padding
                + 1 // Padding length
                + 1 // Integrity data length
                + integrityData.length;
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        int start = buffer.position();
        int length = getWireLength();
        buffer.putInt(sessionId);
        buffer.putInt(sequenceNumber);
        packet.toWire(buffer);
        if (sessionId != 0) {   // Page 24: Unsecured data.
            // Padding aligns us to a DWORD, which is 4 bytes.
            int dataLength = buffer.position() - start;
            int pad = PAD(dataLength);
            buffer.put(new byte[pad]);
            buffer.put((byte) pad);
            buffer.put(packet.getHeader().getVersion().getValue());
            buffer.put(integrityData);
        }
        if (buffer.position() - start != length)
            throw new IllegalStateException("Bad serializer.");
    }
}
