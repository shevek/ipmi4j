/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 13.28 page 157.
 *
 * @author shevek
 */
public enum IpmiAuthenticationAlgorithm implements Code.Wrapper {

    RAKP_NONE(0x00),
    RAKP_HMAC_SHA1(0x01),
    RAKP_HMAC_MD5(0x02),
    RAKP_HMAC_SHA256(0x03);
    private byte code;

    private IpmiAuthenticationAlgorithm(int code) {
        this.code = UnsignedBytes.checkedCast(code);
    }

    @Override
    public byte getCode() {
        return code;
    }
}
