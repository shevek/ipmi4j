/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiCommand;

/**
 *
 * @author shevek
 */
public enum SensorThreshold {

    UpperNonRecoverable,
    UpperCritical,
    UpperNonCritical,
    LowerNonRecoverable,
    LowerCritical,
    LowerNonCritical;

    /* pp */ static void toBuffer(@Nonnull ByteBuffer buffer, @CheckForNull Byte value) {
        if (value == null)
            buffer.put((byte) 0);
        else
            buffer.put(value.byteValue());
    }

    @CheckForNull
    /* pp */ static Byte fromBuffer(@Nonnull ByteBuffer buffer, @Nonnull byte flags, int bit) {
        byte value = buffer.get();
        if (AbstractIpmiCommand.getBit(flags, bit))
            return value;
        else
            return null;
    }

    @Nonnull
    /* pp */ static String toString(@CheckForNull Byte value) {
        if (value == null)
            return "Not specified";
        return UnsignedBytes.toString(value);
    }
}
