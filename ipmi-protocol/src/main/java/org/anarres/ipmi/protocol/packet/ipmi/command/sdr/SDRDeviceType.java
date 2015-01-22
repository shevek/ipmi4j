/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import com.google.common.primitives.UnsignedBytes;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 43.13, table 43-12, page 552.
 *
 * @author shevek
 */
public enum SDRDeviceType implements Code.Wrapper {

    Reserved_00(0x00, "reserved.", UnspecifiedSubType.class),
    Reserved_01(0x01, "reserved.", UnspecifiedSubType.class),
    DS1624_Temperature_Sensor(0x02, "DS1624 temperature sensor / EEPROM or equivalent", UnspecifiedSubType.class),
    DS1621_Temperature_Sensor(0x03, "DS1621 temperature sensor or equivalent", UnspecifiedSubType.class),
    LM75_Tempreature_Sensor(0x04, "LM75 Temperature Sensor or equivalent", UnspecifiedSubType.class),
    Heceta(0x05, "Heceta ASIC or similar", HecetaSubType.class),
    Reserved_06(0x06, "reserved"),
    Reserved_07(0x07, "reserved"),
    EEPROM_24C01(0x08, "EEPROM, 24C01 or equivalent", EEPROMSubType.class),
    EEPROM_24C02(0x09, "EEPROM, 24C02 or equivalent", EEPROMSubType.class),
    EEPROM_24C04(0x0A, "EPROM, 24C04 or equivalent", EEPROMSubType.class),
    EEPROM_24C08(0x0B, "EPROM, 24C08 or equivalent", EEPROMSubType.class),
    EEPROM_24C16(0x0C, "EPROM, 24C16 or equivalent", EEPROMSubType.class),
    EEPROM_24C17(0x0D, "EPROM, 24C17 or equivalent", EEPROMSubType.class),
    EEPROM_24C32(0x0E, "EEPROM, 24C32 or equivalent", EEPROMSubType.class),
    EEPROM_24C64(0x0F, "EEPROM, 24C64 or equivalent", EEPROMSubType.class),
    /** Accessed using Read/Write FRU commands at LUN other than 00b. */
    FRU_Inventory_Device(0x10, "FRU Inventory Device behind management controller", FRUInventoryDeviceSubType.class),
    Reserved_11(0x11, "reserved"),
    Reserved_12(0x12, "reserved"),
    Reserved_13(0x13, "reserved"),
    PCF_8570(0x14, "PCF 8570 256 byte RAM or equivalent", UnspecifiedSubType.class),
    PCF_8573(0x15, "PCF 8573 clock/calendar or equivalent", UnspecifiedSubType.class),
    PCF_8574A(0x16, "PCF 8574A â<80><98>i/o portâ<80><99> or equivalent", UnspecifiedSubType.class),
    PCF_8583(0x17, "PCF 8583 clock/calendar or equivalent", UnspecifiedSubType.class),
    PCF_8593(0x18, "PCF 8593 clock/calendar or equivalent", UnspecifiedSubType.class),
    Clock_Calendar(0x19, "Clock calendar, type not specified", UnspecifiedSubType.class),
    PCF_8591(0x1A, "PCF 8591 A/D, D/A Converter or equivalent", UnspecifiedSubType.class),
    IO_Port(0x1B, "i/o port, specific device not specified", UnspecifiedSubType.class),
    AD_Converter(0x1C, "A/D Converter, specific device not specified", UnspecifiedSubType.class),
    DA_Converter(0x1D, "D/A Converter, specific device not specified", UnspecifiedSubType.class),
    ADDA_Converter(0x1E, "A/D, D/A Converter, specific device not specified", UnspecifiedSubType.class),
    LCD_Controller(0x1F, "LCD controller / Driver, specific device not specified", UnspecifiedSubType.class),
    Core_Logic_Device(0x20, "Core Logic (Chip set) Device, specific device not specified", UnspecifiedSubType.class),
    LMC6874_Battery_Controller(0x21, "LMC6874 Intelligent Battery controller, or equivalent", UnspecifiedSubType.class),
    Intelligent_Battery_Controller(0x22, "Intelligent Battery controller, specific device not specified", UnspecifiedSubType.class),
    Combo_Management_ASIC(0x23, "Combo Management ASIC, specific device not specified", UnspecifiedSubType.class),
    Maxim_1617_Temperature_Sensor(0x24, "Maxim 1617 Temperature Sensor", UnspecifiedSubType.class),
    Other(0xBF, "Other / unspecified device"),
    OEM_C0(0xC0, "OEM type 0xC0"),
    OEM_C1(0xC1, "OEM type 0xC1"),
    OEM_C2(0xC2, "OEM type 0xC2"),
    OEM_C3(0xC3, "OEM type 0xC3"),
    OEM_C4(0xC4, "OEM type 0xC4"),
    OEM_C5(0xC5, "OEM type 0xC5"),
    OEM_C6(0xC6, "OEM type 0xC6"),
    OEM_C7(0xC7, "OEM type 0xC7"),
    OEM_C8(0xC8, "OEM type 0xC8"),
    OEM_C9(0xC9, "OEM type 0xC9"),
    OEM_CA(0xCA, "OEM type 0xCA"),
    OEM_CB(0xCB, "OEM type 0xCB"),
    OEM_CC(0xCC, "OEM type 0xCC"),
    OEM_CD(0xCD, "OEM type 0xCD"),
    OEM_CE(0xCE, "OEM type 0xCE"),
    OEM_CF(0xCF, "OEM type 0xCF"),
    OEM_D0(0xD0, "OEM type 0xD0"),
    OEM_D1(0xD1, "OEM type 0xD1"),
    OEM_D2(0xD2, "OEM type 0xD2"),
    OEM_D3(0xD3, "OEM type 0xD3"),
    OEM_D4(0xD4, "OEM type 0xD4"),
    OEM_D5(0xD5, "OEM type 0xD5"),
    OEM_D6(0xD6, "OEM type 0xD6"),
    OEM_D7(0xD7, "OEM type 0xD7"),
    OEM_D8(0xD8, "OEM type 0xD8"),
    OEM_D9(0xD9, "OEM type 0xD9"),
    OEM_DA(0xDA, "OEM type 0xDA"),
    OEM_DB(0xDB, "OEM type 0xDB"),
    OEM_DC(0xDC, "OEM type 0xDC"),
    OEM_DD(0xDD, "OEM type 0xDD"),
    OEM_DE(0xDE, "OEM type 0xDE"),
    OEM_DF(0xDF, "OEM type 0xDF"),
    OEM_E0(0xE0, "OEM type 0xE0"),
    OEM_E1(0xE1, "OEM type 0xE1"),
    OEM_E2(0xE2, "OEM type 0xE2"),
    OEM_E3(0xE3, "OEM type 0xE3"),
    OEM_E4(0xE4, "OEM type 0xE4"),
    OEM_E5(0xE5, "OEM type 0xE5"),
    OEM_E6(0xE6, "OEM type 0xE6"),
    OEM_E7(0xE7, "OEM type 0xE7"),
    OEM_E8(0xE8, "OEM type 0xE8"),
    OEM_E9(0xE9, "OEM type 0xE9"),
    OEM_EA(0xEA, "OEM type 0xEA"),
    OEM_EB(0xEB, "OEM type 0xEB"),
    OEM_EC(0xEC, "OEM type 0xEC"),
    OEM_ED(0xED, "OEM type 0xED"),
    OEM_EE(0xEE, "OEM type 0xEE"),
    OEM_EF(0xEF, "OEM type 0xEF"),
    OEM_F0(0xF0, "OEM type 0xF0"),
    OEM_F1(0xF1, "OEM type 0xF1"),
    OEM_F2(0xF2, "OEM type 0xF2"),
    OEM_F3(0xF3, "OEM type 0xF3"),
    OEM_F4(0xF4, "OEM type 0xF4"),
    OEM_F5(0xF5, "OEM type 0xF5"),
    OEM_F6(0xF6, "OEM type 0xF6"),
    OEM_F7(0xF7, "OEM type 0xF7"),
    OEM_F8(0xF8, "OEM type 0xF8"),
    OEM_F9(0xF9, "OEM type 0xF9"),
    OEM_FA(0xFA, "OEM type 0xFA"),
    OEM_FB(0xFB, "OEM type 0xFB"),
    OEM_FC(0xFC, "OEM type 0xFC"),
    OEM_FD(0xFD, "OEM type 0xFD"),
    OEM_FE(0xFE, "OEM type 0xFE"),
    OEM_FF(0xFF, "OEM type 0xFF");

