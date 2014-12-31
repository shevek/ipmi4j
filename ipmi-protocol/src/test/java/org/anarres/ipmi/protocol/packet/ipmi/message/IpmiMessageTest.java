/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.logging.LoggingHandler;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.Ipmi15SessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiChannelPrivilegeLevel;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiLun;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionData;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpMessageClass;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpPacket;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class IpmiMessageTest {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiMessageTest.class);

    private static class Formatter extends LoggingHandler {

        @Nonnull
        public String format(@Nonnull String name, @Nonnull ByteBuf buf) {
            return super.formatByteBuf(name, buf);
        }
    }
    private final Formatter formatter = new Formatter();

    @Test
    public void testMessages() {
        GetChannelAuthenticationCapabilitiesRequest request = new GetChannelAuthenticationCapabilitiesRequest();
        request.withSource(0x81, IpmiLun.L0);
        request.withTarget(0x20, IpmiLun.L0);
        request.getExtendedData = true;
        request.channelPrivilegeLevel = IpmiChannelPrivilegeLevel.Administrator;

        IpmiSessionData data = new IpmiSessionData();
        data.setIpmiSessionWrapper(new Ipmi15SessionWrapper());
        data.setIpmiPayload(request);

        RmcpPacket packet = new RmcpPacket();
        packet.getHeader().withMessageClass(RmcpMessageClass.IPMI);
        packet.withData(data);

        ByteBuffer buf = ByteBuffer.allocate(packet.getWireLength());
        packet.toWire(buf);
        buf.flip();
        LOG.info(formatter.format("Request", Unpooled.wrappedBuffer(buf)));

        assertTrue(true);
    }
}