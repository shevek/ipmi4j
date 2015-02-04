/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;

/**
 * [IPMI2] Section 33.12, table 33-6, page 444.
 *
 * @author shevek
 */
public class GetSDRRequest extends AbstractIpmiRequest {

    public char reservationId;
    public char recordId;
    /** Unsigned. */
    public byte recordOffset;
    /** Unsigned. 0xff = all */
    public byte recordLength;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSDR;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiHandlerContext context) {
        handler.handleGetSDRRequest(context, this);
    }

    @Override
    protected int getDataWireLength() {
        return 6;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        toWireCharLE(buffer, reservationId);
        toWireCharLE(buffer, recordId);
        buffer.put(recordOffset);
        buffer.put(recordLength);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        reservationId = fromWireCharLE(buffer);
        recordId = fromWireCharLE(buffer);
        recordOffset = buffer.get();
        recordLength = buffer.get();
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "ReservationId", "0x" + Integer.toHexString(reservationId));
        appendValue(buf, depth, "RecordId", "0x" + Integer.toHexString(recordId));
        appendValue(buf, depth, "RecordOffset", "0x" + UnsignedBytes.toString(recordOffset, 16));
        appendValue(buf, depth, "RecordLength", "0x" + UnsignedBytes.toString(recordLength, 16));
    }
}
