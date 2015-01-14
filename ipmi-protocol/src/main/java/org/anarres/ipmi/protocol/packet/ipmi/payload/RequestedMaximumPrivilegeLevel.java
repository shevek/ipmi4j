/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 13.20 page 150: {@link IpmiRAKPMessage1}.
 * [IPMI2] Section 13.17 page 147: {@link IpmiOpenSessionRequest}.
 */
public enum RequestedMaximumPrivilegeLevel implements Bits.Wrapper, Code.Wrapper {

    /** IpmiOpenSessionRequest only. [IPMI2] Section 13.17 page 147. */
    MAXIMUM_ALLOWED_BY_CIPHER(0),
    CALLBACK(1),
    USER(2),
    OPERATOR(3),
    ADMINISTRATOR(4),
    OEM(5);
    public static final int MASK = 0x0F;
    private final Bits bits;

    private RequestedMaximumPrivilegeLevel(@Nonnegative int code) {
        // Do NOT use the MASK constant here - it's initialized AFTER the enum constants.
        this.bits = new Bits(0, 0xF, UnsignedBytes.checkedCast(code));
    }

    @Override
    public Bits getBits() {
        return bits;
    }

    @Override
    public byte getCode() {
        return UnsignedBytes.checkedCast(getBits().getByteValue());
    }
}
