/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 6.5, table 6-3, page 49.
 *
 * @author shevek
 */
public enum IpmiChannelMedium implements Code.DescriptiveWrapper {

    IPMB(0x1, "IPMB (I2C)"),
    ICMB_v1_0(0x2, "ICMB v1.0"),
    ICMB_v0_9(0x3, "ICMB v0.9"),
    LAN_802_3(0x4, "802.3 LAN"),
    RS232(0x5, "Asynch. Serial/Modem (RS-232)"),
    LAN_Other(0x6, "Other LAN"),
    PCI_SMBus(0x7, "PCI SMBus"),
    SMBus_v1(0x8, "SMBus v1.0/1.1"),
    SMBus_v2(0x9, "SMBus v2.0"),
    USB_1(0xA, "reserved for USB 1.x"),
    USB_2(0xB, "reserved for USB 2.x"),
    SystemInterface(0xC, "System Interface (KCS, SMIC, or BT)"),
    OEM60(0x60, "OEM 0x60"),
    OEM61(0x61, "OEM 0x61"),
    OEM62(0x62, "OEM 0x62"),
    OEM63(0x63, "OEM 0x63"),
    OEM64(0x64, "OEM 0x64"),
    OEM65(0x65, "OEM 0x65"),
    OEM66(0x66, "OEM 0x66"),
    OEM67(0x67, "OEM 0x67"),
    OEM68(0x68, "OEM 0x68"),
    OEM69(0x69, "OEM 0x69"),
    OEM6A(0x6A, "OEM 0x6A"),
    OEM6B(0x6B, "OEM 0x6B"),
    OEM6C(0x6C, "OEM 0x6C"),
    OEM6D(0x6D, "OEM 0x6D"),
    OEM6E(0x6E, "OEM 0x6E"),
    OEM6F(0x6F, "OEM 0x6F"),
    OEM70(0x70, "OEM 0x70"),
    OEM71(0x71, "OEM 0x71"),
    OEM72(0x72, "OEM 0x72"),
    OEM73(0x73, "OEM 0x73"),
    OEM74(0x74, "OEM 0x74"),
    OEM75(0x75, "OEM 0x75"),
    OEM76(0x76, "OEM 0x76"),
    OEM77(0x77, "OEM 0x77"),
    OEM78(0x78, "OEM 0x78"),
    OEM79(0x79, "OEM 0x79"),
    OEM7A(0x7A, "OEM 0x7A"),
    OEM7B(0x7B, "OEM 0x7B"),
    OEM7C(0x7C, "OEM 0x7C"),
    OEM7D(0x7D, "OEM 0x7D"),
    OEM7E(0x7E, "OEM 0x7E"),
    OEM7F(0x7F, "OEM 0x7F");
    private final byte code;
    private final String description;

    private IpmiChannelMedium(int code, String description) {
        this.code = UnsignedBytes.checkedCast(code);
        this.description = description;
    }

    @Override
    public byte getCode() {
        return code;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name() + "(0x" + UnsignedBytes.toString(getCode(), 16) + ": " + getDescription() + ")";
    }
}
