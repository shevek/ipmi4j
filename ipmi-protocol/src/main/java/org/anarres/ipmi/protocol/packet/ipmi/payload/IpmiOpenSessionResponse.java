/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.asf.AsfRsspSessionStatus;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAlgorithmUtils;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiAuthenticationAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiConfidentialityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.security.IpmiIntegrityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [IPMI2] Section 13.18, table 13-10, page 148.
 *
 * @author shevek
 */
public class IpmiOpenSessionResponse extends AbstractIpmiPayload {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiOpenSessionResponse.class);
    private byte messageTag;
    private AsfRsspSessionStatus statusCode;
    private RequestedMaximumPrivilegeLevel requestedMaximumPrivilegeLevel;
    private int consoleSessionId;
    private int systemSessionId;
    private IpmiAuthenticationAlgorithm authenticationAlgorithm;
    private IpmiIntegrityAlgorithm integrityAlgorithm;
    private IpmiConfidentialityAlgorithm confidentialityAlgorithm;

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.RMCPOpenSessionResponse;
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
        LOG.info("Console id is " + Integer.toHexString(consoleSessionId));
        LOG.info("System id is " + Integer.toHexString(systemSessionId));
        authenticationAlgorithm = IpmiAlgorithmUtils.fromWireUnchecked(buffer, IpmiAuthenticationAlgorithm.class);
        integrityAlgorithm = IpmiAlgorithmUtils.fromWireUnchecked(buffer, IpmiIntegrityAlgorithm.class);
        confidentialityAlgorithm = IpmiAlgorithmUtils.fromWireUnchecked(buffer, IpmiConfidentialityAlgorithm.class);
    }
}
