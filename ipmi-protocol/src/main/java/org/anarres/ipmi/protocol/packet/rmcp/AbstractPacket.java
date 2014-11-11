/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import java.net.SocketAddress;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public abstract class AbstractPacket implements Packet {

    private SocketAddress remoteAddress;

    @Override
    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void setRemoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    @Nonnull
    public Packet toPacket() {
        return this;
    }
}
