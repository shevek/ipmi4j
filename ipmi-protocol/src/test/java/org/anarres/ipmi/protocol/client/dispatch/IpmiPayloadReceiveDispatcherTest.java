/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.dispatch;

import java.io.File;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import org.anarres.ipmi.protocol.client.session.IpmiPacketContext;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiPacketReader;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiRequest;
import org.anarres.ipmi.protocol.packet.ipmi.payload.AbstractTaggedIpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpPacket;
import org.junit.Test;
import org.pcap4j.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.junit.Assert.*;

/**
 *
 * @author shevek
 */
public class IpmiPayloadReceiveDispatcherTest {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiPayloadReceiveDispatcherTest.class);

    private static class Receiver implements IpmiReceiver {

        private final Object key;
        private final Object request;
        private boolean received = false;

        public Receiver(Object key, Object request) {
            this.key = key;
            this.request = request;
        }

        @Override
        public void receive(IpmiHandlerContext context, IpmiSession session, IpmiPayload response) {
            LOG.info("Received for " + key + ":\n" + request + "\n" + response);
            // Make this single-shot.
            assertFalse(received);
            received = true;
        }

        @Override
        public void timeout(IpmiReceiverKey key) {
            throw new UnsupportedOperationException();
        }
    }

    @Test
    public void testDispatcher() throws Exception {
        assertNull(null);
        IpmiPacketContext context = new IpmiPacketContext() {
            @Override
            public IpmiSession getIpmiSession(int sessionId) {
                return null;
            }
        };
        final AtomicInteger hitCount = new AtomicInteger(0);
        final AtomicInteger missCount = new AtomicInteger(0);
        IpmiPayloadReceiveDispatcher dispatcher = new IpmiPayloadReceiveDispatcher(context) {

            @Override
            protected void handleDiscard(IpmiHandlerContext context, Object message) {
                super.handleDiscard(context, message);
                fail("Discarded " + message);
            }

            @Override
            public IpmiReceiver getReceiver(IpmiHandlerContext context, Class<? extends IpmiPayload> payloadType, byte messageId) {
                IpmiReceiver receiver = super.getReceiver(context, payloadType, messageId);
                if (receiver == null)
                    missCount.getAndIncrement();
                else
                    hitCount.getAndIncrement();
                return receiver;
            }
        };

        File file = new File("../src/misc/ipmi-lanplus-enc0.tcpdump");
        final AtomicBoolean outgoing = new AtomicBoolean(false);
        try (IpmiPacketReader reader = new IpmiPacketReader(context, file) {

            @Override
            protected void processUdpAddress(InetSocketAddress source, InetSocketAddress target) {
                if (source.getPort() == RmcpPacket.PORT)
                    outgoing.set(false);
                else if (target.getPort() == RmcpPacket.PORT)
                    outgoing.set(true);
                super.processUdpAddress(source, target);
            }

        }) {
            while (reader.hasNext()) {
                RmcpPacket packet = reader.next();
                IpmiHandlerContext handlerContext = new IpmiHandlerContext(null, packet.getRemoteAddress());

                if (outgoing.get()) {
                    // From us to the BMC.

                    COMMAND:
                    {
                        final IpmiRequest request = packet.getEncapsulated(IpmiRequest.class);
                        if (request != null) {
                            final IpmiReceiverKey key = new IpmiReceiverKey(packet.getRemoteAddress(), request.getResponseType(), request.getSequenceNumber());
                            dispatcher.setReceiver(key, new Receiver(key, request));
                            continue;
                        }
                    }

                    SESSION:
                    {
                        final AbstractTaggedIpmiPayload payload = packet.getEncapsulated(AbstractTaggedIpmiPayload.class);
                        if (payload != null) {
                            final IpmiReceiverKey key = new IpmiReceiverKey(packet.getRemoteAddress(), payload.getResponseType(), payload.getMessageTag());
                            dispatcher.setReceiver(key, new Receiver(key, payload));
                            continue;
                        }
                    }

                    throw new UnsupportedOperationException("Why did we send:\n" + packet);
                } else {
                    // From the BMC to us.
                    dispatcher.dispatch(handlerContext, packet);
                }
            }
        }

        LOG.info("Hit " + hitCount + "; missed " + missCount);
        assertEquals(0, missCount.get());
    }

}
