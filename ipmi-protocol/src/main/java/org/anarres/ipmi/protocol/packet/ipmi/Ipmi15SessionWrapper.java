/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSessionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [IPMI2] Section 13.6 pages 133-134, column 1.
 *
 * @author shevek
 */
public class Ipmi15SessionWrapper extends AbstractIpmiSessionWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(Ipmi15SessionWrapper.class);
    private IpmiSessionAuthenticationType authenticationType = IpmiSessionAuthenticationType.NONE;
    // private int ipmiSessionSequenceNumber;
    // private int ipmiSessionId;
    private byte[] ipmiMessageAuthenticationCode;   // 16 bytes

    // @Override
    // public int getIpmiSessionId() { return ipmiSessionId; }

    // @Override
    // public int getIpmiSessionSequenceNumber() { return ipmiSessionSequenceNumber; }

    @Override
    public int getWireLength(IpmiSession session, IpmiPayload payload) {
        return 1 // authenticationType
                + 4 // ipmiSessionSequenceNumber
                + 4 // ipmiSessionId
                + (authenticationType != IpmiSessionAuthenticationType.NONE ? 16 : 0)
                + 1 // payloadLength
                + payload.getWireLength();
    }

    /** Sequence number handling: [IPMI2] Section 6.12.8, page 58. */
    @Override
    public void toWire(ByteBuffer buffer, IpmiSession session, IpmiPayload payload) {
        // Page 133
        buffer.put(authenticationType.getCode());
        buffer.putInt(0 /* ipmiSessionSequenceNumber */);
        buffer.putInt(session == null ? 0 : session.getId());
        if (authenticationType != IpmiSessionAuthenticationType.NONE)
            buffer.put(ipmiMessageAuthenticationCode);
        // Page 134
        int payloadLength = payload.getWireLength();
        LOG.info("payload=" + payload);
        LOG.info("payloadLength=" + payloadLength);
        buffer.put(UnsignedBytes.checkedCast(payloadLength));

        payload.toWire(buffer);
    }

    @Override
    public void fromWire(ByteBuffer buffer, IpmiSessionManager sessionManager, IpmiSessionData sessionData) {
        authenticationType = Code.fromBuffer(IpmiSessionAuthenticationType.class, buffer);
        int ipmiSessionSequenceNumber = buffer.getInt();
        int ipmiSessionId = buffer.getInt();
        IpmiSession session = sessionManager.getSession(ipmiSessionId);
        sessionData.setIpmiSession(session);

        if (authenticationType != IpmiSessionAuthenticationType.NONE)
            ipmiMessageAuthenticationCode = AbstractWireable.readBytes(buffer, 16);
        else
            ipmiMessageAuthenticationCode = null;
        byte payloadLength = buffer.get();

        ByteBuffer payloadBuffer = buffer.duplicate();
        payloadBuffer.limit(payloadBuffer.position() + UnsignedBytes.toInt(payloadLength));
        buffer.position(payloadBuffer.limit());

        IpmiPayload payload = newPayload(payloadBuffer, IpmiPayloadType.IPMI);
        payload.fromWire(payloadBuffer);
        sessionData.setIpmiPayload(payload);

        // assert payloadLength == header.getWireLength() + payload.getWireLength();
    }
}