/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.messaging;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelNumber;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionAuthenticationType;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;
import org.anarres.ipmi.protocol.client.session.IpmiSession;

/**
 * [IPMI2] Section 22.13, table 22-15, page 283.
 *
 * @author shevek
 */
public class GetChannelAuthenticationCapabilitiesResponse extends AbstractIpmiResponse {

    public static enum LoginStatus implements Bits.Wrapper {

        KG_STATUS_NONZERO(Bits.forBitIndex(0, 7)),
        PERMSG_AUTH_DISABLED(Bits.forBitIndex(0, 4)),
        USERLEVEL_AUTH_STATUS(Bits.forBitIndex(0, 3)),
        ANONYMOUS_LOGIN_NONNULL_USERNAMES_ENABLED(Bits.forBitIndex(0, 2)),
        ANONYMOUS_LOGIN_NULL_USERNAMES_ENABLED(Bits.forBitIndex(0, 1)),
        ANONYMOUS_LOGIN_ENABLED(Bits.forBitIndex(0, 0));
        private final Bits bits;

        private LoginStatus(Bits bits) {
            this.bits = bits;
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }

    public static enum ExtendedCapabilities implements Bits.Wrapper {

        IPMI20_CONNECTIONS_SUPPORTED(Bits.forBitIndex(0, 1)),
        IPMI15_CONNECTIONS_SUPPORTED(Bits.forBitIndex(0, 0));
        private final Bits bits;

        private ExtendedCapabilities(Bits bits) {
            this.bits = bits;
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }
    // public IpmiCompletionCode completionCode;
    public IpmiChannelNumber channelNumber;
    public Set<IpmiSessionAuthenticationType> authenticationTypes;
    public Set<LoginStatus> loginStatus;
    public Set<ExtendedCapabilities> extendedCapabilities;
    public int oemEnterpriseNumber;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetChannelAuthenticationCapabilities;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiSession session) {
        handler.handleGetChannelAuthenticationCapabilitiesResponse(session, this);
    }

    @Override
    protected int getResponseDataWireLength() {
        return 9;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer)) // 1
            return;
        buffer.put(channelNumber.getCode());    // 2
        byte b = 0;
        if (extendedCapabilities != null)
            b |= 1 << 7;
        for (IpmiSessionAuthenticationType authenticationType : authenticationTypes)
            if (authenticationType.getCode() <= 5)
                b |= 1 << authenticationType.getCode();
        buffer.put(b);  // 3
        buffer.put(Bits.toByte(loginStatus));   // 4
        buffer.put(extendedCapabilities == null ? (byte) 0 : Bits.toByte(extendedCapabilities)); // 5
        toWireOemIanaLE3(buffer, oemEnterpriseNumber);  // 6:8
        buffer.put((byte) 0);   // 9 OEM data
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))   // 1
            return;
        channelNumber = Code.fromBuffer(IpmiChannelNumber.class, buffer);   // 2
        byte b = buffer.get();  // 3
        boolean hasExtendedCapabilities = (b & (1 << 7)) != 0;
        authenticationTypes = EnumSet.noneOf(IpmiSessionAuthenticationType.class);
        for (int i = 0; i <= 5; i++)
            if ((b & (1 << i)) != 0)
                authenticationTypes.add(Code.fromInt(IpmiSessionAuthenticationType.class, i));
        loginStatus = Bits.fromBuffer(LoginStatus.class, buffer, 1);
        if (hasExtendedCapabilities)
            extendedCapabilities = Bits.fromBuffer(ExtendedCapabilities.class, buffer, 1);
        else
            extendedCapabilities = null;
        // Decode channelPrivilegeLevel.
        oemEnterpriseNumber = fromWireOemIanaLE3(buffer); // 6:8
        buffer.get();   // 9
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "ChannelNumber", channelNumber);
        appendValue(buf, depth, "AuthenticationTypes", authenticationTypes);
        appendValue(buf, depth, "LoginStatus", loginStatus);
        appendValue(buf, depth, "ExtendedCapabilities", extendedCapabilities);
        appendValue(buf, depth, "OemEnterpriseNumber", toStringOemIana(oemEnterpriseNumber));
    }
}
