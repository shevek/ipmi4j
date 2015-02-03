/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.IpmiClientAsfMessageHandler;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * RAKPMessage1.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.13 page 42.
 *
 * @author shevek
 */
public class AsfRAKPMessage1Data extends AbstractAsfData {

    /** Section 3.2.4.13 page 42. */
    public static enum UserRole implements Bits.Wrapper, Code.Wrapper {

        OPERATOR(new Bits(0, 0b1111, 0b0000)),
        ADMINISTRATOR(new Bits(0, 0b1111, 0b0001));
        private final Bits bits;

        private UserRole(@Nonnull Bits bits) {
            this.bits = bits;
        }

        @Override
        public Bits getBits() {
            return bits;
        }

        @Override
        public byte getCode() {
            return (byte) getBits().getByteValue();
        }
    }
    private int clientSessionId;
    private byte[] consoleRandom;   // length = 16
    private UserRole consoleUserRole = UserRole.OPERATOR;
    private String consoleUserName;

    public int getClientSessionId() {
        return clientSessionId;
    }

    @Nonnull
    public AsfRAKPMessage1Data withClientSessionId(int clientSessionId) {
        this.clientSessionId = clientSessionId;
        return this;
    }

    @Nonnull
    public byte[] getConsoleRandom() {
        return consoleRandom;
    }

    @Nonnull
    public AsfRAKPMessage1Data withConsoleRandom(@Nonnull byte[] consoleRandom) {
        Preconditions.checkNotNull(consoleRandom, "Random must be non-null.");
        Preconditions.checkArgument(consoleRandom.length == 16, "Random length must be 16.");
        this.consoleRandom = consoleRandom;
        return this;
    }

    @Nonnull
    public UserRole getConsoleUserRole() {
        return consoleUserRole;
    }

    @Nonnull
    public AsfRAKPMessage1Data withConsoleUserRole(UserRole consoleUserRole) {
        this.consoleUserRole = consoleUserRole;
        return this;
    }

    @CheckForNull
    public String getConsoleUserName() {
        return consoleUserName;
    }

    @Nonnull
    public AsfRAKPMessage1Data withConsoleUserName(String consoleUserName) {
        this.consoleUserName = consoleUserName;
        return this;
    }

    @Override
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.RAKPMessage1;
    }

    @Override
    public void apply(IpmiClientAsfMessageHandler handler) {
        handler.handleAsfRAKPMessage1Data(this);
    }

    @Override
    protected int getDataWireLength() {
        return 40;  // I bet this is variable in reality.
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.putInt(getClientSessionId());
        buffer.put(getConsoleRandom());
        buffer.put(getConsoleUserRole().getCode());
        buffer.putShort((short) 0); // Reserved, 0

        String userName = getConsoleUserName();
        if (userName != null) {
            byte[] userNameBytes = userName.getBytes(Charsets.US_ASCII);
            buffer.put(UnsignedBytes.checkedCast(userNameBytes.length));
            buffer.put(Arrays.copyOf(userNameBytes, 16));   // XXX Wrong: It should be variable?
        } else {
            buffer.put(new byte[17]);
        }
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        withClientSessionId(buffer.getInt());
        withConsoleRandom(readBytes(buffer, 16));
        withConsoleUserRole(Code.fromBuffer(UserRole.class, buffer));
        assertWireBytesZero(buffer, 2);

        int userNameLength = UnsignedBytes.toInt(buffer.get());
        if (userNameLength > 0) {
            byte[] userNameBytes = readBytes(buffer, userNameLength);
            withConsoleUserName(new String(userNameBytes, Charsets.US_ASCII));
        }
    }
}
