package org.anarres.ipmi.protocol.packet.rmcp;

import java.net.SocketAddress;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public interface Packet extends Wireable {

    public void setRemoteAddress(@Nonnull SocketAddress remoteAddress);

    @Nonnull
    public SocketAddress getRemoteAddress();
}
