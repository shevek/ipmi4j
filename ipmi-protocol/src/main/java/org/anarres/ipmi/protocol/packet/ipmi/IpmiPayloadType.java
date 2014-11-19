/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public enum IpmiPayloadType implements Code.Wrapper {

    IPMI(0x00),
    SOL(0x01),
    OEM_EXPLICIT(0x02),
    RMCPOpenSessionRequest(0x10),
    RMCPOpenSessionResponse(0x11),
    RAKPMessage1(0x12),
    RAKPMessage2(0x13),
    RAKPMessage3(0x14),
    RAKPMessage4(0x15),
    OEM0(0x20),
    OEM1(0x21),
    OEM2(0x22),
    OEM3(0x23),
    OEM4(0x24),
    OEM5(0x25),
    OEM6(0x26),
    OEM7(0x27);
    // 6 bits only.
    private final byte code;

    private IpmiPayloadType(int code) {
        this.code = UnsignedBytes.checkedCast(code);
    }

    @Override
    public byte getCode() {
        return code;
    }
}
