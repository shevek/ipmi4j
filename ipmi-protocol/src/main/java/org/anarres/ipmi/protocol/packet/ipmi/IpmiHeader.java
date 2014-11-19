/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * 
 * Section 13.6 page 133.
 *
 * @author shevek
 */
public class IpmiHeader extends AbstractWireable {

    public enum AuthenticationType implements Code.Wrapper {

        NONE(0),
        MD2(1),
        MD5(2),
        PASSWORD(4),
        OEM_PROPRIETARY(5),
        RMCP(6);
        private final byte code;

        private AuthenticationType(int code) {
            this.code = UnsignedBytes.checkedCast(code);
        }

        @Override
        public byte getCode() {
            return code;
        }
    }
    private AuthenticationType authenticationType;
    private IpmiPayloadType payloadType;
    private boolean encrypted;
    private boolean authenticated;
    private int oemIanaEnterpriseNumber;    // 3 byte oem iana; 1 byte zero
    private char oemPayloadId;
    private int ipmiSessionId;
    private int ipmiSessionSequenceNumber;

    // 2 byte payload length
    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        buffer.put(authenticationType.getCode());
        byte payloadType = this.payloadType.getCode();
        if (encrypted)
            payloadType |= 0x80;
        if (authenticated)
            payloadType |= 0x40;
        buffer.put(payloadType);
        // if payloadType == OEM_EXPLICIT
    }
}
