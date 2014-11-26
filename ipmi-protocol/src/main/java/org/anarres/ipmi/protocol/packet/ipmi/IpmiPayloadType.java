/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.CheckForSigned;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 13.27.3 page 157.
 *
 * @author shevek
 */
public enum IpmiPayloadType implements Code.Wrapper {

    IPMI(0x00, 1, 0),
    SOL(0x01, 1, 0),
    OEM_EXPLICIT(0x02, -1, -1),
    RMCPOpenSessionRequest(0x10, 1, 0),
    RMCPOpenSessionResponse(0x11, 1, 0),
    RAKPMessage1(0x12, 1, 0),
    RAKPMessage2(0x13, 1, 0),
    RAKPMessage3(0x14, 1, 0),
    RAKPMessage4(0x15, 1, 0),
    OEM0(0x20, -1, -1),
    OEM1(0x21, -1, -1),
    OEM2(0x22, -1, -1),
    OEM3(0x23, -1, -1),
    OEM4(0x24, -1, -1),
    OEM5(0x25, -1, -1),
    OEM6(0x26, -1, -1),
    OEM7(0x27, -1, -1);
    // 6 bits only.
    private final byte code;
    @CheckForSigned
    private final byte majorFormat;
    @CheckForSigned
    private final byte minorFormat;

    private IpmiPayloadType(int code, int majorFormat, int minorFormat) {
        this.code = UnsignedBytes.checkedCast(code);
        this.majorFormat = UnsignedBytes.checkedCast(majorFormat);
        this.minorFormat = UnsignedBytes.checkedCast(minorFormat);
    }

    @Override
    public byte getCode() {
        return code;
    }
}
