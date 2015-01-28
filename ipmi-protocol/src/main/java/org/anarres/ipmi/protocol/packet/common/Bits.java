/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A bit-masked subset of a byte array.
 * 
 * A Bits object represents a set of bits in a byte array, using a mask and value.
 * It can query or modify a byte or byte array for the presence of the value.
 * It also supports a convenient pattern for serializing an {@link EnumSet} into a byte array.
 *
 * @author shevek
 */
public class Bits {

    private static final Logger LOG = LoggerFactory.getLogger(Bits.class);

    /** Implement this, probably in an enum. */
    public static interface Wrapper {

        @Nonnull
        public Bits getBits();
    }

    public static interface DescriptiveWrapper extends Wrapper {

        @Nonnull
        public String getDescription();
    }

    public static class Utils {

        @Nonnull
        public static <T extends Enum<T> & Bits.DescriptiveWrapper> String toString(@Nonnull T value) {
            return value.getBits() + ": " + value.getDescription();
        }
    }

    public static byte validate(int byteMask, int byteValue) {
        // LOG.info("Validate mask=" + byteMask + ", value=" + byteValue);
        Preconditions.checkArgument(byteMask != 0, "Illegal byte mask 0");
        UnsignedBytes.checkedCast(byteMask);
        if ((byteValue & byteMask) != byteValue)
            throw new IllegalArgumentException("Bad byte value extends outside mask: (" + Integer.toHexString(byteValue) + " & " + Integer.toHexString(byteMask) + ") != " + Integer.toHexString(byteValue));
        return UnsignedBytes.checkedCast(byteValue);
    }

    /** Serializer. */
    public static byte toByte(@Nonnull Iterable<? extends Wrapper> wrappers) {
        byte data = 0;
        int mask = 0;
        for (Wrapper wrapper : wrappers) {
            if (wrapper != null) {
                Bits bits = wrapper.getBits();
                if ((mask & bits.getByteMask()) != 0)
                    throw new IllegalArgumentException("Overlapping masks in " + wrappers);
                mask |= bits.getByteMask();
                data = bits.set(data);
            }
        }
        return data;
    }

    /** Serializer. */
    public static byte toByte(@Nonnull Wrapper... wrappers) {
        return toByte(Arrays.asList(wrappers));
    }

    /** Serializer. */
    public static byte toByte(@Nonnull Bits... bitses) {
        byte data = 0;
        int mask = 0;
        for (Bits bits : bitses) {
            if (bits != null) {
                if ((mask & bits.getByteMask()) != 0)
                    throw new IllegalArgumentException("Overlapping masks in " + Arrays.toString(bitses));
                mask |= bits.getByteMask();
                data = bits.set(data);
            }
        }
        return data;
    }

    /** Serializer. */
    @Nonnull
    public static byte[] toBytes(@Nonnegative int length, @Nonnull Iterable<? extends Wrapper> wrappers) {
        byte[] data = new byte[length];
        for (Wrapper wrapper : wrappers)
            if (wrapper != null)
                wrapper.getBits().set(data);
        return data;
    }

    /** Deserializer. */
    @Nonnull
    public static <T extends Enum<T> & Wrapper> Set<T> fromByte(@Nonnull Class<T> type, byte value) {
        Set<T> out = EnumSet.noneOf(type);
        for (T t : type.getEnumConstants())
            if (t.getBits().get(value))
                out.add(t);
        return out;
    }

    /** Deserializer. */
    @Nonnull
    public static <T extends Enum<T> & Wrapper> Set<T> fromBytes(@Nonnull Class<T> type, byte[] value) {
        Set<T> out = EnumSet.noneOf(type);
        for (T t : type.getEnumConstants())
            if (t.getBits().get(value))
                out.add(t);
        return out;
    }

    /** Deserializer. */
    @Nonnull
    public static <T extends Enum<T> & Wrapper> Set<T> fromBuffer(@Nonnull Class<T> type, @Nonnull ByteBuffer buffer, @Nonnegative int length) {
        Preconditions.checkPositionIndex(length, buffer.remaining(), "Insufficient bytes remaining in buffer");
        switch (length) {
            case 0:
                return Collections.emptySet();
            case 1:
                return fromByte(type, buffer.get());
            default:
                byte[] data = new byte[length];
                buffer.get(data);
                return fromBytes(type, data);
        }
    }

    /** Convenience constructor. */
    @Nonnull
    public static Bits forBitIndex(@Nonnegative int byteIndex, @Nonnegative int bitIndex, boolean bitValue) {
        return new Bits(byteIndex, 1 << bitIndex, bitValue ? 1 << bitIndex : 0);
    }

    /** Convenience constructor. */
    @Nonnull
    public static Bits forBitIndex(@Nonnegative int byteIndex, @Nonnegative int bitIndex) {
        return forBitIndex(byteIndex, bitIndex, true);
    }

    /** Convenience constructor. */
    // @Nonnull public static Bits forBitIndex(@Nonnegative int bitIndex) { return forBitIndex(0, bitIndex); }
    /**
     * Convenience constructor.
     * 
     * @param firstBitIndex The high bit index, e.g. bits 3:0 pass 3, not 0.
     */
    @Nonnull
    public static Bits forBinaryBE(@Nonnegative int byteIndex, @Nonnegative int firstBitIndex, @Nonnegative int length, int value) {
        // First bit 3, length 4 is valid.
        Preconditions.checkArgument(firstBitIndex < 8, "First bit index too large.");
        Preconditions.checkArgument(firstBitIndex - length >= -1, "Length does not fit.");
        // TODO: Slow but I can't be bothered to work out the bit shifts right now.
        // Future maintainer: If you do work out the bitshifts, note the masking in JLS section 15.19
        int byteMask = 0;
        for (int i = 0; i < length; i++) {
            int b = 1 << firstBitIndex + i;
            byteMask |= b;
        }
        if (firstBitIndex - length >= 0)
            value = value << (firstBitIndex - length + 1);
        return new Bits(byteIndex, byteMask, value);
    }
    private final int byteIndex;
    private final int byteMask;
    private final int byteValue;

    public Bits(@Nonnegative int byteIndex, int byteMask, int byteValue) {
        validate(byteMask, byteValue);
        this.byteIndex = byteIndex;
        this.byteMask = byteMask;
        this.byteValue = byteValue;
    }

    @Nonnegative
    public int getByteIndex() {
        return byteIndex;
    }

    public int getByteMask() {
        return byteMask;
    }

    public int getByteValue() {
        return byteValue;
    }

    public boolean get(byte data) {
        return (data & getByteMask()) == getByteValue();
    }

    public byte set(byte data) {
        return (byte) (data | getByteValue());
    }

    public boolean get(@Nonnull byte[] data) {
        int i = getByteIndex();
        return get(data[i]);
    }

    public void set(@Nonnull byte[] data) {
        int i = getByteIndex();
        data[i] = set(data[i]);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[").append(getByteIndex()).append(",");
        for (int i = 0; i < 7; i++) {
            int b = 1 << i;
            if ((byteMask & b) == 0)
                buf.append('_');
            else if ((byteValue & b) == 0)
                buf.append('0');
            else
                buf.append('1');
        }
        buf.append("]");
        return buf.toString();
    }
}
