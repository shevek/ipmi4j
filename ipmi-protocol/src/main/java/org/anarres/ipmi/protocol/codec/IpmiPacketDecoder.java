/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.codec;

import java.net.DatagramPacket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.rmcp.Packet;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class IpmiPacketDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiPacketDecoder.class);

    @Nonnull
    public DatagramPacket encode(@Nonnull Packet packet) throws SocketException {
        int length = packet.getWireLength();
        ByteBuffer buf = ByteBuffer.allocate(length);
        packet.toWire(buf);
        return new DatagramPacket(buf.array(), buf.position(), packet.getRemoteAddress());
    }

    @Nonnull
    public Packet decode(@Nonnull SocketAddress remoteAddress, @Nonnull ByteBuffer buf) {
        RmcpPacket packet = new RmcpPacket();
        packet.withRemoteAddress(remoteAddress);
        packet.fromWire(buf);
        if (buf.position() < buf.limit()) {
            LOG.warn("Discarded " + (buf.limit() - buf.position()) + " trailing bytes in RMCP packet: " + buf);
            buf.position(buf.limit());
        }
        return packet;
    }
}
