/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import java.nio.ByteBuffer;
import javax.annotation.CheckForNull;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;

/**
 * [IPMI2] Section 35.9, table 35-9, page 459.
 *
 * @author shevek
 */
public class GetSensorThresholdResponse extends AbstractIpmiResponse {

    // TODO: -> EnumMap<SensorThreshold, Byte>?
    @CheckForNull
    public Byte lowerNonCritical;
    @CheckForNull
    public Byte lowerCritical;
    @CheckForNull
    public Byte lowerNonRecoverable;
    @CheckForNull
    public Byte upperNonCritical;
    @CheckForNull
    public Byte upperCritical;
    @CheckForNull
    public Byte upperNonRecoverable;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSensorThreshold;
    }

    @Override
    protected int getDataWireLength() {
        return 8;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        byte tmp = 0;
        tmp = setBit(tmp, 5, upperNonRecoverable != null);
        tmp = setBit(tmp, 4, upperCritical != null);
        tmp = setBit(tmp, 3, upperNonCritical != null);
        tmp = setBit(tmp, 2, lowerNonRecoverable != null);
        tmp = setBit(tmp, 1, lowerCritical != null);
        tmp = setBit(tmp, 0, lowerNonCritical != null);
        buffer.put(tmp);
        SensorThreshold.toBuffer(buffer, lowerNonCritical);
        SensorThreshold.toBuffer(buffer, lowerCritical);
        SensorThreshold.toBuffer(buffer, lowerNonRecoverable);
        SensorThreshold.toBuffer(buffer, upperNonCritical);
        SensorThreshold.toBuffer(buffer, upperCritical);
        SensorThreshold.toBuffer(buffer, upperNonRecoverable);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        byte tmp = buffer.get();
        lowerNonCritical = SensorThreshold.fromBuffer(buffer, tmp, 0);
        lowerCritical = SensorThreshold.fromBuffer(buffer, tmp, 1);
        lowerNonRecoverable = SensorThreshold.fromBuffer(buffer, tmp, 2);
        upperNonCritical = SensorThreshold.fromBuffer(buffer, tmp, 3);
        upperCritical = SensorThreshold.fromBuffer(buffer, tmp, 4);
        upperNonRecoverable = SensorThreshold.fromBuffer(buffer, tmp, 5);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "LowerNonCritical", SensorThreshold.toString(lowerNonCritical));
        appendValue(buf, depth, "LowerCritical", SensorThreshold.toString(lowerCritical));
        appendValue(buf, depth, "LowerNonRecoverable", SensorThreshold.toString(lowerNonRecoverable));
        appendValue(buf, depth, "UpperNonCritical", SensorThreshold.toString(upperNonCritical));
        appendValue(buf, depth, "UpperCritical", SensorThreshold.toString(upperCritical));
        appendValue(buf, depth, "UpperNonRecoverable", SensorThreshold.toString(upperNonRecoverable));
    }
}
