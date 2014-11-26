/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import com.google.common.collect.Iterables;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Bits;

/**
 * PowerUp, Reset and PowerCycleReset.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.1 pages 33-35.
 * 
 * @author shevek
 */
public abstract class AbstractAsfBootData extends AbstractAsfData {

    /** Section 3.2.4.1 page 33. */
    public static class SpecialCommand {

        public static final byte NOP = 0x00;
        /** Parameter: 0. */
        public static final byte FORCE_BOOT_PXE = 0x01;
        /** Parameter: 0 = boot default; 1..n = boot drive n. */
        public static final byte FORCE_BOOT_DRIVE = 0x02;
        /** Parameter: 0 = boot default; 1..n = boot drive n. */
        public static final byte FORCE_BOOT_DRIVE_SAFE = 0x03;
        /** Parameter: 0. */
        public static final byte FORCE_BOOT_DIAGNOSTIC = 0x04;
        /** Parameter: 0 = boot default; 1..n = boot drive n. */
        public static final byte FORCE_BOOT_OPTICAL = 0x05;
    }

    /** Section 3.2.4.1 page 34. */
    public static enum BootOption implements Bits.Wrapper {

        LOCK_POWER_BUTTON(0, 1),
        LOCK_RESET_BUTTON(0, 2),
        LOCK_KEYBOARD(0, 5),
        LOCK_SLEEP_BUTTON(0, 6),
        USER_PASSWORD_BYPASS(1, 3),
        FORCE_PROGRESS_EVENTS(1, 4),
        FIRMWARE_VERBOSITY_DEFAULT(1, 6, 2, 0b00),
        FIRMWARE_VERBOSITY_QUIET(1, 6, 2, 0b01),
        FIRMWARE_VERBOSITY_VERBOSE(1, 6, 2, 0b10),
        FIRMWARE_VERBOSITY_SCREEN_BLANK(1, 6, 2, 0b11),
        CONFIGURATION_DATA_RESET(1, 7);
        private final Bits bits;

        private BootOption(@Nonnegative int byteIndex, @Nonnegative int bitIndex) {
            this.bits = Bits.forBitIndex(byteIndex, bitIndex);
        }

        private BootOption(@Nonnegative int byteIndex, @Nonnegative int firstBitIndex, @Nonnegative int length, int value) {
            this.bits = Bits.forBinaryBE(byteIndex, firstBitIndex, length, value);
        }

        @Override
        public Bits getBits() {
            return bits;
        }
    }
    private byte command = SpecialCommand.NOP;
    private char commandParameter = 0;
    private final Set<BootOption> options = EnumSet.noneOf(BootOption.class);

    public byte getSpecialCommand() {
        return command;
    }

    @Nonnull
    public AbstractAsfBootData withSpecialCommand(byte command) {
        this.command = command;
        return this;
    }

    public char getSpecialCommandParameter() {
        return commandParameter;
    }

    @Nonnull
    public AbstractAsfBootData withSpecialCommandParameter(char commandParameter) {
        this.commandParameter = commandParameter;
        return this;
    }

    @Nonnull
    public AbstractAsfBootData withBootOptions(Iterable<? extends BootOption> options) {
        Iterables.addAll(this.options, options);
        return this;
    }

    @Nonnull
    public AbstractAsfBootData withBootOptions(BootOption... options) {
        return withBootOptions(Arrays.asList(options));
    }

    @Nonnull
    public Set<? extends BootOption> getBootOptions() {
        return options;
    }

    @Override
    public int getDataWireLength() {
        return 11;
    }

    @Override
    public void toWireData(ByteBuffer buffer) {
        buffer.putInt(IANA_ENTERPRISE_NUMBER.getNumber());
        buffer.put(getSpecialCommand());
        buffer.putChar(getSpecialCommandParameter());
        buffer.put(Bits.toBytes(2, getBootOptions()));
        buffer.put(new byte[2]);    // TODO: OEM parameters.
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        assertWireInt(buffer, IANA_ENTERPRISE_NUMBER.getNumber());
        withSpecialCommand(buffer.get());
        withSpecialCommandParameter(buffer.getChar());
        withBootOptions(Bits.fromBuffer(BootOption.class, buffer, 2));
        assertWireByte(buffer, (byte) 0);
        assertWireByte(buffer, (byte) 0);
    }
}