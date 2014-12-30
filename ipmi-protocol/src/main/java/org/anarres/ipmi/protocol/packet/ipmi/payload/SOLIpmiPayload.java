/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;

/**
 * [IPMI2] Section 15.9, page 210.
 *
 * @author shevek
 */
public class SOLIpmiPayload extends AbstractIpmiPayload {

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.SOL;
    }

    @Override
    public int getWireLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void fromWireUnchecked(ByteBuffer buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}