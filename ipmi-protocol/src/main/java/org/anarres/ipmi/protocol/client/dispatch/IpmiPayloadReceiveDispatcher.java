/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.dispatch;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiPacketContext;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientRmcpMessageHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.AbstractTaggedIpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.rmcp.Packet;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class IpmiPayloadReceiveDispatcher implements IpmiReceiverRepository {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiPayloadReceiveDispatcher.class);

    private final IpmiPacketContext sessionManager;
    private final RemovalListener<IpmiReceiverKey, IpmiReceiver> removalListener = new RemovalListener<IpmiReceiverKey, IpmiReceiver>() {

        @Override
        public void onRemoval(RemovalNotification<IpmiReceiverKey, IpmiReceiver> notification) {
            IpmiReceiverKey key = notification.getKey();
            IpmiReceiver receiver = notification.getValue();
            if (key != null && receiver != null && notification.wasEvicted())
                receiver.timeout(key);
        }
    };
    /** [IPMI2] Section 6.12.15, page 60: Session inactivity timeout is 60 seconds +/-3 seconds. */
    private final Cache<IpmiReceiverKey, IpmiReceiver> ipmiReceivers = CacheBuilder.newBuilder()
            // .maximumSize(64)
            .expireAfterWrite(180, TimeUnit.SECONDS)
            .removalListener(removalListener)
            .recordStats()
            .build();
    /* First, for RMCP-land. */
    private final IpmiClientRmcpMessageHandler rmcpHandler = new IpmiClientRmcpMessageHandler.Adapter() {

        @Override
        public void handleDefault(IpmiHandlerContext context, RmcpData message) {
            handleDiscard(context, message);
        }

        @Override
        public void handleIpmiRmcpData(IpmiHandlerContext context, IpmiSessionWrapper message) {
            // Pass directly to IpmiPayload to avoid type ambiguity on 'this'.
            int sessionId = message.getIpmiSessionId();
            IpmiSession session = (sessionId == 0) ? null : sessionManager.getIpmiSession(sessionId);
            message.getIpmiPayload().apply(ipmiPayloadHandler, context, session);
        }
    };
    /* And now for IPMI-land. */
    private IpmiClientIpmiPayloadHandler ipmiPayloadHandler = new IpmiClientIpmiPayloadHandler.TaggedAdapter() {

        @Override
        protected void handleDefault(IpmiHandlerContext context, IpmiSession session, IpmiPayload payload) {
            handleDiscard(context, payload);
        }

        @Override
        protected void handleTagged(@Nonnull IpmiHandlerContext context, @Nonnull IpmiSession session, @Nonnull AbstractTaggedIpmiPayload payload) {
            IpmiReceiver receiver = (IpmiReceiver) getReceiver(context, payload.getClass(), payload.getMessageTag());
            if (receiver != null) {
                receiver.receive(context, session, payload);
                return;
            }
            handleDiscard(context, payload);
        }

        /** Section 6.12.8, page 58: Requests and responses are matched on the IPMI Seq field. */
        @Override
        public void handleCommand(IpmiHandlerContext context, IpmiSession session, IpmiCommand message) {
            if (message instanceof IpmiResponse) {
                IpmiResponse response = (IpmiResponse) message;
                IpmiReceiver receiver = getReceiver(context, response.getClass(), message.getSequenceNumber());
                if (receiver != null) {
                    receiver.receive(context, session, response);
                    return;
                }
            }
            handleDiscard(context, message);
        }
    };

    public IpmiPayloadReceiveDispatcher(@Nonnull IpmiPacketContext sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public IpmiReceiver getReceiver(@Nonnull IpmiHandlerContext context, Class<? extends IpmiPayload> payloadType, byte messageId) {
        IpmiReceiverKey key = new IpmiReceiverKey(context.getSystemAddress(), payloadType, messageId);
        IpmiReceiver receiver = ipmiReceivers.asMap().remove(key);
        if (LOG.isDebugEnabled())
            LOG.debug("Get: " + key + " -> " + receiver);
        return receiver;
    }

    @Override
    public void setReceiver(@Nonnull IpmiReceiverKey key, @Nonnull IpmiReceiver receiver) {
        if (LOG.isDebugEnabled())
            LOG.debug("Set: " + key + " -> " + receiver);
        ipmiReceivers.put(key, receiver);
    }

    @VisibleForTesting
    protected void handleDiscard(@Nonnull IpmiHandlerContext context, @Nonnull Object message) {
        Preconditions.checkNotNull(message, "Message was null.");
        LOG.warn("Discarded:\n" + message);
    }

    public void dispatch(@Nonnull IpmiHandlerContext context, @Nonnull Packet packet) {
        packet.apply(rmcpHandler, context);
    }
}
