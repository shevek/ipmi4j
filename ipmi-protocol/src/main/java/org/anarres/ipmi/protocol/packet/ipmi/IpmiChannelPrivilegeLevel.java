/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.payload.RequestedMaximumPrivilegeLevel;

/**
 * [IPMI2] Section 6.8, table 6-5, page 51.
 * Referenced in [IPMI2] Appendix G, pages 591-596.
 * Codes from [IPMI2] Section 22.13, table 22-15, page 283.
 *
 * @see RequestedMaximumPrivilegeLevel
 * @author shevek
 */
public enum IpmiChannelPrivilegeLevel implements Code.Wrapper {

    Unprotected(0),
    Callback(1),
    User(2), Operator(3), Administrator(4), OEM(5);
    private final byte code;

    private IpmiChannelPrivilegeLevel(@Nonnegative int code) {
        this.code = UnsignedBytes.checkedCast(code);
    }

    @Override
    public byte getCode() {
        return code;
    }
}
