/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.alg;

import com.google.common.primitives.UnsignedBytes;

/**
 * [IPMI2] Section 13.28.4, table 13-18, page 159.
 *
 * @author shevek
 */
public enum IpmiIntegrityAlgorithm implements IpmiAlgorithm {

    NONE(0x00),
    HMAC_SHA1_96(0x01),
    HMAC_MD5_128(0x02),
    MD5_128(0x03),
    HMAC_SHA256_128(0x04);
    public static final byte PAYLOAD_TYPE = 1; 
    private byte code;

    private IpmiIntegrityAlgorithm(int code) {
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
