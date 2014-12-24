/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security;

import com.google.common.primitives.UnsignedBytes;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnegative;
import javax.crypto.NoSuchPaddingException;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality.AES_CBC_128;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality.Cipher;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality.None;

/**
 * [IPMI2] Section 13.28.5, table 13-19, page 159.
 *
 * @author shevek
 */
public enum IpmiConfidentialityAlgorithm implements IpmiAlgorithm<Cipher> {

    NONE(0x00) {
        @Override
        public Cipher newImplementation() throws NoSuchAlgorithmException, NoSuchPaddingException {
            return new None();
        }
    },
    AES_CBC_128(0x01) {
        @Override
        public Cipher newImplementation() throws NoSuchAlgorithmException, NoSuchPaddingException {
            return new AES_CBC_128();
        }
    },
    xRC4_128(0x02),
    xRC4_40(0x03),
    OEM_30(0x30),
    OEM_31(0x31),
    OEM_32(0x32),
    OEM_33(0x33),
    OEM_34(0x34),
    OEM_35(0x35),
    OEM_36(0x36),
    OEM_37(0x37),
    OEM_38(0x38),
    OEM_39(0x39),
    OEM_3A(0x3A),
    OEM_3B(0x3B),
    OEM_3C(0x3C),
    OEM_3D(0x3D),
    OEM_3E(0x3E),
    OEM_3F(0x3F);
    public static final byte PAYLOAD_TYPE = 2;
    private final byte code;

    private IpmiConfidentialityAlgorithm(@Nonnegative int code) {
        this.code = UnsignedBytes.checkedCast(code);
    }

    @Override
    public byte getPayloadType() {
        return PAYLOAD_TYPE;
    }

    @Override
    public byte getCode() {
        return code;
    }

    @Override
    public Cipher newImplementation() throws NoSuchAlgorithmException, NoSuchPaddingException {
        throw new UnsupportedOperationException("Unsupported confidentiality algorithm " + this);
    }
}
