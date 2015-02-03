/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * An IPMB address.
 *
 * Introduces a little type-safety to IPMB addresses.
 * Also handles the left-shift for wire encoding.
 *
 * @author shevek
 */
public enum IPMBAddress implements Code.Wrapper {

    Addresss00(0x00),
    Addresss01(0x01),
    Addresss02(0x02),
    Addresss03(0x03),
    Addresss04(0x04),
    Addresss05(0x05),
    Addresss06(0x06),
    Addresss07(0x07),
    Addresss08(0x08),
    Addresss09(0x09),
    Addresss0A(0x0A),
    Addresss0B(0x0B),
    Addresss0C(0x0C),
    Addresss0D(0x0D),
    Addresss0E(0x0E),
    Addresss0F(0x0F),
    Addresss10(0x10),
    Addresss11(0x11),
    Addresss12(0x12),
    Addresss13(0x13),
    Addresss14(0x14),
    Addresss15(0x15),
    Addresss16(0x16),
    Addresss17(0x17),
    Addresss18(0x18),
    Addresss19(0x19),
    Addresss1A(0x1A),
    Addresss1B(0x1B),
    Addresss1C(0x1C),
    Addresss1D(0x1D),
    Addresss1E(0x1E),
    Addresss1F(0x1F),
    Addresss20(0x20),
    Addresss21(0x21),
    Addresss22(0x22),
    Addresss23(0x23),
    Addresss24(0x24),
    Addresss25(0x25),
    Addresss26(0x26),
    Addresss27(0x27),
    Addresss28(0x28),
    Addresss29(0x29),
    Addresss2A(0x2A),
    Addresss2B(0x2B),
    Addresss2C(0x2C),
    Addresss2D(0x2D),
    Addresss2E(0x2E),
    Addresss2F(0x2F),
    Addresss30(0x30),
    Addresss31(0x31),
    Addresss32(0x32),
    Addresss33(0x33),
    Addresss34(0x34),
    Addresss35(0x35),
    Addresss36(0x36),
    Addresss37(0x37),
    Addresss38(0x38),
    Addresss39(0x39),
    Addresss3A(0x3A),
    Addresss3B(0x3B),
    Addresss3C(0x3C),
    Addresss3D(0x3D),
    Addresss3E(0x3E),
    Addresss3F(0x3F),
    Addresss40(0x40),
    Addresss41(0x41),
    Addresss42(0x42),
    Addresss43(0x43),
    Addresss44(0x44),
    Addresss45(0x45),
    Addresss46(0x46),
    Addresss47(0x47),
    Addresss48(0x48),
    Addresss49(0x49),
    Addresss4A(0x4A),
    Addresss4B(0x4B),
    Addresss4C(0x4C),
    Addresss4D(0x4D),
    Addresss4E(0x4E),
    Addresss4F(0x4F),
    Addresss50(0x50),
    Addresss51(0x51),
    Addresss52(0x52),
    Addresss53(0x53),
    Addresss54(0x54),
    Addresss55(0x55),
    Addresss56(0x56),
    Addresss57(0x57),
    Addresss58(0x58),
    Addresss59(0x59),
    Addresss5A(0x5A),
    Addresss5B(0x5B),
    Addresss5C(0x5C),
    Addresss5D(0x5D),
    Addresss5E(0x5E),
    Addresss5F(0x5F),
    Addresss60(0x60),
    Addresss61(0x61),
    Addresss62(0x62),
    Addresss63(0x63),
    Addresss64(0x64),
    Addresss65(0x65),
    Addresss66(0x66),
    Addresss67(0x67),
    Addresss68(0x68),
    Addresss69(0x69),
    Addresss6A(0x6A),
    Addresss6B(0x6B),
    Addresss6C(0x6C),
    Addresss6D(0x6D),
    Addresss6E(0x6E),
    Addresss6F(0x6F),
    Addresss70(0x70),
    Addresss71(0x71),
    Addresss72(0x72),
    Addresss73(0x73),
    Addresss74(0x74),
    Addresss75(0x75),
    Addresss76(0x76),
    Addresss77(0x77),
    Addresss78(0x78),
    Addresss79(0x79),
    Addresss7A(0x7A),
    Addresss7B(0x7B),
    Addresss7C(0x7C),
    Addresss7D(0x7D),
    Addresss7E(0x7E),
    Addresss7F(0x7F);
    private final int code;

    private IPMBAddress(@Nonnegative int code) {
        this.code = code;
    }

    /** The low 7 bits only. */
    public byte getI2CSlaveAddress() {
        return (byte) code;
    }

    /**
     * Returns the I2C Slave address in the high 7 bits, and sets the low bit to 0.
     */
    @Override
    public byte getCode() {
        return (byte) (code << 1);
    }
}
