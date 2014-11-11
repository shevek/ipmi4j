/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import com.google.common.primitives.UnsignedBytes;

/**
 * RSSP and RAKP Status Codes.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.3.5.1 page 32.
 *
 * @author shevek
 */
public enum RsspStatus {

    NO_ERROR(0x00, 0x43, 0x44, 0xc1, 0xc2),
    INSUFFICIENT_RESOURCES(0x01, 0x43),
    INVALID_SESSION_ID(0x02, 0x43, 0x44, 0xc1, 0xc2),
    INVALID_PAYLOAD_TYPE(0x03, 0x43),
    INVALID_AUTHENTICATION_ALGORITHM(0x04, 0x43),
    INVALID_INTEGRITY_ALGORITHM(0x05, 0x43),
    NO_MATCHING_AUTHENTICATION_PAYLOAD(0x06, 0x43),
    NO_MATCHING_INTEGRITY_PAYLOAD(0x07, 0x43),
    INACTIVE_SESSION_ID(0x08, 0x44, 0xc1, 0xc2),
    INVALID_ROLE(0x09, 0xc1),
    UNAUTHORIZED_ROLE(0x0A, 0xc1),
    INSUFFICIENT_RESOURCES_FOR_ROLE(0x0B, 0xc1),
    INVALID_NAME_LENGTH(0x0C, 0xc1),
    UNAUTHORIZED_NAME(0x0D, 0xc1),
    UNAUTHORIZED_GUID(0x0E, 0xc2),
    INVALID_INTEGRITY_CHECK_VALUE(0x0F, 0xc2);
    private final byte code;
    private final byte[] messageTypes;

    private RsspStatus(int code, int... messageTypes) {
        this.code = UnsignedBytes.checkedCast(code);
        this.messageTypes = new byte[messageTypes.length];
        for (int i = 0; i < messageTypes.length; i++)
            this.messageTypes[i] = UnsignedBytes.checkedCast(messageTypes[i]);
    }

    public byte getCode() {
        return code;
    }
}
