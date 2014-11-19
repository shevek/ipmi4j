/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.codec;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.asf.AbstractAsfData;
import org.anarres.ipmi.protocol.packet.asf.AsfRcmpMessageType;
import org.anarres.ipmi.protocol.packet.common.Code;
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

    public void parse(@Nonnull ByteBuffer buffer, @Nonnull Packet packet) {
        int start = buffer.position();
        packet.fromWireHeader(buffer);
        int position = buffer.position();
        switch (packet.getHeader().getMessageClass()) {
            case ASF:
                int enterpriseNumber = buffer.getInt();
                if (enterpriseNumber != AbstractAsfData.IANA_ENTERPRISE_NUMBER)
                    throw new IllegalArgumentException("Unknown enterprise number 0x" + Integer.toHexString(enterpriseNumber));
                AsfRcmpMessageType messageType = Code.fromBuffer(AsfRcmpMessageType.class, buffer);
                packet.withData(messageType.newPacketData());
                buffer.position(position);
                packet.fromWireBody(buffer, start);
                break;
            case IPMI:
            case OEM:
            default:
                throw new IllegalArgumentException("Can't decode buffer.");
        }
    }

    @Nonnull
    public Packet decode(@Nonnull SocketAddress remoteAddress, @Nonnull ByteBuffer buf) {
        Packet packet = new RmcpPacket();
        packet.setRemoteAddress(remoteAddress);
        packet.toWire(buf);
        if (buf.position() < buf.limit()) {
            LOG.warn("Discarded " + (buf.limit() - buf.position()) + " trailing bytes in RMCP packet: " + buf);
            buf.position(buf.limit());
        }
        return packet;
    }
}
