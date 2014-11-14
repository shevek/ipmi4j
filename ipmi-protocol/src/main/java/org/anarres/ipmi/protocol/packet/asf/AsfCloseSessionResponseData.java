/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;

/**
 * CloseSessionResponse.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.7 page 40.
 *
 * @author shevek
 */
public class AsfCloseSessionResponseData extends AbstractAsfData {

    private AsfRsspSessionStatus status;

    @Override
    public AsfRcmpMessageType getMessageType() {
        return AsfRcmpMessageType.CloseSessionResponse;
    }

    @Override
    protected int getDataWireLength() {
        return 1;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(status.getCode());
    }
}
