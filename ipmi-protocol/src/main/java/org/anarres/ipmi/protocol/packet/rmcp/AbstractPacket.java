/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.packet.rmcp;

import java.net.SocketAddress;
import org.anarres.ipmi.protocol.packet.common.AbstractWireable;

/**
 *
 * @author shevek
 */
public abstract class AbstractPacket extends AbstractWireable implements Packet {

    private SocketAddress remoteAddress;

    @Override
    public SocketAddress getRemoteAddress() {
        return remoteAddress;
    }

    @Override
    public void setRemoteAddress(SocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }
}