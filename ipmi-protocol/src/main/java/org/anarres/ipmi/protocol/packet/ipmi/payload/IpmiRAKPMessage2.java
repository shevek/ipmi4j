/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi.payload;

import java.nio.ByteBuffer;
import java.util.UUID;
import org.anarres.ipmi.protocol.packet.asf.AsfRsspSessionStatus;

/**
 * [IPMI2] Section 13.21 page 151.
 *
 * @author shevek
 */
public class IpmiRAKPMessage2 extends TempIpmiWireable {

    private byte messageTag;
    private AsfRsspSessionStatus statusCode;
    private int consoleSessionId;
    private byte[] systemRandom;   // length = 16
    private UUID systemGuid;
    private byte[] keyExchangeAuthenticationCode;

    @Override
    protected void toWireData(ByteBuffer buffer) {
        buffer.put(messageTag);
        buffer.put(statusCode.getCode());
        buffer.putChar((char)0);    // reserved
        buffer.putInt(consoleSessionId);
        buffer.put(systemRandom);
        buffer.putLong(systemGuid.getMostSignificantBits());
        buffer.putLong(systemGuid.getLeastSignificantBits());
        buffer.put(keyExchangeAuthenticationCode);
    }
}
