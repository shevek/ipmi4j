/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.chassis;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.IpmiClientCommandHandler;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;

/**
 * [IPMI2] Section 28.2, table 28-3, page 389.
 *
 * @author shevek
 */
public class GetChassisStatusResponse extends AbstractIpmiResponse {

    public enum CurrentPowerState implements Bits.Wrapper {

        PowerRestorePolicyOff(new Bits(0, 0b11 << 5, 0b00 << 5)),
        PowerRestorePolicyPrevious(new Bits(0, 0b11 << 5, 0b01 << 5)),
        PowerRestorePolicyOn(new Bits(0, 0b11 << 5, 0b10 << 5)),
        PowerRestorePolicyUnknown(new Bits(0, 0b11 << 5, 0b11 << 5)),
        PowerControlFault(4),
        PowerFault(3),
        PowerInterlock(2),
        PowerOverload(1),
        PowerOn(0);
        private final Bits bits;

        /* pp */ CurrentPowerState(@Nonnull Bits bits) {
            this.bits = bits;
        }

        /* pp */ CurrentPowerState(@Nonnegative int bit) {
            this(Bits.forBitIndex(0, bit));
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }

    public enum LastPowerEvent implements Bits.Wrapper {

        LastPoweredOnViaIPMI(4),
        LastPoweredDownByPowerFault(3),
        LastPoweredDownByInterlock(2),
        LastPoweredDownByOverload(1),
        LastPoweredDownByACFailure(0);
        private final Bits bits;
        /* pp */ LastPowerEvent(@Nonnegative int bit) {
            this.bits = Bits.forBitIndex(0, bit);
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }

    public enum MiscChassisState implements Bits.Wrapper {

        ChassisIdentifyCommandAndStateInfoSupported(6),
        ClassisIdentifyStateOff(new Bits(0, 0b11 << 4, 0b00 << 4)),
        ClassisIdentifyStateTemporaryOn(new Bits(0, 0b11 << 4, 0b01 << 4)),
        ClassisIdentifyStateIndefiniteOn(new Bits(0, 0b11 << 4, 0b10 << 4)),
        ClassisIdentifyStateReserved(new Bits(0, 0b11 << 4, 0b11 << 4)),
        CoolingFanFaultDetected(3),
        DriveFaultDetected(2),
        FrontPanelLockoutActive(1),
        ChassisIntrusionActive(0);
        private final Bits bits;

        /* pp */ MiscChassisState(@Nonnull Bits bits) {
            this.bits = bits;
        }

        /* pp */ MiscChassisState(@Nonnegative int bit) {
            this(Bits.forBitIndex(0, bit));
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }
    private Set<CurrentPowerState> currentPowerState = EnumSet.noneOf(CurrentPowerState.class);
    private Set<LastPowerEvent> lastPowerEvent = EnumSet.noneOf(LastPowerEvent.class);
    private Set<MiscChassisState> miscChassisState = EnumSet.noneOf(MiscChassisState.class);

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetChassisStatus;
    }

    @Override
    public void apply(IpmiClientCommandHandler handler) {
        handler.handleGetChassisStatusResponse(this);
    }

    @Override
    protected int getResponseDataWireLength() {
        return 4;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        buffer.put(Bits.toByte(currentPowerState));
        buffer.put(Bits.toByte(lastPowerEvent));
        buffer.put(Bits.toByte(miscChassisState));
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        currentPowerState = Bits.fromBuffer(CurrentPowerState.class, buffer, 1);
        lastPowerEvent = Bits.fromBuffer(LastPowerEvent.class, buffer, 1);
        miscChassisState = Bits.fromBuffer(MiscChassisState.class, buffer, 1);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "CurrentPowerState", currentPowerState);
        appendValue(buf, depth, "LastPowerEvent", lastPowerEvent);
        appendValue(buf, depth, "MiscChassisState", miscChassisState);
    }
}
