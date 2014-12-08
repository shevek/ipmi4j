/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 5.1, Pages 40-41.
 * 
 * Even numbers are requests. Odd numbers are responses.
 *
 * @author shevek
 */
public enum IpmiNetworkFunction implements Code.Wrapper {

    Chassis(0x00),
    Bridge(0x02),
    Sensor(0x04),
    App(0x06),
    Firmware(0x08),
    Storage(0x0A),
    Transport(0x0C),
    GroupExtension(0x2C),
    OEM_Group(0x2E),
    ControllerSpecific0(0x30),
    ControllerSpecific1(0x32),
    ControllerSpecific2(0x34),
    ControllerSpecific3(0x36),
    ControllerSpecific4(0x38),
    ControllerSpecific5(0x3A),
    ControllerSpecific6(0x3C),
    ControllerSpecific7(0x3E);
    private byte code;
    private Bits bits;

    private IpmiNetworkFunction(int code) {
        this.code = UnsignedBytes.checkedCast(code);
        this.bits = new Bits(0, 0xFC, code << 2);
    }

    @Override
    public byte getCode() {
        return code;
    }
}