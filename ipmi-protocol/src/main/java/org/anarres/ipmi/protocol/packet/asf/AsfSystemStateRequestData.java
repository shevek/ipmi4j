/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;

/**
 * SystemStateRequest.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.10 page 40.
 *
 * @author shevek
 */
public class AsfSystemStateRequestData extends AbstractAsfData {

    @Override
    public AsfRcmpMessageType getMessageType() {
        return AsfRcmpMessageType.SystemStateRequest;
    }

    @Override
    protected int getDataWireLength() {
        return 0;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
    }
}