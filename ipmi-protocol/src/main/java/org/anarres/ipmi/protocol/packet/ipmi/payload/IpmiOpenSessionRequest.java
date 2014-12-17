/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.ipmi.alg.IpmiAlgorithmPayload;
import org.anarres.ipmi.protocol.packet.ipmi.alg.IpmiAuthenticationAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.alg.IpmiConfidentialityAlgorithm;
import org.anarres.ipmi.protocol.packet.ipmi.alg.IpmiIntegrityAlgorithm;

/**
 *
 * @author shevek
 */
public class IpmiOpenSessionRequest extends TempIpmiWireable {

    private byte messageTag;
    private RequestedMaximumPrivilegeLevel requestedMaximumPrivilegeLevel;
    private int consoleSessionId;
    private final IpmiAlgorithmPayload<IpmiAuthenticationAlgorithm> authenticationPayload = new IpmiAlgorithmPayload<>(IpmiAuthenticationAlgorithm.class);
    private final IpmiAlgorithmPayload<IpmiIntegrityAlgorithm> integrityPayload = new IpmiAlgorithmPayload<>(IpmiIntegrityAlgorithm.class);
    private final IpmiAlgorithmPayload<IpmiConfidentialityAlgorithm> confidentialityPayload = new IpmiAlgorithmPayload<>(IpmiConfidentialityAlgorithm.class);

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(messageTag);
        buffer.put(Bits.toByte(requestedMaximumPrivilegeLevel));
        buffer.putChar((char) 0);   // reserved
        buffer.putInt(consoleSessionId);
        authenticationPayload.toWire(buffer);
        integrityPayload.toWire(buffer);
        confidentialityPayload.toWire(buffer);
    }
}
