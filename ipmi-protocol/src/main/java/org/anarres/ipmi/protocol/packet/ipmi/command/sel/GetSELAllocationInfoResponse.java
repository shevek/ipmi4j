/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sel;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;

/**
 * [IPMI2] Section 31.3, table 31-3, page 424.
 *
 * @author shevek
 */
public class GetSELAllocationInfoResponse extends AbstractIpmiResponse {

    public char allocationUnitsPossible;
    public char allocationUnitSizeInBytes;
    public char allocationUnitsFree;
    public char largestFreeBlockInUnits;
    /** unsigned */
    public byte maximumRecordSizeInUnits;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSELAllocationInfo;
    }

    @Override
    protected int getResponseDataWireLength() {
        return 10;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        toWireCharLE(buffer, allocationUnitsPossible);
        toWireCharLE(buffer, allocationUnitSizeInBytes);
        toWireCharLE(buffer, allocationUnitsFree);
        toWireCharLE(buffer, largestFreeBlockInUnits);
        buffer.put(maximumRecordSizeInUnits);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        allocationUnitsPossible = fromWireCharLE(buffer);
        allocationUnitSizeInBytes = fromWireCharLE(buffer);
        allocationUnitsFree = fromWireCharLE(buffer);
        largestFreeBlockInUnits = fromWireCharLE(buffer);
        maximumRecordSizeInUnits = buffer.get();
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "AllocationUnitsPossible", (int) allocationUnitsPossible);
        appendValue(buf, depth, "AllocationUnitSizeInBytes", (int) allocationUnitSizeInBytes);
        appendValue(buf, depth, "AllocationUnitsFree", (int) allocationUnitsFree);
        appendValue(buf, depth, "LargestFreeBlockInUnits", (int) largestFreeBlockInUnits);
        appendValue(buf, depth, "MaximumRecordSizeInUnits", UnsignedBytes.toInt(maximumRecordSizeInUnits));
    }
}
