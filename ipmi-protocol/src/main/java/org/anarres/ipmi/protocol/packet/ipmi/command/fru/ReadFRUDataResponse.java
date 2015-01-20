/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.fru;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;

/**
 * [IPMI2] Section 34.2, table 34-3, page 450.
 *
 * @author shevek
 */
public class ReadFRUDataResponse extends AbstractIpmiResponse {

    /** Unsigned. */
    public byte fruInventoryLength;
    private byte[] fruData;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.ReadFRUData;
    }

    @Override
    protected int getResponseDataWireLength() {
        return 2 + fruData.length;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        buffer.put(fruInventoryLength);
        buffer.put(fruData);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        fruInventoryLength = buffer.get();
        fruData = readBytes(buffer, buffer.remaining());
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "FruInventoryLength", "0x" + Integer.toHexString(fruInventoryLength));
        appendValue(buf, depth, "FruInventoryData", toHexString(fruData));
    }
}
