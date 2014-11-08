/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.codec;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.rmcp.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class IpmiPacketDecoder {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiPacketDecoder.class);

    @Nonnull
    public Packet decode(@Nonnull SocketAddress remoteAddress, @Nonnull ByteBuffer buf) {
        Packet packet = null;
        packet.setRemoteAddress(remoteAddress);
        packet.fromWire(buf);
        if (buf.position() < buf.limit()) {
            LOG.warn("Discarded " + (buf.limit() - buf.position()) + " trailing bytes in TFTP packet: " + buf);
            buf.position(buf.limit());
        }
        return packet;
    }
}
