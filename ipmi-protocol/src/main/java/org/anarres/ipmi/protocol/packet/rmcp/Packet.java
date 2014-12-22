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

    /**
     * Returns the remote address from which this packet was received, or to which it will be sent.
     */
    @Nonnull
    public SocketAddress getRemoteAddress();

    /**
     * Returns the RMCP header of this packet.
     */
    @Nonnull
    public RmcpHeader getHeader();

    /**
     * Returns the RMCP payload of this packet.
     */
    // @CheckForNull
    public RmcpData getData();

    /**
     * Sets the RMCP payload of this packet.
     */
    @Nonnull
    public Packet withData(@Nonnull RmcpData data);

    /**
     * Reads bytes from the wire into the header of this packet.
     * @see #fromWire(java.nio.ByteBuffer)
     */
    public void fromWireHeader(@Nonnull ByteBuffer buffer);

    /**
     * Reads bytes from the wire into the body of this packet.
     * @see #fromWire(java.nio.ByteBuffer)
     */
    public void fromWireBody(@Nonnull ByteBuffer buffer, int start);
}
