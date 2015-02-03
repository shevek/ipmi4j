/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.chassis;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiCommandName;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiRequest;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;

/**
 * [IPMI2] Section 28.3, table 28-4, page 390.
 *
 * @author shevek
 */
public class ChassisControlRequest extends AbstractIpmiRequest {

    /**
     * [IPMI2] Section 28.3, table 28-4, page 390.
     */
    public static enum ChassisCommand implements Code.DescriptiveWrapper {

        /** Force system into soft off (S4/S45) state.  This is for 'emergency'
         * management power down actions. The command does not initiate a clean
         * shut-down of the operating system prior to powering down the system. */
        PowerDown(0x00, "Power Down"),
        PowerUp(0x01, "Power Up"),
        /**
         * This command provides a power off interval of at least 1 second
         * following the deassertion of the system's
         * POWERGOOD status from the main power subsystem. It is
         * recommended that no action occur if system power is off (S4/S5)
         * when this action is selected, and that a D5h 'Request parameter(s)
         * not supported in present state.' error completion code be returned.
         * Note that some implementations may cause a system power up if a
         * power cycle operation is selected when system power is down. For
         * consistency of operation, it is recommended that system
         * management software first check the system power state before
         * issuing a power cycle, and only issue the command if system
         * power is ON or in a lower sleep state than S4/S5. */
        PowerCycle(0x02, "Power Cycle"),
        /** In some implementations, the BMC may not know
         * whether a reset will cause any particular effect and will pulse the
         * system reset signal regardless of power state. If the implementation
         * can tell that no action will occur if a reset is delivered in a given
         * power state, then it is recommended (but still optional) that a D5h
         * 'Request parameter(s) not supported in present state.' error
         * completion code be returned. */
        HardReset(0x03, "Hard Reset"),
        /** Pulse a version of a diagnostic
         * interrupt that goes directly to the processor(s). This is typically used
         * to cause the operating system to do a diagnostic dump (OS
         * dependent). The interrupt is commonly an NMI on IA-32 systems
         * and an INIT on Intel(r) Itanium(tm) processor based systems. */
        PulseDiagnosticInterrupt(0x04, "Pulse Diagnostic Interrupt"),
        ACPIOverTemperatureShutdown(0x05, "Initiate a soft-shutdown of OS via ACPI by emulating a fatal overtemperature");
        private final byte code;
        private final String description;
        /* pp */ private ChassisCommand(@Nonnegative int code, @Nonnull String description) {
            this.code = UnsignedBytes.checkedCast(code);
            this.description = description;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String toString() {
            return Code.Utils.toString(this);
        }
    }
    public ChassisCommand chassisCommand;

    @Override
    public IpmiCommandName getCommandName() {
        return IpmiCommandName.ChassisControl;
    }

    @Override
    public void apply(IpmiClientIpmiCommandHandler handler, IpmiSession session) {
        handler.handleChassisControlRequest(session, this);
    }

    @Override
    protected int getDataWireLength() {
        return 1;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(chassisCommand.getCode());
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        chassisCommand = Code.fromBuffer(ChassisCommand.class, buffer);
    }

    @Override
    public void toStringBuilder(StringBuilder buf, int depth) {
        super.toStringBuilder(buf, depth);
        appendValue(buf, depth, "ChassisCommand", chassisCommand);
    }
}
