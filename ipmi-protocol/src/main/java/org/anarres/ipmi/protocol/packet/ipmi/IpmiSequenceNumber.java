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
public class IpmiSequenceNumber {

    private byte value;

    public IpmiSequenceNumber(int sequenceNumber) {
        this.value = Bits.validate(0x3F, sequenceNumber);
    }

    @Nonnegative
    public byte getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "0x" + Integer.toHexString(getValue());
    }
}
