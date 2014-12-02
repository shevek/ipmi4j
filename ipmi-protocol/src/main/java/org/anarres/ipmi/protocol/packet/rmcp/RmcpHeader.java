package org.anarres.ipmi.protocol.packet.rmcp;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * RMCP Packet Header.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.2.2 page 21.
 * 
 * @author shevek
 */
public class RmcpHeader extends AbstractWireable {

    public static final int LENGTH = 4;
    private final RmcpVersion version = RmcpVersion.ASF_RMCP_2_0;
    /** 0xFF means do not generate an ACK. */
    private byte sequenceNumber = (byte) 0xFF;
    private RmcpMessageClass messageClass;
    private RmcpMessageRole messageRole = RmcpMessageRole.REQ;

    @Nonnull
    public RmcpVersion getVersion() {
        return version;
    }

    public byte getSequenceNumber() {
        return sequenceNumber;
    }

    @Nonnull
    public RmcpHeader withSequenceNumber(byte sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    @Nonnull
    public RmcpMessageClass getMessageClass() {
        return messageClass;
    }

    @Nonnull
    public RmcpHeader withMessageClass(RmcpMessageClass messageClass) {
        this.messageClass = messageClass;
        return this;
    }

    @Nonnull
    public RmcpMessageRole getMessageRole() {
        return messageRole;
    }

    @Nonnull
    public RmcpHeader withMessageRole(RmcpMessageRole messageRole) {
        this.messageRole = messageRole;
        return this;
    }

    @Override
    public int getWireLength() {
        return LENGTH;
    }

    @Override
    protected void toWireUnchecked(@Nonnull ByteBuffer buffer) {
        // DSP0136 page 22
        buffer.put(getVersion().getCode());
        buffer.put((byte) 0x00); // ASF reserved
        buffer.put(getSequenceNumber());
        byte messageClass = this.messageClass.getCode();
        if (messageRole == RmcpMessageRole.ACK)
            messageClass |= 0x80;
        buffer.put(messageClass);
    }

    @Override
    protected void fromWireUnchecked(ByteBuffer buffer) {
        assertWireByte(buffer, getVersion().getCode());
        assertWireByte(buffer, (byte) 0);
        withSequenceNumber(buffer.get());
        byte messageClass = buffer.get();
        withMessageClass(Code.fromByte(RmcpMessageClass.class, (byte) (messageClass & 0x7f)));
        withMessageRole(Code.fromByte(RmcpMessageRole.class, (byte) (messageClass >>> 7)));
    }
}
