/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

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

    private AsfRsspSessionStatus status = AsfRsspSessionStatus.NO_ERROR;
    private int consoleSessionId;
    private int clientSessionId;
    private AsfRsspSessionAuthentication.AuthenticationAlgorithm authenticationAlgorithm;
    private AsfRsspSessionAuthentication.IntegrityAlgorithm integrityAlgorithm;

    @Nonnull
    public AsfRsspSessionStatus getStatus() {
        return status;
    }

    @Nonnull
    public AsfOpenSessionResponseData withStatus(@Nonnull AsfRsspSessionStatus status) {
        this.status = status;
        return this;
    }

    public int getConsoleSessionId() {
        return consoleSessionId;
    }

    @Nonnull
    public AsfOpenSessionResponseData withConsoleSessionId(int consoleSessionId) {
        this.consoleSessionId = consoleSessionId;
        return this;
    }

    public int getClientSessionId() {
        return clientSessionId;
    }

    public AsfOpenSessionResponseData withClientSessionId(int clientSessionId) {
        this.clientSessionId = clientSessionId;
        return this;
    }

    @Override
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.OpenSessionResponse;
    }

    @Override
    protected int getDataWireLength() {
        return 28;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(status.getCode());
        buffer.putChar((char) 0);    // Reserved, zero
        buffer.putInt(getConsoleSessionId());
        buffer.putInt(getClientSessionId());
        authenticationAlgorithm.toWire(buffer);
        integrityAlgorithm.toWire(buffer);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        withStatus(Code.fromBuffer(AsfRsspSessionStatus.class, buffer));
        assertWireChar(buffer, (char) 0, "reserved bytes");
        withConsoleSessionId(buffer.getInt());
        withClientSessionId(buffer.getInt());
        authenticationAlgorithm = (AsfRsspSessionAuthentication.AuthenticationAlgorithm) AsfRsspSessionAuthentication.fromWire(buffer);
        integrityAlgorithm = (AsfRsspSessionAuthentication.IntegrityAlgorithm) AsfRsspSessionAuthentication.fromWire(buffer);
    }
}
