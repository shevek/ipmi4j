/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public class AsfRsspSessionAuthentication {

    public interface Payload {

        public void toWire(ByteBuffer buffer);
    }

    public enum AuthenticationAlgorithm implements Payload, Code.Wrapper {

        RAKP_HMAC_SHA1(0x01);
        public static final short LENGTH = 8;
        private byte code;

        private AuthenticationAlgorithm(int code) {
            this.code = (byte) code;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public void toWire(ByteBuffer buffer) {
            buffer.put((byte) 0x01);   // Payload type AuthenticationAlgorithm
            buffer.put((byte) 0x00);   // Reserved
            buffer.putShort(LENGTH);
            buffer.put(code);
            buffer.put(new byte[3]);    // Reserved
        }
    }

    public enum IntegrityAlgorithm implements Payload, Code.Wrapper {

        HMAC_SHA1_96(0x01);
        public static final short LENGTH = 8;
        private byte code;

        private IntegrityAlgorithm(int code) {
            this.code = (byte) code;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public void toWire(ByteBuffer buffer) {
            buffer.put((byte) 0x02);   // Payload type IntegrityAlgorithm
            buffer.put((byte) 0x00);   // Reserved
            buffer.putShort(LENGTH);
            buffer.put(code);
            buffer.put(new byte[3]);    // Reserved
        }
    }

    public enum EndOfList implements Payload {

        INSTANCE;
        public static final short LENGTH = 4;

        @Override
        public void toWire(ByteBuffer buffer) {
            buffer.put((byte) 0x00);   // Payload type EndOfList
            buffer.put((byte) 0x00);   // Reserved
            buffer.putShort(LENGTH);
        }
    };

    @Nonnull
    public static Payload fromWire(@Nonnull ByteBuffer buffer) {
        if (!buffer.hasRemaining())
            return EndOfList.INSTANCE;

        byte type = buffer.get();
        AbstractWireable.assertWireByte(buffer, (byte) 0, "reserved byte");
        short length = buffer.getShort();
        byte[] data = new byte[length - 4];
        buffer.get(data);

        switch (buffer.get()) {
            case 0:
                return EndOfList.INSTANCE;
            case 1:
                return Code.fromByte(AuthenticationAlgorithm.class, data[0]);
            case 2:
                return Code.fromByte(IntegrityAlgorithm.class, data[0]);
            default:
                throw new IllegalArgumentException("Unknown algorithm type 0x" + UnsignedBytes.toString(type, 16));
        }
    }
}