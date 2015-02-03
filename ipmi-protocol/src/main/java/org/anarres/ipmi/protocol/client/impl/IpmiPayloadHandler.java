/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.impl;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.IpmiClient;
import org.anarres.ipmi.protocol.client.IpmiClientResponseHandler;
import org.anarres.ipmi.protocol.client.session.IpmiSession;
import org.anarres.ipmi.protocol.client.session.IpmiSessionManager;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiCommandHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.packet.ipmi.command.IpmiCommand;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAuthenticationCapabilitiesRequest;
import org.anarres.ipmi.protocol.packet.ipmi.command.messaging.GetChannelAuthenticationCapabilitiesResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiOpenSessionResponse;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiPayload;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage2;
import org.anarres.ipmi.protocol.packet.ipmi.payload.IpmiRAKPMessage4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author shevek
 */
public class IpmiPayloadHandler extends IpmiClientIpmiPayloadHandler.Adapter {

    private static final Logger LOG = LoggerFactory.getLogger(IpmiPayloadHandler.class);
    private final IpmiClient client;
    private final IpmiClientIpmiCommandHandler commandHandler = new IpmiClientIpmiCommandHandler.Adapter() {
        // @Override
        // public void handleGetChannelAuthenticationCapabilitiesResponse(IpmiSession session, GetChannelAuthenticationCapabilitiesResponse response) {
            // super.handleGetChannelAuthenticationCapabilitiesResponse(session, response);
        // }
    };

    public IpmiPayloadHandler(@Nonnull IpmiClient client) {
        this.client = client;
    }

    @Nonnull
    private IpmiSessionManager getSessionManager() {
        return client.getSessionManager();
    }

    public void sendInit() {
        IpmiSession session = getSessionManager().newIpmiSession();
        client.send(null, new GetChannelAuthenticationCapabilitiesRequest(), IpmiClientResponseHandler.IGNORE);
    }

    @Override
    public void handleDefault(IpmiPayload payload) {
        LOG.info("Ignored " + payload);
    }

    @Override
    public void handleOpenSessionResponse(IpmiOpenSessionResponse response) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleRAKPMessage2(IpmiRAKPMessage2 message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleRAKPMessage4(IpmiRAKPMessage4 message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleCommand(IpmiCommand message) {
    }
}