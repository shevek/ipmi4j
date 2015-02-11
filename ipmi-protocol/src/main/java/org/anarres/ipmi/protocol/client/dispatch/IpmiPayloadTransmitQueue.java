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
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.Ipmi15SessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionWrapper;
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
import org.anarres.ipmi.protocol.packet.rmcp.RmcpPacket;

/**
 *
 * @author shevek
 */
public class IpmiPayloadTransmitQueue {

    public static interface IpmiPacketSender {

        public void send(@Nonnull Packet packet);
    }

    // -> Queue<QueueItem>
    private static class Queue extends LinkedBlockingQueue<IpmiPayload> {

        /** @see AbstractTaggedIpmiPayload#getMessageTag() */
        private int nextMessageTag;
        /** @see IpmiCommand#getSequenceNumber() */
        private int nextSequenceNumber;
        private int outstandingRequests;
    }

    private static class QueueItem {

        private final IpmiSession session;
        private final IpmiPayload payload;
        private final IpmiReceiverKey key;
        private final IpmiReceiver receiver;

        public QueueItem(@CheckForNull IpmiSession session, @Nonnull IpmiPayload payload,
                @Nonnull IpmiReceiverKey key, @Nonnull IpmiReceiver receiver) {
            this.session = session;
            this.payload = payload;
            this.key = key;
            this.receiver = receiver;
        }
    }
    private final LoadingCache<InetSocketAddress, Queue> ipmiQueues = CacheBuilder.newBuilder()
            // .weakKeys() // Discard queues for closed connections?
            .recordStats()
            .build(new CacheLoader<InetSocketAddress, Queue>() {

                @Override
                public Queue load(InetSocketAddress key) throws Exception {
                    return new Queue();
                }
            });
    private final IpmiClientIpmiPayloadHandler ipmiPayloadSequencer = new IpmiClientIpmiPayloadHandler() {

        private void handleTagged(@Nonnull IpmiHandlerContext context, @Nonnull AbstractTaggedIpmiPayload message) {
            Queue queue = getState(context);
            synchronized (queue) {
                message.setMessageTag((byte) queue.nextMessageTag++);
                queue.add(message);
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
            Queue queue = getState(context);
            synchronized (queue) {
                byte sequenceNumber = (byte) queue.nextSequenceNumber++;
                message.setSequenceNumber(sequenceNumber);
                // receiver.setReceiver(context, null, sequenceNumber, null);
                queue.add(message);
            }
        }

        @Override
        public void handleSOL(IpmiHandlerContext context, IpmiSession session, SOLMessage message) {
            Queue queue = getState(context);
            queue.add(message);
        }

        @Override
        public void handleOemExplicit(IpmiHandlerContext context, IpmiSession session, OemExplicit message) {
            Queue queue = getState(context);
            queue.add(message);
        }
    };
    private final IpmiReceiverRepository receiver;
    private final IpmiPacketSender sender;

    public IpmiPayloadTransmitQueue(@Nonnull IpmiReceiverRepository receiver, @Nonnull IpmiPacketSender sender) {
        this.receiver = receiver;
        this.sender = sender;
    }

    @Nonnull
    private Queue getState(@Nonnull IpmiHandlerContext context) {
        return ipmiQueues.getUnchecked(context.getSystemAddress());
    }

    public void send(@Nonnull IpmiHandlerContext context, @Nonnull IpmiSession session, @Nonnull IpmiPayload message) {
        message.apply(ipmiPayloadSequencer, context, session);
    }

    private void doSend(@Nonnull InetSocketAddress systemAddress, @Nonnull QueueItem item) {
        @CheckForNull
        IpmiSession session = item.session;
        @Nonnull
        IpmiPayload payload = item.payload;

        IpmiSessionWrapper wrapper = new Ipmi15SessionWrapper();
        wrapper.setIpmiPayload(payload);
        if (session != null) {
            if (wrapper.isEncrypted())
                wrapper.setIpmiSessionSequenceNumber(session.nextEncryptedSequenceNumber());
            else
                wrapper.setIpmiSessionSequenceNumber(session.nextUnencryptedSequenceNumber());
        } else {
            wrapper.setIpmiSessionSequenceNumber(0);
        }

        RmcpPacket packet = new RmcpPacket();
        packet.withRemoteAddress(systemAddress);
        packet.withData(wrapper);

        receiver.setReceiver(item.key, item.receiver);
        sender.send(packet);
    }
}
