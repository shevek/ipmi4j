/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.message;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.util.UUID;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiLun;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiNetworkFunction;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [IPMI2] Section 13.8, figure 13-4, page 136.
 *
 * @author shevek
 */
public abstract class AbstractIpmiMessage extends AbstractWireable implements IpmiMessage {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractIpmiMessage.class);
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
    public AbstractIpmiMessage withTarget(byte targetAddress, IpmiLun targetLun) {
        this.targetAddress = targetAddress;
        this.targetLun = targetLun;
        return this;
    }

    @Nonnull
    public AbstractIpmiMessage withTarget(int targetAddress, IpmiLun targetLun) {
        return withTarget(UnsignedBytes.checkedCast(targetAddress), targetLun);
    }

    public byte getSourceAddress() {
        return sourceAddress;
    }

    public IpmiLun getSourceLun() {
        return sourceLun;
    }

    @Nonnull
    public AbstractIpmiMessage withSource(byte sourceAddress, IpmiLun sourceLun) {
        this.sourceAddress = sourceAddress;
        this.sourceLun = sourceLun;
        return this;
    }

    @Nonnull
    public AbstractIpmiMessage withSource(int sourceAddress, IpmiLun sourceLun) {
        return withSource(UnsignedBytes.checkedCast(sourceAddress), sourceLun);
    }

    public int getSequenceNumber() {
        return sequenceNumber;
    }

    @Nonnull
    public abstract IpmiCommand getCommand();

    @Override
    public int getWireLength() {
        return 6
                + getDataWireLength()
                + 1;    // data checksum.
    }

    /** Returns the length of the ASF data part of this packet. */
    @Nonnegative
    protected abstract int getDataWireLength();

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        int chk1Start = buffer.position();
        buffer.put(getTargetAddress());
        buffer.put((byte) (getCommand().getNetworkFunction().getCode() << 2 | getTargetLun().getValue()));
        AbstractIpmiMessage.toWireChecksum(buffer, chk1Start);
        int chk2Start = buffer.position();
        buffer.put(getSourceAddress());
        int seq = getSequenceNumber() & SEQUENCE_NUMBER_MASK;
        buffer.put((byte) (seq << 2 | getSourceLun().getValue()));
        buffer.put(getCommand().getCode());
        toWireData(buffer);
        toWireChecksum(buffer, chk2Start);
    }

    /** Serializes the ASF data into this RMCP data. */
    protected abstract void toWireData(@Nonnull ByteBuffer buffer);

    @Override
    protected void fromWireUnchecked(ByteBuffer buffer) {
        int chk1Start = buffer.position();
        byte tmp;
        targetAddress = buffer.get();
        tmp = buffer.get();
        IpmiNetworkFunction networkFunction = Code.fromInt(IpmiNetworkFunction.class, tmp >>> 2);
        targetLun = Code.fromInt(IpmiLun.class, tmp & IpmiLun.MASK);
        AbstractIpmiMessage.fromWireChecksum(buffer, chk1Start, "IPMI header checksum");
        int chk2Start = buffer.position();
        sourceAddress = buffer.get();
        tmp = buffer.get();
        sequenceNumber = (tmp >>> 2) & SEQUENCE_NUMBER_MASK;
        sourceLun = Code.fromInt(IpmiLun.class, tmp & IpmiLun.MASK);
        // command = IpmiCommand.fromByte(networkFunction, buffer.get());
        fromWireData(buffer);
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

    /** [IPMI2] Section 20.8, table 20-10, page 252. */
    @Nonnull
    public UUID fromWireUUIDLE(@Nonnull ByteBuffer buf) {
        byte[] uuid = readBytes(buf, 16);
        long msb = Longs.fromBytes(uuid[15], uuid[14], uuid[13], uuid[12], uuid[11], uuid[10], uuid[9], uuid[8]);
        long lsb = Longs.fromBytes(uuid[7], uuid[6], uuid[5], uuid[4], uuid[3], uuid[2], uuid[1], uuid[0]);
        return new UUID(msb, lsb);
    }

    /**
     * @see GetChannelAuthenticationCapabilitiesRequest
     */
    public int fromWireOemIanaLE3(@Nonnull ByteBuffer buf) {
        byte b0 = buf.get();
        byte b1 = buf.get();
        byte b2 = buf.get();
        return Ints.fromBytes((byte) 0, b2, b1, b0);
    }
}
