/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.IpmiClientPayloadHandler;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiContext;

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
    public void apply(IpmiClientPayloadHandler handler) {
        handler.handleSOL(this);
    }

    @Override
    public int getWireLength(IpmiContext context) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void toWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void fromWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}