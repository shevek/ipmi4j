/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiCommand;

/**
 *
 * @author shevek
 */
public enum SensorThreshold implements Code.Wrapper {

    LowerNonCritical(SensorBoundary.Lower, SensorCriticality.NonCritical, 0),
    LowerCritical(SensorBoundary.Lower, SensorCriticality.Critical, 1),
    LowerNonRecoverable(SensorBoundary.Lower, SensorCriticality.NonRecoverable, 2),
    UpperNonCritical(SensorBoundary.Upper, SensorCriticality.NonCritical, 3),
    UpperCritical(SensorBoundary.Upper, SensorCriticality.Critical, 4),
    UpperNonRecoverable(SensorBoundary.Upper, SensorCriticality.NonRecoverable, 5);
    private final SensorBoundary boundary;
    private final SensorCriticality criticality;
    private final byte code;

    private SensorThreshold(@Nonnull SensorBoundary boundary, @Nonnull SensorCriticality criticality, int code) {
        this.boundary = boundary;
        this.criticality = criticality;
        this.code = UnsignedBytes.checkedCast(code);
        if (code != ordinal())
            throw new IllegalArgumentException("Bad ordinal on " + this);
    }

    @Nonnull
    public SensorBoundary getBoundary() {
        return boundary;
    }

    @Nonnull
    public SensorCriticality getCriticality() {
        return criticality;
    }

    @Override
    public byte getCode() {
        return code;
    }

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
