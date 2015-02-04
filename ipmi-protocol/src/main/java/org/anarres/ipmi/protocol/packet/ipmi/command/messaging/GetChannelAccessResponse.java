/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.messaging;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.RequestedMaximumPrivilegeLevel;

/**
 * [IPMI2] Section 22.23, table 22-27, page 304.
 *
 * @author shevek
 */
public class GetChannelAccessResponse extends AbstractIpmiResponse {

    public enum AccessMode implements Code.Wrapper {

        Disabled(0),
        PreBootOnly(1),
        AlwaysAvailable(2),
        Shared(3);
        private final byte code;
        /* pp */ AccessMode(int code) {
            this.code = UnsignedBytes.checkedCast(code);
        }

        @Override
        public byte getCode() {
            return code;
        }
    }
    public boolean alertingDisabled;
    public boolean perMessageAuthenticationDisabled;
    public boolean userLevelAuthenticationDisabled;
    public AccessMode accessMode;
    public RequestedMaximumPrivilegeLevel channelPrivilegeLevel;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetChannelAccess;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiHandlerContext context) {
        handler.handleGetChannelAccessResponse(context, this);
    }

    @Override
    protected int getResponseDataWireLength() {
        return 3;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        byte tmp = accessMode.getCode();
        tmp = setBit(tmp, 5, alertingDisabled);
        tmp = setBit(tmp, 4, perMessageAuthenticationDisabled);
        tmp = setBit(tmp, 3, userLevelAuthenticationDisabled);
        buffer.put(tmp);
        buffer.put(channelPrivilegeLevel.getCode());
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        byte tmp = buffer.get();
        alertingDisabled = getBit(tmp, 5);
        perMessageAuthenticationDisabled = getBit(tmp, 4);
        userLevelAuthenticationDisabled = getBit(tmp, 3);
        accessMode = Code.fromInt(AccessMode.class, tmp & 0x7);
        channelPrivilegeLevel = Code.fromBuffer(RequestedMaximumPrivilegeLevel.class, buffer);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "AlertingDisabled", alertingDisabled);
        appendValue(buf, depth, "PerMessageAuthenticationDisabled", perMessageAuthenticationDisabled);
        appendValue(buf, depth, "UserLevelAuthenticationDisabled", userLevelAuthenticationDisabled);
        appendValue(buf, depth, "AccessMode", accessMode);
        appendValue(buf, depth, "ChannelPrivilegeLevel", channelPrivilegeLevel);
    }
}