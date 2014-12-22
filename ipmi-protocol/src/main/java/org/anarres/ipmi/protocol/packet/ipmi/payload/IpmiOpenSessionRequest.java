/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiPayloadType;
import org.anarres.ipmi.protocol.packet.ipmi.alg.IpmiAlgorithmUtils;
import org.anarres.ipmi.protocol.packet.ipmi.alg.IpmiAuthenticationAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.alg.IpmiConfidentialityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.alg.IpmiIntegrityAlgorithm;

/**
 *
 * @author shevek
 */
public class IpmiOpenSessionRequest extends IpmiPayload {

    private byte messageTag;
    private RequestedMaximumPrivilegeLevel requestedMaximumPrivilegeLevel;
    private int consoleSessionId;
    private IpmiAuthenticationAlgorithm authenticationAlgorithm;
    private IpmiIntegrityAlgorithm integrityAlgorithm;
    private IpmiConfidentialityAlgorithm confidentialityAlgorithm;

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.RMCPOpenSessionRequest;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(messageTag);
        buffer.put(Bits.toByte(requestedMaximumPrivilegeLevel));
        buffer.putChar((char) 0);   // reserved
        buffer.putInt(consoleSessionId);
        IpmiAlgorithmUtils.toWireUnchecked(buffer, authenticationAlgorithm);
        IpmiAlgorithmUtils.toWireUnchecked(buffer, integrityAlgorithm);
        IpmiAlgorithmUtils.toWireUnchecked(buffer, confidentialityAlgorithm);
    }
}
