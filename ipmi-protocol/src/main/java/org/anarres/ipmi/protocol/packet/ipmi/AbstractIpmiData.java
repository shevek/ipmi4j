/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;

/**
 * [IPMI2] Section 13.6, page 132, table 13-8.
 *
 * Hands the entire show off to the {@link IpmiSessionWrapper} for encoding.
 *
 * @author shevek
 */
public abstract class AbstractIpmiData extends AbstractWireable implements IpmiData {

    protected IpmiSessionWrapper ipmiSessionWrapper;
    protected final IpmiHeader ipmiHeader = new IpmiHeader();

    @Nonnull
    public IpmiSessionWrapper getIpmiSessionWrapper() {
        return ipmiSessionWrapper;
    }

    @Override
    public IpmiHeader getIpmiHeader() {
        return ipmiHeader;
    }

    @Nonnull
    public IpmiSessionData getIpmiSessionData() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getWireLength() {
        return getIpmiSessionWrapper().getWireLength(getIpmiHeader(), getIpmiSessionData());
    }

    /** Serializes the IPMI data into this RMCP data. */
    protected abstract void toWireData(@Nonnull ByteBuffer buffer);

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        getIpmiSessionWrapper().toWire(buffer, getIpmiHeader(), getIpmiSessionData());
    }

    protected abstract void fromWireData(@Nonnull ByteBuffer buffer);

    @Override
    protected void fromWireUnchecked(ByteBuffer buffer) {
        getIpmiSessionWrapper().fromWire(buffer, getIpmiHeader(), getIpmiSessionData());
    }
}
