/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;

/**
 * CloseSessionRequest.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.12 page 41.
 *
 * @author shevek
 */
public class AsfCloseSessionRequestData extends AbstractAsfData {

    @Override
    public AsfRcmpMessageType getMessageType() {
        return AsfRcmpMessageType.CloseSessionRequest;
    }

    @Override
    protected int getDataWireLength() {
        return 0;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
    }
}