    public static enum UnspecifiedSubType implements SDRDeviceSubtype {

        Unspecified(0x00, "Unspecified");
        private final byte code;
        private final String description;
        /* pp */ UnspecifiedSubType(int code, String description) {
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

    public static class UnknownSubType implements SDRDeviceSubtype {

        private final byte code;

        public UnknownSubType(byte code) {
            this.code = code;
        }

        @Override
        public byte getCode() {
            return code;
        }
    }

    public static enum HecetaSubType implements SDRDeviceSubtype {

        Heceta1(0x00, "Heceta 1 e.g. LM78"),
        Heceta2(0x01, "Heceta 2 e.g. LM79"),
        LM80(0x02, "LM80"),
        Heceta3(0x03, "Heceta 3 e.g. LM81/ ADM9240 / DS1780"),
        Heceta4(0x04, "Heceta 4"),
        Heceta5(0x05, "Heceta 5");
        private final byte code;
        private final String description;
        /* pp */ HecetaSubType(int code, String description) {
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

    public static enum EEPROMSubType implements SDRDeviceSubtype {

        Unspecified(0x00, "unspecified"),
        DIMM_Memory_ID(0x01, "DIMM Memory ID"),
        IPMI_FRU_Inventory(0x02, "IPMI FRU Inventory"),
        System_Processor_Cartridge_FRU(0x03, "System Processor Cartridge FRU / PIROM (processor information ROM)");
        private final byte code;
        private final String description;
        /* pp */ EEPROMSubType(int code, String description) {
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

    public static enum FRUInventoryDeviceSubType implements SDRDeviceSubtype {

        IPMI_FRU_Inventory_Compat(0x00, "IPMI FRU Inventory"),
        DIMM_Memory_ID(0x01, "DIMM Memory ID"),
        IPMI_FRU_Inventory(0x02, "IPMI FRU Inventory"),
        System_Processor_Cartridge_FRU(0x03, "System Processor Cartridge FRU / PIROM (processor information ROM)"),
        Unspecified(0xFF, "unspecified");
        private final byte code;
        private final String description;
        /* pp */ FRUInventoryDeviceSubType(int code, String description) {
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
    @Nonnull
    private final String description;
    @CheckForNull
    private final Class<? extends SDRDeviceSubtype> subtype;

    /* pp */ SDRDeviceType(@Nonnegative int code, @Nonnull String description, @CheckForNull Class<? extends SDRDeviceSubtype> subtype) {
        this.code = UnsignedBytes.checkedCast(code);
        this.description = description;
        this.subtype = subtype;
    }

    /* pp */ SDRDeviceType(@Nonnegative int code, @Nonnull String description) {
        this(code, description, null);
    }

    @Override
    public byte getCode() {
        return code;
    }

    @Nonnull
    public String getDescription() {
        return description;
    }

    @Nonnull
    public SDRDeviceSubtype getSubtype(byte code) {
        // SDRDeviceSubtype doesn't capture that it's an enum.
        if (subtype != null)
            for (SDRDeviceSubtype value : subtype.getEnumConstants())
                if (value.getCode() == code)
                    return value;
        return new UnknownSubType(code);
    }

    @Override
    public String toString() {
        return "0x" + UnsignedBytes.toString(getCode(), 16) + ": " + getDescription();
    }
}