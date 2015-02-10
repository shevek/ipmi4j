/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import com.google.common.base.Charsets;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.client.session.IpmiContext;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;

/**
 * [IPMI2] Section 13.20 page 150.
 *
 * @author shevek
 */
public class IpmiRAKPMessage1 extends AbstractTaggedIpmiPayload {

    public static enum PrivilegeLookupMode implements Bits.Wrapper, Code.Wrapper {

        /**
         * Both the Requested Privilege Level and User Name are used to look up password/key.
         * The BMC will search the user entries
         * starting with USER ID 1 and use the first entry that matches the specified user
         * name and has a Maximum Privilege Level that matches the Requested
         * Privilege Level. This can be used in combination with ‘null’ user names to
         * enable a “role only” login with a password that is just associated with the
         * requested privilege level.
         */
        USERNAME_PRIVILEGE(new Bits(0, 1 << 4, 0)),
        /**
         * User Name alone is used to look up password/key.
         * Privilege Level field acts as a ‘Maximum Requested Privilege Level’ as
         * in IPMI v1.5.
         * <ul>
         * <li>If the Requested Privilege Level is greater than the privilege limit for the
         * channel/user, the user will be allowed to connect but will be restricted to the
         * channel/user privilege limit that was configured for the user.</li>
         * <li>If the Requested Privilege Level is less than the channel/user privilege limit,
         * the user will be allowed to connect and Request Privilege Level will become
         * the effective privilege limit for the user. I.e. the user will not be able to raise
         * their privilege level higher than the Requested Privilege Level.</li>
         * </ul>
         */
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
    private int systemSessionId;
    private byte[] consoleRandom;   // length = 16
    private RequestedMaximumPrivilegeLevel requestedMaximumPrivilegeLevel = RequestedMaximumPrivilegeLevel.ADMINISTRATOR;
    private PrivilegeLookupMode privilegeLookupMode = PrivilegeLookupMode.USERNAME_PRIVILEGE;
    private String username;

    public IpmiRAKPMessage1() {
    }

    public IpmiRAKPMessage1(@Nonnull IpmiSession session) {
        this.systemSessionId = session.getSystemSessionId();
        byte[] tmp = new byte[16];
        session.getRandom().nextBytes(tmp);
        this.consoleRandom = tmp;
    }

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.RAKPMessage1;
    }

    @Override
    public void apply(IpmiClientIpmiPayloadHandler handler, IpmiHandlerContext context) {
        handler.handleRAKPMessage1(context, this);
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
        appendValue(buf, depth, "ConsoleRandom", toHexString(consoleRandom));
        appendValue(buf, depth, "RequestedMaximumPrivilegeLevel", requestedMaximumPrivilegeLevel);
        appendValue(buf, depth, "PrivilegeLookupMode", privilegeLookupMode);
        appendValue(buf, depth, "Username", username);
    }
}
