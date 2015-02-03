/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiResponse;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 * [IPMI2] Section 35.9, table 35-9, page 459.
 *
 * @author shevek
 */
public class GetSensorThresholdResponse extends AbstractIpmiResponse {

    // TODO: -> EnumMap<SensorThreshold, Byte>?
    Map<SensorThreshold, Byte> thresholds = new EnumMap<>(SensorThreshold.class);

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSensorThreshold;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiSession session) {
        handler.handleGetSensorThresholdResponse(session, this);
    }

    @Override
    protected int getResponseDataWireLength() {
        return 8;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        byte tmp = 0;
        for (SensorThreshold t : SensorThreshold.values())
            tmp = setBit(tmp, t.getCode(), thresholds.get(t) != null);
        buffer.put(tmp);
        for (SensorThreshold t : SensorThreshold.values())
            SensorThreshold.toBuffer(buffer, thresholds.get(t));
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        byte tmp = buffer.get();
        for (SensorThreshold t : SensorThreshold.values())
            thresholds.put(t, SensorThreshold.fromBuffer(buffer, tmp, t.getCode()));
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        for (SensorThreshold t : SensorThreshold.values())
            appendValue(buf, depth, t.name(), SensorThreshold.toString(thresholds.get(t)));
    }
}
