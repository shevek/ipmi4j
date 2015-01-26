/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import com.google.common.base.Charsets;
import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import java.util.Arrays;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 43.15, pages 554-556.
 *
 * @author shevek
 */
public class SDRFieldCodec {

    /**
     * [IPMI2] Section 43.15, page 555.
     */
    public static enum CodecType implements Code.Wrapper {

        Unicode(0b00),
        BCDPlus(0b01),
        Ascii6(0b10),
        Ascii8(0b11);
        private final byte code;

        private CodecType(@Nonnegative int code) {
            this.code = UnsignedBytes.checkedCast(code);
        }

        @Override
        public byte getCode() {
            return code;
        }
    }

    @Nonnull
    private static byte[] invert(@Nonnull char[] in) {
        byte[] out = new byte[256];
        Arrays.fill(out, (byte) -1);
        for (int i = 0; i < in.length; i++) {
            UnsignedBytes.checkedCast(in[i]);   // It had better be in [0,255]
            out[in[i]] = UnsignedBytes.checkedCast(i);
        }
        return out;
    }

    private static byte encode(@Nonnull CodecType type, @Nonnull byte[] db, @Nonnull char in) {
        byte out = db[in];
        // All these charsets are sparse, so none of them use 255 as an unsigned value.
        if (out == -1)
            throw new IllegalArgumentException("Cannot encode character " + Character.toString(in) + " in " + type);
        return out;
    }

    private static char decode(@Nonnull char[] db, @Nonnull int in) {
        if (in > db.length)
            return '?';
        return db[in];
    }
    public static final String BCDPLUS_CHARS = "0123456789 -.:,_";
    private static final char[] BCDPLUS_DECODE = BCDPLUS_CHARS.toCharArray();
    private static final byte[] BCDPLUS_ENCODE = invert(BCDPLUS_DECODE);

    @Nonnull
    public static String decodeBcdPlus(@Nonnull ByteBuffer in, @Nonnegative int length) {
        StringBuilder out = new StringBuilder();
        int b = 0;
        for (int i = 0; i < length; i++) {
            if ((i & 1) == 0)
                b = in.get();
            else
                b = b >> 4;
            out.append(decode(BCDPLUS_DECODE, b & 0b1111));
        }
        return out.toString();
    }

    @Nonnull
    public static void encodeBcdPlus(@Nonnull ByteBuffer out, @Nonnull String in) {
        byte b = 0;
        for (int i = 0; i < in.length(); i++) {
            byte nibble = encode(CodecType.BCDPlus, BCDPLUS_ENCODE, in.charAt(i));
            if ((i & 1) == 0) {
                b = nibble;
            } else {
                b |= nibble << 4;
                out.put(b);
            }
            // out[i >> 1] |= nybble << (4 * (i & 1));
        }
        if ((in.length() & 1) != 0)
            out.put(b);
    }
    //                                         01 23456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789AB CDEF
    public static final String ASCII6_CHARS = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_";
    private static final char[] ASCII6_DECODE = ASCII6_CHARS.toCharArray();
    private static final byte[] ASCII6_ENCODE = invert(ASCII6_DECODE);

    @Nonnull
    public static String decodeAscii6(@Nonnull ByteBuffer in, @Nonnegative int length) {
        StringBuilder out = new StringBuilder();
        int accum = 0;
        for (int i = 0; i < length; i++) {
            switch (i & 0x3) {
                case 0:
                    accum = in.get() & 0xFF;
                    break;
                case 1:
                    accum = (accum >> 6) | ((in.get() & 0xFF) << 2);
                    break;
                case 2:
                    accum = (accum >> 6) | ((in.get() & 0xFF) << 4);
                    break;
                case 3:
                    accum = (accum >> 6);
                    break;
            }
            out.append(decode(ASCII6_DECODE, accum & 0x3F));
        }
        return out.toString();
    }

    public static void encodeAscii6(@Nonnull ByteBuffer out, @Nonnull String in) {
        int accum = 0;
        for (int i = 0; i < in.length(); i++) {
            byte b = encode(CodecType.Ascii6, ASCII6_ENCODE, in.charAt(i));
            switch (i & 0x3) {
                case 0:
                    accum = b & 0x3F;
                    break;
                case 1:
                    accum |= (b & 0x3F) << 6;
                    out.put((byte) accum);
                    accum >>= Byte.SIZE;
                    break;
                case 2:
                    accum |= (b & 0x3F) << 4;
                    out.put((byte) accum);
                    accum >>= Byte.SIZE;
                    break;
                case 3:
                    accum |= (b & 0x3F) << 2;
                    out.put((byte) accum);
                    break;
            }
        }
        if ((in.length() & 3) != 0)
            out.put((byte) accum);
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
        out.put(Charsets.ISO_8859_1.encode(in));
    }

    /**
     * Nobody seems to know what unicode encoding is required, so we will assume UTF8.
     * None of the other client libraries even seem to do the decoding anyway.
     */
    @Nonnull
    public static String decodeUnicode(@Nonnull ByteBuffer in, @Nonnegative int length) {
        int limit = in.limit();
        try {
            in.limit(in.position() + length);
            return Charsets.UTF_8.decode(in).toString();
        } finally {
            in.limit(limit);
        }
    }

    public static void encodeUnicode(@Nonnull ByteBuffer out, @Nonnull String in) {
        out.put(Charsets.UTF_8.encode(in));
    }

    @Nonnull
    public static String decode(@Nonnull ByteBuffer in) {
        byte tmp = in.get();
        int length = tmp & 0x1F;
        CodecType type = Code.fromInt(CodecType.class, (tmp >> 6) & 0x3);
        switch (type) {
            case Unicode:
                return decodeUnicode(in, length);
            case BCDPlus:
                return decodeBcdPlus(in, length);
            case Ascii6:
                return decodeAscii6(in, length);
            case Ascii8:
                return decodeAscii(in, length);
            default:
                throw new IllegalArgumentException("Unknown CodecType " + type);
        }
    }

    @Nonnull
    public static void encode(@Nonnull ByteBuffer out, @Nonnull String in, @Nonnull CodecType type) {
        int length = in.length();
        int tmp = (length & 0x1F) | (type.getCode() << 6);
        out.put((byte) tmp);
        switch (type) {
            case Unicode:
                encodeUnicode(out, in);
                break;
            case BCDPlus:
                encodeBcdPlus(out, in);
                break;
            case Ascii6:
                encodeAscii6(out, in);
                break;
            case Ascii8:
                encodeAscii(out, in);
                break;
            default:
                throw new IllegalArgumentException("Unknown CodecType " + type);
        }
    }
}
