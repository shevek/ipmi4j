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
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSession;
import org.anarres.ipmi.protocol.packet.ipmi.session.IpmiSessionManager;

/**
 * [IPMI2] Section 13.6 pages 133-134, column 1.
 *
 * @author shevek
 */
public class Ipmi15SessionWrapper implements IpmiSessionWrapper {

    private IpmiSessionAuthenticationType authenticationType = IpmiSessionAuthenticationType.NONE;
    private int ipmiSessionSequenceNumber;
    private int ipmiSessionId;
    private byte[] ipmiMessageAuthenticationCode;   // 16 bytes

    // @Override
    public int getIpmiSessionId() {
        return ipmiSessionId;
    }

    // @Override
    public int getIpmiSessionSequenceNumber() {
        return ipmiSessionSequenceNumber;
    }

    @Override
    public int getWireLength(IpmiSession session, IpmiPayload payload) {
        return 1 // authenticationType
                + 4 // ipmiSessionSequenceNumber
                + 4 // ipmiSessionId
                + (authenticationType != IpmiSessionAuthenticationType.NONE ? 16 : 0)
                + 1 // payloadLength
                + payload.getWireLength();
    }

    @Override
    public void toWire(ByteBuffer buffer, IpmiSession session, IpmiPayload payload) {
        // Page 133
        buffer.put(authenticationType.getCode());
        buffer.putInt(ipmiSessionSequenceNumber);
        buffer.putInt(ipmiSessionId);
        if (authenticationType != IpmiSessionAuthenticationType.NONE)
            buffer.put(ipmiMessageAuthenticationCode);
        // Page 134
        int payloadLength = payload.getWireLength();
        buffer.put(UnsignedBytes.checkedCast(payloadLength));

        payload.toWire(buffer);
    }

    @Override
    public IpmiSession fromWire(ByteBuffer buffer, IpmiSessionManager sessionManager, IpmiPayload payload) {
        authenticationType = Code.fromBuffer(IpmiSessionAuthenticationType.class, buffer);
        ipmiSessionSequenceNumber = buffer.getInt();
        ipmiSessionId = buffer.getInt();
        IpmiSession session = sessionManager.getSession(ipmiSessionId);
        if (authenticationType != IpmiSessionAuthenticationType.NONE)
            ipmiMessageAuthenticationCode = AbstractWireable.readBytes(buffer, 16);
        else
            ipmiMessageAuthenticationCode = null;
        byte payloadLength = buffer.get();

        payload.fromWire(buffer);

        // assert payloadLength == header.getWireLength() + payload.getWireLength();
        return session;
    }
}