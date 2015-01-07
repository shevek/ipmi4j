package org.anarres.ipmi.protocol.packet.rmcp;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity.IntegrityPad;

/**
 * RMCP Security Extensions Protocol (RSP).
 * 
 * [ASF] Section 3.2.3 pages 23-25.
 * 
 * Uses UDP port 0x0298.
 *
 * @author shevek
 */
public class RspRmcpPacket extends AbstractPacket {

    private int sessionId;
    private int sessionSequenceNumber;
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

    public int getSessionSequenceNumber() {
        return sessionSequenceNumber;
    }

    @Nonnull
    public RspRmcpPacket withSessionSequenceNumber(int sessionSequenceNumber) {
        this.sessionSequenceNumber = sessionSequenceNumber;
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
                + 4 // sessionSequenceNumber
                + getRawWireLength();
        return length
                + IntegrityPad.PAD(length).length // Padding
                + 1 // Padding length
                + 1 // "Next header" length
                + integrityData.length;
    }

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        int start = buffer.position();
        buffer.putInt(sessionId);
        buffer.putInt(sessionSequenceNumber);
        toWireRaw(buffer);
        if (sessionId != 0) {   // Page 24: Unsecured data.
            // Padding aligns us to a DWORD, which is 4 bytes.
            int length = buffer.position() - start;
            byte[] pad = IntegrityPad.PAD(length);
            buffer.put(pad);
            buffer.put((byte) pad.length);
            buffer.put(getVersion().getCode());
            buffer.put(integrityData);
        }
    }

    @Override
    protected final void fromWireUnchecked(ByteBuffer buffer) {
        int start = buffer.position();
        int sessionId = buffer.getInt();
        withSessionId(sessionId);
        withSessionSequenceNumber(buffer.getInt());
        fromWireRaw(buffer);
        if (sessionId != 0) {   // Page 24: Unsecured data.
            int length = buffer.position() - start;
            byte[] pad = IntegrityPad.PAD(length);
            readBytes(buffer, pad.length);  // TODO: Write a temporary.
            assertWireByte(buffer, (byte) pad.length, "padding length");
            assertWireByte(buffer, getVersion().getCode(), "header version");
            withIntegrityData(readBytes(buffer, buffer.remaining()));
        }
    }
}
