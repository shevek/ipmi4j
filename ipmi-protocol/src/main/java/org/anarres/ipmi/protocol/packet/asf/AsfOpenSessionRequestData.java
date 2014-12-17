/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * OpenSessionRequest.
 * 
 * [ASF2] Section 3.2.4.11 page 41.
 *
 * @author shevek
 */
public class AsfOpenSessionRequestData extends AbstractAsfData {

    private int consoleSessionId;
    private final List<AsfRsspSessionAuthentication.AuthenticationAlgorithm> authenticationAlgorithms = new ArrayList<>();
    private final List<AsfRsspSessionAuthentication.IntegrityAlgorithm> integrityAlgorithms = new ArrayList<>();

    @Override
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.OpenSessionRequest;
    }

    public int getConsoleSessionId() {
        return consoleSessionId;
    }

    @Nonnull
    public AsfOpenSessionRequestData withConsoleSessionId(int consoleSessionId) {
        this.consoleSessionId = consoleSessionId;
        return this;
    }

    @Override
    protected int getDataWireLength() {
        return 4
                + authenticationAlgorithms.size() * AsfRsspSessionAuthentication.AuthenticationAlgorithm.LENGTH
                + integrityAlgorithms.size() * AsfRsspSessionAuthentication.IntegrityAlgorithm.LENGTH
                + AsfRsspSessionAuthentication.EndOfList.LENGTH;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.putInt(getConsoleSessionId());
        for (AsfRsspSessionAuthentication.AuthenticationAlgorithm authenticationAlgorithm : authenticationAlgorithms)
            authenticationAlgorithm.toWire(buffer);
        for (AsfRsspSessionAuthentication.IntegrityAlgorithm integrityAlgorithm : integrityAlgorithms)
            integrityAlgorithm.toWire(buffer);
        AsfRsspSessionAuthentication.EndOfList.INSTANCE.toWire(buffer);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        withConsoleSessionId(buffer.getInt());
        while (buffer.hasRemaining()) {
            AsfRsspSessionAuthentication.Payload payload = AsfRsspSessionAuthentication.fromWire(buffer);
            if (payload instanceof AsfRsspSessionAuthentication.AuthenticationAlgorithm)
                authenticationAlgorithms.add((AsfRsspSessionAuthentication.AuthenticationAlgorithm) payload);
            else if (payload instanceof AsfRsspSessionAuthentication.IntegrityAlgorithm)
                integrityAlgorithms.add((AsfRsspSessionAuthentication.IntegrityAlgorithm) payload);
            else if (payload instanceof AsfRsspSessionAuthentication.EndOfList)
                break;
        }
    }
}
