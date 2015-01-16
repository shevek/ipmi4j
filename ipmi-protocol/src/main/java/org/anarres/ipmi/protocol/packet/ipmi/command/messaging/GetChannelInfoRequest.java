/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.messaging;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelNumber;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiSessionRequest;

/**
 * [IPMI2] Section 22.24, table 22-28, page 305.
 *
 * @author shevek
 */
public class GetChannelInfoRequest extends AbstractIpmiSessionRequest {

    public IpmiChannelNumber channelNumber;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetChannelInfoCommand;
    }

    @Override
    protected int getDataWireLength() {
        return 1;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(channelNumber.getCode());
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        channelNumber = Code.fromBuffer(IpmiChannelNumber.class, buffer);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "ChannelNumber", channelNumber);
    }
}
