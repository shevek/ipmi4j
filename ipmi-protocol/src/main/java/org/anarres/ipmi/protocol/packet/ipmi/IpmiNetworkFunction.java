/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 5.1, Pages 40-41.
 * 
 * Called a NetFn in the documentation.
 * Even numbers are requests. Odd numbers are responses.
 *
 * @author shevek
 */
public enum IpmiNetworkFunction implements Code.Wrapper {

    /** Chassis Device Requests and Responses. */
    Chassis(0x00),
    /** Bridge Requests and Responses. */
    Bridge(0x02),
    /** Sensor and Event Requests and Responses. */
    Sensor(0x04),
    /** Application Requests and Responses. */
    App(0x06),
    /** Firmware Transfer Requests and Responses. */
    Firmware(0x08),
    /** Non-volatile Storage Requests and Responses. */
    Storage(0x0A),
    /** Media-specific Configuration and Control. */
    Transport(0x0C),
    /** Non-IPMI Group Requests and Responses. */
    GroupExtension(0x2C),
    /** OEM/Non- IPMI Group Requests and Response. */
    OEM_Group(0x2E),
    ControllerSpecific0(0x30),
    ControllerSpecific1(0x32),
    ControllerSpecific2(0x34),
    ControllerSpecific3(0x36),
    ControllerSpecific4(0x38),
    ControllerSpecific5(0x3A),
    ControllerSpecific6(0x3C),
    ControllerSpecific7(0x3E);
    private final byte code;
    // private Bits bits;

    private IpmiNetworkFunction(@Nonnegative int code) {
        this.code = UnsignedBytes.checkedCast(code);
        // this.bits = new Bits(0, 0xFC, code << 2);
    }

    @Override
    public byte getCode() {
        return code;
    }
}
