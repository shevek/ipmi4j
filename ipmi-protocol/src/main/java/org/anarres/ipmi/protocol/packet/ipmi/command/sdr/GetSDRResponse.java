/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.IpmiClientCommandHandler;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;

/**
 * [IPMI2] Section 33.12, table 33-6, page 444.
 *
 * @author shevek
 */
public class GetSDRResponse extends AbstractIpmiResponse {

    public char nextRecordId;
    public byte[] recordData;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSDR;
    }

    @Override
    public void apply(IpmiClientCommandHandler handler) {
        handler.handleGetSDRResponse(this);
    }

    @Override
    protected int getResponseDataWireLength() {
        return 3 + recordData.length;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        toWireCharLE(buffer, nextRecordId);
        buffer.put(recordData);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        nextRecordId = fromWireCharLE(buffer);
        recordData = readBytes(buffer, buffer.remaining());
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "NextRecordId", "0x" + Integer.toHexString(nextRecordId));
        appendValue(buf, depth, "RecordData", toHexString(recordData));
    }
}
