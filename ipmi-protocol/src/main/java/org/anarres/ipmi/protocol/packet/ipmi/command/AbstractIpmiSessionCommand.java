/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import com.google.common.primitives.UnsignedBytes;

/**
 *
 * @author shevek
 */
public abstract class AbstractIpmiSessionCommand extends AbstractIpmiCommand {

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendValue(buf, depth, "IpmiPayloadType", getPayloadType());
        appendValue(buf, depth, "IpmiCommand", getCommandName());
        appendValue(buf, depth, "SourceAddress", "0x" + UnsignedBytes.toString(getSourceAddress(), 16));
        appendValue(buf, depth, "SourceLun", getSourceLun());
        appendValue(buf, depth, "TargetAddress", "0x" + UnsignedBytes.toString(getTargetAddress(), 16));
        appendValue(buf, depth, "TargetLun", getTargetLun());
    }
}
