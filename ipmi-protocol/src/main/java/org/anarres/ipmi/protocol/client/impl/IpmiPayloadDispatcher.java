/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.impl;

import com.google.common.base.MoreObjects;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import java.net.InetSocketAddress;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nonnegative;
import javax.annotation.Nonnull;
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
public class IpmiPayloadDispatcher implements IpmiClientRmcpMessageHandler, IpmiClientIpmiPayloadHandler {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiPayloadDispatcher.class);

    public static abstract class IpmiReceiver<T> {

        public abstract void receive(@Nonnull IpmiHandlerContext context, @Nonnull T response);

        public abstract void timeout(@Nonnull IpmiReceiverKey<T> key);
    }

    public static class IpmiReceiverKey<T> {

        private final InetSocketAddress remoteAddress;
        // TODO: SessionId?
        private final Class<? extends T> type;
        /** The messageTag or IPMI sequence number. */
        private final byte id;

        public IpmiReceiverKey(@Nonnull InetSocketAddress remoteAddress, @Nonnull Class<? extends T> type, @Nonnegative byte id) {
            this.remoteAddress = remoteAddress;
            this.type = type;
            this.id = id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(remoteAddress, type) ^ id;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (null == obj)
                return false;
            if (!getClass().equals(obj.getClass()))
                return false;
            IpmiReceiverKey o = (IpmiReceiverKey) obj;
            return Objects.equals(remoteAddress, o.remoteAddress)
                    && Objects.equals(type, o.type)
                    && id == o.id;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("remoteAddress", remoteAddress)
                    .add("type", type)
                    .add("id", id)
                    .toString();
        }
    }
    private final RemovalListener<IpmiReceiverKey<?>, IpmiReceiver<?>> removalListener = new RemovalListener<IpmiReceiverKey<?>, IpmiReceiver<?>>() {

        @Override
        public void onRemoval(RemovalNotification<IpmiReceiverKey<?>, IpmiReceiver<?>> notification) {
            IpmiReceiver<?> receiver = notification.getValue();
            if (receiver != null && notification.wasEvicted())
                receiver.timeout((IpmiReceiverKey) notification.getKey());
        }
    };
    private final Cache<IpmiReceiverKey<?>, IpmiReceiver<?>> ipmiReceivers = CacheBuilder.newBuilder()
            .maximumSize(64)
            .expireAfterWrite(60, TimeUnit.SECONDS)
            .removalListener(removalListener)
            .recordStats()
            .build();

    private void handleDiscard(@Nonnull Object message) {
        LOG.warn("Discarded " + message);
    }

    /* First, for RMCP-land. */
    @Override
    public void handleAsfRmcpData(IpmiHandlerContext context, AsfRmcpData message) {
        handleDiscard(message);
    }

    @Override
    public void handleIpmiRmcpData(IpmiHandlerContext context, IpmiSessionWrapper message) {
        message.getIpmiPayload().apply(this, context);
    }

    @Override
    public void handleOemRmcpData(IpmiHandlerContext context, OEMRmcpMessage message) {
        handleDiscard(message);
    }

    /* And now for IPMI-land. */
    private <T extends AbstractTaggedIpmiPayload> void handlePayload(@Nonnull IpmiHandlerContext context, @Nonnull T payload) {
        IpmiReceiverKey<IpmiPayload> key = new IpmiReceiverKey<>(context.getSystemAddress(), payload.getClass().asSubclass(IpmiPayload.class), payload.getMessageTag());
        IpmiReceiver<T> receiver = (IpmiReceiver<T>) ipmiReceivers.getIfPresent(key);
        if (receiver != null) {
            ipmiReceivers.invalidate(key);
            receiver.receive(context, payload);
            return;
        }
        handleDiscard(payload);
    }

    @Override
    public void handleOpenSessionRequest(IpmiHandlerContext context, IpmiOpenSessionRequest message) {
        handlePayload(context, message);
    }

    @Override
    public void handleOpenSessionResponse(IpmiHandlerContext context, IpmiOpenSessionResponse message) {
        handlePayload(context, message);
    }

    @Override
    public void handleRAKPMessage1(IpmiHandlerContext context, IpmiRAKPMessage1 message) {
        handlePayload(context, message);
    }

    @Override
    public void handleRAKPMessage2(IpmiHandlerContext context, IpmiRAKPMessage2 message) {
        handlePayload(context, message);
    }

    @Override
    public void handleRAKPMessage3(IpmiHandlerContext context, IpmiRAKPMessage3 message) {
        handlePayload(context, message);
    }

    @Override
    public void handleRAKPMessage4(IpmiHandlerContext context, IpmiRAKPMessage4 message) {
        handlePayload(context, message);
    }

    /** Section 6.12.8, page 58: Requests and responses are matched on the IPMI Seq field. */
    @Override
    public void handleCommand(IpmiHandlerContext context, IpmiCommand message) {
        if (message instanceof IpmiResponse) {
            IpmiResponse response = (IpmiResponse) message;
            IpmiReceiverKey<IpmiResponse> key = new IpmiReceiverKey<>(context.getSystemAddress(), response.getClass(), message.getSequenceNumber());
            IpmiReceiver<IpmiResponse> receiver = (IpmiReceiver<IpmiResponse>) ipmiReceivers.getIfPresent(key);
            if (receiver != null) {
                ipmiReceivers.invalidate(key);
                receiver.receive(context, response);
                return;
            }
        }
        handleDiscard(message);
    }

    @Override
    public void handleOemExplicit(IpmiHandlerContext context, OemExplicit message) {
        handleDiscard(message);
    }

    @Override
    public void handleSOL(IpmiHandlerContext context, SOLMessage message) {
        handleDiscard(message);
    }
}
