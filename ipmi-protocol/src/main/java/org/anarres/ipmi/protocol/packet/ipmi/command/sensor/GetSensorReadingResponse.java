/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import com.google.common.primitives.UnsignedBytes;
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
 * [IPMI2] Section 35.14, table 35-15, page 470.
 *
 * @author shevek
 */
public class GetSensorReadingResponse extends AbstractIpmiResponse {

    public enum SensorState implements Bits.Wrapper {

        EventMessagesEnabled(7),
        SensorScanningEnabled(6),
        ReadingUnavailable(5);
        private final Bits bits;

        /* pp */ SensorState(@Nonnegative int bit) {
            this.bits = Bits.forBitIndex(0, bit);
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }
    public byte sensorValue;
    public Set<SensorState> sensorState = EnumSet.noneOf(SensorState.class);
    /** For a continuous sensor, should have the top two bits of the low byte set to 1. */
    public char sensorFlags;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSensorReading;
    }

    @Override
    public void apply(IpmiClientCommandHandler handler) {
        handler.handleGetSensorReadingResponse(this);
    }

    @Override
    protected int getResponseDataWireLength() {
        return 5;
    }

    @Nonnull
    public Set<SensorThreshold> getSensorThresholdFailures() {
        Set<SensorThreshold> out = EnumSet.noneOf(SensorThreshold.class);
        for (SensorThreshold t : SensorThreshold.values())
            if (getBit((byte) sensorFlags, t.getCode()))
                out.add(t);
        return out;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        buffer.put(sensorValue);
        buffer.put(Bits.toByte(sensorState));
        toWireCharLE(buffer, sensorFlags);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        sensorValue = buffer.get();
        sensorState = Bits.fromBuffer(SensorState.class, buffer, 1);
        sensorFlags = fromWireCharLE(buffer);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "SensorValue", UnsignedBytes.toInt(sensorValue));
        appendValue(buf, depth, "SensorState", sensorState);
        appendValue(buf, depth, "SensorFlags", Integer.toBinaryString(sensorFlags));
        appendValue(buf, depth, "SensorThresholdFailures", getSensorThresholdFailures());
    }
}
