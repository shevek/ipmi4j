/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;

/**
 * OpenSessionResponse.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.6 page 40.
 *
 * @author shevek
 */
public class AsfOpenSessionResponseData extends AbstractAsfData {

    private AsfRsspSessionStatus status;
    private int consoleSessionId;
    private int clientSessionId;
    private AsfRsspSessionAuthenticationPayload.AuthenticationAlgorithm authenticationAlgorithm;
    private AsfRsspSessionAuthenticationPayload.IntegrityAlgorithm integrityAlgorithm;

    @Override
    public AsfRcmpMessageType getMessageType() {
        return AsfRcmpMessageType.OpenSessionResponse;
    }

    public int getConsoleSessionId() {
        return consoleSessionId;
    }

    public void setConsoleSessionId(int consoleSessionId) {
        this.consoleSessionId = consoleSessionId;
    }

    public int getClientSessionId() {
        return clientSessionId;
    }

    public void setClientSessionId(int clientSessionId) {
        this.clientSessionId = clientSessionId;
    }

    @Override
    protected int getDataWireLength() {
        return 28;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(status.getCode());
        buffer.put(new byte[2]);    // Reserved, zero
        buffer.putInt(getConsoleSessionId());
        buffer.putInt(getClientSessionId());
        authenticationAlgorithm.toWire(buffer);
        integrityAlgorithm.toWire(buffer);
    }
}
