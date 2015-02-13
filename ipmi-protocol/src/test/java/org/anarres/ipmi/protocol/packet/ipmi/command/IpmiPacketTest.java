/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import java.io.File;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionResponse;
import org.anarres.ipmi.protocol.client.session.IpmiPacketContext;
import org.anarres.ipmi.protocol.client.session.IpmiSessionManager;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpPacket;
import org.anarres.ipmi.protocol.packet.rmcp.RspRmcpPacket;
import org.junit.Before;
import org.junit.Test;
import org.pcap4j.packet.namednumber.UdpPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class IpmiPacketTest {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiPacketTest.class);
    private final IpmiPacketContext context = new IpmiSessionManager();

    @Before
    public void setUp() throws Exception {
        UdpPort.register(new UdpPort((short) RmcpPacket.PORT, "RMCP/IPMI"));
        UdpPort.register(new UdpPort((short) RspRmcpPacket.PORT, "RMCP/RSP"));
    }

    @Test
    public void testPackets() throws Exception {
        File file = new File("../src/misc/ipmi-lanplus-enc0.tcpdump");
        final AtomicReference<byte[]> dataReference = new AtomicReference<>();
        try (IpmiPacketReader reader = new IpmiPacketReader(context, file) {
            @Override
            protected void processUdpData(byte[] data) {
                LOG.info("Data: " + AbstractWireable.toHexString(data));
                dataReference.set(data);
            }
        }) {
            while (reader.hasNext()) {
                RmcpPacket packet = reader.next();
                LOG.info("Read:\n" + packet);

                byte[] data = dataReference.get();
                byte[] out = new byte[data.length];
                try {
                    packet.toWire(context, ByteBuffer.wrap(out));
                    // LOG.info("Read:  " + AbstractWireable.toHexString(data));
                    LOG.info("Wrote: " + AbstractWireable.toHexString(out));
                    IpmiSessionWrapper wrapper = packet.getData(IpmiSessionWrapper.class);
                    if (wrapper.getIpmiPayload() instanceof IpmiOpenSessionResponse)
                        continue;
                    assertArrayEquals(data, out);
                } catch (BufferOverflowException e) {
                    LOG.warn("Overflow after " + AbstractWireable.toHexString(out));
                    throw e;
                }
            }
        }
    }
}
