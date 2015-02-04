/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import java.io.EOFException;
import java.io.File;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientAsfMessageHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.packet.asf.AsfRmcpData;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.client.session.IpmiContext;
import org.anarres.ipmi.protocol.client.session.IpmiSessionManager;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpPacket;
import org.anarres.ipmi.protocol.packet.rmcp.RspRmcpPacket;
import org.junit.Before;
import org.junit.Test;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;
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
    private final IpmiContext context = new IpmiSessionManager();

    @Before
    public void setUp() throws Exception {
        UdpPort.register(new UdpPort((short) RmcpPacket.PORT, "RMCP/IPMI"));
        UdpPort.register(new UdpPort((short) RspRmcpPacket.PORT, "RMCP/RSP"));
    }

    @Test
    public void testPackets() throws Exception {
        IpmiClientIpmiPayloadHandler ipmiHandler = new IpmiClientIpmiPayloadHandler.Adapter() {
            @Override
            public void handleDefault(IpmiPayload payload) {
                LOG.debug(String.valueOf(payload));
            }
        };
        IpmiClientAsfMessageHandler asfHandler = new IpmiClientAsfMessageHandler.Adapter() {
            @Override
            public void handleDefault(IpmiHandlerContext context, AsfRmcpData message) {
                LOG.debug(String.valueOf(message));
            }
        };

        File file = new File("../src/misc/ipmi-lanplus-enc0.tcpdump");
        PcapHandle handle = Pcaps.openOffline(file.getAbsolutePath());
        try {
            for (;;) {
                Packet packet = handle.getNextPacketEx();
                // LOG.info("Read:\n" + packet);
                UdpPacket udpPacket = packet.get(UdpPacket.class);

                byte[] data = udpPacket.getPayload().getRawData();
                LOG.info("Data: " + AbstractWireable.toHexString(data));

                RmcpPacket rmcp = new RmcpPacket();
                rmcp.fromWire(context, ByteBuffer.wrap(data));

                LOG.info("Read:\n" + rmcp);

                byte[] out = new byte[data.length];
                try {
                    rmcp.toWire(context, ByteBuffer.wrap(out));
                    LOG.info("Read:  " + AbstractWireable.toHexString(data));
                    LOG.info("Wrote: " + AbstractWireable.toHexString(out));
                    IpmiSessionWrapper wrapper = rmcp.getData(IpmiSessionWrapper.class);
                    if (wrapper.getIpmiPayload() instanceof IpmiOpenSessionResponse)
                        continue;
                    assertArrayEquals(data, out);
                } catch (BufferOverflowException e) {
                    LOG.warn("Overflow after " + AbstractWireable.toHexString(out));
                    throw e;
                }
            }
        } catch (EOFException e) {
        }
    }
}
