/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import com.google.common.primitives.UnsignedBytes;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public enum RmcpMessageClass implements Code.Wrapper {

    ASF(6), IPMI(7), OEM(8);
    final byte code;

    private RmcpMessageClass(int code) {
        this.code = UnsignedBytes.checkedCast(code);
    }

    @Override
    public byte getCode() {
        return code;
    }
}
