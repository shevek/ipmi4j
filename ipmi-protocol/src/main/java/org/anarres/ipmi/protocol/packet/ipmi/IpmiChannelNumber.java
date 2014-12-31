/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 22.13, table 22-15, page 283.
 *
 * @author shevek
 */
public enum IpmiChannelNumber implements Code.Wrapper {

    C0(0x0), C1(0x1), C2(0x2), C3(0x3), C4(0x4), C5(0x5), C6(0x6), C7(0x7), C8(0x8), C9(0x9), CA(0xA), CB(0xB), CURRENT(0xE), CF(0xF);
    private final byte code;

    private IpmiChannelNumber(@Nonnegative int code) {
        this.code = Bits.validate(0xF, code);
    }

    @Override
    public byte getCode() {
        return code;
    }
}
