/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.asf;

import com.google.common.base.Preconditions;
import java.nio.ByteBuffer;
import java.util.UUID;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.IpmiClientAsfMessageHandler;
import org.anarres.ipmi.protocol.packet.common.Code;

/**
 * RAKPMessage2.
 * 
 * http://www.dmtf.org/sites/default/files/standards/documents/DSP0136.pdf
 * http://www.dmtf.org/standards/asf
 * Section 3.2.4.14 page 42.
 *
 * @author shevek
 */
public class AsfRAKPMessage2Data extends AbstractAsfData {

    private AsfRsspSessionStatus status = AsfRsspSessionStatus.NO_ERROR;
    private int consoleSessionId;
    private byte[] clientRandom;      // length = 16
    private UUID clientGuid;

    @Nonnull
    public AsfRsspSessionStatus getStatus() {
        return status;
    }

    @Nonnull
    public AsfRAKPMessage2Data withStatus(@Nonnull AsfRsspSessionStatus status) {
        this.status = status;
        return this;
    }

    public int getConsoleSessionId() {
        return consoleSessionId;
    }

    @Nonnull
    public AsfRAKPMessage2Data withConsoleSessionId(int consoleSessionId) {
        this.consoleSessionId = consoleSessionId;
        return this;
    }

    @Nonnull
    public byte[] getClientRandom() {
        return clientRandom;
    }

    @Nonnull
    public AsfRAKPMessage2Data withClientRandom(@Nonnull byte[] clientRandom) {
        Preconditions.checkNotNull(clientRandom, "Random must be non-null.");
        Preconditions.checkArgument(clientRandom.length == 16, "Random length must be 16.");
        this.clientRandom = clientRandom;
        return this;
    }

    public UUID getClientGuid() {
        return clientGuid;
    }

    @Nonnull
    public AsfRAKPMessage2Data withClientGuid(UUID clientGuid) {
        this.clientGuid = clientGuid;
        return this;
    }

    @Override
    public AsfRmcpMessageType getMessageType() {
        return AsfRmcpMessageType.RAKPMessage2;
    }

    @Override
    public void apply(IpmiClientAsfMessageHandler handler) {
        handler.handleAsfRAKPMessage2Data(this);
    }

    @Override
    protected int getDataWireLength() {
        if (!AsfRsspSessionStatus.NO_ERROR.equals(getStatus()))
            return 8;
        byte[] integrityData = null;
        return 40 + integrityData.length;
    }

    @Override
    protected void toWireData(ByteBuffer buffer) {
        AsfRsspSessionStatus s = getStatus();
        buffer.put(s.getCode());
        buffer.put(new byte[3]);  // Reserved
        buffer.putInt(getConsoleSessionId());
        if (!AsfRsspSessionStatus.NO_ERROR.equals(s))
            return;
        buffer.put(getClientRandom());
        // 16 byte GUID
        UUID g = getClientGuid();
        buffer.putLong(g.getMostSignificantBits());
        buffer.putLong(g.getLeastSignificantBits());
        byte[] integrityData = null;
        buffer.put(integrityData);
    }

    @Override
    protected void fromWireData(ByteBuffer buffer) {
        AsfRsspSessionStatus s = Code.fromBuffer(AsfRsspSessionStatus.class, buffer);
        withStatus(s);
        assertWireBytesZero(buffer, 3);
        withConsoleSessionId(buffer.getInt());
        if (!AsfRsspSessionStatus.NO_ERROR.equals(s))
            return;
        withClientRandom(readBytes(buffer, 16));
        // This is sequenced in Java, right?
        withClientGuid(new UUID(buffer.getLong(), buffer.getLong()));
        byte[] integrityData = null;
        buffer.get(integrityData);
    }
}
