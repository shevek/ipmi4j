/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 5.2 page 43.
 *
 * @author shevek
 */
public enum IpmiCompletionCode implements Code.Wrapper {

    Normal(0x00, "Command Completed Normally."),
    NodeBusy(0xC0, "Node Busy. Command could not be processed because command processing resources are temporarily unavailable."),
    InvalidCommand(0xC1, "Invalid Command. Used to indicate an unrecognized or unsupported command."),
    InvalidCommandForLUN(0xC2, "Command invalid for given LUN."),
    Timeout(0xC3, "Timeout while processing command. Response unavailable."),
    OutOfSpace(0xC4, "Out of space. Command could not be completed because of a lack of storage space required to execute the given command operation."),
    ReservationCanceled(0xC5, "Reservation Canceled or Invalid Reservation ID."),
    RequestDataTruncated(0xC6, "Request data truncated."),
    RequestDataLengthInvalid(0xC7, "Request data length invalid."),
    RequestDataLengthFieldLimitExceeded(0xC8, "Request data field length limit exceeded."),
    ParameterOutOfRange(0xC9, "Parameter out of range. One or more parameters in the data field of the Request are out of range."),
    CannotReturnRequestedNumberOfBytes(0xCA, "Cannot return number of requested data bytes."),
    NotPresent(0xCB, "Requested Sensor, data, or record not present."),
    InvalidDataField(0xCC, "Invalid data field in Request."),
    IllegalCommandForTarget(0xCD, "Command illegal for specified sensor or record type."),
    CommandResponseNotAvailable(0xCE, "Command response could not be provided."),
    CannotExecuteDuplicatedRequest(0xCF, "Cannot execute duplicated request."),
    CommandResponseNotAvailable_SDRUpdate(0xD0, "Command response could not be provided. SDR Repository in update mode."),
    CommandResponseNotAvailable_FirmwareUpdate(0xD1, "Command response could not be provided. Device in firmware update mode."),
    CommandResponseNotAvailable_BMCInitialization(0xD2, "Command response could not be provided. BMC initialization or initialization agent in progress."),
    DestinationUnavailable(0xD3, "Destination unavailable. Cannot deliver request to selected destination."),
    InsufficientPrivilege(0xD4, "Cannot execute command due to insufficient privilege level or other security-based restriction."),
    IllegalState(0xD5, "Cannot execute command. Command, or request parameter(s), not supported in present state."),
    IllegalParameterForSubFunctionState(0xD6, "Cannot execute command. Parameter is illegal because command sub-function has been disabled or is unavailable."),
    UnspecifiedError(0xFF, "Unspecified error.");
    /*
     * DEVICE-SPECIFIC (OEM) CODES 01h-7Eh 
     * 01h-7Eh Device specific (OEM) completion codes. This range is used for command-
     * specific codes that are also specific for a particular device and version. A-priori
     * knowledge of the device command set is required for interpretation of these
     * codes.
     * COMMAND-SPECIFIC CODES 80h-BEh 
     * 80h-BEh Standard command-specific codes. This range is reserved for command-
     * specific completion codes for commands specified in this document.
     */
    private final byte code;
    private final String message;

    private IpmiCompletionCode(int code, String message) {
        this.code = UnsignedBytes.checkedCast(code);
        this.message = message;
    }

    @Override
    public byte getCode() {
        return code;
    }

    @Nonnull
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "(0x" + UnsignedBytes.toString(getCode(), 16) + ") " + getMessage();
    }
}
