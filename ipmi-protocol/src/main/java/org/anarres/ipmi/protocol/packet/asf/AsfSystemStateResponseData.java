/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * SystemStateResponse.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.5 page 39.
 *
 * @author shevek
 */
public class AsfSystemStateResponseData extends AbstractAsfData {

    // TODO: Don't implement Bits.Wrapper
    /** Page 39. */
    public enum SystemState implements Bits.Wrapper, Code.Wrapper {

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

        @Override
        public byte getCode() {
            return (byte) getBits().getByteValue();
        }
    }

    // TODO: Don't implement Bits.Wrapper
    public static enum WatchdogState implements Bits.Wrapper, Code.Wrapper {

        WATCHDOG_TIMER_NORMAL(Bits.forBinaryBE(0, 0, 1, 0b0)),
        WATCHDOG_TIMER_EXPIRED(Bits.forBitIndex(0, 0));
        private final Bits bits;

        private WatchdogState(@Nonnull Bits bits) {
            this.bits = bits;
        }

        @Override
        public Bits getBits() {
            return bits;
        }

        @Override
        public byte getCode() {
            return (byte) getBits().getByteValue();
        }
    }
    private SystemState systemState = SystemState.STATE_UNKNOWN;
    private WatchdogState watchdogState = WatchdogState.WATCHDOG_TIMER_NORMAL;

    @Override
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.SystemStateResponse;
    }

    @Nonnull
    public SystemState getSystemState() {
        return systemState;
    }

    @Nonnull
    public AsfSystemStateResponseData withSystemState(SystemState systemState) {
        this.systemState = systemState;
        return this;
    }

    @Nonnull
    public WatchdogState getWatchdogState() {
        return watchdogState;
    }

    @Nonnull
    public AsfSystemStateResponseData withWatchdogState(WatchdogState watchdogState) {
        this.watchdogState = watchdogState;
        return this;
    }

    @Override
    protected int getDataWireLength() {
        return 4;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(getSystemState().getCode());
        buffer.put(getWatchdogState().getCode());
        buffer.put(new byte[2]);    // Reserved
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        withSystemState(Code.fromBuffer(SystemState.class, buffer));
        withWatchdogState(Code.fromBuffer(WatchdogState.class, buffer));
        assertWireChar(buffer, (char) 0, "reserved byte");
    }
}
