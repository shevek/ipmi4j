/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.alg;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public class IpmiAlgorithmPayload<T extends Enum<T> & IpmiAlgorithm> extends AbstractWireable {

    private final Class<T> algorithmType;
    private final byte algorithmTypeByte;
    private T algorithm;

    public IpmiAlgorithmPayload(@Nonnull Class<T> algorithmType) {
        this.algorithmType = algorithmType;
        this.algorithmTypeByte = algorithmType.getEnumConstants()[0].getPayloadType();
    }

    @Override
    public int getWireLength() {
        return 8;
    }

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        buffer.put(algorithm.getPayloadType());
        buffer.putChar((char) 0);      // reserved
        buffer.put((byte) getWireLength());
        buffer.put(algorithm.getCode());
        buffer.put((byte) 0);    // reserved
        buffer.putChar((char) 0);      // reserved
    }

    @Override
    protected void fromWireUnchecked(@Nonnull ByteBuffer buffer) {
        assertWireBytes(buffer, algorithmTypeByte, 0, 0, getWireLength());
        algorithm = Code.fromByte(algorithmType, buffer.get());
        assertWireBytes(buffer, 0, 0, 0);
    }
}