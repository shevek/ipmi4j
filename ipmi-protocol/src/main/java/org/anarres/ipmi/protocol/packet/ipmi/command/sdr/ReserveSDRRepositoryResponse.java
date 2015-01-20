/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;

/**
 * [IPMI2] Section 33.11, table 33-5, page 443.
 *
 * @author shevek
 */
public class ReserveSDRRepositoryResponse extends AbstractIpmiResponse {

    public char reservationId;

    @Override
    protected int getResponseDataWireLength() {
        return 3;
    }

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.ReserveSDRRepository;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        toWireCharLE(buffer, reservationId);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        reservationId = fromWireCharLE(buffer);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "ReservationId", "0x" + Integer.toHexString(reservationId));
    }
}
