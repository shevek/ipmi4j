/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import org.anarres.ipmi.protocol.IanaEnterpriseNumber;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.AbstractIpmiSessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiLun;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiNetworkFunction;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAuthenticationCapabilitiesRequest;
import org.anarres.ipmi.protocol.packet.ipmi.payload.AbstractIpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;
import org.anarres.ipmi.protocol.client.session.IpmiPacketContext;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [IPMI2] Section 13.8, figure 13-4, page 136.
 *
 * @author shevek
 */
public abstract class AbstractIpmiCommand extends AbstractIpmiPayload implements IpmiCommand {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractIpmiCommand.class);
    private byte targetAddress = 0x20;
    private IpmiLun targetLun = IpmiLun.L0;
    private byte sourceAddress = (byte) 0x81;
    private IpmiLun sourceLun = IpmiLun.L0;
    private byte sequenceNumber;

    /* implements IpmiResponse */
    @Nonnull
    public Class<? extends IpmiRequest> getRequestType() {
        return MoreObjects.firstNonNull(getCommandName().getRequestType(), UnknownIpmiRequest.class);
    }

    /* implements IpmiRequest */
    @Nonnull
    public Class<? extends IpmiResponse> getResponseType() {
        return MoreObjects.firstNonNull(getCommandName().getResponseType(), UnknownIpmiResponse.class);
    }

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.IPMI;
    }

    @Override
    public void apply(IpmiClientIpmiPayloadHandler handler, IpmiHandlerContext context, IpmiSession session) {
        handler.handleCommand(context, session, this);
    }

    @Override
    public byte getTargetAddress() {
        return targetAddress;
    }

    @Override
    public IpmiLun getTargetLun() {
        return targetLun;
    }

    @Nonnull
    public AbstractIpmiCommand withTarget(byte targetAddress, @Nonnull IpmiLun targetLun) {
        this.targetAddress = targetAddress;
        this.targetLun = targetLun;
        return this;
    }

    @Nonnull
    public AbstractIpmiCommand withTarget(@Nonnegative int targetAddress, @Nonnull IpmiLun targetLun) {
        return withTarget(UnsignedBytes.checkedCast(targetAddress), targetLun);
    }

    @Override
    public byte getSourceAddress() {
        return sourceAddress;
    }

    @Override
    public IpmiLun getSourceLun() {
        return sourceLun;
    }

    @Nonnull
    public AbstractIpmiCommand withSource(byte sourceAddress, @Nonnull IpmiLun sourceLun) {
        this.sourceAddress = sourceAddress;
        this.sourceLun = sourceLun;
        return this;
    }

    @Nonnull
    public AbstractIpmiCommand withSource(@Nonnegative int sourceAddress, @Nonnull IpmiLun sourceLun) {
        return withSource(UnsignedBytes.checkedCast(sourceAddress), sourceLun);
    }

    @Override
    public byte getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public void setSequenceNumber(byte sequenceNumber) {
        this.sequenceNumber = (byte) (sequenceNumber & SEQUENCE_NUMBER_MASK);
    }

    @Override
    public int getWireLength(IpmiPacketContext context) {
        return 6
                + getDataWireLength()
                + 1;    // data checksum.
    }

    /** Returns the length of the ASF data part of this packet. */
    @Nonnegative
    protected abstract int getDataWireLength();

    @Override
    protected void toWireUnchecked(IpmiPacketContext context, ByteBuffer buffer) {
        int chk1Start = buffer.position();
        buffer.put(getTargetAddress());
        byte networkFunctionByte = getCommandName().getNetworkFunction().getCode();
        if (this instanceof IpmiResponse)
            networkFunctionByte |= 1;
        buffer.put((byte) (networkFunctionByte << 2 | getTargetLun().getValue()));
        toWireChecksum(buffer, chk1Start);
        int chk2Start = buffer.position();
        buffer.put(getSourceAddress());
        int seq = getSequenceNumber() & SEQUENCE_NUMBER_MASK;
        buffer.put((byte) (seq << 2 | getSourceLun().getValue()));
        buffer.put(getCommandName().getCode());
        toWireData(buffer);
        toWireChecksum(buffer, chk2Start);
    }

    protected abstract void toWireData(@Nonnull ByteBuffer buffer);

    /**
     * {@inheritDoc}
     *
     * This implementation must be called with a buffer of exactly decodeable
     * length, as some packet types require consumption of "optional" or
     * "remaining" data without an auxiliary embedded length field.
     *
     * @see AbstractIpmiSessionWrapper#newPayload(ByteBuffer, IpmiPayloadType)
     */
    @Override
    protected void fromWireUnchecked(IpmiPacketContext context, ByteBuffer buffer) {
        int chk1Start = buffer.position();
        int tmp;
        targetAddress = buffer.get();
        tmp = UnsignedBytes.toInt(buffer.get());
        IpmiNetworkFunction networkFunction = Code.fromInt(IpmiNetworkFunction.class, (tmp >>> 2) & ~1);
        targetLun = Code.fromInt(IpmiLun.class, tmp & IpmiLun.MASK);
        fromWireChecksum(buffer, chk1Start, "IPMI header checksum");
        int chk2Start = buffer.position();
        sourceAddress = buffer.get();
        tmp = UnsignedBytes.toInt(buffer.get());
        sequenceNumber = (byte) ((tmp >>> 2) & SEQUENCE_NUMBER_MASK);
        sourceLun = Code.fromInt(IpmiLun.class, tmp & IpmiLun.MASK);
        IpmiCommandName commandName = IpmiCommandName.fromByte(networkFunction, buffer.get());
        buffer.limit(buffer.limit() - 1);   // Represent an accurate data length to the packet data decoder.
        fromWireData(buffer);
        buffer.limit(buffer.limit() + 1);   // And let us get the checksum out.
        fromWireChecksum(buffer, chk2Start, "IPMI data checksum");
    }

    protected abstract void fromWireData(@Nonnull ByteBuffer buffer);

    /** [IPMI2] Section 13.8, page 137. */
    private static byte toChecksum(@Nonnull ByteBuffer buffer, @Nonnegative int start) {
        int csum = 0;
        for (int i = start; i < buffer.position(); i++)
            csum += buffer.get(i);
        return (byte) -csum;
    }

    public static void toWireChecksum(@Nonnull ByteBuffer buffer, @Nonnegative int start) {
        buffer.put(toChecksum(buffer, start));
    }

    public void fromWireChecksum(@Nonnull ByteBuffer buffer, @Nonnegative int start, @Nonnull String description) {
        byte expect = toChecksum(buffer, start);
        byte actual = buffer.get();
        if (expect != actual)
            throw new IllegalArgumentException("Checksum failure: " + description
                    + ": expected=" + UnsignedBytes.toString(expect, 16)
                    + " actual=" + UnsignedBytes.toString(actual, 16) + "; command=" + getClass().getSimpleName() + "; data=" + this);
    }

    public static void toWireOemIanaLE3(@Nonnull ByteBuffer buf, int data) {
        buf.put((byte) (data));
        buf.put((byte) (data >> 8));
        buf.put((byte) (data >> 16));
    }

    /**
     * @see GetChannelAuthenticationCapabilitiesRequest
     */
    public static int fromWireOemIanaLE3(@Nonnull ByteBuffer buf) {
        byte b0 = buf.get();
        byte b1 = buf.get();
        byte b2 = buf.get();
        return Ints.fromBytes((byte) 0, b2, b1, b0);
    }

    @Nonnull
    public static String toStringOemIana(int code) {
        IanaEnterpriseNumber value = null;
        for (IanaEnterpriseNumber n : IanaEnterpriseNumber.values()) {
            if (code == n.getNumber()) {
                value = n;
                break;
            }
        }
        return "0x" + Integer.toHexString(code) + " (" + code + "): " + value;
    }

    /**
     * Encodes one byte (two digits) of BCD.
     * The argument cannot be negative as the domain is 0-99.
     */
    public static byte toWireBcdLE(@Nonnegative byte data) {
        Preconditions.checkPositionIndex(data, 99, "BCD encoder input out of range");
        int b0 = data % 10; // Low digit
        data /= 10;
        int b1 = data % 10; // High digit
        return (byte) ((b0 & 0xF) << 4 | (b1 & 0xF));
    }

    @Nonnegative
    public static void toWireBcdLE(@Nonnull ByteBuffer buffer, @Nonnegative byte data) {
        buffer.put(toWireBcdLE(data));
    }

    /**
     * Decodes one byte (two digits) of BCD.
     * The return value cannot be negative as the range is 0-99.
     */
    @Nonnegative
    public static byte fromWireBcdLE(byte data) {
        int b0 = (data >> 4) & 0xF; // Low digit
        int b1 = data & 0xF; // High digit
        Preconditions.checkPositionIndex(b0, 9, "BCD decoder low digit out of range");
        Preconditions.checkPositionIndex(b1, 9, "BCD decoder high digit out of range");
        return (byte) (b1 * 10 + b0);
    }

    @Nonnegative
    public static byte fromWireBcdLE(@Nonnull ByteBuffer buffer) {
        return fromWireBcdLE(buffer.get());
    }

    /** I2C addresses are written to the wire with the low bit set to 0. */
    public static void toWireI2CAddress(@Nonnull ByteBuffer buffer, byte data) {
        buffer.put((byte) (data << 1));
    }

    /** I2C addresses are written to the wire with the low bit set to 0. */
    public static byte fromWireI2CAddress(@Nonnull ByteBuffer buffer) {
        byte data = buffer.get();
        return (byte) (data >>> 1);
    }

    public static boolean getBit(byte data, @Nonnegative int bit) {
        return (data & (1 << bit)) != 0;
    }

    public static byte setBit(byte data, @Nonnegative int bit) {
        return (byte) (data | (1 << bit));
    }

    public static byte setBit(byte data, @Nonnegative int bit, boolean value) {
        if (value)
            return setBit(data, bit);
        else
            return data;
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, "IpmiHeader");
        depth++;
        appendValue(buf, depth, "IpmiPayloadType", getPayloadType());
        appendValue(buf, depth, "IpmiCommand", getCommandName());
        appendValue(buf, depth, "SourceAddress", "0x" + UnsignedBytes.toString(getSourceAddress(), 16));
        appendValue(buf, depth, "SourceLun", getSourceLun());
        appendValue(buf, depth, "TargetAddress", "0x" + UnsignedBytes.toString(getTargetAddress(), 16));
        appendValue(buf, depth, "TargetLun", getTargetLun());
        appendValue(buf, depth, "SequenceNumber", getSequenceNumber());
    }
}
