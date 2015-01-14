/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.anarres.ipmi.protocol.packet.asf.AsfRsspSessionStatus;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiContext;

/**
 * [IPMI2] Section 13.21 page 151.
 *
 * @author shevek
 */
public class IpmiRAKPMessage2 extends AbstractIpmiPayload {

    private byte messageTag;
    private AsfRsspSessionStatus statusCode;
    private int consoleSessionId;
    private byte[] systemRandom;   // length = 16
    private UUID systemGuid;
    private byte[] keyExchangeAuthenticationCode;

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.RAKPMessage2;
    }

    @Override
    public int getWireLength(IpmiContext context) {
        return 40 + keyExchangeAuthenticationCode.length;
    }

    @Override
    protected void toWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        buffer.put(messageTag);
        buffer.put(statusCode.getCode());
        buffer.putChar((char) 0);    // reserved
        buffer.putInt(consoleSessionId);
        buffer.put(systemRandom);
        buffer.putLong(systemGuid.getMostSignificantBits());
        buffer.putLong(systemGuid.getLeastSignificantBits());
        buffer.put(keyExchangeAuthenticationCode);
    }

    @Override
    protected void fromWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        messageTag = buffer.get();
        statusCode = Code.fromBuffer(AsfRsspSessionStatus.class, buffer);
        assertWireBytesZero(buffer, 2);
        consoleSessionId = buffer.getInt();
        systemRandom = readBytes(buffer, 16);
        long systemGuidMsb = buffer.getLong();
        long systemGuidLsb = buffer.getLong();
        systemGuid = new UUID(systemGuidMsb, systemGuidLsb);
        // keyExchangeAuthenticationCode = buffer.
        throw new UnsupportedOperationException("keyExchangeAuthenticationCode");
    }
}
