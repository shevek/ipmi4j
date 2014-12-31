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
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality.AES_CBC_128;
import org.anarres.ipmi.protocol.packet.ipmi.security.impl.confidentiality.Cipher;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 * [IPMI2] Section 13.28.5, table 13-19, page 159.
 *
 * @author shevek
 */
public enum IpmiConfidentialityAlgorithm implements IpmiAlgorithm {

    NONE(0x00) {
        @Override
        public int getWireLength(IpmiSession session, IpmiPayload payload) {
            return payload.getWireLength();
        }

        @Override
        public void toWire(ByteBuffer buffer, IpmiSession session, IpmiPayload payload) {
            payload.toWire(buffer);
        }

        @Override
        public void fromWire(ByteBuffer buffer, IpmiSession session, IpmiPayload payload) {
            payload.fromWire(buffer);
        }
    },
    /** [IPMI2] Section 13.29, table 13-20, page 160. */
    AES_CBC_128(0x01) {
        private int pad(int dataLength) {
            int m = dataLength % 16;
            if (m == 0)
                return 0;
            return 16 - m;
        }

        @Override
        public int getWireLength(IpmiSession session, IpmiPayload payload) {
            int dataLength = payload.getWireLength();
            return 16 // IV
                    + dataLength
                    + pad(dataLength) // trailer: encrypted by AES
                    + 1;    // pad length
        }

        @Override
        public void toWire(ByteBuffer buffer, IpmiSession session, IpmiPayload payload)
                throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException {
            int dataLength = payload.getWireLength();
            int padLength = pad(dataLength);
            ByteBuffer tmp = ByteBuffer.allocate(dataLength + padLength);
            payload.toWire(tmp);
            // Pad bytes shall start at 1 and have a monotonically increasing value.
            int i = 1;
            while (tmp.hasRemaining())
                tmp.put(UnsignedBytes.checkedCast(i++));

            byte[] iv = session.newRandomSeed(16);
            byte[] key = session.getAdditionalKey(2);
            buffer.put(iv);
            AES_CBC_128 cipher = new AES_CBC_128();
            cipher.init(Cipher.Mode.ENCRYPT, key, iv);
            cipher.update(tmp, buffer);
            buffer.put(UnsignedBytes.checkedCast(padLength));
        }

        @Override
        public void fromWire(ByteBuffer buffer, IpmiSession session, IpmiPayload payload) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException {
            ByteBuffer tmp = ByteBuffer.allocate(buffer.remaining() * 2);

            byte[] iv = AbstractWireable.readBytes(buffer, 16);
            byte[] key = session.getAdditionalKey(2);
            AES_CBC_128 cipher = new AES_CBC_128();
            cipher.init(Cipher.Mode.DECRYPT, key, iv);
            cipher.update(tmp, buffer); // TODO: Reads too much from buffer.

            int padLength = UnsignedBytes.toInt(buffer.get());

            payload.fromWire(tmp);
            int i = 1;
            while (tmp.hasRemaining())
                if (UnsignedBytes.toInt(tmp.get()) != i++)
                    throw new IllegalArgumentException("Bad pad byte " + i);
            if (i != padLength) // TODO: Debug possibly -1
                throw new IllegalArgumentException("Bad pad length " + i);
        }
    },
    /** [IPMI2] Section 13.30, table 13-21, page 161. */
    xRC4_128(0x02) {
        public int getConfidentialityHeaderLength() {
            // Either a data offset of zero (4 bytes) and a 16 byte IV, or a nonzero data offset.
            return 4 + 16;
        }

        public int getConfidentialityTrailerLength(int payloadLength) {
            return 0;
        }
    },
    /** [IPMI2] Section 13.30, table 13-21, page 161. */
    xRC4_40(0x03) {
        public int getConfidentialityHeaderLength() {
            // Either a data offset of zero (4 bytes) and a 16 byte IV, or a nonzero data offset.
            return 4 + 16;
        }

        public int getConfidentialityTrailerLength(int payloadLength) {
            return 0;
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
    public int getWireLength(@Nonnull IpmiSession session, @Nonnull IpmiPayload payload)
            throws NoSuchAlgorithmException {
        throw new NoSuchAlgorithmException("Unsupported confidentiality algorithm " + this);
    }

    public void toWire(@Nonnull ByteBuffer buffer, @Nonnull IpmiSession session, @Nonnull IpmiPayload payload)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException {
        throw new NoSuchAlgorithmException("Unsupported confidentiality algorithm " + this);
    }

    public void fromWire(@Nonnull ByteBuffer buffer, @Nonnull IpmiSession session, @Nonnull IpmiPayload payload)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException {
        throw new NoSuchAlgorithmException("Unsupported confidentiality algorithm " + this);
    }
}