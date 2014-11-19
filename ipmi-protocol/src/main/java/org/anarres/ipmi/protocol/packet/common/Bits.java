/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

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

    /** Implement this, probably in an enum. */
    public static interface Wrapper {

        @Nonnull
        public Bits getBits();
    }

    /** Serializer. */
    public static byte toByte(@Nonnull Iterable<? extends Wrapper> wrappers) {
        byte data = 0;
        for (Wrapper wrapper : wrappers)
            if (wrapper != null)
                data = wrapper.getBits().set(data);
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
    public static Bits forBitIndex(@Nonnegative int byteIndex, @Nonnegative int bitIndex) {
        return new Bits(byteIndex, 1 << bitIndex, 1 << bitIndex);
    }

    /** Convenience constructor. */
    @Nonnull
    @Deprecated
    public static Bits forBitValues(@Nonnegative int byteIndex, @Nonnull Map<Integer, Boolean> bitValues) {
        Preconditions.checkArgument(!bitValues.isEmpty(), "No bit values.");
        int byteMask = 0;
        int byteValue = 0;
        for (Map.Entry<Integer, Boolean> e : bitValues.entrySet()) {
            int b = 1 << e.getKey();
            byteMask |= b;
            if (e.getValue())
                byteValue |= b;
        }
        return new Bits(byteIndex, byteMask, byteValue);
    }

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
