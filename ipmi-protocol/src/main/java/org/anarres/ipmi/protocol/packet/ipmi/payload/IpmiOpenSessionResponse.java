/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.packet.asf.AsfRsspSessionStatus;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAlgorithmUtils;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;
import org.anarres.ipmi.protocol.client.session.IpmiContext;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [IPMI2] Section 13.18, table 13-10, page 148.
 *
 * @author shevek
 */
public class IpmiOpenSessionResponse extends AbstractTaggedIpmiPayload {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiOpenSessionResponse.class);
    private AsfRsspSessionStatus statusCode;
    private RequestedMaximumPrivilegeLevel requestedMaximumPrivilegeLevel;
    public int consoleSessionId;
    public int systemSessionId;
    private IpmiAuthenticationAlgorithm authenticationAlgorithm;
    private IpmiIntegrityAlgorithm integrityAlgorithm;
    private IpmiConfidentialityAlgorithm confidentialityAlgorithm;

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.RMCPOpenSessionResponse;
    }

    @Override
    public void apply(IpmiClientIpmiPayloadHandler handler, IpmiHandlerContext context) {
        handler.handleOpenSessionResponse(context, this);
    }

    @Override
    public int getWireLength(IpmiContext context) {
        return 36;
    }

    @Override
    protected void toWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        buffer.put(messageTag);
        buffer.put(statusCode.getCode());
        buffer.put(Bits.toByte(requestedMaximumPrivilegeLevel));
        buffer.put((byte) 0);  // reserved
        toWireIntLE(buffer, consoleSessionId);
        toWireIntLE(buffer, systemSessionId);
        IpmiAlgorithmUtils.toWireUnchecked(buffer, authenticationAlgorithm);
        IpmiAlgorithmUtils.toWireUnchecked(buffer, integrityAlgorithm);
        IpmiAlgorithmUtils.toWireUnchecked(buffer, confidentialityAlgorithm);
    }

    @Override
    protected void fromWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        messageTag = buffer.get();
        statusCode = Code.fromBuffer(AsfRsspSessionStatus.class, buffer);
        byte requestedMaximumPrivilegeLevelByte = buffer.get();
        requestedMaximumPrivilegeLevel = Code.fromByte(RequestedMaximumPrivilegeLevel.class, (byte) (requestedMaximumPrivilegeLevelByte & RequestedMaximumPrivilegeLevel.MASK));
        assertWireByte(buffer, (byte) 0, "reserved byte");
        consoleSessionId = fromWireIntLE(buffer);
        systemSessionId = fromWireIntLE(buffer);
        // LOG.info("Console id is " + Integer.toHexString(consoleSessionId));
        // LOG.info("System id is " + Integer.toHexString(systemSessionId));
        authenticationAlgorithm = IpmiAlgorithmUtils.fromWireUnchecked(buffer, IpmiAuthenticationAlgorithm.class);
        integrityAlgorithm = IpmiAlgorithmUtils.fromWireUnchecked(buffer, IpmiIntegrityAlgorithm.class);
        confidentialityAlgorithm = IpmiAlgorithmUtils.fromWireUnchecked(buffer, IpmiConfidentialityAlgorithm.class);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        appendHeader(buf, depth, getClass().getSimpleName());
        depth++;
        appendValue(buf, depth, "MessageTag", "0x" + UnsignedBytes.toString(messageTag, 16));
        appendValue(buf, depth, "StatusCode", statusCode);
        appendValue(buf, depth, "RequestedMaximumPrivilegeLevel", requestedMaximumPrivilegeLevel);
        appendValue(buf, depth, "ConsoleSessionId", "0x" + Integer.toHexString(consoleSessionId));
        appendValue(buf, depth, "SystemSessionId", "0x" + Integer.toHexString(systemSessionId));
        appendValue(buf, depth, "AuthenticationAlgorithm", authenticationAlgorithm);
        appendValue(buf, depth, "IntegrityAlgorithm", integrityAlgorithm);
        appendValue(buf, depth, "ConfidentialityAlgorithm", confidentialityAlgorithm);
    }
}
