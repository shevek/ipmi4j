/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.client.session.IpmiPacketContext;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;

/**
 * [IPMI2] Section 15.9, page 210.
 *
 * @author shevek
 */
public class SOLMessage extends AbstractIpmiPayload {

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.SOL;
    }

    @Override
    public void apply(IpmiClientIpmiPayloadHandler handler, IpmiHandlerContext context, IpmiSession session) {
        handler.handleSOL(context, session, this);
    }

    @Override
    public int getWireLength(IpmiPacketContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void toWireUnchecked(IpmiPacketContext context, ByteBuffer buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void fromWireUnchecked(IpmiPacketContext context, ByteBuffer buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
