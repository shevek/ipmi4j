/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 13.6 page 133, right margin of table.
 * NOT [IPMI2] Section 13.28 page 157.
 *
 * @author shevek
 */
public enum IpmiHeaderAuthenticationType implements Code.Wrapper {

    NONE(0), MD2(1), MD5(2), PASSWORD(4), OEM_PROPRIETARY(5), RMCP(6);
    private final byte code;

    private IpmiHeaderAuthenticationType(int code) {
        this.code = UnsignedBytes.checkedCast(code);
    }

    @Override
    public byte getCode() {
        return code;
    }
}
