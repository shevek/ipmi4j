/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;
import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.asf.AsfRsspSessionStatus;
import org.anarres.ipmi.protocol.packet.common.Code;
import static org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType.*;

/**
 * [IPMI2] Section 13.24, table 13-15, page 154.
 *
 * @see AsfRsspSessionStatus
 * @author shevek
 */
public enum IpmiSessionStatus implements Code.Wrapper {

    NO_ERROR(0x00, RMCPOpenSessionRequest, RAKPMessage2, RAKPMessage3, RAKPMessage4),
    INSUFFICIENT_RESOURCES(0x01, RMCPOpenSessionRequest, RAKPMessage2, RAKPMessage3, RAKPMessage4),
    INVALID_SESSION_ID(0x02, RMCPOpenSessionRequest, RAKPMessage2, RAKPMessage3, RAKPMessage4),
    INVALID_PAYLOAD_TYPE(0x03, RMCPOpenSessionRequest),
    INVALID_AUTHENTICATION_ALGORITHM(0x04, RMCPOpenSessionRequest),
    INVALID_INTEGRITY_ALGORITHM(0x05, RMCPOpenSessionRequest),
    NO_MATCHING_AUTHENTICATION_PAYLOAD(0x06, RMCPOpenSessionRequest),
    NO_MATCHING_INTEGRITY_PAYLOAD(0x07, RMCPOpenSessionRequest),
    INACTIVE_SESSION_ID(0x08, RAKPMessage2, RAKPMessage3, RAKPMessage4),
    INVALID_ROLE(0x09, RMCPOpenSessionRequest, RAKPMessage2),
    UNAUTHORIZED_ROLE(0x0A, RAKPMessage2),
    INSUFFICIENT_RESOURCES_FOR_ROLE(0x0B, RAKPMessage2),
    INVALID_NAME_LENGTH(0x0C, RAKPMessage2),
    UNAUTHORIZED_NAME(0x0D, RAKPMessage2),
    UNAUTHORIZED_GUID(0x0E, RAKPMessage3),
    INVALID_INTEGRITY_CHECK_VALUE(0x0F, RAKPMessage3, RAKPMessage4),
    INVALID_CONFIDENTIALITY_ALGORITHM(0x10, RMCPOpenSessionRequest),
    NO_CIPHER_SUITE_MATCH(0x11, RMCPOpenSessionRequest),
    ILLEGAL_OR_UNRECOGNIZED_PARAMETER(0x12, RMCPOpenSessionRequest, RAKPMessage2, RAKPMessage3, RAKPMessage4);
    private final byte code;
    private final IpmiPayloadType[] allowedPayloadTypes;

    private IpmiSessionStatus(@Nonnegative int code, @Nonnull IpmiPayloadType... allowedPayloadTypes) {
        this.code = UnsignedBytes.checkedCast(code);
        this.allowedPayloadTypes = allowedPayloadTypes;
    }

    @Override
    public byte getCode() {
        return code;
    }
}
