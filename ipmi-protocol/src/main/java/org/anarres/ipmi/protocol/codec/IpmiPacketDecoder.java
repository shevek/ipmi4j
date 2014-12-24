/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.codec;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.asf.AbstractAsfData;
import org.anarres.ipmi.protocol.packet.asf.AsfRmcpMessageType;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiHeaderAuthenticationType;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;
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
                if (enterpriseNumber != AbstractAsfData.IANA_ENTERPRISE_NUMBER.getNumber())
                    throw new IllegalArgumentException("Unknown enterprise number 0x" + Integer.toHexString(enterpriseNumber));
                AsfRmcpMessageType messageType = Code.fromBuffer(AsfRmcpMessageType.class, buffer);
                packet.withData(messageType.newPacketData());
                buffer.position(position);
                packet.fromWireBody(buffer, start);
                break;
            case IPMI:
                IpmiHeaderAuthenticationType format = Code.fromByte(IpmiHeaderAuthenticationType.class, buffer.get());
                if (format == IpmiHeaderAuthenticationType.RMCPP) {
                    // IPMI v2.0
                    IpmiPayloadType payloadType = Code.fromByte(IpmiPayloadType.class, buffer.get());
                } else {
                    // IPMI v1.5
                }
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
