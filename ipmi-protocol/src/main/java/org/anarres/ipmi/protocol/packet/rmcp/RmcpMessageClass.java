/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import com.google.common.primitives.UnsignedBytes;

/**
 *
 * @author shevek
 */
public enum RmcpMessageClass {

    ASF(6), IPMI(7), OEM(8);
    final byte value;

    private RmcpMessageClass(int value) {
        this.value = UnsignedBytes.checkedCast(value);
    }

    public byte getValue() {
        return value;
    }
}
