/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.client.session.IpmiContext;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;

/**
 *
 * @author shevek
 */
public class OemExplicit extends AbstractIpmiPayload {

    private int oemEnterpriseNumber;    // 3 byte oem iana; 1 byte zero
    private char oemPayloadId;
    private byte[] data;

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.OEM_EXPLICIT;
    }

    @Override
    public void apply(IpmiClientIpmiPayloadHandler handler, IpmiHandlerContext context, IpmiSession session) {
        handler.handleOemExplicit(context, session, this);
    }

    public int getOemEnterpriseNumber() {
        return oemEnterpriseNumber;
    }

    public void setOemEnterpriseNumber(int oemEnterpriseNumber) {
        this.oemEnterpriseNumber = oemEnterpriseNumber;
    }

    public char getOemPayloadId() {
        return oemPayloadId;
    }

    public void setOemPayloadId(char oemPayloadId) {
        this.oemPayloadId = oemPayloadId;
    }

    @Override
    public int getWireLength(IpmiContext context) {
        return data.length;
    }

    @Override
    protected void toWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        buffer.put(data);
    }

    @Override
    protected void fromWireUnchecked(IpmiContext context, ByteBuffer buffer) {
        data = new byte[buffer.remaining()];
        buffer.get(data);
    }
}
