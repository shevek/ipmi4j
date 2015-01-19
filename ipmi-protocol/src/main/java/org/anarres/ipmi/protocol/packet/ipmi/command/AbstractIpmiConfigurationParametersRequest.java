/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelNumber;

/**
 *
 * @author shevek
 */
public abstract class AbstractIpmiConfigurationParametersRequest extends AbstractIpmiRequest {

    private IpmiChannelNumber channelNumber;
    private boolean revisionOnly;
    private byte parameterSelector;
    private byte setSelector;
    private byte blockSelector;

    @Override
    protected int getDataWireLength() {
        return 4;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        byte tmp = channelNumber.getCode();
        tmp = setBit(tmp, 7, revisionOnly);
        buffer.put(tmp);
        buffer.put(parameterSelector);
        buffer.put(setSelector);
        buffer.put(blockSelector);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        byte tmp = buffer.get();
        revisionOnly = getBit(tmp, 7);
        channelNumber = Code.fromByte(IpmiChannelNumber.class, (byte) (tmp & 0xF));
        parameterSelector = buffer.get();
        setSelector = buffer.get();
        blockSelector = buffer.get();
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "ChannelNumber", channelNumber);
        appendValue(buf, depth, "RevisionOnly", revisionOnly);
        appendValue(buf, depth, "ParameterSelector", "0x" + Integer.toHexString(parameterSelector));
        appendValue(buf, depth, "SetSelector", "0x" + Integer.toHexString(setSelector));
        appendValue(buf, depth, "BlockSelector", "0x" + Integer.toHexString(blockSelector));
    }
}
