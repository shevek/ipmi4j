/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;

/**
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

    @Override
    public int getWireLength() {
        return getIpmiSessionWrapper().getIpmiSessionHeader(this).getWireLength()
                + getIpmiHeader().getWireLength()
                + getDataWireLength()
                + getIpmiSessionWrapper().getIpmiSessionTrailer(this).getWireLength();
    }

    /** Serializes the IPMI data into this RMCP data. */
    protected abstract void toWireData(@Nonnull ByteBuffer buffer);

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        getIpmiSessionWrapper().getIpmiSessionHeader(this).toWire(buffer);
        getIpmiHeader().toWire(buffer);
        toWireData(buffer);
        getIpmiSessionWrapper().getIpmiSessionTrailer(this).toWire(buffer);
    }

    protected abstract void fromWireData(@Nonnull ByteBuffer buffer);

    @Override
    protected void fromWireUnchecked(ByteBuffer buffer) {
        getIpmiSessionWrapper().getIpmiSessionHeader(this).fromWire(buffer);
        getIpmiHeader().fromWire(buffer);
        fromWireData(buffer);
        getIpmiSessionWrapper().getIpmiSessionTrailer(this).fromWire(buffer);
    }
}
