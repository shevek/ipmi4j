/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import com.google.common.primitives.UnsignedBytes;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.asf.AbstractAsfData;
import org.anarres.ipmi.protocol.packet.asf.AsfRmcpMessageType;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionAuthenticationType;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;

/**
 *
 * @author shevek
 */
public abstract class AbstractPacket extends AbstractWireable implements Packet {

    private SocketAddress remoteAddress;
    protected static final int RMCP_HEADER_LENGTH = 4;
    private final RmcpVersion version = RmcpVersion.ASF_RMCP_2_0;
    /** 0xFF means do not generate an ACK. */
    private byte sequenceNumber = (byte) 0xFF;
    private RmcpMessageClass messageClass;
    private RmcpMessageRole messageRole = RmcpMessageRole.REQ;
    /** Data for a message, null for an ack. */
    private RmcpData data;

    @Override
    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Nonnull
    public AbstractPacket withRemoteAddress(@Nonnull SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
        return this;
    }

    @Nonnull
    public RmcpVersion getVersion() {
        return version;
    }

    public byte getSequenceNumber() {
        return sequenceNumber;
    }

    @Nonnull
    public AbstractPacket withSequenceNumber(byte sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
        return this;
    }

    @Nonnull
    public RmcpMessageClass getMessageClass() {
        return messageClass;
    }

    @Nonnull
    public AbstractPacket withMessageClass(RmcpMessageClass messageClass) {
        this.messageClass = messageClass;
        return this;
    }

    @Nonnull
    public RmcpMessageRole getMessageRole() {
        return messageRole;
    }

    @Nonnull
    public AbstractPacket withMessageRole(RmcpMessageRole messageRole) {
        this.messageRole = messageRole;
        return this;
    }

    @Override
    public RmcpData getData() {
        return data;
    }

    @Override
    public Packet withData(@Nonnull RmcpData data) {
        withMessageClass(data.getMessageClass());
        this.data = data;
        return this;
    }

    @Nonnegative
    protected int getRawWireLength() {
        return RMCP_HEADER_LENGTH + getData().getWireLength();
    }

    /**
     * RMCP Packet Header.
     * [ASF] Section 3.2.2.2 page 21.
     */
    private void toWireHeader(@Nonnull ByteBuffer buffer) {
        // DSP0136 page 22
        buffer.put(getVersion().getCode());
        buffer.put((byte) 0x00); // ASF reserved
        buffer.put(getSequenceNumber());
        byte messageClass = this.messageClass.getCode();
        if (messageRole == RmcpMessageRole.ACK)
            messageClass |= 0x80;
        buffer.put(messageClass);
    }

    protected void toWireRaw(@Nonnull ByteBuffer buffer) {
        toWireHeader(buffer);
        getData().toWire(buffer);
    }

    private void fromWireHeader(@Nonnull ByteBuffer buffer) {
        assertWireByte(buffer, getVersion().getCode(), "RMCP version");
        assertWireByte(buffer, (byte) 0, "reserved field");
        withSequenceNumber(buffer.get());
        byte messageClass = buffer.get();
        withMessageClass(Code.fromByte(RmcpMessageClass.class, (byte) (messageClass & 0x7f)));
        withMessageRole(Code.fromByte(RmcpMessageRole.class, (byte) (messageClass >>> 7)));
    }

    protected void fromWireRaw(@Nonnull ByteBuffer buffer) {
        fromWireHeader(buffer);
        RmcpData data = null;

        int position = buffer.position();
        switch (getMessageClass()) {
            case ASF:
                int enterpriseNumber = buffer.getInt();
                if (enterpriseNumber != AbstractAsfData.IANA_ENTERPRISE_NUMBER.getNumber())
                    throw new IllegalArgumentException("Unknown enterprise number 0x" + Integer.toHexString(enterpriseNumber));
                AsfRmcpMessageType messageType = Code.fromBuffer(AsfRmcpMessageType.class, buffer);
                data = messageType.newPacketData();
                buffer.position(position);
                break;
            case IPMI:
                IpmiSessionAuthenticationType format = Code.fromByte(IpmiSessionAuthenticationType.class, buffer.get());
                if (format == IpmiSessionAuthenticationType.RMCPP) {
                    // IPMI v2.0
                    IpmiPayloadType payloadType = Code.fromByte(IpmiPayloadType.class, buffer.get());
                } else {
                    // IPMI v1.5
                }
                buffer.position(position);
                break;
            case OEM:
            default:
                throw new IllegalArgumentException("Can't decode buffer: Unknown MessageClass " + getMessageClass());
        }

        withData(data);
        data.fromWire(buffer);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        append(buf, depth, "RmcpSequenceNumber", UnsignedBytes.toInt(getSequenceNumber()));
        append(buf, depth, "RmcpMessageClass", getMessageClass());
        append(buf, depth, "RmcpMessageRole", getMessageRole());
    }
}