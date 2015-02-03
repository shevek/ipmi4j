/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality.AES_CBC_128;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality.Cipher;
import org.anarres.ipmi.protocol.client.session.IpmiSession;

/**
 * [IPMI2] Section 13.28.5, table 13-19, page 159.
 *
 * @author shevek
 */
public enum IpmiConfidentialityAlgorithm implements IpmiAlgorithm {

    NONE(0x00) {
        @Override
        public int getEncryptedLength(IpmiSession session, int unencryptedLength) {
            return unencryptedLength;
        }

        @Override
        public void encrypt(IpmiSession session, ByteBuffer out, ByteBuffer in) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException {
            out.put(in);
        }

        @Override
        public ByteBuffer decrypt(IpmiSession session, ByteBuffer in) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException {
            return in;
        }
    },
    /** [IPMI2] Section 13.29, table 13-20, page 160. */
    AES_CBC_128(0x01) {
        @Nonnegative
        private int pad(@Nonnegative int dataLength) {
            int m = dataLength % 16;
            if (m == 0)
                return 0;
            return 16 - m;
        }

        @Override
        public int getEncryptedLength(IpmiSession session, int unencryptedLength) {
            return 16 // IV
                    + unencryptedLength
                    + pad(unencryptedLength) // trailer: encrypted by AES
                    + 1;    // pad length
        }

        @Override
        public void encrypt(IpmiSession session, ByteBuffer out, ByteBuffer in)
                throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException {
            byte[] iv = session.newRandomSeed(16);
            byte[] key = session.getAdditionalKey(2);
            out.put(iv);
            AES_CBC_128 cipher = new AES_CBC_128();
            cipher.init(Cipher.Mode.ENCRYPT, key, iv);
            cipher.update(in, out);

            PAD:
            {
                int padLength = pad(in.remaining());
                if (padLength > 0) {
                    ByteBuffer pad = ByteBuffer.allocate(padLength);
                    // Pad bytes shall start at 1 and have a monotonically increasing value.
                    int i = 1;
                    while (pad.hasRemaining())
                        pad.put(UnsignedBytes.checkedCast(i++));
                    cipher.update(pad, out);
                }
                out.put(UnsignedBytes.checkedCast(padLength));
            }
        }

        @Override
        public ByteBuffer decrypt(IpmiSession session, ByteBuffer in) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException {

            int limit = in.limit() - 1;
            int padLength = UnsignedBytes.toInt(in.get(limit));
            in.limit(limit);

            ByteBuffer payload = ByteBuffer.allocate(in.remaining());
            byte[] iv = AbstractWireable.readBytes(in, 16);
            byte[] key = session.getAdditionalKey(2);
            AES_CBC_128 cipher = new AES_CBC_128();
            cipher.init(Cipher.Mode.DECRYPT, key, iv);
            cipher.update(payload, in); // TODO: Reads too much from buffer.

            for (int i = 1; i <= padLength; i++)
                if (UnsignedBytes.toInt(payload.get(payload.limit() - i)) != i)
                    throw new IllegalArgumentException("Bad pad byte " + i);

            payload.flip();
            return payload;
        }
    },
    /** [IPMI2] Section 13.30, table 13-21, page 161. */
    xRC4_128(0x02) {
        @Override
        public int getEncryptedLength(IpmiSession session, int unencryptedLength) throws NoSuchAlgorithmException {
            State state = session.getConfidentialityAlgorithmState();
            // Either a data offset of zero (4 bytes) and a 16 byte IV, or a nonzero data offset.
            if (state == null)
                return 4 + 16 + unencryptedLength;
            return 4 + unencryptedLength;
        }
    },
    /** [IPMI2] Section 13.30, table 13-21, page 161. */
    xRC4_40(0x03) {
        @Override
        public int getEncryptedLength(IpmiSession session, int unencryptedLength) {
            State state = session.getConfidentialityAlgorithmState();
            // Either a data offset of zero (4 bytes) and a 16 byte IV, or a nonzero data offset.
            if (state == null)
                return 4 + 16 + unencryptedLength;
            return 4 + unencryptedLength;
        }
    },
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

    public static interface State {
    }

    public static class Rc4State implements State {

        private int offset = 0;
    }
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

    @Nonnegative
    public int getEncryptedLength(@CheckForNull IpmiSession session, @Nonnegative int unencryptedLength)
            throws NoSuchAlgorithmException {
        throw new NoSuchAlgorithmException("Unsupported confidentiality algorithm " + this);
    }

    public void encrypt(@CheckForNull IpmiSession session, @Nonnull ByteBuffer out, @Nonnull ByteBuffer in)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException {
        throw new NoSuchAlgorithmException("Unsupported confidentiality algorithm " + this);
    }

    /** Called with a buffer of payload only. Will consume its entire buffer. */
    @Nonnull
    public ByteBuffer decrypt(@CheckForNull IpmiSession session, @Nonnull ByteBuffer in)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException {
        throw new NoSuchAlgorithmException("Unsupported confidentiality algorithm " + this);
    }
}