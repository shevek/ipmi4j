package org.anarres.ipmi.protocol.packet.rmcp;

import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

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

    private static final byte[] PAD0 = new byte[0];
    private static final byte[] PAD1 = new byte[1];
    private static final byte[] PAD2 = new byte[2];
    private static final byte[] PAD3 = new byte[3];

    @Nonnegative
    private static byte[] PAD(@Nonnegative int length) {
        switch (length & 0x03) {
            case 0:
                return PAD0;
            case 1:
                return PAD3;
            case 2:
                return PAD2;
            case 3:
                return PAD1;
            default:
                throw new IllegalStateException("Illegal length " + length);
        }
    }
    private int sessionId;
    private int sequenceNumber;
    // For this specification, the mandatory-to-implement integrity algorithm is HMAC-SHA1-96 defined in [RFC2404]. 
    private byte[] integrityData;

    public int getSessionId() {
        return sessionId;
    }

    @Nonnull
    public RspRmcpPacket withSessionId(int sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Nonnull
    public RspRmcpPacket withSequenceNumber(int sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    public byte[] getIntegrityData() {
        return integrityData;
    }

    @Nonnull
    public RspRmcpPacket withIntegrityData(byte[] integrityData) {
        this.integrityData = integrityData;
        return this;
    }

    @Override
    public int getWireLength() {
        RmcpData data = getData();
        int length = 4 // sessionId
                + 4 // sequenceNumber
                + getHeader().getWireLength()
                + ((data == null) ? 0 : data.getWireLength());
        return length
                + PAD(length).length // Padding
                + 1 // Padding length
                + 1 // "Next header" length
                + integrityData.length;
    }

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        int start = buffer.position();
        buffer.putInt(sessionId);
        buffer.putInt(sequenceNumber);
        RmcpHeader header = getHeader();
        header.toWire(buffer);
        RmcpData data = getData();
        if (data == null)
            return;
        data.toWire(buffer);
        if (sessionId != 0) {   // Page 24: Unsecured data.
            // Padding aligns us to a DWORD, which is 4 bytes.
            int length = buffer.position() - start;
            byte[] pad = PAD(length);
            buffer.put(pad);
            buffer.put((byte) pad.length);
            buffer.put(header.getVersion().getCode());
            buffer.put(integrityData);
        }
    }

    @Override
    public void fromWireHeader(ByteBuffer buffer) {
        int sessionId = buffer.getInt();
        withSessionId(sessionId);
        withSequenceNumber(buffer.getInt());
        getHeader().fromWire(buffer);
    }

    @Override
    public void fromWireBody(ByteBuffer buffer, int start) {
        RmcpData data = getData();
        if (data == null)   // We can't deserialize the trailer without data.
            return;
        data.fromWire(buffer);
        if (sessionId != 0) {   // Page 24: Unsecured data.
            int length = buffer.position() - start;
            byte[] pad = PAD(length);
            readBytes(buffer, pad.length);  // TODO: Write a temporary.
            assertWireByte(buffer, getHeader().getVersion().getCode());
            withIntegrityData(readBytes(buffer, buffer.remaining()));
        }
    }
}
