/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.packet.asf.AsfRsspSessionStatus;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.client.session.IpmiContext;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;

/**
 * [IPMI2] Section 13.22 page 152.
 *
 * @author shevek
 */
public class IpmiRAKPMessage3 extends AbstractTaggedIpmiPayload {

    private AsfRsspSessionStatus statusCode;
    private int systemSessionId;
    private byte[] keyExchangeAuthenticationCode;

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.RAKPMessage3;
    }

    @Override
    public void apply(IpmiClientIpmiPayloadHandler handler, IpmiHandlerContext context) {
        handler.handleRAKPMessage3(context, this);
    }

    @Override
    public int getWireLength(IpmiContext context) {
        return 8 + keyExchangeAuthenticationCode.length;
    }

    @Override
    protected void toWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        buffer.put(messageTag);
        buffer.put(statusCode.getCode());
        buffer.putChar((char) 0);    // reserved
        toWireIntLE(buffer, systemSessionId);
        buffer.put(keyExchangeAuthenticationCode);
    }

    @Override
    protected void fromWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        messageTag = buffer.get();
        statusCode = Code.fromBuffer(AsfRsspSessionStatus.class, buffer);
        assertWireBytesZero(buffer, 2);
        systemSessionId = fromWireIntLE(buffer);
        keyExchangeAuthenticationCode = readBytes(buffer, buffer.remaining());
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        appendValue(buf, depth, "MessageTag", "0x" + UnsignedBytes.toString(messageTag, 16));
        appendValue(buf, depth, "StatusCode", statusCode);
        appendValue(buf, depth, "SystemSessionId", "0x" + Integer.toHexString(systemSessionId));
        appendValue(buf, depth, "KeyExchangeAuthenticationCode", toHexString(keyExchangeAuthenticationCode));
    }
}
