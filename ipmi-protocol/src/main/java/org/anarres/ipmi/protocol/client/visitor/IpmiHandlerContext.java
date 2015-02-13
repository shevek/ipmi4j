/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.visitor;

import java.net.SocketAddress;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.dispatch.AbstractIpmiReceiver;
import org.anarres.ipmi.protocol.client.dispatch.IpmiReceiver;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;

/**
 *
 * @author shevek
 */
public class IpmiHandlerContext {

    public static interface IpmiPacketQueue {

        public void queue(@Nonnull IpmiHandlerContext context, @CheckForNull IpmiSession session, @Nonnull IpmiPayload message,
                @Nonnull Class<? extends IpmiPayload> responseType, @Nonnull IpmiReceiver receiver);
    }

    private final IpmiPacketQueue queue;
    private final SocketAddress systemAddress;
    // private IpmiSession ipmiSession;

    public IpmiHandlerContext(@Nonnull IpmiPacketQueue queue, @Nonnull SocketAddress systemAddress) {
        this.queue = queue;
        this.systemAddress = systemAddress;
    }

    @Nonnull
    public SocketAddress getSystemAddress() {
        return systemAddress;
    }

    public void send(@CheckForNull IpmiSession session, @Nonnull IpmiPayload payload,
            @Nonnull Class<? extends IpmiPayload> responseType, @Nonnull IpmiReceiver receiver) {
        queue.queue(this, session, payload, responseType, receiver);
    }

    public void send(@CheckForNull IpmiSession session, @Nonnull IpmiPayload payload,
            @Nonnull AbstractIpmiReceiver receiver) {
        send(session, payload, receiver.getPayloadType(), receiver);
    }
}
