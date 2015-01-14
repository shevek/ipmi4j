/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.common;

import com.google.common.base.Preconditions;
import com.google.common.primitives.Chars;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiContext;

/**
 *
 * @author shevek
 */
public abstract class AbstractWireable implements Wireable {

    protected abstract void toWireUnchecked(@Nonnull IpmiContext context, @Nonnull ByteBuffer buffer);

    @Override
    public void toWire(@Nonnull IpmiContext context, @Nonnull ByteBuffer buffer) {
        Preconditions.checkNotNull(buffer, "ByteBuffer was null.");
        int start = buffer.position();
        toWireUnchecked(context, buffer);

        int expectedLength = getWireLength(context);
        int actualLength = buffer.position() - start;
        if (actualLength != expectedLength)
            throw new IllegalStateException("Object should serialize to " + expectedLength + " bytes, but generated " + actualLength + ": " + this);
    }

    protected abstract void fromWireUnchecked(@Nonnull IpmiContext context, @Nonnull ByteBuffer buffer);

    @Override
    public void fromWire(@Nonnull IpmiContext context, ByteBuffer buffer) {
        Preconditions.checkNotNull(buffer, "ByteBuffer was null.");
        int start = buffer.position();
        fromWireUnchecked(context, buffer);

        int expectedLength = getWireLength(context);
        int actualLength = buffer.position() - start;
        if (actualLength != expectedLength)
            throw new IllegalStateException("Object should deserialize from " + expectedLength + " bytes, but consumed " + actualLength + ": " + this.getClass() + ": " + this);
    }

    /** Reads an array of bytes from the wire and returns them. */
    @Nonnull
    public static byte[] readBytes(@Nonnull ByteBuffer buffer, @Nonnegative int length) {
        byte[] data = new byte[length];
        buffer.get(data);
        return data;
    }

    // TODO: Take a 'description' message too.
    /** Reads a 4-byte signed int from the wire, and asserts it equal to the given expected value. */
    public static void assertWireInt(@Nonnull ByteBuffer buffer, int expectValue, String description) {
        int actualValue = buffer.getInt();
        if (actualValue != expectValue)
            throw new IllegalArgumentException("In " + description + ": "
                    + "Expected 0x" + Integer.toHexString(expectValue)
                    + " but got 0x" + Integer.toHexString(actualValue));
    }

    /** Reads a 2-byte unsigned char from the wire, and asserts it equal to the given expected value. */
    public static void assertWireChar(@Nonnull ByteBuffer buffer, char expectValue, String description) {
        short actualValue = buffer.get();
        if (actualValue != expectValue)
            throw new IllegalArgumentException("In " + description + ": "
                    + "Expected 0x" + Integer.toHexString(expectValue)
                    + " but got 0x" + Integer.toHexString(actualValue));
    }

    /** Reads a byte from the wire, and asserts it equal to the given expected value. */
    public static void assertWireByte(@Nonnull ByteBuffer buffer, byte expectValue, @Nonnull String description) {
        byte actualValue = buffer.get();
        if (actualValue != expectValue)
            throw new IllegalArgumentException("In " + description + ": "
                    + "Expected 0x" + UnsignedBytes.toString(expectValue, 16)
                    + " but got 0x" + UnsignedBytes.toString(actualValue, 16));
    }

    /** Reads a number of bytes from the wire, and asserts them equal to the given expected values. */
    public static void assertWireBytes(@Nonnull ByteBuffer buffer, @Nonnull int... expectValues) {
        for (int expectValue : expectValues)
            assertWireByte(buffer, (byte) (expectValue & 0xFF), "data byte at offset " + buffer.position());
    }

    /** Reads a number of zero bytes from the buffer. */
    public static void assertWireBytesZero(@Nonnull ByteBuffer buffer, @Nonnegative int count) {
        for (int i = 0; i < count; i++)
            assertWireByte(buffer, (byte) 0, "data byte at offset " + buffer.position() + " (reserved byte " + i + ")");
    }

    protected int assertMask(int value, int nbits) {
        if (Integer.numberOfLeadingZeros(value) < Integer.SIZE - nbits)
            throw new IllegalArgumentException("Too many bits in " + Integer.toHexString(value) + "; expected max " + nbits);
        return value;
    }

    public static long fromWireLongLE(@Nonnull ByteBuffer buffer) {
        byte b0 = buffer.get();
        byte b1 = buffer.get();
        byte b2 = buffer.get();
        byte b3 = buffer.get();
        byte b4 = buffer.get();
        byte b5 = buffer.get();
        byte b6 = buffer.get();
        byte b7 = buffer.get();
        return Longs.fromBytes(b7, b6, b5, b4, b3, b2, b1, b0);
    }

    public static void toWireLongLE(@Nonnull ByteBuffer buffer, long c) {
        buffer.put((byte) (c));
        buffer.put((byte) (c >> 8));
        buffer.put((byte) (c >> 16));
        buffer.put((byte) (c >> 24));
        buffer.put((byte) (c >> 32));
        buffer.put((byte) (c >> 40));
        buffer.put((byte) (c >> 48));
        buffer.put((byte) (c >> 56));
    }

    public static void toWireIntLE(@Nonnull ByteBuffer buffer, int data) {
        buffer.put((byte) (data));
        buffer.put((byte) (data >> 8));
        buffer.put((byte) (data >> 16));
        buffer.put((byte) (data >> 24));
    }

    public static int fromWireIntLE(@Nonnull ByteBuffer buffer) {
        byte b0 = buffer.get();
        byte b1 = buffer.get();
        byte b2 = buffer.get();
        byte b3 = buffer.get();
        return Ints.fromBytes(b3, b2, b1, b0);
    }

    public static void toWireCharLE(@Nonnull ByteBuffer buffer, char data) {
        buffer.put((byte) (data));
        buffer.put((byte) (data >> 8));
    }

    public static char fromWireCharLE(@Nonnull ByteBuffer buffer) {
        byte b0 = buffer.get();
        byte b1 = buffer.get();
        return Chars.fromBytes(b1, b0);
    }

    @Nonnull
    protected static StringBuilder indent(@Nonnull StringBuilder buf, int depth) {
        for (int i = 0; i < depth; i++)
            buf.append("  ");
        return buf;
    }

    @Nonnull
    protected static StringBuilder appendHeader(@Nonnull StringBuilder buf, int depth, String name) {
        indent(buf, depth);
        buf.append(name).append(":").append('\n');
        return buf;
    }

    @Nonnull
    protected static StringBuilder appendValue(@Nonnull StringBuilder buf, int depth, String name, Object value) {
        indent(buf, depth);
        buf.append(name).append(": ").append(value).append('\n');
        return buf;
    }

    @Nonnull
    protected static StringBuilder appendChild(@Nonnull StringBuilder buf, int depth, @Nonnull String name, @CheckForNull Wireable value) {
        appendHeader(buf, depth, name);
        if (value == null)
            indent(buf, depth).append("null\n");
        else
            value.toStringBuilder(buf, depth + 1);
        return buf;
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        indent(buf, depth).append(super.toString()).append('\n');
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        toStringBuilder(buf, 0);
        return buf.toString();
    }
}