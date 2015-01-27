/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command.sdr;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class SDRFieldCodecTest {

    private static final Logger LOG = LoggerFactory.getLogger(SDRFieldCodecTest.class);

    private void test(@Nonnull String in, @Nonnull SDRFieldCodec.CodecType type) {
        LOG.debug("Type: " + type);
        LOG.debug("In:  " + in);
        ByteBuffer buf = ByteBuffer.allocate(in.length() * 2);
        SDRFieldCodec.encode(buf, in, type);
        buf.flip();
        byte[] data = AbstractWireable.readBytes(buf, buf.remaining());
        LOG.debug("Hex:  " + AbstractWireable.toHexString(data));
        LOG.debug("Bin:  " + AbstractWireable.toBinaryString(data));
        assertEquals(data.length, type.getEncodedLength(in) + 1);
        buf.position(0);
        String out = SDRFieldCodec.decode(buf);
        LOG.debug("Out:  " + out);
        assertEquals(in, out);
    }

    @Test
    public void testCodec() {
        BCD:
        {
            String text = SDRFieldCodec.BCDPLUS_CHARS;
            for (int i = 0; i < text.length(); i++)
                for (SDRFieldCodec.CodecType type : SDRFieldCodec.CodecType.values())
                    test(text.substring(i), type);
        }

        ASCII6:
        {
            String text = SDRFieldCodec.ASCII6_CHARS.substring(33);  // Can't encode 64 bytes.
            for (int i = 0; i < text.length(); i++)
                for (SDRFieldCodec.CodecType type : SDRFieldCodec.CodecType.values())
                    if (type != SDRFieldCodec.CodecType.BCDPlus)
                        test(text.substring(i), type);
        }
    }
}