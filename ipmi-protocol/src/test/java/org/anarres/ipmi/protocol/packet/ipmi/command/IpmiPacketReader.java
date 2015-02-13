/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.command;

import com.google.common.base.Throwables;
import com.google.common.collect.AbstractIterator;
import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.concurrent.TimeoutException;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiPacketContext;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpPacket;
import org.anarres.ipmi.protocol.packet.rmcp.RspRmcpPacket;
import org.pcap4j.core.NotOpenException;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.PcapNativeException;
import org.pcap4j.core.Pcaps;
import org.pcap4j.packet.IpV4Packet;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.UdpPacket;
import org.pcap4j.packet.namednumber.UdpPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class IpmiPacketReader extends AbstractIterator<RmcpPacket> implements Closeable {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiPacketReader.class);

    static {
        UdpPort.register(new UdpPort((short) RmcpPacket.PORT, "RMCP/IPMI"));
        UdpPort.register(new UdpPort((short) RspRmcpPacket.PORT, "RMCP/RSP"));
    }

    private final IpmiPacketContext context;
    private final PcapHandle handle;

    public IpmiPacketReader(@Nonnull IpmiPacketContext context, @Nonnull File file) throws PcapNativeException {
        this.context = context;
        this.handle = Pcaps.openOffline(file.getAbsolutePath());
    }

    protected void processRaw(Packet packet) {
    }

    protected void processUdp(UdpPacket packet) {
    }

    protected void processUdpAddress(InetSocketAddress source, InetSocketAddress target) {
    }

    protected void processUdpData(byte[] data) {
    }

    protected void processRmcp(RmcpPacket packet) {
    }

    @Override
    protected RmcpPacket computeNext() {
        try {
            Packet packet = handle.getNextPacketEx();
            processRaw(packet);

            IpV4Packet ipPacket = packet.get(IpV4Packet.class);

            // LOG.info("Read:\n" + packet);
            UdpPacket udpPacket = ipPacket.get(UdpPacket.class);
            processUdp(udpPacket);

            InetAddress sourceAddress = ipPacket.getHeader().getSrcAddr();
            InetSocketAddress sourceSocketAddress = new InetSocketAddress(sourceAddress, udpPacket.getHeader().getSrcPort().valueAsInt());
            InetAddress targetAddress = ipPacket.getHeader().getDstAddr();
            InetSocketAddress targetSocketAddress = new InetSocketAddress(targetAddress, udpPacket.getHeader().getDstPort().valueAsInt());
            processUdpAddress(sourceSocketAddress, targetSocketAddress);

            byte[] data = udpPacket.getPayload().getRawData();
            processUdpData(data);

            RmcpPacket rmcp = new RmcpPacket();
            if (sourceSocketAddress.getPort() == RmcpPacket.PORT)
                rmcp.withRemoteAddress(sourceSocketAddress);
            else if (targetSocketAddress.getPort() == RmcpPacket.PORT)
                rmcp.withRemoteAddress(targetSocketAddress);
            else
                LOG.warn("No RMCP port in " + sourceSocketAddress + " -> " + targetSocketAddress);
            rmcp.fromWire(context, ByteBuffer.wrap(data));
            processRmcp(rmcp);

            return rmcp;
        } catch (PcapNativeException | TimeoutException | NotOpenException e) {
            throw Throwables.propagate(e);
        } catch (EOFException e) {
            return endOfData();
        }
    }

    @Override
    public void close() throws IOException {
        handle.close();
    }
}
