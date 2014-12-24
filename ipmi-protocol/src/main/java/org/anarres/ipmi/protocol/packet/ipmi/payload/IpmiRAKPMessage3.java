/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.asf.AsfRsspSessionStatus;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 13.22 page 152.
 *
 * @author shevek
 */
public class IpmiRAKPMessage3 extends AbstractIpmiPayload {

    private byte messageTag;
    private AsfRsspSessionStatus statusCode;
    private int systemSessionId;
    private byte[] keyExchangeAuthenticationCode;

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.RAKPMessage3;
    }

    @Override
    public int getWireLength() {
        return 8 + keyExchangeAuthenticationCode.length;
    }

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        buffer.put(messageTag);
        buffer.put(statusCode.getCode());
        buffer.putChar((char) 0);    // reserved
        buffer.putInt(systemSessionId);
        buffer.put(keyExchangeAuthenticationCode);
    }

    @Override
    protected void fromWireUnchecked(ByteBuffer buffer) {
        messageTag = buffer.get();
        statusCode = Code.fromBuffer(AsfRsspSessionStatus.class, buffer);
        assertWireCharReserved(buffer, 0);
        systemSessionId = buffer.getInt();
        throw new UnsupportedOperationException("keyExchangeAuthenticationCode");
    }
}
