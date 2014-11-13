/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;

/**
 *
 * @author shevek
 */
public class AsfOpenSessionResponseData extends AbstractAsfData {

    private AsfRsspSessionStatus status;
    private int consoleSessionId;
    private int clientSessionId;

    @Override
    public AsfRcmpMessageType getMessageType() {
        return AsfRcmpMessageType.OpenSessionResponse;
    }

    @Override
    protected int getDataWireLength() {
        return 28;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(status.getCode());
        buffer.put(new byte[2]);    // Reserved, zero
    }
}
