/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.impl;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.dispatch.IpmiReceiver;
import org.anarres.ipmi.protocol.client.dispatch.IpmiReceiverKey;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.CloseSessionRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.CloseSessionResponse;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAuthenticationCapabilitiesRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAuthenticationCapabilitiesResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionRequest;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage1;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage2;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage3;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage4;
import org.anarres.ipmi.protocol.packet.ipmi.payload.RequestedMaximumPrivilegeLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class IpmiLoginHandler {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiLoginHandler.class);
    private final IpmiClientIpmiCommandHandler commandHandler = new IpmiClientIpmiCommandHandler.Adapter() {
        @Override
        public void handleDefault(IpmiHandlerContext context, IpmiCommand command) {
            LOG.info("Ignored " + command);
        }

        // 2
        @Override
        public void handleGetChannelAuthenticationCapabilitiesResponse(IpmiHandlerContext context, GetChannelAuthenticationCapabilitiesResponse response) {
            IpmiSession session = getSession(0, 0);
            context.send(session, new CloseSessionRequest(session),
                    CloseSessionResponse.class, receiver);
        }

        // 3
        @Override
        public void handleCloseSessionResponse(IpmiHandlerContext context, CloseSessionResponse response) {
            IpmiSession session = getSession(0, 0);
            context.send(null, new IpmiOpenSessionRequest(session, RequestedMaximumPrivilegeLevel.ADMINISTRATOR),
                    IpmiOpenSessionResponse.class, receiver);
        }
    };
    private final IpmiClientIpmiPayloadHandler payloadHandler = new IpmiClientIpmiPayloadHandler.Adapter() {

        @Override
        protected void handleDefault(IpmiHandlerContext context, IpmiSession session, IpmiPayload payload) {
            LOG.info("Ignored " + payload);
        }

        // 4
        @Override
        public void handleOpenSessionResponse(IpmiHandlerContext context, IpmiSession _session, IpmiOpenSessionResponse response) {
            IpmiSession session = getSession(response.consoleSessionId, 0);
            session.setSystemSessionId(response.systemSessionId);
            context.send(session, new IpmiRAKPMessage1(session), IpmiRAKPMessage2.class, receiver);
        }

        // 5
        @Override
        public void handleRAKPMessage2(IpmiHandlerContext context, IpmiSession _session, IpmiRAKPMessage2 message) {
            IpmiSession session = getSession(message.consoleSessionId, 0);
            context.send(session, new IpmiRAKPMessage3(), IpmiRAKPMessage4.class, receiver);
        }

        // 6
        @Override
        public void handleRAKPMessage4(IpmiHandlerContext context, IpmiSession _session, IpmiRAKPMessage4 message) {
            IpmiSession session = getSession(message.consoleSessionId, 0);
            // Now your session is open.
        }

        @Override
        public void handleCommand(IpmiHandlerContext context, IpmiSession session, IpmiCommand message) {
            message.apply(commandHandler, context);
        }
    };
    private final IpmiReceiver receiver = new IpmiReceiver() {

        @Override
        public void receive(IpmiHandlerContext context, IpmiSession session, IpmiPayload response) {
            LOG.warn("Receive: " + response);
            response.apply(payloadHandler, context, session);
        }

        @Override
        public void timeout(IpmiReceiverKey key) {
            LOG.warn("Timeout: " + key);
        }
    };
    private final IpmiSession session;

    public IpmiLoginHandler(@Nonnull IpmiSession session) {
        this.session = session;
    }

    @Nonnull
    private IpmiSession getSession(int consoleSessionId, int systemSessionId) {
        if (consoleSessionId != 0)
            if (consoleSessionId != session.getConsoleSessionId())
                throw new IllegalArgumentException("Bad consoleSessionId " + Integer.toHexString(consoleSessionId) + "; expected " + Integer.toHexString(session.getConsoleSessionId()));
        if (systemSessionId != 0)
            if (systemSessionId != session.getSystemSessionId())
                throw new IllegalArgumentException("Bad systemSessionId " + Integer.toHexString(systemSessionId) + "; expected " + Integer.toHexString(session.getSystemSessionId()));
        return session;
    }

    // 1
    public void open(@Nonnull IpmiHandlerContext context) {
        context.send(null, new GetChannelAuthenticationCapabilitiesRequest(),
                GetChannelAuthenticationCapabilitiesResponse.class, receiver);
    }
}
