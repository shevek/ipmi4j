/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.IanaEnterpriseNumber;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;

/**
 * [IPMI2] Section 13.6 page 133.
 *
 * @author shevek
 */
public class Ipmi20SessionHeader extends AbstractWireable {

    private IpmiHeaderAuthenticationType authenticationType;
    private IpmiPayloadType payloadType;
    private boolean encrypted;
    private boolean authenticated;
    private IanaEnterpriseNumber oemEnterpriseNumber;    // 3 byte oem iana; 1 byte zero
    private char oemPayloadId;
    private int ipmiSessionId;
    private int ipmiSessionSequenceNumber;

    @Override
    public int getWireLength() {
        return 1 // authenticationType
                + 1 // payloadType
                // TODO if payloadType == IpmiPayloadType.OEM_EXPLICIT
                + 4 // oemEnterpriseNumber
                // TODO if payloadType == IpmiPayloadType.OEM_EXPLICIT
                + 2 // oemPayloadId
                + 4 // ipmiSessionId
                + 4 // ipmiSessionSequenceNumber
                + 2 // payloadLength
                ;
    }

    // 2 byte payload length
    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        // Page 133
        buffer.put(authenticationType.getCode());
        byte payloadType = this.payloadType.getCode();
        if (encrypted)
            payloadType |= 0x80;
        if (authenticated)
            payloadType |= 0x40;
        buffer.put(payloadType);
        buffer.putInt(oemEnterpriseNumber.getNumber() << Byte.SIZE);
        buffer.putChar(oemPayloadId);
        buffer.putInt(ipmiSessionId);
        buffer.putInt(ipmiSessionSequenceNumber);
        // Page 134
        // 2 byte payload length
    }

    @Override
    protected void fromWireUnchecked(ByteBuffer buffer) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
