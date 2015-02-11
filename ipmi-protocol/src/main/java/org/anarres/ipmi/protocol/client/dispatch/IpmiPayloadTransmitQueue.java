/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.dispatch;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.net.InetSocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiCommand;
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
import org.anarres.ipmi.protocol.packet.rmcp.Packet;

/**
 *
 * @author shevek
 */
public class IpmiPayloadTransmitQueue {

    public static interface Sender {

        public void send(@Nonnull Packet packet);
    }

    private static class State extends LinkedBlockingQueue<Object> {

        /** @see AbstractTaggedIpmiPayload#getMessageTag() */
        private int nextMessageTag;
        /** @see IpmiCommand#getSequenceNumber() */
        private int nextSequenceNumber;
        private int outstandingRequests;
    }
    private final LoadingCache<InetSocketAddress, State> ipmiQueues = CacheBuilder.newBuilder()
            // .weakKeys() // Discard queues for closed connections?
            .recordStats()
            .build(new CacheLoader<InetSocketAddress, State>() {

                @Override
                public State load(InetSocketAddress key) throws Exception {
                    return new State();
                }
            });
    private final IpmiClientIpmiPayloadHandler ipmiSequencer = new IpmiClientIpmiPayloadHandler() {

        private void handleTagged(@Nonnull IpmiHandlerContext context, @Nonnull AbstractTaggedIpmiPayload message) {
            State state = getState(context);
            synchronized (state) {
                message.setMessageTag((byte) state.nextMessageTag++);
                state.add(message);
            }
        }

        @Override
        public void handleOpenSessionRequest(IpmiHandlerContext context, IpmiSession session, IpmiOpenSessionRequest message) {
            handleTagged(context, message);
        }

        @Override
        public void handleOpenSessionResponse(IpmiHandlerContext context, IpmiSession session, IpmiOpenSessionResponse message) {
            handleTagged(context, message);
        }

        @Override
        public void handleRAKPMessage1(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage1 message) {
            handleTagged(context, message);
        }

        @Override
        public void handleRAKPMessage2(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage2 message) {
            handleTagged(context, message);
        }

        @Override
        public void handleRAKPMessage3(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage3 message) {
            handleTagged(context, message);
        }

        @Override
        public void handleRAKPMessage4(IpmiHandlerContext context, IpmiSession session, IpmiRAKPMessage4 message) {
            handleTagged(context, message);
        }

        @Override
        public void handleCommand(IpmiHandlerContext context, IpmiSession session, IpmiCommand message) {
            State state = getState(context);
            synchronized (state) {
                message.setSequenceNumber((byte) state.nextSequenceNumber++);
                state.add(message);
            }
        }

        @Override
        public void handleSOL(IpmiHandlerContext context, IpmiSession session, SOLMessage message) {
            State state = getState(context);
            state.add(message);
        }

        @Override
        public void handleOemExplicit(IpmiHandlerContext context, IpmiSession session, OemExplicit message) {
            State state = getState(context);
            state.add(message);
        }
    };
    private final Sender sender;

    public IpmiPayloadTransmitQueue(@Nonnull Sender sender) {
        this.sender = sender;
    }

    @Nonnull
    private State getState(@Nonnull IpmiHandlerContext context) {
        return ipmiQueues.getUnchecked(context.getSystemAddress());
    }

    public void send(@Nonnull IpmiHandlerContext context, @Nonnull IpmiSession session, @Nonnull IpmiPayload message) {
        message.apply(ipmiSequencer, context, session);
    }
}
