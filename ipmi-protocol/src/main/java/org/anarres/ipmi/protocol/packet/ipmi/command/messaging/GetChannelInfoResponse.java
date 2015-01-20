/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.messaging;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.IanaEnterpriseNumber;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelMedium;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelNumber;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelProtocol;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;

/**
 *
 * @author shevek
 */
public class GetChannelInfoResponse extends AbstractIpmiResponse {

    public enum ChannelSessionSupport implements Code.Wrapper {

        Sessionless(0b00),
        SingleSession(0b01),
        MultiSession(0b10),
        SessionBased(0b11);
        private final byte code;
        /* pp */ ChannelSessionSupport(@Nonnegative int code) {
            this.code = UnsignedBytes.checkedCast(code);
        }

        @Override
        public byte getCode() {
            return code;
        }
    }

    public enum ChannelInterruptType implements Code.Wrapper {

        IRQ0(0x00),
        IRQ1(0x01),
        IRQ2(0x02),
        IRQ3(0x03),
        IRQ4(0x04),
        IRQ5(0x05),
        IRQ6(0x06),
        IRQ7(0x07),
        IRQ8(0x08),
        IRQ9(0x09),
        IRQA(0x0A),
        IRQB(0x0B),
        IRQC(0x0C),
        IRQD(0x0D),
        IRQE(0x0E),
        IRQF(0x0F),
        PCIA(0x10),
        PCIB(0x11),
        PCIC(0x12),
        PCID(0x13),
        SMI(0x14),
        SCI(0x15),
        SystemInterrupt0(0x20),
        SystemInterrupt1(0x21),
        SystemInterrupt2(0x22),
        SystemInterrupt3(0x23),
        SystemInterrupt4(0x24),
        SystemInterrupt5(0x25),
        SystemInterrupt6(0x26),
        SystemInterrupt7(0x27),
        SystemInterrupt8(0x28),
        SystemInterrupt9(0x29),
        SystemInterrupt10(0x2A),
        SystemInterrupt11(0x2B),
        SystemInterrupt12(0x2C),
        SystemInterrupt13(0x2D),
        SystemInterrupt14(0x2E),
        SystemInterrupt15(0x2F),
        SystemInterrupt16(0x30),
        SystemInterrupt17(0x31),
        SystemInterrupt18(0x32),
        SystemInterrupt19(0x33),
        SystemInterrupt20(0x34),
        SystemInterrupt21(0x35),
        SystemInterrupt22(0x36),
        SystemInterrupt23(0x37),
        SystemInterrupt24(0x38),
        SystemInterrupt25(0x39),
        SystemInterrupt26(0x3A),
        SystemInterrupt27(0x3B),
        SystemInterrupt28(0x3C),
        SystemInterrupt29(0x3D),
        SystemInterrupt30(0x3E),
        SystemInterrupt31(0x3F),
        SystemInterrupt32(0x40),
        SystemInterrupt33(0x41),
        SystemInterrupt34(0x42),
        SystemInterrupt35(0x43),
        SystemInterrupt36(0x44),
        SystemInterrupt37(0x45),
        SystemInterrupt38(0x46),
        SystemInterrupt39(0x47),
        SystemInterrupt40(0x48),
        SystemInterrupt41(0x49),
        SystemInterrupt42(0x4A),
        SystemInterrupt43(0x4B),
        SystemInterrupt44(0x4C),
        SystemInterrupt45(0x4D),
        SystemInterrupt46(0x4E),
        SystemInterrupt47(0x4F),
        SystemInterrupt48(0x50),
        SystemInterrupt49(0x51),
        SystemInterrupt50(0x52),
        SystemInterrupt51(0x53),
        SystemInterrupt52(0x54),
        SystemInterrupt53(0x55),
        SystemInterrupt54(0x56),
        SystemInterrupt55(0x57),
        SystemInterrupt56(0x58),
        SystemInterrupt57(0x59),
        SystemInterrupt58(0x5A),
        SystemInterrupt59(0x5B),
        SystemInterrupt60(0x5C),
        SystemInterrupt61(0x5D),
        SystemInterrupt62(0x5E),
        SystemInterrupt63(0x5F),
        ACPI(0x60),
        None(0xFF);
        private final byte code;
        /* pp */ ChannelInterruptType(@Nonnegative int code) {
            this.code = UnsignedBytes.checkedCast(code);
        }

