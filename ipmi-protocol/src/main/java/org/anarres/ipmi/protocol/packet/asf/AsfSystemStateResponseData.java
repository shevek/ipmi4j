/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Bits;

/**
 *
 * @author shevek
 */
public class AsfSystemStateResponseData extends AbstractAsfData {

    /** Page 39. */
    public enum SystemState implements Bits.Wrapper {

        STATE_WORKING(Bits.forBinaryBE(0, 3, 4, 0b0000)),
        STATE_S1(Bits.forBinaryBE(0, 3, 4, 0b0001)),
        STATE_S2(Bits.forBinaryBE(0, 3, 4, 0b0010)),
        STATE_S3(Bits.forBinaryBE(0, 3, 4, 0b0011)),
        STATE_S4(Bits.forBinaryBE(0, 3, 4, 0b0100)),
        STATE_S5(Bits.forBinaryBE(0, 3, 4, 0b0101)),
        STATE_S4_S5(Bits.forBinaryBE(0, 3, 4, 0b0110)),
        STATE_G3(Bits.forBinaryBE(0, 3, 4, 0b0111)),
        STATE_G1(Bits.forBinaryBE(0, 3, 4, 0b1000)),
        STATE_S5_OVERRIDE(Bits.forBinaryBE(0, 3, 4, 0b1001)),
        STATE_LEGACY_ON(Bits.forBinaryBE(0, 3, 4, 0b1011)),
        STATE_LEGACY_OFF(Bits.forBinaryBE(0, 3, 4, 0b1100)),
        STATE_UNKNOWN(Bits.forBinaryBE(0, 3, 4, 0b1110)),
        STATE_RESERVED(Bits.forBinaryBE(0, 3, 4, 0b1110));
        private final Bits bits;

        private SystemState(@Nonnull Bits bits) {
            this.bits = bits;
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }

    public static enum WatchdogState implements Bits.Wrapper {

        WATCHDOG_TIMER_EXPIRED(Bits.forBitIndex(0, 0));
        private final Bits bits;

        private WatchdogState(@Nonnull Bits bits) {
            this.bits = bits;
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }
    private final Set<SystemState> systemState = EnumSet.noneOf(SystemState.class);
    private final Set<WatchdogState> watchdogState = EnumSet.noneOf(WatchdogState.class);

    @Override
    public AsfRcmpMessageType getMessageType() {
        return AsfRcmpMessageType.SystemStateResponse;
    }

    @Nonnull
    public Set<? extends SystemState> getSystemState() {
        return systemState;
    }

    @Nonnull
    public Set<? extends WatchdogState> getWatchdogState() {
        return watchdogState;
    }

    @Override
    protected int getDataWireLength() {
        return 4;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(Bits.toByte(getSystemState()));
        buffer.put(Bits.toByte(getWatchdogState()));
        buffer.put(new byte[2]);    // Reserved
    }
}
