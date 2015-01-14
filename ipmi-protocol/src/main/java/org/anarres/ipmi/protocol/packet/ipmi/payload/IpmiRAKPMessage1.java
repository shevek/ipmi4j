/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import com.google.common.base.Charsets;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiContext;

/**
 * [IPMI2] Section 13.20 page 150.
 *
 * @author shevek
 */
public class IpmiRAKPMessage1 extends AbstractIpmiPayload {

    public static enum PrivilegeLookupMode implements Bits.Wrapper, Code.Wrapper {

        USERNAME_PRIVILEGE(new Bits(0, 1 << 4, 0)),
        NAME_ONLY(new Bits(0, 1 << 4, 1 << 4));
        public static final int MASK = 0x0F << 4;
        private final Bits bits;

        private PrivilegeLookupMode(@Nonnull Bits bits) {
            this.bits = bits;
        }

        @Override
        public Bits getBits() {
            return bits;
        }

        @Override
        public byte getCode() {
            return UnsignedBytes.checkedCast(getBits().getByteValue());
        }
    }
    private byte messageTag;
    private int systemSessionId;
    private byte[] consoleRandom;   // length = 16
    private RequestedMaximumPrivilegeLevel requestedMaximumPrivilegeLevel;
    private PrivilegeLookupMode privilegeLookupMode;
    private String username;

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.RAKPMessage1;
    }

    @Override
    public int getWireLength(IpmiContext context) {
        return 28 + ((username == null) ? 0 : username.length());
    }

    @Override
    protected void toWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        buffer.put(messageTag);
        buffer.put(new byte[3]);    // reserved
        toWireIntLE(buffer, systemSessionId);
        buffer.put(consoleRandom);
        buffer.put(Bits.toByte(requestedMaximumPrivilegeLevel, privilegeLookupMode));
        buffer.putChar((char) 0); // reserved
        if (username != null) {
            byte[] usernameBytes = username.getBytes(Charsets.ISO_8859_1);
            buffer.put(UnsignedBytes.checkedCast(usernameBytes.length));    // Max is 0x10.
            buffer.put(usernameBytes);
        } else {
            buffer.put((byte) 0);
        }
    }

    @Override
    protected void fromWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        messageTag = buffer.get();
        assertWireBytesZero(buffer, 3);
        systemSessionId = fromWireIntLE(buffer);
        consoleRandom = readBytes(buffer, 16);
        byte requestedMaximumPrivilegeLevelByte = buffer.get();
        requestedMaximumPrivilegeLevel = Code.fromByte(RequestedMaximumPrivilegeLevel.class, (byte) (requestedMaximumPrivilegeLevelByte & RequestedMaximumPrivilegeLevel.MASK));
        privilegeLookupMode = Code.fromByte(PrivilegeLookupMode.class, (byte) (requestedMaximumPrivilegeLevelByte & PrivilegeLookupMode.MASK));
        assertWireBytesZero(buffer, 2);
        int usernameLength = UnsignedBytes.toInt(buffer.get());
        if (usernameLength > 0) {
            byte[] usernameBytes = readBytes(buffer, usernameLength);
            username = new String(usernameBytes, Charsets.ISO_8859_1);
        } else {
            username = null;
        }
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        appendValue(buf, depth, "MessageTag", "0x" + UnsignedBytes.toString(messageTag, 16));
        appendValue(buf, depth, "SystemSessionId", "0x" + Integer.toHexString(systemSessionId));
        appendValue(buf, depth, "ConsoleRandom", consoleRandom);
        appendValue(buf, depth, "RequestedMaximumPrivilegeLevel", requestedMaximumPrivilegeLevel);
        appendValue(buf, depth, "PrivilegeLookupMode", privilegeLookupMode);
        appendValue(buf, depth, "Username", username);
    }
}
