/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * A LUN, between 0 and 3.
 *
 * [IPMI2] Section 13.8, page 137.
 *
 * @author shevek
 */
public enum IpmiLun implements Code.Wrapper {

    L0(0), L1(1), L2(2), L3(3);
    public static final int MASK = 0x03;
    private final byte code;

    /**
     * @throws IllegalArgumentException if value is not between 0 and 3, inclusive.
     */
    private IpmiLun(@Nonnegative int value) {
        this.code = Bits.validate(MASK, value);
    }

    @Override
    public byte getCode() {
        return code;
    }

    public int getValue() {
        return getCode() & MASK;
    }
}