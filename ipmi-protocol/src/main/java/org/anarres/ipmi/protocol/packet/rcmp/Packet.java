package org.anarres.ipmi.protocol.packet.rcmp;

import java.net.SocketAddress;
import java.nio.ByteBuffer;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;

/**
 *
 * @author shevek
 */
public interface Packet {

    @Nonnegative
    public int getWireLength();

    public void toWire(@Nonnull ByteBuffer buffer);

    public void fromWire(@Nonnull ByteBuffer buffer);

    public void setRemoteAddress(@Nonnull SocketAddress remoteAddress);

    @Nonnull
    public SocketAddress getRemoteAddress();

}
