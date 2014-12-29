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

    private IpmiHeaderAuthenticationType authenticationType = IpmiHeaderAuthenticationType.NONE;
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
    public int getWireLength(IpmiSession session, IpmiHeader header, IpmiPayload payload) {
        return 1 // authenticationType
                + 4 // ipmiSessionSequenceNumber
                + 4 // ipmiSessionId
                + (authenticationType != IpmiHeaderAuthenticationType.NONE ? 16 : 0)
                + 1 // payloadLength
                + header.getWireLength()
                + payload.getWireLength();
    }

    @Override
    public void toWire(ByteBuffer buffer, IpmiSession session, IpmiHeader header, IpmiPayload payload) {
        // Page 133
        buffer.put(authenticationType.getCode());
        buffer.putInt(ipmiSessionSequenceNumber);
        buffer.putInt(ipmiSessionId);
        if (authenticationType != IpmiHeaderAuthenticationType.NONE)
            buffer.put(ipmiMessageAuthenticationCode);
        // Page 134
        int payloadLength = header.getWireLength() + payload.getWireLength();
        buffer.put(UnsignedBytes.checkedCast(payloadLength));

        header.toWire(buffer);
        payload.toWire(buffer);
    }

    @Override
    public IpmiSession fromWire(ByteBuffer buffer, IpmiSessionManager sessionManager, IpmiHeader header, IpmiPayload payload) {
        authenticationType = Code.fromBuffer(IpmiHeaderAuthenticationType.class, buffer);
        ipmiSessionSequenceNumber = buffer.getInt();
        ipmiSessionId = buffer.getInt();
        if (authenticationType != IpmiHeaderAuthenticationType.NONE)
            ipmiMessageAuthenticationCode = AbstractWireable.readBytes(buffer, 16);
        else
            ipmiMessageAuthenticationCode = null;
        byte payloadLength = buffer.get();

        header.fromWire(buffer);
        payload.fromWire(buffer);

        // assert payloadLength == header.getWireLength() + payload.getWireLength();
        return null;
    }
}