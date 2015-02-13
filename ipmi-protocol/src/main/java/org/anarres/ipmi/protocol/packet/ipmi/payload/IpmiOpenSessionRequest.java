/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAlgorithmUtils;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;
import org.anarres.ipmi.protocol.client.session.IpmiPacketContext;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;

/**
 * [IPMI2] Section 13.17, table 13-9, page 147.
 *
 * @author shevek
 */
public class IpmiOpenSessionRequest extends AbstractTaggedIpmiPayload {

    private RequestedMaximumPrivilegeLevel requestedMaximumPrivilegeLevel;
    private int consoleSessionId;
    private IpmiAuthenticationAlgorithm authenticationAlgorithm;
    private IpmiIntegrityAlgorithm integrityAlgorithm;
    private IpmiConfidentialityAlgorithm confidentialityAlgorithm;

    public IpmiOpenSessionRequest() {
    }

    public IpmiOpenSessionRequest(@Nonnull IpmiSession session, @Nonnull RequestedMaximumPrivilegeLevel requestedMaximumPrivilegeLevel) {
        this.requestedMaximumPrivilegeLevel = requestedMaximumPrivilegeLevel;
        this.consoleSessionId = session.getConsoleSessionId();
        this.authenticationAlgorithm = session.getAuthenticationAlgorithm();
        this.integrityAlgorithm = session.getIntegrityAlgorithm();
        this.confidentialityAlgorithm = session.getConfidentialityAlgorithm();
    }

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.RMCPOpenSessionRequest;
    }

    @Override
    public Class<? extends AbstractTaggedIpmiPayload> getRequestType() {
        return IpmiOpenSessionRequest.class;
    }

    @Override
    public Class<? extends AbstractTaggedIpmiPayload> getResponseType() {
        return IpmiOpenSessionResponse.class;
    }

    @Override
    public void apply(IpmiClientIpmiPayloadHandler handler, IpmiHandlerContext context, IpmiSession session) {
        handler.handleOpenSessionRequest(context, session, this);
    }

    @Override
    public int getWireLength(IpmiPacketContext context) {
        return 32;
    }

    @Override
    protected void toWireUnchecked(IpmiPacketContext context, ByteBuffer buffer) {
        buffer.put(messageTag);
        buffer.put(Bits.toByte(requestedMaximumPrivilegeLevel));
        buffer.putChar((char) 0);   // reserved
        toWireIntLE(buffer, consoleSessionId);
        IpmiAlgorithmUtils.toWireUnchecked(buffer, authenticationAlgorithm);
        IpmiAlgorithmUtils.toWireUnchecked(buffer, integrityAlgorithm);
        IpmiAlgorithmUtils.toWireUnchecked(buffer, confidentialityAlgorithm);
    }

    @Override
    protected void fromWireUnchecked(IpmiPacketContext context, ByteBuffer buffer) {
        messageTag = buffer.get();
        byte requestedMaximumPrivilegeLevelByte = buffer.get();
        requestedMaximumPrivilegeLevel = Code.fromByte(RequestedMaximumPrivilegeLevel.class, (byte) (requestedMaximumPrivilegeLevelByte & RequestedMaximumPrivilegeLevel.MASK));
        assertWireBytesZero(buffer, 2);
        consoleSessionId = fromWireIntLE(buffer);
        authenticationAlgorithm = IpmiAlgorithmUtils.fromWireUnchecked(buffer, IpmiAuthenticationAlgorithm.class);
        integrityAlgorithm = IpmiAlgorithmUtils.fromWireUnchecked(buffer, IpmiIntegrityAlgorithm.class);
        confidentialityAlgorithm = IpmiAlgorithmUtils.fromWireUnchecked(buffer, IpmiConfidentialityAlgorithm.class);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        appendValue(buf, depth, "MessageTag", "0x" + UnsignedBytes.toString(messageTag, 16));
        appendValue(buf, depth, "RequestedMaximumPrivilegeLevel", requestedMaximumPrivilegeLevel);
        appendValue(buf, depth, "ConsoleSessionId", "0x" + Integer.toHexString(consoleSessionId));
        appendValue(buf, depth, "AuthenticationAlgorithm", authenticationAlgorithm);
        appendValue(buf, depth, "IntegrityAlgorithm", integrityAlgorithm);
        appendValue(buf, depth, "ConfidentialityAlgorithm", confidentialityAlgorithm);
    }
}
