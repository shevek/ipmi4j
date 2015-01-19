/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;

/**
 * [IPMI2] Section 35.9, table 35-9, page 459.
 *
 * @author shevek
 */
public class GetSensorThresholdRequest extends AbstractIpmiRequest {

    public byte sensorNumber;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.GetSensorThreshold;
    }

    @Override
    protected int getDataWireLength() {
        return 1;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(sensorNumber);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        sensorNumber = buffer.get();
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "sensorNumber", "0x" + UnsignedBytes.toString(sensorNumber, 16));
    }
}
