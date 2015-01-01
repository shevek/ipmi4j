/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;

/**
 *
 * @author shevek
 */
public abstract class AbstractPacket extends AbstractWireable implements Packet {

    private SocketAddress remoteAddress;
    private final RmcpHeader header = new RmcpHeader();
    /** Data for a message, null for an ack. */
    private RmcpData data;

    @Override
    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void setRemoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Override
    public RmcpHeader getHeader() {
        return header;
    }

    @Override
    public RmcpData getData() {
        return data;
    }

    @Override
    public Packet withData(@Nonnull RmcpData data) {
        getHeader().withMessageClass(data.getMessageClass());
        this.data = data;
        return this;
    }

    @Override
    protected final void fromWireUnchecked(ByteBuffer buffer) {
        int start = buffer.position();
        fromWireHeader(buffer);

        /*
        switch (header.getMessageClass()) {
            case ASF:
            case IPMI:
            case OEM:
        }
        */

        fromWireBody(buffer, start);
    }
}