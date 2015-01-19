/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.messaging;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelNumber;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;

/**
 * [IPMI2] Section 22.15, table 22-17, page 290.
 *
 * @author shevek
 */
public class GetChannelCipherSuitesRequest extends AbstractIpmiRequest {

    public enum ListType {

        ByCipherSuite,
        Supported
    }
    public IpmiChannelNumber channelNumber;
    public IpmiPayloadType payloadType;
    public ListType listType = ListType.Supported;
    public int listIndex;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetChannelCipherSuites;
    }

    @Override
    protected int getDataWireLength() {
        return 3;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(channelNumber.getCode());
        buffer.put(payloadType.getCode());
        byte tmp = (byte) (listIndex & 0x3F);
        tmp = setBit(tmp, 7, listType == ListType.ByCipherSuite);
        buffer.put(tmp);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        channelNumber = Code.fromBuffer(IpmiChannelNumber.class, buffer);
        payloadType = Code.fromBuffer(IpmiPayloadType.class, buffer);
        byte tmp = buffer.get();
        listIndex = tmp & 0x3F;
        listType = getBit(tmp, 7) ? ListType.ByCipherSuite : ListType.Supported;
    }
}
