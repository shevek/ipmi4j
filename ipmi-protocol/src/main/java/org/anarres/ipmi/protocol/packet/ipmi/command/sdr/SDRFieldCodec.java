/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import com.google.common.base.Charsets;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public class SDRFieldCodec {

    @Nonnull
    private static byte[] invert(@Nonnull char[] in) {
        byte[] out = new byte[256];
        Arrays.fill(out, (byte) -1);
        for (int i = 0; i < in.length; i++)
            out[in[i]] = (byte) i;
        return out;
    }
    private static char[] BCDPLUS_DECODE = "0123456789 -.:,_".toCharArray();
    private static byte[] BCDPLUS_ENCODE = invert(BCDPLUS_DECODE);

    @Nonnull
    public static String decodeBcdPlus(@Nonnull ByteBuffer in, @Nonnegative int length) {
        StringBuilder out = new StringBuilder();
        for (int i = 0; i < length; i++) {
            byte b = 0; // in[offset + i];
            out.append(BCDPLUS_DECODE[b & 0xF]);
            out.append(BCDPLUS_DECODE[(b >> 4) & 0xF]);
        }
        return out.toString();
    }

    @Nonnull
    public static void encodeBcdPlus(@Nonnull ByteBuffer out, @Nonnull String in) {
        for (int i = 0; i < in.length(); i++) {
            int nybble = BCDPLUS_ENCODE[in.charAt(i)];
            // out[i >> 1] |= nybble << (4 * (i & 1));
        }
    }
    //                                           01 23456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789AB CDEF
    private static final char[] ASCII6_DECODE = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_".toCharArray();
    private static final byte[] ASCII6_ENCODE = invert(ASCII6_DECODE);

    @Nonnull
    public static String decodeAscii6(@Nonnull ByteBuffer in, @Nonnegative int length) {
        StringBuilder out = new StringBuilder();
        int accum = 0;
        for (int i = 0; i < length; i++) {
            accum = accum << 8 | (in.get() & 0xFF);
        }
        return out.toString();
    }

    @Nonnull
    public static String decodeAscii(@Nonnull ByteBuffer in, @Nonnegative int length) {
        int limit = in.limit();
        try {
            in.limit(in.position() + length);
            return Charsets.ISO_8859_1.decode(in).toString();
        } finally {
            in.limit(limit);
        }
    }

    public static void encodeAscii(@Nonnull ByteBuffer out, @Nonnull String in) {
        Charsets.ISO_8859_1.encode(in);
    }

    public int decodeLength(@Nonnull ByteBuffer in) {
        byte tmp = in.get();
        int type = (tmp >> 6) & 0x11;
        int length = tmp & 0x1F;
        return length;
    }
}
