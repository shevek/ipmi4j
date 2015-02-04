/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.impl;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientAsfMessageHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientIpmiPayloadHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiClientRmcpMessageHandler;
import org.anarres.ipmi.protocol.client.visitor.IpmiHandlerContext;
import org.anarres.ipmi.protocol.packet.asf.AsfRmcpData;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionWrapper;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpData;

/**
 *
 * @author shevek
 */
public class RmcpMessageHandler extends IpmiClientRmcpMessageHandler.Adapter {

    private final IpmiClientAsfMessageHandler asfHandler;
    private final IpmiClientIpmiPayloadHandler ipmiHandler;

    public RmcpMessageHandler(@Nonnull IpmiClientAsfMessageHandler asfHandler, @Nonnull IpmiClientIpmiPayloadHandler ipmiHandler) {
        this.asfHandler = asfHandler;
        this.ipmiHandler = ipmiHandler;
    }

    @Override
    public void handleDefault(IpmiHandlerContext context, RmcpData message) {
        throw new UnsupportedOperationException("Cannot handle " + message.getClass());
    }

    @Override
    public void handleAsfRmcpData(IpmiHandlerContext context, AsfRmcpData message) {
        message.apply(asfHandler, context);
    }

    @Override
    public void handleIpmiRmcpData(IpmiHandlerContext context, IpmiSessionWrapper message) {
        message.apply(ipmiHandler, context);
    }
}
