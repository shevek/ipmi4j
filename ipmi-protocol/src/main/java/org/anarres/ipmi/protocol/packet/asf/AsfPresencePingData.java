/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import java.nio.ByteBuffer;
import org.anarres.ipmi.protocol.client.IpmiClientAsfMessageHandler;

/**
 * PresencePing.
 * 
 * [ASF2] Section 3.2.4.8 page 40.
 * [IPMI2] Section 13.2.3 page 128.
 *
 * @author shevek
 */
public class AsfPresencePingData extends AbstractAsfData {

    @Override
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.PresencePing;
    }

    @Override
    public void apply(IpmiClientAsfMessageHandler handler) {
        handler.handleAsfPresencePingData(this);
    }

    @Override
    protected int getDataWireLength() {
        return 0;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
    }
}