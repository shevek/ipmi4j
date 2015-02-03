/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.fru;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;
import org.anarres.ipmi.protocol.client.session.IpmiSession;

/**
 * [IPMI2] Section 34.1, table 34-2, page 449.
 *
 * @author shevek
 */
public class GetFRUInventoryAreaInfoRequest extends AbstractIpmiRequest {

    public byte fruDeviceId;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetFRUInventoryAreaInfo;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiSession session) {
        handler.handleGetFRUInventoryAreaInfoRequest(session, this);
    }

    @Override
    protected int getDataWireLength() {
        return 1;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(fruDeviceId);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        fruDeviceId = buffer.get();
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "FruDeviceId", "0x" + UnsignedBytes.toString(fruDeviceId, 16));
    }
}