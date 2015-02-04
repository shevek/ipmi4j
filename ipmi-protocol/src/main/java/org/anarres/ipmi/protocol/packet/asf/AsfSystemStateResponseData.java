/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientAsfMessageHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [ASF2] Section 3.2.4.5, page 39.
 *
 * @author shevek
 */
public class AsfSystemStateResponseData extends AbstractAsfData {

    // TODO: Don't implement Bits.Wrapper
    /** [ASF2] Section 3.2.4.5, page 39. */
    public enum SystemState implements Code.Wrapper {

        STATE_WORKING(0b0000),
        STATE_S1(0b0001),
        STATE_S2(0b0010),
        STATE_S3(0b0011),
        STATE_S4(0b0100),
        STATE_S5(0b0101),
        STATE_S4_S5(0b0110),
        STATE_G3(0b0111),
        STATE_G1(0b1000),
        STATE_S5_OVERRIDE(0b1001),
        STATE_LEGACY_ON(0b1011),
        STATE_LEGACY_OFF(0b1100),
        STATE_UNKNOWN(0b1110),
        STATE_RESERVED(0b1110);
        private final byte code;

        private SystemState(@Nonnegative int code) {
            this.code = UnsignedBytes.checkedCast(code);
        }

        @Override
        public byte getCode() {
            return code;
        }
    }

    // TODO: Don't implement Bits.Wrapper
    public static enum WatchdogState implements Bits.Wrapper, Code.Wrapper {

        WATCHDOG_TIMER_NOT_EXPIRED(Bits.forBitIndex(0, 0, false)),
        WATCHDOG_TIMER_EXPIRED(Bits.forBitIndex(0, 0, true));
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
    private WatchdogState watchdogState = WatchdogState.WATCHDOG_TIMER_NOT_EXPIRED;

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
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.SystemStateResponse;
    }

    @Override
    public void apply(IpmiClientAsfMessageHandler handler, IpmiHandlerContext context) {
        handler.handleAsfSystemStateResponseData(context, this);
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
        assertWireBytesZero(buffer, 2);
    }
}
