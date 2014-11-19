package org.anarres.ipmi.protocol.packet.rmcp;

import org.anarres.ipmi.protocol.packet.common.Wireable;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public interface Packet extends Wireable {

    public void setRemoteAddress(@Nonnull SocketAddress remoteAddress);

    @Nonnull
    public SocketAddress getRemoteAddress();

    @Nonnull
    public RmcpHeader getHeader();

    // @CheckForNull
    public RmcpData getData();

    @Nonnull
    public Packet withData(@Nonnull RmcpData data);

    public void fromWireHeader(@Nonnull ByteBuffer buffer);

    public void fromWireBody(@Nonnull ByteBuffer buffer, int start);
}
