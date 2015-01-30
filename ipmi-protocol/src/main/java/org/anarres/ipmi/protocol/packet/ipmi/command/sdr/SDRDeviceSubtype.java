/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import com.google.common.primitives.UnsignedBytes;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 43.13, table 43-12, page 552, rightmost column.
 *
 * @author shevek
 */
public interface SDRDeviceSubtype extends Code.DescriptiveWrapper {

    /**
     * [IPMI2] Section 43.13, table 43-12, page 552, rightmost column.
     * 
     * Many {@link SDRDeviceType SDRDeviceTypes} have only one subtype, "Unknown"
     * with a value of 0. This is a singleton enum supporting that subtype only.
     */
    public static enum Unspecified implements SDRDeviceSubtype {

        Unspecified(0x00, "Unspecified");
        private final byte code;
        private final String description;
        /* pp */ Unspecified(int code, String description) {
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

    /** An undocumented or unsupported SDRDeviceSubtype. */
    public static class Unknown implements SDRDeviceSubtype {

        private final byte code;

        public Unknown(byte code) {
            this.code = code;
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Override
        public String getDescription() {
            return "(unknown)";
        }

        @Override
        public String toString() {
            return "Unknown(0x" + UnsignedBytes.toString(getCode(), 16) + ")";
        }
    }

    /**
     * [IPMI2] Section 43.13, table 43-12, page 552, rightmost column.
     */
    public static enum Heceta implements SDRDeviceSubtype {

        Heceta1(0x00, "Heceta 1 e.g. LM78"),
        Heceta2(0x01, "Heceta 2 e.g. LM79"),
        LM80(0x02, "LM80"),
        Heceta3(0x03, "Heceta 3 e.g. LM81/ ADM9240 / DS1780"),
        Heceta4(0x04, "Heceta 4"),
        Heceta5(0x05, "Heceta 5");
        private final byte code;
        private final String description;
        /* pp */ Heceta(int code, String description) {
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
     * [IPMI2] Section 43.13, table 43-12, page 552, rightmost column.
     */
    public static enum EEPROM implements SDRDeviceSubtype {

        Unspecified(0x00, "unspecified"),
        DIMM_Memory_ID(0x01, "DIMM Memory ID"),
        IPMI_FRU_Inventory(0x02, "IPMI FRU Inventory"),
        System_Processor_Cartridge_FRU(0x03, "System Processor Cartridge FRU / PIROM (processor information ROM)");
        private final byte code;
        private final String description;
        /* pp */ EEPROM(int code, String description) {
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
     * [IPMI2] Section 43.13, table 43-12, page 552, rightmost column.
     */
    public static enum FRUInventoryDevice implements SDRDeviceSubtype {

        IPMI_FRU_Inventory_Compat(0x00, "IPMI FRU Inventory"),
        DIMM_Memory_ID(0x01, "DIMM Memory ID"),
        IPMI_FRU_Inventory(0x02, "IPMI FRU Inventory"),
        System_Processor_Cartridge_FRU(0x03, "System Processor Cartridge FRU / PIROM (processor information ROM)"),
        Unspecified(0xFF, "unspecified");
        private final byte code;
        private final String description;
        /* pp */ FRUInventoryDevice(int code, String description) {
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
