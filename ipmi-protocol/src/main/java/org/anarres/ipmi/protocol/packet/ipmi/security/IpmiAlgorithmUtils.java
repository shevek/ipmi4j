/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.security;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public class IpmiAlgorithmUtils {

    public static byte getWireLength() {
        return 8;
    }

    public static void toWireUnchecked(@Nonnull ByteBuffer buffer, @Nonnull IpmiAlgorithm algorithm) {
        buffer.put(algorithm.getPayloadType());
        buffer.putChar((char) 0);      // reserved
        buffer.put(getWireLength());
        buffer.put(algorithm.getCode());
        buffer.put((byte) 0);    // reserved
        buffer.putChar((char) 0);      // reserved
    }

    @Nonnull
    public static <T extends Enum<T> & IpmiAlgorithm> T fromWireUnchecked(@Nonnull ByteBuffer buffer, @Nonnull Class<T> algorithmType) {
        byte algorithmTypeByte = algorithmType.getEnumConstants()[0].getPayloadType();
        AbstractWireable.assertWireByte(buffer, algorithmTypeByte, "Algorithm type byte");
        AbstractWireable.assertWireBytesZero(buffer, 2);
        AbstractWireable.assertWireByte(buffer, getWireLength(), "Algorithm wire length");
        T algorithm = Code.fromByte(algorithmType, buffer.get());
        AbstractWireable.assertWireBytesZero(buffer, 2);
        buffer.get();   // This is set to 8 in the packet trace I grabbed.
        return algorithm;
    }
}