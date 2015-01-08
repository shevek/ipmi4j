/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;

/**
 *
 * @author shevek
 */
public class OemExplicitPayload extends AbstractIpmiPayload {

    private int oemEnterpriseNumber;    // 3 byte oem iana; 1 byte zero
    private char oemPayloadId;
    private byte[] data;

    @Override
    public IpmiPayloadType getPayloadType() {
        return IpmiPayloadType.OEM_EXPLICIT;
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
    public int getWireLength() {
        return data.length;
    }

    @Override
    protected void toWireUnchecked(ByteBuffer buffer) {
        buffer.put(data);
    }

    @Override
    protected void fromWireUnchecked(ByteBuffer buffer) {
        data = new byte[buffer.remaining()];
        buffer.get(data);
    }
}
