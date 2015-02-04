/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.fru;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;

/**
 * [IPMI2] Section 34.1, table 34-2, page 449.
 *
 * @author shevek
 */
public class GetFRUInventoryAreaInfoResponse extends AbstractIpmiResponse {

    public enum AccessMode {

        Bytes, Words;
    }
    public char fruInventoryAreaSizeInBytes;
    public AccessMode accessMode = AccessMode.Bytes;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetFRUInventoryAreaInfo;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiHandlerContext context) {
        handler.handleGetFRUInventoryAreaInfoResponse(context, this);
    }

    @Override
    protected int getResponseDataWireLength() {
        return 4;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        toWireCharLE(buffer, fruInventoryAreaSizeInBytes);
        byte tmp = 0;
        tmp = setBit(tmp, 7, accessMode == AccessMode.Words);
        buffer.put(tmp);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        fruInventoryAreaSizeInBytes = fromWireCharLE(buffer);
        byte tmp = buffer.get();
        accessMode = getBit(tmp, 7) ? AccessMode.Words : AccessMode.Bytes;
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "FruInventoryAreaSizeInBytes", (int) fruInventoryAreaSizeInBytes);
        appendValue(buf, depth, "AccessMode", accessMode);
    }
}
