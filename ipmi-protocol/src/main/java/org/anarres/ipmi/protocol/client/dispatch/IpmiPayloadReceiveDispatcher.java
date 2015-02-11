/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.dispatch;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiContext;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.session.IpmiSessionManager;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientRmcpMessageHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.asf.AsfRmcpData;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.AbstractTaggedIpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionRequest;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage1;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage2;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage3;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage4;
import org.anarres.ipmi.protocol.packet.ipmi.payload.OemExplicit;
import org.anarres.ipmi.protocol.packet.ipmi.payload.SOLMessage;
import org.anarres.ipmi.protocol.packet.rmcp.OEMRmcpMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class IpmiPayloadReceiveDispatcher
        implements IpmiClientRmcpMessageHandler, IpmiClientIpmiPayloadHandler, IpmiReceiverRepository {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiPayloadReceiveDispatcher.class);

    private final IpmiContext sessionManager;
    private final RemovalListener<IpmiReceiverKey, IpmiReceiver> removalListener = new RemovalListener<IpmiReceiverKey, IpmiReceiver>() {

        @Override
        public void onRemoval(RemovalNotification<IpmiReceiverKey, IpmiReceiver> notification) {
            IpmiReceiverKey key = notification.getKey();
            IpmiReceiver receiver = notification.getValue();
            if (key != null && receiver != null && notification.wasEvicted())
                receiver.timeout(key);
        }
    };
    private final Cache<IpmiReceiverKey, IpmiReceiver> ipmiReceivers = CacheBuilder.newBuilder()
            // .maximumSize(64)
            .expireAfterWrite(180, TimeUnit.SECONDS)
            .removalListener(removalListener)
            .recordStats()
            .build();

    public IpmiPayloadReceiveDispatcher(@Nonnull IpmiSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public IpmiReceiver getReceiver(@Nonnull IpmiHandlerContext context, Class<? extends IpmiPayload> payloadType, byte messageId) {
        IpmiReceiverKey key = new IpmiReceiverKey(context.getSystemAddress(), payloadType, messageId);
        return ipmiReceivers.asMap().remove(key);
    }

    @Override
    public void setReceiver(@Nonnull IpmiReceiverKey key, @Nonnull IpmiReceiver receiver) {
        ipmiReceivers.put(key, receiver);
    }

    private void handleDiscard(@Nonnull IpmiHandlerContext context, @Nonnull Object message) {
        LOG.warn("Discarded " + message);
    }

    /* First, for RMCP-land. */
    @Override
    public void handleAsfRmcpData(IpmiHandlerContext context, AsfRmcpData message) {
        handleDiscard(context, message);
    }

    @Override
    public void handleIpmiRmcpData(IpmiHandlerContext context, IpmiSessionWrapper message) {
        // Pass directly to IpmiPayload to avoid type ambiguity on 'this'.
        int sessionId = message.getIpmiSessionId();
        IpmiSession session = (sessionId == 0) ? null : sessionManager.getIpmiSession(sessionId);
        message.getIpmiPayload().apply(this, context, session);
    }

    @Override
    public void handleOemRmcpData(IpmiHandlerContext context, OEMRmcpMessage message) {
        handleDiscard(context, message);
    }

    /* And now for IPMI-land. */
    private <T extends AbstractTaggedIpmiPayload> void handlePayload(@Nonnull IpmiHandlerContext context, @Nonnull IpmiSession session, @Nonnull T payload) {
        IpmiReceiver receiver = (IpmiReceiver) getReceiver(context, payload.getClass(), payload.getMessageTag());
        if (receiver != null) {
            receiver.receive(context, session, payload);
            return;
        }
        handleDiscard(context, payload);
    }

    @Override
    public void handleOpenSessionRequest(IpmiHandlerContext context, IpmiSession session, IpmiOpenSessionRequest message) {
        handlePayload(context, session, message);
    }

    @Override
    public void handleOpenSessionResponse(IpmiHandlerContext context, IpmiSession session, IpmiOpenSessionResponse message) {
        handlePayload(context, session, message);
    }

    @Override
    public void handleRAKPMessage1(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage1 message) {
        handlePayload(context, session, message);
    }

    @Override
    public void handleRAKPMessage2(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage2 message) {
        handlePayload(context, session, message);
    }

    @Override
    public void handleRAKPMessage3(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage3 message) {
        handlePayload(context, session, message);
    }

    @Override
    public void handleRAKPMessage4(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage4 message) {
        handlePayload(context, session, message);
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
        handleDiscard(context, session);
    }

    @Override
    public void handleSOL(IpmiHandlerContext context, IpmiSession session, SOLMessage message) {
        handleDiscard(context, session);
    }

    @Override
    public void handleOemExplicit(IpmiHandlerContext context, IpmiSession session, OemExplicit message) {
        handleDiscard(context, session);
    }
}
