/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 42.2, table 42-3, page 508-521.
 *
 * @author shevek
 */
public interface SensorSpecificOffset extends Code.DescriptiveWrapper {

    public static class Utils {

        @Nonnull
        public static <T extends Enum<T> & Code.DescriptiveWrapper> String toString(T value) {
            return "0x" + UnsignedBytes.toString(value.getCode(), 16) + ": " + value.getDescription();
        }
    }

    /**
     * [IPMI2] Section 42.2, table 42-3, page 508.
     */
    public static enum PhysicalSecurity implements SensorSpecificOffset {

        General_Chassis_Intrusion(0x00, "General Chassis Intrusion"),
        Drive_Bay_Intrusion(0x01, "Drive Bay intrusion"),
        IO_Card_Area_Intrusion(0x02, "I/O Card area intrusion"),
        Processor_Area_Intrusion(0x03, "Processor area intrusion"),
        /** System is unplugged from LAN. The Event Data 2 field can be used to identify which network controller the leash was lost on where 00h corresponds to the first (or only) network controller. */
        LAN_Leash_Lost(0x04, "LAN Leash Lost"),
        Unauthorized_Dock(0x05, "Unauthorized dock"),
        /** (supports detection of hot plug fan tampering) */
        FAN_Area_Intrusion(0x06, "FAN area intrusion");
        private final byte code;
        private final String description;
        /* pp */ private PhysicalSecurity(@Nonnegative int code, @Nonnull String description) {
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
            return Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.2, table 42-3, page 508.
     */
    public static enum PlatformSecurity implements SensorSpecificOffset {

        Secure_Mode_Violation_Attempt(0x00, "Secure Mode (Front Panel Lockout) Violation attempt"),
        Pre_Boot_Password_Violation_UserPassword(0x01, "Pre-boot Password Violation - user password"),
        Pre_Boot_Password_Violation_SetupPassword(0x02, "Pre-boot Password Violation attempt - setup password"),
        Pre_Boot_Password_Violation_NetworkBootPassword(0x03, "Pre-boot Password Violation - network boot password"),
        Pre_Boot_Password_Violation_Other(0x04, "Other pre-boot Password Violation"),
        Out_Of_Band_Password_Violation(0x05, "Out-of-band Access Password Violation");
        private final byte code;
        private final String description;
        /* pp */ private PlatformSecurity(@Nonnegative int code, @Nonnull String description) {
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
            return Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.2, table 42-3, page 508.
     */
    public static enum Processor implements SensorSpecificOffset {

        IERR(0x00, "IERR"),
        Thermal_Trip(0x01, "Thermal Trip"),
        FRB1_BIST_failure(0x02, "FRB1/BIST failure"),
        /** (used hang is believed to be due or related to a processor failure. Use System Firmware Progress sensor for other BIOS hangs.) */
        FRB2_Hang_in_POST_failure(0x03, "FRB2/Hang in POST failure"),
        FRB3_Processor_Startup_failure(0x04, "FRB3/Processor Startup/Initialization failure (CPU didn't start)"),
        Configuration_Error(0x05, "Configuration Error"),
        Uncorrectable_CPU_complex_Error(0x06, "SM BIOS 'Uncorrectable CPU-complex Error'"),
        Processor_Presence_detected(0x07, "Processor Presence detected"),
        Processor_disabled(0x08, "Processor disabled"),
        Terminator_Presence_Detected(0x09, "Terminator Presence Detected"),
        /** (processor throttling triggered by a hardware-based mechanism operating independent from system software, such as automatic thermal throttling or throttling to limit power consumption.) */
        Processor_Automatically_Throttled(0x0A, "Processor Automatically Throttled"),
        Machine_Check_Exception(0x0B, "Machine Check Exception (Uncorrectable)"),
        Correctable_Machine_Check_Error(0x0C, "Correctable Machine Check Error");
        private final byte code;
        private final String description;
        /* pp */ private Processor(@Nonnegative int code, @Nonnull String description) {
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
            return Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.2, table 42-3, page 509.
     */
    public static enum PowerSupply implements SensorSpecificOffset {

        Presence_Detected(0x00, "Presence detected."),
        Power_Supply_Failure_Detected(0x01, "Power Supply Failure detected."),
        Predictive_Failure(0x02, "Predictive Failure."),
        Power_Supply_Input_Lost(0x03, "Power Supply input lost (AC/DC)."),
        Power_Supply_Input_Lost_or_Out_of_Range(0x04, "Power Supply input lost or out-of-range."),
        Power_Supply_Input_Out_of_Range_but_Present(0x05, "Power Supply input out-of-range, but present."),
        Configuration_Error(0x06, "Configuration error.") {
            // ConfigurationErrorValue
        },
        /** Power supply is in a standby state where its main outputs have been automatically deactivated because the load is being supplied by one or more other power supplies. */
        Power_Supply_Inactive(0x07, "Power Supply Inactive (in standby state).");

        /**
         The Event Data 3 field provides a more detailed definition of the error:
         7:4 = Reserved for future definition, set to 0000b
         3:0 = Error Type, one of */
        public static enum ConfigurationErrorData3 implements Code.Wrapper {

            /** Typically, the system OEM defines the vendor compatibility criteria that drives this status). */
            VendorMismatch(0x00, "Vendor mismatch."),
            /** Typically, the system OEM defines the vendor revision compatibility criteria that drives this status). */
            RevisionMismatch(0x01, "Revision mismatch."),
            /** For processor power supplies (typically DC-to-DC converters or VRMs), there's usually a one-to- one relationship between the supply and the CPU. This offset can indicate the situation where the power supply is present but the processor is not. This offset can be used for reporting that as an unexpected or unsupported condition. */
            ProcessorMissing(0x02, "Processor missing."),
            /** The power rating of the supply does not match the system's requirements. */
            PowerSupplyRatingMismatch(0x03, "Power Supply rating mismatch."),
            /** The voltage rating of the supply does not match the system's requirements. */
            VoltageRatingMismatch(0x04, "Voltage rating mismatch.");
            private final byte code;
            private final String description;
            /* pp */ private ConfigurationErrorData3(@Nonnegative int code, @Nonnull String description) {
                this.code = UnsignedBytes.checkedCast(code);
                this.description = description;
            }

            @Override
            public byte getCode() {
                return code;
            }

            @Nonnull
            public String getDescription() {
                return description;
            }

            @Override
            public String toString() {
                return "0x" + UnsignedBytes.toString(getCode(), 16) + ": " + getDescription();
            }
        }
        private final byte code;
        private final String description;
        /* pp */ private PowerSupply(@Nonnegative int code, @Nonnull String description) {
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
            return Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.2, table 42-3, page 509.
     */
    public static enum PowerUnit implements SensorSpecificOffset {

        PowerOff(0x00, "Power Off / Power Down"),
        PowerCycle(0x01, "Power Cycle"),
        _240VAPowerDown(0x02, "240VA Power Down"),
        InterlockPowerDown(0x03, "Interlock Power Down"),
        /** The power source for the power unit was lost. */
        ACLost(0x04, "AC lost / Power input lost"),
        /** Unit did not respond to request to turn on. */
        SoftPowerControlFailure(0x05, "Soft Power Control Failure"),
        PowerUnitFailure(0x06, "Power Unit Failure detected"),
        PredictiveFailure(0x07, "Predictive Failure");
        private final byte code;
        private final String description;
        /* pp */ private PowerUnit(@Nonnegative int code, @Nonnull String description) {
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
            return Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.2, table 42-3, page 510.
     */
    public static enum Memory implements SensorSpecificOffset {

        CorrectableECC(0x00, "Correctable ECC."),
        UncorrectableECC(0x01, "Uncorrectable ECC."),
        Parity(0x02, "Parity."),
        /** Stuck bit. */
        MemoryScrubFailed(0x03, "Memory Scrub Failed."),
        MemoryDeviceDisabled(0x04, "Memory Device Disabled."),
        CorrectableECCLoggingLimitReached(0x05, "Correctable ECC logging limit reached."),
        /** Indicates presence of entity associated with the sensor. Typically the entity will be a 'memory module' or other entity representing a physically replaceable unit of memory. */
        PresenceDetected(0x06, "Presence detected."),
        /** Indicates a memory configuration error for the entity associated with the sensor. This can include when a given implementation of the entity is not supported by the system (e.g., when the particular size of the memory module is unsupported) or that the entity is part of an unsupported memory configuration (e.g.  the configuration is not supported because the memory module doesn't match other memory modules). */
        ConfigurationError(0x07, "Configuration error."),
        /** Indicates entity associated with the sensor represents a 'spare' unit of memory.  The Event Data 3 field can be used to provide an event extension code, with the following definition: [7:0] - Memory module/device (e.g. DIMM/SIMM/RIMM) identification, relative to the entity that the sensor is associated with (if SDR provided for this sensor). */
        Spare(0x08, "Spare."),
        /** Memory throttling triggered by a hardware-based mechanism operating independent from system software, such as automatic thermal throttling or throttling to limit power consumption. */
        MemoryAutomaticallyThrottled(0x09, "Memory Automatically Throttled."),
        /** Memory device has entered a critical overtemperature state, exceeding specified operating conditions. Memory devices in this state may produce errors or become inaccessible. */
        CriticalOvertemperature(0x0A, "Critical Overtemperature.");
        private final byte code;
        private final String description;
        /* pp */ private Memory(@Nonnegative int code, @Nonnull String description) {
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
            return Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.2, table 42-3, page 510.
     */
    public static enum DriveSlot implements SensorSpecificOffset {

        DrivePresence(0x00, "Drive Presence"),
        DriveFault(0x01, "Drive Fault"),
        PredictiveFailure(0x02, "Predictive Failure"),
        HotSpare(0x03, "Hot Spare"),
        ConsistencyCheckInProgress(0x04, "Consistency Check / Parity Check in progress"),
        InCriticalArray(0x05, "In Critical Array"),
        InFailedArray(0x06, "In Failed Array"),
        RebuildInProgress(0x07, "Rebuild/Remap in progress"),
        RebuildAborted(0x08, "Rebuild/Remap Aborted (was not completed normally)");
        private final byte code;
        private final String description;
        /* pp */ private DriveSlot(@Nonnegative int code, @Nonnull String description) {
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
            return Utils.toString(this);
        }
    }
}
