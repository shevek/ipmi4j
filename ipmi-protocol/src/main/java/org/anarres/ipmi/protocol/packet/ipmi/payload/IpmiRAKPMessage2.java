/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.util.UUID;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.packet.asf.AsfRsspSessionStatus;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.client.session.IpmiPacketContext;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;

/**
 * [IPMI2] Section 13.21 page 151.
 *
 * @author shevek
 */
public class IpmiRAKPMessage2 extends AbstractTaggedIpmiPayload {

    private AsfRsspSessionStatus statusCode;
    public int consoleSessionId;
    private byte[] systemRandom;   // length = 16
    private UUID systemGuid;
    private byte[] keyExchangeAuthenticationCode;

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.RAKPMessage2;
    }

    @Override
    public Class<? extends AbstractTaggedIpmiPayload> getRequestType() {
        return IpmiRAKPMessage1.class;
    }

    @Override
    public Class<? extends AbstractTaggedIpmiPayload> getResponseType() {
        return IpmiRAKPMessage2.class;
    }

    @Override
    public void apply(IpmiClientIpmiPayloadHandler handler, IpmiHandlerContext context, IpmiSession session) {
        handler.handleRAKPMessage2(context, session, this);
    }

    @Override
    public int getWireLength(IpmiPacketContext context) {
        return 40 + keyExchangeAuthenticationCode.length;
    }

    @Override
    protected void toWireUnchecked(IpmiPacketContext context, ByteBuffer buffer) {
        buffer.put(messageTag);
        buffer.put(statusCode.getCode());
        buffer.putChar((char) 0);    // reserved
        toWireIntLE(buffer, consoleSessionId);
        buffer.put(systemRandom);
        toWireUUIDLE(buffer, systemGuid);
        buffer.put(keyExchangeAuthenticationCode);
    }

    @Override
    protected void fromWireUnchecked(IpmiPacketContext context, ByteBuffer buffer) {
        messageTag = buffer.get();
        statusCode = Code.fromBuffer(AsfRsspSessionStatus.class, buffer);
        assertWireBytesZero(buffer, 2);
        consoleSessionId = fromWireIntLE(buffer);
        systemRandom = readBytes(buffer, 16);
        systemGuid = fromWireUUIDLE(buffer);
        // IpmiSession session = context.getIpmiSession(consoleSessionId);
        // session.getAuthenticationAlgorithm().getHashLength();
        keyExchangeAuthenticationCode = readBytes(buffer, buffer.remaining());
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        appendValue(buf, depth, "MessageTag", "0x" + UnsignedBytes.toString(messageTag, 16));
        appendValue(buf, depth, "ConsoleSessionId", "0x" + Integer.toHexString(consoleSessionId));
        appendValue(buf, depth, "SystemRandom", toHexString(systemRandom));
        appendValue(buf, depth, "SystemGUID", systemGuid);
        appendValue(buf, depth, "KeyExchangeAuthenticationCode", toHexString(keyExchangeAuthenticationCode));
    }
}
