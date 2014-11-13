/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.Wireable;

/**
 *
 * @author shevek
 */
public interface AsfRsspSessionAuthenticationPayload extends Wireable {

    public enum AuthenticationAlgorithm implements AsfRsspSessionAuthenticationPayload {

        RAKP_HMAC_SHA1(0x01);
        private byte code;

        private AuthenticationAlgorithm(int code) {
            this.code = (byte) code;
        }

        @Override
        public int getWireLength() {
            return 8;
        }

        @Override
        public void toWire(ByteBuffer buffer) {
            buffer.put((byte) 0x01);   // Payload type AuthenticationAlgorithm
            buffer.put((byte) 0x00);   // Reserved
            buffer.putShort((short) 8);
            buffer.put(code);
            buffer.put(new byte[3]);    // Reserved
        }
    }

    public enum IntegrityAlgorithm implements AsfRsspSessionAuthenticationPayload {

        HMAC_SHA1_96(0x01);
        private byte code;

        private IntegrityAlgorithm(int code) {
            this.code = (byte) code;
        }

        @Override
        public int getWireLength() {
            return 8;
        }

        @Override
        public void toWire(ByteBuffer buffer) {
            buffer.put((byte) 0x02);   // Payload type IntegrityAlgorithm
            buffer.put((byte) 0x00);   // Reserved
            buffer.putShort((short) 8);
            buffer.put(code);
            buffer.put(new byte[3]);    // Reserved
        }
    }
    public static final AsfRsspSessionAuthenticationPayload EndOfList = new AsfRsspSessionAuthenticationPayload() {
        @Override
        public int getWireLength() {
            return 4;
        }

        @Override
        public void toWire(ByteBuffer buffer) {
            buffer.put((byte) 0x00);   // Payload type EndOfList
            buffer.put((byte) 0x00);   // Reserved
            buffer.putShort((short) 8);
        }
    };
}