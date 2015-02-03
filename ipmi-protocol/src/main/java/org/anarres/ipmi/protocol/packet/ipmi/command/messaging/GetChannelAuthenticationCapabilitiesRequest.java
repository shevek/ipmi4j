/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.messaging;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.IpmiClientCommandHandler;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelNumber;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelPrivilegeLevel;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;

/**
 * [IPMI2] Section 22.13, table 22-15, page 283.
 *
 * @author shevek
 */
public class GetChannelAuthenticationCapabilitiesRequest extends AbstractIpmiRequest {

    public boolean extendedCapabilities;
    public IpmiChannelNumber channelNumber = IpmiChannelNumber.CURRENT;
    public IpmiChannelPrivilegeLevel channelPrivilegeLevel;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetChannelAuthenticationCapabilities;
    }

    @Override
    public void apply(IpmiClientCommandHandler handler) {
        handler.handleGetChannelAuthenticationCapabilitiesRequest(this);
    }

    @Override
    public int getDataWireLength() {
        return 2;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        byte b = channelNumber.getCode();
        // If we fail with extendedCapabilities, we might have to try again without.
        b = setBit(b, 7, extendedCapabilities);
        buffer.put(b);
        buffer.put(channelPrivilegeLevel.getCode());
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        byte b = buffer.get();
        channelNumber = Code.fromByte(IpmiChannelNumber.class, (byte) (b & 0xF));
        extendedCapabilities = getBit(b, 7);
        channelPrivilegeLevel = Code.fromByte(IpmiChannelPrivilegeLevel.class, buffer.get());
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "ChannelNumber", channelNumber);
        appendValue(buf, depth, "ExtendedCapabilities", extendedCapabilities);
        appendValue(buf, depth, "ChannelPrivilegeLevel", channelPrivilegeLevel);
    }
}
