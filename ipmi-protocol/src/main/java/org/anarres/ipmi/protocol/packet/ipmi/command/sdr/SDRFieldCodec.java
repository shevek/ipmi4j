/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import com.google.common.base.Charsets;
import com.google.common.math.IntMath;
import com.google.common.primitives.UnsignedBytes;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
     * Nobody seems to know what unicode encoding is required, so based
     * on the date of publication, we will assume UTF16.
     * None of the other client libraries even seem to do the decoding anyway.
     * Given that this is a hardware spec, one rather suspects it will be UCS2.
     */
    public static final Charset UNICODE_CHARSET = StandardCharsets.UTF_16;

    /**
     * [IPMI2] Section 43.15, page 555.
     */
    public static enum CodecType implements Code.Wrapper {

        Unicode(0b00) {
            @Override
            public int getEncodedLength(String in) {
                return UNICODE_CHARSET.encode(in).remaining();
            }

            @Override
            public void encode(ByteBuffer out, String in) {
                encodeUnicode(out, in);
            }

            @Override
            public String decode(ByteBuffer in, int length) {
                return decodeUnicode(in, length);
            }
        },
        BCDPlus(0b01) {
            @Override
            public int getEncodedLength(String in) {
                return IntMath.divide(in.length(), 2, RoundingMode.CEILING);
            }

            @Override
            public void encode(ByteBuffer out, String in) {
                encodeBcdPlus(out, in);
            }

            @Override
            public String decode(ByteBuffer in, int length) {
                return decodeBcdPlus(in, length);
            }
        },
        Ascii6(0b10) {
            @Override
            public int getEncodedLength(String in) {
                return IntMath.divide(in.length() * 3, 4, RoundingMode.CEILING);
            }

            @Override
            public void encode(ByteBuffer out, String in) {
                encodeAscii6(out, in);
            }

            @Override
            public String decode(ByteBuffer in, int length) {
                return decodeAscii6(in, length);
            }
        },
        Ascii8(0b11) {
            @Override
            public int getEncodedLength(String in) {
                return in.length();
            }

            @Override
            public void encode(ByteBuffer out, String in) {
                encodeAscii(out, in);
            }

            @Override
            public String decode(ByteBuffer in, int length) {
                return decodeAscii(in, length);
            }
        };
        private final byte code;

        private CodecType(@Nonnegative int code) {
            this.code = UnsignedBytes.checkedCast(code);
        }

        @Override
        public byte getCode() {
            return code;
        }

        @Nonnegative
        public abstract int getEncodedLength(@Nonnull String in);

        public abstract void encode(@Nonnull ByteBuffer out, @Nonnull String in);

        @Nonnull
        public abstract String decode(@Nonnull ByteBuffer in, @Nonnegative int length);
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
    private static String decodeCharset(@Nonnull ByteBuffer in, @Nonnegative int length, @Nonnull Charset charset) {
        int limit = in.limit();
        try {
            in.limit(in.position() + length);
            return charset.decode(in).toString();
        } finally {
            in.limit(limit);
        }
    }

    @Nonnull
    public static String decodeAscii(@Nonnull ByteBuffer in, @Nonnegative int length) {
        return decodeCharset(in, length, Charsets.ISO_8859_1);
    }

    public static void encodeAscii(@Nonnull ByteBuffer out, @Nonnull String in) {
        out.put(Charsets.ISO_8859_1.encode(in));
    }

    @Nonnull
    public static String decodeUnicode(@Nonnull ByteBuffer in, @Nonnegative int length) {
        return decodeCharset(in, length, UNICODE_CHARSET);
    }

    public static void encodeUnicode(@Nonnull ByteBuffer out, @Nonnull String in) {
        out.put(UNICODE_CHARSET.encode(in));
    }

    @Nonnull
    public static String decode(@Nonnull ByteBuffer in) {
        byte tmp = in.get();
        int length = tmp & 0x1F;
        CodecType type = Code.fromInt(CodecType.class, (tmp >> 6) & 0x3);
        return type.decode(in, length);
    }

    @Nonnull
    public static void encode(@Nonnull ByteBuffer out, @Nonnull String in, @Nonnull CodecType type) {
        int length = in.length();
        int tmp = (length & 0x1F) | (type.getCode() << 6);
        out.put((byte) tmp);
        type.encode(out, in);
    }
}
