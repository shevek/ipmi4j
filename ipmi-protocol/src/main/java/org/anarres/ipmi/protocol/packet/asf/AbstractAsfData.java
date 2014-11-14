/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpData;

/**
 * ASF RMCP Data (Enterprise number 0x4542).
 *
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.2.3 page 22.
 * 
 * This class manages both the RMCP payload and the ASF payload.
 * Implementors desiring to provide the ASF payload should override
 * {@link #getDataWireLength()} and * {@link #toWireData(ByteBuffer)}
 * and ignore the RMCP wrapper.
 *
 * @author shevek
 */
public abstract class AbstractAsfData extends AbstractWireable implements RmcpData {

    public static final int IANA_ENTERPRISE_NUMBER = 4542;
    // Page 33
    private byte messageTag;    // matches request/response

    // Page 22
    @Nonnull
    public abstract AsfRcmpMessageType getMessageType();

    @Override
    public int getWireLength() {
        return 0
                + 4 // ianaEnterpriseNumber
                + 1 // message type
                + 1 // message tag
                + 1 // data length
                + getDataWireLength();
    }

    /** Returns the length of the ASF data part of this packet. */
    @Nonnegative
    protected abstract int getDataWireLength();

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        int start = buffer.position();
        int length = getWireLength();
        buffer.putInt(IANA_ENTERPRISE_NUMBER);
        buffer.put((byte) getMessageType().value);
        buffer.put(messageTag);
        buffer.put((byte) 0);   // reserved
        buffer.put(UnsignedBytes.checkedCast(getDataWireLength()));
        toWireData(buffer);
        if (buffer.position() - start != length)
            throw new IllegalStateException("Bad serializer: Wrote " + buffer.position() + " bytes, not " + length);
    }

    /** Serializes the ASF data into this RCMP data. */
    protected abstract void toWireData(@Nonnull ByteBuffer buffer);
}
