/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client.visitor;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.asf.AsfRmcpData;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionWrapper;
import org.anarres.ipmi.protocol.packet.rmcp.OEMRmcpMessage;
import org.anarres.ipmi.protocol.packet.rmcp.RmcpData;

/**
 *
 * @author shevek
 */
public interface IpmiClientRmcpMessageHandler {

    public static class Adapter implements IpmiClientRmcpMessageHandler {

        public void handleDefault(@Nonnull IpmiHandlerContext context, @Nonnull RmcpData message) {
        }

        @Override
        public void handleAsfRmcpData(IpmiHandlerContext context, AsfRmcpData message) {
            handleDefault(context, message);
        }

        @Override
        public void handleIpmiRmcpData(IpmiHandlerContext context, IpmiSessionWrapper message) {
            handleDefault(context, message);
        }

        @Override
        public void handleOemRmcpData(IpmiHandlerContext context, OEMRmcpMessage message) {
            handleDefault(context, message);
        }
    }

    public void handleAsfRmcpData(@Nonnull IpmiHandlerContext context, @Nonnull AsfRmcpData message);

    public void handleIpmiRmcpData(@Nonnull IpmiHandlerContext context, @Nonnull IpmiSessionWrapper message);

    public void handleOemRmcpData(@Nonnull IpmiHandlerContext context, @Nonnull OEMRmcpMessage message);
}
