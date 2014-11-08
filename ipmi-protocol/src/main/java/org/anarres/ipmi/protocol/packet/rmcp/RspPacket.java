package org.anarres.ipmi.protocol.packet.rmcp;

import java.nio.ByteBuffer;

/**
 * RMCP Security Extensions Protocol.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * Section 3.2.3 page 23.
 * 
 * Uses UDP port 0x0298.
 *
 * @author shevek
 */
public abstract class RspPacket implements Packet {

    private int sessionId;
    private int sequenceNumber;
    private RmcpHeader packet;
    private byte[] pad;
    private byte nextHeader;
    private byte[] integrityData;

    @Override
    public int getWireLength() {
        return 4 + 4 + packet.getWireLength() + pad.length + 1 + 1 + integrityData.length;
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        int start = buffer.position();
        buffer.putInt(sessionId);
        buffer.putInt(sequenceNumber);
        packet.toWire(buffer);
        if (sessionId != 0) {   // Page 24: Unsecured data.
            // Padding aligns us to a DWORD, which is 4 bytes.
            int length = buffer.position() - start;
            int pad = (4 - length) & 0x03;  // I think this is right!
            buffer.put(new byte[pad]);
            buffer.put((byte) pad);
            buffer.put(nextHeader);
            buffer.put(integrityData);
        }
    }

    @Override
    public void fromWire(ByteBuffer buffer) {
        throw new UnsupportedOperationException();
    }

}
