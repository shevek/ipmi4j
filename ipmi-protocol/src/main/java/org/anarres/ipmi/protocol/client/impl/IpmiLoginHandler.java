/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.impl;

import javax.annotation.Nonnull;
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
            // -> Make global.
            IpmiSession session = context.getClient().getSessionManager().newIpmiSession();
            context.send(session, new CloseSessionRequest(session));
        }

        // 3
        @Override
        public void handleCloseSessionResponse(IpmiHandlerContext context, CloseSessionResponse response) {
            IpmiSession session = null;
            context.send(null, new IpmiOpenSessionRequest(session, RequestedMaximumPrivilegeLevel.ADMINISTRATOR));
        }
    };
    private final IpmiClientIpmiPayloadHandler payloadHandler = new IpmiClientIpmiPayloadHandler.Adapter() {

        // 4
        @Override
        public void handleOpenSessionResponse(IpmiHandlerContext context, IpmiOpenSessionResponse response) {
            IpmiSession session = context.getSessionManager().getIpmiSession(response.consoleSessionId);
            session.setSystemSessionId(response.systemSessionId);
            context.send(session, new IpmiRAKPMessage1(session));
        }

        // 5
        @Override
        public void handleRAKPMessage2(IpmiHandlerContext context, IpmiRAKPMessage2 message) {
            IpmiSession session = context.getSessionManager().getIpmiSession(message.consoleSessionId);
            context.send(session, new IpmiRAKPMessage3());
        }

        // 6
        @Override
        public void handleRAKPMessage4(IpmiHandlerContext context, IpmiRAKPMessage4 message) {
            IpmiSession session = context.getSessionManager().getIpmiSession(message.consoleSessionId);
        }

        @Override
        public void handleCommand(IpmiHandlerContext context, IpmiCommand message) {
            message.apply(commandHandler, context);
        }
    };
    private final IpmiPayloadDispatcher.IpmiReceiver<IpmiPayload> receiver = new IpmiPayloadDispatcher.IpmiReceiver<IpmiPayload>() {

        @Override
        public void receive(IpmiHandlerContext context, IpmiPayload response) {
            response.apply(payloadHandler, context);
        }

        @Override
        public void timeout(IpmiPayloadDispatcher.IpmiReceiverKey<IpmiPayload> key) {
            LOG.warn("Timeout: " + key);
        }
    };

    // 1
    public void open(@Nonnull IpmiHandlerContext context) {
        context.send(null, new GetChannelAuthenticationCapabilitiesRequest());
    }
}
