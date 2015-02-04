/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.sel.GetSELInfoResponse;

/**
 * [IPMI2] Section 33.9, table 33-3, page 441.
 *
 * @see GetSELInfoResponse
 * @author shevek
 */
public class GetSDRRepositoryInfoResponse extends AbstractIpmiResponse {

    public static enum SupportedOperation implements Bits.Wrapper {

        OverflowFlag(Bits.forBitIndex(0, 7)),
        // TODO -> two bits?
        RepositoryUpdateModalityUnspecified(new Bits(0, 0b11 << 5, 0b00 << 5)),
        RepositoryUpdateModalityNonModal(new Bits(0, 0b11 << 5, 0b01 << 5)),
        RepositoryUpdateModalityModal(new Bits(0, 0b11 << 5, 0b10 << 5)),
        RepositoryUpdateModalityBoth(new Bits(0, 0b11 << 5, 0b11 << 5)),
        DeleteSDRCommandSupported(Bits.forBitIndex(0, 3)),
        PartialAddSDRCommandSupported(Bits.forBitIndex(0, 2)),
        ReserveSDRRepositoryCommandSupported(Bits.forBitIndex(0, 1)),
        GetSDRRepositoryAllocationInformationCommandSupported(Bits.forBitIndex(0, 0));
        private final Bits bits;

        /* pp */ SupportedOperation(@Nonnull Bits bits) {
            this.bits = bits;
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }
    public char recordCount;
    public char freeSpace;
    public int mostRecentAdditionTimestamp;
    public int mostRecentDeletionTimestamp;
    public Set<SupportedOperation> supportedOperations = EnumSet.noneOf(SupportedOperation.class);

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSDRRepositoryInfo;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiHandlerContext context) {
        handler.handleGetSDRRepositoryInfoResponse(context, this);
    }

    @Override
    protected int getResponseDataWireLength() {
        return 15;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        buffer.put((byte) 0x51);
        toWireCharLE(buffer, recordCount);
        toWireCharLE(buffer, freeSpace);
        toWireIntLE(buffer, mostRecentAdditionTimestamp);
        toWireIntLE(buffer, mostRecentDeletionTimestamp);
        buffer.put(Bits.toByte(supportedOperations));
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        assertWireByte(buffer, (byte) 0x51, "SDR Version");
        recordCount = fromWireCharLE(buffer);
        freeSpace = fromWireCharLE(buffer);
        mostRecentAdditionTimestamp = fromWireIntLE(buffer);
        mostRecentDeletionTimestamp = fromWireIntLE(buffer);
        supportedOperations = Bits.fromBuffer(SupportedOperation.class, buffer, 1);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "RecordCount", (int) recordCount);
        appendValue(buf, depth, "FreeSpace", "0x" + Integer.toHexString(freeSpace));
        appendValue(buf, depth, "MostRecentAdditionTimestamp", "0x" + Integer.toHexString(mostRecentAdditionTimestamp));
        appendValue(buf, depth, "MostRecentDeletionTimestamp", "0x" + Integer.toHexString(mostRecentDeletionTimestamp));
        appendValue(buf, depth, "SupportedOperations", supportedOperations);
    }
}