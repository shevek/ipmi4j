/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import java.nio.ByteBuffer;

/**
 *
 * @author shevek
 */
public abstract class AsfRmcpData implements RmcpData {

    // Page 22
    private int ianaEnterpriseNumber = 0x4542;

    // Page 22
    private enum MessageType {

        Reset(0x10),
        PowerUp(0x11),
        UnconditionalPowerDown(0x12),
        PowerCycleReset(0x13),
        PresencePong(0x40),
        CapabilitiesResponse(0x41),
        SystemStateResponse(0x42),
        OpenSessionResponse(0x43),
        CloseSessionResponse(0x44),
        PresencePing(0x80),
        CapabilitiesRequesT(0x81),
        SystemStateRequest(0x82),
        OpenSessionRequest(0x83),
        CloseSessionRequest(0x84),
        RAKPMessage1(0xC0),
        RAKPMessage2(0xC1),
        RAKPMessage3(0xC2);
        private final int value;

        private MessageType(int value) {
            this.value = value;
        }
    }
    private MessageType messageType;
    // Page 33
    private byte messageTag;    // matches request/response
    // reserved 1 byte
    private byte[] data;

    @Override
    public int getWireLength() {
        return 0
                + 4 // ianaEnterpriseNumber
                + 1 // message type
                + 1 // message tag
                ;
        // + 1 (datalength) + datalength
    }

    @Override
    public void toWire(ByteBuffer buffer) {
        buffer.putInt(ianaEnterpriseNumber);
        buffer.put((byte) messageType.value);
        buffer.put(messageTag);
        buffer.put((byte) 0);
        // 1 byte data length; n bytes data.
    }
}
