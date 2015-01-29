/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Bits;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelNumber;
import org.anarres.ipmi.protocol.packet.ipmi.command.AbstractIpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.command.chassis.GetSystemRestartCauseResponse;

/**
 * [IPMI2] Section 42.2, table 42-3, pages 508-521.
 *
 * @author shevek
 */
public interface SensorSpecificOffset extends Code.DescriptiveWrapper {

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
            return Code.Utils.toString(this);
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
            return Code.Utils.toString(this);
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
            return Code.Utils.toString(this);
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
            public Object decodeEventData3(byte value) {
                return Code.fromByte(ConfigurationErrorData3.class, value);
            }
        },
        /** Power supply is in a standby state where its main outputs have been automatically deactivated because the load is being supplied by one or more other power supplies. */
        Power_Supply_Inactive(0x07, "Power Supply Inactive (in standby state).");

        /**
         The Event Data 3 field provides a more detailed definition of the error:
         7:4 = Reserved for future definition, set to 0000b
         3:0 = Error Type, one of */
        public static enum ConfigurationErrorData3 implements Code.DescriptiveWrapper {

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

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public String toString() {
                return Code.Utils.toString(this);
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
            return Code.Utils.toString(this);
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
            return Code.Utils.toString(this);
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
        Spare(0x08, "Spare.") {
            public Object decodeEventData2(byte value) {
                return UnsignedBytes.toInt(value);
            }
        },
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
            return Code.Utils.toString(this);
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
            return Code.Utils.toString(this);
        }
    }

    /**
     * [IPMI2] Section 42.2, table 42-3, page 511.
     */
    public static enum SystemFirmwareProgress implements SensorSpecificOffset {

        SystemFirmwareError(0x00, "System Firmware Error (POST Error).") {
            public Object decodeEventData2(byte value) {
                return Code.fromByte(SystemFirmwareErrorData2.class, value);
            }
        },
        SystemFirmwareHang(0x01, "System Firmware Hang.") {
            public Object decodeEventData2(byte value) {
                return Code.fromByte(SystemFirmwareProgressData2.class, value);
            }
        },
        SystemFirmwareProgress(0x02, "System Firmware Progress") {
            public Object decodeEventData2(byte value) {
                return Code.fromByte(SystemFirmwareProgressData2.class, value);
            }
        };

        public static enum SystemFirmwareErrorData2 implements Code.DescriptiveWrapper {

            Unspecified(0x00, "Unspecified."),
            NoSystemMemoryInstalled(0x01, "No system memory is physically installed in the system."),
            NoUsableSystemMemory(0x02, "No usable system memory, all installed memory has experienced an unrecoverable failure."),
            UnrecoverableHardDiskDeviceFailure(0x03, "Unrecoverable hard-disk/ATAPI/IDE device failure."),
            UnrecoverableSystemBoardFailure(0x04, "Unrecoverable system-board failure."),
            UnrecoverableDisketteSubsystemFailure(0x05, "Unrecoverable diskette subsystem failure."),
            UnrecoverableHardDiskControllerFailure(0x06, "Unrecoverable hard-disk controller failure."),
            UnrecoverableKeyboardFailure(0x07, "Unrecoverable PS/2 or USB keyboard failure."),
            RemovableBootMediaNotFound(0x08, "Removable boot media not found"),
            UnrecoverableVideoControllerFailure(0x09, "Unrecoverable video controller failure"),
            NoVideoDeviceDetected(0x0A, "No video device detected"),
            FirmwareROMCorruptionDetected(0x0B, "Firmware (BIOS) ROM corruption detected"),
            CPUVoltageMismatch(0x0C, "CPU voltage mismatch (processors that share same supply have mismatched voltage requirements)"),
            CPUSpeedMatchingFailure(0x0D, "CPU speed matching failure");
            private final byte code;
            private final String description;
            /* pp */ private SystemFirmwareErrorData2(@Nonnegative int code, @Nonnull String description) {
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

        public static enum SystemFirmwareProgressData2 implements Code.DescriptiveWrapper {

            Unspecified(0x00, "Unspecified."),
            MemoryInitialization(0x01, "Memory initialization."),
            HardDiskInitialization(0x02, "Hard-disk initialization"),
            SecondaryProcessorInitialization(0x03, "Secondary processor(s) initialization"),
            UserAuthentication(0x04, "User authentication"),
            UserInitiatedSystemSetup(0x05, "User-initiated system setup"),
            USBResourceConfiguration(0x06, "USB resource configuration"),
            PCIResourceConfiguration(0x07, "PCI resource configuration"),
            OptionROMInitialization(0x08, "Option ROM initialization"),
            VideoInitialization(0x09, "Video initialization"),
            CacheInitialization(0x0A, "Cache initialization"),
            SMBusInitialization(0x0B, "SM Bus initialization"),
            KeyboardControllerInitialization(0x0C, "Keyboard controller initialization"),
            ManagementControllerInitialization(0x0D, "Embedded controller/management controller initialization"),
            DockingStationAttachment(0x0E, "Docking station attachment"),
            DockingStationEnablement(0x0F, "Enabling docking station"),
            DockingStationEjection(0x10, "Docking station ejection"),
            DockingStationDisablement(0x11, "Disabling docking station"),
            OperatingSystemWakeUpVectorCall(0x12, "Calling operating system wake-up vector"),
            OperatingSystemBootProcessStart(0x13, "Starting operating system boot process, e.g. calling Int 19h"),
            BaseboardInitialization(0x14, "Baseboard or motherboard initialization"),
            Reserved_15(0x15, "reserved"),
            FloppyInitialization(0x16, "Floppy initialization"),
            KeyboardTest(0x17, "Keyboard test"),
            PointingDeviceTest(0x18, "Pointing device test"),
            PrimaryProcessorInitialization(0x19, "Primary processor initialization");
            private final byte code;
            private final String description;
            /* pp */ private SystemFirmwareProgressData2(@Nonnegative int code, @Nonnull String description) {
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
        private final byte code;
        private final String description;
        /* pp */ private SystemFirmwareProgress(@Nonnegative int code, @Nonnull String description) {
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

    /**
     * [IPMI2] Section 42.2, table 42-3, page 512.
     */
    public static enum EventLoggingDisabled implements SensorSpecificOffset {

        CorrectableMemoryErrorLoggingDisabled(0x00, "Correctable Memory Error Logging Disabled") {
            /** [7:0] - Memory module/device (e.g. DIMM/SIMM/RIMM) identification, relative to the entity that the sensor is associated with (if SDR provided for this sensor). */
            public Object decodeEventData2(byte value) {
                return UnsignedBytes.toInt(value);
            }
        },
        EventTypeLoggingDisabled(0x01, "Event 'Type' Logging Disabled") {
            public Object decodeEventData2(byte value) {
                return Code.fromByte(GenericEventType.class, value);
            }

            public Object decodeEventData3(byte value) {
                boolean allEventsOfType = AbstractIpmiCommand.getBit(value, 5);
                boolean assertion = AbstractIpmiCommand.getBit(value, 4);
                int eventOffset = value & 0xF;
                return null;
            }
        },
        LogAreaReset(0x02, "Log Area Reset/Cleared"),
        AllEventLoggingDisabled(0x03, "All Event Logging Disabled"),
        SELFull(0x04, "SEL Full"),
        SELAlmostFull(0x05, "SEL Almost Full") {
            /** Contains hex value from 0 to 100 decimal (00h to 64h) representing the % of which the SEL is filled at the time the event was generated: 00h is 0% full (SEL is empty), 64h is 100% full, etc. */
            public Object decodeEventData3(byte value) {
                return UnsignedBytes.checkedCast(value);
            }
        },
        CorrectableMachineCheckErrorLoggingDisabled(0x06, "Correctable Machine Check Error Logging Disabled") {
            /** Instance ID number of the (processor) Entity that the sensor is associated with (if SDR provided for this sensor), or a vendor selected logical processor number if no SDR. */
            public Object decodeEventData2(byte value) {
                return UnsignedBytes.toInt(value);
            }

            /** If Event Data 2 is provided then Event Data 3 may be optionally used to indicate whether Event Data 2 is being used to hold an Entity Instance number or a vendor-specific processor number. If Event Data 2 is provided by Event Data 3 is not, then Event Data 2 is assumed to hold an Entity Instance number. */
            public Object decodeEventData3(byte value) {
                return AbstractIpmiCommand.getBit(value, 7);
            }
        };
        private final byte code;
        private final String description;
        /* pp */ private EventLoggingDisabled(@Nonnegative int code, @Nonnull String description) {
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

    /**
     * [IPMI2] Section 42.2, table 42-3, page 512.
     */
    public static enum Watchdog1 implements SensorSpecificOffset {

        BIOSWatchdogReset(0x00, "BIOS Watchdog Reset"),
        OSWatchdogReset(0x01, "OS Watchdog Reset"),
        OSWatchdogShutDown(0x02, "OS Watchdog Shut Down"),
        OSWatchdogPowerDown(0x03, "OS Watchdog Power Down"),
        OSWatchdogPowerCycle(0x04, "OS Watchdog Power Cycle"),
        OSWatchdogNMI(0x05, "OS Watchdog NMI / Diagnostic Interrupt"),
        OSWatchdogExpired(0x06, "OS Watchdog Expired, status only"),
        OSWatchdogPreTimeoutInterrupt(0x07, "OS Watchdog pre-timeout Interrupt, non-NMI");
        private final byte code;
        private final String description;
        /* pp */ private Watchdog1(@Nonnegative int code, @Nonnull String description) {
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

    /**
     * [IPMI2] Section 42.2, table 42-3, page 513.
     */
    public static enum SystemEvent implements SensorSpecificOffset {

        SystemReconfigured(0x00, "System Reconfigured"),
        OEMSystemBootEvent(0x01, "OEM System Boot Event"),
        /** This event would typically require system-specific diagnostics to determine FRU / failure type. */
        UndeterminedSystemHardwareFailure(0x02, "Undetermined system hardware failure"),
        /** See GetAuxiliaryLogStatusCommandRequest etc. */
        EntryAddedToAuxiliaryLog(0x03, "Entry added to Auxiliary Log") {
            // Event Data 2: [7:4] LogEntryAction, [3:0] LogType
            public Object decodeEventData2(byte value) {
                Code.fromInt(LogEntryAction.class, (value >> 4) & 0xF);
                Code.fromInt(LogType.class, value & 0xF);
                return null;
            }
        },
        PEFAction(0x04, "PEF Action") {
            // Event Data 2: Action
            public Object decodeEventData2(byte value) {
                return Bits.fromByte(Action.class, value);
            }
        },
        TimestampClockSynchronization(0x05, "Timestamp Clock Synchronization") {
            /*
             This event can be used to record when changes are made to the
             timestamp clock(s) so that relative time differences between SEL
             entries can be determined.
             */
            public Object decodeEventData2(byte value) {
                boolean first = AbstractIpmiCommand.getBit(value, 7);
                return Code.fromInt(TimestampClockType.class, value & 0xF);
            }
        };

        public static enum LogEntryAction implements Code.DescriptiveWrapper {
            // [7:4] - Log Type

            EntryAdded(0x00, "entry added"),
            EntryAddedNonIPMI(0x01, "entry added because event did not be map to standard IPMI event"),
            EntryAddedWithSEL(0x02, "entry added along with one or more corresponding SEL entries"),
            LogCleared(0x03, "log cleared"),
            LogDisabled(0x04, "log disabled"),
            LogEnabled(0x05, "log enabled");
            private final byte code;
            private final String description;
            /* pp */ private LogEntryAction(@Nonnegative int code, @Nonnull String description) {
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

        public static enum LogType implements Code.DescriptiveWrapper {
            // [3:0] - Log Type

            MCA(0x00, "MCA Log"),
            OEM1(0x01, "OEM 1"),
            OEM2(0x02, "OEM 2");
            private final byte code;
            private final String description;
            /* pp */ private LogType(@Nonnegative int code, @Nonnull String description) {
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

        public static enum Action implements Bits.DescriptiveWrapper {

            DiagnosticInterrupt(5, "Diagnostic Interrupt (NMI)"),
            OEMAction(4, "OEM Action"),
            PowerCycle(3, "Power Cycle"),
            Reset(2, "Reset"),
            PowerOff(1, "Power Off"),
            Alert(0, "Alert");
            private final Bits bits;
            private final String description;
            /* pp */ private Action(@Nonnegative int bit, @Nonnull String description) {
                this.bits = Bits.forBitIndex(0, bit);
                this.description = description;
            }

            @Override
            public Bits getBits() {
                return bits;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public String toString() {
                return Bits.Utils.toString(this);
            }
        }

        public static enum TimestampClockType implements Code.DescriptiveWrapper {
            // [3:0] - Timestamp Clock Type

            /** Also used when both SEL and SDR Timestamp clocks are linked together. */
            SEL(0x00, "SEL Timestamp Clock updated."),
            SDR(0x01, "SDR Timestamp Clock updated.");
            private final byte code;
            private final String description;
            /* pp */ private TimestampClockType(@Nonnegative int code, @Nonnull String description) {
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
        private final byte code;
        private final String description;
        /* pp */ private SystemEvent(@Nonnegative int code, @Nonnull String description) {
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

    /**
     * [IPMI2] Section 42.2, table 42-3, page 514.
     */
    public static enum CriticalInterrupt implements SensorSpecificOffset {

        FrontPanelNMI(0x00, "Front Panel NMI / Diagnostic Interrupt"),
        BusTimeout(0x01, "Bus Timeout"),
        IOChannelCheckNMI(0x02, "I/O channel check NMI"),
        SoftwareNMI(0x03, "Software NMI"),
        PCI_PERR(0x04, "PCI PERR"),
        PCI_SERR(0x05, "PCI SERR"),
        EISAFailSafeTimeout(0x06, "EISA Fail Safe Timeout"),
        BusCorrectableError(0x07, "Bus Correctable Error"),
        BusUncorrectableError(0x08, "Bus Uncorrectable Error"),
        FatalNMI(0x09, "Fatal NMI (port 61h, bit 7)"),
        BusFatalError(0x0A, "Bus Fatal Error"),
        BusDegraded(0x0B, "Bus Degraded (bus operating in a degraded performance state)");
        private final byte code;
        private final String description;
        /* pp */ private CriticalInterrupt(@Nonnegative int code, @Nonnull String description) {
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

    /**
     * [IPMI2] Section 42.2, table 42-3, page 514.
     */
    public static enum Button implements SensorSpecificOffset {

        PowerButtonPressed(0x00, "Power Button pressed"),
        SleepButtonPressed(0x01, "Sleep Button pressed"),
        ResetButtonPressed(0x02, "Reset Button pressed"),
        /** Switch indicating FRU latch is in 'unlatched' position and FRU is mechanically removable. */
        FRULatchOpen(0x03, "FRU latch open"),
        /** e.g. removal/replacement, requested. */
        FRUServiceRequestButton(0x04, "FRU service request button pressed");
        private final byte code;
        private final String description;
        /* pp */ private Button(@Nonnegative int code, @Nonnull String description) {
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

    /**
     * [IPMI2] Section 42.2, table 42-3, page 514.
     */
    public static enum Chipset implements SensorSpecificOffset {

        SoftPowerControlFailure(0x00, "Soft Power Control Failure") {
            /** intended/requested */
            public Object decodeEventData2(byte value) {
                return Code.fromByte(PowerState.class, value);
            }

            /** at time of failure */
            public Object decodeEventData3(byte value) {
                return Code.fromByte(PowerState.class, value);
            }
        },
        ThermalTrip(0x01, "Thermal Trip");

        public static enum PowerState implements Code.DescriptiveWrapper {

            S0(0x00, "S0 / G0 'working'"),
            S1(0x01, "S1 'sleeping with system h/w & processor context maintained'"),
            S2(0x02, "S2 'sleeping, processor context lost'"),
            S3(0x03, "S3 'sleeping, processor & h/w context lost, memory retained.'"),
            S4(0x04, "S4 'non-volatile sleep / suspend-to disk'"),
            S5(0x05, "S5 / G2 'soft-off'"),
            S4S5(0x06, "S4 / S5 soft-off, particular S4 / S5 state cannot be determined"),
            G3(0x07, "G3 / Mechanical Off"),
            S1S2S3(0x08, "Sleeping in an S1, S2, or S3 states (used when particular S1, S2, S3 state cannot be determined)"),
            G1(0x09, "G1 sleeping (S1-S4 state cannot be determined)"),
            S5Override(0x0A, "S5 entered by override"),
            LegacyOn(0x0B, "Legacy ON state"),
            LegacyOff(0x0C, "Legacy OFF state"),
            Unknown(0x0D, "Unknown/Reserved");
            private final byte code;
            private final String description;
            /* pp */ private PowerState(@Nonnegative int code, @Nonnull String description) {
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
        private final byte code;
        private final String description;
        /* pp */ private Chipset(@Nonnegative int code, @Nonnull String description) {
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

    /**
     * [IPMI2] Section 42.2, table 42-3, page 515.
     */
    public static enum Cable implements SensorSpecificOffset {

        CableConnected(0x00, "Cable/Interconnect is connected"),
        CableConfigurationError(0x01, "Configuration Error - Incorrect cable connected / Incorrect interconnection");
        private final byte code;
        private final String description;
        /* pp */ private Cable(@Nonnegative int code, @Nonnull String description) {
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

    /**
     * [IPMI2] Section 42.2, table 42-3, page 515.
     */
    public static enum SystemBootInitiation implements SensorSpecificOffset {

        BIOSPowerUp(0x00, "Initiated by power up (this would typically be generated by BIOS/EFI)"),
        BIOSHardReset(0x01, "Initiated by hard reset (this would typically be generated by BIOS/EFI)"),
        BIOSWarmReset(0x02, "Initiated by warm reset (this would typically be generated by BIOS/EFI)"),
        PXEBoot(0x03, "User requested PXE boot"),
        BootToDiagnostic(0x04, "Automatic boot to diagnostic"),
        OSHardReset(0x05, "OS / run-time software initiated hard reset"),
        OSWarnReset(0x06, "OS / run-time software initiated warm reset"),
        SystemRestart(0x07, "System Restart") {
            public Object decodeEventData2(byte value) {
                return Code.fromInt(GetSystemRestartCauseResponse.RestartCause.class, value & 0xF);
            }

            public Object decodeEventData3(byte value) {
                // TODO: Doublecheck this.
                return Code.fromByte(IpmiChannelNumber.class, value);
            }
        };
        private final byte code;
        private final String description;
        /* pp */ private SystemBootInitiation(@Nonnegative int code, @Nonnull String description) {
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

    /**
     * [IPMI2] Section 42.2, table 42-3, page 515.
     */
    public static enum BootError implements SensorSpecificOffset {

        NoBootableMedia(0x00, "No bootable media"),
        NonBootableDisketteLeftInDrive(0x01, "Non-bootable diskette left in drive"),
        PXEServerNotFound(0x02, "PXE Server not found"),
        InvalidBootSector(0x03, "Invalid boot sector"),
        TimeoutWaitingForUserBootSelection(0x04, "Timeout waiting for user selection of boot source");
        private final byte code;
        private final String description;
        /* pp */ private BootError(@Nonnegative int code, @Nonnull String description) {
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

    /**
     * [IPMI2] Section 42.2, table 42-3, page 515.
     */
    public static enum BootStatus implements SensorSpecificOffset {

        BootCompletedA(0x00, "A: boot completed"),
        BootCompletedC(0x01, "C: boot completed"),
        BootCompletedPXE(0x02, "PXE boot completed"),
        BootCompletedDiagnostic(0x03, "Diagnostic boot completed"),
        BootCompletedCDROM(0x04, "CD-ROM boot completed"),
        BootCompletedROM(0x05, "ROM boot completed"),
        BootCompletedUnspecified(0x06, "Boot Completed - boot device not specified"),
        BaseOSInstallationStarted(0x07, "Base OS/Hypervisor Installation started"),
        BaseOSInstallationCompleted(0x08, "Base OS/Hypervisor Installation completed"),
        BaseOSInstallationAborted(0x09, "Base OS/Hypervisor Installation aborted"),
        BaseOSInstallationFailed(0x0A, "Base OS/Hypervisor Installation failed");
        private final byte code;
        private final String description;
        /* pp */ private BootStatus(@Nonnegative int code, @Nonnull String description) {
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
}
