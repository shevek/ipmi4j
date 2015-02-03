/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.fru;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.IpmiClientCommandHandler;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;

/**
 * [IPMI2] Section 34.2, table 34-3, page 450.
 *
 * @author shevek
 */
public class ReadFRUDataRequest extends AbstractIpmiRequest {

    public byte fruDeviceId;
    public char fruInventoryOffset;
    /** Unsigned. */
    public byte fruInventoryLength;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.ReadFRUData;
    }

    @Override
    public void apply(IpmiClientCommandHandler handler) {
        handler.handleReadFRUDataRequest(this);
    }

    @Override
    protected int getDataWireLength() {
        return 4;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(fruDeviceId);
        toWireCharLE(buffer, fruInventoryOffset);
        buffer.put(fruInventoryLength);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        fruDeviceId = buffer.get();
        fruInventoryOffset = fromWireCharLE(buffer);
        fruInventoryLength = buffer.get();
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "FruDeviceId", "0x" + UnsignedBytes.toString(fruDeviceId, 16));
        appendValue(buf, depth, "FruInventoryOffset", "0x" + Integer.toHexString(fruInventoryOffset));
        appendValue(buf, depth, "FruInventoryLength", "0x" + Integer.toHexString(fruInventoryLength));
    }
}
