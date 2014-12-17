/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import com.google.common.primitives.UnsignedBytes;
import org.anarres.ipmi.protocol.packet.common.Bits;

/**
 * [IPMI2] Section 13.20 page 150: {@link IpmiRAKPMessage1}.
 * [IPMI2] Section 13.17 page 147: {@link IpmiOpenSessionRequest}.
 */
public enum RequestedMaximumPrivilegeLevel implements Bits.Wrapper {

    /** IpmiOpenSessionRequest only. [IPMI2] Section 13.17 page 147. */
    MAXIMUM_ALLOWED_BY_CIPHER(0),
    CALLBACK(1),
    USER(2),
    OPERATOR(3),
    ADMINISTRATOR(4),
    OEM(5);
    private final Bits bits;

    private RequestedMaximumPrivilegeLevel(int code) {
        this.bits = new Bits(0, UnsignedBytes.checkedCast(code), 0x0F);
    }

    @Override
    public Bits getBits() {
        return bits;
    }
}
