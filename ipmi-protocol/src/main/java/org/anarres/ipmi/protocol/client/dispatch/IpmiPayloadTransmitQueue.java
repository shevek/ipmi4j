/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.dispatch;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.net.SocketAddress;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.Ipmi15SessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionWrapper;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.payload.AbstractTaggedIpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.rmcp.Packet;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpPacket;

/**
 *
 * @author shevek
 */
public class IpmiPayloadTransmitQueue implements IpmiHandlerContext.IpmiPacketQueue {

    public static interface IpmiPacketSender {

        public void send(@Nonnull Packet packet);
    }

    // -> Queue<QueueItem>
    private static class Queue extends LinkedBlockingQueue<IpmiPayload> {

        // private final BitSet messageTags = new BitSet(256);
        /** @see AbstractTaggedIpmiPayload#getMessageTag() */
        private int nextMessageTag;
        // private final BitSet sequenceNumbers = new BitSet(IpmiCommand.SEQUENCE_NUMBER_MASK + 1);
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

    private static class QueueFactory extends CacheLoader<SocketAddress, Queue> {

        @Override
        public Queue load(SocketAddress key) throws Exception {
            return new Queue();
        }
    };
    private final LoadingCache<SocketAddress, Queue> ipmiQueues = CacheBuilder.newBuilder()
            // .weakKeys() // Discard queues for closed connections?
            .expireAfterAccess(1, TimeUnit.HOURS)
            .recordStats()
            .build(new QueueFactory());
    private final IpmiClientIpmiPayloadHandler ipmiPayloadSequencer = new IpmiClientIpmiPayloadHandler.TaggedAdapter() {

        @Override
        protected void handleDefault(IpmiHandlerContext context, IpmiSession session, IpmiPayload payload) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void handleTagged(IpmiHandlerContext context, IpmiSession session, AbstractTaggedIpmiPayload message) {
            Queue queue = getState(context);
            synchronized (queue) {
                message.setMessageTag((byte) queue.nextMessageTag++);
                queue.add(message);
            }
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

    @Override
    public void queue(IpmiHandlerContext context, IpmiSession session, IpmiPayload message,
            Class<? extends IpmiPayload> responseType, IpmiReceiver receiver) {
        message.apply(ipmiPayloadSequencer, context, session);
    }

    private void doSend(@Nonnull SocketAddress systemAddress, @Nonnull QueueItem item) {
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
