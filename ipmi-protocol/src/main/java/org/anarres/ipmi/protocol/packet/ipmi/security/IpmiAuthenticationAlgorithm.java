/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security;

import com.google.common.primitives.UnsignedBytes;
import java.security.NoSuchAlgorithmException;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage4;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.authentication.Hash;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.authentication.None;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.authentication.RAKP_HMAC_MD5;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.authentication.RAKP_HMAC_SHA1;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.authentication.RAKP_HMAC_SHA256;

/**
 * [IPMI2] Section 13.28, table 13-17, page 157.
 *
 * @author shevek
 */
public enum IpmiAuthenticationAlgorithm implements IpmiAlgorithm {

    RAKP_NONE(0x00, 0, IpmiIntegrityAlgorithm.NONE) {
        @Override
        public Hash newImplementation() throws NoSuchAlgorithmException {
            return new None();
        }
    },
    RAKP_HMAC_SHA1(0x01, 20, IpmiIntegrityAlgorithm.HMAC_SHA1_96) {
        @Override
        public Hash newImplementation() throws NoSuchAlgorithmException {
            return new RAKP_HMAC_SHA1();
        }
    },
    RAKP_HMAC_MD5(0x02, 16, IpmiIntegrityAlgorithm.HMAC_MD5_128) {
        @Override
        public Hash newImplementation() throws NoSuchAlgorithmException {
            return new RAKP_HMAC_MD5();
        }
    },
    RAKP_HMAC_SHA256(0x03, 32, IpmiIntegrityAlgorithm.HMAC_SHA256_128) {
        @Override
        public Hash newImplementation() throws NoSuchAlgorithmException {
            return new RAKP_HMAC_SHA256();
        }
    };
    public static final byte PAYLOAD_TYPE = 0;
    private final byte code;
    private final int hashLength;
    private final IpmiIntegrityAlgorithm integrityAlgorithm;

    private IpmiAuthenticationAlgorithm(@Nonnegative int code, @Nonnegative int hashLength, @Nonnull IpmiIntegrityAlgorithm integrityAlgorithm) {
        this.code = UnsignedBytes.checkedCast(code);
        this.hashLength = hashLength;
        this.integrityAlgorithm = integrityAlgorithm;
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

    @Nonnegative
    public int getHashLength() {
        return hashLength;
    }

    /** The algorithm used to generate the integrity check in {@link IpmiRAKPMessage4}. */
    @Nonnull
    public IpmiIntegrityAlgorithm getIntegrityAlgorithm() {
        return integrityAlgorithm;
    }

    @Nonnull
    public abstract Hash newImplementation() throws NoSuchAlgorithmException;
}
