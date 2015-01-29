/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sensor;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 42.2, table 42-3, page 508-521.
 *
 * @author shevek
 */
public enum SensorType implements Code.DescriptiveWrapper {

    Reserved(0x00, "Reserved"),
    Temperature(0x01, "Temperature"),
    Voltage(0x02, "Voltage"),
    Current(0x03, "Current"),
    Fan(0x04, "Fan"),
    Physical_Security(0x05, "Physical Security (Chassis Intrusion)", SensorSpecificOffset.PhysicalSecurity.class),
    Platform_Security(0x06, "Platform Security Violation Attempt", SensorSpecificOffset.PlatformSecurity.class),
    Processor(0x07, "Processor", SensorSpecificOffset.Processor.class),
    /** (also used for power converters [e.g. DC-to-DC converters] and VRMs [voltage regulator modules]). */
    Power_Supply(0x08, "Power Supply", SensorSpecificOffset.PowerSupply.class),
    Power_Unit(0x09, "Power Unit", SensorSpecificOffset.PowerUnit.class),
    Cooling_Device(0x0A, "Cooling Device"),
    /** (per units given in SDR) */
    Other(0x0B, "Other Units-based Sensor"),
    Memory(0x0C, "Memory", SensorSpecificOffset.Memory.class),
    Drive_Slot(0x0D, "Drive Slot (Bay)", SensorSpecificOffset.DriveSlot.class),
    POST_Memory_Resize(0x0E, "POST Memory Resize"),
    /** (formerly POST Error) */
    System_Firmware_Progress(0x0F, "System Firmware Progress", SensorSpecificOffset.SystemFirmwareProgress.class),
    Event_Logging_Disabled(0x10, "Event Logging Disabled", SensorSpecificOffset.EventLoggingDisabled.class),
    Watchdog_1(0x11, "Watchdog 1", SensorSpecificOffset.Watchdog1.class),
    System_Event(0x12, "System Event", SensorSpecificOffset.SystemEvent.class),
    Critical_Interrupt(0x13, "Critical Interrupt", SensorSpecificOffset.CriticalInterrupt.class),
    Button_or_Switch(0x14, "Button / Switch", SensorSpecificOffset.Button.class),
    Module_or_Board(0x15, "Module / Board"),
    Microcontroller_or_Coprocessor(0x16, "Microcontroller / Coprocessor"),
    Add_in_Card(0x17, "Add-in Card"),
    Chassis(0x18, "Chassis"),
    Chip_Set(0x19, "Chip Set", SensorSpecificOffset.Chipset.class),
    Other_FRU(0x1A, "Other FRU"),
    Cable_or_Interconnect(0x1B, "Cable / Interconnect", SensorSpecificOffset.Cable.class),
    Terminator(0x1C, "Terminator"),
    System_Boot_or_Restart_Initiated(0x1D, "System Boot / Restart Initiated", SensorSpecificOffset.SystemBootInitiation.class),
    Boot_Error(0x1E, "Boot Error", SensorSpecificOffset.BootError.class),
    Base_OS_Boot_or_Installation_Status(0x1F, "Base OS Boot / Installation Status", SensorSpecificOffset.BootStatus.class),
    OS_Stop_or_Shutdown(0x20, "OS Stop / Shutdown"),
    Slot_or_Connector(0x21, "Slot / Connector"),
    System_ACPI_Power_State(0x22, "System ACPI Power State"),
    Watchdog_2(0x23, "Watchdog 2"),
    Platform_Alert(0x24, "Platform Alert"),
    Entity_Presence(0x25, "Entity Presence"),
    Monitor_ASIC_or_IC(0x26, "Monitor ASIC / IC"),
    LAN(0x27, "LAN"),
    Management_Subsystem_Health(0x28, "Management Subsystem Health"),
    Battery(0x29, "Battery"),
    Session_Audit(0x2A, "Session Audit"),
    Version_Change(0x2B, "Version Change"),
    FRU_State(0x2C, "FRU State"),
    OEM_Reserved_C0(0xC0, "OEM Reserved ID 0xC0"),
    OEM_Reserved_C1(0xC1, "OEM Reserved ID 0xC1"),
    OEM_Reserved_C2(0xC2, "OEM Reserved ID 0xC2"),
    OEM_Reserved_C3(0xC3, "OEM Reserved ID 0xC3"),
    OEM_Reserved_C4(0xC4, "OEM Reserved ID 0xC4"),
    OEM_Reserved_C5(0xC5, "OEM Reserved ID 0xC5"),
    OEM_Reserved_C6(0xC6, "OEM Reserved ID 0xC6"),
    OEM_Reserved_C7(0xC7, "OEM Reserved ID 0xC7"),
    OEM_Reserved_C8(0xC8, "OEM Reserved ID 0xC8"),
    OEM_Reserved_C9(0xC9, "OEM Reserved ID 0xC9"),
    OEM_Reserved_CA(0xCA, "OEM Reserved ID 0xCA"),
    OEM_Reserved_CB(0xCB, "OEM Reserved ID 0xCB"),
    OEM_Reserved_CC(0xCC, "OEM Reserved ID 0xCC"),
    OEM_Reserved_CD(0xCD, "OEM Reserved ID 0xCD"),
    OEM_Reserved_CE(0xCE, "OEM Reserved ID 0xCE"),
    OEM_Reserved_CF(0xCF, "OEM Reserved ID 0xCF"),
    OEM_Reserved_D0(0xD0, "OEM Reserved ID 0xD0"),
    OEM_Reserved_D1(0xD1, "OEM Reserved ID 0xD1"),
    OEM_Reserved_D2(0xD2, "OEM Reserved ID 0xD2"),
    OEM_Reserved_D3(0xD3, "OEM Reserved ID 0xD3"),
    OEM_Reserved_D4(0xD4, "OEM Reserved ID 0xD4"),
    OEM_Reserved_D5(0xD5, "OEM Reserved ID 0xD5"),
    OEM_Reserved_D6(0xD6, "OEM Reserved ID 0xD6"),
    OEM_Reserved_D7(0xD7, "OEM Reserved ID 0xD7"),
    OEM_Reserved_D8(0xD8, "OEM Reserved ID 0xD8"),
    OEM_Reserved_D9(0xD9, "OEM Reserved ID 0xD9"),
    OEM_Reserved_DA(0xDA, "OEM Reserved ID 0xDA"),
    OEM_Reserved_DB(0xDB, "OEM Reserved ID 0xDB"),
    OEM_Reserved_DC(0xDC, "OEM Reserved ID 0xDC"),
    OEM_Reserved_DD(0xDD, "OEM Reserved ID 0xDD"),
    OEM_Reserved_DE(0xDE, "OEM Reserved ID 0xDE"),
    OEM_Reserved_DF(0xDF, "OEM Reserved ID 0xDF"),
    OEM_Reserved_E0(0xE0, "OEM Reserved ID 0xE0"),
    OEM_Reserved_E1(0xE1, "OEM Reserved ID 0xE1"),
    OEM_Reserved_E2(0xE2, "OEM Reserved ID 0xE2"),
    OEM_Reserved_E3(0xE3, "OEM Reserved ID 0xE3"),
    OEM_Reserved_E4(0xE4, "OEM Reserved ID 0xE4"),
    OEM_Reserved_E5(0xE5, "OEM Reserved ID 0xE5"),
    OEM_Reserved_E6(0xE6, "OEM Reserved ID 0xE6"),
    OEM_Reserved_E7(0xE7, "OEM Reserved ID 0xE7"),
    OEM_Reserved_E8(0xE8, "OEM Reserved ID 0xE8"),
    OEM_Reserved_E9(0xE9, "OEM Reserved ID 0xE9"),
    OEM_Reserved_EA(0xEA, "OEM Reserved ID 0xEA"),
    OEM_Reserved_EB(0xEB, "OEM Reserved ID 0xEB"),
    OEM_Reserved_EC(0xEC, "OEM Reserved ID 0xEC"),
    OEM_Reserved_ED(0xED, "OEM Reserved ID 0xED"),
    OEM_Reserved_EE(0xEE, "OEM Reserved ID 0xEE"),
    OEM_Reserved_EF(0xEF, "OEM Reserved ID 0xEF"),
    OEM_Reserved_F0(0xF0, "OEM Reserved ID 0xF0"),
    OEM_Reserved_F1(0xF1, "OEM Reserved ID 0xF1"),
    OEM_Reserved_F2(0xF2, "OEM Reserved ID 0xF2"),
    OEM_Reserved_F3(0xF3, "OEM Reserved ID 0xF3"),
    OEM_Reserved_F4(0xF4, "OEM Reserved ID 0xF4"),
    OEM_Reserved_F5(0xF5, "OEM Reserved ID 0xF5"),
    OEM_Reserved_F6(0xF6, "OEM Reserved ID 0xF6"),
    OEM_Reserved_F7(0xF7, "OEM Reserved ID 0xF7"),
    OEM_Reserved_F8(0xF8, "OEM Reserved ID 0xF8"),
    OEM_Reserved_F9(0xF9, "OEM Reserved ID 0xF9"),
    OEM_Reserved_FA(0xFA, "OEM Reserved ID 0xFA"),
    OEM_Reserved_FB(0xFB, "OEM Reserved ID 0xFB"),
    OEM_Reserved_FC(0xFC, "OEM Reserved ID 0xFC"),
    OEM_Reserved_FD(0xFD, "OEM Reserved ID 0xFD"),
    OEM_Reserved_FE(0xFE, "OEM Reserved ID 0xFE"),
    OEM_Reserved_FF(0xFF, "OEM Reserved ID 0xFF");
    private final byte code;
    private final String description;
    private final Class<? extends SensorSpecificOffset> sensorSpecificOffsetType;
    /* pp */ private SensorType(@Nonnegative int code, @Nonnull String description, @CheckForNull Class<? extends SensorSpecificOffset> sensorSpecificOffsetType) {
        this.code = UnsignedBytes.checkedCast(code);
        this.description = description;
        this.sensorSpecificOffsetType = sensorSpecificOffsetType;
    }
    /* pp */ private SensorType(@Nonnegative int code, @Nonnull String description) {
        this(code, description, null);
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
        return "0x" + UnsignedBytes.toString(getCode(), 16) + ": " + getDescription();
    }
}
