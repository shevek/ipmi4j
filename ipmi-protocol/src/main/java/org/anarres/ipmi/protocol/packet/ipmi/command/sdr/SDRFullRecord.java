/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nonnegative;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiContext;

/**
 * [IPMI2] Section 43.1, table 43-1, pages 524-530.
 *
 * @author shevek
 */
public class SDRFullRecord extends SDRHeader {

    // Page 524.
    // Record key.
    /** 7 bits */
    public byte sensorOwnerId;

    public enum SensorOwnerIdType {

        IPMBSlaveAddress, SystemSoftwareId;
    }
    public SensorOwnerIdType sensorOwnerIdType;
    public byte sensorNumber;
    // Record body.
    public SDREntityId entityId;

    public enum EntityInstanceType {

        Physical, Logical;
    }
    public EntityInstanceType entityInstanceType;
    // Page 525.
    /** 7 bits only. */
    public byte entityInstanceNumber;

    public enum SensorInitialization implements Bits.Wrapper {

        Settable(7, true), NotSettable(7, false),
        InitScanning(6, true),
        InitEvents(5, true),
        InitThresholds(4, true),
        InitHysteresis(3, true),
        InitSensorType(2, true),
        EventGenerationDisabled(1, false), EventGenerationEnabled(1, true),
        SensorScanningDisabled(0, false), SensorScanningEnabled(0, true);
        private final Bits bits;

        /* pp */ SensorInitialization(@Nonnegative int bit, boolean value) {
            this.bits = Bits.forBitIndex(0, bit, value);
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }
    public Set<SensorInitialization> sensorInitialization = EnumSet.noneOf(SensorInitialization.class);

    public enum SensorCapabilities implements Bits.Wrapper {

        IgnoreIfNotPresent(Bits.forBitIndex(0, 7)),
        SensorRearmManual(Bits.forBitIndex(0, 6, false)), SensorRearmAutomatic(Bits.forBitIndex(0, 6, true)),
        HysteresisNone(new Bits(0, 0b11 << 4, 0b00 << 4)),
        HysteresisReadable(new Bits(0, 0b11 << 4, 0b01 << 4)),
        HysteresisReadableAndSettable(new Bits(0, 0b11 << 4, 0b10 << 4)),
        HysteresisFixed(new Bits(0, 0b11 << 4, 0b11 << 4)),
        ThresholdsNone(new Bits(0, 0b11 << 2, 0b00 << 2)),
        ThresholdsReadable(new Bits(0, 0b11 << 2, 0b01 << 2)),
        ThresholdsReadableAndSettable(new Bits(0, 0b11 << 2, 0b10 << 2)),
        ThresholdsFixed(new Bits(0, 0b11 << 2, 0b11 << 2)),
        EventMessageControlPerThreshold(new Bits(0, 0b11 << 2, 0b00 << 2)),
        EventMessageControlPerSensor(new Bits(0, 0b11 << 2, 0b01 << 2)),
        EventMessageControlGlobal(new Bits(0, 0b11 << 2, 0b10 << 2)),
        EventMessageControlNoEvents(new Bits(0, 0b11 << 2, 0b11 << 2));
        private final Bits bits;

        /* pp */ SensorCapabilities(Bits bits) {
            this.bits = bits;
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }
    // Page 526.
    public Set<SensorCapabilities> sensorCapabilities = EnumSet.noneOf(SensorCapabilities.class);
    // late fields
    public SDRFieldCodec.CodecType idCodec;
    public String id;

    @Override
    public int getWireLength(IpmiContext context) {
        return 48 + idCodec.getEncodedLength(id);
    }

    @Override
    protected void toWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void fromWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
