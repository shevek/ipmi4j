/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public enum IpmiChannelProtocol implements Code.Wrapper {

    Reserved_00(0x00, "n/a", "reserved"),
    IPMB_1_0(0x01, "IPMB-1.0", "Used for IPMB, serial/modem Basic Mode, and LAN"),
    ICMB_1_0(0x02, "ICMB-1.0", "ICMB v1.0"),
    Reserved_03(0x03, "reserved", "reserved"),
    IPMI_SMBus(0x04, "IPMI-SMBus", "IPMI on PCI-SMBus / SMBus 1.x - 2.x"),
    KeyboardControllerStyle(0x05, "KCS", "Keyboard Controller Style System Interface Format"),
    SMIC(0x06, "SMIC", "SMIC System Interface Format"),
    BlockTransfer_IPMI1_0(0x07, "BT-10", "BT System Interface Format, IPMI v1.0"),
    BlockTransfer_IPMI1_5(0x08, "BT-15", "BT System Interface Format, IPMI v1.5 ");
    private final byte code;
    private final String name;
    private final String description;

    private IpmiChannelProtocol(int code, String name, String description) {
        this.code = UnsignedBytes.checkedCast(code);
        this.name = name;
        this.description = description;
    }

    @Override
    public byte getCode() {
        return code;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name() + "(0x" + UnsignedBytes.toString(getCode(), 16) + ": " + getName() + " :" + getDescription() + ")";
    }
}