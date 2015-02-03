/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.anarres.ipmi.protocol.client;

import javax.annotation.Nonnull;
import org.anarres.ipmi.protocol.packet.asf.AsfRmcpData;
import org.anarres.ipmi.protocol.packet.ipmi.IpmiSessionWrapper;

/**
 *
 * @author shevek
 */
public interface IpmiClientRmcpMessageHandler {

    public static class Default implements IpmiClientRmcpMessageHandler {

        private final IpmiClientAsfMessageHandler asfHandler;
        private final IpmiClientPayloadHandler ipmiHandler;

        public Default(@Nonnull IpmiClientAsfMessageHandler asfHandler, @Nonnull IpmiClientPayloadHandler ipmiHandler) {
            this.asfHandler = asfHandler;
            this.ipmiHandler = ipmiHandler;
        }

        @Override
        public void handleAsfRmcpData(AsfRmcpData message) {
            message.apply(asfHandler);
        }

        @Override
        public void handleIpmiRmcpData(IpmiSessionWrapper message) {
            message.apply(ipmiHandler);
        }
    }

    public void handleAsfRmcpData(@Nonnull AsfRmcpData message);

    public void handleIpmiRmcpData(@Nonnull IpmiSessionWrapper message);
}
