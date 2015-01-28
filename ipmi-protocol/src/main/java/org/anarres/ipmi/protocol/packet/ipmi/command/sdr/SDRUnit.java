/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import com.google.common.primitives.UnsignedBytes;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 *
 * @author shevek
 */
public enum SDRUnit implements Code.DescriptiveWrapper {

    Unspecified(0, "unspecified"),
    Degrees_C(1, "degrees C"),
    Degrees_F(2, "degrees F"),
    Degrees_K(3, "degrees K"),
    Volts(4, "Volts"),
    Amps(5, "Amps"),
    Watts(6, "Watts"),
    Joules(7, "Joules"),
    Coulombs(8, "Coulombs"),
    VA(9, "VA"),
    Nits(10, "Nits"),
    lumen(11, "lumen"),
    lux(12, "lux"),
    Candela(13, "Candela"),
    kPa(14, "kPa"),
    PSI(15, "PSI"),
    Newton(16, "Newton"),
    CFM(17, "CFM"),
    RPM(18, "RPM"),
    Hz(19, "Hz"),
    microsecond(20, "microsecond"),
    millisecond(21, "millisecond"),
    second(22, "second"),
    minute(23, "minute"),
    hour(24, "hour"),
    day(25, "day"),
    week(26, "week"),
    mil(27, "mil"),
    inches(28, "inches"),
    feet(29, "feet"),
    cubic_in(30, "cu in"),
    cubic_feet(31, "cu feet"),
    mm(32, "mm"),
    cm(33, "cm"),
    m(34, "m"),
    cubic_cm(35, "cu cm"),
    cubic_m(36, "cu m"),
    liters(37, "liters"),
    fluid_ounce(38, "fluid ounce"),
    radians(39, "radians"),
    steradians(40, "steradians"),
    revolutions(41, "revolutions"),
    cycles(42, "cycles"),
    gravities(43, "gravities"),
    ounce(44, "ounce"),
    pound(45, "pound"),
    ft_lb(46, "ft-lb"),
    oz_in(47, "oz-in"),
    gauss(48, "gauss"),
    gilberts(49, "gilberts"),
    henry(50, "henry"),
    millihenry(51, "millihenry"),
    farad(52, "farad"),
    microfarad(53, "microfarad"),
    ohms(54, "ohms"),
    siemens(55, "siemens"),
    mole(56, "mole"),
    becquerel(57, "becquerel"),
    PPM(58, "PPM (parts/million)"),
    Reserved_59(59, "reserved"),
    Decibels(60, "Decibels"),
    DbA(61, "DbA"),
    DbC(62, "DbC"),
    gray(63, "gray"),
    sievert(64, "sievert"),
    color_temp_deg_K(65, "color temp deg K"),
    bit(66, "bit"),
    kilobit(67, "kilobit"),
    megabit(68, "megabit"),
    gigabit(69, "gigabit"),
    Byte(70, "byte"),
    kilobyte(71, "kilobyte"),
    megabyte(72, "megabyte"),
    gigabyte(73, "gigabyte"),
    word(74, "word (data)"),
    dword(75, "dword"),
    qword(76, "qword"),
    line(77, "line (re. mem. line)"),
    hit(78, "hit"),
    miss(79, "miss"),
    retry(80, "retry"),
    reset(81, "reset"),
    overrun(82, "overrun / overflow"),
    underrun(83, "underrun"),
    collision(84, "collision"),
    packets(85, "packets"),
    messages(86, "messages"),
    characters(87, "characters"),
    error(88, "error"),
    correctable_error(89, "correctable error"),
    uncorrectable_error(90, "uncorrectable error"),
    fatal_error(91, "fatal error"),
    grams(92, "grams");
    private final byte code;
    private final String description;
    /* pp */ SDRUnit(int code, String description) {
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
        return "0x" + UnsignedBytes.toString(getCode(), 16) + ": " + getDescription();
    }
}
