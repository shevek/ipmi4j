/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.AbstractIpmiSessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiLun;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiNetworkFunction;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAuthenticationCapabilitiesRequest;
import org.anarres.ipmi.protocol.packet.ipmi.payload.AbstractIpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [IPMI2] Section 13.8, figure 13-4, page 136.
 *
 * @author shevek
 */
public abstract class AbstractIpmiCommand extends AbstractIpmiPayload implements IpmiCommand {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractIpmiCommand.class);
    public static final int SEQUENCE_NUMBER_MASK = 0x3F;
    private byte targetAddress;
    private IpmiLun targetLun = IpmiLun.L0;
    private byte sourceAddress;
    private IpmiLun sourceLun = IpmiLun.L0;
    private int sequenceNumber;

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.IPMI;
    }

    public byte getTargetAddress() {
        return targetAddress;
    }

    public IpmiLun getTargetLun() {
        return targetLun;
    }

    @Nonnull
    public AbstractIpmiCommand withTarget(byte targetAddress, IpmiLun targetLun) {
        this.targetAddress = targetAddress;
        this.targetLun = targetLun;
        return this;
    }

    @Nonnull
    public AbstractIpmiCommand withTarget(int targetAddress, IpmiLun targetLun) {
        return withTarget(UnsignedBytes.checkedCast(targetAddress), targetLun);
    }

    public byte getSourceAddress() {
        return sourceAddress;
    }

    public IpmiLun getSourceLun() {
        return sourceLun;
    }

    @Nonnull
    public AbstractIpmiCommand withSource(byte sourceAddress, IpmiLun sourceLun) {
        this.sourceAddress = sourceAddress;
        this.sourceLun = sourceLun;
        return this;
    }

    @Nonnull
    public AbstractIpmiCommand withSource(int sourceAddress, IpmiLun sourceLun) {
        return withSource(UnsignedBytes.checkedCast(sourceAddress), sourceLun);
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Nonnull
    public abstract IpmiCommandName getCommandName();

    @Override
    public int getWireLength(IpmiContext context) {
        return 6
                + getDataWireLength()
                + 1;    // data checksum.
    }

    /** Returns the length of the ASF data part of this packet. */
    @Nonnegative
    protected abstract int getDataWireLength();

    @Override
    protected void toWireUnchecked(IpmiContext context, ByteBuffer buffer) {
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
    protected void fromWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        int chk1Start = buffer.position();
        byte tmp;
        targetAddress = buffer.get();
        tmp = buffer.get();
        IpmiNetworkFunction networkFunction = Code.fromInt(IpmiNetworkFunction.class, (tmp >>> 2) & ~1);
        targetLun = Code.fromInt(IpmiLun.class, tmp & IpmiLun.MASK);
        fromWireChecksum(buffer, chk1Start, "IPMI header checksum");
        int chk2Start = buffer.position();
        sourceAddress = buffer.get();
        tmp = buffer.get();
        sequenceNumber = (tmp >>> 2) & SEQUENCE_NUMBER_MASK;
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

    public static void fromWireChecksum(@Nonnull ByteBuffer buffer, @Nonnegative int start, @Nonnull String description) {
        byte expect = toChecksum(buffer, start);
        byte actual = buffer.get();
        if (expect != actual)
            throw new IllegalArgumentException("Checkum failure: " + description
                    + ": expected=" + UnsignedBytes.toString(expect, 16)
                    + " actual=" + UnsignedBytes.toString(actual, 16));
    }

    protected static void reverse(byte[] b) {
        Collections.reverse(Arrays.asList(b));
    }

    /** [IPMI2] Section 20.8, table 20-10, page 252. */
    public static void toWireUUIDLE(@Nonnull ByteBuffer buf, @Nonnull UUID uuid) {
        byte[] lsb = Longs.toByteArray(uuid.getLeastSignificantBits());
        reverse(lsb);
        buf.put(lsb);

        byte[] msb = Longs.toByteArray(uuid.getMostSignificantBits());
        reverse(msb);
        buf.put(msb);
    }

    /** [IPMI2] Section 20.8, table 20-10, page 252. */
    @Nonnull
    public static UUID fromWireUUIDLE(@Nonnull ByteBuffer buf) {
        byte[] uuid = readBytes(buf, 16);
        long msb = Longs.fromBytes(uuid[15], uuid[14], uuid[13], uuid[12], uuid[11], uuid[10], uuid[9], uuid[8]);
        long lsb = Longs.fromBytes(uuid[7], uuid[6], uuid[5], uuid[4], uuid[3], uuid[2], uuid[1], uuid[0]);
        return new UUID(msb, lsb);
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

    public static void toWireIntLE(@Nonnull ByteBuffer buf, int data) {
        buf.put((byte) (data));
        buf.put((byte) (data >> 8));
        buf.put((byte) (data >> 16));
        buf.put((byte) (data >> 24));
    }

    public static int fromWireIntLE(@Nonnull ByteBuffer buf) {
        byte b0 = buf.get();
        byte b1 = buf.get();
        byte b2 = buf.get();
        byte b3 = buf.get();
        return Ints.fromBytes(b3, b2, b1, b0);
    }

    public static void toWireCharLE(@Nonnull ByteBuffer buf, char data) {
        buf.put((byte) (data));
        buf.put((byte) (data >> 8));
    }

    public static char fromWireCharLE(@Nonnull ByteBuffer buf) {
        byte b0 = buf.get();
        byte b1 = buf.get();
        return Chars.fromBytes(b1, b0);
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
}
