/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public abstract class AbstractWireable implements Wireable {

    protected abstract void toWireUnchecked(@Nonnull ByteBuffer buffer);

    @Override
    public void toWire(@Nonnull ByteBuffer buffer) {
        Preconditions.checkNotNull(buffer, "ByteBuffer was null.");
        int start = buffer.position();
        toWireUnchecked(buffer);

        int expectedLength = getWireLength();
        int actualLength = buffer.position() - start;
        if (actualLength != expectedLength)
            throw new IllegalStateException("Object should serialize to " + expectedLength + " bytes, but generated " + actualLength + ": " + this);
    }

    protected abstract void fromWireUnchecked(@Nonnull ByteBuffer buffer);

    @Override
    public void fromWire(ByteBuffer buffer) {
        Preconditions.checkNotNull(buffer, "ByteBuffer was null.");
        int start = buffer.position();
        fromWireUnchecked(buffer);

        int expectedLength = getWireLength();
        int actualLength = buffer.position() - start;
        if (actualLength != expectedLength)
            throw new IllegalStateException("Object should deserialize to " + expectedLength + " bytes, but generated " + actualLength + ": " + this);
    }

    @Nonnull
    protected static byte[] readBytes(@Nonnull ByteBuffer buffer, @Nonnegative int length) {
        byte[] data = new byte[length];
        buffer.get(data);
        return data;
    }

    public static void assertWireInt(@Nonnull ByteBuffer buffer, int expectValue) {
        int actualValue = buffer.getInt();
        if (actualValue != expectValue)
            throw new IllegalArgumentException("Expected 0x" + Integer.toHexString(expectValue)
                    + " but got 0x" + Integer.toHexString(actualValue));
    }

    public static void assertWireChar(@Nonnull ByteBuffer buffer, char expectValue) {
        short actualValue = buffer.get();
        if (actualValue != expectValue)
            throw new IllegalArgumentException("Expected 0x" + Integer.toHexString(expectValue)
                    + " but got 0x" + Integer.toHexString(actualValue));
    }

    public static void assertWireByte(@Nonnull ByteBuffer buffer, byte expectValue) {
        byte actualValue = buffer.get();
        if (actualValue != expectValue)
            throw new IllegalArgumentException("Expected 0x" + UnsignedBytes.toString(expectValue, 16)
                    + " but got 0x" + UnsignedBytes.toString(actualValue, 16));
    }

    public static void assertWireBytes(@Nonnull ByteBuffer buffer, @Nonnull int... expectValues) {
        for (int expectValue : expectValues)
            assertWireByte(buffer, (byte) (expectValue & 0xFF));
    }
}