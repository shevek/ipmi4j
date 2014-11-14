/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;

/**
 *
 * @author shevek
 */
public interface AsfRsspSessionAuthenticationPayload {

    public enum AuthenticationAlgorithm implements AsfRsspSessionAuthenticationPayload {

        RAKP_HMAC_SHA1(0x01);
        public static final short LENGTH = 8;
        private byte code;

        private AuthenticationAlgorithm(int code) {
            this.code = (byte) code;
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

    public enum IntegrityAlgorithm implements AsfRsspSessionAuthenticationPayload {

        HMAC_SHA1_96(0x01);
        public static final short LENGTH = 8;
        private byte code;

        private IntegrityAlgorithm(int code) {
            this.code = (byte) code;
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

    public enum EndOfList implements AsfRsspSessionAuthenticationPayload {

        INSTANCE;
        public static final short LENGTH = 4;

        @Override
        public void toWire(ByteBuffer buffer) {
            buffer.put((byte) 0x00);   // Payload type EndOfList
            buffer.put((byte) 0x00);   // Reserved
            buffer.putShort(LENGTH);
        }
    };

    public void toWire(ByteBuffer buffer);
}