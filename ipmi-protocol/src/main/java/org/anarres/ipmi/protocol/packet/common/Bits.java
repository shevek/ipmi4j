/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import java.util.Map;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public class Bits {

    public static interface Wrapper {

        @Nonnull
        public Bits getBits();
    }

    public static byte toByte(@Nonnull Iterable<? extends Wrapper> wrappers) {
        byte data = 0;
        for (Wrapper wrapper : wrappers)
            data = wrapper.getBits().set(data);
        return data;
    }

    @Nonnull
    public static byte[] toBytes(@Nonnegative int length, @Nonnull Iterable<? extends Wrapper> wrappers) {
        byte[] data = new byte[length];
        for (Wrapper wrapper : wrappers)
            wrapper.getBits().set(data);
        return data;
    }

    @Nonnull
    public static Bits forBitIndex(@Nonnegative int byteIndex, int bitIndex) {
        return new Bits(byteIndex, 1 << bitIndex, 1 << bitIndex);
    }

    @Nonnull
    public static Bits forBitValues(@Nonnegative int byteIndex, @Nonnull Map<Integer, Boolean> bitValues) {
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
    private final int byteIndex;
    private final int byteMask;
    private final int byteValue;

    public Bits(int byteIndex, int byteMask, int byteValue) {
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
