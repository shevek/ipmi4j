/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * OpenSessionRequest.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.11 page 41.
 *
 * @author shevek
 */
public class AsfOpenSessionRequestData extends AbstractAsfData {

    private int consoleSessionId;
    private final List<AsfRsspSessionAuthenticationPayload.AuthenticationAlgorithm> authenticationAlgorithms = new ArrayList<>();
    private final List<AsfRsspSessionAuthenticationPayload.IntegrityAlgorithm> integrityAlgorithms = new ArrayList<>();

    @Override
    public AsfRcmpMessageType getMessageType() {
        return AsfRcmpMessageType.OpenSessionRequest;
    }

    public int getConsoleSessionId() {
        return consoleSessionId;
    }

    public void setConsoleSessionId(int consoleSessionId) {
        this.consoleSessionId = consoleSessionId;
    }

    @Override
    protected int getDataWireLength() {
        return 4
                + authenticationAlgorithms.size() * AsfRsspSessionAuthenticationPayload.AuthenticationAlgorithm.LENGTH
                + integrityAlgorithms.size() * AsfRsspSessionAuthenticationPayload.IntegrityAlgorithm.LENGTH
                + AsfRsspSessionAuthenticationPayload.EndOfList.LENGTH;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.putInt(getConsoleSessionId());
        for (AsfRsspSessionAuthenticationPayload.AuthenticationAlgorithm authenticationAlgorithm : authenticationAlgorithms)
            authenticationAlgorithm.toWire(buffer);
        for (AsfRsspSessionAuthenticationPayload.IntegrityAlgorithm integrityAlgorithm : integrityAlgorithms)
            integrityAlgorithm.toWire(buffer);
        AsfRsspSessionAuthenticationPayload.EndOfList.INSTANCE.toWire(buffer);
    }
}
