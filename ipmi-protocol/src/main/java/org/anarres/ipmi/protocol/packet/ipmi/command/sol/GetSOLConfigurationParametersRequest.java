/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sol;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiSessionRequest;

/**
 * [IPMI2] Section 26.3, table 26-4, page 376.
 *
 * @author shevek
 */
public class GetSOLConfigurationParametersRequest extends AbstractIpmiSessionRequest {

    private byte channelNumber;
    private boolean revisionOnly;
    private byte parameterSelector;
    private byte setSelector;
    private byte blockSelector;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSOLConfigurationParameters;
    }

    @Override
    protected int getDataWireLength() {
        return 4;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        byte tmp = 0;
        setBit(tmp, 7, revisionOnly);
        tmp &= (channelNumber & 0xF);
        buffer.put(tmp);
        buffer.put(parameterSelector);
        buffer.put(setSelector);
        buffer.put(blockSelector);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        byte tmp = buffer.get();
        revisionOnly = getBit(tmp, 7);
        channelNumber = (byte) (tmp & 0xF);
        parameterSelector = buffer.get();
        setSelector = buffer.get();
        blockSelector = buffer.get();
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "ChannelNumber", "0x" + Integer.toHexString(channelNumber));
        appendValue(buf, depth, "RevisionOnly", revisionOnly);
        appendValue(buf, depth, "ParameterSelector", "0x" + Integer.toHexString(parameterSelector));
        appendValue(buf, depth, "SetSelector", "0x" + Integer.toHexString(setSelector));
        appendValue(buf, depth, "BlockSelector", "0x" + Integer.toHexString(blockSelector));
    }
}
