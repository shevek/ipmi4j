/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientAsfMessageHandler;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * RAKPMessage3.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.15 page 43.
 *
 * @author shevek
 */
public class AsfRAKPMessage3Data extends AbstractAsfData {

    private AsfRsspSessionStatus status = AsfRsspSessionStatus.NO_ERROR;
    private int clientSessionId;

    @Nonnull
    public AsfRsspSessionStatus getStatus() {
        return status;
    }

    @Nonnull
    public AsfRAKPMessage3Data withStatus(@Nonnull AsfRsspSessionStatus status) {
        this.status = status;
        return this;
    }

    public int getClientSessionId() {
        return clientSessionId;
    }

    @Nonnull
    public AsfRAKPMessage3Data withClientSessionId(int clientSessionId) {
        this.clientSessionId = clientSessionId;
        return this;
    }

    @Override
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.RAKPMessage3;
    }

    @Override
    public void apply(IpmiClientAsfMessageHandler handler) {
        handler.handleAsfRAKPMessage3Data(this);
    }

    @Override
    protected int getDataWireLength() {
        if (!AsfRsspSessionStatus.NO_ERROR.equals(getStatus()))
            return 8;
        byte[] integrityData = null;
        return 8 + integrityData.length;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        AsfRsspSessionStatus s = getStatus();
        buffer.put(s.getCode());
        buffer.put(new byte[3]);  // Reserved
        buffer.putInt(getClientSessionId());
        if (!AsfRsspSessionStatus.NO_ERROR.equals(s))
            return;
        byte[] integrityData = null;
        buffer.put(integrityData);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        AsfRsspSessionStatus s = Code.fromBuffer(AsfRsspSessionStatus.class, buffer);
        withStatus(s);
        assertWireBytesZero(buffer, 3);
        withClientSessionId(buffer.getInt());
        if (!AsfRsspSessionStatus.NO_ERROR.equals(s))
            return;
        byte[] integrityData = null;
        buffer.get(integrityData);
    }
}
