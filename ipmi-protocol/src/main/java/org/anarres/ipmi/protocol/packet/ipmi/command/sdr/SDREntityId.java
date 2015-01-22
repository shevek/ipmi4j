/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public enum SDREntityId implements Code.Wrapper {

    Unspecified(0x00, "Unspecified"),
    Other(0x01, "Other"),
    Unknown(0x02, "Unknown (unspecified)"),
    Processor(0x03, "Processor"),
    Disk(0x04, "Disk or disk bay"),
    Peripheral_Bay(0x05, "Peripheral bay"),
    System_Management_Module(0x06, "System Management Module"),
    /** Main system board, may also be a processor board and/or internal expansion board. */
    System_Board(0x07, "System board"),
    /** (board holding memory devices) */
    Memory_Module(0x08, "Memory module"),
    /** Holds processors, use this designation when processors are not mounted on system board. */
    Processor_Module(0x09, "Processor module"),
    /** Use this value for the main power supply (supplies) for the system. (DMI refers to this as a power unit, but it's used to represent a power supply). */
    Power_Supply(0x0A, "Power supply"),
    Add_In_Card(0x0B, "Add-in card"),
    /** Front control panel. */
    Front_Panel_Board(0x0C, "Front panel board"),
    Back_Panel_Board(0x0D, "Back panel board"),
    Power_System_Board(0x0E, "Power system board"),
    Drive_Backplane(0x0F, "Drive backplane"),
    /** Contains expansion slots. */
    System_Internal_Expansion_Board(0x10, "System internal expansion board"),
    /** Part of board set. */
    Other_System_Board(0x11, "Other system board"),
    /** Holds 1 or more processors - includes boards that hold SECC modules. */
    Processor_Board(0x12, "Processor board"),
    /** This Entity ID is typically used as a pre-defined logical entity for grouping power supplies and/or sensors that are associated in monitoring a particular logical power domain. */
    Power_Unit(0x13, "Power unit / power domain"),
    /* Power module / DC-to-DC converter - Use this value for internal converters. Note: You should use Entity ID 10 (power supply) for the main power supply even if the main supply is a DC-to-DC converter, e.g. gets external power from a -48 DC source. */
    Power_Module(0x14, "Power module / DC-to-DC converter"),
    Power_Management_Board(0x15, "Power management / power distribution board"),
    Chassis_Back_Panel_Board(0x16, "Chassis back panel board"),
    System_Chassis(0x17, "System chassis"),
    Sub_Chassis(0x18, "Sub-chassis"),
    Other_Chassis_Board(0x19, "Other chassis board"),
    Disk_Drive_Bay(0x1A, "Disk drive bay"),
    Peripheral_Bay_Alt(0x1B, "Peripheral bay"),
    Device_Bay(0x1C, "Device bay"),
    Fan(0x1D, "Fan / cooling device"),
    /** This Entity ID can be used as a pre-defined logical entity for grouping fans or other cooling devices and/or sensors that are associated in monitoring a particular logical cooling domain. */
    Cooling_Unit(0x1E, "Cooling unit / cooling domain"),
    Cable(0x1F, "Cable / interconnect"),
    /** This Entity ID should be used for replaceable memory devices, e.g. DIMM/SIMM. It is recommended that Entity IDs not be used for individual non-replaceable memory devices. Rather, monitoring and error reporting should be associated with the FRU [e.g. memory card] holding the memory. */
    Memory_Device(0x20, "Memory device"),
    System_Management_Software(0x21, "System management software"),
    /** E.g. BIOS/EFI. */
    System_Firmware(0x22, "System firmware"),
    Operating_System(0x23, "Operating system"),
    System_Bus(0x24, "System bus"),
    /** This is a logical entity for use with Entity Association records. It is provided to allow an Entity- association record to define a grouping of entities when there is no appropriate pre-defined entity for the container entity. This Entity should not be used as a physical entity. */
    Group(0x25, "Group"),
    Remote_Management_Communication_Device(0x26, "Remote (out of band) management communication device"),
    /** This Entity ID can be used to identify the environment outside the system chassis. For example, a system may have a temperature sensor that monitors the temperature 'outside the box'. Such a temperature sensor can be associated with an External Environment entity. This value will typically be used as a single instance physical entity. However, the Entity Instance value can be used to denote a difference in regions of the external environment. For example, the region around the front of a chassis may be considered to be different from the region around the back, in which case it would be reasonable to have two different instances of the External Environment entity. */
    External_Environment(0x27, "External environment"),
    Battery(0x28, "battery"),
    /** A blade module that contains processor, memory, and I/O connections that enable it to operate as a processing entity. */
    Processing_Blade(0x29, "Processing blade"),
    /** (a blade module that provides the fabric or network connection for one or more processing blades or modules. */
    Connectivity_Switch(0x2A, "Connectivity switch"),
    /** Processor and memory together on a module. */
    Processor_Memory_Module(0x2B, "Processor / memory module"),
    /** A module that contains the main elements of an I/O interface. */
    IO_Module(0x2C, "I/O module"),
    /** A combination processor and I/O module. */
    Processor_IO_Module(0x2D, "Processor / I/O module"),
    /** Represents firmware or software running on a management controller. */
    Management_Controller_Firmware(0x2E, "Management controller firmware"),
    /** This Entity ID enables associating sensors with the IPMI communication channels - for example a Redundancy sensor could be used to report redundancy status for a channel that is composed of multiple physical links. By convention, the Entity Instance corresponds to the channel number. */
    IPMI_Channel(0x2F, "IPMI channel"),
    PCI_Bus(0x30, "PCI bus"),
    PCI_Express_Bus(0x31, "PCI Express bus"),
    SCSI_Bus(0x32, "SCSI bus (parallel)"),
    SATA_SAS_Bus(0x33, "SATA / SAS bus"),
    Processor_Front_Side_Bus(0x34, "Processor / front-side bus"),
    Real_Time_Clock(0x35, "Real Time Clock (RTC)"),
    Reserved_36(0x36, "reserved"),
    /** This Entity ID enables associating sensors such as temperature to the airflow at an air inlet. */
    Air_Inlet(0x37, "Air inlet"),
    Reserved_38(0x38, "reserved"),
    Reserved_39(0x39, "reserved"),
    Reserved_3A(0x3A, "reserved"),
    Reserved_3B(0x3B, "reserved"),
    Reserved_3C(0x3C, "reserved"),
    Reserved_3D(0x3D, "reserved"),
    Reserved_3E(0x3E, "reserved"),
    Reserved_3F(0x3F, "reserved"),
    /** This Entity ID value is equivalent to Entity ID 37h. It is provided for interoperability with the DCMI 1.0 specifications. */
    Air_Inlet_DCMICompat(0x40, "Air inlet (DCMI compatible)"),
    /** This Entity ID value is equivalent to Entity ID 03h (processor). It is provided for interoperability with the DCMI 1.0 specifications. */
    Processor_DCMICompat(0x41, "Processor (DCMI compatible)"),
    /** This Entity ID value is equivalent to Entity ID 07h (system board). It is provided for interoperability with the DCMI 1.0 specifications. */
    System_Board_DCMICompat(0x42, "Baseboard / main system board (DCMI compatible)"),
    Chassis_Specific_90(0x90, "Chassis Specific ID 0x90"),
    Chassis_Specific_91(0x91, "Chassis Specific ID 0x91"),
    Chassis_Specific_92(0x92, "Chassis Specific ID 0x92"),
    Chassis_Specific_93(0x93, "Chassis Specific ID 0x93"),
    Chassis_Specific_94(0x94, "Chassis Specific ID 0x94"),
    Chassis_Specific_95(0x95, "Chassis Specific ID 0x95"),
    Chassis_Specific_96(0x96, "Chassis Specific ID 0x96"),
    Chassis_Specific_97(0x97, "Chassis Specific ID 0x97"),
    Chassis_Specific_98(0x98, "Chassis Specific ID 0x98"),
    Chassis_Specific_99(0x99, "Chassis Specific ID 0x99"),
    Chassis_Specific_9A(0x9A, "Chassis Specific ID 0x9A"),
    Chassis_Specific_9B(0x9B, "Chassis Specific ID 0x9B"),
    Chassis_Specific_9C(0x9C, "Chassis Specific ID 0x9C"),
    Chassis_Specific_9D(0x9D, "Chassis Specific ID 0x9D"),
    Chassis_Specific_9E(0x9E, "Chassis Specific ID 0x9E"),
    Chassis_Specific_9F(0x9F, "Chassis Specific ID 0x9F"),
    Chassis_Specific_A0(0xA0, "Chassis Specific ID 0xA0"),
    Chassis_Specific_A1(0xA1, "Chassis Specific ID 0xA1"),
    Chassis_Specific_A2(0xA2, "Chassis Specific ID 0xA2"),
    Chassis_Specific_A3(0xA3, "Chassis Specific ID 0xA3"),
    Chassis_Specific_A4(0xA4, "Chassis Specific ID 0xA4"),
    Chassis_Specific_A5(0xA5, "Chassis Specific ID 0xA5"),
    Chassis_Specific_A6(0xA6, "Chassis Specific ID 0xA6"),
    Chassis_Specific_A7(0xA7, "Chassis Specific ID 0xA7"),
    Chassis_Specific_A8(0xA8, "Chassis Specific ID 0xA8"),
    Chassis_Specific_A9(0xA9, "Chassis Specific ID 0xA9"),
    Chassis_Specific_AA(0xAA, "Chassis Specific ID 0xAA"),
    Chassis_Specific_AB(0xAB, "Chassis Specific ID 0xAB"),
    Chassis_Specific_AC(0xAC, "Chassis Specific ID 0xAC"),
    Chassis_Specific_AD(0xAD, "Chassis Specific ID 0xAD"),
    Chassis_Specific_AE(0xAE, "Chassis Specific ID 0xAE"),
    Chassis_Specific_AF(0xAF, "Chassis Specific ID 0xAF"),
    Board_Set_Specific_B0(0xB0, "Board Set Specific ID 0xB0"),
    Board_Set_Specific_B1(0xB1, "Board Set Specific ID 0xB1"),
    Board_Set_Specific_B2(0xB2, "Board Set Specific ID 0xB2"),
    Board_Set_Specific_B3(0xB3, "Board Set Specific ID 0xB3"),
    Board_Set_Specific_B4(0xB4, "Board Set Specific ID 0xB4"),
    Board_Set_Specific_B5(0xB5, "Board Set Specific ID 0xB5"),
    Board_Set_Specific_B6(0xB6, "Board Set Specific ID 0xB6"),
    Board_Set_Specific_B7(0xB7, "Board Set Specific ID 0xB7"),
    Board_Set_Specific_B8(0xB8, "Board Set Specific ID 0xB8"),
    Board_Set_Specific_B9(0xB9, "Board Set Specific ID 0xB9"),
    Board_Set_Specific_BA(0xBA, "Board Set Specific ID 0xBA"),
    Board_Set_Specific_BB(0xBB, "Board Set Specific ID 0xBB"),
    Board_Set_Specific_BC(0xBC, "Board Set Specific ID 0xBC"),
    Board_Set_Specific_BD(0xBD, "Board Set Specific ID 0xBD"),
    Board_Set_Specific_BE(0xBE, "Board Set Specific ID 0xBE"),
    Board_Set_Specific_BF(0xBF, "Board Set Specific ID 0xBF"),
    Board_Set_Specific_C0(0xC0, "Board Set Specific ID 0xC0"),
    Board_Set_Specific_C1(0xC1, "Board Set Specific ID 0xC1"),
    Board_Set_Specific_C2(0xC2, "Board Set Specific ID 0xC2"),
    Board_Set_Specific_C3(0xC3, "Board Set Specific ID 0xC3"),
    Board_Set_Specific_C4(0xC4, "Board Set Specific ID 0xC4"),
    Board_Set_Specific_C5(0xC5, "Board Set Specific ID 0xC5"),
    Board_Set_Specific_C6(0xC6, "Board Set Specific ID 0xC6"),
    Board_Set_Specific_C7(0xC7, "Board Set Specific ID 0xC7"),
    Board_Set_Specific_C8(0xC8, "Board Set Specific ID 0xC8"),
    Board_Set_Specific_C9(0xC9, "Board Set Specific ID 0xC9"),
    Board_Set_Specific_CA(0xCA, "Board Set Specific ID 0xCA"),
    Board_Set_Specific_CB(0xCB, "Board Set Specific ID 0xCB"),
    Board_Set_Specific_CC(0xCC, "Board Set Specific ID 0xCC"),
    Board_Set_Specific_CD(0xCD, "Board Set Specific ID 0xCD"),
    Board_Set_Specific_CE(0xCE, "Board Set Specific ID 0xCE"),
    Board_Set_Specific_CF(0xCF, "Board Set Specific ID 0xCF"),
    OEM_Specific_D0(0xD0, "OEM Specific ID 0xD0"),
    OEM_Specific_D1(0xD1, "OEM Specific ID 0xD1"),
    OEM_Specific_D2(0xD2, "OEM Specific ID 0xD2"),
    OEM_Specific_D3(0xD3, "OEM Specific ID 0xD3"),
    OEM_Specific_D4(0xD4, "OEM Specific ID 0xD4"),
    OEM_Specific_D5(0xD5, "OEM Specific ID 0xD5"),
    OEM_Specific_D6(0xD6, "OEM Specific ID 0xD6"),
    OEM_Specific_D7(0xD7, "OEM Specific ID 0xD7"),
    OEM_Specific_D8(0xD8, "OEM Specific ID 0xD8"),
    OEM_Specific_D9(0xD9, "OEM Specific ID 0xD9"),
    OEM_Specific_DA(0xDA, "OEM Specific ID 0xDA"),
    OEM_Specific_DB(0xDB, "OEM Specific ID 0xDB"),
    OEM_Specific_DC(0xDC, "OEM Specific ID 0xDC"),
    OEM_Specific_DD(0xDD, "OEM Specific ID 0xDD"),
    OEM_Specific_DE(0xDE, "OEM Specific ID 0xDE"),
    OEM_Specific_DF(0xDF, "OEM Specific ID 0xDF"),
    OEM_Specific_E0(0xE0, "OEM Specific ID 0xE0"),
    OEM_Specific_E1(0xE1, "OEM Specific ID 0xE1"),
    OEM_Specific_E2(0xE2, "OEM Specific ID 0xE2"),
    OEM_Specific_E3(0xE3, "OEM Specific ID 0xE3"),
    OEM_Specific_E4(0xE4, "OEM Specific ID 0xE4"),
    OEM_Specific_E5(0xE5, "OEM Specific ID 0xE5"),
    OEM_Specific_E6(0xE6, "OEM Specific ID 0xE6"),
    OEM_Specific_E7(0xE7, "OEM Specific ID 0xE7"),
    OEM_Specific_E8(0xE8, "OEM Specific ID 0xE8"),
    OEM_Specific_E9(0xE9, "OEM Specific ID 0xE9"),
    OEM_Specific_EA(0xEA, "OEM Specific ID 0xEA"),
    OEM_Specific_EB(0xEB, "OEM Specific ID 0xEB"),
    OEM_Specific_EC(0xEC, "OEM Specific ID 0xEC"),
    OEM_Specific_ED(0xED, "OEM Specific ID 0xED"),
    OEM_Specific_EE(0xEE, "OEM Specific ID 0xEE"),
    OEM_Specific_EF(0xEF, "OEM Specific ID 0xEF"),
    OEM_Specific_F0(0xF0, "OEM Specific ID 0xF0"),
    OEM_Specific_F1(0xF1, "OEM Specific ID 0xF1"),
    OEM_Specific_F2(0xF2, "OEM Specific ID 0xF2"),
    OEM_Specific_F3(0xF3, "OEM Specific ID 0xF3"),
    OEM_Specific_F4(0xF4, "OEM Specific ID 0xF4"),
    OEM_Specific_F5(0xF5, "OEM Specific ID 0xF5"),
    OEM_Specific_F6(0xF6, "OEM Specific ID 0xF6"),
    OEM_Specific_F7(0xF7, "OEM Specific ID 0xF7"),
    OEM_Specific_F8(0xF8, "OEM Specific ID 0xF8"),
    OEM_Specific_F9(0xF9, "OEM Specific ID 0xF9"),
    OEM_Specific_FA(0xFA, "OEM Specific ID 0xFA"),
    OEM_Specific_FB(0xFB, "OEM Specific ID 0xFB"),
    OEM_Specific_FC(0xFC, "OEM Specific ID 0xFC"),
    OEM_Specific_FD(0xFD, "OEM Specific ID 0xFD"),
    OEM_Specific_FE(0xFE, "OEM Specific ID 0xFE"),
    OEM_Specific_FF(0xFF, "OEM Specific ID 0xFF");
// 90h-AF, "Chassis-specific Entities. These IDs are system specific and can be assigned by the chassis provider.
// B0h-CF, "Board-set specific Entities. These IDs are system specific and can be assigned by the Board-set provider.
// D0h-FF, "OEM System Integrator defined. These IDs are system specific and can be assigned by the system integrator, or OEM.
    private final byte code;
    private final String description;
    /* pp */ SDREntityId(int code, String description) {
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
