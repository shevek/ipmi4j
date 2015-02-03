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
 * CloseSessionResponse.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.7 page 40.
 *
 * @author shevek
 */
public class AsfCloseSessionResponseData extends AbstractAsfData {

    private AsfRsspSessionStatus status = AsfRsspSessionStatus.NO_ERROR;

    @Nonnull
    public AsfRsspSessionStatus getStatus() {
        return status;
    }

    @Nonnull
    public AsfCloseSessionResponseData withStatus(@Nonnull AsfRsspSessionStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.CloseSessionResponse;
    }

    @Override
    public void apply(IpmiClientAsfMessageHandler handler) {
        handler.handleAsfCloseSessionResponseData(this);
    }

    @Override
    protected int getDataWireLength() {
        return 1;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(status.getCode());
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        withStatus(Code.fromBuffer(AsfRsspSessionStatus.class, buffer));
    }
}
