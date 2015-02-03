package org.anarres.ipmi.protocol.packet.rmcp;

import java.net.SocketAddress;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientRmcpMessageHandler;
import org.anarres.ipmi.protocol.packet.common.Wireable;

/**
 *
 * @author shevek
 */
public interface Packet extends Wireable {

    /**
     * Returns the remote address from which this packet was received, or to which it will be sent.
     */
    @Nonnull
    public SocketAddress getRemoteAddress();

    /**
     * Returns the RMCP payload of this packet.
     */
    // @CheckForNull
    public RmcpData getData();

    public <T extends RmcpData> T getData(@Nonnull Class<T> type);

    /**
     * Sets the RMCP payload of this packet.
     */
    @Nonnull
    public Packet withData(@Nonnull RmcpData data);

    public void apply(@Nonnull IpmiClientRmcpMessageHandler handler);
}