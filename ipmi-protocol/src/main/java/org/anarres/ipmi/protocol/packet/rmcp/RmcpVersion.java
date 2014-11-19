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
public enum RmcpVersion implements Code.Wrapper {

    LEGACY0(0),
    LEGACY1(1),
    LEGACY2(2),
    LEGACY3(3),
    LEGACY4(4),
    LEGACY5(5),
    ASF_RMCP_1_0(6);
    final byte code;

    private RmcpVersion(int code) {
        this.code = UnsignedBytes.checkedCast(code);
    }

    @Override
    public byte getCode() {
        return code;
    }
}
