/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.messaging;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelNumber;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;

/**
 *
 * @author shevek
 */
public class GetChannelAccessRequest extends AbstractIpmiRequest {

    public enum RequestedSetting implements Code.Wrapper {

        NonVolatile(0b01),
        Volatile(0b10);
        private final byte code;
        /* pp */ RequestedSetting(int code) {
            this.code = UnsignedBytes.checkedCast(code);
        }

        @Override
        public byte getCode() {
            return code;
        }
    }
    public IpmiChannelNumber channelNumber = IpmiChannelNumber.CURRENT;
    public RequestedSetting requestedSetting;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetChannelAccess;
    }

    @Override
    protected int getDataWireLength() {
        return 2;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(channelNumber.getCode());
        buffer.put((byte) (requestedSetting.getCode() << 6));
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        channelNumber = Code.fromBuffer(IpmiChannelNumber.class, buffer);
        int tmp = UnsignedBytes.toInt(buffer.get());
        requestedSetting = Code.fromInt(RequestedSetting.class, (tmp >> 6) & 0x3);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "ChannelNumber", channelNumber);
        appendValue(buf, depth, "RequestedSetting", requestedSetting);
    }
}
