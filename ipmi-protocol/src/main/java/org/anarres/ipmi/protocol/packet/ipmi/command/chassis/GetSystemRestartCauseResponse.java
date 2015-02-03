/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.chassis;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelNumber;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 * [IPMI2] Section 28.2, table 28-3, page 389.
 *
 * @author shevek
 */
public class GetSystemRestartCauseResponse extends AbstractIpmiResponse {

    public static enum RestartCause implements Code.DescriptiveWrapper {

        Unknown(0x00, "unknown (system start/restart detected, but cause unknown)"),
        ChassisControlCommand(0x01, "Chassis Control command"),
        PushButtonReset(0x02, "reset via pushbutton"),
        PushButtonPowerUp(0x03, "power-up via power pushbutton"),
        WatchdogExpiration(0x04, "Watchdog expiration (see watchdog flags)"),
        OEM(0x05, "OEM"),
        AutomaticAlwaysRestore(0x06, "automatic power-up on AC being applied due to 'always restore' power restore policy "),
        AutomaticRestorePrevious(0x07, "automatic power-up on AC being applied due to 'restore previous power state' power restore policy"),
        PEFReset(0x08, "reset via PEF"),
        PEFPowerCycle(0x09, "power-cycle via PEF"),
        SoftReset(0x0A, "soft reset (e.g. CTRL-ALT-DEL)"),
        RTCPowerUp(0x0B, "power-up via RTC (system real time clock) wakeup");
        private final byte code;
        private final String description;
        /* pp */ private RestartCause(@Nonnegative int code, @Nonnull String description) {
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
            return Code.Utils.toString(this);
        }
    }
    public RestartCause restartCause;
    public IpmiChannelNumber restartChannel;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSystemRestartCause;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiSession session) {
        handler.handleGetSystemRestartCauseResponse(session, this);
    }

    @Override
    protected int getResponseDataWireLength() {
        return 3;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        buffer.put(restartCause.getCode());
        buffer.put(restartChannel.getCode());
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        byte tmp = buffer.get();
        restartCause = Code.fromInt(RestartCause.class, tmp & 0xF);
        restartChannel = Code.fromBuffer(IpmiChannelNumber.class, buffer);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "RestartCause", restartCause);
        appendValue(buf, depth, "RestartChannel", restartChannel);
    }
}
