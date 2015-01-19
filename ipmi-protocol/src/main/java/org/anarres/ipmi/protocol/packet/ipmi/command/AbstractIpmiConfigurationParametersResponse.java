/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;

/**
 *
 * @author shevek
 */
public abstract class AbstractIpmiConfigurationParametersResponse extends AbstractIpmiResponse {

    private int presentRevision;
    private int lastCompatibleRevision;
    private byte[] parameterData;

    @Override
    protected int getDataWireLength() {
        return isIpmiCompletionCodeSuccess() ? 2 + parameterData.length : 1;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        if (toWireCompletionCode(buffer))
            return;
        int tmp = (presentRevision & 0xF) << 4 | lastCompatibleRevision & 0xF;
        buffer.put((byte) tmp);
        buffer.put(parameterData);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        if (fromWireCompletionCode(buffer))
            return;
        int tmp = UnsignedBytes.toInt(buffer.get());
        presentRevision = (tmp >> 4) & 0xF;
        lastCompatibleRevision = (tmp >> 4) & 0xF;
        parameterData = readBytes(buffer, buffer.remaining());
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "PresentRevision", presentRevision);
        appendValue(buf, depth, "LastCompatibleRevision", lastCompatibleRevision);
        appendValue(buf, depth, "ParameterData", toHexString(parameterData));
    }
}
