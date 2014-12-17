/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.alg;

import com.google.common.primitives.UnsignedBytes;

/**
 * [IPMI2] Section 13.28, table 13-17, page 157.
 *
 * @author shevek
 */
public enum IpmiAuthenticationAlgorithm implements IpmiAlgorithm {

    RAKP_NONE(0x00),
    RAKP_HMAC_SHA1(0x01),
    RAKP_HMAC_MD5(0x02),
    RAKP_HMAC_SHA256(0x03);
    public static final byte PAYLOAD_TYPE = 0;
    private byte code;

    private IpmiAuthenticationAlgorithm(int code) {
        this.code = UnsignedBytes.checkedCast(code);
    }

    /** [IPMI2] Section 13.7, table 13-9, page 147. */
    @Override
    public byte getPayloadType() {
        return PAYLOAD_TYPE;
    }

    @Override
    public byte getCode() {
        return code;
    }
}
