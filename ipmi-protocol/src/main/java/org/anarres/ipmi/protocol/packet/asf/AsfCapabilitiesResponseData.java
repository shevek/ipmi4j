/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import com.google.common.collect.Iterables;
import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientAsfMessageHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.common.Bits;

/**
 * CapabilitiesResponse.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.4 pages 36-37.
 *
 * @author shevek
 */
public class AsfCapabilitiesResponseData extends AbstractAsfData {

    /** Page 37. */
    public enum SpecialCommand implements Bits.Wrapper {

        SUPPORTS_FORCE_CD_BOOT(1, 4),
        SUPPORTS_FORCE_DIAGNOSTIC_BOOT(1, 3),
        SUPPORTS_FORCE_HARD_DRIVE_SAFE_MODE_BOOT(1, 2),
        SUPPORTS_FORCE_HARD_DRIVE_BOOT(1, 1),
        SUPPORTS_FORCE_PXE_BOOT(1, 0);
        private final Bits bits;

        private SpecialCommand(@Nonnegative int byteIndex, @Nonnegative int bitIndex) {
            this.bits = Bits.forBitIndex(byteIndex, bitIndex);
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }

    /** Page 37. */
    public enum SystemCapability implements Bits.Wrapper {

        SUPPORTS_RESET_BOTH_PORTS(0, 7),
        SUPPORTS_POWER_UP_BOTH_PORTS(0, 6),
        SUPPORTS_POWER_DOWN_BOTH_PORTS(0, 5),
        SUPPORTS_POWER_CYCLE_RESET_BOTH_PORTS(0, 4),
        SUPPORTS_RESET_SECURE_PORT_ONLY(0, 3),
        SUPPORTS_POWER_UP_SECURE_PORT_ONLY(0, 2),
        SUPPORTS_POWER_DOWN_SECURE_PORT_ONLY(0, 1),
        SUPPORTS_POWER_CYCLE_RESET_SECURE_PORT_ONLY(0, 0);
        private final Bits bits;

        private SystemCapability(@Nonnegative int byteIndex, @Nonnegative int bitIndex) {
            this.bits = Bits.forBitIndex(byteIndex, bitIndex);
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }

    /** Page 38. */
    public enum SystemFirmwareCapability implements Bits.Wrapper {

        SUPPORTS_LOCK_SLEEP_BUTTON(0, 6),
        SUPPORTS_LOCK_KEYBOARD(0, 5),
        SUPPORTS_LOCK_RESET_BUTTON(0, 2),
        SUPPORTS_LOCK_POWER_BUTTON(0, 1),
        SUPPORTS_SCREEN_BLANK(0, 0),
        SUPPORTS_CONFIGURATION_DATA_RESET(1, 7),
        SUPPORTS_FIRMWARE_VERBOSITY_QUIET(1, 6),
        SUPPORTS_FIRMWARE_VERBOSITY_VERBOSE(1, 5),
        SUPPORTS_FORCED_PROGRESS_EVENTS(1, 4),
        SUPPORTS_USER_PASSWORD_BYPASS(1, 3);
        private final Bits bits;

        private SystemFirmwareCapability(@Nonnegative int byteIndex, @Nonnegative int bitIndex) {
            this.bits = Bits.forBitIndex(byteIndex, bitIndex);
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }
    private int oemDefined;
    private final Set<SpecialCommand> specialCommands = EnumSet.noneOf(SpecialCommand.class);
    private final Set<SystemCapability> systemCapabilities = EnumSet.noneOf(SystemCapability.class);
    private final Set<SystemFirmwareCapability> systemFirmwareCapabilities = EnumSet.noneOf(SystemFirmwareCapability.class);

    @Override
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.CapabilitiesResponse;
    }

    @Override
    public void apply(IpmiClientAsfMessageHandler handler, IpmiHandlerContext context) {
        handler.handleAsfCapabilitiesResponseData(context, this);
    }

    public int getOemDefined() {
        return oemDefined;
    }

    @Nonnull
    public AsfCapabilitiesResponseData withOemDefined(int oemDefined) {
        this.oemDefined = oemDefined;
        return this;
    }

    @Nonnull
    public Set<? extends SpecialCommand> getSpecialCommands() {
        return specialCommands;
    }

    @Nonnull
    public AsfCapabilitiesResponseData withSpecialCommands(Iterable<? extends SpecialCommand> specialCommands) {
        Iterables.addAll(this.specialCommands, specialCommands);
        return this;
    }

    @Nonnull
    public Set<? extends SystemCapability> getSystemCapabilities() {
        return systemCapabilities;
    }

    @Nonnull
    public AsfCapabilitiesResponseData withSystemCapabilities(Iterable<? extends SystemCapability> systemCapabilities) {
        Iterables.addAll(this.systemCapabilities, systemCapabilities);
        return this;
    }

    @Nonnull
    public Set<? extends SystemFirmwareCapability> getSystemFirmwareCapabilities() {
        return systemFirmwareCapabilities;
    }

    @Nonnull
    public AsfCapabilitiesResponseData withSystemFirmwareCapabilities(Iterable<? extends SystemFirmwareCapability> systemFirmwareCapabilities) {
        Iterables.addAll(this.systemFirmwareCapabilities, systemFirmwareCapabilities);
        return this;
    }

    @Override
    protected int getDataWireLength() {
        return 16;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.putInt(IANA_ENTERPRISE_NUMBER.getNumber());
        buffer.putInt(getOemDefined());
        buffer.put(Bits.toBytes(2, getSpecialCommands()));
        buffer.put(Bits.toByte(getSystemCapabilities()));
        buffer.put(Bits.toBytes(4, getSystemFirmwareCapabilities()));
        buffer.put((byte) 0);   // Reserved
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        assertWireInt(buffer, IANA_ENTERPRISE_NUMBER.getNumber(), "IANA enterprise number");
        withOemDefined(buffer.getInt());
        withSpecialCommands(Bits.fromBuffer(SpecialCommand.class, buffer, 2));
        withSystemCapabilities(Bits.fromBuffer(SystemCapability.class, buffer, 1));
        withSystemFirmwareCapabilities(Bits.fromBuffer(SystemFirmwareCapability.class, buffer, 4));
        assertWireByte(buffer, (byte) 0, "reserved byte");
    }
}
