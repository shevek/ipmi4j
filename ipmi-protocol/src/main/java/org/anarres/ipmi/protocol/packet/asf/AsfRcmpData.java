/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import com.google.common.primitives.UnsignedBytes;
import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpData;

/**
 * ASF RMCP Data (Enterprise number 0x4542).
 *
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.2.3 page 22.
 *
 * @author shevek
 */
public abstract class AsfRcmpData implements RmcpData {

    public static final int IANA_ENTERPRISE_NUMBER = 0x4542;
    // Page 22
    private AsfRcmpMessageType messageType;
    // Page 33
    private byte messageTag;    // matches request/response
    private AsfData data;

    @Override
    public int getWireLength() {
        return 0
                + 4 // ianaEnterpriseNumber
                + 1 // message type
                + 1 // message tag
                + 1 // data length
                + data.getWireLength();
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        buffer.putInt(IANA_ENTERPRISE_NUMBER);
        buffer.put((byte) messageType.value);
        buffer.put(messageTag);
        buffer.put((byte) 0);   // reserved
        buffer.put(UnsignedBytes.checkedCast(data.getWireLength()));
        data.toWire(buffer);
    }
}
