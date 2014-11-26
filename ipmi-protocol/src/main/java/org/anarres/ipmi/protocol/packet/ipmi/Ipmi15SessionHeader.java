/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * [IPMI2] Section 13.6 page 133.
 *
 * @author shevek
 */
public class Ipmi15SessionHeader extends AbstractWireable {

    private IpmiHeaderAuthenticationType authenticationType;
    private int ipmiSessionSequenceNumber;
    private int ipmiSessionId;
    private byte[] ipmiMessageAuthenticationCode;   // 16 bytes

    @Override
    public int getWireLength() {
        return 1 + 4 + 4 + 16;
    }

    // 2 byte payload length
    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        // Page 133
        buffer.put(authenticationType.getCode());
        buffer.putInt(ipmiSessionSequenceNumber);
        buffer.putInt(ipmiSessionId);
        buffer.put(ipmiMessageAuthenticationCode);
        // Page 134
        // 1 byte payload length
    }

    @Override
    protected void fromWireUnchecked(ByteBuffer buffer) {
        authenticationType = Code.fromBuffer(IpmiHeaderAuthenticationType.class, buffer);
        ipmiSessionSequenceNumber = buffer.getInt();
        ipmiSessionId = buffer.getInt();
        ipmiMessageAuthenticationCode = readBytes(buffer, 16);
    }
}
