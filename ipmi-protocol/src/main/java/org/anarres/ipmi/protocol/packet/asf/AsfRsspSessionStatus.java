/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;
import static org.anarres.ipmi.protocol.packet.asf.AsfRmcpMessageType.*;

/**
 * RSSP and RAKP Status Codes.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.3.5.1 page 32.
 *
 * @author shevek
 */
public enum AsfRsspSessionStatus implements Code.Wrapper {

    NO_ERROR(0x00, OpenSessionResponse, CloseSessionResponse, RAKPMessage2, RAKPMessage3),
    INSUFFICIENT_RESOURCES(0x01, OpenSessionResponse),
    INVALID_SESSION_ID(0x02, OpenSessionResponse, CloseSessionResponse, RAKPMessage2, RAKPMessage3),
    INVALID_PAYLOAD_TYPE(0x03, OpenSessionResponse),
    INVALID_AUTHENTICATION_ALGORITHM(0x04, OpenSessionResponse),
    INVALID_INTEGRITY_ALGORITHM(0x05, OpenSessionResponse),
    NO_MATCHING_AUTHENTICATION_PAYLOAD(0x06, OpenSessionResponse),
    NO_MATCHING_INTEGRITY_PAYLOAD(0x07, OpenSessionResponse),
    INACTIVE_SESSION_ID(0x08, CloseSessionResponse, RAKPMessage2, RAKPMessage3),
    INVALID_ROLE(0x09, RAKPMessage2),
    UNAUTHORIZED_ROLE(0x0A, RAKPMessage2),
    INSUFFICIENT_RESOURCES_FOR_ROLE(0x0B, RAKPMessage2),
    INVALID_NAME_LENGTH(0x0C, RAKPMessage2),
    UNAUTHORIZED_NAME(0x0D, RAKPMessage2),
    UNAUTHORIZED_GUID(0x0E, RAKPMessage3),
    INVALID_INTEGRITY_CHECK_VALUE(0x0F, RAKPMessage3);
    private final byte code;
    private final AsfRmcpMessageType[] messageTypes;

    private AsfRsspSessionStatus(@Nonnegative int code, @Nonnull AsfRmcpMessageType... messageTypes) {
        this.code = UnsignedBytes.checkedCast(code);
        this.messageTypes = messageTypes;
    }

    @Override
    public byte getCode() {
        return code;
    }
}