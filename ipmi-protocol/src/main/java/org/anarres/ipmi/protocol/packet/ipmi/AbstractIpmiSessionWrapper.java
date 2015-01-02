/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.ipmi;

import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionRequest;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayloadType;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage1;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage2;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage3;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage4;

/**
 *
 * @author shevek
 */
public abstract class AbstractIpmiSessionWrapper implements IpmiSessionWrapper {

    @Nonnull
    protected IpmiPayload newPayload(@Nonnull ByteBuffer buffer, @Nonnull IpmiPayloadType payloadType) {
        switch (payloadType) {
            case RMCPOpenSessionRequest:
                return new IpmiOpenSessionRequest();
            case RMCPOpenSessionResponse:
                return new IpmiOpenSessionResponse();
            case RAKPMessage1:
                return new IpmiRAKPMessage1();
            case RAKPMessage2:
                return new IpmiRAKPMessage2();
            case RAKPMessage3:
                return new IpmiRAKPMessage3();
            case RAKPMessage4:
                return new IpmiRAKPMessage4();
            default:
                throw new UnsupportedOperationException("Unsupported payload type " + payloadType);
        }
    }
}