        @Override
        public byte getCode() {
            return code;
        }
    }
    private IpmiChannelNumber channelNumber;
    private IpmiChannelMedium channelMedium;
    private IpmiChannelProtocol channelProtocol;
    private ChannelSessionSupport channelSessionSupport;
    private int channelSessionCount;
    private int oemEnterpriseNumber = IanaEnterpriseNumber.Intelligent_Platform_Management_Interface_forum.getNumber();
    private ChannelInterruptType smsInterruptType;
    private ChannelInterruptType eventMessageBufferInterruptType;
    private byte oem0;
    private byte oem1;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetChannelInfoCommand;
    }

    @Override
    protected int getResponseDataWireLength() {
        return 10;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        buffer.put(channelNumber.getCode());
        buffer.put(channelMedium.getCode());
        buffer.put(channelProtocol.getCode());

        int tmp = channelSessionSupport.getCode() << 6 | channelSessionCount & 0x3F;
        buffer.put((byte) tmp);

        toWireOemIanaLE3(buffer, oemEnterpriseNumber);

        if (IpmiChannelNumber.CF.equals(channelNumber)) {
            buffer.put(smsInterruptType.getCode());
            buffer.put(eventMessageBufferInterruptType.getCode());
        } else if (oemEnterpriseNumber != IanaEnterpriseNumber.Intelligent_Platform_Management_Interface_forum.getNumber()) {
            buffer.put(oem0);
            buffer.put(oem1);
        } else {
            buffer.putChar((char) 0);
        }
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        channelNumber = Code.fromBuffer(IpmiChannelNumber.class, buffer);
        channelMedium = Code.fromBuffer(IpmiChannelMedium.class, buffer);
        channelProtocol = Code.fromBuffer(IpmiChannelProtocol.class, buffer);

        int tmp = UnsignedBytes.toInt(buffer.get());
        channelSessionSupport = Code.fromInt(ChannelSessionSupport.class, tmp >> 6);
        channelSessionCount = tmp & 0x3F;

        oemEnterpriseNumber = fromWireOemIanaLE3(buffer);
        if (IpmiChannelNumber.CF.equals(channelNumber)) {
            smsInterruptType = Code.fromBuffer(ChannelInterruptType.class, buffer);
            eventMessageBufferInterruptType = Code.fromBuffer(ChannelInterruptType.class, buffer);
        } else if (oemEnterpriseNumber != IanaEnterpriseNumber.Intelligent_Platform_Management_Interface_forum.getNumber()) {
            oem0 = buffer.get();
            oem1 = buffer.get();
        } else {
            assertWireBytesZero(buffer, 2);
        }
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "ChannelNumber", channelNumber);
        appendValue(buf, depth, "ChannelMedium", channelMedium);
        appendValue(buf, depth, "ChannelProtocol", channelProtocol);
        appendValue(buf, depth, "ChannelSessionSupport", channelSessionSupport);
        appendValue(buf, depth, "ChannelSessionCount", channelSessionCount);
        appendValue(buf, depth, "OemEnterpriseNumber", toStringOemIana(oemEnterpriseNumber));
        appendValue(buf, depth, "SmsInterruptType", smsInterruptType);
        appendValue(buf, depth, "EventMessageBufferInterruptType", eventMessageBufferInterruptType);
        appendValue(buf, depth, "OEM Data", toHexString(oem0, oem1));
    }
}
