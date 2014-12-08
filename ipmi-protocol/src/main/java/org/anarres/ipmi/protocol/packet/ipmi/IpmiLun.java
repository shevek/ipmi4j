/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.packet.common.Bits;

/**
 *
 * @author shevek
 */
public class IpmiLun {

    public static final int MASK = 0x03;
    private byte value;

    public IpmiLun(@Nonnegative int value) {
        this.value = Bits.validate(MASK, value);
    }

    @Nonnegative
    public byte getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }
}
