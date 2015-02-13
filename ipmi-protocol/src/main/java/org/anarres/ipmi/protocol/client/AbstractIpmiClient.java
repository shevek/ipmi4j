/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client;

import com.google.common.annotations.VisibleForTesting;
import java.io.IOException;
import java.net.InetSocketAddress;
import javax.annotation.Nonnull;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.anarres.ipmi.protocol.client.dispatch.IpmiPayloadReceiveDispatcher;
import org.anarres.ipmi.protocol.client.dispatch.IpmiPayloadTransmitQueue;
import org.anarres.ipmi.protocol.client.dispatch.IpmiReceiver;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.session.IpmiSessionManager;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.rmcp.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public abstract class AbstractIpmiClient implements IpmiClient {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractIpmiClient.class);
    private final IpmiSessionManager sessionManager = new IpmiSessionManager();
    private final IpmiPayloadReceiveDispatcher dispatcher = new IpmiPayloadReceiveDispatcher(sessionManager);
    private final IpmiPayloadTransmitQueue queue = new IpmiPayloadTransmitQueue(dispatcher, this);

    @Override
    public IpmiSessionManager getSessionManager() {
        return sessionManager;
    }

    @PostConstruct
    public abstract void start() throws IOException, InterruptedException;

    @Override
    public void queue(IpmiHandlerContext context, IpmiSession session, IpmiPayload message,
            Class<? extends IpmiPayload> responseType, IpmiReceiver receiver) {
        queue.queue(context, session, message, responseType, receiver);
    }

    @VisibleForTesting
    public void receive(@Nonnull Packet packet) throws IOException {
        LOG.info("Receive\n" + packet);
        IpmiHandlerContext context = new IpmiHandlerContext(this, (InetSocketAddress) packet.getRemoteAddress());
        dispatcher.dispatch(context, packet);
    }

    @PreDestroy
    public abstract void stop() throws IOException, InterruptedException;
}
