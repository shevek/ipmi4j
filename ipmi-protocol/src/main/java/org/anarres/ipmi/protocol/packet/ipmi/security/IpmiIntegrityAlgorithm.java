/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity.HMAC_MD5_128;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity.HMAC_SHA1_96;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity.HMAC_SHA256_128;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity.MAC;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity.MD5_128;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.integrity.None;

/**
 * [IPMI2] Section 13.28.4, table 13-18, page 159.
 *
 * @author shevek
 */
public enum IpmiIntegrityAlgorithm implements IpmiAlgorithm {

    NONE(0x00, 0) {
        @Override
        public MAC newImplementation() {
            return new None();
        }
    },
    HMAC_SHA1_96(0x01, 12) {
        @Override
        public MAC newImplementation() throws NoSuchAlgorithmException {
            return new HMAC_SHA1_96();
        }
    },
    HMAC_MD5_128(0x02, 16) {
        @Override
        public MAC newImplementation() throws NoSuchAlgorithmException {
            return new HMAC_MD5_128();
        }
    },
    MD5_128(0x03, 16) {
        @Override
        public MAC newImplementation() throws NoSuchAlgorithmException {
            return new MD5_128();
        }
    },
    HMAC_SHA256_128(0x04, 16) {
        @Override
        public MAC newImplementation() throws NoSuchAlgorithmException {
            return new HMAC_SHA256_128();
        }
    };
    public static final byte PAYLOAD_TYPE = 1;
    private final byte code;
    private final int macLength;

    private IpmiIntegrityAlgorithm(@Nonnegative int code, @Nonnegative int macLength) {
        this.code = UnsignedBytes.checkedCast(code);
        this.macLength = macLength;
    }

    @Override
    public byte getPayloadType() {
        return PAYLOAD_TYPE;
    }

    @Override
    public byte getCode() {
        return code;
    }

    @Nonnegative
    public int getMacLength() {
        return macLength;
    }

    @Nonnull
    public abstract MAC newImplementation() throws NoSuchAlgorithmException;

    @Nonnull
    public byte[] sign(ByteBuffer integrityInput) throws NoSuchAlgorithmException {
        MAC mac = newImplementation();
        mac.update(integrityInput);
        return mac.doFinal();
    }
}
