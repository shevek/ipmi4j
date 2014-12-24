/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;

/**
 * [IPMI2] Section 13.6, page 132, table 13-8.
 *
 * Hands the entire show off to the {@link IpmiSessionWrapper} for encoding.
 *
 * @author shevek
 */
public abstract class AbstractIpmiData extends AbstractWireable implements IpmiData {

    private IpmiSessionWrapper ipmiSessionWrapper;
    private final IpmiHeader ipmiHeader = new IpmiHeader();
    private IpmiPayload ipmiPayload;

    @Nonnull
    public IpmiSessionWrapper getIpmiSessionWrapper() {
        return ipmiSessionWrapper;
    }

    @Override
    public IpmiHeader getIpmiHeader() {
        return ipmiHeader;
    }

    @Nonnull
    public IpmiPayload getIpmiPayload() {
        return ipmiPayload;
    }

    @Override
    public int getWireLength() {
        return getIpmiSessionWrapper().getWireLength(getIpmiHeader(), getIpmiPayload());
    }

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        getIpmiSessionWrapper().toWire(buffer, getIpmiHeader(), getIpmiPayload());
    }

    @Override
    protected void fromWireUnchecked(ByteBuffer buffer) {
        byte authenticationTypeByte = buffer.get(0);
        IpmiHeaderAuthenticationType authenticationType = Code.fromByte(IpmiHeaderAuthenticationType.class, authenticationTypeByte);
        if (authenticationType == IpmiHeaderAuthenticationType.RMCPP)
            ipmiSessionWrapper = new Ipmi20SessionWrapper();
        else
            ipmiSessionWrapper = new Ipmi15SessionWrapper();
        getIpmiSessionWrapper().fromWire(buffer, getIpmiHeader(), getIpmiPayload());
    }
}
